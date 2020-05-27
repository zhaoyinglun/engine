package com.ruoyi.project.system.mapper;

import java.util.List;
import com.ruoyi.project.system.domain.HkWeather;

/**
 * 天气Mapper接口
 * 
 * @author ruoyi
 * @date 2020-05-25
 */
public interface HkWeatherMapper 
{
    /**
     * 查询天气
     * 
     * @param id 天气ID
     * @return 天气
     */
    public HkWeather selectHkWeatherById(Long id);

    /**
     * 查询天气列表
     * 
     * @param hkWeather 天气
     * @return 天气集合
     */
    public List<HkWeather> selectHkWeatherList(HkWeather hkWeather);

    /**
     * 新增天气
     * 
     * @param hkWeather 天气
     * @return 结果
     */
    public int insertHkWeather(HkWeather hkWeather);

    /**
     * 修改天气
     * 
     * @param hkWeather 天气
     * @return 结果
     */
    public int updateHkWeather(HkWeather hkWeather);

    /**
     * 删除天气
     * 
     * @param id 天气ID
     * @return 结果
     */
    public int deleteHkWeatherById(Long id);

    /**
     * 批量删除天气
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHkWeatherByIds(Long[] ids);
}
