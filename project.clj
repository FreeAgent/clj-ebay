(defproject clj-ebay "0.2.0-SNAPSHOT"
  :description "This is an small Clojure binding for the Ebay APIs for affiliate marketing."
  :url "https://github.com/FreeAgent/clj-ebay"
  :license {:name "Eclipse Public License - v 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo
            :comments "same as Clojure"}
  :dependencies [[org.clojure/clojure "1.2.0"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [clj-http "0.1.3"]]
  :dev-dependencies [[org.clojars.rayne/autodoc "0.8.0-SNAPSHOT"]]
  :autodoc {:name "clj-ebay"
            :description "This is an small Clojure binding for the Ebay APIs for affiliate marketing."
            :copyright "Copyright 2011 Eduardo Julian"
            :web-src-dir "http://github.com/FreeAgent/clj-ebay/src/"
            :web-home "https://github.com/FreeAgent/clj-ebay"
            :output-path "autodoc"}
)

