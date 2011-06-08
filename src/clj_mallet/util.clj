(ns clj-mallet.util)

(defn get-private-field
  "Necessary to access MALLET protected fields
(hacked in from kyle smith via a Google Groups discussion)"
  [instance field-name]
  (. (doto (first (filter #(.. %1 getName (equals field-name))
                          (.. instance getClass getDeclaredFields)))       
       (.setAccessible true))
     (get instance)))
