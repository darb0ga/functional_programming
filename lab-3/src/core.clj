(ns core
  (:gen-class)
  (:require [clojure.string :as str]
            [algorithms :as algo])
  (:import (java.io BufferedReader)))

(defn parse-args [args]
  (loop [opts {:linear? false
               :newton? false
               :step    1.0
               :n       4}
         [a & more :as as] args]
    (if (empty? as)
      opts
      (case a
        "--linear" (recur (assoc opts :linear? true) more)
        "--newton" (recur (assoc opts :newton? true) more)
        "--step"   (let [[v & rest-args] more]
                     (recur (assoc opts :step (Double/parseDouble v)) rest-args))
        "-n"       (let [[v & rest-args] more]
                     (recur (assoc opts :n (Integer/parseInt v)) rest-args))
        (recur opts more)))))


(defn parse-point
  [line]
  (let [[sx sy] (-> line
                    str/trim
                    (str/split #"[;\s]+"))]
    {:x (Double/parseDouble sx)
     :y (Double/parseDouble sy)}))

(defn print-point
  [name x y]
  (println (format "%s: %.4f %.4f" name x y)))

(defn process-stream
  [algos ^BufferedReader rdr]
  (loop [lines (line-seq rdr)
         algos algos]
    (if (seq lines)
      (let [line       (first lines)
            rest-lines (rest lines)]
        (if (str/blank? line)
          (do
            (flush)
            (recur rest-lines algos))
          (let [pt      (parse-point line)
                results (mapv (fn [{:keys [name state step-fn] :as algo}]
                                (let [{next-state :state
                                       out        :out} (step-fn state pt)]
                                  {:algo (assoc algo :state next-state)
                                   :name name
                                   :out  out}))
                              algos)]
            (doseq [{:keys [name out]} results
                    [x y]              out]
              (print-point name x y))
            ;; принудительно сбрасываем буфер
            (flush)
            (recur rest-lines (mapv :algo results)))))
      (do
        (doseq [{:keys [name state finalize-fn]} algos
                [x y]                        (finalize-fn state)]
          (print-point name x y))
        (flush)))))


(defn -main
  [& args]
  (let [opts  (parse-args args)
        algos (algo/build-algorithms opts)]
    (when (empty? algos)
      (binding [*out* *err*]
        (println "Нужно указать хотя бы один алгоритм: --linear или --newton")
        (flush)
        (System/exit 1)))
    (let [rdr (BufferedReader. *in*)]
      (process-stream algos rdr))))
