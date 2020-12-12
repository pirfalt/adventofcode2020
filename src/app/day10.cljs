(ns app.day10
  (:require [app.reader :as reader])
  (:require [clojure.string :as s]))

(def example "16
10
15
5
1
11
7
19
6
12
4")

(defn can-jump [step init]
  (let [last (atom init)]
    (fn [next]
      ; (print last next)
      (let [r (<= next (+ step @last))]
        (reset! last next)
        r))))

(->> example
     s/split-lines
     (map js/parseInt)
     sort
     (reduce (fn [acc step]
              ;  (print acc step)
               {:prev step
                :jumps (update (:jumps acc)
                               (- step (:prev acc))
                               inc)})
             {:prev 0
              :jumps [0 0 0 1]})
     :jumps
     ((fn [[_ a _ c]]
        ; (print a c)
        (* a c))))


(defn one [input]
  (->> input
       s/split-lines
       (map js/parseInt)
       sort
       (reduce (fn [acc step]
              ;  (print acc step)
                 {:prev step
                  :jumps (update (:jumps acc)
                                 (- step (:prev acc))
                                 inc)})
               {:prev 0
                :jumps [0 0 0 1]})
       :jumps
       ((fn [[_ a _ c]]
        ; (print a c)
          (* a c)))))


(defn two [input])

(defn run []
  (print (one (reader/read-file "./data/10.txt")))
  (print (two (reader/read-file "./data/10.txt"))))