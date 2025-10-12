(ns problem1.main
  (:require
   [problem1.solutions :as firstproblem]))

(defn -main [& _args]
  (println "Хвостовая рекурсия:" (firstproblem/sum-tailrec 1 1000 0))
  (println "Обычная рекурсия:" (firstproblem/sum-rec 1 1000))
  (println "Модульная реализация:" (firstproblem/sum-reduce 1 1000))
  (println "Отображение:" (firstproblem/sum-map 1 1000))
  (println "Работа со спец. синтаксисом:" (firstproblem/sum-spec 1 1000))
  (println "Через бесконечные списки:" (firstproblem/sum-lazy 1000)))
