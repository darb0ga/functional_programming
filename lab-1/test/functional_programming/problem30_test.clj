(ns functional-programming.problem30-test
  (:require [clojure.test :refer :all]
            [clojure.java.shell :as shell]
            [clojure.string :as str]
            [problem30.solutions :as sol]))

;; Общие тестовые данные 
(def small-limit 1000) 
(def test-limit 355000) ;; для проверки результата - предел = 6 * 9^5 = 354294

;; Тестирование вспомогательной функции
(deftest test-sum-digits-pow
  (testing "Сумма пятых степеней цифр числа"
    ;; 4150 → 4^5 + 1^5 + 5^5 + 0^5 = 1024 + 1 + 3125 + 0 = 4150
    (is (= 4150 (sol/sum-digits-pow 4150)))
    ;; 123 → 1^5 + 2^5 + 3^5 = 1 + 32 + 243 = 276
    (is (= 276 (sol/sum-digits-pow 123)))))


;; Вызов Python-скрипта
(defn run-python-script [script-path & args]
  (let [command (into ["python" script-path] args)
        result (apply shell/sh command)]
    (if (zero? (:exit result))
      (str/trim (:out result))
      (throw (Exception. (str "Python error: " (:err result)))))))

(defn run-problem30-python [start end]
  (run-python-script "./python_code/sol30.py" (str start) (str end)))

;; Проверка всех функций Clojure на маленьком диапазоне
(deftest test-small-range
  (testing "Проверка всех реализаций на малом диапазоне"
    (let [res-tail (sol/sum-tailrec 1000 2000)
          res-rec  (sol/sum-rec 1000 2000)
          res-red  (sol/sum-reduce 1000 2000)
          res-map  (sol/sum-map 1000 2000)
          res-spec (sol/sum-spec 1000 2000)
          res-lazy (sol/sum-lazy 2000)]
      ;; Проверяем, что все реализации дают одинаковый результат
      (is (apply = [res-tail res-rec res-red res-map res-spec res-lazy])))))

;; Сравнение с Python
(deftest test-python-vs-clojure
  (testing "Сравнение Clojure-реализаций с Python"
    (let [py-result (Long/parseLong (run-problem30-python small-limit test-limit))
          clj-results [(sol/sum-tailrec small-limit test-limit)
                       (sol/sum-reduce small-limit test-limit)
                       (sol/sum-map small-limit test-limit)
                       (sol/sum-spec small-limit test-limit)
                       (sol/sum-lazy test-limit)]]
      ;; Сравниваем Python и все Clojure реализации
      (is (apply = py-result clj-results)))))