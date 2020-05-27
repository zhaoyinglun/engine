package com.ruoyi.project.system.controller;

import cn.hutool.core.util.RandomUtil;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.system.domain.api.AirportHeatMap;
import com.ruoyi.project.system.domain.api.CityHeatMap;
import com.ruoyi.project.system.domain.api.DashboardBaseLocation;
import com.ruoyi.project.system.domain.vo.DefaultConfigValueVo;
import com.ruoyi.project.system.domain.vo.Point;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by zhaoyl on 5/11/20.
 */
@Api("热力图的基础操作接口")
@RestController
@RequestMapping("/heatmap/base")
public class HkHeatMapController {
    @ApiOperation(value = "查询所有省会范围内的飞机数量（5秒调用一次）", response = CityHeatMap.class)
    @GetMapping("/city")
    public AjaxResult cityPlaneNo()
    {
        return AjaxResult.success();
    }

    @ApiOperation(value = "查询所有机场范围内的飞机数量（5秒调用一次）", response = AirportHeatMap.class)
    @GetMapping("/airport")
    public AjaxResult airportPlaneNo()
    {
        return AjaxResult.success();
    }

    @ApiOperation(value = "按时间查询所有省会范围内的飞机数量", response = CityHeatMap.class)
    @PostMapping("/citybytime")
    @ApiImplicitParam(value = "date" , name = "时间" , dataType = "Date")
    public AjaxResult cityPlaneNoByTime(
            @RequestBody Date date
            )
    {
        return AjaxResult.success();
    }

    @ApiOperation(value = "按时间查询所有机场范围内的飞机数量", response = AirportHeatMap.class)
    @PostMapping("/airportbytime")
    @ApiImplicitParam(value = "date" , name = "时间" , dataType = "Date")
    public AjaxResult airportPlaneNoByTime(
            @RequestBody Date date
    )
    {
        return AjaxResult.success();
    }
}
