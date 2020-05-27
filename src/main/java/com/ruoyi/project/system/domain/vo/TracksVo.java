package com.ruoyi.project.system.domain.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;

@ApiModel("单条轨迹实体,轨迹点的集合")
@Data
public class TracksVo {
    private int id ;
    private int planeId;
    private String flightIdent;
    private Date takeOffTime;
    private Date landingTime;
    private ArrayList<TrackPointVo> tracks;
}
