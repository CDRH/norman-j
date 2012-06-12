
;;; norman.clj 
;;;
;;; Norman: An XSLT processor (based on Saxon) that parallelizes across
;;; n cores.
;;;
;;; Written and maintained by Stephen Ramsay for the Center for
;;; Digital Research in the Humanities at the University of Nebraska-Lincoln.
;;;
;;; Last Modified: Mon Jun 11 11:08:26 CDT 2012
;;;
;;; Copyright © 2011-2012 Board of Regents of the University of Nebraska-
;;; Lincoln (and others).  See LICENSE for details.
;;;
;;; Abbot is distributed in the hope that it will be useful, but
;;; WITHOUT ANY WARRANTY; without even the implied warranty of
;;; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See LICENSE
;;; for more details.

(ns edu.unl.norman.core
    (:use clojure.java.io)
  (import
    (java.io File)) 
        (:gen-class))

(require '[clojure.tools.cli :as c]
				 '[saxon :as sax])

(def version "0.0.2")

(def norman-home (System/getenv "NORMAN_HOME"))

(defn has-xml-extension? [file] 
  "Simple substring check for the presence of a .xml extension on
  the filename."
	(let [filename (.getName file)]
		(= ".xml" (.substring filename (.lastIndexOf filename ".")))))


(defn input-files [input-dir]
  "Read input file and do some basic sanity checking."
  ; Apparently, the only truly reliable way to check that a text file
  ; is indeed an XML file is to parse it.  The XML declaration is
  ; optional, and different legal unicode encodings may or may not
  ; have a byte order mark.  The .xml extension is everywhere used
  ; in the specification, but nowhere mandated.
  ;
  ; So we check that the file is, in fact, a file, and demand an .xml
  ; extension.  Notification of more insidious file errors is left to
  ; Saxon itself.
    (let [filters [#(.isFile %)
                                 #(has-xml-extension? %)]]
    (filter (fn [x] (every? #(% x) filters)) (file-seq (File. input-dir)))))

(defn converter [output-dir stylesheet]
    "Returns a function that runs the conversion and writes out the file"
  (let [convert (fn [stylesheet xml-file]
                  (let [xmlfile (sax/compile-xml xml-file)]
                    (stylesheet xmlfile)))]
    ; Written as a clojure to keep the main convert-files function
    ; uncluttered.  
    (fn [x] (spit (str output-dir (.getName x)) (convert stylesheet x)))))

(defn convert-files [{input-dir  :inputdir
                      output-dir :outputdir
                      stylesheet :stylesheet}]
  "Apply the conversion stylesheet to the input files."
    (let [stylesheet (sax/compile-xslt (slurp stylesheet))
          converter (converter output-dir stylesheet)
          input (input-files input-dir)]
        (doall (pmap converter input))))
        ;(doall (pmap converter (input-files input-dir)))))

(defn -main [& args]
  "Process command-line switches and call main conversion function"
  (let [opts (c/cli args
    ["-s" "--stylesheet" "XSLT stylesheet" :default false] 
    ["-i" "--inputdir" "Input directory path" :default (str norman-home "/input")]
    ["-o" "--outputdir" "Output directory path" :default (str norman-home "/output/")]
    ["-h" "--help" "This usage message" :flag true]
    ["-V" "--version" "Show version number" :flag true])
        options (first opts)
        banner  (last opts)]
        (cond
          (:version options) (do
            (println (format "Version %s", version))
            (System/exit 0))
          (:help options) (do
            (println banner))
          :else (convert-files options))))
