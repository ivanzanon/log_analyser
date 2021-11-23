(ns log-analyser.line-manager.line-manager)

(defn line-with-startrendering-info?
  [line]
  (.contains line "Executing request startRendering"))

(defn line-with-startrendering-id-info?
  [line]
  (.contains line "Service startRendering returned"))

(defn line-with-getrendering-info?
  [line]
  (.contains line "Executing request getRendering"))

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

