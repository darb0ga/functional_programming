(ns unit-tests
  (:require [clojure.test :refer [deftest is testing]]
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
      (is (some #(= 5 %) (b/bag-seq bag4)))
      (is (= [3 5 5] (b/bag-seq bag4)))
      (is (b/bag-find bag4 3))
      (is (nil? (b/bag-find bag4 42)))
      (is (= [5 5] (b/bag-seq (b/bag-remove bag4 3)))))))

(deftest removal-operations
  (testing "Операции удаления"
    (let [bag (-> (b/bag-empty)
                  (b/bag-insert 1) (b/bag-insert 2) (b/bag-insert 2) (b/bag-insert 3))
          after-remove (b/bag-remove bag 2)]
      (is (= [1 2 3] (b/bag-seq after-remove)))
      (is (= [1 3] (b/bag-seq (b/bag-remove after-remove 2))))
      (is (= [1 2 2 3] (b/bag-seq (b/bag-remove bag 42)))))))

(deftest mapping-and-filtering
  (testing "Map и Filter"
    (let [bag (b/bag-insert (b/bag-insert (b/bag-insert (b/bag-empty) 1) 2) 3)
          mapped (b/bag-map #(* 2 %) bag)
          filtered (b/bag-filter even? bag)]
      (is (= [2 4 6] (b/bag-seq mapped)))
      (is (= [2] (b/bag-seq filtered))))))

(deftest folds
  (testing "foldr и foldl должны совпадать"
    (let [bag (reduce b/bag-insert (b/bag-empty) [1 2 3])]
      (is (= (b/bag-foldr + 0 bag)
             (b/bag-foldl + 0 bag)))
      (is (= 6 (b/bag-foldr + 0 bag))))))

(deftest monoid-properties
  (testing "Свойства моноида"
    (let [empty-bag (b/bag-empty)
          bag1 (reduce b/bag-insert empty-bag [1 2 2])
          bag2 (reduce b/bag-insert empty-bag [3 4])]
      (testing "Левая единица"
        (is (b/bag-equal? bag1 (b/bag-concat empty-bag bag1))))
      (testing "Правая единица"
        (is (b/bag-equal? bag1 (b/bag-concat bag1 empty-bag))))
      (testing "Ассоциативность"
        (let [bag3 (reduce b/bag-insert empty-bag [5 6])
              left-assoc (b/bag-concat (b/bag-concat bag1 bag2) bag3)
              right-assoc (b/bag-concat bag1 (b/bag-concat bag2 bag3))]
          (is (b/bag-equal? left-assoc right-assoc))))
      (testing "Конкатенация множеств"
        (let [concatenated (b/bag-concat bag1 bag2)]
          (is (= 4 (b/bag-size concatenated)))
          (is (= 5 (b/bag-count concatenated))))))))

(deftest equality-testing
  (testing "Эффективное сравнение множеств"
    (let [bag1 (reduce b/bag-insert (b/bag-empty) [1 2 2 3])
          bag2 (reduce b/bag-insert (b/bag-empty) [2 1 3 2])
          bag3 (reduce b/bag-insert (b/bag-empty) [1 2 3])
          bag4 (reduce b/bag-insert (b/bag-empty) [1 2 3 4])]
      (is (b/bag-equal? bag1 bag2))
      (is (not (b/bag-equal? bag1 bag3)))
      (is (not (b/bag-equal? bag1 bag4)))
      (is (b/bag-equal? bag1 bag1)))))

(deftest edge-cases
  (testing "Операции с пустым множеством"
    (let [empty-bag (b/bag-empty)]
      (testing
       (is (b/bag-empty? empty-bag))
        (is (= 0 (b/bag-size empty-bag)))
        (is (= 0 (b/bag-count empty-bag)))
        (is (empty? (b/bag-seq empty-bag)))
        (is (nil? (b/bag-find empty-bag 42)))
        (is (b/bag-equal? empty-bag (b/bag-remove empty-bag 42)))
        (is (empty? (b/bag-seq (b/bag-filter even? empty-bag))))
        (is (empty? (b/bag-seq (b/bag-map inc empty-bag))))))))