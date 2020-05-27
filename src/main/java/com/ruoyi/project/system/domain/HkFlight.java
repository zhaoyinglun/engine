package com.ruoyi.project.system.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.project.system.domain.enums.WeekEnum;
import com.ruoyi.project.system.domain.vo.LongDate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 航班对象 hk_flight
 * 
 * @author ruoyi
 * @date 2020-04-24
 */
public class HkFlight extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * IATA航班号
     */
    @Excel(name = "IATA航班号")
    private String iataident;

    /**
     * 航班号
     */
    @Excel(name = "航班号")
    private String ident;

    /**
     * 航班号全称
     */
    @Excel(name = "航班号全称")
    private String friendlyident;

    /**
     * 航空公司ICAO码
     */
    @Excel(name = "航空公司ICAO码")
    private String airlineicao;

    /**
     * 飞机型号
     */
    @Excel(name = "飞机型号")
    private String flightype;

    /**
     * 飞机型号全称
     */
    @Excel(name = "飞机型号全称")
    private String flightypefriendlytype;

    /**
     * 起飞地点3位IATA码
     */
    @Excel(name = "起飞地点4位ICAO码")
    private String origin;

    /**
     * 降落地点3位IATA码
     */
    @Excel(name = "降落地点4位ICAO码")
    private String destination;


    /**
     * 计划起飞时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "计划起飞时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private LongDate takeofftimesscheduled;

    /**
     * 计划降落时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "计划降落时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private LongDate landingtimesscheduled;


    private WeekEnum takeOffWeek;

    private WeekEnum landWeek;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LongDate updatedTime;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setIataident(String iataident) {
        this.iataident = iataident;
    }

    public String getIataident() {
        return iataident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getIdent() {
        return ident;
    }

    public void setFriendlyident(String friendlyident) {
        this.friendlyident = friendlyident;
    }

    public String getFriendlyident() {
        return friendlyident;
    }

    public void setAirlineicao(String airlineicao) {
        this.airlineicao = airlineicao;
    }

    public String getAirlineicao() {
        return airlineicao;
    }

    public void setFlightype(String flightype) {
        this.flightype = flightype;
    }

    public String getFlightype() {
        return flightype;
    }

    public void setFlightypefriendlytype(String flightypefriendlytype) {
        this.flightypefriendlytype = flightypefriendlytype;
    }

    public String getFlightypefriendlytype() {
        return flightypefriendlytype;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getOrigin() {
        return origin;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestination() {
        return destination;
    }

    public void setTakeofftimesscheduled(Date takeofftimesscheduled) {
        this.takeofftimesscheduled = new LongDate(takeofftimesscheduled.getTime());
    }

    public Date getTakeofftimesscheduled() {
        return takeofftimesscheduled;
    }

    public void setLandingtimesscheduled(Date landingtimesscheduled) {
        this.landingtimesscheduled = new LongDate(landingtimesscheduled.getTime());
    }

    public Date getLandingtimesscheduled() {
        return landingtimesscheduled;
    }

    public WeekEnum getTakeOffWeek() {
        return takeOffWeek;
    }

    public void setTakeOffWeek(WeekEnum takeOffWeek) {
        this.takeOffWeek = takeOffWeek;
    }

    public WeekEnum getLandWeek() {
        return landWeek;
    }

    public void setLandWeek(WeekEnum landWeek) {
        this.landWeek = landWeek;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = new  LongDate(updatedTime.getTime());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("iataident", getIataident())
                .append("ident", getIdent())
                .append("friendlyident", getFriendlyident())
                .append("airlineicao", getAirlineicao())
                .append("flightype", getFlightype())
                .append("flightypefriendlytype", getFlightypefriendlytype())
                .append("origin", getOrigin())
                .append("destination", getDestination())
                .append("takeofftimesscheduled", getTakeofftimesscheduled())
                .append("landingtimesscheduled", getLandingtimesscheduled())
                .append("takeOffWeek", getTakeOffWeek())
                .append("landWeek", getTakeOffWeek())
                .append("updatedTime", getUpdatedTime())
                .toString();
    }
}
