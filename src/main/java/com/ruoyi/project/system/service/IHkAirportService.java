package com.ruoyi.project.system.service;

import java.util.List;
import com.ruoyi.project.system.domain.HkAirport;

/**
 * 机场Service接口
 * 
 * @author ruoyi
 * @date 2020-04-24
 */
public interface IHkAirportService 
{
    /**
     * 查询机场
     * 
     * @param id 机场ID
     * @return 机场
     */
    public HkAirport selectHkAirportById(Long id);

    /**
     * 查询机场列表
     * 
     * @param hkAirport 机场
     * @return 机场集合
     */
    public List<HkAirport> selectHkAirportList(HkAirport hkAirport);

    /**
     * 查询机场列表
     *
     * @param hkAirport 机场
     * @return 机场集合
     */
    public List<HkAirport> selectHkAirportListByDistance(HkAirport hkAirport);

    /**
     * 新增机场
     * 
     * @param hkAirport 机场
     * @return 结果
     */
    public int insertHkAirport(HkAirport hkAirport);

    /**
     * 修改机场
     * 
     * @param hkAirport 机场
     * @return 结果
     */
    public int updateHkAirport(HkAirport hkAirport);

    /**
     * 批量删除机场
     * 
     * @param ids 需要删除的机场ID
     * @return 结果
     */
    public int deleteHkAirportByIds(Long[] ids);

    /**
     * 删除机场信息
     * 
     * @param id 机场ID
     * @return 结果
     */
    public int deleteHkAirportById(Long id);
}
