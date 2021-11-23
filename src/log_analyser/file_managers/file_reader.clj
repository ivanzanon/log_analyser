(ns log-analyser.file-managers.file-reader 
  (:require [clojure.string :refer [split]]))

(defn valid-file-name?
  "Return true if is a valid file name format and false if it is not."
  [path]
  (and path
       (= (class path) java.lang.String)))

(defn read-file
  "Read a file based in the path and return a array of string"
  [path]
  (when (valid-file-name? path)
    (split (slurp path) #"\n")))
