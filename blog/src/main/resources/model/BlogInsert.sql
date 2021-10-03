Use Blog;

insert into user(
id, `name`, username, `password`, email, enabled)
values
(1, "User deleted", "", "", "", true),
(2, "admin", "admin", "password", "admin@admin.com", true),
(3, "user", "user", "password", "user@gmail.com",  true);


insert into role(id, role)
    values(1,"ROLE_ADMIN"), 
    (2,"ROLE_USER");
    
insert into user_role(user_id, role_id)
    values(2,1),(2,2),(3,2);
    
UPDATE `user` SET `password` = '4F2BBCC3B637B9E5F4A5326574F0FE84629F8710553F9EC57242180CF50FF2D8' WHERE id = 1;
UPDATE `user` SET `password` = '$2a$10$C1triXdUhROmIwLT/mbndeJBGDBE8iKDSoHatzVU7kfUMibVjtX8q' WHERE id = 2;
UPDATE `user` SET `password` = '$2a$10$C1triXdUhROmIwLT/mbndeJBGDBE8iKDSoHatzVU7kfUMibVjtX8q' WHERE id = 3;

select * from user;