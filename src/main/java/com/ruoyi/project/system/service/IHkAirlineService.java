package com.ruoyi.project.system.service;

import java.util.List;
import com.ruoyi.project.system.domain.HkAirline;

/**
 * 航空公司Service接口
 * 
 * @author ruoyi
 * @date 2020-04-22
 */
public interface IHkAirlineService 
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
     * 批量删除航空公司
     * 
     * @param ids 需要删除的航空公司ID
     * @return 结果
     */
    public int deleteHkAirlineByIds(Long[] ids);

    /**
     * 删除航空公司信息
     * 
     * @param id 航空公司ID
     * @return 结果
     */
    public int deleteHkAirlineById(Long id);
}
