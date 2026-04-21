-- PostgreSQL database
ALTER TABLE <prefix>parties ADD COLUMN "tax_last_payer" CHAR(36) DEFAULT NULL;

