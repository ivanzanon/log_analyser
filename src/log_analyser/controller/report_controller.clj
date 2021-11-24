(ns log-analyser.controller.report-controller 
  (:require [log-analyser.report-processor :refer [process-report]]
            [log-analyser.file-managers.file-reader :as fm.file-reader]
            [log-analyser.line-manager.line-manager :as lm.line-manager]
            [log-analyser.line-manager.line-data-extraction :as lm.line-data-extraction]))

(defn get-report
  []
  (->> (process-report fm.file-reader/read-file
                       lm.line-manager/classify-line
                       lm.line-data-extraction/extract-data-from-line)
       (spit "resources/report.json")))