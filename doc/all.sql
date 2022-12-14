drop table if exists `test`;
create table `test`
(
    `id`   bigint not null comment 'id',
    `name` varchar(50) comment '名称',
    primary key (`id`)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8mb4 comment = '测试';

#_电子书表
drop table if exists `eboqk`;
create table `ebook`
(
    `id`           bigint not null comment 'id',
    `name`         varchar(50) comment ' 名称',
    `category1_id` bigint comment '分类1',
    `category2_id` bigint comment '分类2',
    `description`  varchar(200) comment ' 描述',
    `cover`        varchar(200) comment '封面',
    `doc_count`    int comment '文档数',
    `view_count`   int comment ' 阅读数',
    `vote_count`   int comment ' 点赞数',
    primary key (id)
) engine = innodb
  default charset = utf8mb4 comment ='电子书';
insert into ebook (id, name, description)
values (1, 'Spring Boot 入门教程', '零基础入门Java 开发,企业级应用开发最佳首选框架');
insert into ebook (id, name, description)
values (2, 'Vue 入门教程', '零基础入门Vue开发,企业级应用开发最佳首选框架');
insert into ebook (id, name, description)
values (3, 'Python 入门教程', '零基础入门Python 开发,企业级应用开发最佳首选框架');
insert into ebook (id, name, description)
values (4, 'Mysq1 入门教程', '零基础入门Mysql开发,企业级应用开发最佳首选框架');
insert into ebook (id, name, description)
values (5, 'Oracle 入门教程', '零基础入门Oracle 开发,企业级应用开发最佳首选框架');

# 分类
CREATE TABLE `category`
(
    `id`     bigint      NOT NULL COMMENT 'id',
    `parent` bigint      NOT NULL DEFAULT '0' COMMENT '父id',
    `name`   varchar(50) NOT NULL COMMENT '名称',
    `sort`   int                  DEFAULT NULL COMMENT '顺序',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='分类';

# 文档
drop table if exists `doc`;
create table `doc`
(
    `id`       bigint      not null comment 'id',
    `ebook_id` bigint      not NULL default 0 comment '电子书id',
    `parent`   bigint      not NULL default 0 comment ' 父id',
    `name`     varchar(50) not NULL comment '名称',
    sort       int comment ' 顺序',
    view_count int                  default 0 comment '阅读数',
    vote_count int                  default 0 comment ' 点赞数',
    primary key (id)
) engine = innodb
  default charset = utf8mb4 comment ='文档';

insert into doc (id, ebook_id, parent, `name`, sort, view_count, vote_count)
values (1, 1, 0, '文档1', 1, 0, 0);
insert into doc (id, ebook_id, parent, `name`, sort, view_count, vote_count)
values (2, 1, 1, '文档1.1', 1, 0, 0);
insert into doc (id, ebook_id, parent, `name`, sort, view_count, vote_count)
values (3, 1, 0, '文档2', 2, 0, 0);
insert into doc (id, ebook_id, parent, `name`, sort, view_count, vote_count)
values (4, 1, 3, '文档2.1', 1, 0, 0);
insert into doc (id, ebook_id, parent, `name`, sort, view_count, vote_count)
values (5, 1, 3, '文档2.2', 2, 0, 0);
insert into doc (id, ebook_id, parent, `name`, sort, view_count, vote_count)
values (6, 1, 5, '文档2.2.1', 1, 0, 0);

# 文档内容
drop table if exists `content`;
create table `content`
(
    `id`      bigint     not null comment '文档id',
    `content` mediumtext not null comment '内容',
    primary key (`id`)
) ENGINE = innodb
  default charset = utf8mb4 comment ='文档内容';

# 用户表
drop table if exists user;
create table
    user
(
    `id`       bigint      not null comment 'ID',
    `login_name` varchar(50) not null Comment '登陆名',
    `name`     varchar(50) Comment '昵称',
    `password` char(32)    not null comment '密码',
    primary key (id),
    unique key login_name_unique (login_name)
) engine = innodb
  default charset = utf8mb4 comment =' 用户';

insert into user(id,login_name,name,password) values (1, 'test', ' 测试','test' );

# 知识库快照表
drop table if exists `ebook_snapshot`;
create table `ebook_snapshot` (
`id` bigint auto_increment not null comment 'id',
`ebook_id` bigint not null default 0 comment '知识库id' ,
`date` date not null comment '快照日期' ,
`view_count` int not null default 0 comment '阅读数',
`vote_count` int not null default 0 comment '点赞数' ,
`view_increase` int not null default 0 comment '阅读增长',
`vote_increase` int not null default 0 comment '点赞增长' ,
primary key (`id`),
unique key `ebook_id_date_unique` (`ebook_id`,`date`)
) engine=innodb default charset=utf8mb4 comment= '知识库快照表';

# 方案一(ID不连续) :
#   删除今天的数据
#   为所有的知识库生成- -条今天的记录
#   更新总阅读数、总点赞数
#   更新今日阅读数、 今日点赞数


# 方案二(ID连续):
#   为所有的知识库生成一条今天的记录，如果还没有
#   更新总阅读数、总点赞数
#   更新今日阅读数、今日点赞数
insert into ebook_snapshot(ebook_id, `date`, view_count, vote_count, view_increase, vote_increase)
select t1.id, curdate(), 0, 0, 0, 0
from ebook t1
where not exists(select 1
                 from ebook_snapshot t2
                 where t1.id = t2.ebook_id
                   and t2.`date` = curdate());

update ebook_snapshot t1, ebook t2
set t1.view_count = t2.view_count,t1.vote_count = t2.view_count
where t1.`date` = curdate() and t1.ebook_id = t2.id;

# 获取昨天的数据
select t1.ebook_id,view_count,vote_count from ebook_snapshot t1
where t1.`date` = date_sub(curdate(),interval 1 day );

update ebook_snapshot t1 left join
    (select ebook_id,view_count,vote_count from ebook_snapshot
     where `date` = date_sub(curdate(),interval 1 day )) t2
    on t1.ebook_id = t2.ebook_id
set t1.view_increase = (t1.view_count - ifnull(t2.view_count,0)),
    t1.vote_increase = (t1.vote_count - ifnull(t2.vote_count,0))
where t1.`date` = curdate();
# ---------------------------------------
