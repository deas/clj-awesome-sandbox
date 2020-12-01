#!/usr/bin/env bb
;; terraform state pull | /home/deas/work/projects/contentreich/clj-awesome-sandbox/assets/otc-gen-otc-tf-resources-min.clj >new/users.tf

(def token (System/getenv "OS_TOKEN"))

(defn real-email [user]
  (let [email (-> (curl/get (str "https://iam.eu-de.otc.t-systems.com/v3.0/OS-USER/users/" (:id user))
                            {:headers {"Content-type" "application/json"
                                       "X-Auth-Token" token}})
                  :body
                  (json/parse-string keyword)
                  (get-in [:user :email]))]
    (assoc user :email email)))

(defn tf-fmt [user]
  (format "resource \"opentelekomcloud_identity_user_v3\" \"%s\" {
  name  = \"%s\"
  email = \"%s\"
}" (:name-hack user) (:name user) (:email user)))

(defn fmt-users [body]
  (->> (:resources body)
       (filter #(= (:type %1) "openstack_identity_user_v3"))
       (map #(-> %1 :instances first :attributes (assoc :name-hack (:name %1))))
       (map real-email)
       (map tf-fmt)
       (str/join "\n\n")))

(-> (json/parse-stream *in* keyword)
    fmt-users
    println)
