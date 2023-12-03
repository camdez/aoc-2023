(ns aoc-2023.utils
  (:require [clojure.java.io :as io]))

(defn aoc-input-lines
  ([day]
   (aoc-input-lines day "input"))
  ([day file-name]
   (->> (str day "/" file-name ".txt")
        io/resource
        io/reader
        line-seq)))
