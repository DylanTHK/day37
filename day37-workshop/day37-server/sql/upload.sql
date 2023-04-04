CREATE DATABASE blobdb;

use blobdb;

DROP TABLE IF EXISTS posts;

CREATE TABLE posts (
	id int not null auto_increment,
    blobc mediumblob not null,
    name varchar(100) null,
    comment varchar(100) null,
    primary key (id)
);

DESCRIBE posts;

INSERT INTO posts (blobc, name, comment) VALUES('image url', 'nooblet', "noob comment");
INSERT INTO posts (blobc, name, comment) VALUES (?,?,?);

SELECT * from posts;

SELECT * FROM posts WHERE ID=1;
