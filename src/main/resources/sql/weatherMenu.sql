CREATE TABLE `hk_weather` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一标识符',
  `weather` varchar(255) DEFAULT NULL COMMENT '天气',
  `cityid` int(11) DEFAULT NULL COMMENT '城市编号',
  `date` datetime DEFAULT NULL COMMENT '日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='天气表';