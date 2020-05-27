package com.ruoyi.project.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.mapper.HkFlightMapper;
import com.ruoyi.project.system.domain.HkFlight;
import com.ruoyi.project.system.service.IHkFlightService;

/**
 * 航班Service业务层处理
 * 
 * @author ruoyi
 * @date 2020-04-24
 */
@Service
public class HkFlightServiceImpl implements IHkFlightService 
{
    @Autowired
    private HkFlightMapper hkFlightMapper;

    /**
     * 查询航班
     * 
     * @param id 航班ID
     * @return 航班
     */
    @Override
    public HkFlight selectHkFlightById(Long id)
    {
        return hkFlightMapper.selectHkFlightById(id);
    }

    /**
     * 查询航班列表
     * 
     * @param hkFlight 航班
     * @return 航班
     */
    @Override
    public List<HkFlight> selectHkFlightList(HkFlight hkFlight)
    {
        return hkFlightMapper.selectHkFlightList(hkFlight);
    }

    /**
     * 新增航班
     * 
     * @param hkFlight 航班
     * @return 结果
     */
    @Override
    public int insertHkFlight(HkFlight hkFlight)
    {
        return hkFlightMapper.insertHkFlight(hkFlight);
    }

    /**
     * 修改航班
     * 
     * @param hkFlight 航班
     * @return 结果
     */
    @Override
    public int updateHkFlight(HkFlight hkFlight)
    {
        return hkFlightMapper.updateHkFlight(hkFlight);
    }

    /**
     * 批量删除航班
     * 
     * @param ids 需要删除的航班ID
     * @return 结果
     */
    @Override
    public int deleteHkFlightByIds(Long[] ids)
    {
        return hkFlightMapper.deleteHkFlightByIds(ids);
    }

    /**
     * 删除航班信息
     * 
     * @param id 航班ID
     * @return 结果
     */
    @Override
    public int deleteHkFlightById(Long id)
    {
        return hkFlightMapper.deleteHkFlightById(id);
    }
}
