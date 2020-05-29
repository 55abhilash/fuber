/*Invoke this file as follows:
 	sudo mysql --local-infile -u root -p < dbsetup.sql */
/*Starting transaction, although there is no use of it in DDL*/
start transaction;

create database fuber_db;
use fuber_db;

CREATE TABLE `taxi` (
  `taxi_id` int NOT NULL AUTO_INCREMENT,
  `driver_name` varchar(64) NOT NULL,
  `driver_id` int NOT NULL,
  `driver_no` varchar(10) DEFAULT NULL,
  `latitude` float NOT NULL,
  `longitude` float NOT NULL,
  `isPink` tinyint(1) DEFAULT NULL,
  `isAssigned` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`taxi_id`)
); 
CREATE TABLE `ride` (
  `ride_id` int NOT NULL AUTO_INCREMENT,
  `taxi_id` int NOT NULL,
  `start_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `start_latitude` float DEFAULT NULL,
  `start_longitude` float DEFAULT NULL,
  PRIMARY KEY (`ride_id`)
); 

create user fuber identified with 'mysql_native_password' by 'fuber';
grant all on fuber_db.* to 'fuber';
grant all on fuber to 'fuber';

set global local_infile=ON;
load data local infile 'taxis.csv' into table taxi fields terminated by ',' lines terminated by '\n' ignore 1 rows (taxi_id, driver_name, driver_id, driver_no, latitude, longitude, isPink, isAssigned);
set global local_infile=OFF;

commit;
