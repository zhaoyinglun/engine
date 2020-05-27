CREATE TABLE `hk_option` (
  `key` varchar(255) NOT NULL,
  `value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
insert into sys_menu values('8', '策略', '0', '8', 'option',  NULL ,   1, 'M', '0', '0', '', 'option', 'admin', '2018-03-01', 'phy', '2018-03-01', '策略菜单');
insert into sys_menu values('119', '策略管理', '8', '1', 'option',  'system/option/index',   1, 'C', '0', '0', 'system:option:list', 'option', 'admin', '2018-03-01', 'phy', '2018-03-01', '策略菜单');
insert into sys_menu values('2018', '策略查询', '119', '1', '#',  '',   1, 'F', '0', '0', 'system:option:query', '#', 'admin', '2018-03-01', 'phy', '2018-03-01', '');
insert into sys_menu values('2019', '策略新增', '119', '2', '#',  '',   1, 'F', '0', '0', 'system:option:add', '#', 'admin', '2018-03-01', 'phy', '2018-03-01', '');
insert into sys_menu values('2020', '策略修改', '119', '3', '#',  '',   1, 'F', '0', '0', 'system:option:edit', '#', 'admin', '2018-03-01', 'phy', '2018-03-01', '');
insert into sys_menu values('2021', '策略删除', '119', '4', '#',  '',   1, 'F', '0', '0', 'system:option:remove', '#', 'admin', '2018-03-01', 'phy', '2018-03-01', '');
insert into sys_menu values('2022', '策略导出', '119', '5', '#',  '',   1, 'F', '0', '0', 'system:option:export', '#', 'admin', '2018-03-01', 'phy', '2018-03-01', '');

INSERT INTO `hk_option` VALUES ('AirPortRedius', '10');
INSERT INTO `hk_option` VALUES ('BackupAlarm', 'false');
INSERT INTO `hk_option` VALUES ('BackupContent', null);
INSERT INTO `hk_option` VALUES ('BackupPath', null);
INSERT INTO `hk_option` VALUES ('BackupStatus', 'false');
INSERT INTO `hk_option` VALUES ('BackupTimeSet', '0 15 10 ? * MON-FRI');
INSERT INTO `hk_option` VALUES ('CityRadiusKm', '25');
INSERT INTO `hk_option` VALUES ('DataBaseAlarm', 'false');
INSERT INTO `hk_option` VALUES ('FlightTables', 'hk_airline,hk_airport,hk_fleet,hk_flight,hk_prefix,hk_proxy');
INSERT INTO `hk_option` VALUES ('Invalid', 'null');
INSERT INTO `hk_option` VALUES ('MapDefaultLocation', '重庆,106.504959,29.533155');
INSERT INTO `hk_option` VALUES ('OptionTable', 'hk_option');
INSERT INTO `hk_option` VALUES ('QuartzTables', 'QRTZ_BLOB_TRIGGERS,QRTZ_CALENDARS,QRTZ_CRON_TRIGGERS,QRTZ_FIRED_TRIGGERS,QRTZ_JOB_DETAILS,QRTZ_LOCKS,QRTZ_PAUSED_TRIGGER_GRPS,QRTZ_SCHEDULER_STATE,QRTZ_SIMPLE_TRIGGERS,QRTZ_SIMPROP_TRIGGERS,QRTZ_TRIGGERS');
INSERT INTO `hk_option` VALUES ('RuoyiTablles', 'sys_dept,sys_user,sys_post,sys_role,sys_menu,sys_user_role,sys_role_menu,sys_role_dept,sys_user_post,sys_oper_log,sys_dict_type,sys_dict_data,sys_config,sys_logininfor,sys_job,sys_job_log,sys_notice,gen_table,gen_table_column');
INSERT INTO `hk_option` VALUES ('WeatherStatus', 'false');
INSERT INTO `hk_option` VALUES ('WhiteList', null);
INSERT INTO `hk_option` VALUES ('ZoomLevel', '1');
