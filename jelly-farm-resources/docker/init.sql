CREATE SCHEMA IF NOT EXISTS jelly;

CREATE TABLE IF NOT EXISTS jelly.jelly_cage (
	cage_number int8 NOT NULL,
	habitat_name varchar(255) NULL,
	CONSTRAINT jelly_cage_pkey PRIMARY KEY (cage_number)
);

CREATE TABLE IF NOT EXISTS jelly.jelly_farm (
	id uuid NOT NULL,
	cage_number int8 NULL,
	gender varchar(255) NOT NULL,
	color varchar(255) NOT NULL,
	date_time_freed timestamp NULL,
	CONSTRAINT jelly_farm_pkey PRIMARY KEY (id)
);

INSERT INTO jelly.jelly_cage (cage_number, habitat_name) VALUES (1, 'Sunny Beach');
INSERT INTO jelly.jelly_cage (cage_number, habitat_name) VALUES (2, 'Lush Forest');
INSERT INTO jelly.jelly_cage (cage_number, habitat_name) VALUES (3, 'Urban Terrace');
INSERT INTO jelly.jelly_cage (cage_number, habitat_name) VALUES (4, 'Seaside Cliffs');

CREATE USER user WITH ENCRYPTED PASSWORD 'password';

GRANT SELECT, INSERT, UPDATE, DELETE
   ON ALL TABLES IN SCHEMA jelly 
   TO user;

