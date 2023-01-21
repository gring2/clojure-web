(ns practicalli.full-stack-app.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[full-stack-app started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[full-stack-app has shut down successfully]=-"))
   :middleware identity})
