package com.ruoyi.framework.config;


import com.ruoyi.framework.manager.annotation.AttributeTypeConverter;
import com.ruoyi.framework.manager.annotation.DefaultValueAttribute;
import com.ruoyi.framework.manager.annotation.IsCity;
import com.ruoyi.framework.manager.annotation.OptionBehaviourAttribute;
import com.ruoyi.framework.manager.annotation.Reloadable;
import com.ruoyi.project.system.domain.vo.Point;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhaoyl on 5/6/20.
 */
public enum  ConfigValues {
    @DefaultValueAttribute("")
    @Reloadable
    @AttributeTypeConverter(String.class)
    BackupContent,

    @DefaultValueAttribute("0 15 10 ? * MON-FRI")
    @Reloadable
    @AttributeTypeConverter(String.class)
    BackupTimeSet,

    @DefaultValueAttribute("")
    @AttributeTypeConverter(String.class)
    BackupPath,

    @DefaultValueAttribute("false")
    @Reloadable
    @AttributeTypeConverter(Boolean.class)
    BackupStatus,

    @Reloadable
    @DefaultValueAttribute("false")
    @AttributeTypeConverter(Boolean.class)
    DataBaseAlarm,

    @DefaultValueAttribute("false")
    @Reloadable
    @AttributeTypeConverter(Boolean.class)
    BackupAlarm,

    @OptionBehaviourAttribute(behaviour = OptionBehaviour.CommaSeparatedStringArray)
    @DefaultValueAttribute("")
    @AttributeTypeConverter(List.class)
    WhiteList,

    @DefaultValueAttribute("false")
    @AttributeTypeConverter(Boolean.class)
    WeatherStatus,

    @DefaultValueAttribute("10")
    @AttributeTypeConverter(Integer.class)
    AirPortRedius,

    @DefaultValueAttribute("25")
    @AttributeTypeConverter(Integer.class)
    CityRadiusKm,

    @Reloadable
    @DefaultValueAttribute("重庆,106.504959,29.533155")
    @AttributeTypeConverter(String.class)
    MapDefaultLocation,

    @DefaultValueAttribute("1")
    @Reloadable
    @AttributeTypeConverter(Integer.class)
    ZoomLevel,


    @OptionBehaviourAttribute(behaviour = OptionBehaviour.CommaSeparatedStringArray)
    @AttributeTypeConverter(List.class)
    @DefaultValueAttribute("sys_dept,sys_user,sys_post,sys_role,sys_menu,sys_user_role," +
            "sys_role_menu,sys_role_dept,sys_user_post,sys_oper_log,sys_dict_type,sys_dict_data," +
            "sys_config,sys_logininfor,sys_job,sys_job_log,sys_notice,gen_table,gen_table_column")
    RuoyiTablles,

    @OptionBehaviourAttribute(behaviour = OptionBehaviour.CommaSeparatedStringArray)
    @AttributeTypeConverter(List.class)
    @DefaultValueAttribute("QRTZ_BLOB_TRIGGERS,QRTZ_CALENDARS,QRTZ_CRON_TRIGGERS," +
            "QRTZ_FIRED_TRIGGERS,QRTZ_JOB_DETAILS,QRTZ_LOCKS,QRTZ_PAUSED_TRIGGER_GRPS," +
            "QRTZ_SCHEDULER_STATE,QRTZ_SIMPLE_TRIGGERS,QRTZ_SIMPROP_TRIGGERS,QRTZ_TRIGGERS")
    QuartzTables,

    @OptionBehaviourAttribute(behaviour = OptionBehaviour.CommaSeparatedStringArray)
    @AttributeTypeConverter(List.class)
    @DefaultValueAttribute("hk_airline,hk_airport,hk_fleet,hk_flight,hk_prefix,hk_proxy")
    FlightTables,

    @OptionBehaviourAttribute(behaviour = OptionBehaviour.CommaSeparatedStringArray)
    @AttributeTypeConverter(List.class)
    @DefaultValueAttribute("hk_option")
    OptionTable,



    Invalid;
    public static List<String> getNames(){
        List<String> list = new ArrayList<>();
        for (ConfigValues va : ConfigValues.values()){
            list.add(va.name());
        }
        return list;
    }
}
