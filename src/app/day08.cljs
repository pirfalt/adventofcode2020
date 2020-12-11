(ns app.day08
  (:require [app.reader :as reader])
  (:require [clojure.string :as s]))

(def example "nop +0
acc +1
jmp +4
acc +3
jmp -3
acc -99
acc +1
jmp -4
acc +6")

(defn parse-input [input]
  (->> input
       (re-seq #"(\w+) ([+-]\d+)")
       (map (fn [[_ cmd arg]]
              [(keyword cmd)  (js/parseInt arg)]))
       (into [])))

(defn execute [prog]
  (loop [pc 0
         acc 0
         hist (into [] (map (constantly false) prog))]
    (let [[cmd arg] (nth prog pc)
          seen (nth hist pc)]
      ; (print {:pc pc
      ;         :acc acc
      ;         :cmd cmd
      ;         :arg arg
      ;         :seen seen
      ;         :hist hist})
      (if seen
        acc
        (case cmd
          :nop (recur
                (inc pc)
                acc
                (assoc hist pc true))
          :acc (recur
                (inc pc)
                (+ acc arg)
                (assoc hist pc true))
          :jmp (recur
                (+ pc arg)
                acc
                (assoc hist pc true))
        ;
          )))))

(defn one [input]
  (execute (parse-input input)))

(comment
  (one example)
  (one (reader/read-file "./data/08.txt"))
  ;
  )


(defn execute-2 [prog]
  (loop [pc 0
         acc 0
         hist (into [] (map (constantly false) prog))]
    (if (>= pc (count prog))
      acc
      (let [[cmd arg] (nth prog pc)
            seen (nth hist pc)]
        (if seen
          nil
          (case cmd
            :nop (recur
                  (inc pc)
                  acc
                  (assoc hist pc true))
            :acc (recur
                  (inc pc)
                  (+ acc arg)
                  (assoc hist pc true))
            :jmp (recur
                  (+ pc arg)
                  acc
                  (assoc hist pc true))
            ;
            ))))))

(defn change-program [prog idx]
  (update prog idx (fn [[cmd arg]]
                     (case cmd
                       :jmp [:nop arg]
                       :nop [:jmp arg]
                       [cmd arg]))))

(defn two [input]
  (loop [prog (parse-input input)
         i 0
         original true]
    (let [r (execute-2 prog)]
      (cond
        (not= r nil) r
        original (recur (change-program prog i) i false)
        (not original) (recur (change-program prog i) (inc i) true)
      ;  
        ))))


(comment
  (two example)
  (two (reader/read-file "./data/08.txt"))
  ;
  )

(defn run []
  (print (one (reader/read-file "./data/08.txt")))
  (print (two (reader/read-file "./data/08.txt"))))