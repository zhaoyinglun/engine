package com.ruoyi.project.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.mapper.HkWeatherMapper;
import com.ruoyi.project.system.domain.HkWeather;
import com.ruoyi.project.system.service.IHkWeatherService;

/**
 * 天气Service业务层处理
 * 
 * @author ruoyi
 * @date 2020-05-25
 */
@Service
public class HkWeatherServiceImpl implements IHkWeatherService 
{
    @Autowired
    private HkWeatherMapper hkWeatherMapper;

    /**
     * 查询天气
     * 
     * @param id 天气ID
     * @return 天气
     */
    @Override
    public HkWeather selectHkWeatherById(Long id)
    {
        return hkWeatherMapper.selectHkWeatherById(id);
    }

    /**
     * 查询天气列表
     * 
     * @param hkWeather 天气
     * @return 天气
     */
    @Override
    public List<HkWeather> selectHkWeatherList(HkWeather hkWeather)
    {
        return hkWeatherMapper.selectHkWeatherList(hkWeather);
    }

    /**
     * 新增天气
     * 
     * @param hkWeather 天气
     * @return 结果
     */
    @Override
    public int insertHkWeather(HkWeather hkWeather)
    {
        return hkWeatherMapper.insertHkWeather(hkWeather);
    }

    /**
     * 修改天气
     * 
     * @param hkWeather 天气
     * @return 结果
     */
    @Override
    public int updateHkWeather(HkWeather hkWeather)
    {
        return hkWeatherMapper.updateHkWeather(hkWeather);
    }

    /**
     * 批量删除天气
     * 
     * @param ids 需要删除的天气ID
     * @return 结果
     */
    @Override
    public int deleteHkWeatherByIds(Long[] ids)
    {
        return hkWeatherMapper.deleteHkWeatherByIds(ids);
    }

    /**
     * 删除天气信息
     * 
     * @param id 天气ID
     * @return 结果
     */
    @Override
    public int deleteHkWeatherById(Long id)
    {
        return hkWeatherMapper.deleteHkWeatherById(id);
    }
}
