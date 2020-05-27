package com.ruoyi.project.system.domain.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DefaultConfigValueVo {
    private Point point;
    private int   level;
    public DefaultConfigValueVo(){}

    public DefaultConfigValueVo(Point point, int level) {
        this.point = point;
        this.level = level;
    }
}
