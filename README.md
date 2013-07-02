# plugin-fileupload

jQuery-File-Upload packaged as a Servlet 3.0 plugin. With a couple of
clojure/hiccup helpers.


## Dependencies

```clojure
[com.andrewmcveigh/plugin-fileupload "0.1.0"]
```

## Usage

```clojure
> (require '[plugin-fileupload.core :as fileupload])

> fileupload/css
[:link {:href "/plugins/fileupload/css/jquery.fileupload-ui.css", :rel "stylesheet"}]

> (fileupload/js)
([:script {:src "/plugins/fileupload/js/jquery.fileupload.js"}])

> (fileupload/js :angular true
                 :audio true
                 :iframe-transport true
                 :image true
                 :jquery true
                 :jquery-ui true
                 :process true
                 :validate true
                 :video true)
([:script {:src "/plugins/jquery/js/jquery.min.js"}]
 [:script {:src "/plugins/fileupload/js/vendor/jquery.ui.widget.js"}]
 [:script {:src "/plugins/fileupload/js/jquery.fileupload.js"}]
 [:script {:src "/plugins/fileupload/js/jquery.fileupload-angular.js"}]
 [:script {:src "/plugins/fileupload/js/jquery.fileupload-audio.js"}]
 [:script {:src "/plugins/fileupload/js/jquery.fileupload-iframe-transport.js"}]
 [:script {:src "/plugins/fileupload/js/jquery.fileupload-image.js"}]
 [:script {:src "/plugins/fileupload/js/jquery.fileupload-process.js"}]
 [:script {:src "/plugins/fileupload/js/jquery.fileupload-validate.js"}]
 [:script {:src "/plugins/fileupload/js/jquery.fileupload-video.js"}])
```


## License

Copyright © 2013 Andrew Mcveigh

Distributed under the Eclipse Public License, the same as Clojure.
