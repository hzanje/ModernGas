insert into `category_master`(`active_flag`,`created_date`,`updated_date`,`name`)
values (1,now(),now(),'Medical O2 Kit'),
(1,now(),now(),'Industrial Gases'),
(1,now(),now(),'Chemicals');

insert into `gas_master`(`active_flag`,`created_date`,`updated_date`,`description`,`is_avaliable`,`name`,`price`,`type_id`)
values (1,now(),now(),'This is gas. Medical Kit',1,'Medical O2 Kit',1200,1),
(1,now(),now(),'This is gas. Medical Oxygen',1,'Medical Oxygen',200,2),
(1,now(),now(),'This is gas. Nitrogen',1,'Nitrogen',340,2),
(1,now(),now(),'This is gas. Argon',1,'Argon',900,2),
(1,now(),now(),'This is gas. Hydrogen',1,'Hydrogen',100,2),
(1,now(),now(),'This is gas. Hydrogen Peroxide',1,'Hydrogen Peroxide',20,3);