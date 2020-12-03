(ns app.day03
  (:require [app.reader :as reader])
  (:require [clojure.string :as s]))

(def example "..##.......
#...#...#..
.#....#..#.
..#.#...#.#
.#...##..#.
..#.##.....
.#.#.#....#
.#........#
#.##...#...
#...##....#
.#..#...#.#")

(defn is-tree [grid down right]
  (let [row (nth grid down)
        i   (mod right (count row))
        col (nth row i)]
    (= "#" col)))

(comment
  (do
    (print (is-tree (s/split-lines example) 0 0))
    (print (is-tree (s/split-lines example) 1 3))
    (print (is-tree (s/split-lines example) 2 6))))


(defn count-path [input down right]
  (let [grid (s/split-lines input)
        rows (count grid)]
    (->> (range)
         (take-while #(< (* %1 down) rows))
         (map (fn [i]
                (is-tree grid (* i down) (* i right))))
         (filter true?)
         count)))

(defn one [input]
  (count-path input 1 3))

(defn two [input]
  (apply *
         [(count-path input 1 1)
          (count-path input 1 3)
          (count-path input 1 5)
          (count-path input 1 7)
          (count-path input 2 1)]))

(defn run []
  (print (one (reader/read-file "./data/03.txt")))
  (print (two (reader/read-file "./data/03.txt"))))
