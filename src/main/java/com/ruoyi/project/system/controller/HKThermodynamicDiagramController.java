package com.ruoyi.project.system.controller;

import cn.hutool.core.date.DateUtil;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.system.domain.api.DashboardTdAirplaneAirport;
import com.ruoyi.project.system.domain.api.DashboardTdAirplaneProvincial;
import com.ruoyi.project.system.domain.vo.AddressAirplaneCountVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Api("热力图接口")
@RestController
@RequestMapping("/dashboard/td")
public class HKThermodynamicDiagramController extends BaseController {

    @PreAuthorize("@ss.hasPermi('dashboard:td:airplane:provincial')")
    @ApiOperation(value = "查询省会范围内的飞机数量", response = DashboardTdAirplaneProvincial.class)
    @ApiImplicitParam(name = "time", value = "时间", dataType = "Date", paramType = "query")
    @GetMapping("/airplane/provincial")
    public AjaxResult provincialAirplane() {
        String queryTime = ServletUtils.getParameter("time");

        if(StringUtils.isEmpty(queryTime)) {
            System.out.println("没有关于时间的query string");
        } else {
            System.out.println("有关于时间的query string");
            Date time = DateUtil.parse(queryTime);
            System.out.println(time);
        }

        ArrayList<AddressAirplaneCountVo> data= new ArrayList<>();

        return AjaxResult.success(data);
    }


    @PreAuthorize("@ss.hasPermi('dashboard:td:airplane:airport')")
    @ApiOperation(value = "查询所有机场范围内的飞机数量", response = DashboardTdAirplaneAirport.class )
    @ApiImplicitParam(name = "time", value = "时间", dataType = "Date", paramType = "query")
    @GetMapping("/airplane/airport")
    public AjaxResult airportAirplane() {
        String queryTime = ServletUtils.getParameter("time");

        if(StringUtils.isEmpty(queryTime)) {
            System.out.println("没有关于时间的query string");
        } else {
            System.out.println("有关于时间的query string");
            Date time = DateUtil.parse(queryTime);
            System.out.println(time);
        }

        Map<String,Object> data = new HashMap<>();
        data.put("radius", 50);
        data.put("list", new ArrayList<AddressAirplaneCountVo>());
        return AjaxResult.success(data);
    }

}
