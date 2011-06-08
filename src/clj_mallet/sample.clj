(ns clj-mallet.sample
  (:require [clojure.string :as str])
  (:use [clj-mallet.util :only (get-private-field)]))

(defn- get-single-sample
  "Get the token-sample associations for a single document"
  [alphabet topicassign]
  (let [inst (.instance topicassign)]
    (hash-map
     :document (.getName inst)
     :tokens (map #(.. alphabet (lookupObject %1) toString)
                  (.. inst getData getFeatures))
     :topics (.. topicassign topicSequence getFeatures))))

(defn get-sample
  "Extract the actual topic assignments from the final sample" 
  [topicmodel]
  (map (partial get-single-sample (.getAlphabet topicmodel))
       (get-private-field topicmodel "data") ))
