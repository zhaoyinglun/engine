package com.ruoyi.project.system.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.ruoyi.project.system.domain.HkAirport;
import com.ruoyi.project.system.domain.api.SystemAirportFlight;
import com.ruoyi.project.system.domain.api.SystemFlight;
import com.ruoyi.project.system.domain.enums.WeekEnum;
import com.ruoyi.project.system.domain.vo.AirportFlightVo;
import com.ruoyi.project.system.service.IHkAirportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.system.domain.HkFlight;
import com.ruoyi.project.system.service.IHkFlightService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 航班Controller
 * 
 * @author ruoyi
 * @date 2020-04-24
 */
@RestController
@RequestMapping("/system/flight")
@Api("航班信息管理")
public class HkFlightController extends BaseController
{
    @Autowired
    private IHkFlightService hkFlightService;

    @Autowired
    private IHkAirportService hkAirportService;

    /**
     * 查询航班列表
     */
    @ApiOperation(value = "按条件查询航班信息" , response = SystemFlight.class)
    @PreAuthorize("@ss.hasPermi('system:flight:list')")
    @GetMapping("/list")
    public TableDataInfo list(HkFlight hkFlight)
    {
        startPage();
        List<HkFlight> list = hkFlightService.selectHkFlightList(hkFlight);
//        for (HkFlight flight : list){
//            Date takeoff = flight.getTakeofftimesscheduled();
//            Date land = flight.getLandingtimesscheduled();
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(takeoff);
//            for (WeekEnum weekEnum : WeekEnum.values()){
//                if (weekEnum.ordinal() == (cal.get(Calendar.DAY_OF_WEEK )-1)){
//                    flight.setTakeOffWeek(weekEnum);
//                }
//            }
//            cal.setTime(land);
//            for (WeekEnum weekEnum : WeekEnum.values()){
//                if (weekEnum.ordinal() == (cal.get(Calendar.DAY_OF_WEEK )-1)){
//                    flight.setLandWeek(weekEnum);
//                }
//            }
//            flight.setUpdatedTime(flight.getLandingtimesscheduled());
//            hkFlightService.updateHkFlight(flight);
//        }
        return getDataTable(list);
    }

    /**
     * 导出航班列表
     */
    @ApiOperation("导出用户列表")
    @PreAuthorize("@ss.hasPermi('system:flight:export')")
    @Log(title = "航班", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(HkFlight hkFlight)
    {
        List<HkFlight> list = hkFlightService.selectHkFlightList(hkFlight);
        ExcelUtil<HkFlight> util = new ExcelUtil<HkFlight>(HkFlight.class);
        return util.exportExcel(list, "flight");
    }

    /**
     * 获取航班详细信息
     */
    @ApiOperation("获取航班详细信息")
    @PreAuthorize("@ss.hasPermi('system:flight:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(hkFlightService.selectHkFlightById(id));
    }

    /**
     * 新增航班
     */
    @ApiOperation("新增航班")
    @PreAuthorize("@ss.hasPermi('system:flight:add')")
    @Log(title = "航班", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody HkFlight hkFlight)
    {
        return toAjax(hkFlightService.insertHkFlight(hkFlight));
    }

    /**
     * 修改航班
     */
    @ApiOperation("修改航班")
    @PreAuthorize("@ss.hasPermi('system:flight:edit')")
    @Log(title = "航班", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HkFlight hkFlight)
    {
        return toAjax(hkFlightService.updateHkFlight(hkFlight));
    }

    /**
     * 删除航班
     */
    @ApiOperation("删除航班")
    @PreAuthorize("@ss.hasPermi('system:flight:remove')")
    @Log(title = "航班", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(hkFlightService.deleteHkFlightByIds(ids));
    }

    @ApiOperation(value = "查询机场航班" , response = SystemAirportFlight.class)
    @GetMapping("/flightsbyairportid")
    @ApiImplicitParam(name = "airport" , value = "机场" , dataType = "HkAirport")
    public AjaxResult getFlightInfoByPortid(HkAirport airportEntity){
        HkAirport airport = hkAirportService.selectHkAirportById(airportEntity.getId());
        HkFlight flight = new HkFlight();
        flight.setOrigin(airport.getIcao());
        List<HkFlight> flightList = hkFlightService.selectHkFlightList(flight);
        AirportFlightVo airportFlightVo = new AirportFlightVo();
        airportFlightVo.setAirport(airport.getId());
        airportFlightVo.setFlights(flightList);
        return AjaxResult.success(airportFlightVo);
    }
}
