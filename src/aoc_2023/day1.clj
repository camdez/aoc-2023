(ns aoc-2023.day1
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn- calibration-val [ln digit-fn]
  (->> ln
       digit-fn
       ((juxt first last))
       (apply str)
       parse-long))

(defn- file-sum [file-name digit-fn]
  (->> file-name
       io/resource
       io/reader
       line-seq
       (map #(calibration-val % digit-fn))
       (reduce +)))

(defn part1 []
  (file-sum "1/input.txt" (partial re-seq #"\d")))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- re-alt-str [alts]
  (str "(?=(" (str/join "|" alts) "))"))

(defn- re-pos-lookahead [pat-str]
  (str "(?=" pat-str ")"))

(def spelled-digits {"one"   "1"
                     "two"   "2"
                     "three" "3"
                     "four"  "4"
                     "five"  "5"
                     "six"   "6"
                     "seven" "7"
                     "eight" "8"
                     "nine"  "9"})
(def flex-digits-re (-> spelled-digits
                        keys
                        (conj "\\d")
                        re-alt-str
                        re-pos-lookahead
                        re-pattern))

(defn- flex-digits [s]
  (->> s
       (re-seq flex-digits-re)
       (map second)
       (map #(spelled-digits % %))))

(defn part2 []
  (file-sum "1/input.txt" flex-digits))
