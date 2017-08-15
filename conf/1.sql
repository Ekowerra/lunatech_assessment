# --- !Ups
create table AIRPORT(
    id      bigint,
    ident       varchar,
    type        varchar,
    name        varchar,
    latitude_deg        double precision,
    longitude_deg       double precision,
    elevation_ft        integer,
    continent           varchar,
    iso_country         varchar,
    iso_region          varchar,
    municipality        varchar,
    scheduled_service   varchar,
    gps_code            varchar,
    iata_code           varchar,
    local_code          varchar,
    home_link           varchar,
    wikipedia_link      varchar,
    keywords        varchar

);

create table COUNTRY(
    id      bigint,
    code        varchar,
    name       varchar,
    continent       varchar,
    wikipedia_link      varchar,
    keywords        varchar
);

create table RUNWAY(
    id      bigint,
    airport_ref     bigint,
    airport_ident       varchar,
    length_ft       integer,
    width_ft        integer,
    surface     varchar,
    lighted     integer,
    closed      integer,
    le_ident        varchar,
    le_latitude_deg     double precision,
    le_longitude_deg        double precision,
    le_elevation_ft     integer,
    le_heading_degT     double precision,
    le_displaced_threshold_ft   integer,
    he_ident        varchar,
    he_latitude_deg     double precision,
    he_longitude_deg    double precision,
    he_elevation_ft     integer,
    he_heading_degT     varchar,
    he_displaced_threshold_ft   integer
);

# --- !Downs

drop table if exists AIRPORT;
drop table if exists COUNTRY;
drop table if exists RUNWAY;
