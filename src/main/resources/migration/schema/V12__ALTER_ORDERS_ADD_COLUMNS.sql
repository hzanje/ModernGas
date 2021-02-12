ALTER TABLE `orders` ADD COLUMN `cancellation_date` DATETIME NULL AFTER `loaded_date`,
ADD COLUMN `delivery_vehicle` BIGINT(20) NULL AFTER `cancellation_date`;