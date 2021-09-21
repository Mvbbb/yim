create
database yim;
use
yim;
create table if not exists yim_friend_relation
(
    id
    bigint
    auto_increment
    primary
    key,
    user_id_1
    varchar
(
    100
) not null,
    user_id_2 varchar
(
    100
) not null,
    create_time timestamp default CURRENT_TIMESTAMP not null,
    update_time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    deleted tinyint default 0 not null
    );

create table if not exists yim_group
(
    group_id varchar
(
    100
) not null
    primary key,
    group_name varchar
(
    50
) not null,
    avatar varchar
(
    255
) not null,
    owner_uid varchar
(
    100
) not null comment '群主',
    user_cnt int not null,
    create_time timestamp default CURRENT_TIMESTAMP not null,
    update_time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    deleted tinyint default 0 not null
    );

create table if not exists yim_msg_recv
(
    id
    bigint
    auto_increment
    primary
    key,
    client_msg_id
    varchar
(
    100
) not null,
    server_msg_id bigint unsigned not null comment '消息服务端id',
    from_uid varchar
(
    20
) not null comment '发送者',
    to_uid varchar
(
    20
) null comment '接收者',
    session_type tinyint not null,
    group_id varchar
(
    20
) null comment '群 id',
    msg_type tinyint not null comment '消息类型',
    msg_data varchar
(
    266
) not null comment '消息内容',
    timestamp timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '服务器收到消息的时间戳',
    delivered tinyint not null comment '0 未送达，1送达',
    created timestamp default CURRENT_TIMESTAMP not null,
    updated timestamp default CURRENT_TIMESTAMP not null
                                                           on update CURRENT_TIMESTAMP,
    deleted tinyint default 0 not null
    );

create table if not exists yim_msg_send
(
    id
    bigint
    auto_increment
    primary
    key,
    client_msg_id
    varchar
(
    64
) not null,
    server_msg_id bigint not null,
    from_uid varchar
(
    100
) not null,
    to_uid varchar
(
    100
) null,
    session_type tinyint not null,
    group_id varchar
(
    100
) null,
    msg_data varchar
(
    255
) not null comment '消息内容',
    msg_type tinyint not null comment '消息类型',
    timestamp timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '服务器收到消息的时间',
    created timestamp default CURRENT_TIMESTAMP not null,
    `update` timestamp default CURRENT_TIMESTAMP not null
                                                           on update CURRENT_TIMESTAMP,
    deleted tinyint not null
    );

create table if not exists yim_user
(
    user_id varchar
(
    100
) not null
    primary key,
    username varchar
(
    50
) not null comment '用户名',
    avatar varchar
(
    100
) null comment '头像',
    password varchar
(
    128
) not null comment '密码',
    `create` timestamp default CURRENT_TIMESTAMP not null,
    `update` timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    deleted tinyint default 0 not null comment '逻辑删除',
    constraint username
    unique
(
    username
)
    );

create table if not exists yim_user_group_relation
(
    id
    bigint
    auto_increment
    primary
    key,
    user_id
    varchar
(
    100
) not null,
    group_id varchar
(
    100
) not null,
    last_acked_msgid bigint default -1 not null,
    create_time timestamp default CURRENT_TIMESTAMP not null,
    update_time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    deleted tinyint default 0 not null
    );

