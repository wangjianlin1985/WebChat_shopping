/*
 Navicat Premium Data Transfer

 Source Server         : mysql5.6
 Source Server Type    : MySQL
 Source Server Version : 50620
 Source Host           : localhost:3306
 Source Schema         : shopping_db

 Target Server Type    : MySQL
 Target Server Version : 50620
 File Encoding         : 65001

 Date: 01/04/2021 01:05:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `username` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `password` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('a', 'a');

-- ----------------------------
-- Table structure for t_evaluate
-- ----------------------------
DROP TABLE IF EXISTS `t_evaluate`;
CREATE TABLE `t_evaluate`  (
  `evaluateId` int(11) NOT NULL AUTO_INCREMENT COMMENT '评价编号',
  `productObj` int(11) NOT NULL COMMENT '商品名称',
  `userObj` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `content` varchar(800) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '评价内容',
  `evaluateTime` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '评价时间',
  PRIMARY KEY (`evaluateId`) USING BTREE,
  INDEX `productObj`(`productObj`) USING BTREE,
  INDEX `userObj`(`userObj`) USING BTREE,
  CONSTRAINT `t_evaluate_ibfk_1` FOREIGN KEY (`productObj`) REFERENCES `t_product` (`productId`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_evaluate_ibfk_2` FOREIGN KEY (`userObj`) REFERENCES `t_userinfo` (`user_name`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_evaluate
-- ----------------------------
INSERT INTO `t_evaluate` VALUES (1, 1, '13599024234', 'bbb', '2021-03-28');
INSERT INTO `t_evaluate` VALUES (12, 3, '13058019334', '衣服很好穿，喜欢！', '2021-04-01 00:39:22');
INSERT INTO `t_evaluate` VALUES (13, 1, '13058019334', '非常好用的手机！', '2021-04-01 00:39:39');

-- ----------------------------
-- Table structure for t_leaveword
-- ----------------------------
DROP TABLE IF EXISTS `t_leaveword`;
CREATE TABLE `t_leaveword`  (
  `leaveWordId` int(11) NOT NULL AUTO_INCREMENT COMMENT '留言id',
  `leaveTitle` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '留言标题',
  `leaveContent` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '留言内容',
  `userObj` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '留言人',
  `leaveTime` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '留言时间',
  `replyContent` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '管理回复',
  `replyTime` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '回复时间',
  PRIMARY KEY (`leaveWordId`) USING BTREE,
  INDEX `userObj`(`userObj`) USING BTREE,
  CONSTRAINT `t_leaveword_ibfk_1` FOREIGN KEY (`userObj`) REFERENCES `t_userinfo` (`user_name`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_leaveword
-- ----------------------------
INSERT INTO `t_leaveword` VALUES (1, '管理你好，最近优惠吗', '最近快五一了，有什么优惠吗？', '13599024234', '2021-03-15 13:49:11', '有的，请看最新公告消息', '2021-03-28 13:49:15');
INSERT INTO `t_leaveword` VALUES (2, '22', '333', '13688886666', '2021-03-28 22:13:06', '--', '--');
INSERT INTO `t_leaveword` VALUES (3, 'bbb11', 'cccc', '13058019334', '2021-04-01 00:43:02', '--', '--');
INSERT INTO `t_leaveword` VALUES (4, 'ccc', 'ddddd', '13058019334', '2021-04-01 00:43:12', '--', '--');

-- ----------------------------
-- Table structure for t_notice
-- ----------------------------
DROP TABLE IF EXISTS `t_notice`;
CREATE TABLE `t_notice`  (
  `noticeId` int(11) NOT NULL AUTO_INCREMENT COMMENT '公告id',
  `title` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标题',
  `content` varchar(800) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '公告内容',
  `publishDate` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发布时间',
  PRIMARY KEY (`noticeId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_notice
-- ----------------------------
INSERT INTO `t_notice` VALUES (1, '最新优惠活动', '凡是购买下单的朋友，送电饭煲一个', '2021-03-22 12:40:43');
INSERT INTO `t_notice` VALUES (2, '小程序购物商城上线', '这里个商品种类多，发货快！', '2021-03-31 19:11:01');

-- ----------------------------
-- Table structure for t_orderinfo
-- ----------------------------
DROP TABLE IF EXISTS `t_orderinfo`;
CREATE TABLE `t_orderinfo`  (
  `orderNo` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'orderNo',
  `userObj` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '下单用户',
  `totalMoney` float NOT NULL COMMENT '订单总金额',
  `payWayObj` int(11) NOT NULL COMMENT '支付方式',
  `payAccount` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '支付账号',
  `orderState` int(11) NOT NULL COMMENT '订单状态',
  `receiveName` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货人',
  `telephone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货人电话',
  `address` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '收货人地址',
  `orderMemo` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单备注',
  `orderTime` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '下单时间',
  PRIMARY KEY (`orderNo`) USING BTREE,
  INDEX `userObj`(`userObj`) USING BTREE,
  INDEX `orderState`(`orderState`) USING BTREE,
  INDEX `payWayObj`(`payWayObj`) USING BTREE,
  CONSTRAINT `t_orderinfo_ibfk_1` FOREIGN KEY (`userObj`) REFERENCES `t_userinfo` (`user_name`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_orderinfo_ibfk_2` FOREIGN KEY (`orderState`) REFERENCES `t_orderstate` (`stateId`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_orderinfo_ibfk_3` FOREIGN KEY (`payWayObj`) REFERENCES `t_payway` (`payWayId`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_orderinfo
-- ----------------------------
INSERT INTO `t_orderinfo` VALUES ('20210401002344952', '13058019334', 2252, 1, 'dashen@126.com', 5, '李小涛', '13509823423', '四川成都红星路10号', '我用支付宝付款，老板请注意查收！大概5分钟内完成付款！\r\n我是老板，我收到你的资金了，刚发货了！', '2021-04-01 00:23:44');
INSERT INTO `t_orderinfo` VALUES ('20210401002617065', '13058019334', 3868, 3, '6222092312332224328', 1, '李小涛', '13598012342', '四川成都', '银行网银付款！注意查收！', '2021-04-01 00:26:17');

-- ----------------------------
-- Table structure for t_orderitem
-- ----------------------------
DROP TABLE IF EXISTS `t_orderitem`;
CREATE TABLE `t_orderitem`  (
  `itemId` int(11) NOT NULL AUTO_INCREMENT COMMENT '条目id',
  `orderObj` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '所属订单',
  `productObj` int(11) NOT NULL COMMENT '订单商品',
  `price` float NOT NULL COMMENT '商品单价',
  `orderNumer` int(11) NOT NULL COMMENT '购买数量',
  PRIMARY KEY (`itemId`) USING BTREE,
  INDEX `orderObj`(`orderObj`) USING BTREE,
  INDEX `productObj`(`productObj`) USING BTREE,
  CONSTRAINT `t_orderitem_ibfk_1` FOREIGN KEY (`orderObj`) REFERENCES `t_orderinfo` (`orderNo`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_orderitem_ibfk_2` FOREIGN KEY (`productObj`) REFERENCES `t_product` (`productId`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_orderitem
-- ----------------------------
INSERT INTO `t_orderitem` VALUES (15, '20210401002344952', 3, 342, 2);
INSERT INTO `t_orderitem` VALUES (16, '20210401002344952', 1, 1568, 1);
INSERT INTO `t_orderitem` VALUES (17, '20210401002617065', 2, 2300, 1);
INSERT INTO `t_orderitem` VALUES (18, '20210401002617065', 1, 1568, 1);

-- ----------------------------
-- Table structure for t_orderstate
-- ----------------------------
DROP TABLE IF EXISTS `t_orderstate`;
CREATE TABLE `t_orderstate`  (
  `stateId` int(11) NOT NULL AUTO_INCREMENT COMMENT '状态编号',
  `stateName` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单状态名称',
  PRIMARY KEY (`stateId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_orderstate
-- ----------------------------
INSERT INTO `t_orderstate` VALUES (1, '待支付');
INSERT INTO `t_orderstate` VALUES (2, '已支付');
INSERT INTO `t_orderstate` VALUES (3, '待发货');
INSERT INTO `t_orderstate` VALUES (4, '已发货');
INSERT INTO `t_orderstate` VALUES (5, '交易完成');

-- ----------------------------
-- Table structure for t_payway
-- ----------------------------
DROP TABLE IF EXISTS `t_payway`;
CREATE TABLE `t_payway`  (
  `payWayId` int(11) NOT NULL AUTO_INCREMENT COMMENT '支付方式id',
  `payWayName` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '支付方式名称',
  PRIMARY KEY (`payWayId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_payway
-- ----------------------------
INSERT INTO `t_payway` VALUES (1, '支付宝');
INSERT INTO `t_payway` VALUES (2, '微信');
INSERT INTO `t_payway` VALUES (3, '银行卡');

-- ----------------------------
-- Table structure for t_product
-- ----------------------------
DROP TABLE IF EXISTS `t_product`;
CREATE TABLE `t_product`  (
  `productId` int(11) NOT NULL AUTO_INCREMENT COMMENT '商品id',
  `productClassObj` int(11) NOT NULL COMMENT '商品类别',
  `productName` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品名称',
  `mainPhoto` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品主图',
  `price` float NOT NULL COMMENT '商品价格',
  `productCount` int(11) NOT NULL COMMENT '商量数量',
  `productDesc` varchar(800) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品描述',
  `recommendFlag` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '是否推荐',
  `hotNum` int(11) NOT NULL COMMENT '人气值',
  `addTime` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上架时间',
  PRIMARY KEY (`productId`) USING BTREE,
  INDEX `productClassObj`(`productClassObj`) USING BTREE,
  CONSTRAINT `t_product_ibfk_1` FOREIGN KEY (`productClassObj`) REFERENCES `t_productclass` (`classId`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_product
-- ----------------------------
INSERT INTO `t_product` VALUES (1, 1, '苹果SE手机', 'upload/187bd258-99d1-4cd8-abea-333355894b9e.jpg', 1568, 18, '最新上市的新品2代苹果手机，小屏手机王者归来！', '是', 2, '2021-03-28 12:40:31');
INSERT INTO `t_product` VALUES (2, 1, '戴尔笔记本电脑', 'upload/31256367-053b-42a2-9d5b-982542437f3d.jpg', 2300, 9, 'CC证书编号: 2020010902285984品牌: Dell/戴尔系列: 灵越15(3558)型号: Ins15ER-1528BB内存容量: 16G硬盘容量: 512GB固态硬盘CPU: 英特尔 酷睿 i7-4710HQ售后服务: 全国联保屏幕尺寸: 15英寸分辨率: 1600x900显存容量: 2G光驱类型: 无光驱厚度: 10.0mm(含)-15.0mm(不含)颜色分类: 配置一 配置二 配置三 配置四 配置五 配置六 配置七 配置八 配置九 配置十 配置十一 配置十二 配置十三 配置十四 配置十五 特惠套餐 配置00重量: 1kg(含)-1.5kg(不含)输入设备: 触摸板 全尺寸背光键盘是否触摸屏: 非触摸屏固态硬盘: 512GB', '是', 1, '2021-03-28 19:28:42');
INSERT INTO `t_product` VALUES (3, 2, '纯棉短袖t恤女2021新', 'upload/8c9f69a0-c091-46a2-936f-5c570c240edc.jpg', 342, 18, '品牌: 志述适用年龄: 18-24周岁尺码: S M L XL XXL图案: 植物花卉 几何图案 字母 纯色风格: 通勤通勤: 韩版领型: 圆领流行元素: 印花 绣花主要颜色: 黄色 白色 黑色 红色 紫色 绿色 孔雀蓝 黄色（刺绣爱心） 白色（刺绣爱心） 黑色（刺绣爱心） 红色（刺绣爱心） 紫色（刺绣爱心） 嫩粉色（刺绣爱心） 孔雀蓝（刺绣爱心） 孔雀蓝（刺绣海豚） 白色（刺绣海豚） 紫色（刺绣海豚） 嫩粉色 001白色小雏菊 001黄色小雏菊 001紫色小雏菊 001红色小雏菊 001黑色小雏菊 002黄色大雏菊 002紫色大雏菊 002红色大雏菊 002黑色大雏菊 003白色牛油果 003黄色牛油果 003紫色牛油果 003红色牛油果 003黑色牛油果 016白色 016黄色 018白色 018黄色 018紫色 019白色 019黑色 021白色 021黑色 021黄色 021紫色 022黑色 022紫色袖型: 常规货号: ZHISHU8673图案文化: 青春年份季节: 2021年夏季袖长: 短袖衣长: 常规款服装版型: 宽松销售渠道类型: 纯电商(只在线上销售)材质成分: 棉100%', '是', 2, '2021-03-29 15:26:29');
INSERT INTO `t_product` VALUES (4, 3, '四川黄心猕猴桃30枚', 'upload/3f0f5126-2c4d-499d-9dd9-605bab035f00.jpg', 34.8, 100, '厂名：甘福园旗舰店厂址：甘福园旗舰店厂家联系方式：4009039588配料表：四川黄心猕猴桃储藏方法：低温保鲜保质期：7 天食品添加剂：无品牌: 甘福园价格: 0-50元产地: 中国大陆省份: 四川省城市: 成都市是否为有机食品: 否同城服务: 同城24小时物流送货上门包装方式: 包装售卖方式: 单品套餐份量: 3人份套餐周期: 1周配送频次: 1周2次特产品类: 蒲江猕猴桃水果种类: 黄心热卖时间: 1月 2月 3月 4月 5月 6月 7月 8月 9月 10月 11月 12月单箱规格: 精选整箱净含量: 2500g', '是', 0, '2021-03-29 15:28:35');
INSERT INTO `t_product` VALUES (5, 4, 'Java网络编程核心技术详解', 'upload/8be870fd-66a6-43fd-8189-c2ca326bdae0.jpg', 64.5, 50, '产品名称：Java网络编程核心技术详解...品牌: 电子工业出版社ISBN编号: 9787121383151书名: Java网络编程核心技术详解:视频微课版作者: 无定价: 129.00元书名: Java网络编程核心技术详解:视频微课版开本: 16开是否是套装: 否出版社名称: 电子工业出版社出版时间: 2021-03', '是', 0, '2021-03-31 18:49:36');
INSERT INTO `t_product` VALUES (6, 5, '雅诗兰黛护肤品套装女', 'upload/4638386d-b911-46fc-8709-bbd8d087b17b.jpg', 568, 82, '品牌: 百瑟品名: 多肽贵妇八件套产地: 中国颜色分类: 多肽贵妇八件套批准文号: 粤G妆网备字2019145275保质期: 3年适合肤质: 任何肤质上市时间: 2020年功效: 补水 收缩毛孔舒缓肌肤 平衡油脂分泌规格类型: 正常规格是否为特殊用途化妆品: 否', '是', 0, '2021-03-31 18:52:00');

-- ----------------------------
-- Table structure for t_productclass
-- ----------------------------
DROP TABLE IF EXISTS `t_productclass`;
CREATE TABLE `t_productclass`  (
  `classId` int(11) NOT NULL AUTO_INCREMENT COMMENT '类别id',
  `className` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '类别名称',
  `classDesc` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '类别描述',
  PRIMARY KEY (`classId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_productclass
-- ----------------------------
INSERT INTO `t_productclass` VALUES (1, '电脑手机', '电脑手机');
INSERT INTO `t_productclass` VALUES (2, '衣服服装', '衣服服装');
INSERT INTO `t_productclass` VALUES (3, '水果生鲜', '水果生鲜');
INSERT INTO `t_productclass` VALUES (4, '精品图书', '精品图书');
INSERT INTO `t_productclass` VALUES (5, '化妆品', '化妆品');

-- ----------------------------
-- Table structure for t_shopcart
-- ----------------------------
DROP TABLE IF EXISTS `t_shopcart`;
CREATE TABLE `t_shopcart`  (
  `cartId` int(11) NOT NULL AUTO_INCREMENT COMMENT '购物车id',
  `productObj` int(11) NOT NULL COMMENT '商品',
  `userObj` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户',
  `price` float NOT NULL COMMENT '单价',
  `buyNum` int(11) NOT NULL COMMENT '购买数量',
  PRIMARY KEY (`cartId`) USING BTREE,
  INDEX `productObj`(`productObj`) USING BTREE,
  INDEX `userObj`(`userObj`) USING BTREE,
  CONSTRAINT `t_shopcart_ibfk_1` FOREIGN KEY (`productObj`) REFERENCES `t_product` (`productId`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_shopcart_ibfk_2` FOREIGN KEY (`userObj`) REFERENCES `t_userinfo` (`user_name`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 36 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_shopcart
-- ----------------------------
INSERT INTO `t_shopcart` VALUES (1, 1, '13599024234', 22, 3);
INSERT INTO `t_shopcart` VALUES (2, 2, '13599024234', 22, 2);
INSERT INTO `t_shopcart` VALUES (29, 1, '13688886666', 812, 1);

-- ----------------------------
-- Table structure for t_userinfo
-- ----------------------------
DROP TABLE IF EXISTS `t_userinfo`;
CREATE TABLE `t_userinfo`  (
  `user_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'user_name',
  `password` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登录密码',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '姓名',
  `gender` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '性别',
  `birthDate` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出生日期',
  `userPhoto` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户照片',
  `telephone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '邮箱',
  `address` varchar(80) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '家庭地址',
  `regTime` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '注册时间',
  `openid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`user_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_userinfo
-- ----------------------------
INSERT INTO `t_userinfo` VALUES ('13058019334', '--', '牵着蜗牛去逛街', '男', '2020-01-01', 'upload/93a2c6b82ba0462f8717b9c169785e58', '13058019334', 'test@126.com', '四川成都红星路', '2021-03-31 20:23:35', 'oM7Mu5XyeVJSc8roaUCRlcz_IP9k');
INSERT INTO `t_userinfo` VALUES ('13599024234', '123', '王丽英', '女', '2021-03-16', 'upload/17ccbec4-6261-48a4-959a-bcbda7600996.jpg', '13510834234', 'xiaotao@163.com', '四川省成都二仙桥10号', '2021-03-28 12:41:19', NULL);
INSERT INTO `t_userinfo` VALUES ('13688886666', '--', '李小涛', '男', '2020-01-01', 'upload/6ee23d20-a95b-4370-a5fe-150a7071370b.jpg', '--', '--', '--', '2021-03-28 21:51:30', 'oM7Mu5XyeVJSc8roaUCRlcz_IP9j');

SET FOREIGN_KEY_CHECKS = 1;
