package com.ruoyi.project.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 机场对象 hk_airport
 * 
 * @author ruoyi
 * @date 2020-04-24
 */
public class HkAirport extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 时区 */
    @Excel(name = "时区")
    private String tz;

    /** 3位idata码 */
    @Excel(name = "3位idata码")
    private String iata;

    /** 4位icao码 */
    @Excel(name = "4位icao码")
    private String icao;

    /** 机场名 */
    @Excel(name = "机场名")
    private String friendlyname;

    /** 所在地 */
    @Excel(name = "所在地")
    private String friendlylocation;

    /** 所在地经度 */
    @Excel(name = "所在地经度")
    private Double longitude;

    /** 所在地纬度 */
    @Excel(name = "所在地纬度")
    private Double latitude;

    /** 与指定地点距离 */
    @Excel(name = "与指定地点距离")
    private Double distance;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setTz(String tz) 
    {
        this.tz = tz;
    }

    public String getTz() 
    {
        return tz;
    }
    public void setIata(String iata) 
    {
        this.iata = iata;
    }

    public String getIata() 
    {
        return iata;
    }
    public void setIcao(String icao) 
    {
        this.icao = icao;
    }

    public String getIcao() 
    {
        return icao;
    }
    public void setFriendlyname(String friendlyname) 
    {
        this.friendlyname = friendlyname;
    }

    public String getFriendlyname() 
    {
        return friendlyname;
    }
    public void setFriendlylocation(String friendlylocation) 
    {
        this.friendlylocation = friendlylocation;
    }

    public String getFriendlylocation() 
    {
        return friendlylocation;
    }
    public void setLongitude(Double longitude) 
    {
        this.longitude = longitude;
    }

    public Double getLongitude() 
    {
        return longitude;
    }
    public void setLatitude(Double latitude) 
    {
        this.latitude = latitude;
    }

    public Double getLatitude() 
    {
        return latitude;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "HkAirport{" +
                "id=" + id +
                ", tz='" + tz + '\'' +
                ", iata='" + iata + '\'' +
                ", icao='" + icao + '\'' +
                ", friendlyname='" + friendlyname + '\'' +
                ", friendlylocation='" + friendlylocation + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", distance=" + distance +
                '}';
    }
}
