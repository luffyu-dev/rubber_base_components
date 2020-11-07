CREATE DATABASE user_db_00;
USE user_db_00;
CREATE TABLE user_info_00(
`id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
`user_id` int(11) unsigned NOT NULL COMMENT '用户id',
`user_name` VARCHAR(50) NOT NULL COMMENT '用户昵称',
	PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='用户信息表';

CREATE TABLE user_info_01(
`id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
`user_id` int(11) unsigned NOT NULL COMMENT '用户id',
`user_name` VARCHAR(50) NOT NULL COMMENT '用户昵称',
	PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='用户信息表';


CREATE TABLE user_order_00(
`id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
`order_id` int(11) unsigned NOT NULL COMMENT '订单id',
`user_id` int(11) unsigned NOT NULL COMMENT '用户id',
`order_name` VARCHAR(50) NOT NULL COMMENT '用户昵称',
	PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='订单表';

CREATE TABLE user_order_01(
`id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
`order_id` int(11) unsigned NOT NULL COMMENT '订单id',
`user_id` int(11) unsigned NOT NULL COMMENT '用户id',
`order_name` VARCHAR(50) NOT NULL COMMENT '用户昵称',
	PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='订单表';


CREATE DATABASE user_db_01;
USE user_db_01;
CREATE TABLE user_info_00(
`id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
`user_id` int(11) unsigned NOT NULL COMMENT '用户id',
`user_name` VARCHAR(50) NOT NULL COMMENT '用户昵称',
	PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='用户信息表';

CREATE TABLE user_info_01(
`id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
`user_id` int(11) unsigned NOT NULL COMMENT '用户id',
`user_name` VARCHAR(50) NOT NULL COMMENT '用户昵称',
	PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='用户信息表';


CREATE TABLE user_order_00(
`id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
`order_id` int(11) unsigned NOT NULL COMMENT '订单id',
`user_id` int(11) unsigned NOT NULL COMMENT '用户id',
`goods_id` int(11) unsigned NOT NULL COMMENT '商品id',
`order_name` VARCHAR(50) NOT NULL COMMENT '用户昵称',
	PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='订单表';

CREATE TABLE user_order_01(
`id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
`order_id` int(11) unsigned NOT NULL COMMENT '订单id',
`user_id` int(11) unsigned NOT NULL COMMENT '用户id',
`goods_id` int(11) unsigned NOT NULL COMMENT '商品id',
`order_name` VARCHAR(50) NOT NULL COMMENT '用户昵称',
	PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='订单表';




CREATE DATABASE data_config;
USE data_config;
CREATE TABLE goods(
`id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
`goods_id` int(11) unsigned NOT NULL COMMENT '物品id',
`goods_name` int(11) unsigned NOT NULL COMMENT '物品名称',
`goods_price` int(11) unsigned NOT NULL COMMENT '物品价格',
 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='物品配置表';



CREATE DATABASE data_config_slave1;
USE data_config_slave1;
CREATE TABLE goods(
`id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
`goods_id` int(11) unsigned NOT NULL COMMENT '物品id',
`goods_name` int(11) unsigned NOT NULL COMMENT '物品名称',
`goods_price` int(11) unsigned NOT NULL COMMENT '物品价格',
 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='物品配置表';
