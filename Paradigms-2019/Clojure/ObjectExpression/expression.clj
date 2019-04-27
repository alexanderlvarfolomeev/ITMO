(deftype Var [variable])
(deftype ConstOp [value])
(deftype NegateOp [argument])
(deftype DivideOp [arguments])
(deftype MultipleArgumentsOperation [name operation arguments dif])

(defmulti evaluate (fn [exp var] [(class exp) (class var)]))
(defmulti toString class)
(defmulti diff (fn [exp var] [(class exp) (class var)]))

(defmethod toString Var [exp]
  (.-variable exp))
(defmethod toString ConstOp [exp]
  (str (format "%.0f.0" (.-value exp))))
(defmethod toString NegateOp [exp]
  (str "(negate " (toString (.-argument exp)) ")"))
(defmethod toString DivideOp [exp] (str "(/" (reduce #(str %1 " " (toString %2)) "" (.-arguments exp)) ")"))
(defmethod toString MultipleArgumentsOperation [exp]
  (str "(" (.-name exp) (reduce #(str %1 " " (toString %2)) "" (.-arguments exp)) ")"))

(defmethod evaluate [Var Object] [exp vars]
  (vars (.-variable exp)))
(defmethod evaluate [ConstOp Object] [exp _]
  (.-value exp))
(defmethod evaluate [NegateOp Object] [exp vars]
  (- (evaluate (.-argument exp) vars)))
(defmethod evaluate [DivideOp Object] [exp vars]
  (reduce #(/ (double %1) (double %2))
          (map #(evaluate % vars) (.-arguments exp))))
(defmethod evaluate [MultipleArgumentsOperation Object] [exp vars]
  (case (count (.-arguments exp))
    0 ((.-operation exp))
    1 ((.-operation exp) (evaluate (first (.-arguments exp)) vars))
    (reduce (.-operation exp) (map #(evaluate % vars) (.-arguments exp)))))

(defn Variable [var] (Var. var))
(defn Constant [value] (ConstOp. value))
(defn Negate [argument] (NegateOp. argument))
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

(defmethod diff [ConstOp Object] [_ _] (Constant 0))
(defmethod diff [Var Object] [exp var]
  (if (= var (.-variable exp))
    (Constant 1)
    (Constant 0)))
(defmethod diff [NegateOp Object] [exp var]
  (Negate (diff (.-argument exp) var)))
(defmethod diff [DivideOp Object] [exp var]
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
(defmethod diff [MultipleArgumentsOperation Object] [exp var]
  ((.-dif exp) (.-arguments exp) var))

(def operations {'+ Add, '- Subtract, '* Multiply, '/ Divide, 'negate Negate})

(defn parseExpression [expr] (cond
                               (number? expr) (Constant expr)
                               (symbol? expr) (Variable (str expr))
                               :else (apply (operations (first expr)) (map parseExpression (rest expr)))))

(def parseObject (comp parseExpression read-string))

(def parseObjectInfix (comp parseExpression read-string))

