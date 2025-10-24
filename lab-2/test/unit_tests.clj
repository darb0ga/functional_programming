(ns unit-tests
  (:require [clojure.test :refer [deftest is testing]]
            [bt-bag :as b]
            [clojure.test.check.generators :as gen]))

;;  Генерация случайных множеств
(defn random-bag
  ([] (random-bag 5))
  ([n]
   (reduce b/bag-insert (b/bag-empty)
           (gen/sample (gen/choose 0 9) n))))

(deftest basic-operations
  (testing "Проверка базовых операций bag"
    (let [bag1 (b/bag-empty)
          random-vals (gen/sample (gen/elements [1 2 3 4 5 6 7 8 9]) 3)
          bag2 (reduce b/bag-insert bag1 random-vals)]
      (is (false? (b/bag-empty? bag2)))
      (is (>= (b/bag-size bag2) 1))
      (is (= (count (b/bag-seq bag2)) (b/bag-count bag2)))
      (is (every? int? (b/bag-seq bag2)))
      (is (= (sort (b/bag-seq bag2)) (sort (b/bag-seq bag2))))
      (doseq [x random-vals]
        (is (b/bag-find bag2 x))))))

(deftest removal-operations
  (testing "Операции удаления"
    (let [bag (random-bag 6)
          seq-before (b/bag-seq bag)
          element (rand-nth seq-before)
          after-remove (b/bag-remove bag element)]
      (is (<= (b/bag-size after-remove) (b/bag-size bag)))
      (is (not (nil? (b/bag-seq after-remove))))
      (is (<= (b/bag-count after-remove) (b/bag-count bag)))
      (is (not-any? #(= % element)
                    (when (not-any? #(= % element) seq-before)
                      (b/bag-seq (b/bag-remove bag element))))))))

(deftest mapping-and-filtering
  (testing "Map и Filter"
    (let [bag (random-bag 4)
          mapped (b/bag-map #(* 2 %) bag)
          filtered (b/bag-filter even? bag)]
      (is (every? int? (b/bag-seq mapped)))
      (is (every? even? (b/bag-seq filtered)))
      (is (= (count (b/bag-seq filtered))
             (count (filter even? (b/bag-seq bag))))))))

(deftest folds
  (testing "foldr и foldl должны совпадать"
    (let [bag (random-bag 5)]
      (is (= (b/bag-foldr + 0 bag)
             (b/bag-foldl + 0 bag))))))

(deftest monoid-properties
  (testing "Свойства моноида"
    (let [empty-bag (b/bag-empty)
          bag1 (random-bag 4)
          bag2 (random-bag 3)]
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
          (is (= 7 (b/bag-count concatenated))))))))

(deftest equality-testing
  (testing "Эффективное сравнение множеств"
    (let [vals [1 2 2 3]
          bag1 (reduce b/bag-insert (b/bag-empty) vals)
          bag2 (reduce b/bag-insert (b/bag-empty) (shuffle vals))
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