###Its an auction of paintings. 

I used Spring MVC scheme. Connection with PostgreSQL database is implemented using Hibernate. 

Login as admin with password admin
   or as user with password user

Admin can add authors and many paintings for a certain author. 
User can add many comments to a certain painting.

### Tables creation  
CREATE TABLE auction.author(  
id int primary key,  
first_name varchar(50) not null,  
last_name varchar(50) not null,
email varchar(50) not null
);  

CREATE TABLE auction.comment(  
id int primary key,  
text varchar(50) not null,   
painting_id int not null,
constraint pnt_id foreign key(painting_id)
references auction.painting(id)
);  

CREATE TABLE auction.painting(  
id int primary key,  
title varchar(50) not null,   
information varchar(500) default null,
author_id int not null,
image bytea default null,
best_price int  default null,
customer_email varchar(50) default null,
constraint au_id foreign key(author_id)
references auction.author(id)
);  