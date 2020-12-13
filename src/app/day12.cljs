(ns app.day12
  (:require [app.reader :as reader])
  (:require [clojure.string :as s]))

(def example "F10
N3
F7
R90
F11")

;> 17 + 8 = 25.

(defn rotate [direction rotation]
  (let [m {"N" 0
           "E" 90
           "S" 180
           "W" 270}
        inverted-m (into {} (map (juxt val key) m))
        angle (m direction)
        next-angle (mod (+ angle rotation) 360)]
    (inverted-m next-angle)))

(defn abs [n]
  (if (> 0 n)
    (- 0 n)
    n))



(defn one [input]
  (->> (re-seq #"(\w)(\d+)" input)
       (map (fn [[_ a v]]
              [a (js/parseInt v)]))
       (reduce (fn [acc [a v]]
                 ; (print a v acc)
                 (let [out (update acc :history #(conj % [a v]))]
                   (case a
                     "N" (update out :y + v)
                     "S" (update out :y - v)
                     "E" (update out :x + v)
                     "W" (update out :x - v)
                     "L" (update out :d rotate (- 0 v))
                     "R" (update out :d rotate v)
                     "F" (case (acc :d)
                           "N" (update out :y + v)
                           "S" (update out :y - v)
                           "E" (update out :x + v)
                           "W" (update out :x - v)))))
               {:history []
                :d "E"
                :x 0
                :y 0})
       (#(+ (abs (:x %)) (abs (:y %))))))

(comment
  (one example)
  (one (reader/read-file "./data/12.txt"))
  ;
  )


(defn rotate-wp [{:keys [wpy wpx]} rotation]
  (let [m {0   {:wpx wpx :wpy wpy}
           90 {:wpx wpy :wpy (- 0 wpx)}
           180 {:wpx (- 0 wpx) :wpy (- 0 wpy)}
           270  {:wpx (- 0 wpy) :wpy wpx}}]
    (m (mod rotation 360))))

(defn two [input]
  (->> (re-seq #"(\w)(\d+)" input)
       (map (fn [[_ a v]]
              [a (js/parseInt v)]))
       (reduce (fn [acc [a v]]
                 (let [out (update acc :history #(conj % [a v (dissoc acc :history)]))]
                   (case a
                     "N" (update out :wpy + v)
                     "S" (update out :wpy - v)
                     "E" (update out :wpx + v)
                     "W" (update out :wpx - v)
                     "L" (merge out (rotate-wp out (- 0 v)))
                     "R" (merge out (rotate-wp out v))
                     "F" (assoc out
                                :x (+ (:x acc)
                                      (* (:wpx acc)
                                         v))
                                :y (+ (:y acc)
                                      (* (:wpy acc)
                                         v))))))
               {:history []
                :x 0
                :y 0
                :wpx 10
                :wpy 1})
       (#(+ (abs (:x %)) (abs (:y %))))))

(comment
  (two example)
  (two (reader/read-file "./data/12.txt"))
  ;
  )

(defn run []
  (print (one (reader/read-file "./data/12.txt")))
  (print (two (reader/read-file "./data/12.txt"))))