(ns app.day07
  (:require [app.reader :as reader])
  (:require [clojure.string :as s]))

(def example "light red bags contain 1 bright white bag, 2 muted yellow bags.
dark orange bags contain 3 bright white bags, 4 muted yellow bags.
bright white bags contain 1 shiny gold bag.
muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.
shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.
dark olive bags contain 3 faded blue bags, 4 dotted black bags.
vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.
faded blue bags contain no other bags.
dotted black bags contain no other bags.
")

(defn parse-line [line]
  (let [[_ type inner-line] (re-matches #"^(.+) bags contain (.+)$" line)]
    [type inner-line]))

(defn parse-contined
  "Expects input like:
    '1 bright white bag, 2 muted yellow bags.'
   "
  [inner-line]
  (->> (re-seq #"(\d+) ([\w ]+?) bags?[,\.]" inner-line)
       (map (fn [[_ count type]]
              {:count count, :type type}))))

(defn parse-input
  "Exects input like example"
  [input]
  (->> (s/split-lines input)
       (map (fn [line]
              (let [[type inner-line] (parse-line line)
                    inner (parse-contined inner-line)]
                {:type type
                 :inner inner})))
      ;;  
       ))

(defn rules-map
  ""
  [rules]
  (reduce #(assoc %1
                  (:type %2)
                  (->> (:inner %2)
                       (map :type)))
          {}
          rules))

(defn traverse [m start target]
  (loop [m m
         key start
         d 0]
    (let [s (m key)]
      (cond
        (= key target) d
        (empty? s) nil
        :else (recur (update-in m [key] next)
                     (first s)
                     (inc d)
                     ;
                     )))))

(defn one-first-attempt [input]
  (let [rules (parse-input input)
        m (rules-map rules)]
    (->> (keys m)
         dedupe
         (map (fn [k] [k (traverse m k "shiny gold")]))
         (filter #(case (second %)
                    nil false
                    0 false
                    true))
         count
         ;
         )))


(defn inner-has [rule target]
  (->> rule
       :inner
       (map :type)
       (some (partial = target))))

(defn includes-target [rules target]
  (->> rules
       (filter #(inner-has % target))
       (map :type)))

(defn bag-includes [rules target]
  (let [includes (includes-target rules target)]
    (concat includes
            (mapcat #(bag-includes rules %)
                    includes))))

(defn one [input]
  (-> input
      parse-input
      (bag-includes "shiny gold")
      distinct
      count))

(comment
  (one example)
  (one (s/trim (reader/read-file "./data/07.txt")))

  (one-first-attempt example)
  (one-first-attempt (s/trim (reader/read-file "./data/07.txt")))
  ;
  )

(defn inners-for [rules target]
  (:inner (first (filter #(= target (:type %)) rules))))


(defn nested-bags [rules t]
  (let [inners (inners-for rules t)]
    (+ 1
       (->> inners
            (map (fn [inner]
                   (* (:count inner)
                      (nested-bags rules (:type inner)))))
            (apply +)))))

(defn two [input]
  (-> input
      parse-input
      (nested-bags "shiny gold")
      dec))

(comment
  (two example)
  (two (reader/read-file "./data/07.txt")))

(defn run []

  (print (one (reader/read-file "./data/07.txt")))
  (print (two (reader/read-file "./data/07.txt"))))