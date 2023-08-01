create table notification
(
    id bigserial primary key,
    title  text,
    status text default 'open',
    description text,
    owner text,
    visibility text,
    type text,
    lock text
);
