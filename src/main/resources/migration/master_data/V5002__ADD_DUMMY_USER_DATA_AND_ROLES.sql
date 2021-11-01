INSERT INTO `user`
VALUES (4000,1,NULL,NULL,'Test Company','Test Person','testReek@yopmail.co',9002490025,'Test','$2a$10$yB4LnJ37/Rys6LTdectZt.PWvhXmrCEIJ//8iQKIHHnXwTU390ka2',NULL,NULL,1,0),
(4001,1,NULL,NULL,'Test Company','Test Person','testReek@yopmail.co',9002490026,'Test User','$2a$10$yB4LnJ37/Rys6LTdectZt.PWvhXmrCEIJ//8iQKIHHnXwTU390ka2',NULL,4000,1,0);

INSERT INTO `user_roles` VALUES (1,4000,'ROLE_SUPER_ADMIN'),(2,4000,'ROLE_ADMIN'),(3,4001,'ROLE_USER');
