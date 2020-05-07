package net.zdsoft.exammanage.data.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.exammanage.data.constant.ExammanageConstants;
import net.zdsoft.exammanage.data.entity.EmExamInfo;
import net.zdsoft.exammanage.data.entity.EmOutTeacher;
import net.zdsoft.exammanage.data.entity.EmPlaceTeacher;
import net.zdsoft.exammanage.data.entity.EmSubjectInfo;
import net.zdsoft.exammanage.data.service.EmExamInfoService;
import net.zdsoft.exammanage.data.service.EmOutTeacherService;
import net.zdsoft.exammanage.data.service.EmPlaceTeacherService;
import net.zdsoft.exammanage.data.service.EmSubjectInfoService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.SUtils;
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
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/exammanage/inspectorsImport")
public class InspectorsTeacherImportAction extends DataImportAction {

    private Logger logger = Logger.getLogger(InspectorsTeacherImportAction.class);

    private String examCode;

    @Autowired
    private EmSubjectInfoService emSubjectInfoService;
    @Autowired
    private CourseRemoteService courseRemoteService;
    @Autowired
    private EmPlaceTeacherService emPlaceTeacherService;
    @Autowired
    private TeacherRemoteService teacherRemoteService;
    @Autowired
    private EmOutTeacherService emOutTeacherService;
    @Autowired
    private EmExamInfoService emExamInfoService;

    @ControllerInfo("进入导入首页")
    @RequestMapping("/main")
    public String importMain(String examId, ModelMap map) {
        LoginInfo loginInfo = getLoginInfo();
        String unitId = loginInfo.getUnitId();
        examCode = examId;
        // 业务名称
        map.put("businessName", "巡考老师设置");
        // 导入URL
        map.put("businessUrl", "/exammanage/inspectorsImport/import");
        // 导入模板
        map.put("templateDownloadUrl", "/exammanage/inspectorsImport/template");
        // 导入对象
        map.put("objectName", "");
        // 导入说明
        StringBuffer description = new StringBuffer();
        description.append(getDescription());

        // 如果导入文件中前面有说明性文字的  这里需要传一个有效数据开始行（列名那一行）
        //如果列名在第一行的就不需要传
        map.put("validRowStartNo", 0);
        //模板校验
        map.put("validateUrl", "/exammanage/inspectorsImport/validate");

        map.put("description", description);
        map.put("businessKey", UuidUtils.generateUuid());

        JSONObject obj = new JSONObject();
        obj.put("examId", examId);

        obj.put("unitId", unitId);
        map.put("monthPermance", JSON.toJSONString(obj));
        map.put("examId", examId);
        return "/exammanage/importFtl/inspectorsImport.ftl";
    }

    @Override
    public String getObjectName() {
        return "inspectorsTeacherFilter";
    }

    @Override
    public String getDescription() {
        return "<h4 class='box-graybg-title'>说明</h4>"
                + "<p>1、模板中列出的考试信息请勿修改</p>"
                + "<p>2、请填写监考老师的姓名，多位老师以逗号分隔</p>"
                + "<p>3、同一科目考试监考和巡考老师不能重复</p>";
    }

    @Override
    public List<String> getRowTitleList() {
        //下载文件表头
        List<String> tis = new ArrayList<String>();
        tis.add("科目");
        tis.add("日期");
        tis.add("时段");
        tis.add("巡考老师");
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
            errorData[1] = "科目";
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
            String[] errorData = new String[4];
            sequence++;
            errorData[0] = String.valueOf(sequence);
            errorData[1] = "科目";
            errorData[2] = "";
            errorData[3] = "该考试不存在";
            errorDataList.add(errorData);
            return result(datas.size() - 1, 0, 0, errorDataList);
        }

        List<EmSubjectInfo> findSByExamIdIn = emSubjectInfoService.findByExamId(examId);
        Map<String, String> courseNamesMap = new LinkedHashMap<String, String>();
        Map<String, EmSubjectInfo> subjectInfoMap = new LinkedHashMap<String, EmSubjectInfo>();
        if (CollectionUtils.isNotEmpty(findSByExamIdIn)) {
            for (EmSubjectInfo emSubjectInfo : findSByExamIdIn) {
                courseNamesMap.put(emSubjectInfo.getSubjectId(), emSubjectInfo.getCourseName());
                subjectInfoMap.put(emSubjectInfo.getCourseName(), emSubjectInfo);
            }
        }

        List<EmOutTeacher> teacherOutLists = emOutTeacherService.findByExamIdAndSchoolId(examCode, unitId);
        Map<String, String> teacherOutsMap = new LinkedHashMap<String, String>();
        if (CollectionUtils.isNotEmpty(teacherOutLists)) {
            for (EmOutTeacher emOutTeacher : teacherOutLists) {
                teacherOutsMap.put(emOutTeacher.getTeacherName(), emOutTeacher.getId());
            }
        }
        List<Teacher> teacherInLists = SUtils.dt(teacherRemoteService.findByUnitId(unitId), new TR<List<Teacher>>() {
        });
        Map<String, String> teacherInsMap = new LinkedHashMap<String, String>();
        if (CollectionUtils.isNotEmpty(teacherInLists)) {
            for (Teacher teacher : teacherInLists) {
                teacherInsMap.put(teacher.getTeacherName(), teacher.getId());
            }
        }
        Map<String, String> invigilateTeacherMap = new LinkedHashMap<String, String>();
        List<EmPlaceTeacher> invigilateTeacherList = emPlaceTeacherService.findByExamIdAndType(examId, ExammanageConstants.TEACHER_TYPE1);
        if (CollectionUtils.isNotEmpty(invigilateTeacherList) && CollectionUtils.isNotEmpty(findSByExamIdIn)) {
            for (EmSubjectInfo subjectInfo : findSByExamIdIn) {
                String invigilateTeacherIds = "";
                for (EmPlaceTeacher emPlaceTeacher : invigilateTeacherList) {
                    if (emPlaceTeacher.getSubjectId().equals(subjectInfo.getSubjectId())) {
                        if (StringUtils.isNotBlank(emPlaceTeacher.getTeacherIdsIn())) {
                            invigilateTeacherIds += emPlaceTeacher.getTeacherIdsIn() + ",";
                        }
                        if (StringUtils.isNotBlank(emPlaceTeacher.getTeacherIdsOut())) {
                            invigilateTeacherIds += emPlaceTeacher.getTeacherIdsOut() + ",";
                        }
                    }
                }
                invigilateTeacherMap.put(subjectInfo.getSubjectId(), invigilateTeacherIds);
            }
        }

        List<EmPlaceTeacher> invigilateList = new ArrayList<EmPlaceTeacher>();
        EmPlaceTeacher emPlaceTeacher = null;
        int successCount = 0;
        for (String[] arr : datas) {
            String[] errorData = new String[4];
            sequence++;

            if (StringUtils.isBlank(arr[0])) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "科目";
                errorData[2] = "";
                errorData[3] = "不能为空";
                errorDataList.add(errorData);
                continue;
            }

            if (!subjectInfoMap.containsKey(arr[0])) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "科目";
                errorData[2] = "";
                errorData[3] = "不存在该科目的考试";
                errorDataList.add(errorData);
                continue;
            }

            if (StringUtils.isBlank(arr[3])) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "巡考老师";
                errorData[2] = "";
                errorData[3] = "不能为空";
                errorDataList.add(errorData);
                continue;
            }

            String[] teacherNames = arr[3].replaceAll("，", ",").split(",");
            Set<String> teacherNamesSet = new HashSet<String>();
            boolean haveTeacher = true;
            String wrongName1 = "";
            for (String str : teacherNames) {
                teacherNamesSet.add(str);
                if ((!teacherInsMap.containsKey(str)) && (!teacherOutsMap.containsKey(str))) {
                    wrongName1 = str;
                    haveTeacher = false;
                }
            }

            if (!haveTeacher) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "巡考老师";
                errorData[2] = wrongName1;
                errorData[3] = "老师不存在";
                errorDataList.add(errorData);
                continue;
            }

            if (teacherNames.length != teacherNamesSet.size()) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "巡考老师";
                errorData[2] = wrongName1;
                errorData[3] = "老师不能重复";
                errorDataList.add(errorData);
                continue;
            }

            EmSubjectInfo subjectInfo = subjectInfoMap.get(arr[0]);
            String teacherInIds = "";
            String teacherOutIds = "";
            String wrongName2 = "";
            boolean haveinvigilate = false;
            for (String str : teacherNames) {
                String invigilateIds = invigilateTeacherMap.get(subjectInfo.getSubjectId());
                if (teacherInsMap.containsKey(str)) {
                    if (StringUtils.isNotBlank(invigilateIds)) {
                        if (invigilateIds.indexOf(teacherInsMap.get(str)) != -1) {
                            wrongName2 = str;
                            haveinvigilate = true;
                            break;
                        }
                    }
                    teacherInIds += teacherInsMap.get(str) + ",";
                }
                if (teacherOutsMap.containsKey(str)) {
                    if (StringUtils.isNotBlank(invigilateIds)) {
                        if (invigilateIds.indexOf(teacherOutsMap.get(str)) != -1) {
                            wrongName2 = str;
                            haveinvigilate = true;
                            break;
                        }
                    }
                    teacherOutIds += teacherOutsMap.get(str) + ",";
                }
            }

            if (haveinvigilate) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "巡考老师";
                errorData[2] = wrongName2;
                errorData[3] = "已设置该老师为当前时间段的监考老师";
                errorDataList.add(errorData);
                continue;
            }

            successCount++;
            emPlaceTeacher = new EmPlaceTeacher();
            emPlaceTeacher.setUnitId(unitId);
            emPlaceTeacher.setExamId(examId);
            if (StringUtils.isNotBlank(teacherInIds)) {
                teacherInIds = teacherInIds.substring(0, teacherInIds.length() - 1);
                emPlaceTeacher.setTeacherIdsIn(teacherInIds);
            }
            if (StringUtils.isNotBlank(teacherOutIds)) {
                teacherOutIds = teacherOutIds.substring(0, teacherOutIds.length() - 1);
                emPlaceTeacher.setTeacherIdsOut(teacherOutIds);
            }
            emPlaceTeacher.setSubjectId(subjectInfo.getSubjectId());
            emPlaceTeacher.setStartTime(subjectInfo.getStartDate());
            emPlaceTeacher.setEndTime(subjectInfo.getEndDate());
            emPlaceTeacher.setType(ExammanageConstants.TEACHER_TYPE2);
            invigilateList.add(emPlaceTeacher);
        }

        try {
            if (CollectionUtils.isNotEmpty(invigilateList)) {
                emPlaceTeacherService.saveAllAndDel(examId, invigilateList, ExammanageConstants.TEACHER_TYPE2);
            }
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
        LoginInfo loginInfo = getLoginInfo();
        String unitId = loginInfo.getUnitId();
        List<EmSubjectInfo> findSByExamIdIn = emSubjectInfoService.findByExamId(examCode);
        Map<String, String> courseNamesMap = new LinkedHashMap<String, String>();
        if (CollectionUtils.isNotEmpty(findSByExamIdIn)) {
            for (EmSubjectInfo emSubjectInfo : findSByExamIdIn) {
                courseNamesMap.put(emSubjectInfo.getSubjectId(), emSubjectInfo.getCourseName());
            }
        }
        List<EmPlaceTeacher> teacherList = emPlaceTeacherService.findByExamIdAndType(examCode, ExammanageConstants.TEACHER_TYPE2);
        Map<String, String> teacherNamesMap = new LinkedHashMap<String, String>();
        if (CollectionUtils.isNotEmpty(teacherList)) {
            List<EmOutTeacher> teacherOutLists = emOutTeacherService.findByExamIdAndSchoolId(examCode, unitId);
            Map<String, String> teacherOutsMap = new LinkedHashMap<String, String>();
            if (CollectionUtils.isNotEmpty(teacherOutLists)) {
                for (EmOutTeacher emOutTeacher : teacherOutLists) {
                    teacherOutsMap.put(emOutTeacher.getId(), emOutTeacher.getTeacherName());
                }
            }
            List<Teacher> teacherInLists = SUtils.dt(teacherRemoteService.findByUnitId(unitId), new TR<List<Teacher>>() {
            });
            Map<String, String> teacherInsMap = new LinkedHashMap<String, String>();
            if (CollectionUtils.isNotEmpty(teacherInLists)) {
                for (Teacher teacher : teacherInLists) {
                    teacherInsMap.put(teacher.getId(), teacher.getTeacherName());
                }
            }
            for (EmPlaceTeacher emPlaceTeacher : teacherList) {
                String teacherNamesIn = "";
                String teacherNamesOut = "";
                String teacherNames = "";
                if (StringUtils.isNotBlank(emPlaceTeacher.getTeacherIdsIn())) {
                    String[] teacherIdsIn = emPlaceTeacher.getTeacherIdsIn().split(",");
                    for (String str : teacherIdsIn) {
                        teacherNamesIn += teacherInsMap.get(str) + ",";
                    }
                    teacherNamesIn = teacherNamesIn.substring(0, teacherNamesIn.length() - 1);
                }
                if (StringUtils.isNotBlank(emPlaceTeacher.getTeacherIdsOut())) {
                    String[] teacherIdsOut = emPlaceTeacher.getTeacherIdsOut().split(",");
                    for (String str : teacherIdsOut) {
                        teacherNamesOut += teacherOutsMap.get(str) + ",";
                    }
                    teacherNamesOut = teacherNamesOut.substring(0, teacherNamesOut.length() - 1);
                }
                if (StringUtils.isNotBlank(teacherNamesIn) && StringUtils.isNotBlank(teacherNamesOut)) {
                    teacherNames = teacherNamesIn + "," + teacherNamesOut;
                } else if (StringUtils.isNotBlank(teacherNamesIn) && StringUtils.isBlank(teacherNamesOut)) {
                    teacherNames = teacherNamesIn;
                } else if (StringUtils.isBlank(teacherNamesIn) && StringUtils.isNotBlank(teacherNamesOut)) {
                    teacherNames = teacherNamesOut;
                }
                if (StringUtils.isNotBlank(teacherNames)) {
                    teacherNamesMap.put(emPlaceTeacher.getSubjectId(), teacherNames);
                }
            }
        }
        List<Map<String, String>> templateList = new ArrayList<Map<String, String>>();
        Map<String, String> templateMap = null;
        if (CollectionUtils.isNotEmpty(findSByExamIdIn)) {
            for (EmSubjectInfo subjectInfo : findSByExamIdIn) {
                templateMap = new LinkedHashMap<String, String>();
                templateMap.put("科目", courseNamesMap.get(subjectInfo.getSubjectId()));
                templateMap.put("日期", new SimpleDateFormat("yyyy-MM-dd").format(subjectInfo.getStartDate()));
                String time = new SimpleDateFormat("HH:mm").format(subjectInfo.getStartDate()) + "-" + new SimpleDateFormat("HH:mm").format(subjectInfo.getEndDate());
                templateMap.put("时段", time);
                templateMap.put("巡考老师", teacherNamesMap.get(subjectInfo.getSubjectId()));
                templateList.add(templateMap);
            }
        }
        List<String> titleList = getRowTitleList();//表头
        Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
        sheetName2RecordListMap.put(getObjectName(), templateList);
        Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
        titleMap.put(getObjectName(), titleList);
        ExportUtils exportUtils = ExportUtils.newInstance();
        exportUtils.exportXLSFile("巡考老师导入", titleMap, sheetName2RecordListMap, response);
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
