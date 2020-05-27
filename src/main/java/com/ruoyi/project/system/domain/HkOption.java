package com.ruoyi.project.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 策略配置对象 hk_option
 * 
 * @author zhaoyl
 * @date 2020-05-06
 */
public class HkOption extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String key;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String value;

    public HkOption(String key, String value) {
        this.key = key;
        this.value = value;
    }
    public HkOption(){

    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getKey() 
    {
        return key;
    }
    public void setValue(String value) 
    {
        this.value = value;
    }

    public String getValue() 
    {
        return value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("key", getKey())
            .append("value", getValue())
            .toString();
    }
}
