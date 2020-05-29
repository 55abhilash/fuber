(ns fuber.api.end
  (:require [clojure.java.jdbc :as jdbc]
            [fuber.helper :as helper]
            [fuber.dbspec :as dbspec])
  (:gen-class))

(defn endRideAndGetFare [taxi_id ride_id end_lat end_long]
  (try (jdbc/with-db-transaction [t-con dbspec/dbspec]
         (jdbc/update! t-con :taxi {:isAssigned 0 :latitude end_lat :longitude end_long}
                       ["taxi_id = ?" taxi_id])
         (def start_params (first (jdbc/query t-con ["select start_latitude, start_longitude, 
                                                       start_time, current_timestamp as end_time from ride 
                                                       where ride_id = ?" ride_id])))
          ;(println (str "DEBUG: start_params" (get (first start_params) :start_latitude)))
         (helper/calcFare ride_id {:lat end_lat :long end_long}
                          {:lat (get start_params :start_latitude) :long (get start_params :start_longitude)}
                          (get start_params :start_time)
                          (get start_params :end_time)
                          (jdbc/query t-con ["select isPink from taxi where taxi_id = ?" taxi_id])))
       (catch Exception e (println (Throwable->map e)))))
