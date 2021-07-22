ALTER TABLE `admin_gas_mapping` ADD COLUMN `category_id` BIGINT(20) NOT NULL AFTER `gas_name`;
ALTER TABLE `admin_gas_mapping` ADD COLUMN `category_name` VARCHAR(255) NOT NULL AFTER `category_id`;
ALTER TABLE `admin_gas_mapping` MODIFY COLUMN `price` FLOAT;