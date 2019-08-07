SET search_path = dokha;

--Заведение
create table dokha.s_store
(
    id       bigserial not null
        constraint s_store_pkey
            primary key,
    location varchar(255),
    name     varchar(255),
    photo    bytea
);

--Место резерва (столики)
create table dokha.s_place_reservation
(
    id          bigserial not null
        constraint s_place_reservation_pkey
            primary key,
    description varchar(255),
    seats_count integer,
    store_id    bigint
        constraint s_place_reservation_store_fk
            references s_store
);


--Справочник дни недели
create table dokha.s_day_of_week
(
    id   bigserial not null
        constraint s_day_of_week_pkey
            primary key,
    name varchar(255)
);

--Конфиг расписаний
create table dokha.s_timetable_config
(
    id             bigserial not null
        constraint s_timetable_config_pkey
            primary key,
    end_time       time,
    start_time     time,
    day_of_week_id bigint
        constraint s_timetable_config_day_of_week_fk
            references s_day_of_week,
    store_id       bigint
        constraint s_timetable_config_store_fk
            references s_store
);

--Расписание заведения
create table dokha.timetable
(
    id           bigserial not null
        constraint timetable_pkey
            primary key,
    end_time     time,
    start_time   time,
    working_date date,
    working_day  boolean,
    store_id     bigint
        constraint timetable_store_fk
            references s_store
);

--Пользователи
create table dokha."user"
(
    id       bigserial not null
        constraint user_pkey
            primary key,
    login    varchar(255),
    password varchar(255)
);

--Роли пользователя
create table dokha.user_role
(
    user_id bigint not null
        constraint user_role_user_fk
            references "user",
    roles   integer
);

--Брони
create table dokha.reservation
(
    id               bigserial not null
        constraint reservation_pkey
            primary key,
    reservation_time bigint,
    place_id         bigint
        constraint s_place_reservation_reservation_fk
            references s_place_reservation,
    timetable_id     bigint
        constraint timetable_reservation_fk
            references timetable,
    user_id          bigint
        constraint user_reservation_fk
            references "user"
);

--Должеости персонала
create table dokha.s_personal_position
(
    id   bigserial not null
        constraint s_personal_position_pkey
            primary key,
    name varchar(255)
);

--Персонал
create table dokha.s_personal
(
    id          bigserial not null
        constraint s_personal_pkey
            primary key,
    age         integer,
    first_name  varchar(255),
    last_name   varchar(255),
    position_id bigint
        constraint s_personal_position_personal_fk
            references s_personal_position
);
