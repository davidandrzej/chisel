(ns clj-mallet.lda
  (:import (java.io File ObjectOutputStream FileOutputStream))
  (:import (cc.mallet.topics ParallelTopicModel)))

;; For now, simply hard-code all these parameters...
;; TODO: fixme
(def topwords 20)
(def showinterval 50)
(def numthreads (dec (.availableProcessors (Runtime/getRuntime))))
(def optimizeinterval 25)
(def optimizeburnin 200)
(def usesymmetricalpha false)
(def alpha 50.0)
(def beta 0.01)
(def thetathresh 0.0)
(def thetamax -1) 
            
(defn run-lda
  "Given an InstanceList, train a ParallelTopicModel"
  [instancelist T numiter]
  (doto (new ParallelTopicModel T alpha beta)
    (.addInstances instancelist)
    (.setTopicDisplay showinterval topwords)
    (.setNumIterations numiter)        
    (.setOptimizeInterval optimizeinterval)
    (.setBurninPeriod optimizeburnin)
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
  [topicmodel outfile]  
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
