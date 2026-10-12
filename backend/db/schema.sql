-- Pawly schema for MySQL 8.4.
-- Apply to an already-created, verified-empty pawly_db database.
-- Column nullability and types mirror the current JPA entities.

CREATE TABLE `users` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `uk_users_email` UNIQUE (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `pets` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    `species` VARCHAR(255) NOT NULL,
    `breed` VARCHAR(255) NULL,
    `age` INT NULL,
    `gender` VARCHAR(255) NULL,
    `user_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_pets_user_id` (`user_id`),
    CONSTRAINT `fk_pets_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `appointments` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `pet_id` BIGINT NOT NULL,
    `date` DATE NULL,
    `time` TIME(6) NULL,
    `veterinarian` VARCHAR(255) NULL,
    `clinic` VARCHAR(255) NULL,
    `reason` VARCHAR(255) NULL,
    `status` VARCHAR(255) NULL,
    `notes` VARCHAR(255) NULL,
    PRIMARY KEY (`id`),
    KEY `idx_appointments_pet_id` (`pet_id`),
    CONSTRAINT `fk_appointments_pet` FOREIGN KEY (`pet_id`) REFERENCES `pets` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `health_records` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `pet_id` BIGINT NOT NULL,
    `record_type` VARCHAR(255) NULL,
    `title` VARCHAR(255) NULL,
    `date` DATE NULL,
    `description` VARCHAR(255) NULL,
    `veterinarian` VARCHAR(255) NULL,
    `clinic` VARCHAR(255) NULL,
    `status` VARCHAR(255) NULL,
    `notes` VARCHAR(255) NULL,
    PRIMARY KEY (`id`),
    KEY `idx_health_records_pet_id` (`pet_id`),
    CONSTRAINT `fk_health_records_pet` FOREIGN KEY (`pet_id`) REFERENCES `pets` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;