CREATE TABLE `resource_centre`(
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `active_flag` TINYINT(1),
  `created_date` DATETIME,
  `updated_date` DATETIME,
  `name` TEXT(255) NOT NULL,
  `alias` TEXT(255) NOT NULL,
  `user_id` BIGINT(20),
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_USER_RESOURCE_ID` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
);