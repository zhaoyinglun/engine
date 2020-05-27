package com.ruoyi.project.system.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruoyi.DatabaseFileExcute;
import com.ruoyi.common.utils.DBConfigUtils;
import com.ruoyi.framework.config.ConfigValues;
import com.ruoyi.project.system.domain.api.CityHeatMap;
import com.ruoyi.project.system.domain.vo.CityHeatVo;
import com.ruoyi.project.system.domain.vo.Point;
import io.swagger.annotations.ApiOperation;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.system.domain.HkOption;
import com.ruoyi.project.system.service.IHkOptionService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

import static com.ruoyi.common.utils.DBConfigUtils.getConfigValue;

/**
 * 策略配置Controller
 * 
 * @author zhaoyl
 * @date 2020-05-06
 */
@RestController
@RequestMapping("/system/option")
public class HkOptionController extends BaseController
{
    @Autowired
    private IHkOptionService hkOptionService;

    /**
     * 查询策略配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:option:list')")
    @GetMapping("/list")
    public TableDataInfo list(HkOption hkOption)
    {
        startPage();
        List<HkOption> list = hkOptionService.selectHkOptionList(hkOption);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('system:option:aaa')")
    @GetMapping("/configvalue")
    public AjaxResult configvalue(){
        List<HkOption> optionList = hkOptionService.selectHkOptionList(null);
        List<String> keyLists = new ArrayList<>();
        for (HkOption hkOption : optionList){
            keyLists.add(hkOption.getKey());
        }
        for (ConfigValues configValues : ConfigValues.values()){
            Object object = DBConfigUtils.getConfigValue(configValues);
            String key = configValues.name();
            String value = "";
            if (object instanceof List){
                List list = (List) object;
                for (Object o : list){
                    if (value==null||value.equals("")){
                        value = String.valueOf(o);
                        continue;
                    }
                    if (o == null || o.equals("")){
                        continue;
                    }
                    value = value + "," + String.valueOf(o);
                }
                if (!keyLists.contains(key)){
                    hkOptionService.insertHkOption(new HkOption(key,value));
                }
                continue;
            }
            value = String.valueOf(object);
            if (!keyLists.contains(key)){
                hkOptionService.insertHkOption(new HkOption(key,value));
            }
        }
        return AjaxResult.success();
    }


    @GetMapping("/refreshconfigvalue")
    public AjaxResult refreshConfigvalue(){
//        dbConfigUtils.refreshOption();
        try {
            DBConfigUtils.getInstance().refreshOption();
        }catch (IOException e){
            String message = e.getMessage();
        }
        return AjaxResult.success();
    }

    @GetMapping("city")
    @ApiOperation(value = "查询所有省会", response = CityHeatMap.class)
    public AjaxResult city(){


        List<CityHeatVo> cityHeatVoList = new ArrayList<>();
        for (ConfigValues configValues : DBConfigUtils.getAllCitys()){
            Point point = DBConfigUtils.getConfigValue(configValues);
            CityHeatVo cityHeatVo = new CityHeatVo(point,configValues.toString(),0);
            cityHeatVoList.add(cityHeatVo);
        }
        return AjaxResult.success(cityHeatVoList);
    }

    /**
     * 导出策略配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:option:export')")
    @Log(title = "策略配置", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(HkOption hkOption)
    {
        List<HkOption> list = hkOptionService.selectHkOptionList(hkOption);
        ExcelUtil<HkOption> util = new ExcelUtil<HkOption>(HkOption.class);
        return util.exportExcel(list, "option");
    }

    /**
     * 获取策略配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:option:query')")
    @GetMapping(value = "/{key}")
    public AjaxResult getInfo(@PathVariable("key") String key)
    {
        return AjaxResult.success(hkOptionService.selectHkOptionById(key));
    }

    /**
     * 新增策略配置
     */
    @PreAuthorize("@ss.hasPermi('system:option:add')")
    @Log(title = "策略配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody HkOption hkOption)
    {
        return toAjax(hkOptionService.insertHkOption(hkOption));
    }

    /**
     * 修改策略配置
     */
    @PreAuthorize("@ss.hasPermi('system:option:edit')")
    @Log(title = "策略配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HkOption hkOption)
    {
        return toAjax(hkOptionService.updateHkOption(hkOption));
    }

    /**
     * 删除策略配置
     */
    @PreAuthorize("@ss.hasPermi('system:option:remove')")
    @Log(title = "策略配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{keys}")
    public AjaxResult remove(@PathVariable String[] keys)
    {
        return toAjax(hkOptionService.deleteHkOptionByIds(keys));
    }
}
