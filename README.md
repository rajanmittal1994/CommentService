SQL commands FOR DB INITIALISATION

./gradlew bootRun -- for running this command, we need sql installed in server..



CREATE DATABASE social;


create table reaction_count (id bigint not null, content_type tinyint, content_type_id bigint, created_at bigint, dis_like integer, like integer, updated_at bigint, primary key (id))

18:13:51	create table reaction_count (id bigint not null, content_type tinyint, content_type_id bigint, created_at bigint, dis_like integer, like integer, updated_at bigint, primary key (id))	Error Code: 1064. You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near 'like integer, updated_at bigint, primary key (id))' at line 1	0.000 sec


Columns:
id int(11) PK 
created_at bigint(20) 
email varchar(255) 

INSERT INTO user(id, created_at, email, first_name, last_name) 
VALUES('1', '1708347976', 'ram@gmail.com', 'ram ', 'gupta');

INSERT INTO user(id, created_at, email, first_name, last_name) 
VALUES('2', '1708347976', 'ram@gmail.com', 'sham ', 'gupta');

INSERT INTO user(id, created_at, email, first_name, last_name) 
VALUES('3', '1708347976', 'ram@gmail.com', 'ramesh ', 'gupta');

INSERT INTO user(id, created_at, email, first_name, last_name) 
VALUES('4', '1708347976', 'ram@gmail.com', 'rama ', 'gupta');

INSERT INTO user(id, created_at, email, first_name, last_name) 
VALUES('5', '1708347976', 'ram@gmail.com', 'shama', 'gupta');

INSERT INTO user(id, created_at, email, first_name, last_name) 
VALUES('6', '1708347976', 'ram@gmail.com', 'grama', 'gupta');


INSERT INTO post(id, content, created_at, user_id)
VALUES('1','job id 1234 created for role in intuit', '1708347976', '1');

INSERT INTO post(id, content, created_at, user_id)
VALUES('2','job id 1236 created for role in samsung', '1708347976', '1');





