(ns practicalli.full-stack-app.db.core-test
  (:require
   [practicalli.full-stack-app.db.core :refer [*db*] :as db]
   [java-time.pre-java8]
   [luminus-migrations.core :as migrations]
   [clojure.test :refer :all]
   [next.jdbc :as jdbc]
   [practicalli.full-stack-app.config :refer [env]]
   [mount.core :as mount]))

(use-fixtures
  :once
  (fn [f]
    (mount/start
     #'practicalli.full-stack-app.config/env
     #'practicalli.full-stack-app.db.core/*db*)
    (migrations/migrate ["migrate"] (select-keys env [:database-url]))
    (f)))

(deftest test-messages
  (jdbc/with-transaction [t-conn *db* {:rollback-only true}]
    (is (= 1 (db/save-message! t-conn
                               {:name "Bob" :message "BBB"}
                               {:connection t-conn})))
    (is (=  {:name "Bob" :message "BBB"}
            (-> (db/get-messages t-conn {})
                (first)
                (select-keys [:name :message])
                )
            ))
    )
  )