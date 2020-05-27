package com.ruoyi.project.system.mapper;

import com.ruoyi.project.system.domain.HkCity;

import java.util.List;

/**
 * 城市坐标功能Mapper接口
 * 
 * @author zhaoyl
 * @date 2020-05-15
 */
public interface HkCityMapper 
{
    /**
     * 查询城市坐标功能
     * 
     * @param id 城市坐标功能ID
     * @return 城市坐标功能
     */
    public HkCity selectHkCityById(Long id);

    /**
     * 查询城市坐标功能列表
     * 
     * @param hkCity 城市坐标功能
     * @return 城市坐标功能集合
     */
    public List<HkCity> selectHkCityList(HkCity hkCity);

    /**
     * 新增城市坐标功能
     * 
     * @param hkCity 城市坐标功能
     * @return 结果
     */
    public int insertHkCity(HkCity hkCity);

    /**
     * 修改城市坐标功能
     * 
     * @param hkCity 城市坐标功能
     * @return 结果
     */
    public int updateHkCity(HkCity hkCity);

    /**
     * 删除城市坐标功能
     * 
     * @param id 城市坐标功能ID
     * @return 结果
     */
    public int deleteHkCityById(Long id);

    /**
     * 批量删除城市坐标功能
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHkCityByIds(Long[] ids);
}
