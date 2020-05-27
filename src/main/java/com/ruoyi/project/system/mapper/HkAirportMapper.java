package com.ruoyi.project.system.mapper;

import java.util.List;

import com.ruoyi.project.system.domain.HkAirline;
import com.ruoyi.project.system.domain.HkAirport;
import com.ruoyi.project.system.domain.vo.AirplaneEntityVo;

/**
 * 机场Mapper接口
 * 
 * @author ruoyi
 * @date 2020-04-24
 */
public interface HkAirportMapper 
{
    /**
     * 查询机场
     * 
     * @param id 机场ID
     * @return 机场
     */
    public HkAirport selectHkAirportById(Long id);

    /**
     * 经纬度查询机场
     *
     * @param id 机场ID
     * @return 机场
     */
    public List<HkAirport> selectHkAirportByDistance(HkAirport hkAirport);

    /**
     * 查询机场列表
     * 
     * @param hkAirport 机场
     * @return 机场集合
     */
    public List<HkAirport> selectHkAirportList(HkAirport hkAirport);

    /**
     * 新增机场
     * 
     * @param hkAirport 机场
     * @return 结果
     */
    public int insertHkAirport(HkAirport hkAirport);

    public int insertHkAirplane(List<AirplaneEntityVo> list);
    public List<AirplaneEntityVo> selectPlane();

    /**
     * 修改机场
     * 
     * @param hkAirport 机场
     * @return 结果
     */
    public int updateHkAirport(HkAirport hkAirport);

    /**
     * 删除机场
     * 
     * @param id 机场ID
     * @return 结果
     */
    public int deleteHkAirportById(Long id);

    /**
     * 批量删除机场
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHkAirportByIds(Long[] ids);
}
