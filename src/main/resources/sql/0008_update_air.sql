-- 添加二级菜单
insert into sys_menu values('116', '公司管理', '5', '1', 'airline', 'system/airline/index',   1, 'C', '0', '0', '', 'airline', 'admin', '2018-03-01', 'ry', '2018-03-01', '航空公司菜单');
insert into sys_menu values('117', '机场管理', '6', '1', 'airport', 'system/airport/index',   1, 'C', '0', '0', '', 'airport', 'admin', '2018-03-01', 'ry', '2018-03-01', '机场菜单');
insert into sys_menu values('118', '航班管理', '7', '1', 'flight',  'system/flight/index',   1, 'C', '0', '0', '', 'flight', 'admin', '2018-03-01', 'ry', '2018-03-01', '航班菜单');

-- 修改已有菜单
UPDATE sys_menu SET menu_id = '5', menu_type = 'M' , parent_id = '0' , order_num = '5' , component = NULL WHERE menu_name = '航空公司';
UPDATE sys_menu SET menu_id = '6', menu_type = 'M' , parent_id = '0' , order_num = '6' , component = NULL WHERE menu_name = '机场';
UPDATE sys_menu SET menu_id = '7', menu_type = 'M' , parent_id = '0' , order_num = '7' , component = NULL WHERE menu_name = '航班';
UPDATE sys_menu SET visible = '1', menu_name = '鄱湖云官网' , path = 'http://www.htphy.com/' WHERE menu_id = '4';

-- 修改已有菜单
UPDATE sys_menu SET parent_id = '116' , menu_type = 'F' WHERE menu_name LIKE '航空公司%' AND menu_id NOT IN ('5','116');
UPDATE sys_menu SET parent_id = '117' , menu_type = 'F' WHERE menu_name LIKE '机场%' AND menu_id NOT IN ('6','117');
UPDATE sys_menu SET parent_id = '118' , menu_type = 'F' WHERE menu_name LIKE '航班%' AND menu_id NOT IN ('7','118');
UPDATE sys_menu SET update_by = 'phy';

-- 修改部分字符
UPDATE sys_dept SET dept_name = '鄱湖云科技' WHERE dept_name = '若依科技';
UPDATE sys_dept SET dept_name = '总公司' WHERE dept_name = '深圳总公司';
UPDATE sys_dept SET dept_name = '分公司' WHERE dept_name = '长沙分公司';
UPDATE sys_dept SET update_by = 'phy';

-- 修改部分字符
UPDATE sys_user SET email = 'hkjs@163.com' , nick_name = '航空监视' WHERE user_id = '1';
UPDATE sys_user SET email = 'hkjs@qq.com' , nick_name = '航空监视' , user_name = 'hkjs' WHERE user_id = '2';
UPDATE sys_user SET update_by = 'phy';

-- 修改部分字符
UPDATE sys_post SET post_name = '小组组长' , post_code = 'gm' WHERE post_id = '3';
UPDATE sys_post SET update_by = 'phy';

