DO $$ BEGIN IF NOT EXISTS (
    SELECT
    FROM pg_roles
    WHERE rolname = 'tradingdb'
) THEN CREATE ROLE tradingdb WITH LOGIN PASSWORD 'tradingPass';
ALTER ROLE tradingdb CREATEDB;
END IF;
END $$;
GRANT ALL PRIVILEGES ON DATABASE tradingdb TO tradingdb;