package com.ruoyi.project.system.domain.vo;

import com.ruoyi.project.system.domain.HkFlight;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * Created by zhaoyl on 5/9/20.
 * 通过航班获得时间和航班号
 */

@ApiModel("飞机轨迹实体,多条轨迹的集合")
@Data
public class PlaneTracksListVo {
    private Integer planeId;
    private List<HkFlight> flights;
}
