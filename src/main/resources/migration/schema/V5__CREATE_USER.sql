CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `active_flag` tinyint(1) DEFAULT '1',
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `company_name` varchar(255) DEFAULT NULL,
  `contact_person` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `mobile_number` bigint(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` longtext,
  `address_id` bigint(20) DEFAULT NULL,
  `employer_id` BIGINT(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_USER_ADDRESS` FOREIGN KEY (`address_id`) REFERENCES `address`(`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4000;