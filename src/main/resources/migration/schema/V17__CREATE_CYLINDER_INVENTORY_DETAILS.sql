CREATE TABLE `cylinder_inventory_details`(
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `active_flag` TINYINT(1),
  `created_date` DATETIME,
  `updated_date` DATETIME,
  `is_transit` TINYINT(1) DEFAULT 0 NULL,
  `delivery_vehicle_id` BIGINT(20) NULL,
  `resource_centre_id` BIGINT(20) NULL,
  `inventory_status_id` BIGINT(20) NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_DELIVERY_VEHICLE_ID` FOREIGN KEY (`delivery_vehicle_id`) REFERENCES `delivery_vehicle`(id),
  CONSTRAINT `FK_RESOURCE_CENTRE_ID` FOREIGN KEY (`resource_centre_id`) REFERENCES `resource_centre`(id)
);