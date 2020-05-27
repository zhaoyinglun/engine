package com.ruoyi.framework.manager.annotation;

import com.ruoyi.framework.config.ConfigValues;
import com.ruoyi.framework.config.OptionBehaviour;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zhaoyl on 4/30/20.
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OptionBehaviourAttribute {
    OptionBehaviour behaviour();

    ConfigValues dependentOn() default ConfigValues.Invalid;

    String realValue() default "";
}
