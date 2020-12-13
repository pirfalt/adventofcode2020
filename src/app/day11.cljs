(ns app.day11
  (:require [app.reader :as reader])
  (:require [clojure.string :as s]))

(def example "L.LL.LL.LL
LLLLLLL.LL
L.L.L..L..
LLLL.LL.LL
L.LL.LL.LL
L.LLLLL.LL
..L.L.....
LLLLLLLLLL
L.LLLLLL.L
L.LLLLL.LL")


(defn neighbours [s i input]
  [(get input (- (dec i) s) ".") (get input (- i s) ".") (get input (- (inc i) s) ".")
   (get input    (dec i)    ".")                         (get input    (inc i)    ".")
   (get input (+ (dec i) s) ".") (get input (+ i s) ".") (get input (+ (inc i) s) ".")])

(defn count-occupied-neighbours [n]
  (->> n
       (filter #(= % "#"))
       count))

(defn round [input]
  (let [s (inc (.indexOf input "\n"))]
    (->> input
         (map-indexed (fn [i v]
                        (let [n (neighbours s i input)
                              c (count-occupied-neighbours n)]
                          (case v
                            "L" (if (= c 0)
                                  "#"
                                  "L")
                            "#" (if (>= c 4)
                                  "L"
                                  "#")
                            v))))
         s/join)))

(defn one [input]
  (loop [input input]
    (let [next (round input)]
      (if (not= input next)
        (recur next)
        (count (filter #(= % "#") input))))))

(comment
  (one example)
  (one (reader/read-file "./data/11.txt"))
  ;
  )


(defn look [input i next-idx]
  (loop [i i]
    (let [v (get input i "\n")]
      (case v
        "." (recur (next-idx i))
        v))))


(defn visible-neighbours [input i]
  (let [s (inc (.indexOf input "\n"))
        ul #(- % s 1)
        u  #(- % s 0)
        ur #(- % s -1)

        cl #(- % 1)
        cr #(+ % 1)

        dl #(+ % s -1)
        d  #(+ % s 0)
        dr #(+ % s 1)]
    [(look input (ul i) ul) (look input (u i) u) (look input (ur i) ur)
     (look input (cl i) cl)                      (look input (cr i) cr)
     (look input (dl i) dl) (look input (d i) d) (look input (dr i) dr)]))

(defn round-visible [input]
  (->> input
       (map-indexed (fn [i v]
                      (let [n (visible-neighbours input i)
                            c (count-occupied-neighbours n)]
                        (case v
                          "L" (if (= c 0)
                                "#"
                                "L")
                          "#" (if (>= c 5)
                                "L"
                                "#")
                          v))))
       s/join))

(defn two [input]
  (loop [input input]
    (let [next (round-visible input)]
      (if (not= input next)
        (recur next)
        (count (filter #(= % "#") input))))))

(comment
  (two "L.LL.LL.LL
LLLLLLL.LL
L.L.L..L..
LLLL.LL.LL
L.LL.LL.LL
L.LLLLL.LL
..L.L.....
LLLLLLLLLL
L.LLLLLL.L
L.LLLLL.LL")

  (two (reader/read-file "./data/11.txt"))
  ;
  )

(defn run []
  (print (one (reader/read-file "./data/11.txt")))
  (print (two (reader/read-file "./data/11.txt"))))