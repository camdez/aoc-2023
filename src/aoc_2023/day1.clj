(ns aoc-2023.day1
  (:require
   [aoc-2023.utils :as utils]
   [clojure.string :as str]))

(defn- calibration-val [ln digit-fn]
  (->> ln
       digit-fn
       ((juxt first last))
       (apply str)
       parse-long))

(defn- calibration-sum [digit-fn lns]
  (->> lns
       (map #(calibration-val % digit-fn))
       (reduce +)))

(defn part1 []
  (->> (utils/aoc-input-lines 1)
       (calibration-sum (partial re-seq #"\d"))))

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
  (->> (utils/aoc-input-lines 1)
       (calibration-sum flex-digits)))
