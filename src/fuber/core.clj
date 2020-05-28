(ns fuber.core
  (:require [org.httpkit.server :as server]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer :all]
            [clojure.pprint :as pp]
            [clojure.string :as str]
            [clojure.data.json :as json]
            [clojure.java.jdbc :as jdbc])
  (:gen-class))
;----------------------CRUD--------------------------

;DB Spec Init

(def dbspec 
  {
    :dbtype "mysql"
    :dbname "fuber_db"
    :user   "fuber"
    :password "fuber"
  })

(comment

(defn getAllTaxiLocations [pinkRequested]
  ;If pinkRequested is true, add where clause for color pink, otherwise don't
  (jdbc/query dbspec ["select taxi_id, latitude, longitude from taxi"])    
)

(defn calcTaxiDistance [taxiLocation userLocation]
;Pythagoras Theorem here
)

(defn calcFare [ride_id endLocation]
  ; Retrieve start location and start time, then
  (+ (* (- endLocation startLocation) 2) (- currentTime startTime) )   
  ; Add condition for pink car
)

(defn getNearestTaxi [userLocation pinkRequested]
  ;getSmallest will return the row with smallest distance
  (getSmallest (for each (getAllTaxiLocations pinkRequested) (conj distances (calcTaxiDistance [taxtLoc userLocation]))))
)

(defn assignRide [taxi_id start_lat start_long]
  (jdbc/query dbspec ["insert into ride (taxi_id, start_latitude, start_longitude start_time) 
                    values(" taxi_id "," start_lat "," start_long "," (getCurrentTime) 
                    ");"])    
  (jdbc/query dbspec ["update taxi set isAssigned = 1 where taxi_id = " taxi_id  ";"])  
  (jdbc/query dbspec ["select ride_id from ride where taxi_id = " taxi_id ";"])  
)

(defn endRideAndGetFare [ride_id endLocation]
  (jdbc/query dbspec [])  
  (jdbc/query dbspec ["update taxi set isAssigned = 0 and latitude = endLocation.lat and longitude = endLocation.long  where taxi_id in (select taxi_id from ride where ride_id = " ride_id ";) ;"])
  (calculateFare ride_id endLocation)  
 )

)
;----------------------------------------------------

;----------------------REST API----------------------

(defn findTaxi [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   ;:body (str "Taxi Available. Your location is" (:lat (:params req)) " " (:long (:params req)))
   ;Condition remaining: When no taxi available
   :body (getNearestTaxi [(:lat (:params req)) (:long (:params req))] (:pinkRequested (:params req)))
  }
)

(defn startRide [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (assignRide [(:taxi_id (:params req)) (:start_latitude (:params req)) (:start_longitude (:params req))])
   })

(defn endRide [req]
  {:status 200
   :headers {"Content-Type}" "text/html"}
   :body (endRideAndGetFare (:ride_id (:params req)) (:endLocation (:params req)))})

;-----------------------------------------------------


(defroutes app-routes
  (GET "/findTaxi" [] findTaxi)
  (GET "/startRide" [] startRide)
  (GET "/endRide" [] endRide)
  (route/not-found "Error! Page Not Found"))

(defn -main
  "Clojure Server"
  [& args]
  (let [port (Integer/parseInt "3000")]
    (server/run-server (wrap-defaults #'app-routes site-defaults) {:port port})
  (println (str "Server Running at localhost:" port))))

