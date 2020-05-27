package com.ruoyi.framework.config;

/**
 * Created by zhaoyl on 5/6/20.
 */
public enum  OptionBehaviour {
    /**
     * value is a comma separated string array - for List of String
     */
    CommaSeparatedStringArray,
    /**
     * value is dependent in another value
     */
    ValueDependent,
    /**
     * value is a comma separated version array - for hashset of versions
     */
    CommaSeparatedIntegerArray;

    public int getValue() {
        return this.ordinal();
    }

    public static OptionBehaviour forValue(int value) {
        return values()[value];
    }
}
