package com.ruoyi.project.system.domain.vo;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

@ApiModel("飞机实体")
@Data
@Builder
public class AirplaneEntityVo {
    private Integer id;
    private Point point;
    private Float height;
    private Float speed;
    private Float vspeed;
    private String type;
    private Float direction;
    private String airplaneICAO;
    private String flightICAO;
    private Long time;
    private Boolean isSurface;
}
