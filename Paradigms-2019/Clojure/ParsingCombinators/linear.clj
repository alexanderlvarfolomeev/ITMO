(defn v? [v] (and (vector? v) (every? #(number? %) v)))
(defn m? [m] (and
               (coll? m)
               (every? #(and (v? %) (== (count %) (count (first m))))
                       m)))

(defn v-op [f & vs]
  {:pre  [(m? vs)]
   :post [(vector? %)]}
  (apply mapv f vs))

(defn v+ [& vs] (apply v-op + vs))
(defn v- [& vs] (apply v-op - vs))
(defn v* [& vs] (apply v-op * vs))

(defn v*s [v & ss]
  {:pre  [(v? v) (every? #(number? %) ss)]
   :post [(v? v) (== (count v) (count %))]}
  (mapv #(apply * % ss) v))

(defn scalar [v1 v2]
  {:pre  [(m? (vector v1 v2))]
   :post [(number? %)]}
  (apply + (v* v1 v2)))

(defn shift [v]
  {:pre  [(v? v)]
   :post [(v? v) (== (count v) (count %))]}
  (conj (vec (rest v)) (first v)))

(defn vect
  ([v]
   {:pre  [(v? v)]
    :post [(identical? v %)]}
   (identity v))
  ([v1 v2]
   {:pre  [(m? (vector v1 v2)) (== 3 (count v1))]
    :post [(v? %) (== 3 (count %))]}
   (shift (v- (v* v1 (shift v2)) (v* v2 (shift v1)))))
  ([v1 v2 & vs] (apply vect (vect v1 v2) vs)))

(defn sameSize? [m1 m2]
  {:pre  [(m? m1) (m? m2)]
   :post [(boolean? %)]}
  (and
    (== (count m1) (count m2))
    (== (count (first m1)) (count (first m2)))))

(defn m-op [f & ms]
  {:pre  [(every?
            #(sameSize? % (first ms))
            ms)]
   :post [(sameSize? % (first ms))]}
  (apply mapv f ms))
(defn m+ [& ms] (apply m-op v+ ms))
(defn m- [& ms] (apply m-op v- ms))
(defn m* [& ms] (apply m-op v* ms))

(defn m*s [m & ss]
  {:pre  [m? m]
   :post [(sameSize? % m)]}
  (mapv #(apply v*s % ss) m))

(defn m*v [m v]
  {:pre  [(m? m) (v? v) (== (count (first m)) (count v))]
   :post [(v? v) (== (count %) (count m))]}
  (mapv #(apply + (v* % v)) m))

(defn transpose [m]
  {:pre  [(m? m)]
   :post [(m? %)
          (== (count m) (count (first %)))
          (== (count %) (count (first m)))]}
  (apply v-op vector m))

(defn m*m
  ([m]
   {:pre  [(m? m)]
    :post [(identical? m %)]}
   (identity m))
  ([m1 m2]
   {:pre  [(m? m1)
           (m? m2)
           (== (count (first m1)) (count m2))]
    :post [(m? %)
           (== (count m1) (count %))
           (== (count (first m2)) (count (first %)))]}
   (transpose (mapv #(m*v m1 %) (transpose m2))))
  ([m1 m2 & ms] (apply m*m (m*m m1 m2) ms)))

(def sizeT (memoize
             (fn [t]
               {:pre  [(or (vector? t) (number? t))]
                :post [(v? %)]
                }
               ((if (number? t)
                  #(vector)
                  #(apply vector (count t) (sizeT (first t))))))))

(def t? (memoize
          (fn [t] (and
                    (coll? t)
                    (or
                      (every? #(number? %) t)
                      (every? #(and (t? %) (= (sizeT %) (sizeT (first t))))
                              t))))))

(defn t-op [f & ts]
  {:pre  [(or (every? #(number? %) ts)
              (every? #(and
                         (t? %)
                         (= (sizeT %) (sizeT (first ts))))
                      ts))]
   :post [(or (number? %)
              (and
                (t? %)
                (= (sizeT (first ts)) (sizeT %))))]}
  ((if (number? (first ts))
     #(apply f ts)
     #(apply mapv (fn [& t] (apply t-op f t)) ts))))

(defn t+ [& ts] (apply t-op + ts))
(defn t- [& ts] (apply t-op - ts))
(defn t* [& ts] (apply t-op * ts))