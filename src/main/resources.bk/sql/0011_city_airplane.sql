CREATE TABLE `hk_city` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `location` varchar(255) NOT NULL,
  `visiable` int(4) DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `nameunique` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




insert into sys_menu values('9', '城市', '0', '5', 'city',  NULL ,   1, 'M', '0', '0', '', 'city', 'admin', '2018-03-01', 'phy', '2018-03-01', '城市菜单');
insert into sys_menu values('120', '城市管理', '9', '1', 'city',  'system/city/index',   1, 'C', '0', '0', 'system:city:list', 'city', 'admin', '2018-03-01', 'phy', '2018-03-01', '城市菜单');
insert into sys_menu values('10', '飞机', '0', '1', 'airplane',  NULL ,   1, 'M', '0', '0', '', 'airplane', 'admin', '2018-03-01', 'phy', '2018-03-01', '飞机菜单');
insert into sys_menu values('121', '飞机管理', '10', '1', 'airplane',  'system/airplane/index',   1, 'C', '0', '0', 'system:airplane:list', 'city', 'admin', '2018-03-01', 'phy', '2018-03-01', '飞机菜单');
insert into sys_menu values('2023', '城市查询', '120', '1', '#',  '',   1, 'F', '0', '0', 'system:city:query', '#', 'admin', '2018-03-01', 'phy', '2018-03-01', '');
insert into sys_menu values('2024', '城市新增', '120', '2', '#',  '',   1, 'F', '0', '0', 'system:city:add', '#', 'admin', '2018-03-01', 'phy', '2018-03-01', '');
insert into sys_menu values('2025', '城市修改', '120', '3', '#',  '',   1, 'F', '0', '0', 'system:city:edit', '#', 'admin', '2018-03-01', 'phy', '2018-03-01', '');
insert into sys_menu values('2026', '城市删除', '120', '4', '#',  '',   1, 'F', '0', '0', 'system:city:remove', '#', 'admin', '2018-03-01', 'phy', '2018-03-01', '');
insert into sys_menu values('2027', '城市导出', '120', '5', '#',  '',   1, 'F', '0', '0', 'system:city:export', '#', 'admin', '2018-03-01', 'phy', '2018-03-01', '');

UPDATE sys_menu SET order_num = '7'  WHERE menu_id = '1' ;
UPDATE sys_menu SET order_num = '8'  WHERE menu_id = '2' ;
UPDATE sys_menu SET order_num = '9'  WHERE menu_id = '3' ;
UPDATE sys_menu SET order_num = '10'  WHERE menu_id = '4' ;
UPDATE sys_menu SET order_num = '2'  WHERE menu_id = '5' ;
UPDATE sys_menu SET order_num = '3'  WHERE menu_id = '6' ;
UPDATE sys_menu SET order_num = '4'  WHERE menu_id = '7' ;
UPDATE sys_menu SET order_num = '6'  WHERE menu_id = '8' ;

INSERT INTO `hkjs`.`hk_city` (`name`, `location`) VALUES ('上海', '121.472641,31.231707');
INSERT INTO `hkjs`.`hk_city` (`name`, `location`) VALUES ('乌鲁木齐', '87.61688,43.82663');
INSERT INTO `hkjs`.`hk_city` (`name`, `location`) VALUES ('兰州', '103.83417,36.06138');
INSERT INTO `hkjs`.`hk_city` (`name`, `location`) VALUES ('北京', '116.405289,39.904987');
INSERT INTO `hkjs`.`hk_city` (`name`, `location`) VALUES ('南京', '118.76741,32.041546');
INSERT INTO `hkjs`.`hk_city` (`name`, `location`) VALUES ('南宁', '108.320007,22.82402');
INSERT INTO `hkjs`.`hk_city` (`name`, `location`) VALUES ('南昌', '115.892151,28.676493');
INSERT INTO `hkjs`.`hk_city` (`name`, `location`) VALUES ('台北', '121.520076,25.030724');
INSERT INTO `hkjs`.`hk_city` (`name`, `location`) VALUES ('合肥', '117.283043,31.861191');
INSERT INTO `hkjs`.`hk_city` (`name`, `location`) VALUES ('呼和浩特', '111.75199,40.84149');
INSERT INTO `hkjs`.`hk_city` (`name`, `location`) VALUES ('哈尔滨', '126.642464,45.756966');
INSERT INTO `hkjs`.`hk_city` (`name`, `location`) VALUES ('天津', '117.190186,39.125595');
INSERT INTO `hkjs`.`hk_city` (`name`, `location`) VALUES ('太原', '112.549248,37.857014');
INSERT INTO `hkjs`.`hk_city` (`name`, `location`) VALUES ('广州', '113.28064,23.125177');
INSERT INTO `hkjs`.`hk_city` (`name`, `location`) VALUES ('成都', '104.065735,30.659462');
INSERT INTO `hkjs`.`hk_city` (`name`, `location`) VALUES ('拉萨', '91.1145,29.64415');
INSERT INTO `hkjs`.`hk_city` (`name`, `location`) VALUES ('昆明', '102.71225,25.040609');
INSERT INTO `hkjs`.`hk_city` (`name`, `location`) VALUES ('杭州', '120.15358,30.287458');
INSERT INTO `hkjs`.`hk_city` (`name`, `location`) VALUES ('武汉', '114.298569,30.584354');
INSERT INTO `hkjs`.`hk_city` (`name`, `location`) VALUES ('沈阳', '123.429092,41.796768');
INSERT INTO `hkjs`.`hk_city` (`name`, `location`) VALUES ('济南', '117.000923,36.675808');
INSERT INTO `hkjs`.`hk_city` (`name`, `location`) VALUES ('海口', '110.19989,20.04422');
INSERT INTO `hkjs`.`hk_city` (`name`, `location`) VALUES ('澳门', '113.54913,22.19875');
INSERT INTO `hkjs`.`hk_city` (`name`, `location`) VALUES ('石家庄', '114.502464,38.045475');
INSERT INTO `hkjs`.`hk_city` (`name`, `location`) VALUES ('福州', '119.306236,26.075302');
INSERT INTO `hkjs`.`hk_city` (`name`, `location`) VALUES ('西宁', '101.77782,36.61729');
INSERT INTO `hkjs`.`hk_city` (`name`, `location`) VALUES ('西安', '108.948021,34.263161');
INSERT INTO `hkjs`.`hk_city` (`name`, `location`) VALUES ('贵阳', '106.713478,26.578342');
INSERT INTO `hkjs`.`hk_city` (`name`, `location`) VALUES ('郑州', '113.665413,34.757977');
INSERT INTO `hkjs`.`hk_city` (`name`, `location`) VALUES ('重庆', '106.504959,29.533155');
INSERT INTO `hkjs`.`hk_city` (`name`, `location`) VALUES ('银川', '106.23248,38.48644');
INSERT INTO `hkjs`.`hk_city` (`name`, `location`) VALUES ('长春', '125.324501,43.886841');
INSERT INTO `hkjs`.`hk_city` (`name`, `location`) VALUES ('长沙', '112.982277,28.19409');
INSERT INTO `hkjs`.`hk_city` (`name`, `location`) VALUES ('香港', '114.16546,22.27534');