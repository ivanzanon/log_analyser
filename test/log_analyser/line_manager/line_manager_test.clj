(ns log-analyser.line-manager.line-manager-test
  (:require [clojure.test :refer :all]
            [log-analyser.line-manager.line-manager :as la.line-manager]))

(deftest line-evaluation-test
  (let [start-rendering-line "2010-10-06 09:05:54,854 [WorkerThread-11] INFO  [ServiceProvider]: Executing request startRendering with arguments [114273, 1] on service object { ReflectionServiceObject -> com.dn.gaverzicht.dms.services.DocumentService@4a3a4a3a }"
        start-rendering-id-line "2010-10-06 09:04:27,729 [WorkerThread-11] INFO  [ServiceProvider]: Service startRendering returned 1286373867729-7370"
        get-rendering-line "2010-10-06 09:05:55,954 [WorkerThread-15] INFO  [ServiceProvider]: Executing request getRendering with arguments [1286373954922-6090] on service object { ReflectionServiceObject -> com.dn.gaverzicht.dms.services.DocumentService@4a3a4a3a }"
        invalid-line "line"]

    (testing "Evaluate a line"

      (testing "if it has Start Rendering information"
        (is (la.line-manager/line-with-startrendering-info? start-rendering-line))
        (is (not (la.line-manager/line-with-startrendering-info? invalid-line))))

      (testing "if it has Start Rendering information with id"
        (is (la.line-manager/line-with-startrendering-id-info? start-rendering-id-line))
        (is (not (la.line-manager/line-with-startrendering-id-info? invalid-line))))

      (testing "if it has Get Rendering information"
        (is (la.line-manager/line-with-getrendering-info? get-rendering-line))
        (is (not (la.line-manager/line-with-getrendering-info? invalid-line))))

      (testing "if it is a relevant line"
        (is (la.line-manager/relevant-line? get-rendering-line))
        (is (la.line-manager/relevant-line? start-rendering-line))
        (is (la.line-manager/relevant-line? start-rendering-id-line))
        (is (not (la.line-manager/relevant-line? invalid-line)))))))

(deftest line-classification-test
  (let [start-rendering-line "2010-10-06 09:05:54,854 [WorkerThread-11] INFO  [ServiceProvider]: Executing request startRendering with arguments [114273, 1] on service object { ReflectionServiceObject -> com.dn.gaverzicht.dms.services.DocumentService@4a3a4a3a }"
        start-rendering-id-line "2010-10-06 09:04:27,729 [WorkerThread-11] INFO  [ServiceProvider]: Service startRendering returned 1286373867729-7370"
        get-rendering-line "2010-10-06 09:05:55,954 [WorkerThread-15] INFO  [ServiceProvider]: Executing request getRendering with arguments [1286373954922-6090] on service object { ReflectionServiceObject -> com.dn.gaverzicht.dms.services.DocumentService@4a3a4a3a }"
        invalid-line "line"]

    (testing "Evaluate a line"

      (testing "if is a StartRender line"
        (is (= :start-rendering-line (la.line-manager/classify-line start-rendering-line))))

      (testing "if is a StartRender line with uid"
        (is (= :start-rendering-id-line (la.line-manager/classify-line start-rendering-id-line))))

      (testing "if is a GetRender line"
        (is (= :get-rendering-line (la.line-manager/classify-line get-rendering-line))))

      (testing "if is another line"
        (is (nil? (la.line-manager/classify-line invalid-line)))))))

