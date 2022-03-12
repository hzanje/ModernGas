CREATE TABLE `gas_master` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `active_flag` tinyint(1) DEFAULT '1',
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `description` longtext,
  `is_avaliable` tinyint(1) DEFAULT '1',
  `name` varchar(255) DEFAULT NULL,
  `price` int(11) DEFAULT NULL,
  `type_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_GAS_CATEGORY_MASTER` FOREIGN KEY (`type_id`) REFERENCES `category_master`(`id`)
);