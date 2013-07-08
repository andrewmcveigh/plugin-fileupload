(ns plugin-fileupload.demo
  (:require
    [plugin-fileupload.core :as fileupload]
    [clojure.string :as string]
    [clojure.java.io :as io]
    [clojure.data.json :as json]
    [clojure.pprint :refer [pprint]]
    [compojure.core :refer [defroutes GET POST]]
    [compojure.handler :as handler]
    [compojure.route :as route]
    [ring.middleware.resource :as resource]
    [ring.middleware.file-info :as file-info]
    [ring.middleware.keyword-params :as kparams]
    [hiccup.core :refer [html]])
  (:import
    [java.io FileOutputStream InputStream OutputStream]))

(defn ->map [path {:keys [params multipart-params] :as request}]
  (let [multipart-params (#'kparams/keyify-params multipart-params)
        multipart? (> (count multipart-params) 0)
        _ (prn params)
        {{filename :filename} "files[]"
         :as params} (if multipart? multipart-params params)
        filename (last (string/split filename #"\\"))]
    {:body (if multipart? (:tempfile (params "files[]")) (:body request))
     :chunk-index (Integer. (params "chunk" 0))
     :params (assoc-in params [:qqfile :filename] filename)
     :path (str path "/" filename)}))

(defn handle-file [{:keys [body path chunk-index]}]
  (io/copy (try (cast InputStream body) (catch ClassCastException _ body))
           (cast OutputStream
                 (FileOutputStream. path (not= 0 chunk-index)))))

(defn handler
  "Saves uploaded file to \"path\" location, from request."
  [path request]
  (handle-file (->map path request)))

(def response-success
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body "{\"success\": true}"})

(defn response-success-merge [m]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body (json/write-str (merge {:success true} m))})

;(def bootstrap-style-block
  ;[:style {:type "text/css"}
   ;" /* Fine Uploader
   ;-------------------------------------------------- */
   ;.qq-upload-list {
   ;text-align: left;
   ;}
   ;/* For the bootstrapped demos */
   ;li.alert-success {
   ;background-color: #DFF0D8;
   ;}
   ;li.alert-error {
   ;background-color: #F2DEDE;
   ;}
   ;.alert-error .qq-upload-failed-text {
   ;display: inline;
   ;}"])



(def test-page
  (html [:html
         [:head
          [:link
           {:rel "stylesheet"
            :href "//netdna.bootstrapcdn.com/twitter-bootstrap/2.3.2/css/bootstrap-combined.min.css"}]
          [:link
           {:rel "stylesheet"
            :href "http://localhost:3000/plugins/fileupload/css/style.css"}]
          fileupload/css
          ]
         [:body
          [:div.container
            [:div.row
             [:div.span12
              [:h1 "test"]]]
           [:div.row
            [:span.btn.btn-success.fileinput-button
             [:i.icon-plus.icon-white] \space
             [:span "Select Files..."]
             [:input#fileupload {:type :file :name "files[]" :multiple true :data-url "/upload"}]]]
           [:div.row
            [:div#progress.progress.progress-success.progress-striped
             [:div.bar]
             ]
            ]
           [:div.row [:div#files.files]]
           ]
          (fileupload/js :jquery true
                         :jquery-ui true
                         :process true
                         :validate true)
          [:script {:src "/js/main.js"}]
          ]]))

(defroutes app-routes
  (GET "/upload" request
       (do (pprint request)
           (prn (slurp (:body request)))
           (prn) response-success))
  (POST "/upload" request (do (pprint request) (prn) (handler "/tmp" request)
                              (response-success-merge
                                {:files (mapv #(hash-map :name (:filename %))
                                              (:files (:params request)))}
                                )
                              ))
  (GET "/" [] test-page)
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      handler/site
      (resource/wrap-resource "META-INF/resources")
      file-info/wrap-file-info))
