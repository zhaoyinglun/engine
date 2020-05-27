package com.ruoyi.project.system.domain.vo;

import com.ruoyi.common.utils.HkjsUtils;
import lombok.Builder;
import lombok.Data;

/**
 * Created by zhaoyl on 5/9/20.
 */

@Data
@Builder
public class Point {
    private double latitude;
    private double longitude;
    public Point(){

    }

    public Point(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return longitude + "," + latitude;
    }

    public boolean inArea(double slon, double slat, double elon, double elat){
        return latitude >= slat && latitude <= elat
                && longitude >= slon && longitude <= elon;
    }

    public boolean inCircle(double slon, double slat, float radiusKm){
        return HkjsUtils.distanceByLongNLat(longitude, latitude, slon, slat) <= radiusKm * 1000;
    }
}
