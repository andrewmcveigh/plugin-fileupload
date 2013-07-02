(ns plugin-fileupload.core
  (:require
    [plugin-jquery.core :as jquery]))

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
