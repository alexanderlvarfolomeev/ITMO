(load-file "numeric_tower.clj")
(load-file "expression_classes.clj")
(load-file "combinators.clj")

(def operations {'+      Add, '- Subtract, '* Multiply, '/ Divide,
                 'negate Negate, 'square Square, 'sqrt Sqrt, 'sinh Sinh, 'cosh Cosh
                 '&      And, '| Or, (symbol "^") Xor, '** Pow, (symbol "//") Log})

(defn parseExpression [expr] (cond
                               (number? expr) (Constant expr)
                               (symbol? expr) (Variable (str expr))
                               :else (apply (operations (first expr)) (map parseExpression (rest expr)))))

(def parseObject (comp parseExpression read-string))


(def =all-chars (mapv char (range 1 128)))
(def =digit (+char (apply str (filter #(Character/isDigit %) =all-chars))))
(def =number (+map read-string (+str (+seqf #(into (vec (cons %1 %2)) (vec (cons %3 %4)))
                                            (+opt (+char "+-")) (+plus =digit) (+opt (+char "."))
                                            (+opt (+plus =digit))))))
(def =spaces (apply str (filter #(or (Character/isWhitespace %) (= \, %)) =all-chars)))
(def =space (+char =spaces))


(declare *valueSuffix)
(def *symbol (+map symbol (+str (+seqf cons
                                       (+char-not (str =spaces \u0000 "().1234567890"))
                                       (+star (+char-not (str =spaces "() \u0000")))))))
(def *ws (+ignore (+star =space)))
(defn *seq [begin p end]
  (+seqn 1 (+char begin) (+opt (+seqf cons *ws p (+star (+seqn 0 *ws p)))) *ws (+char end)))

(defn *list [] (+map #(cons (last %) (drop-last %)) (*seq "(" (delay (*valueSuffix)) ")")))
(defn *valueSuffix [] (+or =number *symbol (*list)))

(def parserObjectSuffix (+parser (+seqn 0 *ws (*valueSuffix) *ws)))
(defn parseObjectSuffix [str] (parseExpression (parserObjectSuffix str)))

(declare *valueInfix)

(def *variable (+map symbol (+str (+plus (+char "xyzXYZ")))))

(def *addSubSymbol (+map symbol (+str (+map list (+char "+-")))))
(def *divMulSymbol (+map symbol (+str (+plus (+char "/*")))))
(def *andSymbol (+map symbol (+str (+plus (+char "&")))))
(def *orSymbol (+map symbol (+str (+plus (+char "|")))))
(def *xorSymbol (+map symbol (+str (+plus (+char "^")))))
(def *powLogSymbol (+map symbol (+str (+or (+seq (+char "/") (+char "/")) (+seq (+char "*") (+char "*"))))))

(def *funcSymbol (+map symbol (+str (+seqf cons (+char-not (str =spaces \u0000 "xyzXYZ+-/*&|^().1234567890"))
                                           (+star (+char-not (str =spaces "() \u0000")))))))
(defn *listInfix [] (+seqn 1 (+char "(") *ws (delay (*valueInfix)) *ws (+char ")")))
(defn *func [] (+map #(list (first %) (second %)) (+seq *funcSymbol *ws (+or (delay (*listInfix)) (delay (*func))
                                                                             (delay =number) (delay *variable)))))

(declare *powlog, *divmul, *addsub, *and, *or, *xor)
(def *priority [(delay (*xor)) (delay (*or)) (delay (*and))
               (delay (*addsub)) (delay (*divmul)) (delay (*powlog))
               (delay (*func)) (delay (*listInfix)) (delay =number) (delay *variable)])

(defn *multipleArguments [priority sign]
  (let
    [op (apply +or (take-last priority *priority))]
    (+map (partial reduce #(list (first %2) %1 (second %2)))
          (+seqf cons *ws op (+star (+seq *ws sign *ws op)) *ws))))
(defn *multipleArgumentsRight [priority sign]
  (let
    [op (apply +or (take-last priority *priority))]
    (+map (fn [s] (reduce #(list (second %2) (first %2) %1) (second s) (reverse (first s))))
          (+seq *ws (+star (+seq op *ws sign *ws)) op *ws))))

(defn *powlog [] (*multipleArgumentsRight 4 *powLogSymbol))
(defn *divmul [] (*multipleArguments 5 *divMulSymbol))
(defn *addsub [] (*multipleArguments 6 *addSubSymbol))
(defn *and [] (*multipleArguments 7 *andSymbol))
(defn *or [] (*multipleArguments 8 *orSymbol))
(defn *xor [] (*multipleArguments 9 *xorSymbol))
(defn *valueInfix [] (apply +or *priority))

(def parserObjectInfix (+parser (+seqn 0 *ws (*valueInfix) *ws)))
(defn parseObjectInfix [str] (parseExpression (parserObjectInfix str)))