#!/usr/bin/env bb
(require '[babashka.curl :as curl]
         '[cheshire.core :as json])

(def token (System/getenv "OS_TOKEN"))

(defn keep-email [user] user)

(defn real-email [user]
  (let [email (-> (curl/get (str "https://iam.eu-de.otc.t-systems.com/v3.0/OS-USER/users/" (:id user))
                            {:headers {"Content-type" "application/json"
                                       "X-Auth-Token" token}})
                  :body
                  (json/parse-string keyword)
                  (get-in [:user :email]))]
    (assoc user :email email)))

(defn get-users []
  (-> (curl/get "https://iam.eu-de.otc.t-systems.com/v3/users"
                {:headers {"Content-type" "application/json"
                           "X-Auth-Token" token}})
      :body
      (json/parse-string keyword)))

(defn email-dupes [body]
  (let [emails-grps (->> (:users body)
                         (map real-email)
                         (filter :email)
                         (group-by :email))
        emails-grps-lim (select-keys emails-grps (for [[k v] emails-grps :when (<= 2 (count v))] k))]
    (json/generate-string
     (into {} (for [[k v] emails-grps-lim] [k (#(map :name %1) v)])))))

(defn -main[&args]
  (-> (json/parse-stream *in* keyword)
      email-dupes
      println))

(when (= *file* (System/getProperty "babashka.file"))
  (-main *command-line-args*))

;; REPL
(comment
  (with-redefs [token "MIIF7wYJKoZIhvcNAQcCoIIF4DCCBdwCAQExDTALBglghkgBZQMEAgEwggN0BgkqhkiG9w0BBwGgggNlBIIDYXsidG9rZW4iOnsiZXhwaXJlc19hdCI6IjIwMjAtMTItMDFUMjA6MDE6MDEuMjAwMDAwWiIsIm1ldGhvZHMiOlsidG9rZW4iXSwiY2F0YWxvZyI6W10sImRvbWFpbiI6eyJ4ZG9tYWluX3R5cGUiOiJUU0kiLCJuYW1lIjoiT1RDLUVVLURFLTAwMDAwMDAwMDAxMDAwMDE5NTUwIiwiaWQiOiI1NTUxNjc1ZDMyZTA0YWE0OGZhMzdjYjk0NzEwYTU3NSIsInhkb21haW5faWQiOiIwMDAwMDAwMDAwMTAwMDAxOTU1MCJ9LCJyb2xlcyI6W3sibmFtZSI6InNlY3VfYWRtaW4iLCJpZCI6IjAifSx7Im5hbWUiOiJ0ZV9hZ2VuY3kiLCJpZCI6IjAifSx7Im5hbWUiOiJ0ZV9hZG1pbiIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2NjZV9zd2l0Y2giLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF90cmF1cnVzX21jcyIsImlkIjoiMCJ9XSwiaXNzdWVkX2F0IjoiMjAyMC0xMS0zMFQyMDowMTowMS4yMDAwMDBaIiwidXNlciI6eyJPUy1GRURFUkFUSU9OIjp7ImlkZW50aXR5X3Byb3ZpZGVyIjp7ImlkIjoiaGVybWVzLXNzby1kZXYifSwicHJvdG9jb2wiOnsiaWQiOiJzYW1sIn0sImdyb3VwcyI6W3sibmFtZSI6ImRldi1hZG1pbiIsImlkIjoiZjFhNjE2NjY2ZmVmNDdlMGI1NDM4ZTI0ODk2MzhkYTEifV19LCJkb21haW4iOnsieGRvbWFpbl90eXBlIjoiVFNJIiwibmFtZSI6Ik9UQy1FVS1ERS0wMDAwMDAwMDAwMTAwMDAxOTU1MCIsImlkIjoiNTU1MTY3NWQzMmUwNGFhNDhmYTM3Y2I5NDcxMGE1NzUiLCJ4ZG9tYWluX2lkIjoiMDAwMDAwMDAwMDEwMDAwMTk1NTAifSwibmFtZSI6IlN0ZWZmYW4sIEFuZHJlYXMiLCJwYXNzd29yZF9leHBpcmVzX2F0IjoiIiwiaWQiOiJEU3JWQWltUjZ3dkdOY0tPWXc4eWxMWURpZTh0NG5wUSJ9fX0xggJOMIICSgIBATCBpDCBljELMAkGA1UEBhMCREUxDDAKBgNVBAgMA05SVzENMAsGA1UEBwwEQm9ubjEcMBoGA1UECgwTRGV1dHNjaGUgVGVsZWtvbSBBRzE3MDUGA1UECwwuVC1TeXN0ZW1zIFBVIFB1YmxpYyBDbG91ZCAtIE9wZW4gVGVsZWtvbSBDbG91ZDETMBEGA1UEAwwKY2EuaWFtLnBraQIJAItDZVC4s9oiMAsGCWCGSAFlAwQCATANBgkqhkiG9w0BAQEFAASCAYClqxgH-qG29uIlxCd5OJ9c+fsA1A3jbXUeh-Ngj2A08hDa6OuKIv9KvOEDRwBeTw2UfEX3NKbQhBmg2RhfsaToO-og19DvYxEbjI9IJ7QBP2f-jBbCFJIhrwancCAAb+GbkOjeIfemEV-cOQbCSSqtRId3RRywp9uM4jgVmN7bjedT0T-1UHHAUZBAW10hRomrHefxlFRPfKpXzbX5KBKRJJw6pFpKgB1Uue0ONHVaWAmsgyz8pXBrwYnb1CNFGLa5zx5XVP9bbWOX8d7HlJN8qUKNhM6+F-sSby+sWt2cz-I6hm-L7zoAl+WgWemMFUTmL78Lo5u-j7WfMeYQa36SORz4lUG52Ww3XrnPULzujKOkqdLwRfDFD9mgdS6K0ds4YtXMhl-DVMbfSFQDT6gB5CUQQHlV9hy9-XUlyCobKvcWvmrk2ZaObwv8C8ccb9uTjwn7bwqrh4dEhs+hz+1vemLmMAj3IoNazsyW8-q1z-QAI4yzsSxESzjxG15SBsc="]
    (-> (get-users)
        email-dupes
        println))
  )