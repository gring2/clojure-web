(ns practicalli.full-stack-app.message
  (:require
   [practicalli.full-stack-app.db.core :as db]
   [practicalli.full-stack-app.validation :refer [validate-message]]
   )
  )

(defn message-list []
  {:messages (vec (db/get-messages))})

(defn save-message! [message]
  (if-let [errors (validate-message message)]
    (throw (ex-info "message is invalid"
                    {:guestbook/error-id :validation
                     :errors errors}
                    ))
    (db/save-message! message)
    )
  )