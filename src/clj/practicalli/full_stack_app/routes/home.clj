(ns practicalli.full-stack-app.routes.home
  (:require
   [practicalli.full-stack-app.layout :as layout]
   [practicalli.full-stack-app.middleware :as middleware]
   ))

(defn home-page [request]
  (layout/render request "home.html") 
  )


(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/" {:get home-page}]
   ]
  )


