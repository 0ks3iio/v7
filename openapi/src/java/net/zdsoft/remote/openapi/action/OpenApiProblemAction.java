/* 
 * @(#)OpenApiProblemAction.java    Created on 2017-5-16
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.remote.openapi.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.TypeReference;

import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.Validators;
import net.zdsoft.remote.openapi.constant.OpenApiConstants;
import net.zdsoft.remote.openapi.entity.Developer;
import net.zdsoft.remote.openapi.enums.YesNoEnum;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.remote.dto.ProblemRemoteDto;
import net.zdsoft.system.remote.dto.ProblemTypeRemoteDto;
import net.zdsoft.system.remote.service.ProblemRemoteService;
import net.zdsoft.system.remote.service.SysOptionRemoteService;

/**
 * @author xuxiyu
 * @version $Revision: 1.0 $, $Date: 2017-5-16 上午10:54:12 $
 */
@Controller
@RequestMapping("/problem/")
public class OpenApiProblemAction extends OpenApiBaseAction {
    private String serverCode = "openapi";
    @Autowired
    private ProblemRemoteService problemRemoteService;
    @Resource
    private SysOptionRemoteService sysOptionRemoteService;

    @RequestMapping("/problemList")
    public String problemList(ModelMap model) {
        List<ProblemRemoteDto> problemList = SUtils.dt(problemRemoteService.findProblemListByServerCode(serverCode),
                new TypeReference<List<ProblemRemoteDto>>() {
                });
        List<ProblemTypeRemoteDto> problemTypeList = SUtils.dt(
                problemRemoteService.findProblemTypeListByServerCode(serverCode),
                new TypeReference<List<ProblemTypeRemoteDto>>() {
                });
        // Map<String, String> typeIdAndName =
        // SUtils.dt(problemRemoteService.findProblemTypeListByServerCode(serverCode),
        // new TypeReference<Map<String, String>>() {
        // });
        Map<String, List<ProblemRemoteDto>> typeProblems = new HashMap<String, List<ProblemRemoteDto>>();
        if (problemTypeList != null && !problemTypeList.isEmpty()) {
            for (ProblemTypeRemoteDto type : problemTypeList) {
                List<ProblemRemoteDto> dtoList = new ArrayList<ProblemRemoteDto>();
                for (ProblemRemoteDto dto : problemList) {
                    if (dto.getTypeId().equals(type.getId())) {
                        dtoList.add(dto);
                    }
                }
                typeProblems.put(type.getId(), dtoList);
            }
        }
        putLogoInfo(model);
        Developer dev = getDeveloper();
        int islogin = YesNoEnum.NO.getValue();
        if (null != dev) {
            islogin = YesNoEnum.YES.getValue();
        }
        model.addAttribute("islogin", islogin);
        model.addAttribute("developer", getDeveloper());
        model.addAttribute("typeProblemsMap", typeProblems);
        model.addAttribute("problemTypeList", problemTypeList);
        return "/openapi/commonProblem/leftProblemList.ftl";
    }

    @RequestMapping("/lastProblemList")
    public String lastProblemList(ModelMap model) {
        List<ProblemRemoteDto> byTimeList = new ArrayList<ProblemRemoteDto>();
        List<ProblemRemoteDto> byViewNumList = new ArrayList<ProblemRemoteDto>();
        /* 按照时间排序取前6个 */
        List<ProblemRemoteDto> pList = SUtils.dt(problemRemoteService.findProblemOrderByModifyTime(serverCode),
                new TypeReference<List<ProblemRemoteDto>>() {
                });
        int end = 0;
        if (pList.size() > 6) {
            end = 6;
        }
        else if (pList.size() > 0) {
            end = pList.size();
        }
        byTimeList = pList.subList(0, end);
        /* 按照点击量排序取前6个 */
        pList = SUtils.dt(problemRemoteService.findProblemOrderByViewNum(serverCode),
                new TypeReference<List<ProblemRemoteDto>>() {
                });
        end = 0;
        if (pList.size() > 6) {
            end = 6;
        }
        else if (pList.size() > 0) {
            end = pList.size();
        }
        byViewNumList = pList.subList(0, end);
        model.addAttribute("byTimeList", byTimeList);
        model.addAttribute("byViewNumList", byViewNumList);
        return "/openapi/commonProblem/lastProblemList.ftl";
    }

    @RequestMapping("problemDetail")
    public String problemDetail(String id, ModelMap model) {
        ProblemRemoteDto dto = SUtils.dt(problemRemoteService.findProblemById(id),
                new TypeReference<ProblemRemoteDto>() {
                });
        problemRemoteService.updatViewNum(id);
        String fileUrl = sysOptionRemoteService.findValue(Constant.FILE_PATH);
        // String fileUrl = "E://data";
        String content = getContent(fileUrl + dto.getAnswer());
        model.addAttribute("content", content);
        model.addAttribute("problem", dto);
        return "/openapi/commonProblem/problemDetail.ftl";
    }

    @RequestMapping("serchProblem")
    public String serchProblem(String question, ModelMap model) {
        List<ProblemRemoteDto> pList = SUtils.dt(problemRemoteService.findByServerAndQuestion(serverCode, question),
                new TypeReference<List<ProblemRemoteDto>>() {
                });
        String filePath = sysOptionRemoteService.findValue(Constant.FILE_PATH);
        // String filePath = "E://data";
        if (pList != null && !pList.isEmpty()) {
            for (ProblemRemoteDto dto : pList) {
                dto.setAnswer(getContent(filePath + dto.getAnswer()));
            }
        }
        model.addAttribute("pList", pList);
        return "/openapi/commonProblem/serchProblemList.ftl";
    }

    /**
     * 获取文件中的内容
     * 
     * @param filePath
     * @return
     */
    public static String getContent(String filePath) {
        StringBuffer content = new StringBuffer();
        if (!Validators.isEmpty(filePath) && !Validators.isBlank(filePath)) {
            File file = new File(filePath);
            if (file.exists()) {
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GBK"));
                    /* S=按行读出文件中的内容 */
                    try {
                        String tempString = "";
                        while ((tempString = reader.readLine()) != null) {
                            content.append(tempString);
                        }
                    }
                    /* E=按行读出文件中的内容 */
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return content.toString().replaceAll("\ue003", "").replaceAll("\ue004", "");
    }

    private void putLogoInfo(ModelMap map) {
        @SuppressWarnings("unchecked")
        Map<String, String> logoInfoMap = (HashMap<String, String>) getRequest().getSession().getAttribute(
                OpenApiConstants.OPEN_LOGO_INFO_SESSION);
        logoInfoMap = judgeLogoInfoMap(logoInfoMap);
        map.put("logoUrl", logoInfoMap.get(OpenApiConstants.OPEN_API_LOGO_RUL));
        map.put("logoName", logoInfoMap.get(OpenApiConstants.OPEN_API_LOGO_NAME));
        map.put("foot", logoInfoMap.get(OpenApiConstants.OPEN_API_FOOT_VALUE));
        // map.put("islogin", 0);
    }

    private Map<String, String> judgeLogoInfoMap(Map<String, String> logoInfoMap) {
        if (MapUtils.isEmpty(logoInfoMap)) {
            logoInfoMap = new HashMap<String, String>();

            // String url = sysOptionRemoteService.findValue(Constant.PLAT_LOGO_PATH);
            String url = sysOptionRemoteService.findValue(OpenApiConstants.OPEN_API_LOGO_RUL);
            String name = sysOptionRemoteService.findValue(OpenApiConstants.OPEN_API_LOGO_NAME);
            logoInfoMap.put(OpenApiConstants.OPEN_API_LOGO_RUL, url);
            logoInfoMap.put(OpenApiConstants.OPEN_API_LOGO_NAME, name);
            logoInfoMap.put(OpenApiConstants.OPEN_API_FOOT_VALUE,
                    sysOptionRemoteService.findValue(OpenApiConstants.OPEN_API_FOOT_VALUE));
            getRequest().getSession().setAttribute(OpenApiConstants.OPEN_LOGO_INFO_SESSION, logoInfoMap);
        }
        return logoInfoMap;
    }
}
