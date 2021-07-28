DROP TABLE message IF EXISTS;

create table message
(
    message_id serial not null
        constraint message_pk
            primary key,
    data       text
);

alter table message
    owner to postgres;

create unique index message_message_id_uindex
    on message (message_id);