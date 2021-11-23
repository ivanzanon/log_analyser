(ns log-analyser.line-manager.line-data-extraction-teste 
  (:require [clojure.test :refer :all]
            [log-analyser.line-manager.line-data-extraction :as lde]
            [log-analyser.line-manager.line-manager :refer [classify-line]]))

(deftest start-rendering-extraction-test
  (let [line "2010-10-06 09:05:54,854 [WorkerThread-11] INFO  [ServiceProvider]: Executing request startRendering with arguments [114273, 1] on service object { ReflectionServiceObject -> com.dn.gaverzicht.dms.services.DocumentService@4a3a4a3a }"]
    (testing "Extracting data from a Start Rendering line"
      (is (=
           {:type :start-rendering-line
            :id "114273"
            :page "1"
            :time "2010-10-06 09:05:54,854"}
           (lde/extract-data-from-line classify-line line))))))

(deftest start-rendering-uid-extraction-test
  (let [line "2010-10-06 09:04:27,729 [WorkerThread-11] INFO  [ServiceProvider]: Service startRendering returned 1286373867729-7370"]
    (testing "Extracting uid from a Start Rendering line"
      (is (=
           {:type :start-rendering-id-line
            :uid "1286373867729-7370"
            :time "2010-10-06 09:04:27,729"}
           (lde/extract-data-from-line classify-line line)))))
  (let [line "2010-10-06 09:04:27,729 [WorkerThread-1] INFO  [ServiceProvider]: Service startRendering returned 1286373867729-7370"]
    (testing "Extracting uid from a Start Rendering line where thread number has one digit"
      (is (=
           {:type :start-rendering-id-line
            :uid "1286373867729-7370"
            :time "2010-10-06 09:04:27,729"}
           (lde/extract-data-from-line classify-line line))))))

(deftest get-rendering-uid-extraction-test
  (let [line "2010-10-06 09:05:55,954 [WorkerThread-15] INFO  [ServiceProvider]: Executing request getRendering with arguments [1286373954922-6090] on service object { ReflectionServiceObject -> com.dn.gaverzicht.dms.services.DocumentService@4a3a4a3a }"]
    (testing "Extracting uid from a Get Rendering line"
      (is (=
           {:type :get-rendering-line
            :uid "1286373954922-6090"
            :time "2010-10-06 09:05:55,954"}
           (lde/extract-data-from-line classify-line line))))))

(run-tests)