(ns app-test
  (:require [cljs.test :refer (deftest is)]
            app))

(deftest should-say-hello
  (is (= "Hello, Jayway!" (app/hello-jayway))))
