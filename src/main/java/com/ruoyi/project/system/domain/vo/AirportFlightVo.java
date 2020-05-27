package com.ruoyi.project.system.domain.vo;

import com.ruoyi.project.system.domain.HkFlight;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@ApiModel("机场航班实体")
@Data
public class AirportFlightVo {
    Long airport;
    List<HkFlight> flights;
}
