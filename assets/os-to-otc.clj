#!/usr/bin/env bb
#_("exec" "bb" "$0" "$@")
(require '[cheshire.core :as json]
         '[clojure.java.io :as io])

(defn tf-state-ressource-attributes
  [ressource rdr]
  (->> (json/parse-stream rdr keyword)
       :resources
       (filter #(= (:type %1) ressource))
       ;; (assoc :name-hack (:name %1))
       (map #(-> %1 :instances first :attributes (assoc :name-hack (:name %1))))))

(defn tf-ressource-fmt
  [fmt ressources]
  (map #(format fmt (:name-hack %1) (:id %1)) ressources))

(defn print-tf-ressources-fmt
  [rdr ressource fmt]
  (->> rdr
       (tf-state-ressource-attributes ressource)
       (tf-ressource-fmt fmt)
       (map println)
       dorun))

;; openstack_identity_project_v3
;; openstack_identity_group_v3
;; openstack_identity_user_v3

(defn -main
  "sneak:
   os-to-otc.clj 'name: %s id: %s' <state.json

   import:
   terraform state pull | os-to-otc.clj openstack_identity_user_v3 'echo terraform import opentelekomcloud_identity_user_v3.%s %s' | sh

   remove:
   terraform state pull | os-to-otc.clj openstack_identity_user_v3 'echo terraform state rm -dry-run openstack_identity_user_v3.%s' | sh"
  [[ressource fmt] & _]
  (print-tf-ressources-fmt *in* ressource fmt))

(when (= *file* (System/getProperty "babashka.file"))
  (-main *command-line-args*))

;; REPL
(comment
  (-> (io/input-stream "state.json")
      io/reader
      (print-tf-users-fmt "terraform import openstack_identity_user_v3.%s %s"))
  )
