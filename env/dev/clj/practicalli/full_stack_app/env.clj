(ns practicalli.full-stack-app.env
  (:require
    [selmer.parser :as parser]
    [clojure.tools.logging :as log]
    [practicalli.full-stack-app.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[full-stack-app started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[full-stack-app has shut down successfully]=-"))
   :middleware wrap-dev})
