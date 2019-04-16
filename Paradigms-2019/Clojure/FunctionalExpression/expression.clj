(defn variable [var] #(% var))
(defn constant [value] (constantly value))

(defn unaryOperation [f argument] (comp f argument))
(defn negate [argument] (unaryOperation - argument))
(defn sinh [argument] (unaryOperation #(Math/sinh %) argument))
(defn cosh [argument] (unaryOperation #(Math/cosh %) argument))

(defn operationWithMultipleArguments [f arguments] (fn [coll] (apply f
                                                                     ((apply juxt arguments) coll))))
(defn add [& arguments] (operationWithMultipleArguments + arguments))
(defn subtract [& arguments] (operationWithMultipleArguments - arguments))
(defn multiply [& arguments] (operationWithMultipleArguments * arguments))

(defn divide [x y] #(/ (double (x %)) (y %)))               ;;cause java.lang.Number checks division by zero

(def operations {'+ add, '- subtract, '* multiply, '/ divide, 'negate negate, 'sinh sinh, 'cosh cosh})

(defn parseExpression [expr] (cond
                               (number? expr) (constant expr)
                               (symbol? expr) (variable (str expr))
                               :else (apply (operations (first expr)) (map parseExpression (rest expr)))))
(def parseFunction (comp parseExpression read-string))