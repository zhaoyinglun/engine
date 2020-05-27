package com.ruoyi.project.system.mapper;

import java.util.List;
import com.ruoyi.project.system.domain.HkFlight;

/**
 * 航班Mapper接口
 * 
 * @author ruoyi
 * @date 2020-04-24
 */
public interface HkFlightMapper 
{
    /**
     * 查询航班
     * 
     * @param id 航班ID
     * @return 航班
     */
    public HkFlight selectHkFlightById(Long id);

    /**
     * 查询航班列表
     * 
     * @param hkFlight 航班
     * @return 航班集合
     */
    public List<HkFlight> selectHkFlightList(HkFlight hkFlight);

    /**
     * 新增航班
     * 
     * @param hkFlight 航班
     * @return 结果
     */
    public int insertHkFlight(HkFlight hkFlight);

    /**
     * 修改航班
     * 
     * @param hkFlight 航班
     * @return 结果
     */
    public int updateHkFlight(HkFlight hkFlight);

    /**
     * 删除航班
     * 
     * @param id 航班ID
     * @return 结果
     */
    public int deleteHkFlightById(Long id);

    /**
     * 批量删除航班
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHkFlightByIds(Long[] ids);
}
