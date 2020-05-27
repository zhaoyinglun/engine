package com.ruoyi.project.system.domain.vo;

import sun.util.calendar.BaseCalendar;
import sun.util.calendar.CalendarUtils;

import java.util.Date;
import java.util.TimeZone;

/**
 * Created by zhaoyl on 5/22/20.
 */
public class LongDate extends Date {
    public Long longTime;

    public LongDate() {
        super();
        this.longTime = getTime()/1000;
    }

    public LongDate(long date) {
        super(date);
        this.longTime = getTime()/1000;
    }

    /** @deprecated */
    @Deprecated
    public LongDate(int year, int month, int date) {
        super(year, month, date, 0, 0, 0);
        this.longTime = getTime()/1000;
    }

    /** @deprecated */
    @Deprecated
    public LongDate(int year, int month, int date, int hrs, int min) {
        super(year, month, date, hrs, min, 0);
        this.longTime = getTime()/1000;
    }

    /** @deprecated */
    @Deprecated
    public LongDate(int year, int month, int date, int hrs, int min, int sec) {
        super(year, month, date, hrs, min, sec);
        this.longTime = getTime()/1000;
    }

    /** @deprecated */
    @Deprecated
    public LongDate(String s) {
        super(s);
        this.longTime = getTime()/1000;
    }

    @Override
    public void setTime(long time) {
        super.setTime(time);
        this.longTime = getTime()/1000;
    }



}
