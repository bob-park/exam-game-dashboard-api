create table games
(
    id                 bigserial               not null primary key,
    name               varchar(100)            not null,
    description        text,
    game_date          date                    not null,
    created_date       timestamp default now() not null,
    last_modified_date timestamp
);

create table game_dashboards
(
    id                 bigserial               not null primary key,
    game_id            bigint                  not null,
    home_score         bigint    default 0     not null,
    away_score         bigint    default 0     not null,
    created_date       timestamp default now() not null,
    last_modified_date timestamp,

    foreign key (game_id) references games (id)
)