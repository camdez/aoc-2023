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

(defn seed-location [n steps alma]
  (loop [n    n
         kind :seed]
    (if (= :location kind)
      n
      (let [kind' (steps kind)
            n'    (convert alma kind kind' n)]
        (recur n' kind')))))

(defn min-seed [lines]
  (let [[seeds & rst] lines

        seed-ranges (->> (-> seeds
                             (str/split #": ")
                             (second)
                             (str/split #" "))
                         (mapv parse-long)
                         (partition 2)
                         #_(mapcat (fn [[start cnt]]
                                     (range start (+ start cnt))))
                         ;;(distinct)
                         )
        alma        (->> rst
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
        steps       (->> alma
                         (keys)
                         (into {}))]
    (->> (for [[start cnt :as r] seed-ranges]
           (do
             (println "processing" r)
             (let [stop (+ start cnt)]
               (loop [n  start
                      mn Integer/MAX_VALUE]
                 (let [loc (seed-location n steps alma)
                       mn' (min loc mn)]
                   (if (= n stop)
                     mn'
                     (recur (inc n) mn')))))))
         (reduce min))))


(defn part1 []
  (->> (utils/aoc-input-lines 5 #_"sample-almanac")
       (min-seed)
       ;;(transduce (map second) +)
       )
  )

;;(part1)
;; => 462648396

(defn part2 []
  (->> (utils/aoc-input-lines 5)))
;; => #'aoc-2023.day5/part2

(defn game [lines]
  (let [[seeds & rst] lines

        seed-ranges (->> (-> seeds
                             (str/split #": ")
                             (second)
                             (str/split #" "))
                         (mapv parse-long)
                         (partition 2))
        alma        (->> rst
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
        steps       (->> alma
                         (keys)
                         (into {}))]
    {:ranges seed-ranges
     :alma alma
     :steps steps}))

(def g (-> (utils/aoc-input-lines 5)
           (game)))

(defn- normalize-ranges [rs]
  (let [rs (->> rs
                (map (fn [[start cnt]]
                       [start (+ start cnt)]))
                (sort-by first)
                vec)
        rs (concat [[Integer/MIN_VALUE (transduce (map first) (completing min) 0 rs)]]
                   rs
                   [[(transduce (map second) (completing max) 0 rs) Integer/MAX_VALUE]])]
       ;; next, fill gaps
    (loop [[x & xs :as xss] rs
           rs'      []
           prev-end nil]
      (if x
        (if (or (nil? prev-end)
                (= (inc prev-end) (first x)))
          (recur xs (conj rs' x) (second x))
          (recur xss (conj rs' [(inc prev-end) (dec (first x))]) (dec (first x))))
        rs'))))

;; Oh, interleave and remove might be an easy way to do this.
;;
;; Or map with a window and some junk on the end or something. idk.

;; These are the seed ranges.  Kind ranges are different.
(normalize-ranges (:ranges g))
