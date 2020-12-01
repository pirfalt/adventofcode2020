(ns app.runner
  (:require [app.reader :refer [read-file]]))

;; currently broken in shadow-cljs
(set! *warn-on-infer* true)

(defn main []
  (println (read-file "./test.txt")))
