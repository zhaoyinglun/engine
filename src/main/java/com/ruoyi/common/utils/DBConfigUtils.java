package com.ruoyi.common.utils;

import com.ruoyi.SpringContextUtil;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.framework.config.ConfigValues;
import com.ruoyi.framework.config.MyBatisConfig;
import com.ruoyi.framework.manager.annotation.IsCity;
import com.ruoyi.framework.manager.annotation.OptionBehaviourAttribute;
import com.ruoyi.framework.manager.annotation.Reloadable;
import com.ruoyi.project.system.domain.HkOption;
import com.ruoyi.project.system.domain.vo.Point;
import com.ruoyi.project.system.mapper.HkOptionMapper;
import com.ruoyi.project.system.service.IHkOptionService;
import com.ruoyi.project.system.service.impl.HkOptionServiceImpl;
import org.apache.commons.lang.*;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by zhaoyl on 5/6/20.
 */
@Component
public class DBConfigUtils  {


    public void refreshOption() throws IOException {
        SqlSessionFactory sqlSessionFactory = SpringUtils.getBean("sqlSessionFactory");
        SqlSession sqlSession = sqlSessionFactory.openSession();
        HkOptionMapper hkOptionMapper = sqlSession.getMapper(HkOptionMapper.class);
        List<HkOption> list = hkOptionMapper.selectHkOptionList(null);
        for (HkOption option : list){
            try {
                if (isReloadable(option.getKey())){
                    updateOption(option);
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<ConfigValues> getAllCitys(){
        List<ConfigValues> configValuesList = new ArrayList<>();
        for (ConfigValues configValues : ConfigValues.values()){
            if (isCity(configValues)){
                configValuesList.add(configValues);
            }
        }
        return configValuesList;
    }


    private static DBConfigUtils instance = new DBConfigUtils() ;
    private static final Logger LOGGER = LoggerFactory.getLogger(DBConfigUtils.class);
    private static final Map<String, Object> _policyOptionCache = new HashMap<>();// Map<name, value>
    private static List<HkOption> optionList;


    public void setOptionList() {
        SqlSessionFactory sqlSessionFactory = SpringUtils.getBean("sqlSessionFactory");
        SqlSession sqlSession = sqlSessionFactory.openSession();
        HkOptionMapper hkOptionMapper = sqlSession.getMapper(HkOptionMapper.class);
        List<HkOption> optionLists = hkOptionMapper.selectHkOptionList(null);
        if (optionLists == null){
            return;
        }
        if (optionList != null){
            return;
        }
        optionList = optionLists;
        refreshCache();
    }

    private DBConfigUtils(){
    }


    public static DBConfigUtils getInstance(){
        return new DBConfigUtils();
    }

    private static void refreshCache(){
        for (HkOption option : optionList){
            updateOption(option);
        }
    }

    private static Object getValue(HkOption option){
        Object result = option.getValue();
        EnumValueUtils valueUtils = EnumValueUtils.parseEnumValue(option.getKey());
        if (valueUtils != null){
            result = parseValue(option , valueUtils);

            final OptionBehaviourAttribute optionBehaviour = valueUtils.getOptionBehaviour();

            if (optionBehaviour != null) {
                Object value;
                switch (optionBehaviour.behaviour()) {
                    // split string by comma for List<string> constructor
                    case CommaSeparatedStringArray:
                        result = Arrays.asList(((String) result).split("[,]", -1));
                        break;
                    case ValueDependent:
                        // get the config that this value depends on
                        if ((value = instance._policyOptionCache.get(optionBehaviour.dependentOn().toString())) != null) {
                            // its value is this value's prefix
                            String prefix = value.toString();
                            // combine the prefix with the 'real value'
                            if ((value = instance._policyOptionCache.get(String.format("%1$s%2$s", prefix, optionBehaviour.realValue()))) != null) {
                                // get value of the wanted config - assuming default!!
                                result = value.toString();
                            }
                        }
                        break;
                    case CommaSeparatedIntegerArray:
                        HashSet<Integer> versions = new HashSet<>();
                        for (String ver : result.toString().split("[,]", -1)) {
                            try {
                                versions.add(new Integer(ver));
                            } catch (Exception e) {
                                LOGGER.error("Could not parse integer '{}' for config value '{}'", ver, option.getKey());
                            }
                        }
                        result = versions;
                        break;
                }
            }
        }
        return result;
    }

    /**
     * 读取ConfigValues中对应的项, 并转换其值
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static Object parseValue(HkOption option, EnumValueUtils enumValue) {
        String value = option.getValue();
        if (value == null) {
            value = enumValue.getDefaultValue();
        }
        Class fieldType = enumValue.getFieldType();
        try {
            if (fieldType == Integer.class) {// 整型
                return Integer.parseInt(value);
            } else if (fieldType == Long.class) {// 长整型
                return Long.parseLong(value);
            } else if (fieldType == Boolean.class) {// 布尔型
                return Boolean.parseBoolean(value);
            } else if (fieldType == Date.class) {// 日期类型
                return new SimpleDateFormat("Y-M-d k:m:s").parse(value);
            } else if (fieldType == Double.class) {// 双精度型
                return Double.parseDouble(value);
            } else if (fieldType.isEnum()) {// 枚举型
                return Enum.valueOf((Class<Enum>) fieldType, value.toUpperCase());
            }else if (fieldType == Point.class){
                return new Point(Double.parseDouble(StringUtils.split(value,",")[0]),Double.parseDouble(StringUtils.split(value,",")[1]));
            }else {// 其他
                return value;
            }
        } catch (Exception e) {
            LOGGER.error("Error parsing option '{}' value: {}", option.getKey(), e.getMessage());
            return null;
        }
    }

    private static void updateOption(HkOption option){
        instance._policyOptionCache.put(option.getKey(),getValue(option));
    }

    public static  <T> T getConfigValue(ConfigValues name) {
        T returnValue;
        Object value;
        if ((value = instance._policyOptionCache.get(name.toString())) != null) {
            returnValue = (T) value;
        } else {
            HkOption option = new HkOption();
            option.setKey(name.toString());
            option.setValue(null);
            returnValue = (T) getValue(option);
            instance._policyOptionCache.put(name.toString(), returnValue);
        }
        return returnValue;
    }

    private static boolean isCity(ConfigValues configValues){
        try {
            return ConfigValues.class.getField(configValues.name()).isAnnotationPresent(IsCity.class);
        }catch (Exception e){
            return false;
        }
    }





    private static boolean isReloadable(String optionName) throws NoSuchFieldException {
        try {
            return ConfigValues.class.getField(optionName).isAnnotationPresent(Reloadable.class);
        }catch (NoSuchFieldException e){
            return false;
        }
    }
}
