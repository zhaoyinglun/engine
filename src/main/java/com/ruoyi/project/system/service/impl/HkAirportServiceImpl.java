package com.ruoyi.project.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.mapper.HkAirportMapper;
import com.ruoyi.project.system.domain.HkAirport;
import com.ruoyi.project.system.service.IHkAirportService;

/**
 * 机场Service业务层处理
 * 
 * @author ruoyi
 * @date 2020-04-24
 */
@Service
public class HkAirportServiceImpl implements IHkAirportService 
{
    @Autowired
    private HkAirportMapper hkAirportMapper;

    /**
     * 查询机场
     * 
     * @param id 机场ID
     * @return 机场
     */
    @Override
    public HkAirport selectHkAirportById(Long id)
    {
        return hkAirportMapper.selectHkAirportById(id);
    }

    /**
     * 查询机场列表
     * 
     * @param hkAirport 机场
     * @return 机场
     */
    @Override
    public List<HkAirport> selectHkAirportList(HkAirport hkAirport)
    {
        return hkAirportMapper.selectHkAirportList(hkAirport);
    }

    /**
     * 经纬度查询机场列表
     *
     * @param hkAirport 机场
     * @return 机场
     */
    @Override
    public List<HkAirport> selectHkAirportListByDistance(HkAirport hkAirport) {
        return hkAirportMapper.selectHkAirportByDistance(hkAirport);
    }

    /**
     * 新增机场
     * 
     * @param hkAirport 机场
     * @return 结果
     */
    @Override
    public int insertHkAirport(HkAirport hkAirport)
    {
        return hkAirportMapper.insertHkAirport(hkAirport);
    }

    /**
     * 修改机场
     * 
     * @param hkAirport 机场
     * @return 结果
     */
    @Override
    public int updateHkAirport(HkAirport hkAirport)
    {
        return hkAirportMapper.updateHkAirport(hkAirport);
    }

    /**
     * 批量删除机场
     * 
     * @param ids 需要删除的机场ID
     * @return 结果
     */
    @Override
    public int deleteHkAirportByIds(Long[] ids)
    {
        return hkAirportMapper.deleteHkAirportByIds(ids);
    }

    /**
     * 删除机场信息
     * 
     * @param id 机场ID
     * @return 结果
     */
    @Override
    public int deleteHkAirportById(Long id)
    {
        return hkAirportMapper.deleteHkAirportById(id);
    }
}
