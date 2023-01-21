CREATE TABLE guestbook
(id INTEGER PRIMARY KEY AUTO_INCREMENT,
name varchar(30),
message varchar(200),
timestamp timestamp default current_timestamp


);