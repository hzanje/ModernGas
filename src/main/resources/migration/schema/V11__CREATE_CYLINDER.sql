CREATE TABLE `cylinder`(
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `active_flag` TINYINT(1),
  `created_date` DATETIME,
  `updated_date` DATETIME,
  `cylinder_code` TEXT(255) NOT NULL,
  `cylinder_status_id` BIGINT(20) NOT NULL,
  `assigned_user_id` BIGINT(20),
  `assigned_user_name` TEXT(255),
  `user_id` BIGINT(20),
  `cylinder_detail_id` BIGINT(20),
  `manufacturer` TEXT(255),
  `manufacturing_date` DATETIME,
  `expiry_date` DATETIME,
  `last_service` DATETIME,
  `next_service` DATETIME,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_USER_ID` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
);