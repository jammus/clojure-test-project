(ns project.core
  (:require [clj-http.client :as client])
  (:require [cheshire.core :refer [generate-string]])
  (:gen-class))

(defn fetch-json
  ([url]
  (fetch-json url {}))
  ([url, options]
  (:body (client/get url (into {:as :json} options)))))

(defn fetch-timeline 
  [] 
  (fetch-json "https://mastodon.return12.net/api/v1/timelines/public"))

(defn extract-details
  [toots]
  (map #(select-keys % [:id :url]) toots))

(defn write-json
  [filename data]
  (spit filename (generate-string data)))

(def write-details-json
  (partial write-json "details.json"))

(defn -main
  []
  (->> (fetch-timeline)
       (extract-details)
       (write-details-json)))
