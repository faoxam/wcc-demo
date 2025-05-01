CREATE DATABASE IF NOT EXISTS wcc_demo;

USE wcc_demo;

CREATE TABLE `postcd_geo` (
  `id` bigint NOT NULL,
  `latitude` float NOT NULL,
  `longitude` float NOT NULL,
  `postcode` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_postcd_geo` (`postcode`)
) ;

-- Notes: There is some record having empty latitude and longitude in CSV file
-- This value will be convert to 0 (Or should we exclude this record?)
SET GLOBAL local_infile=1;
LOAD DATA INFILE '/var/lib/mysql-files/ukpostcodes.csv'
INTO TABLE postcd_geo
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n'
IGNORE 1 LINES (id, postcode, @var1, @var2)
SET latitude = CAST(@var1 AS float),
longitude = CAST(@var2 as float) ;