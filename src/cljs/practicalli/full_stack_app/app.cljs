(ns ^:dev/once practicalli.full-stack-app.app
    (:require
     [devtools.core :as devtools]
     [practicalli.full-stack-app.core :as core]
     )
    )

(enable-console-print!)
(println "loading env/dev/cljs/guestbook/app.cljs...")

(devtools/install!)
(core/init!)