(defproject fuber "0.0.1-SNAPSHOT"
  :description "Fuber Taxi Booking Service"
  :url "https://github.com/55abhilash/fuber"
  :license {:name "GPL-3.0 WITH Classpath-exception-2.0"
            :url "https://www.gnu.org/licenses/gpl-3.0.en.html"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                  ; Compojure - A basic routing library
                 [compojure "1.6.1"]
                  ; Our Http library for client/server
                 [http-kit "2.3.0"]
                  ; Ring defaults - for query params etc
                 [ring/ring-defaults "0.3.2"]
                  ; Clojure data.JSON library
                 [org.clojure/data.json "0.2.6"]
                  ; JDBC
                 [org.clojure/java.jdbc "0.7.11"]
                  ; MySQL driver
                 [mysql/mysql-connector-java "8.0.20"]]
  :main ^:skip-aot fuber.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
