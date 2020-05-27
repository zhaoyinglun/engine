package com.ruoyi.project.system.domain.vo;

import lombok.Builder;
import lombok.Data;

/**
 * Created by zhaoyl on 5/11/20.
 */
@Data
@Builder
public class CityHeatVo {
    Point cityPoint;
    String cityName;
    int cityHeat;
    public CityHeatVo(){}

    public CityHeatVo(Point cityPoint, String cityName, int cityHeat) {
        this.cityPoint = cityPoint;
        this.cityName = cityName;
        this.cityHeat = cityHeat;
    }
}
