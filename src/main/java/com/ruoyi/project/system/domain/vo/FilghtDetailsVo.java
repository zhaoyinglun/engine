package com.ruoyi.project.system.domain.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

@ApiModel("航班详细信息")
@Data
public class FilghtDetailsVo {
    int flightId ;
    Date takeOffTime;
    Date landingTime;
    int airportIdFrom;
    int airportIdTo;
    String palneMark;
    TrackPointVo tracksEntity;
}
