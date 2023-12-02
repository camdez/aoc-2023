(ns aoc-2023.day2
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn parse-game [ln]
  (let [[_ id moves-str] (re-find #"Game (\d+): (.*)" ln)]
    [id
     (for [move-str (str/split moves-str #"; ")]
       (->> (str/split move-str #", ")
            (map #(let [[count color] (str/split % #" ")]
                    [(keyword color)
                     (parse-long count)]))
            (into {})))]))

(defn move-possible? [move bag-cnts]
  (every? (fn [color]
            (>= (get bag-cnts color 0)
                (get move color 0)))
          (keys move)))

(defn game-possible? [[_id moves] bag-cnts]
  (every? #(move-possible? % bag-cnts) moves))

(defn part1 []
  (->> "2/input.txt"
       io/resource
       io/reader
       line-seq
       (map parse-game)
       (filter #(game-possible? % {:red 12, :green 13, :blue 14}))
       (map (comp parse-long first))
       (reduce +)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn min-cubes-needed [[_id moves]]
  (apply merge-with max moves))

(defn cube-power [cube-cnts]
  (reduce * (vals cube-cnts)))

(defn part2 []
  (->> "2/input.txt"
       io/resource
       io/reader
       line-seq
       (map (comp cube-power min-cubes-needed parse-game))
       (reduce +)))
