  CREATE TABLE `admin_gas_cylinder_type_mapping` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  	`admin_gas_id` BIGINT(20) NOT NULL,
  	`cylinder_type` bigint(20) DEFAULT NULL,
  	 PRIMARY KEY (`id`)
  );