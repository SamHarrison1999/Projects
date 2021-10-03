DROP DATABASE IF EXISTS Blog;

CREATE DATABASE Blog;

use Blog;

create table `user`(
`name` varchar(45) not null,
id int primary key auto_increment,
username varchar(45) not null unique,
`password` varchar(100) not null,
email varchar(45) not null,
enabled boolean not null);

create table `role`(
id int primary key auto_increment,
`role` varchar(30) not null
);

create table user_role(
user_id int not null,
role_id int not null,
primary key(user_id, role_id),
foreign key (user_id) references user(id),
foreign key (role_id) references role(id));


Create table post(
id int primary key not null auto_increment,
title varchar(100) not null,
body varchar(15000) not null,
`timestamp` timestamp not null default current_timestamp,
startdate Date not null default '1000-01-01',
enddate Date not null default '9999-12-31',
featured boolean not null,
user_id  int not null,
approved boolean not null default false,

foreign key (user_id)
references User(id)
);

Create table tag(
`name` varchar(45) primary key
);

Create table post_tag(
id  int not null,
`name` varchar(45) not null,

foreign key (id)
references post(id),

foreign key (`name`)
references tag(`name`)
);

select * from post_tag;