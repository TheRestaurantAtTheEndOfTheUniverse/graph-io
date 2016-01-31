(ns converisgraphs.graphml
  (:gen-class))

(defn create-node [node]
  (str "<node id=\"" (:id node) "\"/>\n")
)

(defn create-edge [edge]
  (let [source (:from edge)
        target (:to edge)
        name (:label edge)]
    (str "<edge source="\" source "\" target=\"" target "\">\n"
         "<data key=\"edgelabel\">"
         name
         "</data>\n"
         "</edge>\n")))

(defn create-graph [graph]
  (str "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
       "<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\""  
       " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
       " xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd\">"
       "<graph id=\""
       (:title graph)
       "\" edgedefault=\""
       (if (:directed graph)
         "directed"
         "undirected")
       "\">"

       (apply str (map create-node (:nodes graph)))
       (apply str (map create-edge (:edges graph)))
       "</graph>"
       "</graphml>"
       ))
