(deftype Var [variable])
(deftype ConstOp [value])
(deftype NegateOp [argument])
(deftype SquareOp [argument])
(deftype SquareRootOp [argument])
(deftype DivideOp [arguments])
(deftype MultipleArgumentsOperation [name operation arguments dif])

(defmulti evaluate (fn [exp _] (class exp)))
(defmulti toString class)
(defmulti diff (fn [exp _] (class exp)))

(defmethod toString Var [exp]
  (.-variable exp))
(defmethod toString ConstOp [exp]
  (str (format "%.0f.0" (.-value exp))))
(defmethod toString NegateOp [exp]
  (str "(negate " (toString (.-argument exp)) ")"))
(defmethod toString SquareOp [exp]
  (str "(square " (toString (.-argument exp)) ")"))
(defmethod toString SquareRootOp [exp]
  (str "(sqrt " (toString (.-argument exp)) ")"))
(defmethod toString DivideOp [exp] (str "(/" (reduce #(str %1 " " (toString %2)) "" (.-arguments exp)) ")"))
(defmethod toString MultipleArgumentsOperation [exp]
  (str "(" (.-name exp) (reduce #(str %1 " " (toString %2)) "" (.-arguments exp)) ")"))

(defmethod evaluate Var [exp vars]
  (vars (.-variable exp)))
(defmethod evaluate ConstOp [exp _]
  (.-value exp))
(defmethod evaluate NegateOp [exp vars]
  (- (evaluate (.-argument exp) vars)))
(defmethod evaluate SquareOp [exp vars]
  (#(* % %) (evaluate (.-argument exp) vars)))
(defmethod evaluate SquareRootOp [exp vars]
  (Math/sqrt (Math/abs (evaluate (.-argument exp) vars))))
(defmethod evaluate DivideOp [exp vars]
  (reduce #(/ (double %1) (double %2))
          (map #(evaluate % vars) (.-arguments exp))))
(defmethod evaluate MultipleArgumentsOperation [exp vars]
  (case (count (.-arguments exp))
    0 ((.-operation exp))
    1 ((.-operation exp) (evaluate (first (.-arguments exp)) vars))
    (reduce (.-operation exp) (map #(evaluate % vars) (.-arguments exp)))))

(defn Variable [var] (Var. var))
(defn Constant [value] (ConstOp. value))
(defn Negate [argument] (NegateOp. argument))
(defn Square [argument] (SquareOp. argument))
(defn Sqrt [argument] (SquareRootOp. argument))
(defn Divide [& arguments] (DivideOp. arguments))
(defn Add [& arguments] (MultipleArgumentsOperation. "+" + arguments (fn [args var]
                                                                       (apply Add
                                                                              (map #(diff % var) args)))))
(defn Subtract [& arguments] (MultipleArgumentsOperation. "-" - arguments (fn [args var]
                                                                            (apply Subtract
                                                                                   (map #(diff % var) args)))))
(defn Multiply [& arguments] (MultipleArgumentsOperation. "*" * arguments (fn [args var]
                                                                            (Add
                                                                              (apply Multiply
                                                                                     (diff (first args)
                                                                                           var)
                                                                                     (rest args))
                                                                              (if (== (count args) 1)
                                                                                (Constant 0)
                                                                                (Multiply (first args)
                                                                                          (diff (apply Multiply
                                                                                                       (rest args))
                                                                                                var)))))))

(defmethod diff ConstOp [_ _] (Constant 0))
(defmethod diff Var [exp var]
  (if (= var (.-variable exp))
    (Constant 1)
    (Constant 0)))
(defmethod diff NegateOp [exp var]
  (Negate (diff (.-argument exp) var)))
(defmethod diff SquareOp [exp var]
  (Multiply (Constant 2) (.-argument exp) (diff (.-argument exp) var)))
(defmethod diff SquareRootOp [exp var]
  (Divide (Multiply (.-argument exp) (diff (.-argument exp) var)) (Constant 2) exp (Square exp)))
(defmethod diff DivideOp [exp var]
  (def d (rest (.-arguments exp)))
  (Divide
    (Subtract
      (Multiply (diff (first (.-arguments exp))
                      var)
                (apply Multiply d))
      (Multiply (first (.-arguments exp))
                (diff (apply Multiply d)
                      var)))
    (apply Multiply (into d d))))
(defmethod diff MultipleArgumentsOperation [exp var]
  ((.-dif exp) (.-arguments exp) var))

(def operations {'+ Add, '- Subtract, '* Multiply, '/ Divide, 'negate Negate, 'square Square, 'sqrt Sqrt})
(def argsCount {"+" 2, "-" 2, "*" 2, "/" 2, "negate" 1, "square" 1, "sqrt" 1})
(def operationRank {"+" 1, "-" 1, "*" 2, "/" 2, "negate" 3, "square" 3, "sqrt" 3})

(defn parseExpression [expr] (cond
                               (number? expr) (Constant expr)
                               (symbol? expr) (Variable (str expr))
                               :else (apply (operations (first expr)) (map parseExpression (rest expr)))))

(def parseObject (comp parseExpression read-string))

(defn fold [stack rank]
  (if (or (empty? (second stack)) (< (operationRank (last (second stack))) rank))
    stack
    (fold [(vec (conj (vec (drop-last (argsCount (last (second stack)))
                                 (first stack)))
                      (apply (operations (symbol (last (second stack)))) (take-last (argsCount (last (second stack))) (first stack)))))
           (vec (drop-last 1 (second stack)))] rank)))

(defn parseInfix [stack token]
  (cond
    (or (= token "x") (= token "y") (= token "z")) [(vec (conj (first stack) (Variable token))) (second stack)]
    (not= (operationRank token) nil) [(first (fold stack (operationRank token))) (vec (conj (second (fold stack (operationRank token))) token))]
    :else [(vec (conj (first stack) (Constant (Double/parseDouble token)))) (second stack)]))

(defn parseObjectInfix [expr]
  (def tokens (vec (.split expr " ")))
  (first (first (fold (reduce parseInfix [[] []] tokens) 0))))