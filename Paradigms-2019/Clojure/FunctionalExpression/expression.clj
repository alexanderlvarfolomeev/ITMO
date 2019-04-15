(defn variable [var] #(% var))
(defn constant [value] (constantly value))

(defn negate [argument] (comp - argument))

(defn operationWithMultipleArguments [f arguments] (fn [coll] (apply f
                                                                     (map #(% coll) arguments))))
(defn add [& arguments] (operationWithMultipleArguments + arguments))
(defn subtract [& arguments] (operationWithMultipleArguments - arguments))
(defn multiply [& arguments] (operationWithMultipleArguments * arguments))

(defn divide [x y] #(/ (double (x %)) (y %)))               ;;cause java.lang.Number checks division by zero

(def operations {'+ add, '- subtract, '* multiply, '/ divide, 'negate negate})

(defn parseExpression [expr] (cond
                               (number? expr) (constant expr)
                               (symbol? expr) (variable (str expr))
                               :else (apply (operations (first expr)) (map parseExpression (rest expr)))))
(def parseFunction (comp parseExpression read-string))