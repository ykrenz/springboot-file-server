/*
Navicat MySQL Data Transfer

Source Server         : MySQL57
Source Server Version : 50728
Source Host           : localhost:3306
Source Database       : filedb

Target Server Type    : MYSQL
Target Server Version : 50728
File Encoding         : 65001

Date: 2020-06-10 21:13:05
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for fileinfo
-- ----------------------------
DROP TABLE IF EXISTS `fileinfo`;
CREATE TABLE `fileinfo` (
  `id` varchar(255) NOT NULL,
  `filename` varchar(255) DEFAULT NULL COMMENT '文件名称',
  `md5` varchar(255) NOT NULL DEFAULT '' COMMENT '文件md5值',
  `filesize` bigint(20) NOT NULL DEFAULT '0' COMMENT '文件大小b',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `extension` varchar(255) DEFAULT NULL COMMENT '文件后缀名',
  `path` varchar(255) DEFAULT NULL COMMENT '访问路径 oss存储objectname fast存储fullpath',
  `filetype` varchar(255) DEFAULT NULL COMMENT '文件类型',
  `folder_id` int(11) DEFAULT NULL COMMENT '文件夹id',
  `folder_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `md5` (`md5`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
