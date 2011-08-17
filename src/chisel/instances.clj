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


(ns chisel.instances
  "Convert docID->text hashmap to MALLET input format (InstanceList)"
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
