package com.ruoyi.project.system.domain.vo;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

@ApiModel("天气实体")
@Data
@Builder
public class WeatherEntityVo {
    private Double longitude;
    private Double latitude;
    private String weather;
    private Integer icon;
}
