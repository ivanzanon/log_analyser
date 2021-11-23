(ns log-analyser.line-manager.line-data-extraction
  (:require [clojure.string :refer [index-of split trim]]))

(defn- start-index
  [line]
  (inc (index-of line "[" 86)))

(defn- end-index
  [line]
  (index-of line "]" 86))

(defn- infos
  [line]
  (subs line (start-index line) (end-index line)))

(defn- get-time-from-line
  [line]
  (subs line 0 23))

(defn- extract-page-and-id-infos-from-line
  "Extracts page and id from a Start Rendering line"
  [line]
  (let [split-infos (split (infos line) #",")]
    {
     :type :start-rendering-line
     :id (trim (first split-infos))
     :page (trim (second split-infos))
     :time (get-time-from-line line)}))

(defn- extract-start-rendering-uid-from-line
  "Extracts uid from a Start Rendering line"
  [line]
  {:type :start-rendering-id-line
   :uid (subs line (+ (index-of line "startRendering") 24))
   :time (get-time-from-line line)})

(defn- extract-get-rendering-uid-from-line
  "2010-10-06 09:05:55,954 [WorkerThread-15] INFO  [ServiceProvider]: Executing request getRendering with arguments [1286373954922-6090] on service object { ReflectionServiceObject -> com.dn.gaverzicht.dms.services.DocumentService@4a3a4a3a }"
  [line]
  {:type :get-rendering-line
   :uid (infos line)
   :time (get-time-from-line line)})

(defn extract-data-from-line
  [classify-line line]
  (let [functions {:start-rendering-line extract-page-and-id-infos-from-line
                   :start-rendering-id-line extract-start-rendering-uid-from-line
                   :get-rendering-line extract-get-rendering-uid-from-line}]
    (((classify-line line) functions) line)))

