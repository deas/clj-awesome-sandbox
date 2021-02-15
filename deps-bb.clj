{;; :paths ["src"],
 :deps {org.clojure/clojure {:mvn/version "1.10.2"},
        ;; babashka/babasha.curl {:local/root "babashka.curl"}
        org.clojure/core.async {:mvn/version "1.3.610"},
        org.clojure/tools.cli {:mvn/version "1.0.194"},
        org.clojure/data.csv {:mvn/version "1.0.0"},
        cheshire/cheshire {:mvn/version "5.10.0"}
        org.clojure/data.xml {:mvn/version "0.2.0-alpha6"}
        clj-commons/clj-yaml {:mvn/version "0.7.106"}
        com.cognitect/transit-clj {:mvn/version "1.0.324"}
        org.clojure/test.check {:mvn/version "1.1.0"}
        nrepl/bencode {:mvn/version "1.1.0"}
        seancorfield/next.jdbc {:mvn/version "1.1.610"}
        org.postgresql/postgresql {:mvn/version "42.2.18"}
        org.hsqldb/hsqldb {:mvn/version "2.5.1"}
        datascript/datascript {:mvn/version "1.0.1"}
        http-kit/http-kit {:mvn/version "2.5.1"}
        babashka/clojure-lanterna {:mvn/version "0.9.8-SNAPSHOT"}
        org.clojure/math.combinatorics {:mvn/version "0.1.6"}
        org.clojure/core.match {:mvn/version "1.0.0"}
        hiccup/hiccup {:mvn/version "2.0.0-alpha2"}}
 :aliases {:main
           {:main-opts ["-m" "babashka.main"]}
           ;; :profile
           #_{:extra-deps
            {com.clojure-goes-fast/clj-async-profiler {:mvn/version "0.4.1"}}
            :extra-paths ["test"]
            :jvm-opts ["-Djdk.attach.allowAttachSelf"]
            :main-opts ["-m" "babashka.profile"]}
           }}
