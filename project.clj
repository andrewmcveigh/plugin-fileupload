(defproject com.andrewmcveigh/plugin-fileupload "0.1.0"
  :description "jQuery-File-Upload packaged as a Servlet 3.0 plugin. With a
                couple of clojure/hiccup helpers."
  :url "http://github.com/andrewmcveigh/plugin-fileupload"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [com.andrewmcveigh/plugin-jquery "0.1.0"]
                 [org.clojure/data.json "0.2.2"]
                 [compojure "1.1.5"]
                 [hiccup "1.0.3"]]
  :plugins [[lein-ring "0.8.2"]]
  :ring {:handler plugin-fileupload.demo/app}
  :repositories [["snapshots" {:url "https://clojars.org/repo/" :creds :gpg}]
                 ["releases" {:url "https://clojars.org/repo/" :creds :gpg}]])
