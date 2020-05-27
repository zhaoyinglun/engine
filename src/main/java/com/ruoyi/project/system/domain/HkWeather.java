package com.ruoyi.project.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.project.system.domain.vo.Point;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 天气对象 hk_weather
 * 
 * @author ruoyi
 * @date 2020-05-25
 */
public class HkWeather extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 唯一标识符 */
    private Long id;

    /** 天气 */
    @Excel(name = "天气")
    private String weather;

    /** 城市编号 */
    @Excel(name = "城市编号")
    private Long cityid;

    /** 日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date date;

    private Point point;

    private String location;

    private String cityName;

    private void setPoint() {
        String[] ll = location.split(",");
        Point point = new Point(Double.parseDouble(ll[0]),Double.parseDouble(ll[1]));
        this.point = point;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
        this.location = String.valueOf(point.getLongitude()) + "," + String.valueOf(point.getLatitude());
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
        setPoint();
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setWeather(String weather) 
    {
        this.weather = weather;
    }

    public String getWeather() 
    {
        return weather;
    }
    public void setCityid(Long cityid) 
    {
        this.cityid = cityid;
    }

    public Long getCityid() 
    {
        return cityid;
    }
    public void setDate(Date date) 
    {
        this.date = date;
    }

    public Date getDate() 
    {
        return date;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("weather", getWeather())
            .append("cityid", getCityid())
            .append("date", getDate())
            .toString();
    }
}
