(ns fuber.server
  (:require [org.httpkit.server :as server]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer :all]
            [clojure.pprint :as pp]
            [clojure.string :as str]
            [clojure.data.json :as json]
            [clojure.java.jdbc :as jdbc]
            [clj-time.core :as t]
            [clj-time.coerce :as coer]
            [fuber.apiRoutes :as api])
  (:gen-class))

(defroutes app-routes
  (GET "/findTaxi" [] api/findTaxi)
  (GET "/startTrip" [] api/startRide)
  (GET "/endTrip" [] api/endRide)
  (route/not-found "Error! Page Not Found"))

(defn start
  "Clojure Server"
  [& args]
  (let [port (Integer/parseInt "3000")]
    (server/run-server (wrap-defaults #'app-routes site-defaults) {:port port})
    (println (str "Server Running at localhost:" port))))
