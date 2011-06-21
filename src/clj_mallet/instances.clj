;;
;; Convert docID->text hashmap to MALLET input format (InstanceList)
;; 
(ns clj-mallet.instances
  (:import (cc.mallet.types InstanceList Instance))
  (:import (cc.mallet.pipe Pipe
                           SerialPipes
                           CharSequence2TokenSequence
                           TokenSequence2FeatureSequence)))
                                  
(defn document-to-instance
  "Convert a document ID and a text string to a MALLET instance"
  [[documentid text]]
  (Instance. text "nolabel" documentid nil))
                           
(defn get-instance-list
  "Convert (documentID, text) map to MALLET InstanceList"
  [documentmap]
  (let [pipes (new SerialPipes                
                   [(new CharSequence2TokenSequence #"[\p{L}\p{P}]*\p{L}")
                    (new TokenSequence2FeatureSequence)])]                    
    (doto (new InstanceList pipes)
      (.addThruPipe (.iterator (map document-to-instance documentmap)))))) 

(defn get-instance-list-from-iter
  "Convert (documentID, text) map to MALLET InstanceList"
  [instanceiter]
  (let [pipes (new SerialPipes                
                   [(new CharSequence2TokenSequence #"[\p{L}\p{P}]*\p{L}")
                    (new TokenSequence2FeatureSequence)])]                    
    (doto (new InstanceList pipes)
      (.addThruPipe (.iterator instanceiter)))))
