(defproject midje-cascalog "0.4.0"
  :description "Cascalog functions for Midje."
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [midje "1.3.0"]]
  :dev-dependencies [[org.apache.hadoop/hadoop-core "0.20.2-dev"]
                     [cascalog "1.8.5"]
                     [lein-multi "1.1.0-SNAPSHOT"]
                     [org.clojure/math.combinatorics "0.0.2"]]
  :multi-deps {"1.8" [[org.clojure/clojure "1.3.0"]
                      [cascalog "1.8.5"]
                      [midje "1.3.0"]]
               "1.9" [[org.clojure/clojure "1.3.0"]
                      [cascalog "1.9.0-wip"]
                      [midje "1.3.0"]]})
