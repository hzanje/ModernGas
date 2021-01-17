CREATE TABLE `cart` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `active_flag` tinyint(1) DEFAULT '1',
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `cylinder_type` bigint(20) DEFAULT NULL,
  `is_refill` tinyint(1) DEFAULT '1',
  `price` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `refill_count` int(11) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `gas_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_CART_GAS_MASTER` FOREIGN KEY (gas_id) REFERENCES `gas_master`(`id`)
);