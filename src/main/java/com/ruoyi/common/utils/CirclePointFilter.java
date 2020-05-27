package com.ruoyi.common.utils;

import com.ruoyi.project.system.domain.vo.Point;

/**
 * Created by root on 5/19/20.
 */
public class CirclePointFilter implements IPointFilter{

    private double lat;
    private double lon;
    private float radiusKm;

    public CirclePointFilter(double lat, double lon, float radiusKm) {
        this.lat = lat;
        this.lon = lon;
        this.radiusKm = radiusKm;
    }

    @Override
    public boolean filter(Point point) {
        return point.inCircle(lon, lat, radiusKm);
    }
}
