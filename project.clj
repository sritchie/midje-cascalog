(defproject midje-cascalog "0.3.1-SNAPSHOT"
  :description "Cascalog functions for Midje."
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [cascalog "1.8.4"]
                 [midje "1.3.0"]]
  :dev-dependencies [[org.apache.hadoop/hadoop-core "0.20.2-dev"]
                     [lein-multi "1.1.0-SNAPSHOT"]]
  :multi-deps {"1.9" [[org.clojure/clojure "1.3.0"]
                      [cascalog "1.9.0-wip"]
                      [midje "1.3.0"]]})
