/*
Navicat MySQL Data Transfer

Source Server         : 1
Source Server Version : 50636
Source Host           : 192.168.1.80:3306
Source Database       : system_comprehensive_management

Target Server Type    : MYSQL
Target Server Version : 50636
File Encoding         : 65001

Date: 2017-11-29 14:30:24
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for system_user
-- ----------------------------
DROP TABLE IF EXISTS `system_user`;
CREATE TABLE `system_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `gid` varchar(32) DEFAULT NULL COMMENT '唯一id',
  `name` varchar(128) DEFAULT NULL COMMENT '登录名',
  `password` varchar(128) NOT NULL COMMENT '登录密码',
  `business_group_code` varchar(32) DEFAULT NULL COMMENT '所属应用id',
  `type` tinyint(4) DEFAULT NULL COMMENT '用户类型',
  `phone` varchar(32) DEFAULT NULL COMMENT '手机号',
  `email` varchar(32) DEFAULT NULL COMMENT '邮箱',
  `realname` varchar(128) DEFAULT NULL COMMENT '真实姓名',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态(数据字典)',
  `last_login_time` datetime DEFAULT NULL COMMENT '最近一次登录时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uidx_gid` (`gid`),
  UNIQUE KEY `uidx_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8 COMMENT='系统用户表';
