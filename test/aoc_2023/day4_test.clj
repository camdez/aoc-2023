(ns aoc-2023.day4-test
  (:require [aoc-2023.day4 :as sut]
            [aoc-2023.utils :as utils]
            [clojure.test :refer [deftest is]]))

(deftest answers
  (is (= 21568 (sut/part1)))
  (is (= 30 (sut/cards-won (utils/aoc-input-lines 4 "sample2"))))
  ;; Slow:
  ;;(is (= 11827296 (sut/part2)))
  )
