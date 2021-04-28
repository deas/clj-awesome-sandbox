#!/usr/bin/env bb
#_(
   "exec" "bb" "$0" hello "$@"
   )
;; bb --classpath . --main curl
(require '[org.httpkit.client :as http])

(def sample_params {:method :get
                    :url "https://www.google.com"})


(defn measure [params]
  (let [start (System/currentTimeMillis)
        _ @(http/request params)
        stop (System/currentTimeMillis)
        _ (println (java.util.Date.) ": GET " (:url params) ":" (- stop start) "ms")]))

;; We still want the main entrypoint to be compatible with standard clojure
(defn -main
  "Abc"
  [& _args]
  (loop [_ nil]
    (measure sample_params)
    (recur (Thread/sleep 5000))))

(when (= *file* (System/getProperty "babashka.file"))
  (-main *command-line-args*))

(comment
  ;; :basic-auth ["elastic" "password"]
  (measure sample_params)

  ;;(doc http/get)
  #_ (with-redefs [auth-values test-auth-values]
    (-main nil)))
