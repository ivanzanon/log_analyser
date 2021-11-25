(ns log-analyser.playground)

(def repo (atom []))

repo

(def report1 {:document 1
             :page 2
             :uid 555
             :start [0 1 2 3]
             :get [0 1 2 3]})

(def report2 {:document 2
              :page 2
              :uid 555
              :start ["a" "b" "c"]
              :get ["a" "b" "c"]})

repo
(swap! repo conj report1 report2)
(conj repo report1 report2)
(assoc report1 :belo 0)

(swap! repo conj {:document 3
            :page 4
            :uid 666})

(conj repo {:document 3
            :page 4
            :uid 666})


@repo
(conj (:start (nth @repo 1)) 2 3)


(swap! repo
       #(-> repo
            (nth ,,, 1)
            :start
            (conj ,,, 2)))


(-> @repo
    (nth ,,, 1)
    :start
    (conj ,,, 2))

@repo

;(swap! repo update-in [0] assoc :id 20)
;(update-in @repo [0] assoc :id 20)

(defn get-index-in-oi
  "Find the index of a map from an array, using :document as a predicate"
  ([list document]
   (let [new-list (first list)
         is-empty-list? (nil? new-list)
         index 0]
     (if (or is-empty-list? (= (:document new-list) document))
       index
       (get-index-in-oi (rest list) document (inc index)))))
  ([list document index]
   (let [new-list (first list)
         is-empty-list? (nil? new-list)]
     (cond is-empty-list? -1
           (= (:document new-list) document) index
           :else (get-index-in-oi (rest list) document (inc index))))))

(swap! repo update-in [(get-index-in-oi @repo 3) :get] conj "Mais um")

(update-in @repo [0 :start] conj "a")

(conj ((nth @repo 1) :start) 2 3)

@repo

(.index @repo {:document 2
                 :page 2
                 :uid 555})



(.indexOf ["one" "two" "three"] "bla")


(conj (:start (first (filter #(= (:document %) 2) @repo))) 25)

@repo

(get-index-in-oi @repo 4)

(empty? (rest [{:a 0 :b 1}]))
(first (rest [{:a 0 :b 1}]))



(assoc {:a "a" :b "b"} :c "c")






(some even? [0 1 2 3 4 5 6])

(some #(:d %) [{:a 22 :b 1 :c 2} {:a 4 :b 5 :c 6}])

(some even? [1 3 5 7 9])

(contains? [{:a 22 :b 1 :c 2} {:a 4 :b 5 :c 6}] {:a 22 :b 1 :c 2})




(defn print-things
  []
  (when false (println "True!"))
  (when true (println "False!")))

(print-things)


(re-matches #"oi.*" "oi gente tudo bem?")
