
;;; norman.clj 
;;;
;;; Norman: An XSLT processor (based on Saxon) that parallelizes across
;;; n cores.
;;;
;;; Written and maintained by Stephen Ramsay for the Center for
;;; Digital Research in the Humanities at the University of Nebraska-Lincoln.
;;;
;;; Last Modified: Thu Mar 14 13:28:15 CDT 2013
;;;
;;; Copyright © 2012-2013 Board of Regents of the University of Nebraska-
;;; Lincoln (and others).  See COPYING for details.
;;;
;;; Abbot is distributed in the hope that it will be useful, but
;;; WITHOUT ANY WARRANTY; without even the implied warranty of
;;; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See COPYING 
;;; for more details.

(ns edu.unl.norman.core
  (:use clojure.java.io)
  (:use [clojure.tools.logging :only (info error fatal)])
  (:import (java.io File))
  (:gen-class)
	(:require [clojure.tools.cli :as c]
				    [saxon :as sax]
					  [clojure.java.io :as io]))

(def version "0.2.0")

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

(defn apply-stylesheet [stylesheet xml-file]
"Apply stylesheet to individual XML file."
  (with-open [rdr (io/reader xml-file)]
	    (let [xml (sax/compile-xml rdr)] 
			      (stylesheet xml))))

(defn converter [output-dir stylesheet]
    "Returns a function that runs the conversion and writes out the file"
    ; Written as a closure to keep the main convert-files function
    ; uncluttered.  
    (fn [x] (spit (str output-dir (.getName x)) (apply-stylesheet stylesheet x))))

(defn convert-files [{input-dir  :inputdir
                      output-dir :outputdir
                      stylesheet :stylesheet}]
  "Apply the conversion stylesheet to the input files."
	(if stylesheet
    (if (and (.isDirectory (File. input-dir)) (.isDirectory (File. output-dir)))
      (try
        (info "Starting job")
          (let [stylesheet (sax/compile-xslt (slurp stylesheet))
                converter (converter output-dir stylesheet)
                input (input-files input-dir)]
            (doall (map #(future (converter %)) input)))
        (catch Exception ex
          (error ex "There was an error during file processing")))
       (do
         (println "No input/output directories specified (-h for details)")
         (fatal "No input/output directories specified (-h for details)")))
    (do
      (fatal "No stylesheet provided")
      (println "You must pass a stylesheet to norman with the -s switch."))))

(defn -main [& args]
  "Process command-line switches and call main conversion function"
  (if norman-home
    (let [opts (c/cli args
         ["-s" "--stylesheet" "XSLT stylesheet" :default false] 
         ["-i" "--inputdir" "Input directory path" :default (str norman-home "/input")]
         ["-o" "--outputdir" "Output directory path" :default (str norman-home "/output/")]
         ["-h" "--help" "This usage message" :flag true]
         ["-V" "--version" "Show version number" :flag true])
         options (first opts)
         banner  (last opts)]
        (cond
          (:version options)
            (do
              (println (format "Version %s", version))
              (System/exit 0))
          (:help options)
            (println banner)
          :else
            (convert-files options)))
    (do
      (println "Please set NORMAN_HOME to the directory containing the norman jarfile")
      (fatal "NORMAN_HOME not set"))))
