  CREATE TABLE `admin_gas_mapping` (
  	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  	`active_flag` TINYINT(1) DEFAULT '1' NULL,
  	`created_date` DATETIME NULL,
  	`updated_date` DATETIME NULL,
  	`admin_id` BIGINT(20) NOT NULL,
  	`gas_id` BIGINT(20) NOT NULL,
  	`gas_name` VARCHAR(255) NOT NULL,
  	`category_id` BIGINT(20) NOT NULL,
  	`category_name` VARCHAR(255) NOT NULL,
  	`description` longtext NOT NULL,
  	PRIMARY KEY (`id`),
  	CONSTRAINT `FK_ADMIN_GAS_ID` FOREIGN KEY (`admin_id`) REFERENCES `user`(`id`)
  );