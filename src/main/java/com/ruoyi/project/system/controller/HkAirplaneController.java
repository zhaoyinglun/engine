package com.ruoyi.project.system.controller;

import cn.hutool.core.util.RandomUtil;
import com.ruoyi.common.utils.ADSBTool;
import com.ruoyi.common.utils.DBConfigUtils;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.HkjsUtils;
import com.ruoyi.framework.config.ConfigValues;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.system.domain.HkAirport;
import com.ruoyi.project.system.domain.HkFlight;
import com.ruoyi.project.system.domain.api.SystemAirPlaneFlight;
import com.ruoyi.project.system.domain.api.SystemAirplaneAirport;
import com.ruoyi.project.system.domain.api.SystemAirplaneArea;
import com.ruoyi.project.system.domain.vo.AirplaneEntityVo;
import com.ruoyi.project.system.domain.vo.CityHeatVo;
import com.ruoyi.project.system.domain.vo.Point;
import com.ruoyi.project.system.domain.vo.RectangleVo;
import com.ruoyi.project.system.mapper.HkAirportMapper;
import com.ruoyi.project.system.service.IHkAirplaneService;
import com.ruoyi.project.system.service.IHkAirportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by root on 4/26/20.
 */
@RestController
@RequestMapping("/system/airplane")
@Api("飞机信息管理")
@ApiOperation("飞机信息管理")
public class HkAirplaneController extends BaseController {

    @Autowired
    IHkAirplaneService planeService;

    @PreAuthorize("@ss.hasPermi('system:airplane:area')")
    @ApiOperation(value = "查询区域范围内的飞机信息", response = SystemAirplaneArea.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rectangleVo" , value = "两地经纬度" , dataType = "RectangleVo"),
            @ApiImplicitParam(name = "time" , value = "时间" , dataType = "Long")
    })
    @PostMapping("/area")
    public AjaxResult getPlaneInfo(
            @RequestBody RectangleVo rectangleVo,
            @Param("time") Long time
            ){
        List<AirplaneEntityVo> list = null;
        double slon, slat, elon, elat;
        if(rectangleVo == null || rectangleVo.getA() == null || rectangleVo.getB() == null){
            // TODO 获取默認(中国)的经纬度范围
            slat = 3.5;
            slon=73.0;
            elat=53.33;
            elon=134.1;
        } else {
            slat = rectangleVo.getA().getLatitude();
            slon = rectangleVo.getA().getLongitude();
            elat = rectangleVo.getB().getLatitude();
            elon = rectangleVo.getB().getLongitude();
        }

        if(time != null){
            list = planeService.findHisPlaneByArea(slon, slat, elon, elat, time);
        }else{
            list = planeService.findPlaneByArea(slon, slat, elon, elat);
        }
        return AjaxResult.success(list);
    }

    @PreAuthorize("@ss.hasPermi('system:airplane:airport')")
    @ApiOperation(value = "按机场查询范围内的飞机信息", response = SystemAirplaneAirport.class)
    @GetMapping("/airport")
    @ApiImplicitParam(name = "location", value = "所在地经纬度", dataType = "Point")
    public AjaxResult searchAirplaneByAirPort(
            @RequestBody Point point
    ){

        return AjaxResult.success();
    }

    /**
     * 获取机场详细信息
     */
    @ApiOperation("获取飞机航迹列表")
    @PreAuthorize("@ss.hasPermi('system:airplane:track')")
    @GetMapping(value = "/{icao}")
    public AjaxResult getInfo(@PathVariable("icao") Long icao,
                              @Param("time") Long time)
    {
        return AjaxResult.success();
    }

    @PreAuthorize("@ss.hasPermi('system:airplane:flight')")
    @ApiOperation(value = "按条件查询飞机信息", response = SystemAirPlaneFlight.class)
    @PostMapping("/flight")
    @ApiImplicitParam(name = "flight", value = "航班信息", dataType = "HkFlight")
    public AjaxResult searchAirplaneByFlight(
            @RequestBody HkFlight flight
    ){

        return AjaxResult.success();
    }

    @Autowired
    private HkAirportMapper airportMapper;

    @PreAuthorize("@ss.hasPermi('system:airplane:area')")
    @ApiOperation(value = "随机生成一万个飞机实体", response = SystemAirplaneArea.class)
    @GetMapping("/makeplane")
    public AjaxResult makeTenthousand(){
        List<AirplaneEntityVo> list = new ArrayList<>();
        int total = 10000;
        byte icao2 = 0;
        for (int i = 0 ; i < total/256 ; i++){
            byte icao3 = 0;
            for(int j=0; j<256; j++) {
                Point point = Point.builder()
                        .latitude(RandomUtil.randomDouble(-89, 90 - 3.51) + 3.51)  // 中国的纬度范围 3°51′N至53°33′N
                        .longitude(RandomUtil.randomDouble(-180, 180 - 73.33) + 73.33) // 中国的经度范围 73°33′E至135°05′E
                        .build();
                AirplaneEntityVo vo = AirplaneEntityVo.builder()
                        .point(point)
                        .height((float) (RandomUtil.randomDouble(2, 100000) + 100.0))
                        .direction(RandomUtil.randomInt(0, 360) + (float) ((int) (Math.random() * 10)) / 10)
                        .airplaneICAO("ee" + ADSBTool.toHexString(icao2) + ADSBTool.toHexString(icao3))
                        .build();
                list.add(vo);
                icao3++;
            }
            icao2++;
        }
        return AjaxResult.success(list);
//        airportMapper.insertHkAirplane(list);
//        return AjaxResult.success();
    }

    @PreAuthorize("@ss.hasPermi('system:airplane:area')")
    @ApiOperation(value = "获取数据库中所有飞机信息", response = SystemAirplaneArea.class)
    @GetMapping("/getplane")
    public AjaxResult getTenthousand(@Param("time") Long time){
        List<AirplaneEntityVo> list = null;
        if(time != null){
            list = planeService.findHisPlaneByArea(91.0, 15.0, 130.76, 43.5, time);
        }else{
            list = planeService.findPlaneByArea(91.0, 15.0, 130.76, 43.5);
        }

        return AjaxResult.success(list);
    }

    @Autowired
    IHkAirportService hkAirportService;

    @PreAuthorize("@ss.hasPermi('system:airplane:airport')")
    @ApiOperation(value = "获取所有中国机场指定半径内飞机信息", response = SystemAirplaneArea.class)
    @GetMapping("/forairports")
    public AjaxResult getAllForAirports(@Param("time") Long time){
        List<HkAirport> airportList = hkAirportService.selectHkAirportList(null);
        Map<String, List<AirplaneEntityVo>> map = new HashMap<>();
        int radius = DBConfigUtils.getConfigValue(ConfigValues.AirPortRedius);
        for (HkAirport ap: airportList){
            List<AirplaneEntityVo> list = null;
            if(time == null){
                list = planeService.findPlaneByCircle(ap.getLongitude(), ap.getLatitude(), radius);
            } else {
                list = planeService.findHisPlaneByCircle(ap.getLongitude(), ap.getLatitude(),radius, time);
            }

            map.put(ap.getIcao(), list);
        }
        return AjaxResult.success(map);
    }

    @PreAuthorize("@ss.hasPermi('system:airplane:airport')")
    @ApiOperation(value = "获取指定机场指定半径内飞机信息", response = SystemAirplaneArea.class)
    @GetMapping("/forairport")
    public AjaxResult getAllForAirport(@Param("id") Long id, @Param("time") Long time){
        HkAirport ap = hkAirportService.selectHkAirportById(id);
        int radius = DBConfigUtils.getConfigValue(ConfigValues.AirPortRedius);
        List<AirplaneEntityVo> list = null;
        if(time == null){
            list = planeService.findPlaneByCircle(ap.getLongitude(), ap.getLatitude(), radius);
        } else {
            list = planeService.findHisPlaneByCircle(ap.getLongitude(), ap.getLatitude(),radius, time);
        }

        return AjaxResult.success(list);
    }

    @PreAuthorize("@ss.hasPermi('system:airplane:area')")
    @ApiOperation(value = "获取所有省会城市指定半径内飞机信息", response = SystemAirplaneArea.class)
    @GetMapping("/forcities")
    public AjaxResult getAllForCities(@Param("time") Long time){

        Map<String, List<AirplaneEntityVo>> map = new HashMap<>();
        int radius = DBConfigUtils.getConfigValue(ConfigValues.CityRadiusKm);
        for (ConfigValues configValues : DBConfigUtils.getAllCitys()){
            Point point = DBConfigUtils.getConfigValue(configValues);
            List<AirplaneEntityVo> list = null;
            if(time == null){
                list = planeService.findPlaneByCircle(point.getLongitude(), point.getLatitude(), radius);
            } else {
                list = planeService.findHisPlaneByCircle(point.getLongitude(), point.getLatitude(),radius, time);
            }

            map.put(configValues.toString(), list);
        }
        return AjaxResult.success(map);
    }

    @PreAuthorize("@ss.hasPermi('system:airplane:area')")
    @ApiOperation(value = "获取单个省会城市指定半径内飞机信息", response = SystemAirplaneArea.class)
    @GetMapping("/forcity")
    public AjaxResult getAllForCity(
            @Param("city") String city,
            @Param("time") Long time){
        Point point = DBConfigUtils.getConfigValue(ConfigValues.valueOf(city));
        int radius = DBConfigUtils.getConfigValue(ConfigValues.CityRadiusKm);
        List<AirplaneEntityVo> list = null;
        if(time == null) {
            list = planeService.findPlaneByCircle(point.getLongitude(), point.getLatitude(), radius);
        }else{
            list = planeService.findHisPlaneByCircle(point.getLongitude(), point.getLatitude(), radius, time);
        }

        return AjaxResult.success(list);
    }


    @ApiOperation(value = "添加測試數據", response = SystemAirplaneArea.class)
    @GetMapping("/init")
    public AjaxResult addPostData(
            @Param("type") String type,
            @Param("file") String file,
            @Param("cntTrack") Integer cntTrack,
            @Param("cntPlane") Integer cntPlane,
            @Param("icaoPre") String icaoPre,
            @Param("rad") Double rad,
            @Param("time") String time){

        long startTimeMilli = 1589165698523L; // 2020-05-11 10:54:58
        if(time != null){
            startTimeMilli = HkjsUtils.dateTimeToMilli(time);
        }
        if(type == null){
            type = "airport";
        }
        if(cntTrack == null){
            cntTrack = 2; // 2seconds
        }
        if(cntPlane == null){
            cntPlane = 10; //
        }
        if(rad == null){
            rad = 0.002;
        }
        if(icaoPre == null){
            icaoPre = "FE";
        }
        long stime = DateUtils.getNowDate().getTime();
        if(file != null){
            planeService.initPlaneByFile(startTimeMilli, cntTrack, cntPlane, icaoPre, file, rad);
        }else if(type.equals("airport")){
            planeService.initPlaneByAirport(startTimeMilli, cntTrack, cntPlane, icaoPre, rad);
        }else if(type.equals("city")){
            planeService.initPlaneByCity(startTimeMilli, cntTrack, cntPlane, icaoPre, rad);
        }

        return AjaxResult.success(String.format("Succeed in %dms", DateUtils.getNowDate().getTime() - stime));
    }

}
