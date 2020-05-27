package com.ruoyi.project.system.mapper;

import java.util.List;
import com.ruoyi.project.system.domain.HkAirline;

/**
 * 航空公司Mapper接口
 * 
 * @author ruoyi
 * @date 2020-04-24
 */
public interface HkAirlineMapper 
{
    /**
     * 查询航空公司
     * 
     * @param id 航空公司ID
     * @return 航空公司
     */
    public HkAirline selectHkAirlineById(Long id);

    /**
     * 查询航空公司列表
     * 
     * @param hkAirline 航空公司
     * @return 航空公司集合
     */
    public List<HkAirline> selectHkAirlineList(HkAirline hkAirline);

    /**
     * 新增航空公司
     * 
     * @param hkAirline 航空公司
     * @return 结果
     */
    public int insertHkAirline(HkAirline hkAirline);

    /**
     * 修改航空公司
     * 
     * @param hkAirline 航空公司
     * @return 结果
     */
    public int updateHkAirline(HkAirline hkAirline);

    /**
     * 删除航空公司
     * 
     * @param id 航空公司ID
     * @return 结果
     */
    public int deleteHkAirlineById(Long id);

    /**
     * 批量删除航空公司
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHkAirlineByIds(Long[] ids);
}
