#!/usr/bin/env bb
;;  curl -s -H "X-Auth-Token: $OS_TOKEN" -H "Content-Type: application/json" https://iam.eu-de.otc.t-systems.com/v3/users | otc-gen-otc-tf-resources.clj

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
}" (:name user) (:name user) (:email user)))

(defn fmt-users [body]
  (->> (:users body)
       (map real-email)
       (map tf-fmt)
       (str/join "\n\n")))

(-> (json/parse-stream *in* keyword)
    fmt-users
    println)
