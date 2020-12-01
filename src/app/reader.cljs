(ns app.reader
  (:require ["fs" :as fs]))

(defn read-file [file-name]
  (let [buffer (.readFileSync fs file-name)]
    (.toString buffer "utf-8")))
