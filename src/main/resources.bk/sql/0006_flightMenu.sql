-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('航班', '3', '1', 'flight', 'system/flight/index', 1, 'C', '0', '0', 'system:flight:list', 'flight', 'admin', '2018-03-01', 'ry', '2018-03-01', '航班菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('航班查询', @parentId, '1',  '#', '', 1,  'F', '0',  '0', 'system:flight:query',        '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('航班新增', @parentId, '2',  '#', '', 1,  'F', '0',  '0', 'system:flight:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('航班修改', @parentId, '3',  '#', '', 1,  'F', '0',  '0', 'system:flight:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('航班删除', @parentId, '4',  '#', '', 1,  'F', '0',  '0', 'system:flight:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('航班导出', @parentId, '5',  '#', '', 1,  'F', '0',  '0', 'system:flight:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');