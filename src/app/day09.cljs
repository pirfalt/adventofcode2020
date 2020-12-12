(ns app.day09
  (:require [app.reader :as reader])
  (:require [clojure.string :as s]))

(def example "35
20
15
25
47
40
62
55
65
95
102
117
150
182
127
219
299
277
309
576")

(defn parse-input [input] (map js/parseInt (s/split-lines input)))

(defn take-sums [count input]
  (for [a (take count input)
        b (take count input)
        :when (not= a b)]
    (+ a b)))

(defn one [size input]
  (loop [input (parse-input input)]
    (if (some #(= % (first (drop size input)))
              (take-sums size input))
      (recur (next input))
      (first (drop size input)))))

(comment
  (def input (parse-input example))
  (one 5 example)
  (one 25 (reader/read-file "./data/09.txt"))
  ;
  )

(defn seq-sums [target input]
  (for [i (range (count input))
        j (range (count input))
        :let [range (take j (drop i input))
              sum (apply + range)]
        :while (>= target sum)]
    [sum range]))

(defn two [target input]
  (->> (seq-sums target input)
       (filter (fn [[sum]]
                 (= sum target)))
       first
       ((fn [[_ r]]
          (+ (apply min r) (apply max r))))))

(comment
  (def input (parse-input example))
  (def target 127)

  (two 127 (parse-input example))
  (two 22406676 (parse-input (reader/read-file "./data/09.txt")))
  ;
  )
