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
