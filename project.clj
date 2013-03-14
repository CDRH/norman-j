(defproject edu.unl/norman "0.2.0"
  :description "Norman: A Parallelizing XSLT Processor based on Saxon"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/tools.cli "0.2.2"]
                 [org.clojure/tools.logging "0.2.6"]
                 [clojure-saxon "0.9.3"]
                 [log4j "1.2.17"]]
  :main edu.unl.norman.core
  :jar-name "norman.jar"
  :uberjar-name "norman-0.2.0.jar")
