-- --------------------------------------------------------
-- 主机:                           192.168.24.130
-- 服务器版本:                        5.7.25 - MySQL Community Server (GPL)
-- 服务器操作系统:                      Linux
-- HeidiSQL 版本:                  11.3.0.6295
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- 导出 filedb 的数据库结构
CREATE DATABASE IF NOT EXISTS `filedb` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `filedb`;

-- 导出  表 filedb.file_info 结构
CREATE TABLE IF NOT EXISTS `file_info` (
  `id` varchar(50) NOT NULL DEFAULT '',
  `fileName` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '文件名称',
  `bucketName` varchar(50) NOT NULL COMMENT '存储桶 fastdfs对应group',
  `objectName` varchar(50) NOT NULL COMMENT '文件路径 fastdfs对应path',
  `fileSize` bigint(20) DEFAULT '0' COMMENT '文件大小',
  `md5` varchar(50) DEFAULT NULL COMMENT '文件md5值',
  `crc32` bigint(20) NOT NULL DEFAULT '0' COMMENT '文件crc32值',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态 1 正常 -1删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文件信息';

-- 数据导出被取消选择。

-- 导出  表 filedb.file_part_info 结构
CREATE TABLE IF NOT EXISTS `file_part_info` (
  `id` varchar(50) NOT NULL DEFAULT '',
  `uploadId` varchar(50) DEFAULT NULL COMMENT '分片上传唯一标识 云存储使用',
  `fileName` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '文件名称',
  `fileSize` bigint(20) NOT NULL DEFAULT '0' COMMENT '文件大小',
  `partNumber` int(11) NOT NULL DEFAULT '0' COMMENT '分片索引',
  `partSize` bigint(20) NOT NULL DEFAULT '0' COMMENT '分片大小',
  `bucketName` varchar(50) NOT NULL COMMENT '存储桶 fastdfs对应group',
  `objectName` varchar(50) NOT NULL COMMENT '文件路径 fastdfs对应path',
  `create_time` date DEFAULT NULL,
  `update_time` date DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态 1 正常 -1删除 0 终止',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文件分片信息';

-- 数据导出被取消选择。

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
