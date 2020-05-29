(ns fuber.core-test
  (:require [clojure.test :refer :all]
            [fuber.core :refer :all]
            [fuber.api.find :as find]
            [fuber.api.start :as start]
            [fuber.api.end :as end]
            [fuber.api.getAll :as getAll]
            [clojure.java.jdbc :as jdbc]
            [fuber.dbspec :as dbspec]))

(deftest api_test
;We make 2 sample entries in DB: 1 pink and 1 non-pink.
  (def testTaxi
    {:taxi_id 200 :driver_name "test_200"
     :driver_id 200 :driver_no "12345"
     :latitude 1000.0 :longitude 1000.0
     :isPink 0      :isAssigned 0})

  (def testTaxiPink
    {:taxi_id 201 :driver_name "test_201"
     :driver_id 201 :driver_no "12345"
     :latitude 2000.0 :longitude 2000.0
     :isPink 1      :isAssigned 0})

  (jdbc/insert! dbspec/dbspec :taxi
                testTaxi)
  (jdbc/insert! dbspec/dbspec :taxi
                testTaxiPink)

;Test /findTaxi
;pinkRequired = false
  (is (=  (dissoc testTaxi :isPink :isAssigned)
         (find/getNearestTaxi {:lat "1005" :long "1005"} false)))
;pinkRequired = true
  (is (=  (dissoc testTaxiPink :isPink :isAssigned)
         (find/getNearestTaxi {:lat "2005" :long "2005"} true)))

;Test /startRide
;pink = false
(def assignedTripId (start/assignRide 200 "1005" "1005"))
(def testTripId (jdbc/query dbspec/dbspec ["select max(ride_id) as ride_id, taxi_id
  from ride where taxi_id = ?" 200]))
(is (= testTripId assignedTripId))

;pink = true
(def assignedTripIdPink (start/assignRide 201 "2005" "2005"))
(def testTripIdPink (jdbc/query dbspec/dbspec ["select max(ride_id) as ride_id, taxi_id
  from ride where taxi_id = ?" 201]))
(is (= testTripIdPink assignedTripIdPink))

;Test /endRide
;pink = false
(is (=  {:fare (str (* (Math/sqrt 2) 10) " dogecoins")}
(end/endRideAndGetFare 200 (get (first assignedTripId) :ride_id) "1010" "1010")))
;pink = true
(is (=  {:fare (str (+ (* (Math/sqrt 2) 10) 5) " dogecoins")}
  (end/endRideAndGetFare 201 (get (first assignedTripIdPink) :ride_id) "2010" "2010")))

;Test /getAll
(is (=  (jdbc/query dbspec/dbspec
            [(str "select taxi_id, 
                          latitude, 
                          longitude, 
                          driver_name, 
                          driver_id, 
                          driver_no 
                   from taxi where isAssigned = 0 ")])
  (getAll/getAllTaxis)))

;Delete the test entries from db before ending
  (jdbc/delete! dbspec/dbspec :taxi ["taxi_id = ?" 200])
  (jdbc/delete! dbspec/dbspec :taxi ["taxi_id = ?" 201])
)