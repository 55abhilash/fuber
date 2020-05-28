(ns fuber.helper
  (:require [clojure.pprint :as pp]
            [clojure.string :as str]
            [clojure.data.json :as json]
            [clojure.java.jdbc :as jdbc]
            [clj-time.core :as t]
            [clj-time.coerce :as coer]
            [fuber.dbspec :as dbspec])
  (:gen-class))

(defn calcTaxiDistance [location1 location2]
    ;Pythagoras Theorem here
    ; Sqrt((x2 - x1)^2 + (y2 - y1)^2), x - long, y lat
  (Math/sqrt (+ (Math/pow (- (get location1 :lat) (Float/parseFloat (get location2 :lat))) 2)
                (Math/pow  (- (get location1 :long) (Float/parseFloat (get location2 :long))) 2))))

(defn calcFare [ride_id endLocation startLocation startTime endTime isPink]
    ;(+ (* (- endLocation startLocation) 2) (- currentTime startTime) )   
    ; Add condition for pink car
  (def totalFare (+ (* (calcTaxiDistance startLocation endLocation) 2)
                    (t/in-minutes (t/interval (coer/from-sql-time startTime) (coer/from-sql-time endTime)))))
  {:fare (if (get (first isPink) :ispink) (+ totalFare 5) totalFare)})

(defn getAllTaxiLocations [pinkRequested]
                    ;If pinkRequested is true, add where clause for color pink, otherwise don't
  (jdbc/query dbspec/dbspec
              [(str "select taxi_id, 
                                    latitude, 
                                    longitude, 
                                    driver_name, 
                                    driver_id, 
                                    driver_no 
                             from taxi where isAssigned = 0 " (if (= pinkRequested "true") "and isPink = 1;" ";"))]))

(defn getSmallest [x]
    ;Sort by distance, get first map, then remove distance from the map and return
  (dissoc (first (sort-by :dist x)) :dist))