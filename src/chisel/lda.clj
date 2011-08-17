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


(ns chisel.lda
  "Run LDA inference and write out results"
  (:import (java.io File ObjectOutputStream FileOutputStream))
  (:import (cc.mallet.topics ParallelTopicModel)))

(defn run-lda
  "Train a ParallelTopicModel from a given InstanceList with named
arguments for all LDA parameters"
  [instancelist & {:keys [T numiter topwords
                          showinterval numthreads
                          optinterval optburnin
                          usesymmetricalpha alpha beta
                          thetathresh thetamax]
                   :or {T 50 numiter 500
                        topwords 20 showinterval 50 
                        numthreads
                        (dec (.availableProcessors
                              (Runtime/getRuntime)))
                        optinterval 25 optburnin 200
                        usesymmetricalpha false
                        alpha 50.0 beta 0.01
                        thetathresh 0.0 thetamax -1}}]
  (doto (new ParallelTopicModel T alpha beta)
    (.addInstances instancelist)
    (.setTopicDisplay showinterval topwords)
    (.setNumIterations numiter)        
    (.setOptimizeInterval optinterval)
    (.setBurninPeriod optburnin)
    (.setSymmetricAlpha usesymmetricalpha)                
    (.setNumThreads numthreads)
    (.estimate)))

(defn write-topic-word
  "Write out topic-word weights (propto phi_z=P(w|z))"
  [topicmodel outfile]
  (.printTopicWordWeights topicmodel (new File outfile)))

(defn write-document-topic
  "Write out document-topic probabilities theta_d=P(z|d)"
  [topicmodel outfile]
  (.printDocumentTopics topicmodel (new File outfile)))

(defn write-topics
  "Write out top N words for each topic"
  [topicmodel outfile & {:keys [topwords] :or {topwords 20}}]
  (.printTopWords topicmodel (new File outfile) topwords false))

(defn write-sample
  "Write out top N words for each topic"
  [topicmodel outfile]  
  (.printState topicmodel (new File outfile)))

(defn write-documents
  "Write out InstanceList to MALLET-ready input file"
  [instancelist outfile]  
  (doto (ObjectOutputStream. (FileOutputStream. outfile))
    (.writeObject instancelist)
    (.close)))
