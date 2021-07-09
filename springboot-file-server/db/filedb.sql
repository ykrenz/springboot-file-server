/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80023
 Source Host           : localhost:3306
 Source Schema         : filedb

 Target Server Type    : MySQL
 Target Server Version : 80023
 File Encoding         : 65001

 Date: 09/07/2021 23:28:29
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_file
-- ----------------------------
DROP TABLE IF EXISTS `t_file`;
CREATE TABLE `t_file`  (
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `md5` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件md5值 客户端计算',
  `eTag` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件服务端响应的eTag',
  `filesize` bigint(0) NOT NULL DEFAULT 0 COMMENT '文件大小b',
  `objectName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '存储桶里的对象名称',
  `create_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `status` tinyint(0) NULL DEFAULT 1 COMMENT '状态 1 正常 -1删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `md5`(`md5`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '文件信息主表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_part
-- ----------------------------
DROP TABLE IF EXISTS `t_part`;
CREATE TABLE `t_part`  (
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `uploadId` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分片上传返回的uploadId',
  `objectName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分片文件所属的objectName',
  `partNumber` int(0) NOT NULL DEFAULT 0 COMMENT '分片索引',
  `partSize` bigint(0) NOT NULL DEFAULT 0 COMMENT '分片大小',
  `eTag` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分片上传完成后响应的eTag值',
  `create_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '状态 0 就绪 1正常 -1删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `md5`(`eTag`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '文件分片信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_upload
-- ----------------------------
DROP TABLE IF EXISTS `t_upload`;
CREATE TABLE `t_upload`  (
  `id` bigint(0) NOT NULL,
  `uploadId` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `objectName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `expireAt` datetime(0) NULL DEFAULT NULL COMMENT '过期时间(过期后不可使用 null为不过期需要手动清理)',
  `fileSize` double NULL DEFAULT 0 COMMENT '文件大小',
  `partSize` double NULL DEFAULT 0 COMMENT '分片大小',
  `storage` int(0) NULL DEFAULT 0 COMMENT '文件存储方式 1 本地 2 fastdfs 3 minio 4 alioss ...',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `status` tinyint(0) NULL DEFAULT 0 COMMENT '0初始化 1完成 -1取消',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_file
-- ----------------------------
DROP TABLE IF EXISTS `tb_file`;
CREATE TABLE `tb_file`  (
  `md5` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件md5值',
  `filesize` bigint(0) NOT NULL DEFAULT 0 COMMENT '文件大小b',
  `path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件路径 oss存储objectname fast存储fullpath',
  `create_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `creater` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `updater` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `version` int(0) NULL DEFAULT 0 COMMENT '乐观锁 版本控制',
  `status` tinyint(0) NULL DEFAULT 1 COMMENT '状态 1 正常 -1删除',
  PRIMARY KEY (`md5`) USING BTREE,
  INDEX `md5`(`md5`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '文件信息主表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_file_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_file_info`;
CREATE TABLE `tb_file_info`  (
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键',
  `md5` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '外键 关联tb_file表中主键',
  `filename` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件或文件夹名称',
  `folder` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属文件夹id',
  `create_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `creater` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `updater` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `version` int(0) NULL DEFAULT 0 COMMENT '乐观锁 版本控制',
  `status` tinyint(0) NULL DEFAULT 1 COMMENT '状态 1正常-1删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `folder`(`folder`) USING BTREE,
  INDEX `md5`(`md5`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '文件信息从表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
