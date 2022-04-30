  CREATE TABLE `user_gas_cylinder_type_mapping` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  	`user_gas_id` BIGINT(20) NOT NULL,
  	`cylinder_type` bigint(20) DEFAULT NULL,
  	`price` float(11) NOT NULL,
  	 PRIMARY KEY (`id`),
  	 CONSTRAINT `FK_USER_GAS_CYLINDER_TYPE_ID` FOREIGN KEY (`user_gas_id`) REFERENCES `user_gas_mapping`(`id`)
  );