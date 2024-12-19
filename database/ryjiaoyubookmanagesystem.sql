/*
 Navicat Premium Data Transfer

 Source Server         : ReadJoy
 Source Server Type    : MySQL
 Source Server Version : 80039 (8.0.39)
 Source Host           : localhost:3306
 Source Schema         : ryjiaoyubookmanagesystem

 Target Server Type    : MySQL
 Target Server Version : 80039 (8.0.39)
 File Encoding         : 65001

 Date: 20/12/2024 00:06:44
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
) ENGINE = InnoDB AUTO_INCREMENT = 44 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '书籍表' ROW_FORMAT = Dynamic;

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
INSERT INTO `book` VALUES (13, '978-0-7617-5504-3', '云南通志', 3, '鄂尔泰', '他如杨慎之《滇程记》、《滇载记》诸书，掇拾成编，不免挂一漏万。谢肇淛所辑《滇略》，号为善本，然所述止於明代。本朝康熙三十年始草创《通志》，稍具规模，犹多舛略。雍正七年，鄂尔泰总督云贵，奉诏纂辑，乃属姚州知州靖道谟因旧志增修。凡为门三十，门为一卷。乾隆元年书成，后任总督尹继善等具表进之。其间视旧志增并不一，如图之有说及府州县题名，皆补旧志之所无。《大事考》、《使命》、《师命》诸目，旧志所有而冗复失当者，则删去之。又课程原附盐法，闸坝、堰塘原附城池，今皆别自为门。纲领粲然，视原本颇有条理焉。', 20.00, '云南人民出版社出版', 'files/public/1734249544145.png', '2007-03-01 00:00:00');
INSERT INTO `book` VALUES (14, '978-3-16-148410-0', '未来简史', 1, '尤瓦尔·赫拉利', '探讨人类未来可能面临的科技和社会挑战。', 59.99, '中信出版社', 'files/public/1734527874544.jpg', '2023-01-15 00:00:00');
INSERT INTO `book` VALUES (15, '978-7-121-34567-8', '深度学习', 1, 'Ian Goodfellow', '深度学习领域的经典之作，详细介绍了深度学习的原理和应用。', 99.99, '电子工业出版社', 'files/public/1734527994643.jpg', '2023-06-20 00:00:00');
INSERT INTO `book` VALUES (16, '978-0-13-235612-0', '计算机程序设计艺术', 10, 'Donald E. Knuth', '计算机科学领域的经典著作，深入探讨了算法和程序设计。', 49.99, '机械工业出版社', 'files/public/1734528002135.jpg', '2023-09-10 00:00:00');
INSERT INTO `book` VALUES (17, '978-7-302-56789-0', '算法导论', 10, 'Thomas H. Cormen', '算法领域的经典教材，详细介绍了各种算法的设计和分析。', 49.99, '清华大学出版社', 'files/public/1734527806008.jpg', '2023-11-05 00:00:00');
INSERT INTO `book` VALUES (18, '978-7-100-12345-6', '教育心理学', 2, 'Jeanne Ellis Ormrod', '深入探讨了教育过程中的心理学原理和实践。', 69.99, '人民教育出版社', 'files/public/1734528014959.jpg', '2023-03-01 00:00:00');
INSERT INTO `book` VALUES (19, '978-7-5447-87654-3', '围城', 3, '钱钟书', '一部讽刺小说，讲述了主人公方鸿渐的人生经历。', 39.99, '人民文学出版社', 'files/public/1734528023001.jpg', '2023-05-20 00:00:00');
INSERT INTO `book` VALUES (20, '978-3-16-789012-3', '百年孤独', 3, '加西亚·马尔克斯', '魔幻现实主义文学的代表作，讲述了布恩迪亚家族七代人的故事。', 49.99, '南海出版公司', 'files/public/1734528031303.jpg', '2023-07-30 00:00:00');
INSERT INTO `book` VALUES (21, '978-7-121-34567-8', '心理学的100个基本（口袋里的心理学指南）', 2, '陈琳', '人生无须大道理，懂点心理学过好每一天。100张世界名画，轻松读图加深理解，视觉美感治愈心灵。', 22.10, '四川文艺出版社', '', '2023-09-01 00:00:00');
INSERT INTO `book` VALUES (22, '978-3-16-148410-0', '未来简史', 1, '尤瓦尔·赫拉利', '探讨人类未来可能面临的科技和社会挑战。', 59.99, '中信出版社', '', '2023-01-15 00:00:00');
INSERT INTO `book` VALUES (23, '978-7-302-56789-0', '算法导论', 10, 'Thomas H. Cormen', '算法领域的经典教材，详细介绍了各种算法的设计和分析。', 49.99, '清华大学出版社', 'files/public/1734601601129.webp', '2023-11-05 00:00:00');
INSERT INTO `book` VALUES (24, '978-7-100-12345-6', '教育心理学', 2, '罗伯特斯莱文', '系统性地介绍了教育心理学的基本理论和实践应用。', 69.99, '人民教育出版社', 'files/public/1734528042267.jpg', '2023-03-01 00:00:00');
INSERT INTO `book` VALUES (25, '978-0-13-235612-0', '计算机程序设计艺术', 10, 'Donald E. Knuth', '计算机科学领域的经典著作，深入探讨了算法和程序设计。', 49.99, '机械工业出版社', 'files/public/1734527834646.jpg', '2023-09-10 00:00:00');
INSERT INTO `book` VALUES (26, '978-7-5447-87654-3', '积极心理学研究（2024）', 2, '今心', '积极心理学领域中人格频率学与PERMA模型融合应用的探讨。', 49.99, '经济日报出版社', 'files/public/1734526558482.jpg', '2024-01-01 00:00:00');
INSERT INTO `book` VALUES (27, '978-7-100-67890-1', '心理学与生活', 2, '理查德·格里格、菲利普·津巴多', '斯坦福大学多年来使用的教材，也是许多大学里推广使用的经典教材。', 62.60, '人民邮电出版社', 'files/public/1734528051162.jpg', '2023-09-01 00:00:00');
INSERT INTO `book` VALUES (28, '978-7-300-12345-7', '社会心理学', 2, 'D.A.巴伦, D.伯恩', '社会心理学是一门研究人们如何看待他人，如何相互影响，以及如何与他人相互关联的科学。', 39.99, '华东师范大学出版社', 'files/public/1734528067967.jpg', '2023-05-20 00:00:00');
INSERT INTO `book` VALUES (29, '978-7-300-23456-8', '发展心理学', 2, '林崇德', '一部关于生命之旅，成长之旅的经典著作。', 59.99, '北京师范大学出版社', 'files/public/1734528076452.jpg', '2023-08-15 00:00:00');
INSERT INTO `book` VALUES (30, '978-7-300-34567-9', '改变心理学的40项研究', 2, '罗杰·霍克', '用通俗的语言介绍了心理学研究发展历程中对心理学发展影响深远的40项研究。', 39.99, '中国轻工业出版社', 'files/public/1734528084940.jpg', '2023-10-30 00:00:00');
INSERT INTO `book` VALUES (31, '978-3-16-789456-1', '黑暗之光：后真理时代的现实主义', 1, 'Massimiliano Gioni', '探索后真理时代现实主义的深度作品。', 88.00, 'Skira Editore', 'files/public/1734528126729.jpg', '2023-04-10 00:00:00');
INSERT INTO `book` VALUES (32, '978-7-305-23456-7', '身体、空间与后现代性', 2, '汪民安', '对身体、空间等论题所做的研究结集。', 58.00, '南京大学出版社', 'files/public/1734528547244.jpg', '2023-07-15 00:00:00');
INSERT INTO `book` VALUES (33, '978-1-23-456789-0', '策展人手册', 8, 'Adrian George', '掌握策展的乐趣与知识。', 69.99, '北京美术摄影出版社', 'files/public/1734528555614.jpg', '2023-09-20 00:00:00');
INSERT INTO `book` VALUES (34, '978-7-5447-56789-6', '微笑偷盗者', 3, 'Richard Barnett', '全景式的牙科学演化史。', 45.00, '北京日报出版社', 'files/public/1734527820413.jpg', '2023-11-05 00:00:00');
INSERT INTO `book` VALUES (35, '978-3-16-123456-7', '岛屿书', 9, 'Judith Schalansky', '探索不为世界所知的孤独小岛。', 39.99, '湖南文艺出版社', 'files/public/1734527747872.jpg', '2023-12-01 00:00:00');
INSERT INTO `book` VALUES (36, '978-7-300-12345-8', '肉桂色铺子', 3, 'Bruno Schulz', '15个相对独立又自成一体的小短篇，构成一个神话的世俗化过程。', 29.99, '北京大学出版社', 'files/public/1734526521004.jpg', '2024-01-25 00:00:00');
INSERT INTO `book` VALUES (37, '978-7-543-23456-9', '虞美人草', 3, '夏目漱石', '一本难读、奇怪又华丽无比，被称作“炫技”的小说。', 35.00, '人民文学出版社', 'files/public/1734526507639.jpg', '2024-03-30 00:00:00');
INSERT INTO `book` VALUES (38, '978-1-23-456789-1', '第一个人', 3, 'Albert Camus', '一部寻根小说，叙述作者怎样从蒙昧中蹒跚走来。', 48.00, '上海译文出版社', 'files/public/1734526481555.jpg', '2024-05-15 00:00:00');
INSERT INTO `book` VALUES (39, '978-7-108-56789-0', '丛林故事', 3, 'Rudyard Kipling', '英国19世纪末20世纪初的文学大家，儿童文学巨匠的代表作品。', 39.99, '商务印书馆', 'files/public/1734526462157.jpg', '2024-07-10 00:00:00');

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `categoryId` int NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `categoryName` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '分类名称',
  PRIMARY KEY (`categoryId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '分类表' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES (1, 1, 2, '感觉这个书不错的样子！', 2, 0, '2024-12-14 20:14:58');
INSERT INTO `comment` VALUES (2, 1, 2, '感觉还可以吧。', 2, 0, '2024-12-14 19:05:10');
INSERT INTO `comment` VALUES (3, 1, 2, '感觉还可以吧。', 2, 0, '2024-12-14 20:20:31');
INSERT INTO `comment` VALUES (4, 1, 3, '挪威的森林名字感觉不错！', 2, 0, '2024-12-15 19:16:57');
INSERT INTO `comment` VALUES (5, 1, 3, '挪威的森林', 2, 0, '2024-12-15 19:22:09');
INSERT INTO `comment` VALUES (6, 1, 2, '你好', 2, 1, '2024-12-15 19:23:40');
INSERT INTO `comment` VALUES (7, 1, 2, '**', 3, 0, '2024-12-15 19:23:55');
INSERT INTO `comment` VALUES (8, 1, 10, '这个史记也不错', 2, 0, '2024-12-15 21:08:33');
INSERT INTO `comment` VALUES (9, 4, 10, '你好', 2, 0, '2024-12-15 22:07:10');
INSERT INTO `comment` VALUES (10, 1, 2, '这个是百年孤独', 2, 0, '2024-12-15 23:15:10');
INSERT INTO `comment` VALUES (11, 2, 2, '红楼梦挺不错，经典！', 2, 0, '2024-12-17 00:28:14');
INSERT INTO `comment` VALUES (12, 2, 2, '赞赞赞', 2, 0, '2024-12-17 00:30:06');
INSERT INTO `comment` VALUES (13, 13, 2, '云南通志 小说', 2, 0, '2024-12-17 00:32:15');
INSERT INTO `comment` VALUES (14, 13, 2, '还可以', 2, 0, '2024-12-17 00:32:19');
INSERT INTO `comment` VALUES (15, 11, 2, '史记', 2, 0, '2024-12-17 00:39:26');

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
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '资源表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of resource
-- ----------------------------
INSERT INTO `resource` VALUES (4, 'application/pdf', 1, '《百年孤独》高长荣[译]（正版）.pdf', 2615527, 'files/auth/1734252417307.pdf', 'admin', '2024-12-15 12:50:15', 0, 10, 2);
INSERT INTO `resource` VALUES (5, 'image/png', 1, '杂谈2.png', 33922, 'files/auth/1734284945239.png', 'admin', '2024-12-16 01:49:05', 0, 2, 1);
INSERT INTO `resource` VALUES (6, 'application/x-msdownload', 2, 'wtsapi32.dll', 46080, 'files/auth/1734621909997.dll', 'admin123', '2024-12-19 23:25:10', 1, 0, 0);

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
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (2, 'kiwi2333', '123456', '张武', '13415000000', 'files/public/2_1734272754454.jpg', 1, '2024-12-09 23:23:25', 1);
INSERT INTO `user` VALUES (3, 'Huya2333', '123456', '张', '18624581213', NULL, 1, '2024-12-08 22:10:43', 1);
INSERT INTO `user` VALUES (4, 'Liwu12', '123456', '张', '18624581213', NULL, 1, '2024-12-08 22:10:50', 1);
INSERT INTO `user` VALUES (7, 'liuxiwu2', '123456', '张', '18624581213', NULL, 1, '2024-12-13 03:27:55', 1);
INSERT INTO `user` VALUES (8, 'liuxiw2', '123456', '张', '18624581213', NULL, 1, '2024-12-13 03:28:17', 1);
INSERT INTO `user` VALUES (10, 'ikun233', '123456', NULL, NULL, 'files/public/10_1734525111020.png', 1, '2024-12-15 19:26:27', 1);
INSERT INTO `user` VALUES (11, 'ikun2333', '123456', NULL, NULL, NULL, 0, '2024-12-16 12:05:51', 1);
INSERT INTO `user` VALUES (12, 'bjj2333', '123456', NULL, NULL, NULL, 1, '2024-12-18 10:44:26', 1);

SET FOREIGN_KEY_CHECKS = 1;
