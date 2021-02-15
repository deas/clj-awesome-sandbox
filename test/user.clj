(ns user
  (:require [clojure.test :refer :all]
            [clojure.pprint :refer [pprint]]
    ;; [org.httpkit.client :as http]
    ;; [clj-http.lite.client :as http-light]
    ;; [cheshire.core :refer :all]
    ;; [kubernetes-api.internals.martian :as martian-int]
    ;; [kubernetes-api.core :as k8s-api]
    ;; [clj-awesome-sandbox.k8s :refer :all]
    ;; [kubernetes-api.misc :as misc]
            ))

(defn debug-response-for [& args]
  (pprint args))

(comment
  ;; neo4j
  #_(with-open [session (db/get-session local-db)]
      (create-user session {:user {:first-name "Luke" :last-name "Skywalker"}}))
  ;; helm-2.16.5 get devops-pfm-cloud-exporter | less
  ;; kubectl -n pfm-utilities port-forward service/cloud-overview-neo4j 7474:7474 7687:7687
  ;; Using a transaction
  ;; https://git01.int.hlg.de/pfm/misc/pfm-cloud-report-page/tree/master/content/reports/otc
  (require '[neo4j-clj.core :as db])
  (import (java.net URI))
  #_(db/defquery get-all-servers
                 "MATCH (n:CloudServer{})-[]-(project:CloudProject)-[]-(dom:Domain),(n)-[]-(flav:Flavor),(n)-[]-(image:CloudImage) RETURN n.name,flav.name,image.name,project.name,dom.domainId")

  #_(with-open [session (db/get-session local-db)]
      (create-user session {:user {:first-name "Luke" :last-name "Skywalker"}}))
  ;; helm-2.16.5 get devops-pfm-cloud-exporter | less
  ;; kubectl -n pfm-utilities port-forward service/cloud-overview-neo4j 7474:7474 7687:7687
  ;; Using a transaction
  ;; https://git01.int.hlg.de/pfm/misc/pfm-cloud-report-page/tree/master/content/reports/otc
  #_(db/defquery get-all-servers
                 "MATCH (n:CloudServer{})-[]-(project:CloudProject)-[]-(dom:Domain),(n)-[]-(flav:Flavor),(n)-[]-(image:CloudImage) RETURN n.name,flav.name,image.name,project.name,dom.domainId")
  (let [local-db (db/connect (URI. "bolt://10.168.26.210:7687")
                             (System/getenv "NEO4J_USER")
                             (System/getenv "NEO4J_PASSWORD"))
        query (db/create-query "MATCH (n:CloudServer{})-[]-(project:CloudProject)-[]-(dom:Domain),(n)-[]-(flav:Flavor),(n)-[]-(image:CloudImage) RETURN n.name,flav.name,image.name,project.name,dom.domainId")]
    (db/with-transaction local-db tx
                         (println (query tx))
                         ;; (println (get-all-servers tx))
                         ))

  (+ 1 1)

  )

(comment
  ;; otc
  ;; https://docs.otc.t-systems.com/en-us/endpoint/index.html
  (import [com.huawei.openstack4j.api OSClient]
          [com.huawei.openstack4j.openstack OSFactory]
          [com.huawei.openstack4j.model.common Identifier]
          [com.huawei.openstack4j.openstack.vpc.v1.domain Vpc])
  (let [username (System/getenv "OS_USERNAME")
        password (System/getenv "OS_PASSWORD")
        auth-url (System/getenv "OS_AUTH_URL")
        domain-name (System/getenv "OS_DOMAIN_NAME")
        project (Identifier/byName (System/getenv "OS_PROJECT_NAME"))
        os-client (-> (OSFactory/builderV3)
                      (.endpoint auth-url)
                      (.credentials username password (Identifier/byName domain-name))
                      (.scopeToProject project)
                      .authenticate)]
    (->> os-client .vpc .vpcs .list (map bean)))
  )

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
                        sort)]
    ;; role-names
    (->
     (shell/sh "jq" "." :in (json/generate-string role-names {:pretty true}))
     :out
     println)

    ;; (println (json/generate-string role-names {:pretty true})))
    )
    ;; (sort ["x" "a" "z"])
  )
