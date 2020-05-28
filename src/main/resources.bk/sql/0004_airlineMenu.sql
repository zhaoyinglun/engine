-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('航空公司', '3', '1', 'airline', 'system/airline/index', 1, 'C', '0', '0', 'system:airline:list', 'airline', 'admin', '2018-03-01', 'ry', '2018-03-01', '航空公司菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('航空公司查询', @parentId, '1',  '#', '', 1,  'F', '0',  '0', 'system:airline:query',        '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('航空公司新增', @parentId, '2',  '#', '', 1,  'F', '0',  '0', 'system:airline:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('航空公司修改', @parentId, '3',  '#', '', 1,  'F', '0',  '0', 'system:airline:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('航空公司删除', @parentId, '4',  '#', '', 1,  'F', '0',  '0', 'system:airline:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('航空公司导出', @parentId, '5',  '#', '', 1,  'F', '0',  '0', 'system:airline:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');