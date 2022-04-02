### Barcode API  
**/api/getBarcodeImage?type=2&id=115&method=1&userId=6**  
to get barcode image for special input parameters.  
Input params:  
*type*:  1 - technological equipment, 2 - building,  
*id*:  is object id,  
*method*: 1 - for QRCode type, 2 - for DataMatrix type, 3 - for Aztec type,  
*userId*: is user id  
returns image file  
  
**/api/getBarcodeBase64?type=2&id=115&method=1&userId=6**  
to get barcode in Base64 encoded string for special input parameters.  
Input params:  
*type*:  1 - technological equipment, 2 - building,  
*id*:  is object id,  
*method*: 1 - for QRCode type, 2 - for DataMatrix type, 3 - for Aztec type,  
*userId*: is user id  
returns string;  
  
**/api/addBarcode?type=2&id=115&method=2&data=hfjhagbsdayhb**  
to add barcode data for special input parameters.  
Input params:  
*type*:  1 - technological equipment, 2 - building,  
*id*:  is object id,  
*method*: 1 - for QRCode type, 2 - for DataMatrix type, 3 - for Aztec type,  
*data*: Base64 content  
returns void  
  
  
### Tables creation
create user ruslan with encrypted password 'ruslan';
create database petprojects;
grant all privileges on database petprojects to ruslan;
psql -d petprojects -U ruslan
CREATE TABLE barcode(  
type int not null,  
id int not null,  
data varchar default null,  
method int not null  
);  
  
CREATE TABLE barcode_auth_history(  
user_id   int not null,  
auth_date timestamp not null  
);  
  
CREATE TABLE barcode_method(  
method_id int not null,  
method_name varchar not null  
);  















