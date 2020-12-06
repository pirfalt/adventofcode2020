(ns app.day05
  (:require [app.reader :as reader])
  (:require [clojure.string :as s])
  (:require [clojure.set :as set]))

;; BFFFBBFRRR: row 70, column 7, seat ID 567.
;; FFFBBBFRRR: row 14, column 7, seat ID 119.
;; BBFFBBFRLL: row 102, column 4, seat ID 820.
(def example "BFFFBBFRRR
FFFBBBFRRR
BBFFBBFRLL")


(defn to-int [c]
  (case c
    "B" "1"
    "F" "0"
    "R" "1"
    "L" "0"))


(defn parse-line [line]
  (let [ints (s/join "" (map to-int line))
        [_ row-s col-s] (re-matches #"(\d{7})(\d{3})" ints)
        row (js/parseInt row-s 2)
        col (js/parseInt col-s 2)]
    {:row row
     :col col
     :seat-id (+ (* row 8) col)}))

(comment
  (parse-line (first (s/split-lines example)))
  (parse-line (second (s/split-lines example)))
  (parse-line (nth (s/split-lines example) 2)))

(defn one [input]
  (->> (s/split-lines input)
       (map parse-line)
       (map :seat-id)
       (reduce max)))

(comment
  (one example))


;; --------------------------------------------------------------


(defn two [input]
  (let [nrs (->> (s/split-lines input)
                 (map parse-line)
                 (map :seat-id)
                 sort)
        all (range (first nrs) (last nrs))]
    (first (set/difference
            (into #{} all)
            (into #{} nrs)))))

(comment
  (def input (reader/read-file "./data/05.txt"))
  (two input)
  ;;
  )

(defn run []
  (print (one (reader/read-file "./data/05.txt")))
  (print (two (reader/read-file "./data/05.txt"))))

(run)
