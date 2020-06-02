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