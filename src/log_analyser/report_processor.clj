(ns log-analyser.report-processor 
  (:require [log-analyser.line-manager.line-manager :refer [relevant-line?]]
            [clojure.data.json :as json]
            [clojure.pprint :refer [pprint]]))

(defn get-index-in
  "Find the index of a map from an array, based on the predicate condition"
  ([predicate list]
   (get-index-in predicate list 0))
  ([predicate list index]
   (let [first-element (first list)
         is-empty-list? (nil? first-element)]
     (cond is-empty-list? -1
           (predicate first-element) index
           :else (get-index-in predicate (rest list) (inc index))))))

(defn put-start-rendering
  [report line]
  (let [data {:document (:id line)
              :page (:page line)}]
    (update-in report [:report] conj data)))

(defn put-start-rendering!
  [report line]
    (swap! report put-start-rendering line))


(defn put-start-rendering-new-id
  [report line index-of-line]
  (swap! report
         update-in [:report index-of-line :start]
         conj (:time line))
  (swap! report
         update-in [:report index-of-line]
         assoc :id (:uid line)))

(defn put-start-rendering-existing-id
  [report line index-of-line]
  (swap! report
         update-in [:report index-of-line :start]
         conj (:time line)))
  
(defn put-start-rendering-id!
  [report line]
  (let [index-of-line-with-no-id (get-index-in #(nil? (:id %)) (:report @report))
        index-of-line-same-id (get-index-in #(= (:id %) (:uid line)) (:report @report))]
    (when (>= index-of-line-with-no-id 0) (put-start-rendering-new-id report line index-of-line-with-no-id))
    (when (>= index-of-line-same-id 0) (put-start-rendering-existing-id report line index-of-line-same-id))))

(defn put-get-rendering!
  [report line]
  (let [index-of-line (get-index-in #(= (:id %) (:uid line)) (:report @report))]
    (when (not= index-of-line -1)
      (swap! report
           update-in [:report index-of-line :get]
           conj (:time line)))))

(defn- put-on-report
  "Function that write the information on the report"
  [report line]
  (let [line-type (:type line)]
    (case line-type
      :start-rendering-line (put-start-rendering! report line)
      :start-rendering-id-line (put-start-rendering-id! report line)
      :get-rendering-line (put-get-rendering! report line))))

(defn process-report
  [read-file classify-line extract-data-from-line]
  (let [relevant-lines (filter relevant-line? (read-file "resources/server-t.log"))
        line-extractor (partial extract-data-from-line classify-line)
        individual-lines (map line-extractor relevant-lines)
        report (atom {:report []})]

    (mapv #(put-on-report report %) individual-lines)

    (swap! report assoc :summary {:count (count (:report @report))
                                  :duplicates (count (filter #(> (count (:start %)) 1) (:report @report)))
                                  :unecessary (count (filter #(= (count (:get %)) 0) (:report @report)))})

    (json/write-str @report)))
