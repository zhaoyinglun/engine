package com.ruoyi.project.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.mapper.HkAirlineMapper;
import com.ruoyi.project.system.domain.HkAirline;
import com.ruoyi.project.system.service.IHkAirlineService;

/**
 * 航空公司Service业务层处理
 * 
 * @author ruoyi
 * @date 2020-04-22
 */
@Service
public class HkAirlineServiceImpl implements IHkAirlineService 
{
    @Autowired
    private HkAirlineMapper hkAirlineMapper;

    /**
     * 查询航空公司
     * 
     * @param id 航空公司ID
     * @return 航空公司
     */
    @Override
    public HkAirline selectHkAirlineById(Long id)
    {
        return hkAirlineMapper.selectHkAirlineById(id);
    }

    /**
     * 查询航空公司列表
     * 
     * @param hkAirline 航空公司
     * @return 航空公司
     */
    @Override
    public List<HkAirline> selectHkAirlineList(HkAirline hkAirline)
    {
        return hkAirlineMapper.selectHkAirlineList(hkAirline);
    }

    /**
     * 新增航空公司
     * 
     * @param hkAirline 航空公司
     * @return 结果
     */
    @Override
    public int insertHkAirline(HkAirline hkAirline)
    {
        return hkAirlineMapper.insertHkAirline(hkAirline);
    }

    /**
     * 修改航空公司
     * 
     * @param hkAirline 航空公司
     * @return 结果
     */
    @Override
    public int updateHkAirline(HkAirline hkAirline)
    {
        return hkAirlineMapper.updateHkAirline(hkAirline);
    }

    /**
     * 批量删除航空公司
     * 
     * @param ids 需要删除的航空公司ID
     * @return 结果
     */
    @Override
    public int deleteHkAirlineByIds(Long[] ids)
    {
        return hkAirlineMapper.deleteHkAirlineByIds(ids);
    }

    /**
     * 删除航空公司信息
     * 
     * @param id 航空公司ID
     * @return 结果
     */
    @Override
    public int deleteHkAirlineById(Long id)
    {
        return hkAirlineMapper.deleteHkAirlineById(id);
    }
}
