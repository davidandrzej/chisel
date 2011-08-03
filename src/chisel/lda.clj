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
