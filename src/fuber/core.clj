(ns fuber.core
  (:require [fuber.server :as server])
  (:gen-class))

(defn startServer []
  (server/start))

(defn -main [& args]
  (startServer))
