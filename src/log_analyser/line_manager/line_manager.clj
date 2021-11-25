(ns log-analyser.line-manager.line-manager 
  (:require [clojure.string :refer [includes?]]))

(defn line-with-startrendering-info?
  [line]
  (includes? line "Executing request startRendering"))

(defn line-with-startrendering-id-info?
  [line]
  (includes? line "Service startRendering returned"))

(defn line-with-getrendering-info?
  [line]
  (includes? line "Executing request getRendering"))

(defn relevant-line?
  [line]
  (or 
   (line-with-startrendering-info? line)
   (line-with-startrendering-id-info? line)
   (line-with-getrendering-info? line)))

(defn classify-line
  [line]
  (cond
   (line-with-getrendering-info? line) :get-rendering-line
   (line-with-startrendering-info? line) :start-rendering-line
   (line-with-startrendering-id-info? line) :start-rendering-id-line))

