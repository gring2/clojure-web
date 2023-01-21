(ns practicalli.full-stack-app.app
  (:require [practicalli.full-stack-app.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
