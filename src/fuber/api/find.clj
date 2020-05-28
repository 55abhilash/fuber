(ns fuber.api.find
  (:require [fuber.helper :as helper])
  (:gen-class))

(defn getNearestTaxi [userLocation pinkRequested]
    ;getSmallest will return the row with smallest distance 
    ;(getAllTaxiLocations pinkRequested)
    ;(for [x (getAllTaxiLocations pinkRequested)] (conj [] (get x :taxi_id))) 
  (if-let [result (helper/getSmallest (let [y (for [x (helper/getAllTaxiLocations pinkRequested)]
                                                (merge {} {:dist (helper/calcTaxiDistance {:lat (get x :latitude) :long (get x :longitude)}
                                                                                          userLocation)} x))] y))] result {:err "No Taxi Available at the moment."}))