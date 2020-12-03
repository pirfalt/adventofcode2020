(ns app.day0
  (:require [app.reader :as reader])
  (:require [clojure.string :as s]))

(def example "")

(defn one [input])

(defn two [input])

(defn run []
  (print (one (reader/read-file "./data/00.txt")))
  (print (two (reader/read-file "./data/00.txt"))))