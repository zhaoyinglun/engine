package com.ruoyi.project.system.controller;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.system.domain.api.DashboardTracksAirplane;
import com.ruoyi.project.system.domain.api.DashboardTracksArea;
import com.ruoyi.project.system.domain.api.DashboardTracksPlane;
import com.ruoyi.project.system.domain.vo.AirplaneEntityVo;
import com.ruoyi.project.system.domain.vo.RectangleVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by zhaoyl on 5/7/20.
 */
@Api("航迹信息管理")
@RestController
@RequestMapping("/dashboard/tracks")
public class HkAirtracksController {



    @ApiOperation(value = "查询区域范围内的飞机轨迹", response = DashboardTracksArea.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "locationA" , value = "A地经纬度" , dataType = "Point"),
            @ApiImplicitParam(name = "locationB" , value = "B地经纬度" , dataType = "Point"),
            @ApiImplicitParam(name = "locationC" , value = "C地经纬度" , dataType = "Point"),
            @ApiImplicitParam(name = "locationD" , value = "D地经纬度" , dataType = "Point")
    })
    @PreAuthorize("@ss.hasPermi('dashboard:tracks:area')")
    @PostMapping("/area")
    public AjaxResult getTrackInfo(@Validated @RequestBody RectangleVo rectangle){
        System.out.println(rectangle);

        return AjaxResult.success();
    }

    @ApiOperation(value = "按飞机查询的飞机轨迹列表" ,response = DashboardTracksPlane.class)
    @GetMapping("/airplane")
    @ApiImplicitParam(name = "planeId", value = "飞机ID", dataType = "Long")
    public AjaxResult getTrackListInfoByAirlaneId(
            @RequestParam Long id
    ){

        return AjaxResult.success();
    }

    @ApiOperation(value = "按飞机/航班号和时间查询的飞机轨迹" , response = DashboardTracksAirplane.class)
    @PostMapping("/tracksbyplaneid")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "planeID" , value = "飞机ID" , dataType = "String"),
            @ApiImplicitParam(name = "flightIdent" , value = "航班号" , dataType = "String"),
            @ApiImplicitParam(name = "date" , value = "时间;yyyy-MM-DD" , dataType = "String")
    })
    public AjaxResult getTracksInfoByPlaneid(
            @RequestBody Map<String , String> message
    ){
        return AjaxResult.success();
    }

//    @ApiOperation("获得城市内飞机信息")
//    @GetMapping("/cityplane")
//    @ApiImplicitParam(name = "area" , value = "区域" , dataType = "List")
//    public AjaxResult getCityPlaneInfo(){
//        List<PlaneEntity> planeEntities = new ArrayList<>();
//        return AjaxResult.success(planeEntities);
//    }
//
//    @ApiOperation("获得城市内机场信息")
//    @GetMapping("/cityairport")
//    @ApiImplicitParam(name = "area" , value = "区域" , dataType = "List")
//    public AjaxResult getCityAirportInfos(){
//        List<HkAirport> planeEntities = new ArrayList<>();
//        return AjaxResult.success(planeEntities);
//    }
//
//    @ApiOperation("按机场查询范围内的飞机信息")
//    @GetMapping("/airportplane")
//    @ApiImplicitParam(name = "area" , value = "区域" , dataType = "List")
//    public AjaxResult getAirportPlaneInfo(List<String> area){
//        List<PlaneEntity> planeEntities = new ArrayList<>();
//        return AjaxResult.success(planeEntities);
//    }
//
//    @ApiOperation("按航班号查询航班信息")
//    @GetMapping("/flightbyid")
//    @ApiImplicitParam(name = "flight" , value = "航班" , dataType = "HkFlight")
//    public AjaxResult getFlightInfoByid(HkFlight area){
//        FlightDetails flightInfo = new FlightDetails();
//        return AjaxResult.success(flightInfo);
//    }
//

//
//    @ApiOperation("按飞机/航班号和时间查询的飞机轨迹")
//    @GetMapping("/tracksbyplaneid")
//    @ApiImplicitParam(name = "flight" , value = "flight" , dataType = "Map")
//    public AjaxResult getTracksInfoByPlaneid(Map<String , String> message){
//        PlaneEntity planeEntity1 = new PlaneEntity();
//        return AjaxResult.success(planeEntity1);
//    }
//
//    @ApiOperation("查询机场航班")
//    @GetMapping("/flightsbyairportid")
//    @ApiImplicitParam(name = "flight" , value = "flight" , dataType = "HkAirport")
//    public AjaxResult getFlightInfoByPortid(HkAirport planeEntity){
//        AirPortFlight planeEntity1 = new AirPortFlight();
//        return AjaxResult.success(planeEntity1);
//    }




}
