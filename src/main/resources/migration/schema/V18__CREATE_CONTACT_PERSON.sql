CREATE TABLE `contact_person` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `active_flag` tinyint(1) DEFAULT '1',
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `mobile_number` bigint(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `user_id`bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_USER_CONTACT_PERSON` FOREIGN KEY (`user_id`) REFERENCES `contact_person`(`id`)
);