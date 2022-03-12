CREATE TABLE `user_roles` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT(20),
    `role` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_USER_ROLE_ID` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
);