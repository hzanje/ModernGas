CREATE TABLE `cylinder`(
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `active_flag` TINYINT(1),
  `created_date` DATETIME,
  `updated_date` DATETIME,
  `cylinder_code` TEXT(255) NOT NULL,
  `cylinder_status_id` BIGINT(20) NOT NULL,
  `user_id` BIGINT(20),
  PRIMARY KEY (`id`)
);