/* 
 * @(#)ProblemAction.java    Created on 2017-5-8
 * Copyright (c) 2017 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.system.action.commonProblem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
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

import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.framework.utils.Validators;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.entity.commonProblem.Problem;
import net.zdsoft.system.entity.commonProblem.ProblemServer;
import net.zdsoft.system.entity.commonProblem.ProblemType;
import net.zdsoft.system.service.commonProblem.ProblemServerService;
import net.zdsoft.system.service.commonProblem.ProblemService;
import net.zdsoft.system.service.commonProblem.ProblemTypeService;
import net.zdsoft.system.service.config.SysOptionService;

@Controller
@RequestMapping("/system/problem")
public class ProblemAction extends BaseAction {
    @Autowired
    private ProblemServerService problemServerService;
    @Autowired
    private ProblemTypeService problemTypeService;
    @Autowired
    private ProblemService problemService;
    @Autowired
    private SysOptionService sysOptionService;

    @RequestMapping("/index")
    public String ConfigHome(String serverCode, ModelMap map) {
        String url = "/system/problem/commonProblem";
        if (StringUtils.isNotEmpty(serverCode)) {
            url += "?serverCode=" + serverCode;
        }
        map.put("url", url);
        return "/system/common/home.ftl";
    }

    @RequestMapping("/commonProblem")
    public String commonProblem(ModelMap map, String serverCode) {
        List<ProblemServer> serverList = problemServerService.findAll();
        map.put("serverList", serverList);
        if (serverList != null && !serverList.isEmpty()) {
            ProblemServer server = serverList.get(0);
            if (Validators.isEmpty(serverCode)) {
                serverCode = server.getCode();
            }
            List<ProblemType> typeList = problemTypeService.findByServerCode(serverCode);
            String filePath = sysOptionService.findValueByOptionCode(Constant.FILE_PATH);
            map.put("typeList", typeList);
            map.put("serverCode", serverCode);
            if (typeList != null && !typeList.isEmpty()) {
                List<Problem> problemList = problemService.findByServerCode(serverCode);
                if (problemList != null && !problemList.isEmpty()) {
                    Map<String, List<Problem>> typeAndproblems = new HashMap<String, List<Problem>>();
                    for (ProblemType type : typeList) {
                        List<Problem> problems = typeAndproblems.get(type.getId());
                        if (problems == null) {
                            problems = new ArrayList<Problem>();
                        }
                        for (Problem problem : problemList) {
                            if (problem.getTypeId().equals(type.getId())) {
                                problem.setAnswer(filePath + problem.getAnswer());// 文件地址加域名
                                problems.add(problem);
                            }
                        }
                        typeAndproblems.put(type.getId(), problems);
                    }
                    map.put("typeAndproblems", typeAndproblems);
                }
            }
        }
        return "/system/commonProblem/commonProblem.ftl";
    }

    /**
     * 新增问题类型
     * 
     * @param serverCode
     * @param name
     * @return
     */
    @ResponseBody
    @RequestMapping("addProblemType")
    public String addProblemType(String serverCode, String name) {
        if (Validators.isEmpty(name)) {
            return returnSuccess("-1", "分类名不能为空");
        }
        ProblemType type = new ProblemType();
        type.setId(UuidUtils.generateUuid());
        type.setName(name);
        type.setServerCode(serverCode);
        type.setIsDeleted(0);
        type.setModifyTime(new Date());
        if (problemTypeService.saveOne(type) != null) {
            return returnSuccess();
        }
        return returnError();
    }

    /**
     * 删除问题类型
     * 
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("removeProblemType")
    public String removeProblemType(String id) {
        problemTypeService.removeProblemTypeById(id);
        problemService.delProblemByTypeId(id);
        return returnSuccess();
    }

    /**
     * 修改问题类型
     * 
     * @param id
     * @param name
     * @return
     */
    @ResponseBody
    @RequestMapping("updateProblemType")
    public String updateProblemType(String id, String name) {
        if (Validators.isEmpty(name)) {
            return returnSuccess("-1", "分类名称不能为空");
        }
        problemTypeService.updateTypeName(name, id);
        return returnSuccess();
    }

    @ResponseBody
    @RequestMapping("addProblem")
    public String addProblem(Problem problem, String content) {
        if (Validators.isEmpty(content)) {
            return returnSuccess("-1", "问题答案不能为空");
        }
        String id = UuidUtils.generateUuid();
        problem.setId(id);
        // 设置问题答案保存路径
        Date date = new Date();
        String path = buildContentFilePath(id, date);
        // 获取
        String filePath = sysOptionService.findValueByOptionCode(Constant.FILE_PATH);
        // filePath = "E://data/";
        // 问题答案保存文本
        saveOrUpdate(filePath + path, content);
        problem.setAnswer(path);
        problem.setCreationTime(date);
        problem.setModifyTime(date);
        problemService.saveProblem(problem);
        return returnSuccess();
    }

    @ResponseBody
    @RequestMapping("updateProblem")
    public String updateProblem(Problem problem, String content) {
        if (Validators.isEmpty(content)) {
            return returnSuccess("-1", "问题答案不能为空");
        }
        String id = problem.getId();
        // 设置问题答案保存路径
        Date date = new Date();
        String path = buildContentFilePath(id, date);
        // 获取
        String filePath = sysOptionService.findValueByOptionCode(Constant.FILE_PATH);
        // filePath = "E://data/";
        // 问题答案保存文本
        saveOrUpdate(filePath + path, content);
        problem.setModifyTime(date);
        problemService.updateProblem(problem.getQuestion(), path, date, id);
        return returnSuccess();
    }

    @ResponseBody
    @RequestMapping("delProblem")
    public String delProblem(String id) {
        problemService.delProblemById(id);
        return returnSuccess();
    }

    /**
     * 获取文件中的内容
     * 
     * @param filePath
     * @return
     */
    @ResponseBody
    @RequestMapping("getContent")
    public String getContent(String filePath) {
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
        return returnSuccess("00", content.toString().replaceAll("\ue003", "").replaceAll("\ue004", ""));
    }

    /**
     * 生成文件存放的路径
     * 
     * @param month
     * @param noticeId
     * @return
     */
    private String buildContentFilePath(String problemId, Date date) {
        StringBuilder builder = new StringBuilder();
        builder.append("/commonProblem/ueditor/");
        builder.append((new SimpleDateFormat("yyyyMM")).format(date));
        builder.append("/" + problemId);
        return builder.toString();
    }

    /**
     * 将编辑框中的文件内容保存到文件中
     * 
     * @param filePath
     * @param content
     * @return
     */
    public static void saveOrUpdate(String filePath, String content) {
        /* 1. 保证要保存的对象内容不为空 */
        if (!Validators.isEmpty(content) && !Validators.isEmpty(filePath)) {
            /* 2. 定义文件对象和流对象 */
            BufferedWriter outWriter = null;
            File file = new File(filePath);// 要修改或者保存的文件对象

            /* S=创建文件保存目录 */
            if (!file.getParentFile().exists()) {// 如果目录不存在，新建
                file.getParentFile().mkdirs();
            }
            /* E=创建文件保存目录 */

            try {
                outWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "GBK"));
                /* 把修改后的内容写入文件中 */
                outWriter.write(content);
                outWriter.flush();
            }
            catch (Exception e) {
                log.debug("上传文件失败：" + content);
                e.printStackTrace();
            }
            finally {
                if (outWriter != null) {
                    try {
                        outWriter.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
