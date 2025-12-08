(ns algorithms)

;; ------------------------------------------------------------
;; Линейная интерполяция
;; ------------------------------------------------------------

(defn linear-step
  "Один шаг линейной интерполяции.
   state  – {:prev точка-1 :next-x x-для-следующего-вывода :step h}
   pt     – новая входная точка {:x .. :y ..}.
   Возвращает {:state new-state :out [[x1 y1] [x2 y2] ...]}."
  [{:keys [prev next-x step] :as state} pt]
  (if (nil? prev)
    {:state (assoc state :prev pt :next-x (:x pt))
     :out   []}
    (let [x0 (:x prev)
          y0 (:y prev)
          x1 (:x pt)
          y1 (:y pt)
          start-x (double (or next-x x0))]
      (loop [x   start-x
             out []]
        (if (<= x x1)
          (let [t (/ (- x x0) (- x1 x0))
                y (+ y0 (* t (- y1 y0)))]
            (recur (+ x step)
                   (conj out [x y])))
          {:state {:prev   pt
                   :next-x x
                   :step   step}
           :out   out})))))

(defn linear-finalize [_state]
  [])

;; ------------------------------------------------------------
;; Ньютон
;; ------------------------------------------------------------

(defn newton-coeffs
  "Считает коэффициенты полинома Ньютона по узлам xs и значениям ys."
  [xs ys]
  (let [xs (vec xs)]
    (loop [k       0
           coeffs  []
           fk      (vec ys)]
      (if (= k (count xs))
        coeffs
        (let [c       (nth fk 0)
              next-fk (vec
                       (for [i (range (dec (count fk)))]
                         (/ (- (nth fk (inc i)) (nth fk i))
                            (- (nth xs (+ i k 1))
                               (nth xs i)))))]
          (recur (inc k)
                 (conj coeffs c)
                 next-fk))))))

(defn newton-eval
  "Вычисляет значение полинома Ньютона с коэффициентами coeffs
   и узлами xs в точке x."
  [coeffs xs x]
  (let [xs     (vec xs)
        coeffs (vec coeffs)]
    (loop [i   (dec (count coeffs))
           acc 0.0]
      (if (neg? i)
        acc
        (recur (dec i)
               (+ (* acc (- x (nth xs i)))
                  (nth coeffs i)))))))

(defn newton-step
  "Шаг поточной интерполяции Ньютона по окну из n точек.
   state – {:points [...], :step h, :n n, :next-x x}
   pt    – новая входная точка.
   Возвращает {:state new-state :out [[x1 y1] ...]}."
  [{:keys [points step n next-x] :as state} pt]
  (let [pts' (conj (vec points) pt)
        ps   (if (> (count pts') n)
               (vec (take-last n pts'))
               pts')]
    (if (< (count ps) 2)
      {:state (assoc state :points ps)
       :out   []}
      (let [xs      (mapv :x ps)
            ys      (mapv :y ps)
            coeffs  (newton-coeffs xs ys)
            start-x (double (or next-x (first xs)))
            last-x  (last xs)]
        (loop [x   start-x
               out []]
          (if (<= x last-x)
            (let [y (newton-eval coeffs xs x)]
              (recur (+ x step)
                     (conj out [x y])))
            {:state {:points ps
                     :step   step
                     :n      n
                     :next-x x}
             :out   out}))))))

(defn newton-finalize [_state]
  [])

;; ------------------------------------------------------------
;; Построение списка активных алгоритмов
;; ------------------------------------------------------------

(defn build-algorithms
  "Принимает opts {:linear? .. :newton? .. :step .. :n ..} и
   возвращает вектор структур алгоритмов."
  [{:keys [linear? newton? step n]}]
  (cond-> []
    linear?
    (conj {:name        "linear"
           :state       {:prev nil :next-x nil :step step}
           :step-fn     linear-step
           :finalize-fn linear-finalize})

    newton?
    (conj {:name        "newton"
           :state       {:points [] :next-x nil :step step :n n}
           :step-fn     newton-step
           :finalize-fn newton-finalize})))
