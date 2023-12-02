(ns aoc-2023.utils
  (:require [clojure.java.io :as io]))

(defn aoc-input-lines [day]
  (->> (str day "/input.txt")
       io/resource
       io/reader
       line-seq))
