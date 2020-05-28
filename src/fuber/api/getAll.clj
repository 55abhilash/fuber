(ns fuber.api.getAll
  (:require [fuber.helper :as helper])
  (:gen-class))

(defn getAllTaxis []
  (helper/getAllTaxiLocations false))
