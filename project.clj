;;; Copyright (c) 2013 TouchType Ltd. All Rights Reserved.

(defproject org.clojars.allumbra/conf-er "1.0.4bc"
  :description "Simple global configuration library"
  :url "https://github.com/benFlightStats/conf-er"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]]
  :profiles {:dev {:dependencies [[midje "1.5.1"]]
                   :plugins [[lein-midje "3.0.1"]]}})
