(ns aoc-2023.day3-test
  (:require [aoc-2023.day3 :as sut]
            [clojure.test :refer [deftest is]]))

(deftest answers
  (is (= 525119 (sut/part1)))
  (is (= 76504829 (sut/part2))))
