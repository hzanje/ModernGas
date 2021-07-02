  CREATE TABLE `admin_gas_mapping` (
  	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  	`active_flag` TINYINT(1) DEFAULT '1' NULL,
  	`created_date` DATETIME NULL,
  	`updated_date` DATETIME NULL,
  	`admin_id` BIGINT(20) NOT NULL,
  	`gas_id` BIGINT(20) NOT NULL,
  	`gas_name` VARCHAR(255) NOT NULL,
  	`description` LONGTEXT NULL,
  	`price` INT(11) NULL,
  	PRIMARY KEY (`id`)
  );