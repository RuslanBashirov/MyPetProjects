## winter-period  
  
### Api  
#### Parameters  
id_day_type = {1, 2}: 1 - add start winter day, 2 - add final winter day;  
id_operation = {1, 2, 3, 4}: 1 - create, 2 - read, 3 - update, 4 - delete;  
 
**/api/createTimestamp?id_depo=2&id_day_type=1&timestamp_input=19.11.21 11:23:55&id_winter_period=2&timestamp_current=05.10.21 12:33:45&id_operation=1**  
  
**/api/readTimestamp?id_depo=5&id_winter_period=2&timestamp_current=05.10.21 12:33:45&id_operation=2**  
  
**/api/updateTimestamp?id_depo=14&id_day_type=2&timestamp_input=19.11.21 11:23:55&id_winter_period=2&timestamp_current=05.10.21 17:22:55&id_operation=3**  
  
**/api/deleteTimestamp?id_depo=5&id_winter_period=2&timestamp_current=05.10.21 12:33:45&id_operation=4**   
  
  
### Tables creation  
  
CREATE TABLE winter_period(  
id_winter_period int PRIMARY KEY,  
year_start int NOT NULL,  
year_end int NOT NULL,  
);  
    
CREATE TABLE history(  
id_history int PRIMARY KEY,  
id_depo int NOT NULL,  
id_day_type int DEFAULT NULL,  
timestamp_input timestamp DEFAULT NULL,  
id_winter_period int REFERENCES winter_period(id_winter),  
timestamp_current timestamp NOT NULL,  
id_operation int NOT NULL  
);  
  
CREATE TABLE final_start(  
id_history int REFERENCES history(id_history),  
id_depo int NOT NULL,  
id_winter_period int REFERENCES winter_period(id_winter),  
timestamp_start timestamp NOT NULL  
);  
  
CREATE TABLE final_end(  
id_history int REFERENCES history(id_history),  
id_depo int NOT NULL,  
id_winter_period int REFERENCES winter_period(id_winter),  
timestamp_end timestamp NOT NULL  
);  
  
  
  
  
  
### Api  
type_code = {1, 2}: 1 - start date, 2 - finish date;  
  
**/api/create?depo_id=2&season=2018&type_code=1&curr_timestamp=30.04.2018 16:12:45&input_date=06.11.2018**  
  
**/api/create?depo_id=2&season=2018&type_code=2&curr_timestamp=31.04.2018 16:12:45&input_date=19.03.2019**  
  
**/api/read?depo_id=2&season=2018**  
  
**/api/update?depo_id=2&season=2018&type_code=1&curr_timestamp=01.05.2018 16:12:45&input_date=10.11.2018**  
  
**/api/delete?depo_id=2&season=2018&type_code=2&current_timestamp=02.05.2018 16:12:45**  
  
  
### Table creation  
CREATE TABLE winter_period(  
id int PRIMARY KEY,  
depo_id int NOT NULL,  
season int NOT NULL,  
type_code int NOT NULL,  
curr_timestamp timestamp NOT NULL,  
f date NOT NULL,  
t date NOT NULL,  
);  
  

