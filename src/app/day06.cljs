(ns app.day06
  (:require [app.reader :as reader])
  (:require [clojure.string :as s]))

(def example "abc

a
b
c

ab
ac

a
a
a
a

b")


(defn one [input]
  (->> (s/split input #"\n\n")
       (map #(into #{} %1))
       (map #(disj %1 "\n"))
       (map count)
       (apply +)))

(comment
  (one example)
  ;;
  )

(defn two [input]
  (->>
   ;; Split groups
   (s/split (s/trim input) #"\n\n")
   ;; Add trailing newline, newline count -> group size
   (map #(str % "\n"))
   (map (fn [g]
          (let [freq (frequencies g)     ;; Char frequencie in group
                c (freq "\n")            ;; Group size
                freq (dissoc freq "\n")] ;; Frequencie wihtout size
            (->> freq
                 ;; Filter where frequencie value = group size (all said "yes")
                 (filter (fn [[_ v]] (= v c)))
                 ;; To map
                 (into {})))))
   ;; Map size = amount of keys where all is "yes"
   (map count)
   (apply +)))

(comment
  (two example)

  (two (reader/read-file "./data/06.txt"))
  ;;
  )

(defn run []
  (print (one (reader/read-file "./data/06.txt")))
  (print (two (reader/read-file "./data/06.txt"))))
