-- Структура скрипта:
--
-- (1) Дроп триггерам (пропущенно)
-- (2) Дроп функциям  (пропущенно)
-- (3) Дроп индексам, таблицам, сиквенсам
-- (4) Дроп схеме
-- (5) Дроп базе данных
--
-- (6) Содание базы данных
-- (7) Создание схемы
-- (8) Создание сиквенсов, таблиц, индексов, гранты
-- (9) Создание функций (пропущенно)
-- (10) Создание триггеров (пропущенно)
-- (11) Ввод первичных данных
--
-- Всё скопом не запускать!
-- Берём нужный фрагмент, копи-пастим в админку СУБД и запускаем там.


SET search_path TO sch_planets, public; -- sets only for current session

DROP TABLE IF EXISTS planets;
DROP SEQUENCE IF EXISTS planets_pk_seq;

--------------------------------------------------------------------------
--------------------------------------------------------------------------
--------------------------------------------------------------------------
--------------------------------------------------------------------------

DROP SCHEMA IF EXISTS sch_planets CASCADE;

DROP TABLESPACE IF EXISTS tabsp_planets;
DROP TABLESPACE IF EXISTS tabsp_planets_idxs;

REVOKE CONNECT ON DATABASE planets FROM user_planets_sch_owner;
REVOKE CONNECT ON DATABASE planets FROM user_planets_data_admin;
REVOKE CONNECT ON DATABASE planets FROM user_planets_data_reader;

DROP ROLE IF EXISTS user_planets_sch_owner;
DROP ROLE IF EXISTS user_planets_data_admin;
DROP ROLE IF EXISTS user_planets_data_reader;

--------------------------------------------------------------------------
--------------------------------------------------------------------------
--------------------------------------------------------------------------
--------------------------------------------------------------------------

DROP DATABASE IF EXISTS planets;

DROP TABLESPACE IF EXISTS tabsp_planets_dflt;
DROP ROLE IF EXISTS user_planets_owner;

--------------------------------------------------------------------------
--------------------------------------------------------------------------
--------------------------------------------------------------------------
--------------------------------------------------------------------------

CREATE ROLE user_planets_owner
   WITH SUPERUSER
        NOCREATEDB
        LOGIN
        CREATEROLE
        UNENCRYPTED PASSWORD 'db_owner_password';

-- указать свой путь для тэйблспэйсов, директория должна существовать

CREATE TABLESPACE tabsp_planets_dflt
   OWNER user_planets_owner
   LOCATION 'e:\\java\\workspace\\planets\\out\\db\\default.data';

-------------------------

CREATE DATABASE planets
  WITH OWNER = user_planets_owner
       ENCODING = 'UTF8'
       TABLESPACE = tabsp_planets_dflt;

-------------------------
-- (1) case sensetive (2) postgres lowercases real names
\c planets

CREATE LANGUAGE plpgsql;

--------------------------------------------------------------------------
--------------------------------------------------------------------------
--------------------------------------------------------------------------
--------------------------------------------------------------------------

CREATE ROLE user_planets_sch_owner
   WITH LOGIN
        UNENCRYPTED PASSWORD 'user_planets_owner';

CREATE ROLE user_planets_data_admin
   WITH LOGIN
        UNENCRYPTED PASSWORD 'user_planets_data_admin';

CREATE ROLE user_planets_data_reader
   WITH LOGIN
        UNENCRYPTED PASSWORD 'user_planets_data_reader';

-- указать свой путь для тэйблспэйсов, директория должна существовать

CREATE TABLESPACE tabsp_planets
        OWNER user_planets_sch_owner
        LOCATION 'E:\\Java\\workspace\\planets\\out\\db\\schema\\tabsp.data';

CREATE TABLESPACE tabsp_planets_pwa_idxs
        OWNER user_planets_sch_owner
        LOCATION 'E:\\Java\\workspace\\planets\\out\\db\\schema\\tabsp_idxs.data';

GRANT CONNECT ON DATABASE planets TO user_planets_data_admin, user_planets_sch_owner, user_planets_data_reader;

CREATE SCHEMA sch_planets AUTHORIZATION user_planets_sch_owner;

GRANT USAGE ON SCHEMA sch_planets TO user_planets_data_admin, user_planets_data_reader;
ALTER ROLE user_planets_sch_owner   SET search_path TO sch_planets, public;
ALTER ROLE user_planets_data_admin  SET search_path TO sch_planets, public;
ALTER ROLE user_planets_data_reader SET search_path TO sch_planets, public;

--------------------------------------------------------------------------
--------------------------------------------------------------------------
--------------------------------------------------------------------------
--------------------------------------------------------------------------

SET search_path TO sch_planets, public; -- sets only for current session

CREATE SEQUENCE planets_pk_seq INCREMENT BY 1
       MINVALUE 1
       START WITH 1
       NO CYCLE;

GRANT SELECT, UPDATE ON SEQUENCE planets_pk_seq TO user_planets_data_admin;

CREATE TABLE planets (
         planet_id            integer NOT NULL DEFAULT nextval('planets_pk_seq') PRIMARY KEY
       , name                 varchar NOT NULL
       , dist_to_earth        float   NOT NULL
       , discoverer_name      varchar     NULL DEFAULT '<неизвестно>'
       , diameter             float   NOT NULL
       , atmosphere           boolean NOT NULL
       , date_created         timestamp without time zone NOT NULL DEFAULT now()
       , is_deleted           boolean NOT NULL DEFAULT FALSE
) TABLESPACE tabsp_planets;

GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE planets TO user_planets_data_admin;
GRANT SELECT                         ON TABLE planets TO user_planets_data_reader;

--------------------------------------------------------------------------
--------------------------------------------------------------------------
--------------------------------------------------------------------------
--------------------------------------------------------------------------

INSERT INTO planets (name, dist_to_earth, discoverer_name, diameter, atmosphere)
VALUES ('Меркурий', 10, '<неизвестно>', 10, false)
     , ('Венера', 5, '<неизвестно>', 10, true)
     , ('Земля', 0, '<неизвестно>', 10, true)
     , ('Марс', 5, '<неизвестно>', 10, true)
     , ('Юпитер', 15, '<неизвестно>', 500, true)
     ;
