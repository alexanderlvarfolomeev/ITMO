(deftype Var [variable])
(deftype ConstOp [value])
(deftype DivideOp [arguments])
(deftype UnaryOperation [name operation argument dif])
(deftype MultipleArgumentsOperation [name operation arguments dif])

(declare Variable Constant Negate Square Sqrt Sinh Cosh Add Subtract Multiply Divide)

(defmulti evaluate (fn [exp _] (class exp)))
(defmulti toString class)
(defmulti toStringSuffix class)
(defmulti diff (fn [exp _] (class exp)))

(defmethod toString Var [exp]
  (.-variable exp))
(defmethod toStringSuffix Var [exp]
  (.-variable exp))
(defmethod evaluate Var [exp vars]
  (vars (.-variable exp)))
(defmethod diff Var [exp var]
  (if (= var (.-variable exp))
    (Constant 1)
    (Constant 0)))

(defmethod toString ConstOp [exp]
  (str (format "%.0f.0" (.-value exp))))                    ;;TODO f != java.lang.Long
(defmethod toStringSuffix ConstOp [exp]
  (str (format "%.0f.0" (.-value exp))))                    ;;TODO f != java.lang.Long
(defmethod evaluate ConstOp [exp _]
  (.-value exp))
(defmethod diff ConstOp [_ _] (Constant 0))

(defmethod toString UnaryOperation [exp]
  (str "(" (.-name exp) " " (toString (.-argument exp)) ")"))
(defmethod toStringSuffix UnaryOperation [exp]
  (str "(" (toString (.-argument exp)) " " (.-name exp) ")"))
(defmethod evaluate UnaryOperation [exp vars]
  ((.-operation exp) (evaluate (.-argument exp) vars)))
(defmethod diff UnaryOperation [exp var]
  ((.-dif exp) (.-argument exp) var))

(defmethod toString DivideOp [exp] (str "(/" (reduce #(str %1 " " (toString %2)) "" (.-arguments exp)) ")"))
(defmethod toStringSuffix DivideOp [exp] (str "(" (reduce #(str %1 (toString %2) " ") "" (.-arguments exp)) "\\)"))
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
  (str "(" (reduce #(str %1 (toString %2) " ") "" (.-arguments exp)) (.-name exp) ")"))
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
  (let [tokens (vec (.split expr " "))]
  (first (first (fold (reduce parseInfix [[] []] tokens) 0)))))

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
;(tabulate (_empty nil) ["" "~"])

(defn _char [p]
  (fn [[c & cs]]
    (if (and c (p c)) (-return c cs))))
;(tabulate (_char #{\a \b \c}) ["ax" "by" "" "a" "x" "xa"])

(defn _map [f]
  (fn [result]
    (if (-valid? result)
      (-return (f (-value result)) (-tail result)))))
;(tabulate (comp (_map clojure.string/upper-case) (_char #{\a \b \c})) ["a" "a~" "b" "b~" "" "x" "x~"])

(defn _combine [f a b]
  (fn [str]
    (let [ar ((force a) str)]
      (if (-valid? ar)
        ((_map (partial f (-value ar)))
          ((force b) (-tail ar)))))))
;(tabulate (_combine str (_char #{\a \b}) (_char #{\x})) ["ax" "ax~" "bx" "bx~" "axx" "" "a" "x" "xa"])

(defn _either [a b]
  (fn [str]
    (let [ar ((force a) str)]
      (if (-valid? ar) ar ((force b) str)))))
;(tabulate (_either (_char #{\a}) (_char #{\b})) ["ax" "ax~" "bx" "bx~" "" "a" "x" "xa"])

(defn _parser [p]
  (fn [input]
    (-value ((_combine (fn [v _] v) p (_char #{\u0000})) (str input \u0000)))))
;(mapv (_parser (_combine str (_char #{\a \b}) (_char #{\x}))) ["ax" "ax~" "bx" "bx~" "" "a" "x" "xa"])

(defn +char [chars] (_char (set chars)))
;(tabulate (+char "abc") ["a" "a~" "b" "b~" "" "x" "x~"])

(defn +char-not [chars] (_char (comp not (set chars))))
;(tabulate (+char-not "abc") ["a" "a~" "b" "b~" "" "x" "x~"])

(defn +map [f parser] (comp (_map f) parser))
;(tabulate (+map clojure.string/upper-case (+char "abc")) ["a" "a~" "b" "b~" "" "x" "x~"])

(def +parser _parser)

(def +ignore (partial +map (constantly 'ignore)))
;(tabulate (+ignore (+char "abc")) ["a" "a~" "b" "b~" "" "x" "x~"])

(defn iconj [coll value]
  (if (= value 'ignore) coll (conj coll value)))
(defn +seq [& ps]
  (reduce (partial _combine iconj) (_empty []) ps))
;(tabulate (+seq (+char "abc") (+ignore (+char "xyz")) (+char "ABC")) ["axA~" "axA" "aXA~"])

(defn +seqf [f & ps] (+map (partial apply f) (apply +seq ps)))
;(tabulate (+seqf str (+char "abc") (+ignore (+char "xyz")) (+char "ABC")) ["axA~" "axA" "aXA~"])

(defn +seqn [n & ps] (apply +seqf (fn [& vs] (nth vs n)) ps))
;(tabulate (+seqn 1 (+char "abc") (+ignore (+char "xyz")) (+char "ABC")) ["axA~" "axA" "aXA~"])

(defn +or [p & ps]
  (reduce (partial _either) p ps))
;(tabulate (+or (+char "a") (+char "b") (+char "c")) ["a" "a~" "b" "b~" "" "x" "x~"])

(defn +opt [p]
  (+or p (_empty nil)))
;(tabulate (+opt (+char "a")) ["a" "a~" "aa" "aa~" "" "~"])

(defn +star [p]
  (letfn [(rec [] (+or (+seqf cons p (delay (rec))) (_empty ())))] (rec)))
;(tabulate (+star (+char "a")) ["a" "a~" "aa" "aa~" "" "~"])

(defn +plus [p] (+seqf cons p (+star p)))
;(tabulate (+plus (+char "a")) ["a" "a~" "aa" "aa~" "" "~"])

(defn +str [p] (+map (partial apply str) p))
;(tabulate (+str (+star (+char "a"))) ["a" "a~" "aa" "aa~" "" "~"])

(def parserObjectSuffix
  (let
    [*all-chars (mapv char (range 0 128))
     *digit (+char (apply str (filter #(Character/isDigit %) *all-chars)))
     *number (+map read-string (+str (+plus *digit)))       ;;works?
     spaces (apply str (filter #(or (Character/isWhitespace %) (= \, %)) *all-chars))
     *space (+char spaces)
     *symbol (+map symbol (+str (+seqf cons (+char-not (str spaces \( \) "1234567890")) (+star (+char-not (str spaces \( \)))))))
     *ws (+ignore (+star *space))]
    (letfn [(*seq [begin p end]
              (+seqn 1 (+char begin) (+opt (+seqf cons *ws p (+star (+seqn 0 *ws p)))) *ws (+char end)))
            (*list [] (+map #(cons (last %) (drop-last %)) (*seq "(" (delay (*value)) ")")))
            (*value [] (+or *number *symbol (*list)))]      ;;TODO *symbol
      (+parser (+seqn 0 *ws (*value) *ws)))))
(defn parseObjectSuffix [str] (parseExpression (parserObjectSuffix str)))