CREATE TABLE .`gas_image_mapping`(
`gas_id` BIGINT(20) NOT NULL,
`image_id` BIGINT(20) NOT NULL,
FOREIGN KEY (`gas_id`) REFERENCES `gas_master`(`id`),
FOREIGN KEY (`image_id`) REFERENCES `gas_image`(`id`) );