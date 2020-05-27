package com.ruoyi.project.system.controller;

import java.util.ArrayList;
import java.util.List;

import com.ruoyi.common.exception.BaseException;
import com.ruoyi.project.system.domain.api.SystemAirportArea;
import com.ruoyi.project.system.domain.vo.RectangleVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
import com.ruoyi.project.system.domain.HkAirport;
import com.ruoyi.project.system.service.IHkAirportService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 机场Controller
 * 
 * @author ruoyi
 * @date 2020-04-24
 */
@RestController
@RequestMapping("/system/airport")
@Api("机场信息管理")
public class HkAirportController extends BaseController
{
    @Autowired
    private IHkAirportService hkAirportService;

    /**
     * 查询机场列表
     */
    @PreAuthorize("@ss.hasPermi('system:airport:list')")
    @GetMapping("/list")
    @ApiOperation("查询机场列表")
    public TableDataInfo list(HkAirport hkAirport)
    {
        startPage();
        if (hkAirport.getDistance() != null ){
            if (hkAirport.getLatitude() != null && hkAirport.getLongitude() != null){
                List<HkAirport> list = hkAirportService.selectHkAirportListByDistance(hkAirport);
                return getDataTable(list);
            }else {
                throw new BaseException("请输入完整的经纬度！!");
            }
        }
        List<HkAirport> list = hkAirportService.selectHkAirportList(hkAirport);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('system:airport:list')")
    @GetMapping("/chineseairport")
    @ApiOperation("查询中国机场列表")
    public TableDataInfo listByTimeZone(){
        HkAirport hkAirport = new HkAirport();
        hkAirport.setTz(":Asia/Shanghai");
        List<HkAirport> list = hkAirportService.selectHkAirportList(hkAirport);
        return getDataTable(list);
    }


    /**
     * 导出机场列表
     */
    @ApiOperation("导出机场列表")
    @PreAuthorize("@ss.hasPermi('system:airport:export')")
    @Log(title = "机场", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(HkAirport hkAirport)
    {
        List<HkAirport> list = hkAirportService.selectHkAirportList(hkAirport);
        ExcelUtil<HkAirport> util = new ExcelUtil<HkAirport>(HkAirport.class);
        return util.exportExcel(list, "airport");
    }

    /**
     * 获取机场详细信息
     */
    @ApiOperation("获取机场详细信息")
    @PreAuthorize("@ss.hasPermi('system:airport:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(hkAirportService.selectHkAirportById(id));
    }

    /**
     * 新增机场
     */
    @ApiOperation("新增机场")
    @PreAuthorize("@ss.hasPermi('system:airport:add')")
    @Log(title = "机场", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody HkAirport hkAirport)
    {
        return toAjax(hkAirportService.insertHkAirport(hkAirport));
    }

    /**
     * 修改机场
     */
    @ApiOperation("修改机场")
    @PreAuthorize("@ss.hasPermi('system:airport:edit')")
    @Log(title = "机场", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HkAirport hkAirport)
    {
        return toAjax(hkAirportService.updateHkAirport(hkAirport));
    }

    /**
     * 删除机场
     */
    @ApiOperation("删除机场")
    @PreAuthorize("@ss.hasPermi('system:airport:remove')")
    @Log(title = "机场", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(hkAirportService.deleteHkAirportByIds(ids));
    }


    @ApiOperation(value = "查询区域范围内的机场信息" , response = SystemAirportArea.class)
    @PreAuthorize("@ss.hasPermi('system:airport:area')")
    @PostMapping(value = "/area")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "locationA" , value = "A地经纬度" , dataType = "Point"),
            @ApiImplicitParam(name = "locationB" , value = "B地经纬度" , dataType = "Point")
    })
    public AjaxResult areaQuery(
            @Validated @RequestBody RectangleVo rectangle
    ){
        List<HkAirport> airportList = hkAirportService.selectHkAirportList(null);
        List<HkAirport> innerAirports = new ArrayList<>();
        for (HkAirport airport : airportList){
            if (Math.max(rectangle.getA().getLatitude(),rectangle.getB().getLatitude()) > airport.getLatitude()
                    && airport.getLatitude() > Math.min(rectangle.getA().getLatitude(),rectangle.getB().getLatitude())){
                if (Math.max(rectangle.getA().getLongitude(),rectangle.getB().getLongitude()) > airport.getLongitude()
                        && airport.getLongitude() > Math.min(rectangle.getA().getLongitude(),rectangle.getB().getLongitude())){
                    innerAirports.add(airport);
                }
            }
        }

        return AjaxResult.success(innerAirports);
    }

}
