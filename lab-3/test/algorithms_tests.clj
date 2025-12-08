(ns algorithms-tests
  (:require [clojure.test :refer :all]
            [algorithms :as algo]))

(defn run-linear
  "прогоняет список точек через linear-step"
  [step points]
  (let [init-state {:prev nil :next-x nil :step step}]
    (:out
     (reduce (fn [{:keys [state out]} pt]
               (let [{s :state o :out} (algo/linear-step state pt)]
                 {:state s
                  :out   (into out o)}))
             {:state init-state
              :out   []}
             points))))

(defn run-newton
  "прогоняет список точек через newton-step"
  [step n points]
  (let [init-state {:points [] :step step :n n :next-x nil}]
    (:out
     (reduce (fn [{:keys [state out]} pt]
               (let [{s :state o :out} (algo/newton-step state pt)]
                 {:state s
                  :out   (into out o)}))
             {:state init-state
              :out   []}
             points))))

(deftest linear-interpolation-simple
  (testing "Линейная интерполяция для y = x, шаг 0.5"
    (let [points  [{:x 0.0 :y 0.0}
                   {:x 1.0 :y 1.0}
                   {:x 2.0 :y 2.0}]
          result  (run-linear 0.5 points)
          expected [[0.0 0.0]
                    [0.5 0.5]
                    [1.0 1.0]
                    [1.5 1.5]
                    [2.0 2.0]]]
      (is (= expected result))))

  (testing "Линейная интерполяция с другим шагом"
    (let [points  [{:x 0.0 :y 0.0}
                   {:x 2.0 :y 2.0}]
          result  (run-linear 1.0 points)
          expected [[0.0 0.0]
                    [1.0 1.0]
                    [2.0 2.0]]]
      (is (= expected result)))))

(deftest newton-coeffs-and-eval
  (testing "Полином Ньютона для y = x^2"
    (let [xs [0.0 1.0 2.0]
          ys [0.0 1.0 4.0]
          c  (algo/newton-coeffs xs ys)]
      (doseq [x [0.0 0.5 1.0 1.5 2.0]]
        (let [expected (* x x)
              actual   (algo/newton-eval c xs x)]
          (is (< (Math/abs (- expected actual)) 1.0e-9)))))))

(deftest newton-interpolation-stream
  (testing "Поточная интерполяция Ньютона для y = x, n = 4, шаг 0.5"
    (let [points  [{:x 0.0 :y 0.0}
                   {:x 1.0 :y 1.0}
                   {:x 2.0 :y 2.0}
                   {:x 3.0 :y 3.0}
                   {:x 5.0 :y 5.0}]
          result  (run-newton 0.5 4 points)]
      ;; Для линейной функции результат должен совпадать с y = x
      (doseq [[x y] result]
        (is (< (Math/abs (- x y)) 1.0e-9))))))

