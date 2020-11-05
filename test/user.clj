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
  ;; https://docs.otc.t-systems.com/en-us/endpoint/index.html
  (import [com.huawei.openstack4j.api OSClient]
          [com.huawei.openstack4j.openstack OSFactory]
          [com.huawei.openstack4j.model.common Identifier]
          [com.huawei.openstack4j.openstack.vpc.v1.domain Vpc])
  (let [username (System/getenv "OS_USERNAME")
        password (System/getenv "OS_PASSWORD")
        auth-url (System/getenv "OS_AUTH_URL")
        domain-name (System/getenv "OS_DOMAIN_NAME")
        project (Identifier/byName "eu-de")
        os-client (-> (OSFactory/builderV3)
                      (.endpoint auth-url)
                      (.credentials username password (Identifier/byName domain-name))
                      (.scopeToProject project)
                      .authenticate)]
    (->> os-client .vpc .vpcs .list (map bean)))

  #_(with-open [session (db/get-session local-db)]
      (create-user session {:user {:first-name "Luke" :last-name "Skywalker"}}))
  ;; helm-2.16.5 get devops-pfm-cloud-exporter | less
  ;; kubectl -n pfm-utilities port-forward service/cloud-overview-neo4j 7687:7687
  ;; Using a transaction
  ;; https://git01.int.hlg.de/pfm/misc/pfm-cloud-report-page/tree/master/content/reports/otc
  (require '[neo4j-clj.core :as db])
  (import (java.net URI))
  (db/defquery get-all-servers
               "MATCH (n:CloudServer{})-[]-(project:CloudProject)-[]-(dom:Domain),(n)-[]-(flav:Flavor),(n)-[]-(image:CloudImage) RETURN n.name,flav.name,image.name,project.name,dom.domainId")
  (let [local-db (db/connect (URI. "bolt://localhost:7687")
                             "admin"
                             "admin")]
    (db/with-transaction local-db tx
                         (println (get-all-servers tx))))
  )
