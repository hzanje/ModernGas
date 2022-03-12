INSERT INTO `user` VALUES (4000,1,NULL,NULL,'Test Company','Text Contact','testSuperAdmin@yopmail.com',9800098000,'Super Admin','$2a$10$yB4LnJ37/Rys6LTdectZt.PWvhXmrCEIJ//8iQKIHHnXwTU390ka2',1,0),
(4001,1,NULL,NULL,'Test Company','Text Contact','testAdmin@yopmail.com',9800098020,'Admin Test','$2a$10$yB4LnJ37/Rys6LTdectZt.PWvhXmrCEIJ//8iQKIHHnXwTU390ka2',1,0),
(4002,1,NULL,NULL,'Test Company','Text Contact','testAdmin@yopmail.com',9800098030,'Employee Test','$2a$10$yB4LnJ37/Rys6LTdectZt.PWvhXmrCEIJ//8iQKIHHnXwTU390ka2',1,0),
(4003,1,NULL,NULL,'Test Company','Text Contact','testUser@yopmail.com',9800098040,'User Test','$2a$10$yB4LnJ37/Rys6LTdectZt.PWvhXmrCEIJ//8iQKIHHnXwTU390ka2',1,0);

INSERT INTO `user_roles` VALUES (1,4000,'ROLE_SUPER_ADMIN'),
(2,4001,'ROLE_ADMIN'),
(3,4001,'ROLE_EMPLOYEE'),
(4,4002,'ROLE_EMPLOYEE'),
(5,4003,'ROLE_USER');

INSERT INTO `user_privilege` VALUES (1,4,'Order'),
(2,4,'User'),
(3,4,'Employee'),
(4,4,'Inventory'),
(5,4,'Resource Centre'),
(6,4,'Account');

INSERT INTO `resource_centre` VALUES (1,NULL,NULL,NULL,'Ambernath','Refill',4001),
(2,NULL,NULL,NULL,'Dombivli','Refill',4001);

INSERT INTO `user_admin_mapping` VALUES (4003, 4001);