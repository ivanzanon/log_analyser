(ns log-analyser.file-manager.file-reader-test 
  (:require [clojure.test :refer [testing is deftest]]
            [log-analyser.file-managers.file-reader :refer [read-file valid-file-name?]]))

(deftest file-name-validation
  (testing "File name validation"
    (testing "with nil value"
      (is (not (valid-file-name? nil))))
    (testing "with empty string"
      (is (valid-file-name? "")))
    (testing "with number"
      (is (not (valid-file-name? 000))))))

(deftest file-reader-test
  (spit "resources/test_file.txt" "Line 01\nLine 02\nLine 03")
  (testing "File reader"

    (testing "with invalid parameter"
      (is (thrown? java.io.FileNotFoundException (read-file "")))
      (is (thrown? java.io.FileNotFoundException (read-file "     ")))
      (is (nil? (read-file nil)))
      (is (nil? (read-file 000))))

    (testing "with test_file"
      (is (=
           "Line 01"
           (first (read-file "resources/test_file.txt"))))
      (is (=
           "Line 02"
           (second (read-file "resources/test_file.txt"))))
      (is (=
           "Line 03"
           (nth (read-file "resources/test_file.txt") 2))))))

