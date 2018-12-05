(defproject edu.unl/norman "1.0.0"
  :description "Norman: A Parallelizing XSLT Processor based on Saxon"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/tools.cli "0.4.1"]
                 [org.clojure/tools.logging "0.4.1"]
                 [clojure-saxon "0.9.4"]
                 [log4j "1.2.17"]]
  :main edu.unl.norman.core
  :aot [edu.unl.norman.core]
  :jar-name "norman.jar"
  :uberjar-name "norman-1.0.0.jar")
