drop table dt_watched_user;
CREATE TABLE `dt_watched_user` (
  `user_id` varchar(128) DEFAULT NULL COMMENT '用户id',
  `screen_name` varchar(512) DEFAULT NULL COMMENT '用户名称',
  `token` varchar(512) DEFAULT NULL COMMENT '用户授权码',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  `status` varchar(128) DEFAULT NULL COMMENT '状态',
  `expire_date` datetime DEFAULT NULL COMMENT '过期时间',
  `client_id` varchar(512) DEFAULT NULL COMMENT '客户端ID',
  `client_secret` varchar(512) DEFAULT NULL COMMENT '客户端密钥',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;
#insert into dt_watched_user (user_id,token,screen_name,status) values ('1925238912','2.00WaGSGCnpP1DBdf51a94343llFD7C','陈俊文V','FOREVER_VALID');
#insert into dt_watched_user (user_id,token,screen_name,status) values ('3221292113','2.006ONAWD0TXPFWdf8b2ae70b00kK9K','霸气软件推荐','FOREVER_VALID');
#update dt_watched_user set token ='2.00WaGSGCnpP1DBdf51a94343llFD7C',status='FOREVER_VALID' where user_id='1925238912'
drop table dt_tweet;
CREATE TABLE `dt_tweet` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'pk',
  `tid` bigint(20) unsigned NOT NULL COMMENT '微博id',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  `post_date` datetime DEFAULT NULL COMMENT '微博发送时间',
  `user_id` varchar(128) DEFAULT NULL COMMENT '用户id',
  `member_user_id` varchar(128) DEFAULT NULL COMMENT '会员用户id',
  `content` blob COMMENT '微博内容',
  `reposts_count` int unsigned NOT NULL COMMENT '转发数',
  `comments_count` int unsigned NOT NULL COMMENT '评论数',
  
  PRIMARY KEY (`id`),
  KEY `idx_dt_tweet_member_user_id_tid` (`member_user_id`,`tid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

drop table dt_deleted_tweet;
CREATE TABLE `dt_deleted_tweet` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'pk',
  `tid` bigint(20) unsigned NOT NULL COMMENT '微博id',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  `post_date` datetime DEFAULT NULL COMMENT '微博发送时间',
  `delete_date` datetime DEFAULT NULL COMMENT '微博删除时间',
  `delete_sort` varchar(128) DEFAULT NULL COMMENT '微博删除时间',
  `user_id` varchar(128) DEFAULT NULL COMMENT '用户id',
  `member_user_id` varchar(128) DEFAULT NULL COMMENT '会员用户id',
  `content` blob COMMENT '微博内容',
  `reposts_count` int unsigned NOT NULL COMMENT '转发数',
  `comments_count` int unsigned NOT NULL COMMENT '评论数',
  PRIMARY KEY (`id`),
  KEY `idx_dt_deleted_tweet_member_user_id_tid` (`member_user_id`,`delete_sort`,`tid`),
  KEY `idx_dt_deleted_tweet_tid` (`tid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;
