insert into `category_master`(`active_flag`,`created_date`,`updated_date`,`name`)
values (1,now(),now(),'Medical O2 Kit'),
(1,now(),now(),'Medical Gases'),
(1,now(),now(),'Industrial Gases'),
(1,now(),now(), 'Mixture Gases');

insert into `gas_master`(`active_flag`,`created_date`,`updated_date`,`description`,`is_avaliable`,`name`,`price`,`type_id`)
values (1,now(),now(),'Medical O2 Kit ',1,'Medical O2 Kit',1200,1),
(1,now(),now(),'Medical Oxygen IP',1,'Medical Oxygen IP',200,2),
(1,now(),now(),'Nitrous Oxide',1,'Nitrous Oxide',340,2),
(1,now(),now(),'Carbon Dioxide',1,'Carbon Dioxide (CO2)',900,2),
(1,now(),now(),'Commercial Oxygen',1,'Commercial Oxygen',900,3),
(1,now(),now(),'Nitrogen',1,'Nitrogen',900,3),
(1,now(),now(),'Argon',1,'Argon',900,3),
(1,now(),now(),'Carbon Dioxide (CO2)',1,'Carbon Dioxide (CO2)',900,3),
(1,now(),now(),'Liquid Oxygen',1,'Liquid Oxygen',900,3),
(1,now(),now(),'Liquid Nitrogen',1,'Liquid Nitrogen',900,3),
(1,now(),now(),'Hydrogen',1,'Hydrogen',900,3),
(1,now(),now(),'Hydrogen Bulk',1,'Hydrogen Bulk',900,3),
(1,now(),now(),'Argon CO2 Mixture',1,'Argon CO2 Mixture',900,4),
(1,now(),now(),'Nitrogen CO2 Mixture',1,'Nitrogen CO2 Mixture',900,4);