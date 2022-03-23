drop table if exists `test`;
create table `test` (
    `id` bigint not null comment 'id',
    `name` varchar(50) comment '名称',
    primary key (`id`)
)ENGINE=INNODB DEFAULT CHARSET =utf8mb4 comment = '测试';
