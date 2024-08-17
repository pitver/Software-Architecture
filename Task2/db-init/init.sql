CREATE EXTENSION IF NOT EXISTS dblink;

DO $$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = 'DeviceManagement') THEN
            PERFORM dblink_exec('dbname=mydb', 'CREATE DATABASE "DeviceManagement"');
        END IF;
    END
$$;

DO $$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = 'telemetry') THEN
            PERFORM dblink_exec('dbname=mydb', 'CREATE DATABASE "telemetry"');
        END IF;
    END
$$;
DO $$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = 'kong') THEN
            PERFORM dblink_exec('dbname=mydb', 'CREATE DATABASE "kong"');
        END IF;
    END
$$;