package com.ruoyi.common.utils;

import com.ruoyi.project.system.domain.vo.Point;

/**
 * Created by root on 5/19/20.
 */
public class RectPointFilter implements IPointFilter{

    private double slat;
    private double slon;
    private double elat;
    private double elon;

    public RectPointFilter(double slat, double slon, double elat, double elon) {
        this.slat = slat;
        this.slon = slon;
        this.elat = elat;
        this.elon = elon;
    }

    @Override
    public boolean filter(Point point) {
        return point.inArea(slon, slat, elon, elat);
    }
}
