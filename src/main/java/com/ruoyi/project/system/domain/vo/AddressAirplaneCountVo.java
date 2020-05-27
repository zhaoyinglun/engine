package com.ruoyi.project.system.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@ApiModel("展示的飞机对象")
@Data
@Builder
public class AddressAirplaneCountVo {
    @ApiModelProperty("经度")
    private float longitude;

    @ApiModelProperty("维度")
    private float latitude;

    @ApiModelProperty("飞机数量")
    private Integer number;
}
