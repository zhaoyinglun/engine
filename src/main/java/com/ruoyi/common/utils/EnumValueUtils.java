package com.ruoyi.common.utils;

import com.ruoyi.framework.config.ConfigValues;
import com.ruoyi.framework.manager.annotation.AttributeTypeConverter;
import com.ruoyi.framework.manager.annotation.DefaultValueAttribute;
import com.ruoyi.framework.manager.annotation.OptionBehaviourAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * Created by zhaoyl on 5/6/20.
 */
public class EnumValueUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnumValueUtils.class);

    private final Class<?> fieldType;// 字段类型
    private final String defaultValue;// 字段默认值: 如果数据库中没有配置, 则采用此值
    private final OptionBehaviourAttribute optionBehaviour;// 特定的解析方式，无法由字符串直接转化的属性需要指定git a解析方式，如集合，密码等

    public EnumValueUtils(Class<?> fieldType, String defaultValue, OptionBehaviourAttribute optionBehaviour) {
        this.fieldType = fieldType;
        this.defaultValue = defaultValue;
        this.optionBehaviour = optionBehaviour;
    }

    public Class<?> getFieldType() {
        return fieldType;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public OptionBehaviourAttribute getOptionBehaviour() {
        return optionBehaviour;
    }

    /**
     * parse the enum value by its attributes and return the type, default value, and option behaviour (if any) return
     * false if cannot find value in enum or cannot get type
     *
     * 通过反射机制，获取该配置枚举对象的field，从field获取注解中的变量类型，默认值，和解析方式
     */
    protected static EnumValueUtils parseEnumValue(String name) {

        // get field from enum for its attributes
        Field field;
        try {
            // 从ConfigValues类中找到相应名称的项
            field = ConfigValues.class.getField(name);
        } catch (Exception ex) {
            LOGGER.warn("Could not find enum value for option: '{}'", name);
            return null;
        }

        if (field != null && field.isAnnotationPresent(AttributeTypeConverter.class)) {
            // 获取其类型
            final Class<?> fieldType = field.getAnnotation(AttributeTypeConverter.class).value();
            String defaultValue = null;
            OptionBehaviourAttribute optionBehaviour = null;

            // 获取其默认值
            if (field.isAnnotationPresent(DefaultValueAttribute.class)) {
                defaultValue = field.getAnnotation(DefaultValueAttribute.class).value();
            }

            // 获取其解析方式
            if (field.isAnnotationPresent(OptionBehaviourAttribute.class)) {
                optionBehaviour = field.getAnnotation(OptionBehaviourAttribute.class);
            }
            return new EnumValueUtils(fieldType, defaultValue, optionBehaviour);
        }
        return null;
    }
}
