package com.ruoyi.project.system.controller;

import java.util.List;

import io.swagger.annotations.Api;
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
import com.ruoyi.project.system.domain.HkAirline;
import com.ruoyi.project.system.service.IHkAirlineService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 航空公司Controller
 * 
 * @author ruoyi
 * @date 2020-04-24
 */
@RestController
@RequestMapping("/system/airline")
@Api("航空公司信息管理")
@ApiOperation("航空公司信息管理")
public class HkAirlineController extends BaseController
{
    @Autowired
    private IHkAirlineService hkAirlineService;

    /**
     * 查询航空公司列表
     */
    @PreAuthorize("@ss.hasPermi('system:airline:list')")
    @GetMapping("/list")
    @ApiOperation("查询航空公司列表")
    public TableDataInfo list(HkAirline hkAirline)
    {
        startPage();
        List<HkAirline> list = hkAirlineService.selectHkAirlineList(hkAirline);
        return getDataTable(list);
    }

    /**
     * 导出航空公司列表
     */
    @ApiOperation("导出航空公司列表")
    @PreAuthorize("@ss.hasPermi('system:airline:export')")
    @Log(title = "航空公司", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(HkAirline hkAirline)
    {
        List<HkAirline> list = hkAirlineService.selectHkAirlineList(hkAirline);
        ExcelUtil<HkAirline> util = new ExcelUtil<HkAirline>(HkAirline.class);
        return util.exportExcel(list, "airline");
    }

    /**
     * 获取航空公司详细信息
     */
    @ApiOperation("获取航空公司详细信息")
    @PreAuthorize("@ss.hasPermi('system:airline:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(hkAirlineService.selectHkAirlineById(id));
    }

    /**
     * 新增航空公司
     */
    @ApiOperation("新增航空公司")
    @PreAuthorize("@ss.hasPermi('system:airline:add')")
    @Log(title = "航空公司", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody HkAirline hkAirline)
    {
        return toAjax(hkAirlineService.insertHkAirline(hkAirline));
    }

    /**
     * 修改航空公司
     */
    @ApiOperation("修改航空公司")
    @PreAuthorize("@ss.hasPermi('system:airline:edit')")
    @Log(title = "航空公司", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HkAirline hkAirline)
    {
        return toAjax(hkAirlineService.updateHkAirline(hkAirline));
    }

    /**
     * 删除航空公司
     */
    @ApiOperation("删除航空公司")
    @PreAuthorize("@ss.hasPermi('system:airline:remove')")
    @Log(title = "航空公司", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(hkAirlineService.deleteHkAirlineByIds(ids));
    }
}
