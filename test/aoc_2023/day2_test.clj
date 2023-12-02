(ns aoc-2023.day2-test
  (:require [aoc-2023.day2 :as sut]
            [clojure.test :refer [deftest is]]))

(deftest parse-game
  (is (= ["1" [{:blue 3, :red 4}
               {:red 1, :green 2, :blue 6}
               {:green 2}]]
         (sut/parse-game "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green"))))

(deftest move-possible?
  (is (sut/move-possible? {:blue 1} {:blue 2}))
  (is (not (sut/move-possible? {:blue 2} {:blue 1})))
  (is (not (sut/move-possible? {:red 7} {})))
  (is (not (sut/move-possible? {:red 2 :blue 1} {:red 1 :blue 1}))))

(deftest game-possible?
  (is (sut/game-possible?
       ["1" [{:blue 1}
             {:blue 1, :red 1}]]
       {:blue 2, :red 1}))
  (is (not (sut/game-possible?
            ["2" [{:blue 1}
                  {:red 1}]]
            {:blue 1}))))

(deftest min-cubes-needed
  (is (= {:red 2, :blue 3, :yellow 4}
         (sut/min-cubes-needed
          ["1" [{:red 2 :blue 1}
                {:yellow 4}
                {:blue 3}]]))))

(deftest answers-match
  (is (= 2416  (sut/part1)))
  (is (= 63307 (sut/part2))))
