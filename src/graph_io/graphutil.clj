(ns graph-io.graphutil
  (:gen-class)

  (:require [clojure.java.jdbc :as sql])
  (:require [clojure.pprint :as pprint])
  (:require [clojure.math.numeric-tower :as math])
  (:require clojure.set)
  (:require clojure.string)
)

;; Create a node
(defn make-node [id label & extrainfo]
  {:id id
   :label label
   :properties (first extrainfo)})

;; Create an edge
;; from, to ids of the nodes
;; label of the edge
(defn make-edge [from to label]
  {:from from
   :label label
   :to to})

;; Lookup the color of an iot by name
(defn iot-color [iot colors]
  (get colors iot))

