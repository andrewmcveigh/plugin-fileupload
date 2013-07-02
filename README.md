# plugin-fileupload

Select2 3.4.1 packaged as a Servlet 3.0 plugin. With a couple of clojure/hiccup
helpers.


## Dependencies

```clojure
[com.andrewmcveigh/plugin-fileupload "0.1.0"]
```

## Usage

```clojure
> (require '[plugin-fileupload.core :as select2])

> select2/css
[:link {:href "/plugins/select2/select2.css", :rel "stylesheet"}]

> select2/js
([:script {:src "/plugins/jquery/js/jquery.min.js"}]
 [:script {:src "/plugins/select2/select2.min.js"}])

> select2/js-debug ; non-minimized version(s)
([:script {:src "/plugins/jquery/js/jquery.js"}]
 [:script {:src "/plugins/select2/select2.js"}])
```


## License

Copyright © 2013 Andrew Mcveigh

Distributed under the Apache License, Version 2.0, the same as Select2.
