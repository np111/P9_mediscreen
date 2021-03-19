CREATE TABLE `patients` (
  `id` BINARY(16) NOT NULL,
  `first_name` VARCHAR(191) CHARACTER SET `utf8mb4` COLLATE `utf8mb4_unicode_ci` NOT NULL,
  `last_name` VARCHAR(191) CHARACTER SET `utf8mb4` COLLATE `utf8mb4_unicode_ci` NOT NULL,
  `birthdate` DATE,
  `gender` VARCHAR(6) CHARACTER SET `ascii` COLLATE `ascii_bin`,
  `phone` VARCHAR(16) CHARACTER SET `ascii` COLLATE `ascii_bin` COMMENT '[country-calling-code][space][number]'
    CHECK (`phone` IS NULL OR `phone` REGEXP '^(?=.{3,16}$)[0-9]{1,4} [0-9]+$'),
  `address` VARCHAR(255) CHARACTER SET `utf8mb4` COLLATE `utf8mb4_unicode_ci`,
  `city` VARCHAR(255) CHARACTER SET `utf8mb4` COLLATE `utf8mb4_unicode_ci`,
  `postal_code` VARCHAR(16) CHARACTER SET `ascii` COLLATE `ascii_general_ci`,
  `state` VARCHAR(255) CHARACTER SET `utf8mb4` COLLATE `utf8mb4_unicode_ci`,
  `country_code` VARCHAR(2) CHARACTER SET `ascii` COLLATE `ascii_bin` COMMENT 'ISO 3166-1 alpha-2',

  PRIMARY KEY (`id`),
  INDEX `first_name` (`first_name`),
  INDEX `last_name` (`last_name`)
) ENGINE = InnoDB DEFAULT CHARSET = `utf8mb4` ROW_FORMAT = COMPACT;
