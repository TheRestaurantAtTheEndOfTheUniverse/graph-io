;; Convert graphs to the dot language
;; see http://www.graphviz.org/

(ns graph-io.dot
  (:gen-class)
  (:require [clojure.pprint :as pprint])
)

;; Create a dot node
;; recognizes the following properties:
;;  :fillcolor background of the node
;;  :linewidth pen width for drawing the node outline
;;  :shape shape of the node
;;  :color textcolor
(defn- create-node [node]
  (let [color (:color (:properties node))
        fillcolor (:fillcolor (:properties node))
        linewidth  (:linewidth (:properties node))
        shape  (:shape (:properties node))
        dot-node (str (:id node) " [label=\"" (:label node)  "\""
                  (if color
                    (str " fontcolor=\"" color "\""))
                  (if fillcolor
                    (str " fillcolor=\"" fillcolor "\" style=\"filled\""))
                  (if linewidth
                    (str " penwidth=\"" linewidth "\""))
                  (if shape
                    (str " shape=\"" shape "\""))
                  "];\n")]
    dot-node)
)

;; Create a dot edge
;; directed defines if the edge is directed
;;   (only forward supported)
(defn create-edge [edge directed]
  (str (:from edge)
       (if directed
         " -> "
         " -- ")
       (:to edge)
       " ["
       (if directed
         "dir=forward,")
       "label=\"" (:label edge) "\"];\n"))

;; Create a valid name for the graph by eliminating
;; all characters that are not letters or digits
(defn- create-graph-name [name]
  (reduce (fn [previous next-char]
            (str previous
                 (if (Character/isLetterOrDigit next-char)
                   next-char)))
          ""
          name))

;; Default graph setup
(defn- create-graph-params [graph]
  (str "graph [label=\"" 
       (:title graph)
       "\",fontname=\"Helvetica\",labelloc=\"top\",labeljust=\"left\",fontsize=\"18\"")
  )

;; Default node setup
(defn- create-node-params [graph]
  "node [shape=\"note\",fontname=\"Helvetica\"]\n"
  )

;; Default edge setup
(defn- create-edge-params [graph]
  "edge [fontsize=\"10\",fontname=\"Helvetica\"]\n"
  )

;; Create nodes and edges
(defn- create-nodes-and-edges [graph]
  (str
   (apply str (map create-node (:nodes graph)))
   (apply str (map create-edge
                   (:edges graph)
                   (repeat (count (:edges graph))
                           (:directed graph)))))
  )

;; Create a subgraph
(defn- create-subgraph [graph]
  (str "subgraph "
       (str "cluster_" (create-graph-name (:title graph))) " {\n"
       "label=\"" (:title graph) "\"\n"
       "color=black\n"
       (create-nodes-and-edges graph)
       "}\n"
       )
  )

;; Create a top level graph
;; also creates all graphs in :subgraphs
;; as cluster subgraphs
(defn create-graph [graph]
  (assoc {}
    :extension "gv"
    :graph (str (if (:directed graph)
         "digraph"
         "graph")
       " " (create-graph-name (:title graph)) " {\n"
       (create-graph-params graph)
       (if (:ratio (:properties graph))
         (str ", ratio=\"" (:ratio (:properties graph)) "\""))        
       "];\n" 
       (create-node-params graph)
       (create-edge-params graph)
       (apply str (map create-subgraph (:subgraphs graph)))
       (create-nodes-and-edges graph)
       "}\n"
       )))
