package com.ruoyi.project.system.domain.vo;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

@ApiModel("机场实体")
@Data
@Builder
public class AirportVo {
    private Long id;
    private Double longitude;
    private Double latitude;
    private String icao;
    private String iata;
    private String friendlylocation;
    private String friendlyname;
}
