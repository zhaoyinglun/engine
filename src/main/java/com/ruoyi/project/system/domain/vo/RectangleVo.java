package com.ruoyi.project.system.domain.vo;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;


@Builder
@Data
public class RectangleVo {

    /*
      c ----- b
      |       |
      |       |
      a ----- d
     */

    @NotNull
    private Point a;

    @NotNull
    private Point b;

    public RectangleVo() {
    }

    public RectangleVo(@NotNull Point a, @NotNull Point b) {
        this.a = a;
        this.b = b;
    }
}
