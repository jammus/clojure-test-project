(ns project.core
  (:require [clj-http.client :as client]
            [clojure.tools.cli :refer [parse-opts]]
            [cheshire.core :refer [generate-string]])
  (:gen-class))

(defn fetch-json
  ([url]
  (fetch-json url {}))
  ([url, options]
  (:body (client/get url (into {:as :json} options)))))

(defn fetch-timeline
  [domain]
  (fetch-json (str "https://" domain "/api/v1/timelines/public")))

(defn extract-details
  [toots]
  (map #(select-keys % [:id :url]) toots))

(defn write-json
  [filename data]
  (spit filename (generate-string data)))

(def write-details-json
  (partial write-json "details.json"))

(def cli-options
  [["-d" "--domain DOMAIN" "Mastodon domain to query"]
   ["-h" "--help"]])

(defn -main
  [& args]
  (let [{:keys [options summary]} (parse-opts args cli-options)]
    (cond
      (:help options)
      (println (str "Query the supplied Mastodon instance\n" summary))
      (nil? (:domain options))
      (println "Missing domain argument")
      :else
      (->> (fetch-timeline (:domain options))
           (extract-details)
           (write-details-json)))))
