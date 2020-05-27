package com.ruoyi.project.system.service.impl;

import com.ruoyi.project.system.domain.HkAdsbIdent;
import com.ruoyi.project.system.domain.HkAdsbPostion;
import com.ruoyi.project.system.domain.HkAdsbSpeed;
import com.ruoyi.project.system.service.IHkAdsbService;
import org.apache.hadoop.hbase.client.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by root on 5/9/20.
 */
@Service
public class HkAdsbServiceImpl implements IHkAdsbService {

    @Autowired
    private Connection connection;

    @Override
    public boolean savePosition(HkAdsbPostion hkAdsbPostion) {

        return false;
    }

    @Override
    public boolean saveSpeed(HkAdsbSpeed hkAdsbSpeed) {
        return false;
    }

    @Override
    public boolean saveIdent(HkAdsbIdent hkAdsbIdent) {
        return false;
    }
}
