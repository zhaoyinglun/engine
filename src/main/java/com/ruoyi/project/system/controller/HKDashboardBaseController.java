package com.ruoyi.project.system.controller;

import cn.hutool.core.util.RandomUtil;
import com.ruoyi.common.utils.DBConfigUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.config.ConfigValues;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.system.domain.HkOption;
import com.ruoyi.project.system.domain.api.ApiDataExample;
import com.ruoyi.project.system.domain.api.DashboardBaseLocation;
import com.ruoyi.project.system.domain.api.DashboardBasePlane;
import com.ruoyi.project.system.domain.api.DashboardBaseWeather;
import com.ruoyi.project.system.domain.vo.DefaultConfigValueVo;
import com.ruoyi.project.system.domain.vo.Point;
import com.ruoyi.project.system.service.IHkOptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Api("大屏的基础操作接口")
@RestController
@RequestMapping("/dashboard/base")
public class HKDashboardBaseController extends BaseController {

    @Autowired
    private IHkOptionService optionService;

    @ApiOperation(value = "获取系统默认位置/缩放级别", response = DashboardBaseLocation.class)
    @GetMapping("/location")
    public AjaxResult location()
    {
        String loca = DBConfigUtils.getConfigValue(ConfigValues.MapDefaultLocation);
        Point point = new Point(Double.parseDouble(StringUtils.split(loca,",")[0]),Double.parseDouble(StringUtils.split(loca,",")[1]));
        int level = DBConfigUtils.getConfigValue(ConfigValues.ZoomLevel);

        return AjaxResult.success(new DefaultConfigValueVo(point,level));
    }

    @PreAuthorize("@ss.hasPermi('dashboard:base:location:update')")
    @ApiOperation(value = "设置统默认位置/缩放级别", response = ApiDataExample.class)
    @PostMapping("/location")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "location", value = "所在地经纬度", dataType = "Point"),
        @ApiImplicitParam(name = "level", value = "视角等级", dataType = "int")
    })
    public AjaxResult setLocation(
            @RequestBody DefaultConfigValueVo config
    ) throws IOException {
        optionService.updateHkOption(new HkOption("AirPortLocation",config.getPoint().toString()));
        optionService.updateHkOption(new HkOption("ZoomLevel",String.valueOf(config.getLevel())));
        DBConfigUtils.getInstance().refreshOption();
        return AjaxResult.success();
    }


    @ApiOperation(value = "查询天气信息", response = DashboardBaseWeather.class)
    @GetMapping("/weather")
    public AjaxResult getWeatherInfo(){

        return AjaxResult.success();
    }

    @ApiOperation(value = "查询所有飞机位置", response = DashboardBasePlane.class)
    @GetMapping("/plane")
    public AjaxResult getAllPlaneInfo(){


        return AjaxResult.success();
    }
}
