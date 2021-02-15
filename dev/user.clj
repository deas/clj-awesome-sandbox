(ns user
  (:require [clojure.string :as str]
            [clojure.java.shell :as shell]
            [cheshire.core :as json]))

;; https://github.com/babashka/babashka
;; bb --nrepl-server
;; jack in
;; 🚀
(comment
  (ns-aliases 'user)

  (def roles
    (-> (shell/sh "gcloud" "iam" "roles" "list" "--format" "json")
        :out
        (json/parse-string keyword)))

  (let [sa-exlusions ["roles/accesscontextmanager.policyAdmin"
                      "roles/compute.networkAdmin"
                      "roles/compute.xpnAdmin"
                      "roles/iam.securityAdmin"
                      "roles/iam.serviceAccountAdmin"
                      "roles/logging.configWriter"
                      "roles/resourcemanager.projectCreator"
                      "roles/resourcemanager.folderAdmin"]
        role-names (->> roles
                        (filter #(and
                                  (str/includes? (:name %) "dmin")
                                  (not (str/includes? (:name %) "org"))
                                  (not (contains? sa-exlusions (:name %)))))
                        (map :name)
                        sort
                        )]
    ;; role-names
    (->
     (shell/sh "jq" "." :in (json/generate-string role-names {:pretty true}))
     :out
     println)

    ;; (println (json/generate-string role-names {:pretty true})))
    )
    ;; (sort ["x" "a" "z"])
    )