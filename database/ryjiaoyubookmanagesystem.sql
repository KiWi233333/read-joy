/*
 Navicat Premium Data Transfer

 Source Server         : 我的电脑
 Source Server Type    : MySQL
 Source Server Version : 80039 (8.0.39)
 Source Host           : localhost:3306
 Source Schema         : ryjiaoyubookmanagesystem

 Target Server Type    : MySQL
 Target Server Version : 80039 (8.0.39)
 File Encoding         : 65001

 Date: 16/12/2024 02:04:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '管理员用户名',
  `password` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '管理员密码',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '管理员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, 'admin', '123456');
INSERT INTO `admin` VALUES (3, 'admin123', '123456');

-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book`  (
  `bookId` int NOT NULL AUTO_INCREMENT COMMENT '书籍ID',
  `isbn` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '国际标准书号',
  `title` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '书名',
  `categoryId` int NOT NULL COMMENT '分类ID',
  `author` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '作者',
  `introduction` varchar(1024) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '书籍介绍',
  `price` double(10, 2) NOT NULL COMMENT '价格',
  `publisher` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '出版社',
  `coverImageUrl` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '封面图片URL',
  `publishionDate` datetime NOT NULL COMMENT '出版日期',
  PRIMARY KEY (`bookId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '书籍表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of book
-- ----------------------------
INSERT INTO `book` VALUES (1, '978-3-16-148410-0', '百年孤独', 1, '加西亚·马尔克斯', '《百年孤独》是魔幻现实主义文学的代表作，描写了布恩迪亚家族七代人的传奇故事，以及加勒比海沿岸小镇马孔多的百年兴衰，反映了拉丁美洲一个世纪以来风云变幻的历史。作品融入神话传说、民间故事、宗教典故等神秘因素，巧妙地糅合了现实与虚幻，展现出一个瑰丽的想象世界，成为20世纪最重要的经典文学巨著之一。1982年加西亚•马尔克斯获得诺贝尔文学奖，奠定世界级文学大师的地位，很大程度上乃是凭借《百年孤独》的巨大影响。', 59.80, '南海出版公司', 'files/public/cover1.jpg', '1967-01-01 00:00:00');
INSERT INTO `book` VALUES (2, '978-7-02-009393-9', '红楼梦', 1, '曹雪芹', '中国古典小说四大名著之一', 68.00, '人民文学出版社', 'files/public/cover2.jpg', '1791-01-01 00:00:00');
INSERT INTO `book` VALUES (3, '978-4-5-258128-7', '挪威的森林', 1, '村上春树', '村上春树的畅销小说', 39.50, '上海译文出版社', 'files/public/cover3.jpg', '1987-01-01 00:00:00');
INSERT INTO `book` VALUES (4, '978-0-14-310459-0', '简爱', 1, '夏洛蒂·勃朗特', '英国文学经典作品', 45.00, '译林出版社', 'files/public/cover4.jpg', '1847-10-16 00:00:00');
INSERT INTO `book` VALUES (5, '978-7-5063-8628-8', '追风筝的人', 2, '卡勒德·胡赛尼', '关于友谊和救赎的故事', 39.90, '上海人民出版社', 'files/public/cover5.jpg', '2003-06-01 00:00:00');
INSERT INTO `book` VALUES (6, '978-3-16-148410-1', '活着', 2, '余华', '揭示人性的小说', 29.80, '作家出版社', 'files/public/cover6.jpg', '1993-01-01 00:00:00');
INSERT INTO `book` VALUES (7, '978-7-5327-8122-6', '解忧杂货店', 2, '东野圭吾', '日本畅销书', 35.00, '南海出版公司', 'files/public/cover7.jpg', '2012-01-01 00:00:00');
INSERT INTO `book` VALUES (8, '978-0-06-093546-1', '动物农场', 2, '乔治·奥威尔', '政治讽刺小说', 25.50, '上海译文出版社', 'files/public/cover8.jpg', '1945-08-17 00:00:00');
INSERT INTO `book` VALUES (9, '978-7-208-07978-8', '万历十五年', 3, '黄仁宇', '明史研究的经典之作', 48.00, '生活·读书·新知三联书店', 'files/public/cover9.jpg', '1982-05-01 00:00:00');
INSERT INTO `book` VALUES (10, '978-7-101-09500-1', '史记', 3, '司马迁', '中国古代史学巨著', 128.00, '中华书局', 'files/public/cover10.jpg', '2019-01-01 00:00:00');
INSERT INTO `book` VALUES (11, '978-7-5447-7148-0', '世界历史', 3, '威尔·杜兰特', '世界历史的全景式描述', 98.00, '上海译文出版社', 'files/public/cover11.jpg', '2024-01-01 00:00:00');
INSERT INTO `book` VALUES (12, '978-0-19-936041-5', '枪炮、病菌与钢铁', 3, '贾雷德·戴蒙德', '人类社会的命运', 58.00, '上海译文出版社', 'files/public/cover12.jpg', '2009-01-16 08:00:00');
INSERT INTO `book` VALUES (13, '978-0-7617-5504-3', '云南通志', 3, '赫连梓睿', '须马们X', 20.00, '山西省鹏煊保险股份有限公司', 'files/public/1734249544145.png', '2025-07-26 00:00:00');

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `categoryId` int NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `categoryName` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '分类名称',
  PRIMARY KEY (`categoryId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (1, '科技');
INSERT INTO `category` VALUES (2, '教育');
INSERT INTO `category` VALUES (3, '小说');
INSERT INTO `category` VALUES (4, '文学');
INSERT INTO `category` VALUES (5, '历史');
INSERT INTO `category` VALUES (6, '艺术');
INSERT INTO `category` VALUES (7, '经济管理');
INSERT INTO `category` VALUES (8, '社会科学');
INSERT INTO `category` VALUES (9, '自然科学');
INSERT INTO `category` VALUES (10, '计算机');
INSERT INTO `category` VALUES (11, '心理学');
INSERT INTO `category` VALUES (12, '哲学');
INSERT INTO `category` VALUES (13, '宗教');
INSERT INTO `category` VALUES (14, '法律');
INSERT INTO `category` VALUES (15, '医学');
INSERT INTO `category` VALUES (16, '工程');
INSERT INTO `category` VALUES (17, '旅游');
INSERT INTO `category` VALUES (18, '文化');
INSERT INTO `category` VALUES (19, '传记');
INSERT INTO `category` VALUES (20, '儿童文学');
INSERT INTO `category` VALUES (21, '外语学习');
INSERT INTO `category` VALUES (22, '健康');
INSERT INTO `category` VALUES (23, '军事');
INSERT INTO `category` VALUES (24, '科普');
INSERT INTO `category` VALUES (25, '体育');
INSERT INTO `category` VALUES (26, '生活');
INSERT INTO `category` VALUES (27, '娱乐');

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `bookId` int NOT NULL COMMENT '书籍ID',
  `commentator` int NOT NULL COMMENT '评论者ID',
  `commentBody` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '评论内容',
  `commentStatus` tinyint(1) NOT NULL COMMENT '评论状态：1待审核 2已审核 3已驳回',
  `isDeleted` tinyint(1) NOT NULL COMMENT '是否删除',
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES (1, 1, 2, '感觉这个书不错的样子！', 2, 0, '2024-12-14 20:14:58');
INSERT INTO `comment` VALUES (2, 1, 2, '感觉还可以吧。', 2, 0, '2024-12-14 19:05:10');
INSERT INTO `comment` VALUES (3, 1, 2, '感觉还可以吧。', 2, 0, '2024-12-14 20:20:31');
INSERT INTO `comment` VALUES (4, 1, 3, '挪威的森林名字感觉不错！', 2, 0, '2024-12-15 19:16:57');
INSERT INTO `comment` VALUES (5, 1, 3, '挪威的森林', 2, 0, '2024-12-15 19:22:09');
INSERT INTO `comment` VALUES (6, 1, 2, '你好', 2, 0, '2024-12-15 19:23:40');
INSERT INTO `comment` VALUES (7, 1, 2, '**', 1, 0, '2024-12-15 19:23:55');
INSERT INTO `comment` VALUES (8, 1, 10, '这个史记也不错', 2, 0, '2024-12-15 21:08:33');
INSERT INTO `comment` VALUES (9, 4, 10, '你好', 1, 0, '2024-12-15 22:07:10');
INSERT INTO `comment` VALUES (10, 1, 2, '这个是百年孤独', 1, 0, '2024-12-15 23:15:10');

-- ----------------------------
-- Table structure for resource
-- ----------------------------
DROP TABLE IF EXISTS `resource`;
CREATE TABLE `resource`  (
  `resourceId` int NOT NULL AUTO_INCREMENT COMMENT '资源ID',
  `type` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '资源类型，视频，教学大纲，课件等',
  `bookId` int NOT NULL COMMENT '关联的图书ID',
  `title` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '资源标题',
  `size` int NOT NULL COMMENT '资源大小',
  `url` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '资源保存地址',
  `submitter` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '资源提交者',
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `isDeleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  `downloadCount` int NOT NULL DEFAULT 0 COMMENT '下载数',
  `likeCount` int NOT NULL DEFAULT 0 COMMENT '点赞数',
  PRIMARY KEY (`resourceId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '资源表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of resource
-- ----------------------------
INSERT INTO `resource` VALUES (4, 'application/pdf', 1, '《百年孤独》高长荣[译]（正版）.pdf', 2615527, 'files/auth/1734252417307.pdf', 'admin', '2024-12-15 12:50:15', 0, 9, 2);
INSERT INTO `resource` VALUES (5, 'image/png', 1, '杂谈2.png', 33922, 'files/auth/1734284945239.png', 'admin', '2024-12-16 01:49:05', 0, 2, 1);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `loginName` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '登录名',
  `loginPassword` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '登录密码',
  `trueName` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `telephone` varchar(11) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '电话号码',
  `imgUrl` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户头像URL',
  `isChecked` tinyint(1) NOT NULL COMMENT '是否审核通过',
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `userType` tinyint(1) NOT NULL COMMENT '用户类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '头委听段', '123456', '领流', '18624581213', NULL, 1, '2024-12-08 22:06:40', 1);
INSERT INTO `user` VALUES (2, 'kiwi2333', '1234567', '张武', '13415000000', 'files/public/2_1734272754454.jpg', 1, '2024-12-09 23:23:25', 1);
INSERT INTO `user` VALUES (3, 'Huya2333', '123456', '张', '18624581213', NULL, 1, '2024-12-08 22:10:43', 1);
INSERT INTO `user` VALUES (4, 'Liwu12', '123456', '张', '18624581213', NULL, 1, '2024-12-08 22:10:50', 1);
INSERT INTO `user` VALUES (7, 'liuxiwu2', '123456', '张', '18624581213', NULL, 1, '2024-12-13 03:27:55', 1);
INSERT INTO `user` VALUES (8, 'liuxiw2', '123456', '张', '18624581213', NULL, 1, '2024-12-13 03:28:17', 1);
INSERT INTO `user` VALUES (9, 'wudi233', '123456', NULL, NULL, NULL, 1, '2024-12-13 03:28:51', 1);
INSERT INTO `user` VALUES (10, 'ikun233', '123456', NULL, NULL, 'files/public/10_1734273896322.png', 1, '2024-12-15 19:26:27', 1);

SET FOREIGN_KEY_CHECKS = 1;
