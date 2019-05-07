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
(defmethod toStringInfix DivideOp [exp] (str "(" (reduce #(str %1 " / " (toStringInfix %2)) (toStringInfix (first (.-arguments exp))) (rest (.-arguments exp))) ")"))
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
  (str "(" (reduce #(str %1 " " (.-name exp) " " (toStringInfix %2)) (toStringInfix (first (.-arguments exp))) (rest (.-arguments exp))) ")"))
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
    #(/ (Math/log (Math/abs %2)) (Math/log (Math/abs %1)))
    arguments #()))

(def operations {'+      Add, '- Subtract, '* Multiply, '/ Divide,
                 'negate Negate, 'square Square, 'sqrt Sqrt, 'sinh Sinh, 'cosh Cosh
                 '&      And, '| Or, (symbol "^") Xor, '** Pow, (symbol "//") Log})

(defn parseExpression [expr] (cond
                               (number? expr) (Constant expr)
                               (symbol? expr) (Variable (str expr))
                               :else (apply (operations (first expr)) (map parseExpression (rest expr)))))

(def parseObject (comp parseExpression read-string))

(defn -return [value tail] {:value value :tail tail})
(def -valid? boolean)
(def -value :value)
(def -tail :tail)

(defn _show [result]
  (if (-valid? result) (str "-> " (pr-str (-value result)) " | " (pr-str (apply str (-tail result))))
                       "!"))
(defn tabulate [parser inputs]
  (println)
  (run! (fn [input] (printf "    %-10s %s\n" input (_show (parser input)))) inputs))

(defn _empty [value] (partial -return value))
(defn _char [p]
  (fn [[c & cs]]
    (if (and c (p c)) (-return c cs))))
(defn _map [f]
  (fn [result]
    (if (-valid? result)
      (-return (f (-value result)) (-tail result)))))
(defn _combine [f a b]
  (fn [str]
    (let [ar ((force a) str)]
      (if (-valid? ar)
        ((_map (partial f (-value ar)))
          ((force b) (-tail ar)))))))
(defn _either [a b]
  (fn [str]
    (let [ar ((force a) str)]
      (if (-valid? ar) ar ((force b) str)))))
(defn _parser [p]
  (fn [input]
    (-value ((_combine (fn [v _] v) p (_char #{\u0000})) (str input \u0000)))))

(defn +char [chars] (_char (set chars)))
(defn +char-not [chars] (_char (comp not (set chars))))
(defn +map [f parser] (comp (_map f) parser))
(def +parser _parser)
(def +ignore (partial +map (constantly 'ignore)))

(defn iconj [coll value]
  (if (= value 'ignore) coll (conj coll value)))
(defn +seq [& ps]
  (reduce (partial _combine iconj) (_empty []) ps))
(defn +seqf [f & ps] (+map (partial apply f) (apply +seq ps)))
(defn +seqn [n & ps] (apply +seqf (fn [& vs] (nth vs n)) ps))

(defn +or [p & ps]
  (reduce (partial _either) p ps))
(defn +opt [p]
  (+or p (_empty nil)))
(defn +star [p]
  (letfn [(rec [] (+or (+seqf cons p (delay (rec))) (_empty ())))] (rec)))
(defn +plus [p] (+seqf cons p (+star p)))
(defn +str [p] (+map (partial apply str) p))

(declare *value)
(def *all-chars (mapv char (range 1 128)))
(def *digit (+char (apply str (filter #(Character/isDigit %) *all-chars))))
(def *number (+map read-string (+str (+seqf #(into (vec (cons %1 %2)) (vec (cons %3 %4))) (+opt (+char "+-")) (+plus *digit) (+opt (+char ".")) (+opt (+plus *digit))))))
(def *spaces (apply str (filter #(or (Character/isWhitespace %) (= \, %)) *all-chars)))
(def *space (+char *spaces))
(def *symbol (+map symbol (+str (+seqf cons (+char-not (str *spaces \u0000 "().1234567890")) (+star (+char-not (str *spaces "() \u0000")))))))
(def *ws (+ignore (+star *space)))
(defn *seq [begin p end]
  (+seqn 1 (+char begin) (+opt (+seqf cons *ws p (+star (+seqn 0 *ws p)))) *ws (+char end)))

(defn *list [] (+map #(cons (last %) (drop-last %)) (*seq "(" (delay (*value)) ")")))
(defn *value [] (+or *number *symbol (*list)))

(def parserObjectSuffix (+parser (+seqn 0 *ws (*value) *ws)))
(defn parseObjectSuffix [str] (parseExpression (parserObjectSuffix str)))

(declare *valueInfix)
(defn *multipleArguments [p sign] (+map (partial reduce #(list (first %2) %1 (second %2))) (+seqf cons *ws p (+star (+seq *ws sign *ws p)) *ws)))

(def *variable (+map symbol (+str (+plus (+char "xyzXYZ")))))

(def *addsubSymbol (+map symbol (+str (+map list (+char "+-")))))
(def *divmulSymbol (+map symbol (+str (+plus (+char "/*")))))
(def *andSymbol (+map symbol (+str (+plus (+char "&")))))
(def *orSymbol (+map symbol (+str (+plus (+char "|")))))
(def *xorSymbol (+map symbol (+str (+plus (+char "^")))))
(def *powlogSymbol (+map symbol (+str (+or (+seq (+char "/") (+char "/")) (+seq (+char "*") (+char "*"))))))

(def *funcSymbol (+map symbol (+str (+seqf cons (+char-not (str *spaces \u0000 "xyzXYZ+-/*&|^().1234567890")) (+star (+char-not (str *spaces "() \u0000")))))))
(defn *listInfix [] (+seqn 1 (+char "(") *ws (delay (*valueInfix)) *ws (+char ")")))
(defn *func [] (+map #(list (first %) (second %)) (+seq *funcSymbol *ws (+or (delay (*listInfix)) (delay (*func)) (delay *number) (delay *variable)))))

(defn *powlog [] (*multipleArguments (+or (delay (*listInfix)) (delay (*func)) (delay *number) (delay *variable)) *divmulSymbol))

(defn *divmul [] (*multipleArguments (+or (delay (*powlog)) (delay (*listInfix)) (delay (*func)) (delay *number) (delay *variable)) *divmulSymbol))
(defn *addsub [] (*multipleArguments (+or (delay (*divmul)) (delay (*powlog)) (delay (*listInfix)) (delay (*func)) *number *variable) *addsubSymbol))
(defn *and [] (*multipleArguments (+or (delay (*addsub)) (delay (*divmul)) (delay (*powlog)) (delay (*listInfix)) (delay (*func)) *number *variable) *andSymbol))
(defn *or [] (*multipleArguments (+or (delay (*and)) (delay (*addsub)) (delay (*divmul)) (delay (*powlog)) (delay (*listInfix)) (delay (*func)) *number *variable) *orSymbol))
(defn *xor [] (*multipleArguments (+or (delay (*or)) (delay (*and)) (delay (*addsub)) (delay (*divmul)) (delay (*powlog)) (delay (*listInfix)) (delay (*func)) *number *variable) *xorSymbol))
(defn *valueInfix [] (+or (delay (*xor)) (delay (*or)) (delay (*and)) (delay (*addsub)) (delay (*divmul)) (delay (*powlog)) (delay (*listInfix)) (delay (*func)) *number *variable))

(def parserObjectInfix (+parser (+seqn 0 *ws (*valueInfix) *ws)))
(defn parseObjectInfix [str] (parseExpression (parserObjectInfix str)))
