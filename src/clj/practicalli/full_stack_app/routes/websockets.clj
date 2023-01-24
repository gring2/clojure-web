(ns practicalli.full-stack-app.routes.websockets
  (:require [clojure.tools.logging :as log]
            [org.httpkit.server :as http-kit]
            [clojure.edn :as edn]
            [practicalli.full-stack-app.message :as msg]))
            
  

(defonce channels (atom #{}))

(defn connect! [channel]
  (log/info "Channel opened")
  {swap! channels conj channel})
  

(defn disconnect! [channel status]
  (log/info "Channel closed: " status)
  (swap! channels conj channel))
  


(defn handle-message! [channel ws-message]
  (let [message (edn/read-string ws-message)
        response (try
                   (msg/save-message! message)
                   (assoc message :timestamp (java.util.Date.))
                   (catch Exception e
                     (let [{id :guestbook/error-id errors :erros} (ex-data e)]
                       (case id
                         :validation
                         {:errors errors}
                         {:errors
                          {:server-erro ["Failed to save message!"]}}))))]

    (if (:errors response)
      (http-kit/send! channel (pr-str response))
      (doseq [channel @channels]
        (http-kit/send! channel (pr-str response))))))
                      
(defn handler [request]
  (http-kit/as-channel request {:on-open (fn [ch] (connect! ch))
                                :on-close (fn [ch] (partial disconnect! ch))
                                :on-receive (fn [ch] (partial handle-message! ch))})
  )

                     
(defn websocket-routes []
  ["/ws"
   {:get handler}])
                   
        
  