(ns aoc-2023.day5
  (:require [aoc-2023.utils :as utils]
            [clojure.string :as str]))

(defn- convert [alma from to n]
  (->> (alma [from to])
       (reduce (fn [m [dest start cnt]]
                 (if (<= start m (+ start cnt))
                   (reduced (+ dest (- m start)))
                   m))
               n)))

(defn- seed-location [n steps alma]
  (loop [n    n
         kind :seed]
    (if (= :location kind)
      n
      (let [kind' (steps kind)
            n'    (convert alma kind kind' n)]
        (recur n' kind')))))

(defn min-seed [lines]
  (let [[seeds & rst] lines

        seeds (->> (-> seeds
                       (str/split #": ")
                       (second)
                       (str/split #" "))
                   (mapv parse-long))
        alma  (->> rst
                   (partition-by #{""})
                   (remove #{'("")})
                   (map (fn [[label & cnts]]
                          [(->> (-> label
                                    (str/split #" ")
                                    (first)
                                    (str/split #"-to-"))
                                (mapv keyword))
                           (->> cnts
                                (mapv #(str/split % #" "))
                                (mapv (partial mapv parse-long)))]))
                   (into {}))
        steps (->> alma
                   (keys)
                   (into {}))]
    ;;(convert alma :soil :fertilizer 13)
    (->> seeds
         (map #(seed-location % steps alma))
         (apply min))))

(defn part1 []
  (->> (utils/aoc-input-lines 5 #_"sample-almanac")
       (min-seed)))

;;(part1)
;; => 462648396

(defn part2 []
  (->> (utils/aoc-input-lines 5)))
;; => #'aoc-2023.day5/part2
