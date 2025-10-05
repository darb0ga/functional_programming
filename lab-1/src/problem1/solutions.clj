(ns problem1.solutions)

;; Проверка кратности 3 или 5
(defn multiple-num? [n]
  (or (zero? (mod n 3))
      (zero? (mod n 5))))


;; Хвостовая рекурсия
(defn sum-tailrec [n limit acc]
  (if (>= n limit)
    acc
    (recur (inc n)
           limit
           (if (multiple-num? n )
             (+ acc n) acc))))

;; Рекурсия 
(defn sum-rec [n limit]
  (if (>= n limit)
    0
    (+ (if (multiple-num? n) 
         n 0)
       (sum-rec (inc n) limit))))

;; Модульная реализация со сверткой
(defn sum-reduce [fir las]
  (->> (range fir las)
        (filter multiple-num?)
        (reduce +)))


;; Генерация последовательности при помощи отображения
(defn sum-map [fir las]
  (->> (range fir las)
          (map #(if (multiple-num? %) % 0))
          (reduce +)))


;; Со спец. синтаксисом для циклов
(defn sum-spec [fir las]
  (loop [x fir
         acc 0]
    (if (>= x las)
      acc
      (recur (inc x) 
             (if (multiple-num? x) 
               (+ acc x) acc)))))


;; Бесконечные списки
(defn sum-lazy [limit]
  (->> (range limit)
        (filter multiple-num?)
        (reduce +)))

