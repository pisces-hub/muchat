/*
 Navicat Premium Data Transfer

 Source Server         : local-mysql
 Source Server Type    : MySQL
 Source Server Version : 80029
 Source Host           : localhost:3306
 Source Schema         : muchat

 Target Server Type    : MySQL
 Target Server Version : 80029
 File Encoding         : 65001

 Date: 24/07/2023 21:40:04
*/
create database `muchat` default character set utf8mb4 collate utf8mb4_general_ci;
USE muchat;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for chat_session
-- ----------------------------
DROP TABLE IF EXISTS `chat_session`;
CREATE TABLE `chat_session` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `own_id` bigint DEFAULT NULL COMMENT '归属用户id',
  `target_id` bigint DEFAULT NULL COMMENT '对方id',
  `chat_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '聊天类型',
  `update_time` bigint DEFAULT NULL COMMENT '更新时间',
  `top_flag` int DEFAULT NULL COMMENT '是否置顶',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of chat_session
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for im_friend
-- ----------------------------
DROP TABLE IF EXISTS `im_friend`;
CREATE TABLE `im_friend` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `friend_id` bigint NOT NULL COMMENT '好友id',
  `friend_nick_name` varchar(255) NOT NULL COMMENT '好友昵称',
  `friend_head_image` varchar(255) DEFAULT '' COMMENT '好友头像',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_friend_id` (`friend_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='好友';

-- ----------------------------
-- Records of im_friend
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for im_group
-- ----------------------------
DROP TABLE IF EXISTS `im_group`;
CREATE TABLE `im_group` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(255) NOT NULL COMMENT '群名字',
  `owner_id` bigint NOT NULL COMMENT '群主id',
  `head_image` varchar(255) DEFAULT '' COMMENT '群头像',
  `head_image_thumb` varchar(255) DEFAULT '' COMMENT '群头像缩略图',
  `notice` varchar(1024) DEFAULT '' COMMENT '群公告',
  `remark` varchar(255) DEFAULT '' COMMENT '群备注',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '是否已删除',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `group_type` int DEFAULT '0' COMMENT '群类型:0正常，1匿名群',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3 COMMENT='群';

-- ----------------------------
-- Records of im_group
-- ----------------------------
BEGIN;
INSERT INTO `im_group` (`id`, `name`, `owner_id`, `head_image`, `head_image_thumb`, `notice`, `remark`, `deleted`, `created_time`, `group_type`) VALUES (5, '万人大群聊', 1, 'http://43.138.164.74:9000/muchat/image/20230702/1688284083704.png', 'http://43.138.164.74:9000/muchat/image/20230702/1688284083704.png', '开源不易，觉得喜欢的话，可以帮忙给个小星星<br/><br/><a herf=\"https://gitee.com/pisces-hub/muchat\">https://gitee.com/pisces-hub/muchat</a>', 'Muchat是用JAVA语言开发的轻量、高性能、单机支持几十万至百万在线用户IM，主要目标降低即时通讯门槛，快速打造低成本接入在线IM系统，通过极简洁的消息格式就可以实现多端不同协议间的消息发送如内置(Http、Websocket、Tcp自定义IM协议)\n', 0, '2023-06-17 11:43:40', 1);
COMMIT;

-- ----------------------------
-- Table structure for im_group_member
-- ----------------------------
DROP TABLE IF EXISTS `im_group_member`;
CREATE TABLE `im_group_member` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `group_id` bigint NOT NULL COMMENT '群id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `alias_name` varchar(255) DEFAULT '' COMMENT '组内显示名称',
  `head_image` varchar(255) DEFAULT '' COMMENT '用户头像',
  `remark` varchar(255) DEFAULT '' COMMENT '备注',
  `quit` tinyint(1) DEFAULT '0' COMMENT '是否已退出',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_group_id` (`group_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3 COMMENT='群成员';

-- ----------------------------
-- Records of im_group_member
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for im_group_message
-- ----------------------------
DROP TABLE IF EXISTS `im_group_message`;
CREATE TABLE `im_group_message` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `group_id` bigint NOT NULL COMMENT '群id',
  `send_id` bigint NOT NULL COMMENT '发送用户id',
  `content` text COMMENT '发送内容',
  `type` tinyint(1) NOT NULL COMMENT '消息类型 0:文字 1:图片 2:文件 3:语音 10:系统提示',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态 0:正常  2:撤回',
  `send_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
  PRIMARY KEY (`id`),
  KEY `idx_group_id` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='群消息';

-- ----------------------------
-- Records of im_group_message
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for im_private_message
-- ----------------------------
DROP TABLE IF EXISTS `im_private_message`;
CREATE TABLE `im_private_message` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `send_id` bigint NOT NULL COMMENT '发送用户id',
  `recv_id` bigint NOT NULL COMMENT '接收用户id',
  `content` text COMMENT '发送内容',
  `type` tinyint(1) NOT NULL COMMENT '消息类型 0:文字 1:图片 2:文件 3:语音 10:系统提示',
  `status` tinyint(1) NOT NULL COMMENT '状态 0:未读 1:已读 2:撤回',
  `send_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
  PRIMARY KEY (`id`),
  KEY `idx_send_recv_id` (`send_id`,`recv_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='私聊消息';

-- ----------------------------
-- Records of im_private_message
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for im_user
-- ----------------------------
DROP TABLE IF EXISTS `im_user`;
CREATE TABLE `im_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_name` varchar(255) NOT NULL COMMENT '用户名',
  `nick_name` varchar(255) NOT NULL COMMENT '用户昵称',
  `head_image` varchar(255) DEFAULT '' COMMENT '用户头像',
  `head_image_thumb` varchar(255) DEFAULT '' COMMENT '用户头像缩略图',
  `password` varchar(255) NOT NULL COMMENT '密码(明文)',
  `sex` tinyint(1) DEFAULT '0' COMMENT '性别 0:男 1::女',
  `signature` varchar(1024) DEFAULT '' COMMENT '个性签名',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `register_from` int DEFAULT '0' COMMENT '注册来源：0本系统，1gitee,2github',
  `oauth_src` text COMMENT 'oauth认证结果',
  `account_type` int DEFAULT '0' COMMENT '账号类型:0正常，1匿名',
  `anonymou_id` varchar(255) DEFAULT NULL COMMENT '匿名id',
  `last_login_ip` varchar(30) DEFAULT NULL COMMENT '最新一次登录的ip信息',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_name` (`user_name`),
  KEY `idx_nick_name` (`nick_name`)
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8mb3 COMMENT='用户';

-- ----------------------------
-- Records of im_user
-- ----------------------------
BEGIN;
INSERT INTO `im_user` (`id`, `user_name`, `nick_name`, `head_image`, `head_image_thumb`, `password`, `sex`, `signature`, `last_login_time`, `created_time`, `register_from`, `oauth_src`, `account_type`, `anonymou_id`, `last_login_ip`) VALUES (1, 'admin', 'admin', '', '', '$2a$10$MEHf4SDJi6.Wfxj0KDQ4EeUc47YIymx7vMzWKIyz6YR7hKiE1Rnl2', 0, '', NULL, '2023-06-12 13:12:43', 0, NULL, 0, NULL, '127.0.0.1');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
