package com.ruoyi.project.system.domain.enums;

import org.apache.hadoop.hbase.exceptions.IllegalArgumentIOException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by zhaoyl on 5/22/20.
 */
public class EnumTypeHandler<E extends Enum > extends BaseTypeHandler<E> {

    private Class<E> clazz;
    private E [] enums;

    public EnumTypeHandler(Class<E> clazz){
        if (clazz == null){
            throw new IllegalArgumentException("To be changed class cannot be null !"
                    + "\n转换对象不得为空!" );
        }

        this.clazz = clazz;
        this.enums = clazz.getEnumConstants();

        if (enums == null){
            throw new IllegalArgumentException(clazz.getSimpleName() + clazz.getName() + "is not a Enum type"
                    + "\n" + clazz.getSimpleName() + "不是枚举类型!");
        }
    }


    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, E e, JdbcType jdbcType) throws SQLException {
        preparedStatement.setObject(i,(String)e.name(),jdbcType.TYPE_CODE);
    }

    @Override
    public E getNullableResult(ResultSet resultSet, String s) throws SQLException {
        Integer index = resultSet.getInt(s);
        if (index == null){
            return null;
        }else {
            return locateEnum(index);
        }
    }

    private E locateEnum(int value){
        for (E e : enums){
            if (e.ordinal() == (value)){
                return e;
            }
        }
        throw new IllegalArgumentException("未知的枚举类型：" + value + ",请核对" + clazz.getSimpleName());
    }

    @Override
    public E getNullableResult(ResultSet resultSet, int i) throws SQLException {
        int index = resultSet.getInt(i);
        if (resultSet.wasNull()){
            return null;
        }else {
            return locateEnum(index);
        }
    }

    @Override
    public E getNullableResult(CallableStatement callableStatement, int i) throws SQLException {

        int index = callableStatement.getInt(i);
        if (callableStatement.wasNull()){
            return null;
        }else {
            return locateEnum(index);
        }
    }
}
