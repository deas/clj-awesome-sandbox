# clj-awesome-sandbox

# Babashka Cheats

```shell script
bb '(json/parse-stream *in* keyword)' <test/sample.json

bb '(->> (json/parse-stream *in* keyword) (reduce-kv #(+ %1 %3) 0)' <test/sample.json

clj -Sdeps '{:deps {cheshire {:mvn/version "RELEASE"}}}' -e "(require '[cheshire.core :as json])(->> (json/parse-stream *in* keyword) (reduce-kv #(+ %1 %3) 0))" <test/sample.json

```

Like `jq`, but awesome:

```shell script
kubectl get pod -A -o json | bb \
 '(->> (json/parse-stream *in* keyword) 
        :items
        (map #(get-in %1 [:spec :containers]))
        flatten
        (map :image)
        distinct)' | jet --pretty

kubectl get pod -A -o yaml | bb \
 '(->> (slurp *in*)
        yaml/parse-string
        :items
        (map #(get-in %1 [:spec :containers]))
        flatten
        (map :image)
        distinct)' | jet --pretty

gcloud organizations get-iam-policy hermesworld.com | bb \
 '(->> (slurp *in*)
        yaml/parse-string
        :bindings
        first
        )' | jet --pretty

```

```clojure
(def stuff (->> (shell/sh "gcloud" "organizations" "get-iam-policy" "hermesworld.com")
                :out
                yaml/parse-string
                :bindings))

(->> (shell/sh "gcloud" "organizations" "get-iam-policy" "hermesworld.com")
     :out
     yaml/parse-string
     :bindings
     (map #(get-in %1 [:spec :containers]))
     flatten
     (map :image)
     distinct) 
```


```clojure
(->> (shell/sh "kubectl" "get" "pod" "-A" "-o" "yaml")
     :out
     yaml/parse-string
     :items
     (map #(get-in %1 [:spec :containers]))
     flatten
     (map :image)
     distinct) 
```

## Usage

FIXME

## TODO
- Should probably swallow clj-infra-sandbox

## License

Copyright © 2020 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
