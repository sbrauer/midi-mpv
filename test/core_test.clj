(ns core-test
  (:require [clojure.test :refer :all]
            [core :as sut]))

(deftest test-hello
  (is (= "Hello World"
         (sut/hello "World"))))
