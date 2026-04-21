-- MariaDB database
ALTER TABLE `<prefix>players` ADD `xp_contribution` INTEGER DEFAULT 1;
ALTER TABLE `<prefix>parties` ADD `created_at` BIGINT DEFAULT 0;
ALTER TABLE `<prefix>parties` ADD `tax_last_paid` BIGINT DEFAULT 0;
