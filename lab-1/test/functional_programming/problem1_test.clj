(ns functional-programming.problem1-test
  (:require [clojure.test :refer :all]
            [clojure.java.shell :as shell]
            [clojure.string :as str]
            [problem1.solutions :as sol]))

;; Общие тестовые данные 
(def small-limit 10)  ;; числа до 10 → кратные 3 или 5: 3 + 5 + 6 + 9 = 23
(def test-limit 1000) ;; для проверки результата

;; Проверка вспомогательной функции 
(deftest test-multiple-num
  (testing "Проверка кратности 3 или 5"
    (is (true? (sol/multiple-num? 3)))
    (is (true? (sol/multiple-num? 5)))
    (is (false? (sol/multiple-num? 7)))))


;; Вызов Python-скрипта
(defn run-python-script [script-path & args]
  (let [command (into ["python" script-path] args)
        result (apply shell/sh command)]
    (if (zero? (:exit result))
      (str/trim (:out result))
      (throw (Exception. (str "Python error: " (:err result)))))))

(defn run-problem1-python [start end]
  (run-python-script "./python_code/sol1.py" (str start) (str end)))


;; Проверка каждой реализации 
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

;; Проверка согласованности между всеми реализациями 
(deftest test-consistency
  (testing "Все реализации дают одинаковый результат при большом лимите"
    (let [py-result (Long/parseLong (run-problem1-python 1 test-limit))
          clj-results [(sol/sum-tailrec 1 test-limit 0)
                   (sol/sum-rec 1 test-limit)
                   (sol/sum-reduce 1 test-limit)
                   (sol/sum-map 1 test-limit)
                   (sol/sum-spec 1 test-limit)
                   (sol/sum-lazy test-limit)]]
      (is (apply = py-result clj-results)))))
