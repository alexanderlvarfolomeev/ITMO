(deftype Var [variable])
(deftype ConstOp [value])
(deftype DivideOp [arguments])
(deftype UnaryOperation [name operation argument dif])
(deftype MultipleArgumentsOperation [name operation arguments dif])

(declare Variable Constant Negate Square Sqrt Sinh Cosh Add Subtract Multiply Divide)

(defmulti evaluate (fn [exp _] (class exp)))
(defmulti toString class)
(defmulti diff (fn [exp _] (class exp)))

(defmethod toString Var [exp]
  (.-variable exp))
(defmethod evaluate Var [exp vars]
  (vars (.-variable exp)))
(defmethod diff Var [exp var]
  (if (= var (.-variable exp))
    (Constant 1)
    (Constant 0)))

(defmethod toString ConstOp [exp]
  (str (format "%.0f.0" (.-value exp))))
(defmethod evaluate ConstOp [exp _]
  (.-value exp))
(defmethod diff ConstOp [_ _] (Constant 0))

(defmethod toString UnaryOperation [exp]
  (str "(" (.-name exp) " " (toString (.-argument exp)) ")"))
(defmethod evaluate UnaryOperation [exp vars]
  ((.-operation exp) (evaluate (.-argument exp) vars)))
(defmethod diff UnaryOperation [exp var]
  ((.-dif exp) (.-argument exp) var))

(defmethod toString DivideOp [exp] (str "(/" (reduce #(str %1 " " (toString %2)) "" (.-arguments exp)) ")"))
(defmethod evaluate DivideOp [exp vars]
  (reduce #(/ (double %1) (double %2))
          (map #(evaluate % vars) (.-arguments exp))))
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

(defmethod toString MultipleArgumentsOperation [exp]
  (str "(" (.-name exp) (reduce #(str %1 " " (toString %2)) "" (.-arguments exp)) ")"))
(defmethod evaluate MultipleArgumentsOperation [exp vars]
  (case (count (.-arguments exp))
    0 ((.-operation exp))
    1 ((.-operation exp) (evaluate (first (.-arguments exp)) vars))
    (reduce (.-operation exp) (map #(evaluate % vars) (.-arguments exp)))))
(defmethod diff MultipleArgumentsOperation [exp var]
  ((.-dif exp) (.-arguments exp) var))

(defn Variable [var] (Var. var))
(defn Constant [value] (ConstOp. value))
(defn Negate [argument]
  (UnaryOperation. "negate" - argument (fn [arg var]
                                         (Negate (diff arg var)))))
(defn Square [argument]
  (UnaryOperation. "square" #(* % %) argument (fn [arg var]
                                                (Multiply (Constant 2) arg (diff arg var)))))
(defn Sqrt [argument]
  (UnaryOperation. "sqrt" #(Math/sqrt (Math/abs %)) argument (fn [arg var]
                                                               (Divide
                                                                 (Multiply arg (diff arg var))
                                                                 (Constant 2)
                                                                 (Sqrt arg)
                                                                 (Square (Sqrt arg))))))
(defn Sinh [argument]
  (UnaryOperation. "sinh" #(Math/sinh %) argument (fn [arg var]
                                                    (Multiply
                                                      (diff arg var)
                                                      (Cosh arg)))))
(defn Cosh [argument]
  (UnaryOperation. "cosh" #(Math/cosh %) argument (fn [arg var]
                                                    (Multiply
                                                      (diff arg var)
                                                      (Sinh arg)))))
(defn Divide [& arguments] (DivideOp. arguments))

(defn Add [& arguments]
  (MultipleArgumentsOperation. "+" + arguments (fn [args var]
                                                 (apply Add
                                                        (map #(diff % var) args)))))
(defn Subtract [& arguments]
  (MultipleArgumentsOperation. "-" - arguments (fn [args var]
                                                 (apply Subtract
                                                        (map #(diff % var) args)))))
(defn Multiply [& arguments]
  (MultipleArgumentsOperation. "*" * arguments (fn [args var]
                                                 (Add (apply Multiply
                                                             (diff (first args)
                                                                   var)
                                                             (rest args))
                                                      (if (== (count args) 1)
                                                        (Constant 0)
                                                        (Multiply (first args)
                                                                  (diff (apply Multiply
                                                                               (rest args))
                                                                        var)))))))

(def operations {'+      Add, '- Subtract, '* Multiply, '/ Divide,
                 'negate Negate, 'square Square, 'sqrt Sqrt, 'sinh Sinh, 'cosh Cosh})

(defn parseExpression [expr] (cond
                               (number? expr) (Constant expr)
                               (symbol? expr) (Variable (str expr))
                               :else (apply (operations (first expr)) (map parseExpression (rest expr)))))

(def parseObject (comp parseExpression read-string))


(def argsCount {"+" 2, "-" 2, "*" 2, "/" 2, "negate" 1, "square" 1, "sqrt" 1, "sinh" 1, "cosh" 1})
(def operationRank {"+" 1, "-" 1, "*" 2, "/" 2, "negate" 3, "square" 3, "sqrt" 3, "sinh" 3, "cosh" 3})

(defn fold [stack rank]
  (if (or (empty? (second stack)) (< (operationRank (last (second stack))) rank))
    stack
    (fold [(vec (conj (vec (drop-last (argsCount (last (second stack)))
                                      (first stack)))
                      (apply (operations (symbol (last (second stack)))) (take-last (argsCount (last (second stack)))
                                                                                    (first stack)))))
           (vec (drop-last 1 (second stack)))] rank)))

(defn parseInfix [stack token]
  (cond
    (or (= token "x") (= token "y") (= token "z")) [(vec (conj (first stack) (Variable token))) (second stack)]
    (not= (operationRank token) nil) [(first (fold stack (operationRank token))) (vec (conj
                                                                                        (second
                                                                                          (fold stack
                                                                                                (operationRank token)))
                                                                                        token))]
    :else [(vec (conj (first stack) (Constant (Double/parseDouble token)))) (second stack)]))

(defn parseObjectInfix [expr]
  (def tokens (vec (.split expr " ")))
  (first (first (fold (reduce parseInfix [[] []] tokens) 0))))