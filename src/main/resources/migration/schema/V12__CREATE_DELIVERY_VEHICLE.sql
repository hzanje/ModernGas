CREATE TABLE `delivery_vehicle`(
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `active_flag` TINYINT(1) DEFAULT 1,
  `created_date` DATETIME,
  `updated_date` DATETIME,
  `user_id` BIGINT(20) NOT NULL,
  `name` VARCHAR(255),
  `color` VARCHAR(255),
  `number` VARCHAR(15) NOT NULL,
  PRIMARY KEY (`id`)
);