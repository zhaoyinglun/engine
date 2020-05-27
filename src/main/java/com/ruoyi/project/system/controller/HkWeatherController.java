package com.ruoyi.project.system.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ruoyi.project.system.domain.HkCity;
import com.ruoyi.project.system.domain.api.SystemFlight;
import com.ruoyi.project.system.domain.api.SystemWeather;
import com.ruoyi.project.system.service.IHkCityService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
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
import com.ruoyi.project.system.domain.HkWeather;
import com.ruoyi.project.system.service.IHkWeatherService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 天气Controller
 * 
 * @author ruoyi
 * @date 2020-05-25
 */
@RestController
@RequestMapping("/system/weather")
public class HkWeatherController extends BaseController
{
    @Autowired
    private IHkWeatherService hkWeatherService;

    @Autowired
    private IHkCityService hkCityService;

    /**
     * 查询天气列表
     */
    @ApiOperation(value = "查询天气列表" , response = SystemWeather.class)
    @GetMapping("/list")
    @ApiImplicitParam(name = "hkWeather" , value = "天气实体" , type = "HkWeather")
    public TableDataInfo list(HkWeather hkWeather)
    {
        startPage();
        List<HkWeather> list = hkWeatherService.selectHkWeatherList(hkWeather);
        return getDataTable(list);
    }

    /**
     * 查询今日天气列表
     */
    @ApiOperation(value = "查询今日天气列表" , response = SystemWeather.class)
    @GetMapping("/todaylist")
    public TableDataInfo listToday()
    {
        startPage();
        HkWeather hkWeather = new HkWeather();
        hkWeather.setDate(new Date());
        List<HkWeather> list = hkWeatherService.selectHkWeatherList(hkWeather);
        List<HkWeather> newestList = getNewestCity(list);
        return getDataTable(newestList);
    }

    private List getNewestCity(List<HkWeather> list){
        for (HkWeather x : list){
            for (HkWeather y : list){
                if (x.getCityid() == y.getCityid()){
                    if (x.getDate().getTime() < y.getDate().getTime()){
                        list.remove(x);
                    }else {
                        list.remove(y);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 查询城市最新天气列表
     */
    @ApiOperation(value = "查询城市最新天气列表" , response = SystemWeather.class)
    @GetMapping("/newestlist")
    public TableDataInfo listNewest()
    {
        startPage();
        HkWeather hkWeather = new HkWeather();
        List<HkWeather> list = hkWeatherService.selectHkWeatherList(hkWeather);
        List<HkWeather> newestList = new ArrayList<>();
        List<HkCity> cities = hkCityService.selectHkCityList(new HkCity());
        for (HkCity city : cities){
            HkWeather cityWeather = new HkWeather();
            for (HkWeather weather : list){
                if (weather.getCityid() == city.getId()){
                    if (weather.getDate() == null){
                        cityWeather = weather;
                    } else if (cityWeather.getDate() != null && cityWeather.getDate().getTime()<weather.getDate().getTime()){
                        cityWeather = weather;
                    }
                }
            }
            newestList.add(cityWeather);
        }
        return getDataTable(list);
    }

    /**
     * 导出天气列表
     */
    @Log(title = "天气", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(HkWeather hkWeather)
    {
        List<HkWeather> list = hkWeatherService.selectHkWeatherList(hkWeather);
        ExcelUtil<HkWeather> util = new ExcelUtil<HkWeather>(HkWeather.class);
        return util.exportExcel(list, "weather");
    }

    /**
     * 获取天气详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(hkWeatherService.selectHkWeatherById(id));
    }

    /**
     * 新增天气
     */
    @Log(title = "天气", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody HkWeather hkWeather)
    {
        if (hkWeather.getCityid() == null || hkWeather.getCityid() == 0){
            if (!hkWeather.getCityName().isEmpty() && hkWeather.getCityName().equals("")){
                HkCity city = new HkCity();
                city.setName(hkWeather.getCityName());
                List<HkCity> cityList = hkCityService.selectHkCityList(city);
                if (cityList.size()<1){
                    return AjaxResult.error("当前城市不存在");
                }
                hkWeather.setCityid(cityList.get(0).getId());
            }
        }
        return toAjax(hkWeatherService.insertHkWeather(hkWeather));
    }

    /**
     * 新增今日随机天气
     */
    @ApiOperation(value = "新增今日随机天气" )
    @Log(title = "天气", businessType = BusinessType.INSERT)
    @PostMapping("/random")
    public AjaxResult addRondom()
    {
        List<HkCity> cities = hkCityService.selectHkCityList(new HkCity());
        for (HkCity city : cities){
            HkWeather weather = new HkWeather();
            weather.setCityid(city.getId());
            if (Math.random() < 0.5){
                weather.setWeather("晴");
            }else {
                weather.setWeather("雨");
            }
            weather.setLocation(city.getLocation());
            weather.setDate(new Date());
            hkWeatherService.insertHkWeather(weather);
        }
        return AjaxResult.success();
    }

    /**
     * 修改天气
     */
    @Log(title = "天气", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HkWeather hkWeather)
    {
        return toAjax(hkWeatherService.updateHkWeather(hkWeather));
    }

    /**
     * 删除天气
     */
    @Log(title = "天气", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(hkWeatherService.deleteHkWeatherByIds(ids));
    }
}
