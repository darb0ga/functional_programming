(ns test
  (:require [clojure.test :refer :all]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [tree]
            [bt-bag :as b]))

(defspec prop-bag-count-vs-seq
  20
  (prop/for-all [xs (gen/vector gen/int)]
                (let [bag (reduce b/bag-insert (b/bag-empty) xs)]
                  (= (b/bag-count bag)
                     (count (b/bag->seq bag))))))

(defspec prop-bag-equal-symmetric
  20
  (prop/for-all [xs (gen/vector gen/int)]
                (let [b1 (reduce b/bag-insert (b/bag-empty) xs)
                      b2 (reduce b/bag-insert (b/bag-empty) xs)]
                  (= (b/bag-equal? b1 b2)
                     (b/bag-equal? b2 b1)))))

(defspec prop-map-preserves-size
  20
  (prop/for-all [xs (gen/vector gen/int)]
                (let [bag (reduce b/bag-insert (b/bag-empty) xs)
                      mapped (b/bag-map inc bag)]
                  (= (b/bag-size bag)
                     (b/bag-size mapped)))))
