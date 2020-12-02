(ns app.day02
  (:require [app.reader :as reader])
  (:require [clojure.string :as s]))

(def example "1-3 a: abcde
1-3 b: cdefg
2-9 c: ccccccccc")

(defn parse-int [s] (js/parseInt s))

(defn parse-line [line]
  (let [[_ min max key pwd] (re-find #"(\d+)-(\d+) (.): (.*)" line)]
    {:min (parse-int min)
     :max (parse-int max)
     :key key
     :pwd pwd}))

(defn validate [in]
  (let [c     (:key in)
        count (count (filter #(= c %1) (:pwd in)))]
    (and (>= count (:min in))
         (<= count (:max in)))))

(comment
  (validate {:min 1, :max 3, :key "a", :pwd "abcde"})
  (validate {:min 1, :max 3, :key "b", :pwd "cdefg"})
  (validate {:min 2, :max 9, :key "c", :pwd "ccccccccc"}))

(defn one [input]
  (->> input
       (s/split-lines)
       (map parse-line)
       (filter validate)
       count))

(one example)
(one (reader/read-file "./data/02.txt"))


(defn validate-2 [in]
  (let [c     (:key in)
        first  (nth (:pwd in) (dec (:min in)))
        second (nth (:pwd in) (dec (:max in)))]
    (cond
      (and (= c first) (= c second)) false
      (and (not= c first) (not= c second)) false
      :else true)))

(comment
  (validate-2 {:min 1, :max 3, :key "a", :pwd "abcde"})
  (validate-2 {:min 1, :max 3, :key "b", :pwd "cdefg"})
  (validate-2 {:min 2, :max 9, :key "c", :pwd "ccccccccc"}))


(defn two [input]
  (->> input
       (s/split-lines)
       (map parse-line)
       (filter validate-2)
       count))

(defn run []
  (print (one (reader/read-file "./data/02.txt")))
  (print (two (reader/read-file "./data/02.txt"))))