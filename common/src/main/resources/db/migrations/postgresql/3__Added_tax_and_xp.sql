-- PostgreSQL database
ALTER TABLE <prefix>players ADD COLUMN "xp_contribution" INTEGER DEFAULT 1;
ALTER TABLE <prefix>parties ADD COLUMN "created_at" BIGINT DEFAULT 0;
ALTER TABLE <prefix>parties ADD COLUMN "tax_last_paid" BIGINT DEFAULT 0;


