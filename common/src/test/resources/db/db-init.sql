CREATE SCHEMA IF NOT EXISTS destination_lookup;

ALTER ROLE postgres SET search_path TO destination_lookup;

CREATE USER destination_lookup_writer_ddl WITH PASSWORD 'destination_lookup_writer_ddl';
REVOKE ALL PRIVILEGES ON SCHEMA PUBLIC FROM destination_lookup_writer_ddl;
GRANT CONNECT ON DATABASE destination_lookup TO destination_lookup_writer_ddl;
ALTER ROLE destination_lookup_writer_ddl SET search_path TO destination_lookup;
GRANT ALL PRIVILEGES ON SCHEMA destination_lookup TO destination_lookup_writer_ddl;

CREATE USER destination_lookup_reader WITH PASSWORD 'destination_lookup_reader';
REVOKE ALL PRIVILEGES ON SCHEMA PUBLIC FROM destination_lookup_reader;
GRANT CONNECT ON DATABASE destination_lookup TO destination_lookup_reader;
GRANT USAGE ON SCHEMA destination_lookup TO destination_lookup_reader;
ALTER ROLE destination_lookup_reader SET search_path TO destination_lookup;

CREATE USER destination_lookup_writer WITH PASSWORD 'destination_lookup_writer';
REVOKE ALL PRIVILEGES ON SCHEMA PUBLIC FROM destination_lookup_writer;
GRANT CONNECT ON DATABASE destination_lookup TO destination_lookup_writer;
GRANT USAGE ON SCHEMA destination_lookup TO destination_lookup_writer;
ALTER ROLE destination_lookup_writer SET search_path TO destination_lookup;

CREATE USER destination_lookup_purger WITH PASSWORD 'destination_lookup_purger';
REVOKE ALL PRIVILEGES ON SCHEMA PUBLIC FROM destination_lookup_purger;
GRANT CONNECT ON DATABASE destination_lookup TO destination_lookup_purger;
GRANT USAGE ON SCHEMA destination_lookup TO destination_lookup_purger;
ALTER ROLE destination_lookup_purger SET search_path TO destination_lookup;
