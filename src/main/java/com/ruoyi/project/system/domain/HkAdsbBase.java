package com.ruoyi.project.system.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Created by root on 5/9/20.
 */
@Data
@RequiredArgsConstructor
public class HkAdsbBase {
    private long timestamp;
    private String icao;
}
