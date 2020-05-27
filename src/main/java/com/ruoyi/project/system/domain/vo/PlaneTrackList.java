package com.ruoyi.project.system.domain.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * Created by zhaoyl on 5/11/20.
 * 直接获得时间和航班号
 */

@ApiModel("飞机轨迹实体,多条轨迹的集合")
@Data
public class PlaneTrackList {
    private int planeId;
    List<TimeAndIdentVo> trackList;
}
