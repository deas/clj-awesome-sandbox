#!/usr/bin/env bb
;; OS_USERNAME=steffanand OS_PASSWORD=wont-tell OS_DOMAIN=OTC-EU-DE-00000000001000019548 OS_PROJECT_ID=c6a207a392ce480cb7c163be66472456 bb
;; bb --classpath . --main scratch
(ns scratch
  (:require [babashka.curl :as curl]
            [cheshire.core :as json]))

(def auth-endpoint "https://iam.eu-de.otc.t-systems.com:443/v3/auth/tokens")

(def security-endpoint "https://iam.eu-de.otc.t-systems.com:443/v3.0/OS-CREDENTIAL/securitytokens")
;; ENDPOINT=https://iam.eu-de.otc.t-systems.com/v3/users
;; ENDPOINT=https://cce.eu-de.otc.t-systems.com/api/v3/projects/${PROJECT_ID}/clusters/${CLUSTER_ID}/clustercert
;; CLUSTER_ID=4f964d04-c2ac-11ea-8b1d-0255ac10166b

(defn create-keys [token]
  (let [body (-> {:auth {:identity {:methods ["token"]
                                    :token   {;; :id token
                                              :duration-seconds 86400}}}}
                 json/generate-string)]
    (curl/post security-endpoint
               {:body    body
                :headers {"Content-type" "application/json"
                          "X-Auth-Token" token
                          }})))

(defn authenticate
  [username password domain project-id]
  (let [body (-> {:auth (conj
                          {:identity {:methods  ["password"]
                                      :password {:user {:name     username
                                                        :password password
                                                        :domain   {:name domain}}}}}
                          (when project-id [:scope {:project {:id project-id}}]))}
                 json/generate-string)]
    (curl/post auth-endpoint
               {:body    body
                :headers {"Content-type" "application/json"}})))

(defn get-token
  [response]
  (-> response :headers (get "x-subject-token")))

(def auth-values [(System/getenv "OS_USERNAME")
                  (System/getenv "OS_PASSWORD")
                  (System/getenv "OS_DOMAIN")
                  (System/getenv "OS_PROJECT_ID")])

(def test-auth-values ["steffanand"
                       "500Enten"
                       "OTC-EU-DE-00000000001000019548"
                       "c6a207a392ce480cb7c163be66472456"])

(defn -main [& _args]
  (let [;; response (apply authenticate auth-values)
        ;;token (get-token response)
        token "MIIGFgYJKoZIhvcNAQcCoIIGBzCCBgMCAQExDTALBglghkgBZQMEAgEwggObBgkqhkiG9w0BBwGgggOMBIIDiHsidG9rZW4iOnsiZXhwaXJlc19hdCI6IjIwMjAtMDgtMDdUMTQ6MjY6MDguOTYxMDAwWiIsIm1ldGhvZHMiOlsidG9rZW4iXSwiY2F0YWxvZyI6W10sImRvbWFpbiI6eyJ4ZG9tYWluX3R5cGUiOiJUU0kiLCJuYW1lIjoiT1RDLUVVLURFLTAwMDAwMDAwMDAxMDAwMDIyMjE2IiwiaWQiOiI5N2I0ODU4OTYyMTY0M2Q1OTZiZTQ5YjMxZWE0ZWY1MSIsInhkb21haW5faWQiOiIwMDAwMDAwMDAwMTAwMDAyMjIxNiJ9LCJyb2xlcyI6W3sibmFtZSI6InNlY3VfYWRtaW4iLCJpZCI6IjAifSx7Im5hbWUiOiJ0ZV9hZ2VuY3kiLCJpZCI6IjAifSx7Im5hbWUiOiJ0ZV9hZG1pbiIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2Vjc19ub3JtYWxfczMiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9jY2Vfc3dpdGNoIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfZWNzLW5vcm1hbF9QaTIiLCJpZCI6IjAifV0sImlzc3VlZF9hdCI6IjIwMjAtMDgtMDZUMTQ6MjY6MDguOTYxMDAwWiIsInVzZXIiOnsiT1MtRkVERVJBVElPTiI6eyJpZGVudGl0eV9wcm92aWRlciI6eyJpZCI6ImtleWNsb2FrcG9jIn0sInByb3RvY29sIjp7ImlkIjoic2FtbCJ9LCJncm91cHMiOlt7Im5hbWUiOiJhZG1pbiIsImlkIjoiYjMzZDNjYThkYmNjNDE2MmFmOTQzZDk0NWU1MzM0ZmUifV19LCJkb21haW4iOnsieGRvbWFpbl90eXBlIjoiVFNJIiwibmFtZSI6Ik9UQy1FVS1ERS0wMDAwMDAwMDAwMTAwMDAyMjIxNiIsImlkIjoiOTdiNDg1ODk2MjE2NDNkNTk2YmU0OWIzMWVhNGVmNTEiLCJ4ZG9tYWluX2lkIjoiMDAwMDAwMDAwMDEwMDAwMjIyMTYifSwibmFtZSI6IlN0ZWZmYW4sIEFuZHJlYXMiLCJwYXNzd29yZF9leHBpcmVzX2F0IjoiIiwiaWQiOiJnSVVpbzJqZHU1aXNGWkZzNU0wVTIxZG94U3E2OHBicCJ9fX0xggJOMIICSgIBATCBpDCBljELMAkGA1UEBhMCREUxDDAKBgNVBAgMA05SVzENMAsGA1UEBwwEQm9ubjEcMBoGA1UECgwTRGV1dHNjaGUgVGVsZWtvbSBBRzE3MDUGA1UECwwuVC1TeXN0ZW1zIFBVIFB1YmxpYyBDbG91ZCAtIE9wZW4gVGVsZWtvbSBDbG91ZDETMBEGA1UEAwwKY2EuaWFtLnBraQIJAItDZVC4s9oiMAsGCWCGSAFlAwQCATANBgkqhkiG9w0BAQEFAASCAYBr35fMxOUjBm6iB0skRaW29jnd2nGmIR-0n2EbOXUpM68sHIdK1udSHhswYM0PDObaijFNpbcUTC0gMkyYbI8ZhGbfxRBoJEIGjZgBjG01LbUso4E02RHZr-9BIfuE2P1WtqWAHh-2ezix2i3NUCNl9sFN3tuaZVfmyluk7LHYWoiDvVOQKzyCrkDp8QTa+inl1m0N5opAlryqX+A9jaDHJ7xhbcxBxl9VQF-Zk4nR-2gCEaxCTZGLRBYxg5z-sO32rtyP-sOX6B3xMuOfFgZqR1jGLd4YQPEsPN2sUlx1j3LWT-c2hGpPaMaXmWWshE3WdCbhVA1biAk1UokjPK50ESPQ-vPNL6jw07qECTdhA9V2LkR1P5JlmVFUX36jdOcM4EdbS8IUgEpoOfYM6i2RX8syEoDESHTDRFP8BHapOh+9PJmMyHEvqd5npe8qHSqByBNWgpYemrBLrtKi+BLE1szAdccGNtqzlAsrDMs8m-BE4uQjaTGc5OBoHUBSooU="
        ]
    (create-keys token)))

;; (-main nil)

(comment
  (with-redefs [auth-values test-auth-values]
    (-main nil)
    #_(let [response (apply authenticate auth-values)]
        (get-token response)
        ))
  )
