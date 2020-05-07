/* 
 * @(#)SysOptionAction.java    Created on 2017-2-28
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.action.config;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.framework.utils.Validators;
import net.zdsoft.passport.remoting.system.CooperatorDto;
import net.zdsoft.passport.remoting.system.ServerService;
import net.zdsoft.system.config.PassportServerClient;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.dto.common.TabMenuDto;
import net.zdsoft.system.entity.config.SysOption;
import net.zdsoft.system.entity.config.SystemIni;
import net.zdsoft.system.enums.YesNoEnum;
import net.zdsoft.system.enums.config.BaseOptionCodesEnum;
import net.zdsoft.system.service.config.SysOptionService;
import net.zdsoft.system.service.config.SystemIniService;

@Controller
@RequestMapping("/system/sysOption")
public class SysOptionAction extends BaseAction {

    @Autowired
    private SysOptionService sysOptionService;
    @Autowired
    private SystemIniService systemIniService;
    @Autowired
    private PassportServerClient passportServerClient;

    @RequestMapping("/index")
    public String ConfigHome(String tabCode, ModelMap map) {
        String url = "/system/sysOption/tab";
        if (StringUtils.isNotEmpty(tabCode)) {
            url += "?tabCode=" + tabCode;
        }
        map.put("url", url);
        return "/system/common/home.ftl";
    }

    @RequestMapping("/tab")
    public String configTab(String tabCode, ModelMap map) {
        List<TabMenuDto> tabList = new ArrayList<TabMenuDto>();
        tabList.add(new TabMenuDto("基本信息设置", "/system/sysOption/sysOption", "option"));
        tabList.add(new TabMenuDto("平台参数设置", "/system/sysOption/optionList", "option"));
        // tabList.add(new TabMenuDto("系统应用设置", "/system/sysOption/optionList"));
        // tabList.add(new TabMenuDto("微代码设置1", "/system/mcode/mcodeDetailList"));
        tabList.add(new TabMenuDto("微代码设置", "/system/mcode/mcodeLists", "mcode"));
        map.put("tabCode", tabCode);
        map.put("tabList", tabList);
        return "/system/common/tabHome.ftl";
    }

    /**
     * 进入基本信息设置页面
     * 
     * @param map
     * @return
     */
    @RequestMapping("/sysOption")
    public String addOption(ModelMap map) {
        BaseOptionCodesEnum[] baseOptionCodes = BaseOptionCodesEnum.values();
        Map<String, SysOption> codeAndOptionMap = new HashMap<String, SysOption>();
        for (BaseOptionCodesEnum optionCode : baseOptionCodes) {
            SysOption option = sysOptionService.findOneByOptionCode(optionCode.getCode());
            if (option != null) {
                codeAndOptionMap.put(optionCode.getCode(), option);
            }
        }
        String fileUrl = sysOptionService.getFileUrl(getRequest().getServerName());
        map.put("baseOptionCodes", baseOptionCodes);
        map.put("codeAndOptionMap", codeAndOptionMap);
        map.put("fileUrl", fileUrl);
        return "/system/config/sysOption.ftl";
    }

    /**
     * 基本信息设置保存信息
     * 
     * @param codeAndValues
     * @return
     * @throws UnsupportedEncodingException
     */
    @ResponseBody
    @RequestMapping("saveBaseOptions")
    public String saveBaseOptions(String codeAndValues) {
        JSONObject jsonObject = JSONObject.parseObject(codeAndValues);
        Map<String, String> codeAndValueMap = new HashMap<String, String>();
        ServerService client = passportServerClient.getPassportServerService();
        CooperatorDto cooperator = new CooperatorDto();
        for (BaseOptionCodesEnum codeEnum : BaseOptionCodesEnum.values()) {
            String optionCode = codeEnum.getCode();
            String nowValue = jsonObject.getString(optionCode).trim();
            if (!Validators.isEmpty(nowValue)) {
                codeAndValueMap.put(optionCode, nowValue);
            }
        }
        if (codeAndValueMap != null && codeAndValueMap.size() == BaseOptionCodesEnum.values().length) {
            for (Map.Entry<String, String> entry : codeAndValueMap.entrySet()) {
                String nowValue = entry.getValue();
                String optionCode = entry.getKey();
                SysOption sysOption = sysOptionService.findOneByOptionCode(optionCode);
                if (sysOption != null) {// 修改
                    sysOptionService.updateNowValueByCode(nowValue, optionCode);
                }
                else {// 新增
                    sysOption = new SysOption();
                    sysOption.setId(UuidUtils.generateUuid());
                    sysOption.setName(BaseOptionCodesEnum.getName(optionCode));
                    sysOption.setOptionCode(optionCode);
                    sysOption.setDefaultValue(nowValue);
                    sysOption.setDescription(BaseOptionCodesEnum.getName(optionCode));
                    sysOption.setNowValue(nowValue);
                    sysOption.setViewable(YesNoEnum.YES.getValue());
                    sysOption.setValueType(0);
                    sysOptionService.saveOne(sysOption);
                }
            }
            // 同步合作站点
            Date date = new Date();
            cooperator.setId(0);
            cooperator.setSystemName(codeAndValueMap.get(BaseOptionCodesEnum.PLAT_NAME.getCode()));// 平台名称
            cooperator.setName(codeAndValueMap.get(BaseOptionCodesEnum.PLAT_ABBREVIATION.getCode()));
            cooperator.setLogoPath(codeAndValueMap.get(BaseOptionCodesEnum.PLAT_LOGO_PATH.getCode()));// 反白logo
            cooperator.setFootPage(codeAndValueMap.get(BaseOptionCodesEnum.LPAT_BOTTOM.getCode()));// 页脚
            cooperator.setRegionId(sysOptionService.findValueByOptionCode(Constant.REGION_CODE));// 行政区划
            cooperator.setPassportURL(sysOptionService.findValueByOptionCode(Constant.PASSPORT_URL));// passport接口地址
            cooperator.setModifyTime(date);
            try {
                client.modifyCooperator(cooperator);
            }
            catch (Exception e) {
                return error(e.getMessage());
            }
        }
        else {
            return error("请维护所有参数");
        }
        return returnSuccess();
    }

    @ResponseBody
    @RequestMapping("/modifySysOption")
    public String modifySysOption() {
        // TODO
        return returnSuccess();
    }

    /**
     * 平台参数信息设置列表页面
     * 
     * @param map
     * @return
     */
    @RequestMapping("/optionList")
    public String getOptionList(ModelMap map) {
        List<SysOption> systemOptionList = sysOptionService.findAll();
        List<SystemIni> systemIniList = systemIniService.findAll();
        map.put("systemOptionList", systemOptionList);
        map.put("systemIniList", systemIniList);
        return "/system/config/optionList.ftl";
    }

    /**
     * 修改单个平台参数值
     * 
     * @param nowValue
     * @param id
     * @param isBase
     * @return
     */
    @ResponseBody
    @RequestMapping("/updateNowValue")
    public String updateNowValue(String nowValue, String code, boolean isBase) {
        if (StringUtils.length(nowValue) > 1000) {
            return error("内容过长");
        }
        if (isBase) {
            sysOptionService.updateNowValueByCode(nowValue, code);
        }
        else {
            systemIniService.updateNowvalue(nowValue, code);
        }
        return returnSuccess();
    }

    /**
     * 修改单个平台参数值
     * 
     * @param nowValue
     * @param id
     * @param isBase
     * @return
     */
    @ResponseBody
    @RequestMapping("/updateValueType")
    public String updateValueType(Integer valueType, String code, boolean isBase) {
        if (isBase) {
            sysOptionService.updateValueType(valueType, code);
        }
        else {
            systemIniService.updateValueType(valueType, code);
        }
        return returnSuccess();
    }
}
