-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('机场', '3', '1', 'airport', 'system/airport/index', 1, 'C', '0', '0', 'system:airport:list', 'airport', 'admin', '2018-03-01', 'ry', '2018-03-01', '机场菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('机场查询', @parentId, '1',  '#', '', 1,  'F', '0',  '0', 'system:airport:query',        '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('机场新增', @parentId, '2',  '#', '', 1,  'F', '0',  '0', 'system:airport:add',          '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('机场修改', @parentId, '3',  '#', '', 1,  'F', '0',  '0', 'system:airport:edit',         '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('机场删除', @parentId, '4',  '#', '', 1,  'F', '0',  '0', 'system:airport:remove',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');

insert into sys_menu  (menu_name, parent_id, order_num, path, component, is_frame, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('机场导出', @parentId, '5',  '#', '', 1,  'F', '0',  '0', 'system:airport:export',       '#', 'admin', '2018-03-01', 'ry', '2018-03-01', '');