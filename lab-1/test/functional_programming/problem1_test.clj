(ns functional-programming.problem1-test
  (:require [clojure.test :refer :all]
            [problem1.solutions :as sol]))

;; ---------- Общие тестовые данные ----------
(def small-limit 10)  ;; числа до 10 → кратные 3 или 5: 3 + 5 + 6 + 9 = 23
(def medium-limit 1000) ;; для проверки производительности и совпадения результатов

;; ---------- Проверка отдельной функции ----------
(deftest test-multiple-of-3-or-5
  (testing "Проверка кратности 3 или 5"
    (is (true? (sol/multiple-num? 3)))
    (is (true? (sol/multiple-num? 5)))
    (is (false? (sol/multiple-num? 7)))))

;; ---------- Проверка каждой реализации ----------
(deftest test-sum-tailrec
  (testing "Хвостовая рекурсия до 10"
    (is (= 23 (sol/sum-tailrec 1 small-limit 0)))))

(deftest test-sum-rec
  (testing "Обычная рекурсия до 10"
    (is (= 23 (sol/sum-rec 1 small-limit)))))

(deftest test-sum-reduce
  (testing "Через reduce"
    (is (= 23 (sol/sum-reduce 1 small-limit)))))

(deftest test-sum-map
  (testing "Через map"
    (is (= 23 (sol/sum-map 1 small-limit)))))

(deftest test-sum-spec
  (testing "Через loop/recur"
    (is (= 23 (sol/sum-spec 1 small-limit)))))

(deftest test-sum-lazy
  (testing "Через ленивую последовательность"
    (is (= 23 (sol/sum-lazy small-limit)))))

;; ---------- Проверка согласованности между всеми реализациями ----------
(deftest test-consistency
  (testing "Все реализации дают одинаковый результат при большом лимите"
    (let [results [(sol/sum-tailrec 1 medium-limit 0)
                   (sol/sum-rec 1 medium-limit)
                   (sol/sum-reduce 1 medium-limit)
                   (sol/sum-map 1 medium-limit)
                   (sol/sum-spec 1 medium-limit)
                   (sol/sum-lazy medium-limit)]]
      (is (apply = results)))))
