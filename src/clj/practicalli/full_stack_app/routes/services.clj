(ns practicalli.full-stack-app.routes.services
  (:require
   [practicalli.full-stack-app.middleware.formats :as formats] 
   [practicalli.full-stack-app.message :as msg]
   [reitit.swagger :as swagger]
   [reitit.swagger-ui :as swagger-ui]
   [reitit.ring.coercion :as coercion]
   [reitit.coercion.spec :as spec-coercion]
   [reitit.ring.middleware.muuntaja :as muuntaja]
   [reitit.ring.middleware.exception :as exception]
   [reitit.ring.middleware.multipart :as multipart]
   [reitit.ring.middleware.parameters :as parameters]
   [ring.util.http-response :as response])
  )

(defn service-routes []
  ["/api"
   {:middleware [parameters/parameters-middleware
                 muuntaja/format-negotiate-middleware
                 muuntaja/format-response-middleware
                 exception/exception-middleware
                 muuntaja/format-request-middleware
                 coercion/coerce-request-middleware
                 coercion/coerce-response-middleware
                 multipart/multipart-middleware
                 ] 
    :swagger {:id ::api}
    :muuntaja formats/instance
    :coercion spec-coercion/coercion
    }
   ["" {:no-doc true}
    ["/swagger.json" {:get (swagger/create-swagger-handler)}]
    ["/swagger-ui*" {:get (swagger-ui/create-swagger-ui-handler {:url "/api/swagger.json"})}]
    ]
   ["/messages"
    {:get {
           :responses {200 {:body {:messages [{:id pos-int? :name string? :message string? :timestamp inst?}]}}}
           :handler (fn [_] (response/ok (msg/message-list)))}}]
   ["/message"
    {:post 
     
     {:parameters {:body {:name string? :message string?}}
      :responses {200 {:body map?}
                  400 {:body map?} 
                  500 {:errors map?}}
      :handler (fn [{{params :body} :parameters}]
                 (try
                   (msg/save-message! params)
                   (response/ok  {:status :ok})
                   (catch Exception e
                     (let [{id :guestbook/error-id
                            errors :errors} (ex-data e)]
                       (case id
                         :validation
                         (response/bad-request {:errors errors})
                         (response/internal-server-error
                          {:errors {:server-error ["Failed to save msg!"] :errors errors}}))))))}
     }]])
