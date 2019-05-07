(deftype Var [variable])
(deftype ConstOp [value])
(deftype DivideOp [arguments])
(deftype UnaryOperation [name operation argument dif])
(deftype MultipleArgumentsOperation [name operation arguments dif])

(declare Variable Constant Negate Square Sqrt Sinh Cosh Add Subtract Multiply Divide)

(defmulti toString class)
(defmulti toStringSuffix class)
(defmulti toStringInfix class)
(defmulti evaluate (fn [exp _] (class exp)))
(defmulti diff (fn [exp _] (class exp)))

(defmethod toString Var [exp]
  (.-variable exp))
(defmethod toStringSuffix Var [exp]
  (.-variable exp))
(defmethod toStringInfix Var [exp]
  (.-variable exp))
(defmethod evaluate Var [exp vars]
  ((comp vars clojure.string/lower-case str) (nth (.-variable exp) 0)))
(defmethod diff Var [exp var]
  (if (= var ((comp clojure.string/lower-case str) (nth (.-variable exp) 0)))
    (Constant 1)
    (Constant 0)))

(defmethod toString ConstOp [exp]
  (str (format "%.0f.0" (.-value exp))))
(defmethod toStringSuffix ConstOp [exp]
  (str (format "%.0f.0" (.-value exp))))
(defmethod toStringInfix ConstOp [exp]
  (str (format "%.0f.0" (.-value exp))))
(defmethod evaluate ConstOp [exp _]
  (.-value exp))
(defmethod diff ConstOp [_ _] (Constant 0))

(defmethod toString UnaryOperation [exp]
  (str "(" (.-name exp) " " (toString (.-argument exp)) ")"))
(defmethod toStringSuffix UnaryOperation [exp]
  (str "(" (toStringSuffix (.-argument exp)) " " (.-name exp) ")"))
(defmethod toStringInfix UnaryOperation [exp]
  (str (.-name exp) "(" (toStringInfix (.-argument exp)) ")"))
(defmethod evaluate UnaryOperation [exp vars]
  ((.-operation exp) (evaluate (.-argument exp) vars)))
(defmethod diff UnaryOperation [exp var]
  ((.-dif exp) (.-argument exp) var))

(defmethod toString DivideOp [exp] (str "(/" (reduce #(str %1 " " (toString %2)) "" (.-arguments exp)) ")"))
(defmethod toStringSuffix DivideOp [exp] (str "(" (reduce #(str %1 (toStringSuffix %2) " ") "" (.-arguments exp)) "/)"))
(defmethod toStringInfix DivideOp [exp] (str "(" (reduce #(str %1 " / " (toStringInfix %2))
                                                         (toStringInfix (first (.-arguments exp)))
                                                         (rest (.-arguments exp))) ")"))
(defmethod evaluate DivideOp [exp vars]
  (reduce #(/ (double %1) (double %2))
          (map #(evaluate % vars) (.-arguments exp))))
(defmethod diff DivideOp [exp var]
  (let [d (rest (.-arguments exp))]
    (Divide
      (Subtract
        (Multiply (diff (first (.-arguments exp))
                        var)
                  (apply Multiply d))
        (Multiply (first (.-arguments exp))
                  (diff (apply Multiply d)
                        var)))
      (apply Multiply (into d d)))))

(defmethod toString MultipleArgumentsOperation [exp]
  (str "(" (.-name exp) (reduce #(str %1 " " (toString %2)) "" (.-arguments exp)) ")"))
(defmethod toStringSuffix MultipleArgumentsOperation [exp]
  (str "(" (reduce #(str %1 (toStringSuffix %2) " ") "" (.-arguments exp)) (.-name exp) ")"))
(defmethod toStringInfix MultipleArgumentsOperation [exp]
  (str "(" (reduce #(str %1 " " (.-name exp) " " (toStringInfix %2)) (toStringInfix (first (.-arguments exp)))
                   (rest (.-arguments exp))) ")"))
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
  (UnaryOperation. "sqrt" (comp numeric_tower/sqrt numeric_tower/abs) argument (fn [arg var]
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
(defn And [& arguments]
  (MultipleArgumentsOperation.
    "&"
    #(Double/longBitsToDouble (bit-and (Double/doubleToLongBits %1) (Double/doubleToLongBits %2)))
    arguments #()))
(defn Or [& arguments]
  (MultipleArgumentsOperation.
    "|"
    #(Double/longBitsToDouble (bit-or (Double/doubleToLongBits %1) (Double/doubleToLongBits %2)))
    arguments #()))
(defn Xor [& arguments]
  (MultipleArgumentsOperation.
    "^"
    #(Double/longBitsToDouble (bit-xor (Double/doubleToLongBits %1) (Double/doubleToLongBits %2)))
    arguments #()))

(defn Pow [& arguments]
  (MultipleArgumentsOperation.
    "**"
    #(Math/pow %1 %2)
    arguments #()))
(defn Log [& arguments]
  (MultipleArgumentsOperation.
    "//"
    #(/ (Math/log (numeric_tower/abs %2)) (Math/log (numeric_tower/abs %1)))
    arguments #()))
