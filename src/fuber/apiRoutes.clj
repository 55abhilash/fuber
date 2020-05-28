(ns fuber.apiRoutes
  (:require [clojure.pprint :as pp]
            [clojure.string :as str]
            [clojure.data.json :as json]
            [fuber.api.find :as find]
            [fuber.api.getAll :as getAll]
            [fuber.api.start :as start]
            [fuber.api.end :as end])
  (:gen-class))

(defn findTaxi [req]
  {:status 200
   :headers {"Content-Type" "application/json"}
     ;:body (str "Taxi Available. Your location is" (:lat (:params req)) " " (:long (:params req)))
     ;Condition remaining: When no taxi available
   :body (str (json/write-str (find/getNearestTaxi {:lat (:lat (:params req)) :long (:long (:params req))} (:pinkRequested (:params req)))))})


(defn getAllTaxis [req]
  {:status 200
   :headers {"Content-Type" "application/json"}
     ;:body (str "Taxi Available. Your location is" (:lat (:params req)) " " (:long (:params req)))
     ;Condition remaining: When no taxi available
   :body (str (json/write-str (getAll/getAllTaxis )))})


(defn startRide [req]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body (str (json/write-str (start/assignRide (:taxi_id (:params req)) (:lat (:params req)) (:long (:params req)))))})

(defn endRide [req]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body (str (json/write-str
               (end/endRideAndGetFare (:taxi_id (:params req))
                                      (:ride_id (:params req))
                                      (:lat (:params req))
                                      (:long (:params req)))))})
