package com.ruoyi.project.system.service;

import com.ruoyi.project.system.domain.AdsbVector;
import com.ruoyi.project.system.domain.HkFlightTrack;
import com.ruoyi.project.system.domain.vo.AirplaneEntityVo;

import java.util.List;

/**
 * Created by root on 4/26/20.
 */
public interface IHkAirplaneService {
    public HkFlightTrack findCurrentTrackByIcao(String icao);
    public HkFlightTrack findTrackByIcao(String icao, long startTime);
    public List<AirplaneEntityVo> findPlaneByArea(double slon, double slat, double elon, double elat);
    public List<AirplaneEntityVo> findHisPlaneByArea(double slon, double slat, double elon, double elat, long time);
    public List<AirplaneEntityVo> findPlaneByCircle(double lon, double lat, float radius);
    public List<AirplaneEntityVo> findHisPlaneByCircle(double lon, double lat, float radius, long time);
    public void initPlaneByCity(long startTimeMilli, int cntTrack, int cntPlane, String icaoPrefix, double radius);
    public void initPlaneByAirport(long startTimeMilli, int cntTrack, int cntPlane, String icaoPrefix, double radius);
    public boolean initPlaneByFile(long startTimeMilli, int cntTrack, int cntPlane, String icaoPrefix, String filePath, double radius);
    public void createTables();
}
