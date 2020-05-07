package net.zdsoft.exammanage.data.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.zdsoft.exammanage.data.entity.EmExamInfo;
import net.zdsoft.exammanage.data.entity.EmOutTeacher;
import net.zdsoft.exammanage.data.service.EmExamInfoService;
import net.zdsoft.exammanage.data.service.EmOutTeacherService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/exammanage/outTeacherImport")
public class OutTeacherImportAction extends DataImportAction {

    private Logger logger = Logger.getLogger(OutTeacherImportAction.class);

    @Autowired
    private EmExamInfoService emExamInfoService;
    @Autowired
    private EmOutTeacherService emOutTeacherService;

    @ControllerInfo("进入导入首页")
    @RequestMapping("/main")
    public String importMain(String examId, ModelMap map) {
        LoginInfo loginInfo = getLoginInfo();
        String unitId = loginInfo.getUnitId();
        // 业务名称
        map.put("businessName", "校外老师");
        // 导入URL
        map.put("businessUrl", "/exammanage/outTeacherImport/import");
        // 导入模板
        map.put("templateDownloadUrl", "/exammanage/outTeacherImport/template");
        // 导入对象
        map.put("objectName", "");
        // 导入说明
        StringBuffer description = new StringBuffer();
        description.append(getDescription());

        // 如果导入文件中前面有说明性文字的  这里需要传一个有效数据开始行（列名那一行）
        //如果列名在第一行的就不需要传
        map.put("validRowStartNo", 0);
        //模板校验
        map.put("validateUrl", "/exammanage/outTeacherImport/validate");

        map.put("description", description);
        map.put("businessKey", UuidUtils.generateUuid());

        JSONObject obj = new JSONObject();
        obj.put("examId", examId);

        obj.put("unitId", unitId);
        map.put("monthPermance", JSON.toJSONString(obj));
        map.put("examId", examId);
        return "/exammanage/importFtl/outTeacherImport.ftl";
    }

    @Override
    public String getObjectName() {
        return "outTeacherFilter";
    }

    @Override
    public String getDescription() {
        return "<h4 class='box-graybg-title'>说明</h4>"
                + "<p>1、老师姓名和电话号码不能重复</p>";
    }

    @Override
    public List<String> getRowTitleList() {
        //下载文件表头
        List<String> tis = new ArrayList<String>();
        tis.add("老师姓名");
        tis.add("电话号码");
        return tis;
    }

    @Override
    @RequestMapping("/import")
    @ResponseBody
    public String dataImport(String filePath, String params) {
        logger.info("业务数据处理中......");
        //每一行数据   表头列名：0
        List<String[]> datas = ExcelUtils.readExcelByRow(filePath, 1, getRowTitleList().size());
        //错误数据序列号
        int sequence = 1;
        int totalSize = datas.size();

        List<String[]> errorDataList = new ArrayList<String[]>();
        if (CollectionUtils.isEmpty(datas)) {
            String[] errorData = new String[4];
            sequence++;
            errorData[0] = String.valueOf(sequence);
            errorData[1] = "老师姓名";
            errorData[2] = "";
            errorData[3] = "没有导入数据";
            errorDataList.add(errorData);
            return result(datas.size() - 1, 0, 0, errorDataList);
        }

        JSONObject performance = JSON.parseObject(params, JSONObject.class);
        String examId = (String) performance.get("examId");
        String unitId = (String) performance.get("unitId");

        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        if (examInfo == null) {
            for (String[] s : datas) {
                String[] errorData = new String[4];
                sequence++;
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "老师姓名";
                errorData[2] = "";
                errorData[3] = "该考试不存在";
                errorDataList.add(errorData);
            }
            return result(datas.size() - 1, 0, 0, errorDataList);
        }
        List<String> outTeacherNames = new ArrayList<String>();
        List<String> outTeacherTels = new ArrayList<String>();
        List<EmOutTeacher> teacherOutLists = emOutTeacherService.findByExamIdAndSchoolId(examId, unitId);
        for (EmOutTeacher emOutTeacher : teacherOutLists) {
            outTeacherNames.add(emOutTeacher.getTeacherName());
            outTeacherTels.add(emOutTeacher.getMobilePhone());
        }

        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

        List<EmOutTeacher> insertList = new ArrayList<EmOutTeacher>();
        List<String> teachersList = new ArrayList<String>();
        List<String> teacherTelList = new ArrayList<String>();
        EmOutTeacher emOutTeacher = null;
        int successCount = 0;
        for (String[] arr : datas) {
            String[] errorData = new String[4];
            sequence++;

            if (StringUtils.isBlank(arr[0])) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "老师姓名";
                errorData[2] = "";
                errorData[3] = "不能为空";
                errorDataList.add(errorData);
                continue;
            }

            if (StringUtils.isBlank(arr[1])) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "电话号码";
                errorData[2] = "";
                errorData[3] = "不能为空";
                errorDataList.add(errorData);
                continue;
            }

            if (arr[0].length() > 10) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "老师姓名";
                errorData[2] = "";
                errorData[3] = "长度不能大于20个字符";
                errorDataList.add(errorData);
                continue;
            }

            Matcher m = p.matcher(arr[1]);
            if (!m.matches()) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "电话号码";
                errorData[2] = "";
                errorData[3] = "请输入有效的电话号码！";
                errorDataList.add(errorData);
                continue;
            }

            if (outTeacherNames.contains(arr[0])) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "老师姓名";
                errorData[2] = "";
                errorData[3] = "该老师已存在";
                errorDataList.add(errorData);
                continue;
            }

            if (teachersList.contains(arr[0])) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "老师姓名";
                errorData[2] = "";
                errorData[3] = "不能重复导入相同的老师";
                errorDataList.add(errorData);
                continue;
            }

            if (teacherTelList.contains(arr[1])) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "电话号码";
                errorData[2] = "";
                errorData[3] = "不能重复导入相同的电话号码";
                errorDataList.add(errorData);
                continue;
            }

            if (outTeacherTels.contains(arr[1])) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "电话号码";
                errorData[2] = "";
                errorData[3] = "该电话号码已存在";
                errorDataList.add(errorData);
                continue;
            }

            successCount++;
            teachersList.add(arr[0]);
            teacherTelList.add(arr[1]);
            emOutTeacher = new EmOutTeacher();
            emOutTeacher.setExamId(examId);
            emOutTeacher.setSchoolId(unitId);
            emOutTeacher.setTeacherName(arr[0]);
            emOutTeacher.setMobilePhone(arr[1]);
            insertList.add(emOutTeacher);
        }

        try {
            emOutTeacherService.saveAll(insertList);
        } catch (Exception e) {
            e.printStackTrace();

        }

        int errorCount = totalSize - successCount;
        String result = result(totalSize, successCount, errorCount, errorDataList);
        return result;
    }

    @Override
    @RequestMapping("/template")
    public void downloadTemplate(HttpServletRequest request,
                                 HttpServletResponse response) {
        List<String> titleList = getRowTitleList();//表头
        Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
        sheetName2RecordListMap.put(getObjectName(),
                new ArrayList<Map<String, String>>());
        Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
        titleMap.put(getObjectName(), titleList);
        ExportUtils exportUtils = ExportUtils.newInstance();
        exportUtils.exportXLSFile("校外老师信息", titleMap, sheetName2RecordListMap, response);
    }


    @RequestMapping("/validate")
    @ResponseBody
    public String validate(String filePath, String validRowStartNo) {
        logger.info("模板校验中......");
        if (StringUtils.isBlank(validRowStartNo)) {
            validRowStartNo = "0";
        }
        try {
            List<String[]> datas = ExcelUtils.readExcelByRow(filePath,
                    Integer.valueOf(validRowStartNo), getRowTitleList().size());
            return templateValidate(datas, getRowTitleList());

        } catch (Exception e) {
            e.printStackTrace();
            return "上传文件不符合模板要求";
        }
    }

    private String result(int totalCount, int successCount, int errorCount, List<String[]> errorDataList) {
        Json importResultJson = new Json();
        importResultJson.put("totalCount", totalCount);
        importResultJson.put("successCount", successCount);
        importResultJson.put("errorCount", errorCount);
        importResultJson.put("errorData", errorDataList);
        return importResultJson.toJSONString();
    }
}
