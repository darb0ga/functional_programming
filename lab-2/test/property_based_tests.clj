(ns property-based-tests
  (:require [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [tree]
            [bt-bag :as b]))

 ;; Кастомный генератор
(defn gen-int-bag []
  (gen/vector gen/int))

 ;; Тестирование функции count
(defspec prop-bag-count-vs-seq
  20
  (prop/for-all [xs (gen-int-bag)]
                (let [bag (reduce b/bag-insert (b/bag-empty) xs)]
                  (= (b/bag-count bag)
                     (count (b/bag-seq bag))))))

 ;; Сравнение двух множеств
(defspec prop-bag-equal-symmetric
  20
  (prop/for-all [xs (gen-int-bag)]
                (let [b1 (reduce b/bag-insert (b/bag-empty) xs)
                      b2 (reduce b/bag-insert (b/bag-empty) xs)]
                  (= (b/bag-equal? b1 b2)
                     (b/bag-equal? b2 b1)))))

 ;; Тестирование размера множества
(defspec prop-map-preserves-size
  20
  (prop/for-all [xs (gen-int-bag)]
                (let [bag (reduce b/bag-insert (b/bag-empty) xs)
                      mapped (b/bag-map inc bag)]
                  (= (b/bag-size bag)
                     (b/bag-size mapped)))))

 ;; Свойства моноида - нейтральный элемент
(defspec prop-monoid-identity
  20
  (prop/for-all [xs (gen-int-bag)]
                (let [bag (reduce b/bag-insert (b/bag-empty) xs)
                      empty-bag (b/bag-empty)]
                  (b/bag-equal? bag (b/bag-concat empty-bag bag)))))

 ;; Свойства моноида - ассоциативность
(defspec prop-monoid-associativity
  20
  (prop/for-all [xs (gen-int-bag)
                 ys (gen-int-bag)
                 zs (gen-int-bag)]
                (let [b1 (reduce b/bag-insert (b/bag-empty) xs)
                      b2 (reduce b/bag-insert (b/bag-empty) ys)
                      b3 (reduce b/bag-insert (b/bag-empty) zs)]
                  (b/bag-equal? (b/bag-concat (b/bag-concat b1 b2) b3)
                                (b/bag-concat b1 (b/bag-concat b2 b3))))))
