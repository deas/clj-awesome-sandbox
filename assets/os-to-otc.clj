#!/usr/bin/env bb
#_("exec" "bb" "$0" "$@")
(require '[cheshire.core :as json]
         '[clojure.java.io :as io])

(defn tf-state-user-attributes
  [rdr]
  (->> (json/parse-stream rdr keyword)
       :resources
       (filter #(= (:type %1) "openstack_identity_user_v3"))
       (map #(-> %1 :instances first :attributes))))

(defn tf-user-fmt
  [fmt users]
  (map #(format fmt (:name %1) (:id %1)) users))

(defn print-tf-users-fmt
  [rdr fmt]
  (->> rdr
       tf-state-user-attributes
       (tf-user-fmt fmt)
       (map println)
       dorun))

(defn -main
  "sneak:
   os-to-otc.clj 'name: %s id: %s' <state.json
   
   import:
   terraform state pull | os-to-otc.clj 'echo terraform import opentelekomcloud_identity_user_v3.%s %s' | sh
   
   remove:
   terraform state pull | os-to-otc.clj 'echo terraform state rm -dry-run openstack_identity_user_v3.%s' | sh"
  [[fmt] & _]
  (print-tf-users-fmt *in* fmt))

(when (= *file* (System/getProperty "babashka.file"))
  (-main *command-line-args*))

;; REPL
(comment
 (-> (io/input-stream "state.json")
     io/reader
     (print-tf-users-fmt "terraform import openstack_identity_user_v3.%s %s"))
  )
