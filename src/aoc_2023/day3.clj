(ns aoc-2023.day3
  (:require [aoc-2023.utils :as utils]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Part 1
;;
;; Given a grid...
;;
;; 467..114..
;; ...*......
;; ..35..633.
;; ......#...
;; 617*......
;; .....+.58.
;; ..592.....
;; ......755.
;; ...$.*....
;; .664.598..
;;
;; ...find all "part numbers"--numbers where any digit is adjacent to
;; a symbol (in any direction)--and compute their sum.

(def digit? (comp boolean (set (apply str (range 10)))))
(def sym?   (complement (some-fn digit? #{\.})))

(def neighbor-offsets
  (for [x-off [-1 0 1]
        y-off [-1 0 1]
        :when (not= 0 x-off y-off)]
    [x-off y-off]))

;; Will generate off-grid coordinates--ignore nil lookups.
(defn- neighbor-coords [coords]
  (mapv (partial mapv +) (repeat coords) neighbor-offsets))

(defn- make-grid [lines]
  (->> (for [[y line] (map-indexed vector lines)
             [x ch]   (map-indexed vector line)]
         [[x y] ch])
       (into {})))

(defn- grid-size [grid]
  (->> grid keys sort last (mapv inc)))

;; Locs - positions with character values

(defn- grid-loc [grid coords]
  (when-let [ch (grid coords)]
    {:coords coords :ch ch}))

(defn- grid-loc-rows [grid]
  (let [[width height] (grid-size grid)]
    (for [y (range height)]
      (for [x (range width)]
        (grid-loc grid [x y])))))

;; Runs - sequences of horizontally-adjacent locs

(defn- digit-runs [grid]
  (->> (grid-loc-rows grid)
       (mapcat (partial partition-by (comp digit? :ch)))
       (filter (comp digit? :ch first))))

(defn- run-part-num [run]
  (->> run (map :ch) (apply str) parse-long))

(defn- run-neighbor-locs [grid run]
  (->> run
       (mapcat (comp neighbor-coords :coords))
       (distinct)
       (keep (partial grid-loc grid))))

;;;

(defn- part-numbers [grid]
  (->> (digit-runs grid)
       (filter #(some (comp sym? :ch) (run-neighbor-locs grid %)))
       (map run-part-num)))

(defn part1 []
  (->> (utils/aoc-input-lines 3)
       (make-grid)
       (part-numbers)
       (reduce +)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Part 2
;;
;; Find all stars adjacent to exactly two numbers, multiply the pairs
;; of numbers, and sum the results.

(defn- gears-and-parts [grid]
  (->> (digit-runs grid)
       (mapcat (fn [run]
                 (->> (run-neighbor-locs grid run)
                      (filter (comp #{\*} :ch))
                      (map (juxt :coords (constantly (run-part-num run)))))))
       (reduce (fn [m [star-coords part-num]]
                 (update m star-coords conj part-num))
               {})
       (filter #(= 2 (count (second %))))))

(defn part2 []
  (->> (utils/aoc-input-lines 3)
       (make-grid)
       (gears-and-parts)
       (vals)
       (map (partial apply *))
       (reduce +)))
