(ns app.day01p1
  (:require [app.reader :refer [read-file]])
  (:require [clojure.string :refer [split-lines]]))

;; (def data "1721\n979\n366\n299\n675\n1456")

(defn parse-int [str]
  (js/parseInt str))

(defn parse []
  (let [input (read-file "./data/01.txt")
        str-vector (split-lines input)
        str-list (list* str-vector)
        numbers (map parse-int str-list)]
    numbers))

(defn create-sum [n, m]
  {:n n :m m :sum (+ n m)})

(defn add-to-all [numbers n]
  (map #(create-sum n %) numbers))


(defn find-numbers [numbers]
  (->> numbers
       (map #(add-to-all numbers %))
       (reduce #(concat %1 %2))
       (filter #(= 2020 (:sum %)))
       (first)))

(defn run []
  (let [numbers (parse)
        result (find-numbers numbers)]
    (println (* (:n result) (:m result)))))