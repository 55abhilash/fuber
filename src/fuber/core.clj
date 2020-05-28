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

(comment)

(defn getCurrentTime [] 
    "12:00:00.00" 
  )

(defn getAllTaxiLocations [pinkRequested]
  ;If pinkRequested is true, add where clause for color pink, otherwise don't
  (jdbc/query dbspec 
    [(str "select taxi_id, 
                  latitude, 
                  longitude, 
                  driver_name, 
                  driver_id, 
                  driver_no 
           from taxi where isAssigned = 0 " (if (= pinkRequested "true") "and isPink = 1;" ";"))]))

(defn calcTaxiDistance [taxiLocation userLocation]
;Pythagoras Theorem here
; Sqrt((x2 - x1)^2 + (y2 - y1)^2), x - long, y lat
(Math/sqrt (+ (Math/pow (- (get taxiLocation :lat) (Float/parseFloat (get userLocation :lat))) 2)
              (Math/pow (- (get taxiLocation :long) (Float/parseFloat (get userLocation :long))) 2))
           ))

(defn getSmallest [x] 
;Sort by distance, get first map, then remove distance from the map and return
  (dissoc (first (sort-by :dist x)) :dist)
)

(defn calcFare [ride_id endLocation]
  ; Retrieve start location and start time, then
  1;(+ (* (- endLocation startLocation) 2) (- currentTime startTime) )   
  ; Add condition for pink car
)

(defn getNearestTaxi [userLocation pinkRequested]
  ;getSmallest will return the row with smallest distance 
  ;(getAllTaxiLocations pinkRequested)
  ;(for [x (getAllTaxiLocations pinkRequested)] (conj [] (get x :taxi_id))) 
  (if-let [result (getSmallest (let [y (for [x (getAllTaxiLocations pinkRequested)] 
                 (merge {} {:dist (calcTaxiDistance {:lat (get x :latitude) :long (get x :longitude)} 
                                                     userLocation)} x)
                 )] y))] result {:err "No Taxi Available at the moment."}
  )
)

(defn assignRide [taxi_id start_lat start_long]
  (try (jdbc/with-db-transaction [t-con dbspec]
    (jdbc/insert! t-con :ride {:taxi_id taxi_id :start_latitude start_lat :start_longitude start_long})    
    (jdbc/update! t-con :taxi {:isAssigned 1} ["taxi_id = ?" taxi_id])  
    (jdbc/query t-con ["select ride_id from ride where taxi_id = ?" taxi_id]))  
  
  (catch Exception e (.printStackTrace (.getNextException e)))
  )
)

(defn endRideAndGetFare [ride_id endLocation]
  (jdbc/query dbspec [])  
  (jdbc/query dbspec ["update taxi set isAssigned = 0 and latitude = endLocation.lat and longitude = endLocation.long  where taxi_id in (select taxi_id from ride where ride_id = " ride_id ";) ;"])
  (calcFare ride_id endLocation)  
 )

;)
;----------------------------------------------------

;----------------------REST API----------------------

(defn findTaxi [req]
  {:status 200
   :headers {"Content-Type" "application/json"}
   ;:body (str "Taxi Available. Your location is" (:lat (:params req)) " " (:long (:params req)))
   ;Condition remaining: When no taxi available
   :body (str (json/write-str (getNearestTaxi {:lat (:lat (:params req)) :long (:long (:params req))} (:pinkRequested (:params req)))))
  }
)

(defn startRide [req]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body (str (json/write-str (assignRide (:taxi_id (:params req)) (:lat (:params req)) (:long (:params req)))))
  }
)

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

