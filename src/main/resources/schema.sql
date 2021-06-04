drop table if exists "Organizations";
drop table if exists customer_status;
drop sequence if exists Customers_id_seq;
drop sequence if exists customer_status_customer_status_id_seq;

CREATE SEQUENCE Customers_id_seq START 1 INCREMENT 1;
CREATE SEQUENCE customer_status_customer_status_id_seq START 1 INCREMENT 1;

CREATE TABLE customer_status (
	customer_status_id bigserial NOT NULL,
	customer_status_name text NOT NULL,
	CONSTRAINT customer_status_customer_status_name_key UNIQUE (customer_status_name),
	CONSTRAINT pk_customer_status PRIMARY KEY (customer_status_id)
);
CREATE TABLE "Organizations" (
	id int8 NOT NULL ,
	"name" varchar(255) NULL,
	created_at timestamptz NOT NULL DEFAULT now(),
	updated_at timestamptz NOT NULL DEFAULT now(),
	replay bool NOT NULL DEFAULT false,
	replay_flags int4 NULL DEFAULT 12,
	entity_overwrites text NULL,
	proc_disabled bool NOT NULL DEFAULT false,
	disabled_reason text NULL,
	disable_if_err bool NOT NULL DEFAULT false,
	alias_organization_id int8 NULL,
	use_case_sensitive_ids bool NOT NULL DEFAULT true,
	customer bool NULL DEFAULT false,
	proc_metadata jsonb NULL,
	retain bool NULL,
	customer_status int8 NOT NULL DEFAULT 1,
	um_owner text NULL,
	customer_contact text NULL,
	"version" int8 NOT NULL DEFAULT 1,
	configurations jsonb NULL,
	CONSTRAINT "Organizations_pkey" PRIMARY KEY (id)
);
CREATE INDEX codemonkey_organization_test_idx ON "Organizations" USING btree (proc_disabled, id);

ALTER TABLE "Organizations" ADD CONSTRAINT fk_customer_status FOREIGN KEY (customer_status) REFERENCES customer_status(customer_status_id);
