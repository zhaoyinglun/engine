package com.ruoyi.project.system.domain;

import lombok.Data;

/**
 * Created by root on 5/9/20.
 */
@Data
public class HkAdsbPostion extends HkAdsbBase{
    private double latitude;
    private double longitude;
    private float altitude; // meters
    private float track; // 0~360 degrees
    private float gspeed; // knots (NM/h) >= 0
    private boolean isSurface;

    public HkAdsbPostion(){

    }

    public HkAdsbPostion(double latitude, double longitude, float altitude, boolean isSurface) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.isSurface = isSurface;
        this.track = -1;
        this.gspeed = -1;
    }

    @Override
    public String toString() {
        return "HkAdsbPostion{" +
                "ts=" + getTimestamp() +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", altitude=" + altitude +
                ", track=" + track +
                ", isSurface=" + isSurface +
                '}';
    }
}
