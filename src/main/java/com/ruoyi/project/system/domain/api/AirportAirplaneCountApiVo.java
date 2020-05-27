package com.ruoyi.project.system.domain.api;

import com.ruoyi.project.system.domain.vo.AddressAirplaneCountVo;
import lombok.Data;

import java.util.ArrayList;

@Data
public class AirportAirplaneCountApiVo{

    private int radius;
    private ArrayList<AddressAirplaneCountVo> list;
}
