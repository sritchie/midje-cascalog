(defproject midje-cascalog "0.3.0-SNAPSHOT"
  :description "Cascalog functions for Midje."
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [cascalog "1.8.2"]
                 [midje "1.3-alpha4"]]
  :dev-dependencies [[org.apache.hadoop/hadoop-core "0.20.2-dev"]
                     [lein-midje "1.0.3"]
                     [swank-clojure "1.4.0-SNAPSHOT"]])
