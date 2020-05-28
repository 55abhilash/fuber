(ns fuber.api.start
  (:require [clojure.java.jdbc :as jdbc]
            [fuber.dbspec :as dbspec])
  (:gen-class))

(defn assignRide [taxi_id start_lat start_long]
  (if-let [checkTaxi (= (get (first (jdbc/query dbspec/dbspec ["select isAssigned from taxi where taxi_id = ?" taxi_id])) :isassigned) false)]
    (try (jdbc/with-db-transaction [t-con dbspec/dbspec]
           (jdbc/insert! t-con :ride {:taxi_id taxi_id :start_latitude start_lat :start_longitude start_long})
           (jdbc/update! t-con :taxi {:isAssigned 1} ["taxi_id = ?" taxi_id])
           (jdbc/query t-con ["select ride_id, taxi_id from ride where taxi_id = ?" taxi_id]))

         (catch Exception e (.printStackTrace (.getNextException e)))) {:err "This Taxi is no longer available. Run /findTaxi again to find nearest available Taxi."}))