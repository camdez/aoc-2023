(ns aoc-2023.day4
  (:require [aoc-2023.utils :as utils]
            [clojure.math :as math]
            [clojure.set :as set]
            [clojure.string :as str]))

(defn- line-wins [line]
  (->> (str/split line #"(:| \|) +")
       (drop 1)
       (map #(into #{} (map parse-long) (str/split % #" +")))
       (apply set/intersection)
       count))

(defn- line-points [line]
  (->> (line-wins line) dec (math/pow 2) int))

(defn pile-worth [lines]
  (->> lines (map line-points) (reduce +)))

(defn part1 []
  (pile-worth (utils/aoc-input-lines 4)))

(defn cards-won [lines]
  (let [idx-wins (mapv line-wins lines)
        init-cnt (count idx-wins)]
    (loop [held-idxs (range init-cnt)
           card-cnt  init-cnt]
      (let [idxs+wins (->> held-idxs
                           (keep (fn [idx]
                                   (let [wins (nth idx-wins idx)]
                                     (when (pos? wins)
                                       [idx wins])))))
            card-cnt  (+ card-cnt (reduce + (map second idxs+wins)))
            new-idxs  (->> idxs+wins
                           (mapcat (fn [[idx wins]]
                                     (let [nidx (inc idx)]
                                       (range nidx (+ wins nidx))))))]
        (if (seq new-idxs)
          (recur new-idxs card-cnt)
          card-cnt)))))

(defn part2 []
  (cards-won (utils/aoc-input-lines 4)))
