#!/usr/bin/env bb
(let [emails-grps (->> (-> (curl/get "https://iam.eu-de.otc.t-systems.com/v3/users"
                                     {:headers {"Content-type" "application/json"
                                                "X-Auth-Token" (System/getenv "OS_TOKEN")}})
                           :body
                           (json/parse-string keyword)
                           :users)
                       (filter :email)
                       (group-by :email))
      emails-grps-lim (select-keys emails-grps (for [[k v] emails-grps :when (<= 2 (count v))] k))]
  (println (json/generate-string
            (into {} (for [[k v] emails-grps-lim] [k (#(map :name %1) v)])))))
