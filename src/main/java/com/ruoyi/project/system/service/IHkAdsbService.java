package com.ruoyi.project.system.service;

import com.ruoyi.project.system.domain.HkAdsbIdent;
import com.ruoyi.project.system.domain.HkAdsbPostion;
import com.ruoyi.project.system.domain.HkAdsbSpeed;

/**
 * Created by root on 5/9/20.
 */
public interface IHkAdsbService {
    public boolean savePosition(HkAdsbPostion hkAdsbPostion);
    public boolean saveSpeed(HkAdsbSpeed hkAdsbSpeed);
    public boolean saveIdent(HkAdsbIdent hkAdsbIdent);
}
