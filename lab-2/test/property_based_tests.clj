(ns property-based-tests
  (:require [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [tree]
            [bt-bag :as b]))

;; Кастомный генератор, который сразу создает bag
(defn gen-bag []
  (gen/fmap
   (fn [xs] (reduce b/bag-insert (b/bag-empty) xs))
   (gen/vector gen/int)))

;; Альтернативный вариант с более сложной логикой генерации
(defn gen-bag-complex []
  (gen/let [xs (gen/vector gen/int)]
    (reduce b/bag-insert (b/bag-empty) xs)))

;; Упрощенные тесты с новым генератором

(defspec prop-bag-count-vs-seq
  20
  (prop/for-all [bag (gen-bag)]
                (= (b/bag-count bag)
                   (count (b/bag-seq bag)))))

(defspec prop-bag-equal-symmetric
  20
  (prop/for-all [bag (gen-bag)]
                (let [bag-copy (reduce b/bag-insert (b/bag-empty) (b/bag-seq bag))]
                  (= (b/bag-equal? bag bag-copy)
                     (b/bag-equal? bag-copy bag)))))

(defspec prop-map-preserves-size
  20
  (prop/for-all [bag (gen-bag)]
                (let [mapped (b/bag-map inc bag)]
                  (= (b/bag-size bag)
                     (b/bag-size mapped)))))

(defspec prop-monoid-identity
  20
  (prop/for-all [bag (gen-bag)]
                (let [empty-bag (b/bag-empty)]
                  (and (b/bag-equal? bag (b/bag-concat empty-bag bag))
                       (b/bag-equal? bag (b/bag-concat bag empty-bag))))))

(defspec prop-monoid-associativity
  20
  (prop/for-all [b1 (gen-bag)
                 b2 (gen-bag)
                 b3 (gen-bag)]
                (b/bag-equal? (b/bag-concat (b/bag-concat b1 b2) b3)
                              (b/bag-concat b1 (b/bag-concat b2 b3)))))
