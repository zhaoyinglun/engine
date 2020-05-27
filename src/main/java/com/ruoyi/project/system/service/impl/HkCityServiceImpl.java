package com.ruoyi.project.system.service.impl;

import java.util.List;

import com.ruoyi.project.system.domain.HkCity;
import com.ruoyi.project.system.mapper.HkCityMapper;
import com.ruoyi.project.system.service.IHkCityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 城市坐标功能Service业务层处理
 * 
 * @author zhaoyl
 * @date 2020-05-15
 */
@Service
public class HkCityServiceImpl implements IHkCityService
{
    @Autowired
    private HkCityMapper hkCityMapper;

    /**
     * 查询城市坐标功能
     * 
     * @param id 城市坐标功能ID
     * @return 城市坐标功能
     */
    @Override
    public HkCity selectHkCityById(Long id)
    {
        return hkCityMapper.selectHkCityById(id);
    }

    /**
     * 查询城市坐标功能列表
     * 
     * @param hkCity 城市坐标功能
     * @return 城市坐标功能
     */
    @Override
    public List<HkCity> selectHkCityList(HkCity hkCity)
    {
        return hkCityMapper.selectHkCityList(hkCity);
    }

    /**
     * 新增城市坐标功能
     * 
     * @param hkCity 城市坐标功能
     * @return 结果
     */
    @Override
    public int insertHkCity(HkCity hkCity)
    {
        return hkCityMapper.insertHkCity(hkCity);
    }

    /**
     * 修改城市坐标功能
     * 
     * @param hkCity 城市坐标功能
     * @return 结果
     */
    @Override
    public int updateHkCity(HkCity hkCity)
    {
        return hkCityMapper.updateHkCity(hkCity);
    }

    /**
     * 批量删除城市坐标功能
     * 
     * @param ids 需要删除的城市坐标功能ID
     * @return 结果
     */
    @Override
    public int deleteHkCityByIds(Long[] ids)
    {
        return hkCityMapper.deleteHkCityByIds(ids);
    }

    /**
     * 删除城市坐标功能信息
     * 
     * @param id 城市坐标功能ID
     * @return 结果
     */
    @Override
    public int deleteHkCityById(Long id)
    {
        return hkCityMapper.deleteHkCityById(id);
    }
}
