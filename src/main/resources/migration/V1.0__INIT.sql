create table "public"."t_user"
(
    "id"           SERIAL PRIMARY KEY,
    "address"      varchar(255) COLLATE "pg_catalog"."default",
    "name"         varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
    "age"          INT,
    "email"        varchar(32) COLLATE "pg_catalog"."default",
    "phone_number" varchar(16) COLLATE "pg_catalog"."default",
    created_at     TIMESTAMP WITH TIME ZONE,
    updated_at     TIMESTAMP WITH TIME ZONE
);

