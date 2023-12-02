(ns aoc-2023.day1-test
  (:require [aoc-2023.day1 :as sut]
            [clojure.test :refer [deftest is]]))

(deftest calibration-val
  (is (= [[29 83 13 24 42 14 76] 281]
         (->> ["two1nine"
               "eightwothree"
               "abcone2threexyz"
               "xtwone3four"
               "4nineeightseven2"
               "zoneight234"
               "7pqrstsixteen"]
              (map #(#'sut/calibration-val % #'sut/flex-digits))
              ((juxt identity #(reduce + %)))))))

(deftest flex-digits
  (is (= ["2" "1" "3" "4"]
         (#'sut/flex-digits "xtwone3four"))
      "doesn't eagerly eat letters needed for subsequent digits"))

(deftest answers-match
  (is (= 54081 (sut/part1)))
  (is (= 54649 (sut/part2))))
