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
CREATE TABLE erto_barcode(  
type int not null,  
id int not null,  
data varchar2(2000) default null,  
method int not null  
);  
  
CREATE TABLE erto_barcode_auth_history(  
user_id   int not null,  
auth_date timestamp not null  
);  
  
CREATE TABLE erto_barcode_method(  
method_id int not null,  
method_name varchar2(40) not null  
);  
  
  

### Procedures creation  
Procedures are situated at ARM_ERTO_SERVICE package

procedure add_barcode  
(
 type_ IN NUMBER,   
 id_ IN NUMBER,   
 data_ IN VARCHAR2,   
 method_ IN NUMBER  
)   
 is   
 begin  
    insert into erto_barcode  
    (type, id, data, method)   
    values(type_, id_, data_, method_);   
 end;   
     
    
procedure add_user_visit  
(  
user_id_ IN NUMBER,   
my_time_ IN VARCHAR2  
)   
is   
 begin  
    insert into erto_barcode_auth_history  
    (user_id, auth_date)   
    values(user_id_, to_timestamp(my_time_,'yyyy-mm-dd hh24-mi-ss'));  
 end;  
   
procedure get_info_by_passport_id(  
cursor_ IN OUT SYS_REFCURSOR,  
passport_id_ IN NUMBER  
)  
is  
begin  
open cursor_ for  
      select invnumber,net_number from iob_passport where passport_id=passport_id_;  
end;  

  
procedure get_info_by_building_id(  
cursor_ IN OUT SYS_REFCURSOR,   
building_id_ IN NUMBER  
)  
is  
begin  
open cursor_ FOR  
      SELECT inv_number,net_number FROM erto_bld WHERE building_id=building_id_;  
end;  

















