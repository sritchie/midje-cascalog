(defproject midje-cascalog "0.5.0" 
  :min-lein-version "2.0.0"
  :profiles {:1.9
             {:dependencies
              [[org.clojure/clojure "1.4.0"]
               [cascalog "1.9.0"]
               [midje "1.4.0"]]},
             :1.8
             {:dependencies
              [[org.clojure/clojure "1.3.0"]
               [cascalog "1.8.5"]
               [midje "1.3.0"]]},
             :dev
             {:dependencies
              [[org.apache.hadoop/hadoop-core "0.20.2-dev"]
               [cascalog "1.8.5"]
               [org.clojure/math.combinatorics "0.0.2"]]}}
  :dependencies [[org.clojure/clojure "1.4.0"] [midje "1.4.0"]]
  :plugins [[lein-clojars "0.9.1"]]
  :description "Cascalog functions for Midje.")
