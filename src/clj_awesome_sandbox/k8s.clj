(ns clj-awesome-sandbox.k8s
  (:import (io.fabric8.kubernetes.client DefaultKubernetesClient)
           (io.fabric8.kubernetes.api.model ConfigBuilder))
  (:require [kubernetes-api.core :as k8s-api]))

