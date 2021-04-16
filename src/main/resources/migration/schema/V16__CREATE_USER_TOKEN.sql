CREATE TABLE `user_token` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT(20) NOT NULL,
    `token` VARCHAR(255) NOT NULL,
    `expired_date` datetime DEFAULT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_USER_TOKEN_ID` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
);
