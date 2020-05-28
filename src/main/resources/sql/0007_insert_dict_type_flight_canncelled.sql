insert into sys_dict_type values(100, '航班是否取消', 'hk_flight_cancelled', '0', 'admin', (SELECT DATE_FORMAT(NOW(),'%Y-%m-%d %H-%i-%s')), 'phy',null,'航班是否取消列表');

insert into sys_dict_data values(100, 1, '正常', '0', 'hk_flight_cancelled', '', 'primary', 'N', '0', 'admin' , (SELECT DATE_FORMAT(NOW(),'%Y-%m-%d %H-%i-%s')),'phy',null, '正常起飞' );
insert into sys_dict_data values(101, 2, '取消', '1', 'hk_flight_cancelled', '', 'danger', 'N', '0', 'admin' , (SELECT DATE_FORMAT(NOW(),'%Y-%m-%d %H-%i-%s')),'phy',null, '航班取消' );
