CREATE TABLE `user_privilege` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
   `role_id` BIGINT(20),
   `privilege` VARCHAR(255) NOT NULL,
   PRIMARY KEY (`id`),
   CONSTRAINT `FK_USER_PRIVILEGE_ROLE_ID` FOREIGN KEY (`role_id`) REFERENCES `user_roles`(`id`)
);