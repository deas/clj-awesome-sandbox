#!/usr/bin/env bb

(def token (System/getenv "OS_TOKEN"))

(defn real-email [user]
  (let [email (-> (curl/get (str "https://iam.eu-de.otc.t-systems.com/v3.0/OS-USER/users/" (:id user))
                            {:headers {"Content-type" "application/json"
                                       "X-Auth-Token" token}})
                  :body
                  (json/parse-string keyword)
                  (get-in [:user :email]))]
    (assoc user :email email)))

(defn email-dupes [body]
  (let [emails-grps (->> (:users body)
                         (map real-email)
                         (filter :email)
                         (group-by :email))
        emails-grps-lim (select-keys emails-grps (for [[k v] emails-grps :when (<= 2 (count v))] k))]
    (json/generate-string
     (into {} (for [[k v] emails-grps-lim] [k (#(map :name %1) v)])))))

(-> (json/parse-stream *in* keyword)
    email-dupes
    println)