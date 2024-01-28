create table if not exists "user"
(
    id         bigserial,
    email      varchar(300) not null,
    username   varchar(300) not null,
    created_at timestamp    not null default now(),
    updated_at timestamp    not null default now(),

    constraint email_uq unique (email),
    constraint user_pk PRIMARY KEY (id)
);

create table if not exists profile
(
    user_id      bigint,
    title        varchar(256) not null,
    department   varchar(256) not null,
    picture_url  varchar(500),
    phone_number varchar(100) not null,
    created_at   timestamp    not null default now(),
    updated_at   timestamp    not null default now(),

    constraint profile_pk primary key (user_id),
    constraint profile_user_fk foreign key (user_id) references "user"
);

create table if not exists board
(
    id         bigserial,
    name       varchar(256) not null,
    created_at timestamp    not null default now(),
    updated_at timestamp    not null default now(),

    constraint board_pk primary key (id),
    constraint name_uq unique (name)
);

create table if not exists user_board
(
    user_id  bigint not null,
    board_id bigint not null,

    constraint user_board_pk primary key (user_id, board_id),
    constraint user_board_user_fk foreign key (user_id) references "user",
    constraint user_board_board_fk foreign key (board_id) references board
);

create type ticket_status as enum ('TODO', 'IN PROGRESS', 'DONE');

create table if not exists ticket
(
    id          bigserial,
    title       varchar(256)  not null,
    description text          not null,
    status      ticket_status not null default 'TODO',
    user_id     bigint        not null,
    board_id    bigint        not null,

    constraint ticket_pk primary key (id),
    constraint ticket_user_fk foreign key (user_id) references "user",
    constraint ticket_board_fk foreign key (board_id) references board
);

create table if not exists comment
(
    id         bigserial,
    body       text      not null,
    user_id    bigint    not null,
    ticket_id  bigint    not null,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now(),

    constraint comment_pk primary key (id),
    constraint comment_user_fk foreign key (user_id) references "user",
    constraint comment_ticket_fk foreign key (ticket_id) references ticket
)