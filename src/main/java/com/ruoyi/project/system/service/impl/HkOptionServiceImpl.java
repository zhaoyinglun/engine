package com.ruoyi.project.system.service.impl;

import java.util.List;

import org.apache.hadoop.hbase.client.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.mapper.HkOptionMapper;
import com.ruoyi.project.system.domain.HkOption;
import com.ruoyi.project.system.service.IHkOptionService;

/**
 * 策略配置Service业务层处理
 * 
 * @author zhaoyl
 * @date 2020-05-06
 */
@Service
public class HkOptionServiceImpl implements IHkOptionService 
{

    private Connection connection ;

    public HkOptionServiceImpl(){}
    public HkOptionServiceImpl(Connection connection){
        this.connection = connection;
    }

    @Autowired
    private HkOptionMapper hkOptionMapper;

    /**
     * 查询策略配置
     * 
     * @param key 策略配置ID
     * @return 策略配置
     */
    @Override
    public HkOption selectHkOptionById(String key)
    {
        return hkOptionMapper.selectHkOptionById(key);
    }

    /**
     * 查询策略配置列表
     * 
     * @param hkOption 策略配置
     * @return 策略配置
     */
    @Override
    public List<HkOption> selectHkOptionList(HkOption hkOption)
    {
        return hkOptionMapper.selectHkOptionList(hkOption);
    }

    /**
     * 新增策略配置
     * 
     * @param hkOption 策略配置
     * @return 结果
     */
    @Override
    public int insertHkOption(HkOption hkOption)
    {
        return hkOptionMapper.insertHkOption(hkOption);
    }

    /**
     * 修改策略配置
     * 
     * @param hkOption 策略配置
     * @return 结果
     */
    @Override
    public int updateHkOption(HkOption hkOption)
    {
        return hkOptionMapper.updateHkOption(hkOption);
    }

    /**
     * 批量删除策略配置
     * 
     * @param keys 需要删除的策略配置ID
     * @return 结果
     */
    @Override
    public int deleteHkOptionByIds(String[] keys)
    {
        return hkOptionMapper.deleteHkOptionByIds(keys);
    }

    /**
     * 删除策略配置信息
     * 
     * @param key 策略配置ID
     * @return 结果
     */
    @Override
    public int deleteHkOptionById(String key)
    {
        return hkOptionMapper.deleteHkOptionById(key);
    }
}
