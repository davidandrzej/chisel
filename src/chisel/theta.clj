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


(ns chisel.theta
  "Estimation and manipulation of theta=P(z|d)"
  (:use [chisel.util :only (get-private-field)]))

(defn- get-single-theta
  "Extract a single document theta_d = P(z|d).  Does not currently
include smoothing parameter, but a closer read of MALLET src reveals
they don't either...!"
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
