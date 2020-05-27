package com.ruoyi.project.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 航空公司对象 hk_airline
 * 
 * @author ruoyi
 * @date 2020-04-24
 */
public class HkAirline extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /**  航空公司全称 */
    @Excel(name = " 航空公司全称")
    private String fullname;

    /** 航空公司简称 */
    @Excel(name = "航空公司简称")
    private String shortname;

    /** 3位icao码 */
    @Excel(name = "3位icao码")
    private String icao;

    /** 2位idata码 */
    @Excel(name = "2位idata码")
    private String iata;

    /** 飞行称呼 */
    @Excel(name = "飞行称呼")
    private String callsign;

    /** 公司官网地址 */
    @Excel(name = "公司官网地址")
    private String url;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setFullname(String fullname) 
    {
        this.fullname = fullname;
    }

    public String getFullname() 
    {
        return fullname;
    }
    public void setShortname(String shortname) 
    {
        this.shortname = shortname;
    }

    public String getShortname() 
    {
        return shortname;
    }
    public void setIcao(String icao) 
    {
        this.icao = icao;
    }

    public String getIcao() 
    {
        return icao;
    }
    public void setIata(String iata) 
    {
        this.iata = iata;
    }

    public String getIata() 
    {
        return iata;
    }
    public void setCallsign(String callsign) 
    {
        this.callsign = callsign;
    }

    public String getCallsign() 
    {
        return callsign;
    }
    public void setUrl(String url) 
    {
        this.url = url;
    }

    public String getUrl() 
    {
        return url;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("fullname", getFullname())
            .append("shortname", getShortname())
            .append("icao", getIcao())
            .append("iata", getIata())
            .append("callsign", getCallsign())
            .append("url", getUrl())
            .toString();
    }
}
