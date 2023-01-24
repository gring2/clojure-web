(ns practicalli.full-stack-app.handler
  (:require
   [practicalli.full-stack-app.middleware :as middleware]
   [practicalli.full-stack-app.layout :refer [error-page]]
   [practicalli.full-stack-app.routes.home :refer [home-routes]]

   [practicalli.full-stack-app.routes.services :refer [service-routes]]
   [reitit.ring.middleware.dev :as dev]

   [reitit.ring :as ring]
   [ring.middleware.content-type :refer [wrap-content-type]]
   [ring.middleware.webjars :refer [wrap-webjars]]
   [practicalli.full-stack-app.env :refer [defaults]]
   [practicalli.full-stack-app.routes.websockets :refer [websocket-routes]]
   [mount.core :as mount]))

(mount/defstate init-app
  :start ((or (:init defaults) (fn [])))
  :stop  ((or (:stop defaults) (fn []))))

(defn- async-aware-default-handler
  ([_] nil)
  ([_ respond _] (respond nil)))


(mount/defstate app-routes
  :start
  (ring/ring-handler
    (ring/router
      [(home-routes)
       (service-routes)
       (websocket-routes)
       ]
     {:reitit.middleware/transform dev/print-request-diffs})
    (ring/routes
      (ring/create-resource-handler
        {:path "/"})
      (wrap-content-type
        (wrap-webjars async-aware-default-handler))
      (ring/create-default-handler
        {:not-found
         (constantly (error-page {:status 404, :title "404 - Page not found"}))
         :method-not-allowed
         (constantly (error-page {:status 405, :title "405 - Not allowed"}))
         :not-acceptable
         (constantly (error-page {:status 406, :title "406 - Not acceptable"}))}))))


(defn app []
  (middleware/wrap-base #'app-routes))
