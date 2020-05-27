(ns fuber.core
  (:require [org.httpkit.server :as server]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer :all]
            [clojure.pprint :as pp]
            [clojure.string :as str]
            [clojure.data.json :as json])
  (:gen-class))

(defroutes app-routes
  (GET "/getTaxt" [] getTaxt)
  (GET "/endRide" [] endRide)
  (route/not-found "Error! Page Not Found"))

(defn -main
  "Clojure Server"
  [& args]
  (let [port (Integer/parseInt "3000")]
    (server/run-server #'app-routes {:port port})
  (println (str "Server Running at localhost:" port))))
