CREATE TABLE `gas_image_mapping`(
`admin_gas_id` BIGINT(20) NOT NULL,
`image_id` BIGINT(20) NOT NULL,
FOREIGN KEY (`admin_gas_id`) REFERENCES `admin_gas_mapping`(`id`),
FOREIGN KEY (`image_id`) REFERENCES `gas_image`(`id`) );