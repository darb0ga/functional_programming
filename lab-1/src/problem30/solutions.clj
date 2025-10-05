(ns problem30.solutions)


(defn sum-digits-pow [n]
  (->> (str n)
       (map #(Integer/parseInt (str %)))
       (map #(Math/pow % 5))
       (reduce +)
       (long)))



 (defn sum-tailrec [n limit]
   (loop [i n
          acc 0]
     (if (>= i limit)
       acc
       (recur (inc i)
              (if (= (sum-digits-pow i) i)
                (+ acc i) acc)))))


(defn sum-rec [n limit]
  (if (>= n limit)
    0
    (+ (if (= (sum-digits-pow n) n) n 0) 
       (sum-rec (inc n) limit)))
  )


(defn sum-reduce [fir las]
  (reduce + 0 (filter (fn [x]
                       (= (sum-digits-pow x) x))
                      (range fir las))))


(defn sum-map [fir las])


(defn sum-spec [fir las])


(defn sum-lazy [limit]) 