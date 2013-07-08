(ns plugin-fileupload.demo
  (:require
    [plugin-fileupload.core :as fileupload
     :refer [handler response-success-merge]]
    [clojure.pprint :refer [pprint]]
    [compojure.core :refer [defroutes GET POST]]
    [compojure.handler :as handler]
    [compojure.route :as route]
    [ring.middleware.resource :as resource]
    [ring.middleware.file-info :as file-info]
    [hiccup.page :refer [html5]]))

(def load-js
  [:script
  "
/*jslint unparam: true */
/*global window, $ */
$(function () {
'use strict';
// Change this to the location of your server-side upload handler:
var url = window.location.hostname === 'blueimp.github.io' ?
'//jquery-file-upload.appspot.com/' : 'server/php/';
$('#fileupload').fileupload({
url: url,
dataType: 'json',
done: function (e, data) {
$.each(data.result.files, function (index, file) {
$('<p/>').text(file.name).appendTo('#files');
});
},
progressall: function (e, data) {
var progress = parseInt(data.loaded / data.total * 100, 10);
$('#progress .bar').css(
'width',
progress + '%'
);
}
})
});
  "])


(def test-page
  (html5
    [:head
     [:link
      {:rel "stylesheet"
       :href "//netdna.bootstrapcdn.com/twitter-bootstrap/2.3.2/css/bootstrap-combined.min.css"}]
     [:link {:rel "stylesheet" :href "/plugins/fileupload/css/style.css"}]
     fileupload/css]
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
        [:div.bar]]]
      [:div.row [:div#files.files]]]
     (fileupload/js :jquery true
                    :jquery-ui true
                    :process true
                    :validate true)
     load-js]))

(defroutes app-routes
  (GET "/upload" request
       (do (pprint request)
           (prn (slurp (:body request)))
           (prn) response-success))
  (POST "/upload" request (do (pprint request) (prn) (handler "/tmp" request)
                              (response-success-merge
                                {:files (mapv #(hash-map :name (:filename %))
                                              (:files (:params request)))})))
  (GET "/" [] test-page)
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      handler/site
      (resource/wrap-resource "META-INF/resources")
      file-info/wrap-file-info))
