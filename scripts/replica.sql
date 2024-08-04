CREATE PUBLICATION goalscore_publication FOR ALL TABLES;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO replicator;
ALTER USER replicator WITH REPLICATION LOGIN;

  show wal_level;
  show all
ALTER SYSTEM SET wal_level = 'logical';
SELECT pg_reload_conf();

SELECT * FROM pg_replication_slots;
SELECT * FROM pg_stat_replication;

insert into public.areas values (2999, 'replica');
insert into public.areas values (2998, 'replica2');