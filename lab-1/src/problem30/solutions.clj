(ns problem30.solutions)

;; Сумма пятых степеней цифр числа
(defn sum-digits-pow [n]
  (->> (str n)
       (map #(Integer/parseInt (str %)))
       (map #(Math/pow % 5))
       (reduce +)
       long))


;; Хвостовая рекурсия
 (defn sum-tailrec [n limit]
   (loop [i n
          acc 0]
     (if (>= i limit)
       acc
       (recur (inc i)
              (if (= (sum-digits-pow i) i)
                (+ acc i) acc)))))


;; Рекурсия 
(defn sum-rec [n limit]
  (if (>= n limit)
    0
    (+ (if (= (sum-digits-pow n) n) n 0) 
       (sum-rec (inc n) limit)))
  )


;; Модульная реализация со сверткой
(defn sum-reduce [fir las]
  (reduce + 0 (filter (fn [x]
                       (= (sum-digits-pow x) x))
                      (range fir las))))


;; Генерация последовательности при помощи отображения
(defn sum-map [fir las]
  (->> (range fir las)
        (map #(if (= (sum-digits-pow %) %) % 0))
        (reduce +)))

;; Со спец. синтаксисом для циклов
(defn sum-spec [fir las] 
  (loop [x fir, acc 0]
    (if (< x las) 
      (recur (inc x)
           (if (= (sum-digits-pow x) x) 
             (+ acc x) acc)) 
      acc)))

;; Бесконечные списки
(defn sum-lazy [limit]
  (->> (range 1000 limit)
       (filter #(= (sum-digits-pow %) %))
       (reduce +)))