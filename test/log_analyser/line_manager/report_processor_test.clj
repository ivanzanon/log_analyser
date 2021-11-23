(ns log-analyser.line-manager.report-processor-test
  (:require [clojure.test :refer [deftest testing is run-tests]]
            [log-analyser.report-processor :as rp]))

(deftest test-get-index-in
  (let [list [{:a 000
               :b 111
               :c 222}
              {:a 333
               :b 444
               :c 555}]]
    (testing "If I can discover an index on a list from a predicate"
      (is (= (rp/get-index-in #(= (:a %) 0) list) 0))
      (is (= (rp/get-index-in #(= (:a %) 333) list) 1))
      (is (= (rp/get-index-in #(= (:a %) 444) list) -1))
      (is (= (rp/get-index-in #(nil? (:d %)) list) 0))))
  (let [list [{:a 000
               :b 111
               :c 222}]]
    (testing "If I can discover an index on a list with one element from a predicate"
      (is (= (rp/get-index-in #(= (:a %) 0) list) 0))
      (is (= (rp/get-index-in #(= (:b %) 111) list) 0))
      (is (= (rp/get-index-in #(= (:a %) 444) list) -1))
      (is (= (rp/get-index-in #(nil? (:d %)) list) 0)))))

(deftest set-start-rendering-test
  (testing "Insertion of Start Rendering information on the report"
    (testing "when the list is empty"
      (let [list (atom {:report []})
            line {:type :start-rendering-line
                  :id "115392"
                  :page "0"
                  :time "2010-10-06 09:04:27,725"}]
        (is (= (rp/put-start-rendering! list line)
               {:report [{:document "115392"
                          :page "0"}]}))))
    (testing "when the list has data"
      (let [list (atom {:report [{:document "115392"
                          :page "0"}]})
            line {:type :start-rendering-line
                  :id "115395"
                  :page "1"
                  :time "2010-10-06 09:04:27,725"}]
        (is (= (rp/put-start-rendering! list line)
               {:report [{:document "115392"
                          :page "0"}
                         {:document "115395"
                          :page "1"}]}))))))

(deftest get-rendering-test
  (testing "Insertion of Get Rendering information on the report"
    (testing "when the list is empty"
      (let [list (atom {:report []})
            line {:type :get-rendering-line
                  :uid "115392"
                  :time "2010-10-06 09:04:27,725"}]
        (is (nil? (rp/put-get-rendering! list line)))))
    (testing "when the list has data with id"
      (let [list (atom {:report [{:document "115392"
                                  :page "0"
                                  :id "115392"}
                                 {:document "115395"
                                  :page "1"
                                  :id "115393"}]})
            line {:type :get-rendering-line
                  :uid "115392"
                  :time "2010-10-06 09:04:27,725"}]
        (is (= (rp/put-get-rendering! list line)
               {:report [{:document "115392"
                          :page "0"
                          :id "115392"
                          :get '("2010-10-06 09:04:27,725")}
                         {:document "115395"
                          :page "1"
                          :id "115393"}]}))))
    (testing "when the list has data with no id"
      (let [list (atom {:report [{:document "115392"
                                  :page "0"
                                  :id "115392"}
                                 {:document "115395"
                                  :page "1"
                                  :id "115393"}]})
            line {:type :get-rendering-line
                  :uid "115395"
                  :time "2010-10-06 09:04:27,725"}]
        (is (nil? (rp/put-get-rendering! list line)))))))

(run-tests)
