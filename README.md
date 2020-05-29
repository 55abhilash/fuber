# fuber

FUBER: A simple Taxi booking service 

## Setup

Fuber is a Clojure based application and makes use of Leiningen.
You must install all of the following so as to run Fuber:
Clojure, Leiningen, JDK, MySQL

Now, clone this repo:<br>
```
git clone https://github.com/55abhilash/fuber
cd fuber
```
We need to setup MySQL database for Fuber to use. <br>
Database setup script and sample data is included in fuber/db/.
```
sudo mysql --local-infile -u root -p < db/dbsetup.sql
```

[Note: If you have MySQL privileges to create database and tables, no need to run the above command with sudo]<br>
This script will create database, tables, and a DB user called 'fuber'. It will also load sample data from fuber/db/taxis.csv.

Once database setup is done, simply run 'leiningen run' to start Fuber endpoint service.
```
sudo leiningen run
```

It will start on host http://localhost:3000/

To run Leiningen tests on the API endpoint functions, simply run:
```
lein test fuber.core-test
```
## Usage

# API Endpoints 

To get a list of Taxis and their info:

GET /getAllTaxis

Returns: Array of JSON Objects. Each object is information of one Taxi.

To find Taxi nearest to you:

GET /findTaxi

Parameters: 
    PinkRequired: 'true' if you want a Pink taxi, 'false' otherwise
    lat: Latitude of the user
    long: Longitude of the user

Returns: JSON Object with information on Taxi nearest to you.

To book a Taxi and start Trip:

GET /startTrip

Parameters:
    taxi_id: ID of the required Taxi. This ID was returned by /findTaxi.
    lat: Latitude of the user
    long: Longitude of the user

Returns: JSON Object with the booked taxi_id and ride_id. 

To end Trip:

GET /endTrip

Parameters:
    taxi_id: ID of booked Taxi
    ride_id: ID of the trip, returned by /startTrip
    lat: Latitude of the user at trip end
    long: Longitude of the user at trip end

Returns: Fare owed by the user in dogecoins.

# View Taxis on Map

Additionally, there is a visual representation of all (non assigned) Taxis on a world map. This is included in the folder fuber/ui/.

To view the representation, open the following in your browser:

file:///path_to_fuber/ui/index.html

Replace 'path_to_fuber' with the actual path of fuber on your machine.

![alt text](https://github.com/55abhilash/fuber/blob/master/taxismap.png?raw=true)

The Yellow cab markers show locations of Taxis. Click on the markers to get details about a Taxi.

![alt text](https://github.com/55abhilash/fuber/blob/master/taxi_popup.png?raw=true)

## Examples

Find Nearest Taxi:
http://localhost:3000/findTaxi?pinkRequired=false&lat=10&long=10

Book the Taxi and start Trip:

End Trip:

## License

Copyright © 2020 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
