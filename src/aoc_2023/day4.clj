(ns aoc-2023.day4
  (:require [aoc-2023.utils :as utils]
            [clojure.math :as math]
            [clojure.set :as set]
            [clojure.string :as str]))

(defn- line-wins [line]
  (->> (str/split line #"(:| \|) +")
       (drop 1)
       (mapv #(set (map parse-long (str/split % #" +"))))
       (apply set/intersection)
       count))

(defn- line-points [line]
  (->> (line-wins line)
       dec
       (math/pow 2)
       int))

(defn pile-worth [lines]
  (->> lines
       (map line-points)
       (reduce +)))

(defn part1 []
  (pile-worth (utils/aoc-input-lines 4)))

(defn cards-won [lines]
  (let [idx-cards (into {} (map-indexed vector) lines)
        idx-wins  (memoize (comp line-wins idx-cards))]
    (loop [held-idxs (keys idx-cards)
           card-cnt  (count held-idxs)]
      (let [idxs+pts (->> held-idxs
                          (keep (fn [idx]
                                  (let [points (idx-wins idx)]
                                    (when (pos? points)
                                      [idx points])))))
            card-cnt (+ card-cnt (reduce + (map second idxs+pts)))
            new-idxs (->> idxs+pts
                          (mapcat (fn [[idx points]]
                                    (range (inc idx)
                                           (+ points (inc idx))))))]
        (if (seq new-idxs)
          (recur new-idxs card-cnt)
          card-cnt)))))

(defn part2 []
  (cards-won (utils/aoc-input-lines 4)))
