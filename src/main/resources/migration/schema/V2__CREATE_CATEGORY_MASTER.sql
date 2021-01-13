CREATE TABLE `category_master` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `active_flag` tinyint(1) DEFAULT '1',
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);
