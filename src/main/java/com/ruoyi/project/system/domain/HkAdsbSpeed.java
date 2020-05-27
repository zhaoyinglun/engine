package com.ruoyi.project.system.domain;

import lombok.Data;

/**
 * Created by root on 5/9/20.
 */
@Data
public class HkAdsbSpeed extends HkAdsbBase {
    private float speed;  // knots (NM/h) >= 0
    private float gspeed; // knots (NM/h) >= 0
    private float vspeed; // feet/min
    private float track;  // 0~360 degrees
    private Double lat;
    private Double lon;

    public HkAdsbSpeed(){
        this.speed = -1;
        this.gspeed = -1;
        this.vspeed = 0;
        this.track = -1;
        this.lat = null;
        this.lon = null;
    }
}
