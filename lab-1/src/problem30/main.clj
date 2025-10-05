(ns problem30.main
  (:require
  [problem30.solutions :as nextproblem]))

(defn -main [& args]
  (println "Хвостовая рекурсия:" (nextproblem/sum-tailrec 1000 355000))
  (println "Обычная рекурсия:" (nextproblem/sum-rec 1000 5000))
  (println "Модульная реализация:" (nextproblem/sum-reduce 1000 355000))
  (println "Отображение:" (nextproblem/sum-map 1000 355000))
  (println "Работа со спец. синтаксисом:" (nextproblem/sum-spec 1000 355500))
  (println "Через бесконечные списки:" (nextproblem/sum-lazy 355000)))
