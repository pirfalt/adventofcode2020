(ns app.day01p1
  (:require [app.reader :as reader])
  (:require [clojure.string :as s]))

(def example (s/split-lines "1721
979
366
299
675
1456"))


(defn lines [filename]
  (let [file (app.reader/read-file filename)
        lines (s/split-lines file)]
    lines))

(defn numbers [lines]
  (map #(js/parseInt %1) lines))

(def input (lines "./data/01.txt"))

(defn one []
  (->> (for [a (numbers input)
             b (numbers input)
             :when (= 2020 (+ a b))]
         (* a b))
       (first)))


(defn two []
  (->> (for [a (numbers input)
             b (numbers input)
             c (numbers input)
             :when (= 2020 (+ a b c))]
         (* a b c))
       (first)))

(defn run []
  (print (one))
  (print (two)))