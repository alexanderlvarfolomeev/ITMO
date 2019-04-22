;(use '[clojure.math.numeric-tower :as math])
(defn variable [var] #(% var))
(defn constant [value] (constantly value))
(defn unaryOperation [& functions] (apply comp functions))
(defn negate [argument] (unaryOperation - argument))
(defn square [argument] (unaryOperation #(* % %) argument))
;(defn sqrt [argument] (unaryOperation math/sqrt math/abs argument))
(defn sqrt [argument] (unaryOperation #(Math/sqrt %) #(Math/abs %) argument))
(defn sinh [argument] (unaryOperation #(Math/sinh %) argument))
(defn cosh [argument] (unaryOperation #(Math/cosh %) argument))

(defn operationWithMultipleArguments
  [f arguments] (fn [coll] (apply f
                                  ((apply juxt arguments) coll))))
(defn add [& arguments] (operationWithMultipleArguments + arguments))
(defn subtract [& arguments] (operationWithMultipleArguments - arguments))
(defn multiply [& arguments] (operationWithMultipleArguments * arguments))

(def originalMin min)
(def originalMax max)
(defn functionalChecker [f arguments] (if (number? (first arguments))
                                          (apply f arguments)
                                          (operationWithMultipleArguments f arguments)))
(defn min [& arguments] (functionalChecker originalMin arguments))
(defn max [& arguments] (functionalChecker originalMax arguments))

(defn divide [x y] #(/ (double (x %)) (y %)))               ;;Cause java.lang.Number checks division by zero

(def operations {'+    add, '- subtract, '* multiply, '/ divide, 'negate negate,
                 'sinh sinh, 'cosh cosh, 'min min, 'max max, 'square square, 'sqrt sqrt})

(defn parseExpression [expr] (cond
                               (number? expr) (constant expr)
                               (symbol? expr) (variable (str expr))
                               :else (apply (operations (first expr)) (map parseExpression (rest expr)))))
(def parseFunction (comp parseExpression read-string))