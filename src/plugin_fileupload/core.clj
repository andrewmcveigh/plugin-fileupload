(ns plugin-fileupload.core
  (:require
    [plugin-jquery.core :as jquery]
    [clojure.string :as string]
    [clojure.data.json :as json]
    [clojure.java.io :as io]
    [ring.middleware.keyword-params :as kparams])
  (:import
    [java.io FileOutputStream InputStream OutputStream]))

(def css
  [:link {:rel "stylesheet"
          :href "/plugins/fileupload/css/jquery.fileupload-ui.css"}])

(defn js [& {:keys [angular audio iframe-transport image jquery jquery-ui
                    process validate video]}]
  (let [base #(format "/plugins/fileupload/js/%s" %)
        fileupload #(vector
                      :script
                      {:src (format "/plugins/fileupload/js/jquery.fileupload-%s.js" %)})]
    (remove
      nil?
      [(when jquery jquery/js)
       (when jquery-ui [:script {:src (base "vendor/jquery.ui.widget.js")}])
       [:script {:src "/plugins/fileupload/js/jquery.fileupload.js"}] 
       (when angular (fileupload "angular"))
       (when audio (fileupload "audio"))
       (when iframe-transport (fileupload "iframe-transport"))
       (when image (fileupload "image"))
       (when process (fileupload "process"))
       (when validate (fileupload "validate"))
       (when video (fileupload "video"))])))

(defn ->map [path {:keys [params multipart-params] :as request}]
  (let [multipart-params (#'kparams/keyify-params multipart-params)
        multipart? (> (count multipart-params) 0)
        _ (prn params)
        {{filename :filename} "files[]"
         :as params} (if multipart? multipart-params params)
        filename (last (string/split filename #"\\"))]
    {:body (if multipart? (:tempfile (params "files[]")) (:body request))
     :chunk-index (Integer. (params "chunk" 0))
     :params (assoc-in params [:files :filename] filename)
     :path (str path "/" filename)}))

(defn handle-file [{:keys [body path chunk-index]}]
  (io/copy (try (cast InputStream body) (catch ClassCastException _ body))
           (cast OutputStream
                 (FileOutputStream. path (not= 0 chunk-index)))))

(defn handler
  "Saves uploaded file to \"path\" location, from request."
  [path request]
  (handle-file (->map path request)))

(defn response-success-merge [m]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body (json/write-str (merge {:success true} m))})
