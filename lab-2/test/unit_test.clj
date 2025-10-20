(ns unit-test
  (:require [clojure.test :refer :all]
            [bt-bag :as b]))

(deftest basic-operations
  (testing "Проверка базовых операций bag"
    (let [bag1 (b/bag-empty)
          bag2 (b/bag-insert bag1 5)
          bag3 (b/bag-insert bag2 3)
          bag4 (b/bag-insert bag3 5)]
      (is (false? (b/bag-empty? bag4)))
      (is (= 2 (b/bag-size bag4)))
      (is (= 3 (b/bag-count bag4)))
      (is (some #(= 5 %) (b/bag->seq bag4)))
      (is (= [3 5 5] (b/bag->seq bag4)))
      (is (b/bag-find bag4 3))
      (is (nil? (b/bag-find bag4 42)))
      (is (= [5 5] (b/bag->seq (b/bag-remove bag4 3)))))))

(deftest mapping-and-filtering
  (testing "Map и Filter"
    (let [bag (b/bag-insert (b/bag-insert (b/bag-insert (b/bag-empty) 1) 2) 3)
          mapped (b/bag-map #(* 2 %) bag)
          filtered (b/bag-filter even? bag)]
      (is (= [2 4 6] (b/bag->seq mapped)))
      (is (= [2] (b/bag->seq filtered))))))

(deftest folds
  (testing "foldr и foldl должны совпадать при коммутативных функциях"
    (let [bag (reduce b/bag-insert (b/bag-empty) [1 2 3])]
      (is (= (b/bag-foldr + 0 bag)
             (b/bag-foldl + 0 bag)))
      (is (= 6 (b/bag-foldr + 0 bag))))))