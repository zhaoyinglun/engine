package com.ruoyi.project.system.domain;

import com.ruoyi.project.system.domain.vo.Point;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

import java.util.Arrays;

/**
 * 城市坐标功能对象 hk_city
 * 
 * @author zhaoyl
 * @date 2020-05-15
 */
public class HkCity extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 简称 */
    private Long id;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 坐标 */
    @Excel(name = "坐标")
    private String location;

    private int visiable;

    private Point point;

    public int getVisiable() {
        return visiable;
    }

    public void setVisiable(int visiable) {
        this.visiable = visiable;
    }

    public Point getPoint() {
        return point;
    }

    private void setPoint() {
        String[] ll = location.split(",");
        Point point = new Point(Double.parseDouble(ll[0]),Double.parseDouble(ll[1]));
        this.point = point;
    }

    public void setPoint(Point point) {
        this.point = point;
        this.location = String.valueOf(point.getLongitude()) + "," + String.valueOf(point.getLatitude());
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setLocation(String location) 
    {
        this.location = location;
        setPoint();
    }

    public String getLocation() 
    {
        return location;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("location", getLocation())
            .toString();
    }
}
