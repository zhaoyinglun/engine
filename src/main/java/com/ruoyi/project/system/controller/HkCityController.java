package com.ruoyi.project.system.controller;

import java.util.Arrays;
import java.util.List;

import com.ruoyi.common.utils.DBConfigUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.framework.config.ConfigValues;
import com.ruoyi.project.system.domain.HkCity;
import com.ruoyi.project.system.mapper.HkCityMapper;
import com.ruoyi.project.system.service.IHkCityService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 城市坐标功能Controller
 * 
 * @author zhaoyl
 * @date 2020-05-15
 */
@RestController
@RequestMapping("/system/city")
public class HkCityController extends BaseController
{
    @Autowired
    private IHkCityService hkCityService;

    /**
     * 查询城市坐标功能列表
     */
    @PreAuthorize("@ss.hasPermi('system:city:list')")
    @GetMapping("/list")
    public TableDataInfo list(HkCity hkCity)
    {
        startPage();
        List<HkCity> list = hkCityService.selectHkCityList(hkCity);
        return getDataTable(list);
    }

    /**
     * 导出城市坐标功能列表
     */
    @PreAuthorize("@ss.hasPermi('system:city:export')")
    @Log(title = "城市坐标功能", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(HkCity hkCity)
    {
        List<HkCity> list = hkCityService.selectHkCityList(hkCity);
        ExcelUtil<HkCity> util = new ExcelUtil<HkCity>(HkCity.class);
        return util.exportExcel(list, "city");
    }

    /**
     * 获取城市坐标功能详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:city:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(hkCityService.selectHkCityById(id));
    }

    /**
     * 新增城市坐标功能
     */
    @PreAuthorize("@ss.hasPermi('system:city:add')")
    @Log(title = "城市坐标功能", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody HkCity hkCity)
    {
        if (ConfigValues.getNames().contains(hkCity.getName())){
            return AjaxResult.error("省会城市已存在，不允许修改！");
        }
        return toAjax(hkCityService.insertHkCity(hkCity));
    }

    /**
     * 修改城市坐标功能
     */
    @PreAuthorize("@ss.hasPermi('system:city:edit')")
    @Log(title = "城市坐标功能", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HkCity hkCity)
    {
        return toAjax(hkCityService.updateHkCity(hkCity));
    }

    /**
     * 删除城市坐标功能
     */
    @PreAuthorize("@ss.hasPermi('system:city:remove')")
    @Log(title = "城市坐标功能", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(hkCityService.deleteHkCityByIds(ids));
    }
}
