/*
Navicat MySQL Data Transfer

Source Server         : mysql8
Source Server Version : 80022
Source Host           : localhost:3308
Source Database       : ryjiaoyubookmanagesystem

Target Server Type    : MYSQL
Target Server Version : 80022
File Encoding         : 65001

Date: 2024-12-04 18:39:13
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book` (
  `bookId` int NOT NULL,
  `isbn` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `title` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `categoryId` int NOT NULL,
  `author` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `introduction` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `price` double(10,2) NOT NULL,
  `publisher` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '出版社',
  `coverImageUrl` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `publishionDate` datetime NOT NULL,
  PRIMARY KEY (`bookId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `categoryId` int NOT NULL AUTO_INCREMENT,
  `categoryName` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`categoryId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `bookId` int NOT NULL,
  `commentator` int NOT NULL,
  `commentBody` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '评论的内容',
  `commentStatus` tinyint(1) NOT NULL COMMENT '评论状态，控制是否暂停评论',
  `isDeleted` tinyint(1) NOT NULL,
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for resource
-- ----------------------------
DROP TABLE IF EXISTS `resource`;
CREATE TABLE `resource` (
  `resourceId` int NOT NULL,
  `type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '类型名，视频，教学大纲，课件等',
  `bookId` int NOT NULL COMMENT ' 关联的图书的id',
  `title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `size` int NOT NULL COMMENT '  资源的标题',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT ' 资源保存的地址',
  `submitter` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '资源提交者',
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `isDeleted` tinyint(1) NOT NULL,
  `downloadCount` int NOT NULL COMMENT ' 下载数',
  `likeCount` int NOT NULL COMMENT '点赞数',
  PRIMARY KEY (`resourceId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `loginName` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `loginPassword` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `trueName` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `telephone` varchar(11) NOT NULL,
  `imgUrl` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `isChecked` tinyint(1) NOT NULL,
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `userType` tinyint(1) NOT NULL COMMENT ' 用户类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
