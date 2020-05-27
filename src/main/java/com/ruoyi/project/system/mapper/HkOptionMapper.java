package com.ruoyi.project.system.mapper;

import java.util.List;
import com.ruoyi.project.system.domain.HkOption;

/**
 * 策略配置Mapper接口
 * 
 * @author zhaoyl
 * @date 2020-05-06
 */
public interface HkOptionMapper 
{
    /**
     * 查询策略配置
     * 
     * @param key 策略配置ID
     * @return 策略配置
     */
    public HkOption selectHkOptionById(String key);

    /**
     * 查询策略配置列表
     * 
     * @param hkOption 策略配置
     * @return 策略配置集合
     */
    public List<HkOption> selectHkOptionList(HkOption hkOption);

    /**
     * 新增策略配置
     * 
     * @param hkOption 策略配置
     * @return 结果
     */
    public int insertHkOption(HkOption hkOption);

    /**
     * 修改策略配置
     * 
     * @param hkOption 策略配置
     * @return 结果
     */
    public int updateHkOption(HkOption hkOption);

    /**
     * 删除策略配置
     * 
     * @param key 策略配置ID
     * @return 结果
     */
    public int deleteHkOptionById(String key);

    /**
     * 批量删除策略配置
     * 
     * @param keys 需要删除的数据ID
     * @return 结果
     */
    public int deleteHkOptionByIds(String[] keys);
}
