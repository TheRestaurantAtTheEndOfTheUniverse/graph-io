(ns graph-io.graphml
  (:gen-class))

(defn create-node [node]
  (str "node [ id " (:id node) "\n"
       "       label \"" (:label node) "\"\n"
       "]"
       )
)

(defn create-edge [edge]
  (let [source (:from edge)
        target (:to edge)
        name (:label edge)]
    (str "edge [ source " source "\n"
         "       target " target "\n"
         "       label " name "\n"
         "]")))

(defn create-graph [graph]
  (assoc {}
    :extension "gml"
    :graph (str "graph [ directed "
       (if (:directed graph)
         "1"
         "0")
       "\n"
       "label \"" (:title graph) "\"\n"
       (apply str (map create-node (:nodes graph)))
       (apply str (map create-edge (:edges graph))))))
