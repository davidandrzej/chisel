(ns test-lda
  "Barebones test of the basic LDA wrapper"
  (:use chisel.instances)
  (:use chisel.lda)
  (:use chisel.phi)
  (:use chisel.theta)
  (:use [clojure.test :only (testing deftest is are use-fixtures)]))

(defn make-wordprob
  "Add :wordprob field containing the probability of word in this topic"
  [word topic]
  (assoc topic
    :wordprob (:prob (first (filter #(= word (:word %1)) (:words topic))))))
                             
(defn get-max-topic
  "Which topic places the largest probability on this word?"
  [phi word]
  (:topic (apply max-key :wordprob (map (partial make-wordprob word) phi))))
                   
(def docs {"doc1" "cat cat bat bat"
           "doc2" "cat cat bat bat dog dog"
           "doc3" "dog dog dog"})

(deftest simpletest
  "Run LDA with T=2 topics on tiny dataset, should get (cat,bat) topic and (dog) topic"
  (let [inst (get-instance-list docs)
        tm (run-lda inst :T 2 :numiter 50 :topwords 3 :alpha 0.5)
        phi (get-phi tm)
        catmax (get-max-topic phi "cat")
        batmax (get-max-topic phi "bat")
        dogmax (get-max-topic phi "dog")]
    (is (and (= catmax batmax)
             (not (= catmax dogmax))))))   
