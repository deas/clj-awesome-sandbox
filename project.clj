(defproject clj-awesome-sandbox "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [ring/ring-core "1.8.0"]
                 [ring/ring-jetty-adapter "1.8.0"]
                 [org.martinklepsch/clj-http-lite "0.4.3"]
                 [io.fabric8/kubernetes-client "4.9.0"]
                 ;; [http-kit "2.4.0-alpha6"] ;; java 11
                 ;; [martian-httpkit "0.1.12-SNAPSHOT"]
                 [martian-clj-http-lite "0.1.12-SNAPSHOT"]
                 [nubank/k8s-api "0.1.1-SNAPSHOT"]]
  :profiles {:dev     {:plugins      [[lein-shell "0.5.0"]]
                       :repl-options {:init-ns clj-awesome-sandbox.core-test}
                       ;;:dependencies
                       #_[[org.clojure/tools.namespace "1.0.0"]
                        [org.clojure/tools.trace "0.7.10"]]}
             :uberjar {:aot :all}}
  :aliases {"native"
            ["shell"
             "native-image" "--report-unsupported-elements-at-runtime"
             "--initialize-at-build-time"
             "-jar" "./target/${:uberjar-name:-${:name}-${:version}-standalone.jar}"
             "-H:Name=./target/${:name}"]})
