/*
Navicat MySQL Data Transfer

Source Server         : MySQL57
Source Server Version : 50728
Source Host           : localhost:3306
Source Database       : filedb

Target Server Type    : MYSQL
Target Server Version : 50728
File Encoding         : 65001

Date: 2021-04-16 19:54:37
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_file
-- ----------------------------
DROP TABLE IF EXISTS `tb_file`;
CREATE TABLE `tb_file` (
  `md5` varchar(255) NOT NULL COMMENT '文件md5值',
  `filesize` bigint(20) NOT NULL DEFAULT '0' COMMENT '文件大小b',
  `path` varchar(255) NOT NULL COMMENT '文件路径 oss存储objectname fast存储fullpath',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creater` varchar(255) DEFAULT NULL COMMENT '创建者',
  `updater` varchar(255) DEFAULT NULL COMMENT '更新者',
  `version` int(11) DEFAULT '0' COMMENT '乐观锁 版本控制',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态 1 正常 -1删除',
  PRIMARY KEY (`md5`),
  KEY `md5` (`md5`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文件信息主表';

-- ----------------------------
-- Table structure for tb_file_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_file_info`;
CREATE TABLE `tb_file_info` (
  `id` varchar(255) NOT NULL COMMENT '主键',
  `md5` varchar(255) NOT NULL COMMENT '外键 关联tb_file表中主键',
  `filename` varchar(255) DEFAULT NULL COMMENT '文件或文件夹名称',
  `folder` varchar(255) DEFAULT NULL COMMENT '所属文件夹id',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creater` varchar(255) DEFAULT NULL COMMENT '创建者',
  `updater` varchar(255) DEFAULT NULL COMMENT '更新者',
  `version` int(11) DEFAULT '0' COMMENT '乐观锁 版本控制',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态 1正常-1删除',
  PRIMARY KEY (`id`),
  KEY `folder` (`folder`) USING BTREE,
  KEY `md5` (`md5`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文件信息从表';
