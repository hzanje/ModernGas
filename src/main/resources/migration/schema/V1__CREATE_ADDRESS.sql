CREATE TABLE `address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `active_flag` tinyint(1) DEFAULT '1',
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `address_1` varchar(255) DEFAULT NULL,
  `address_2` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `landmark` varchar(255) DEFAULT NULL,
  `pincode` int(11) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);