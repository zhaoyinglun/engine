package com.ruoyi.project.system.domain.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel("飞机轨迹点")
@Data
public class TrackPointVo {
    private Point point;
    private Double direction;
}
