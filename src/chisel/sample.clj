;; Copyright (c) 2011, Lawrence Livermore National Security, LLC. Produced at
;; the Lawrence Livermore National Laboratory. Written by David Andrzejewski,
;; andrzejewski1@llnl.gov OCEC-10-073 All rights reserved.

;; This file is part of the C-Cat package and is covered under the terms
;; and conditions therein.  See https://github.com/fozziethebeat/C-Cat
;; for details.

;; This code is free software: you can redistribute it and/or modify it
;; under the terms of the GNU General Public License version 2 as
;; published by the Free Software Foundation and distributed hereunder to
;; you.

;; THIS SOFTWARE IS PROVIDED "AS IS" AND NO REPRESENTATIONS OR
;; WARRANTIES, EXPRESS OR IMPLIED ARE MADE. BY WAY OF EXAMPLE, BUT NOT
;; LIMITATION, WE MAKE NO REPRESENTATIONS OR WARRANTIES OF MERCHANT-
;; ABILITY OR FITNESS FOR ANY PARTICULAR PURPOSE OR THAT THE USE OF THE
;; LICENSED SOFTWARE OR DOCUMENTATION WILL NOT INFRINGE ANY THIRD PARTY
;; PATENTS, COPYRIGHTS, TRADEMARKS OR OTHER RIGHTS.

;; You should have received a copy of the GNU General Public License
;; along with this program. If not, see <http://www.gnu.org/licenses/>.


(ns chisel.sample
  "Actual latent z assignments from final Gibbs sample"
  (:require [clojure.string :as str])
  (:use [chisel.util :only (get-private-field)]))

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
