(ns project.core-test
  (:require [clojure.test :refer :all]
            [project.core :refer :all]))

(def single-toot (
 list {:id 4 :url "https://google.com" :other "property" }
))

(def multiple-toots (
 list
   {:id 4 :url "https://google.com" :other "property" }
   {:id 6 :url "https://example" :ignore "this" }
   {:id 3 :url "https://last.fm" :no "good" }
))

(deftest extract-details-test
  (testing "empty list when empty"
    (is (empty? (extract-details []))))
  (testing "strips unrequired properties"
    (is (=
         (first (extract-details single-toot))
         {:id 4 :url "https://google.com"})))
  (testing "strips from multiple toots"
    (is (=
         (extract-details multiple-toots)
         (list
           {:id 4 :url "https://google.com"}
           {:id 6 :url "https://example"}
           {:id 3 :url "https://last.fm"})))))
