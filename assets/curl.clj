#!/usr/bin/env bb
#_(
   "exec" "bb" "$0" hello "$@"
   )
;; bb --classpath . --main curl
(require '[babashka.curl :as curl]
         '[cheshire.core :as json])

(def endpoint "http://localhost:9876/v3/auth/tokens")


(defn sample-curl [endpoint]
  (curl/get endpoint {:raw-args ["--max-redirs" "0"
                             "-k"
                             "-v"
                             ]
                  :query-params {"q" "clojure"}
                  :basic-auth ["elastic" "password"]
                  ;; :body (io/input-stream "README.md")
                  ;; :form-params {"name" "Michiel"}
                  :throw false
                  :headers {"Accept" "application/json"}})
  )

;; We still want the main entrypoint to be compatible with standard clojure
(defn -main
  "Abc"
  [& _args]
  (sample-curl endpoint))

(when (= *file* (System/getProperty "babashka.file"))
  (-main *command-line-args*))

(comment
  (with-redefs [auth-values test-auth-values]
    (-main nil))
  )
