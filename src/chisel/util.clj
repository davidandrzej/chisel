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


(ns chisel.util
  "Utility function needed to access MALLET protected fields")

(defn get-private-field
  "Access private field of a Java object (credit: kyle smith on Google Groups)"
  [instance field-name]
  (. (doto (first (filter #(.. %1 getName (equals field-name))
                          (.. instance getClass getDeclaredFields)))       
       (.setAccessible true))
     (get instance)))
