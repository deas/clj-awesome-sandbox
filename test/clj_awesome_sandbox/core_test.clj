(ns clj-awesome-sandbox.core-test
  (:require [clojure.test :refer :all]
            [kubernetes-api.core :as k8s]
            [clj-awesome-sandbox.core :refer :all]))

(def chuck "https://192.168.178.52:6443")
;; (def proxy "http://localhost:8001")
(def basic-auth {:username "admin"
                 :password "94a0f6c76ddd9f623b35a7a06865107d"})
;; Authorization: Bearer <token>
;; (def token "ZXlKaGJHY2lPaUpTVXpJMU5pSXNJbXRwWkNJNkluTjNhMmx4U0ZOUU9UUmFWMGxVZVhSUVh5MXpZemQ0YVZWQ1NHZEhNelZtVnpSclZrNXBiRlJLWVhjaWZRLmV5SnBjM01pT2lKcmRXSmxjbTVsZEdWekwzTmxjblpwWTJWaFkyTnZkVzUwSWl3aWEzVmlaWEp1WlhSbGN5NXBieTl6WlhKMmFXTmxZV05qYjNWdWRDOXVZVzFsYzNCaFkyVWlPaUprWldaaGRXeDBJaXdpYTNWaVpYSnVaWFJsY3k1cGJ5OXpaWEoyYVdObFlXTmpiM1Z1ZEM5elpXTnlaWFF1Ym1GdFpTSTZJbVJsWm1GMWJIUXRkRzlyWlc0dGREWTBaSElpTENKcmRXSmxjbTVsZEdWekxtbHZMM05sY25acFkyVmhZMk52ZFc1MEwzTmxjblpwWTJVdFlXTmpiM1Z1ZEM1dVlXMWxJam9pWkdWbVlYVnNkQ0lzSW10MVltVnlibVYwWlhNdWFXOHZjMlZ5ZG1salpXRmpZMjkxYm5RdmMyVnlkbWxqWlMxaFkyTnZkVzUwTG5WcFpDSTZJakJrTlRGaVpqUTVMVGMyWldFdE5ESmpOUzFoWVdNeExUZGhaakl6WkdSaFptUTFaaUlzSW5OMVlpSTZJbk41YzNSbGJUcHpaWEoyYVdObFlXTmpiM1Z1ZERwa1pXWmhkV3gwT21SbFptRjFiSFFpZlEub2REZ05GQVZYVGt2blFpZkw5bE9iOUl2S0V2Y2FKQUpzTzFNN2g3MUNIRVQwNk1UWVJzR0lQbTlleHRiRG9ZcWtoZHhpSjBnQktwOVlULXlVamFlVEkwbEFYa0tqczdBUklKVWdhaTd2VUVVR01NS3d4dTdiRngyRXF5MVJiX0N1MXJ1Q1ZqVFJlSVQyRUU2RGxkeG5KNDFUVENTbk5ia2RINENNUDFpWmRJdFo5bVlYOS16N1RPYndoQ3hDUWRPUzdPNUJCOUhiZFpxYmJYb2hMdGNhc2FtZWllX0dueUl4X18zWS01dFhBVEhrUHAxSl9sV2hienE0QVIwVzJuamJpYzV1cVVCOEROVTNvbmJfYzVHN3h4TXRCMkZ0YndzN084Nk5tUXFTNlJxclkyNWxrSFpyQV9yVkJyeG5UQ1VHUFVWYjFUbVJtd3lKeFh4U2FBRGhn")

#_(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 1))))

(comment
   ;; {:token token}
  (let [client (k8s/client 
                #_ proxy
                chuck
                #_{:token token}
                {:insecure? true
                 :basic-auth basic-auth}
                )]
    ;;(k8s/explore client)
    (k8s/info client {:kind :Pod} )
    )
  )