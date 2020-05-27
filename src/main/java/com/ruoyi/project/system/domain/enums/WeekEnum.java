package com.ruoyi.project.system.domain.enums;

/**
 * Created by zhaoyl on 5/21/20.
 */
public enum  WeekEnum {
    周日,
    周一,
    周二,
    周三,
    周四,
    周五,
    周六;

    private String weekday;
    public int ordinal;

    WeekEnum(){
        this.ordinal = this.ordinal();
    }




    public String getWeekday() {
        return this.weekday;
    }

}
