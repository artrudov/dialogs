create table messages
(
    id        uuid not null default gen_random_uuid(),

    -- md5 хэш из строки вида 1232:1232 - айди отправителя и получателя
    sha_code  text,

    user_from int  not null,
    user_to   int  not null,
    text      text not null,
    sent_at   timestamp     default now(),
    primary key (sha_code, id)
);

SELECT create_distributed_table('messages', 'sha_code');