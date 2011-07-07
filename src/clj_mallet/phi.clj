(ns clj-mallet.phi
  "Estimation and manipulation of phi=P(w|z)"
  (:use [clj-mallet.util :only (get-private-field)]))

(defn- extract-weight
  "Process typeTopicCounts[] (from
ParallelTopicModel.printTopicWordWeights).  Clojure re-write of tricky
MALLET code for extracting both topic index and count from a single
int using bit-twiddling tricks"
  [beta topicmask topicbits topiccounts topic]
  (loop [idx 0
         weight beta]
    (if (or (>= idx (alength topiccounts))
            (<= (aget topiccounts idx) 0))
      weight
      (if (= topic (bit-and (aget topiccounts idx)
                            topicmask))                
        (+ weight (bit-shift-right (aget topiccounts idx)
                                   topicbits))
        (recur (inc idx)
               weight)))))

(defn- get-single-phi
  "Extract a single topic phi_z = P(w | z)"
  [alphabet beta topicmask topicbits typetopiccounts topic]
  (hash-map
   :topic topic
   :words
   (map
    #(hash-map :word (.lookupObject alphabet %1)
               :prob
               (extract-weight beta topicmask topicbits
                               (aget typetopiccounts %1)
                               topic))
    (range (.size alphabet)))))

(defn get-likely-words
  "Given all word-score string pairs, drop out the tied-for-last"
  [records]
  (let [minval (:prob (apply min-key :prob records))]
   (filter #(> (:prob %1) minval) records)))

(defn get-phi
  "Extract/normalize phi values from MALLET ParallelTopicModel"
  [topicmodel]
  (let [alphabet (.getAlphabet topicmodel)
        typetopiccounts (get-private-field topicmodel "typeTopicCounts")
        beta (get-private-field topicmodel "beta")
        topicmask (get-private-field topicmodel "topicMask")
        topicbits (get-private-field topicmodel "topicBits")]
    (map 
       (partial get-single-phi alphabet beta topicmask
                topicbits typetopiccounts)
       (range (.getNumTopics topicmodel)))))

(defn get-likely-phi
  "Wrapper for get-phi that discards low-prob (smoothing only) words"
  [topicmodel]
  (map #(assoc %1 :words (get-likely-words (:words %1)))
       (get-phi topicmodel)))
