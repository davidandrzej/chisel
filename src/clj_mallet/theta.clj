(ns clj-mallet.theta
  (:use [clj-mallet.util :only (get-private-field)]))

;; Does *not* include smoothing parameter...!
(defn- get-single-theta
  "Extract a single document theta_d = P(z|d)"
  [topicassign]
  (let [counts (frequencies (.. topicassign topicSequence getFeatures))
        doclen (reduce + (map second counts))]      
    (hash-map
     :document (.. topicassign instance getName)
     :topics (map #(hash-map     
                    :topic (first %1)
                    :prob (-> %1 second double (/ ,,, doclen)))
                  counts))))
    
(defn get-theta
  "Extract/normalize theta values from MALLET ParallelTopicModel"
  [topicmodel]
  (map get-single-theta (get-private-field topicmodel "data")))
