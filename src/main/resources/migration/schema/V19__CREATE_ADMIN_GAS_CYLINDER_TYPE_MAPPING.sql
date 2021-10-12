  CREATE TABLE `admin_gas_cylinder_type_mapping` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  	`admin_gas_id` BIGINT(20) NOT NULL,
  	`cylinder_type` bigint(20) DEFAULT NULL,
  	 PRIMARY KEY (`id`),
  	 CONSTRAINT `FK_ADMIN_GAS_CYLINDER_TYPE_ID` FOREIGN KEY (`admin_gas_id`) REFERENCES `admin_gas_mapping`(`id`)
  );