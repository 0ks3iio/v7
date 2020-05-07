package net.zdsoft.exammanage.data.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.exammanage.data.constant.ExammanageConstants;
import net.zdsoft.exammanage.data.entity.*;
import net.zdsoft.exammanage.data.service.*;
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
@RequestMapping("/exammanage/invigilateImport")
public class InvigilateTeacherImportAction extends DataImportAction {

    private Logger logger = Logger.getLogger(InvigilateTeacherImportAction.class);

    private String examCode;

    @Autowired
    private EmPlaceService emPlaceService;
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
        map.put("businessName", "监考老师设置");
        // 导入URL
        map.put("businessUrl", "/exammanage/invigilateImport/import");
        // 导入模板
        map.put("templateDownloadUrl", "/exammanage/invigilateImport/template");
        // 导入对象
        map.put("objectName", "");
        // 导入说明
        StringBuffer description = new StringBuffer();
        description.append(getDescription());

        // 如果导入文件中前面有说明性文字的  这里需要传一个有效数据开始行（列名那一行）
        //如果列名在第一行的就不需要传
        map.put("validRowStartNo", 0);
        //模板校验
        map.put("validateUrl", "/exammanage/invigilateImport/validate");

        map.put("description", description);
        map.put("businessKey", UuidUtils.generateUuid());

        JSONObject obj = new JSONObject();
        obj.put("examId", examId);

        obj.put("unitId", unitId);
        map.put("monthPermance", JSON.toJSONString(obj));
        map.put("examId", examId);
        return "/exammanage/importFtl/invigilateImport.ftl";
    }


    @Override
    public String getObjectName() {
        return "invigilateTeacherFilter";
    }

    @Override
    public String getDescription() {
        return "<h4 class='box-graybg-title'>说明</h4>"
                + "<p>1、模板中列出的考试信息请勿修改</p>"
                + "<p>2、请填写监考老师的姓名，多位老师以逗号分隔</p>"
                + "<p>3、同一科目考试监考老师不能重复</p>"
                + "<p>4、同一科目考试监考和巡考老师不能重复</p>";
    }

    @Override
    public List<String> getRowTitleList() {
        //下载文件表头
        List<String> tis = new ArrayList<String>();
        tis.add("考场编号");
        tis.add("考试场地");
        tis.add("科目");
        tis.add("考试日期");
        tis.add("时段");
        tis.add("监考老师");
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
            errorData[1] = "考场编号";
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
            errorData[1] = "考场编号";
            errorData[2] = "";
            errorData[3] = "该考试不存在";
            errorDataList.add(errorData);
            return result(datas.size() - 1, 0, 0, errorDataList);
        }
        Map<String, String> inspectorsMap = new LinkedHashMap<String, String>();
        List<EmPlaceTeacher> inspectorsTeacherList = emPlaceTeacherService.findByExamIdAndType(examId, ExammanageConstants.TEACHER_TYPE2);
        if (CollectionUtils.isNotEmpty(inspectorsTeacherList)) {
            for (EmPlaceTeacher emPlaceTeacher : inspectorsTeacherList) {
                String inspectorsTeacherName = "";
                if (StringUtils.isNotBlank(emPlaceTeacher.getTeacherIdsIn())) {
                    inspectorsTeacherName += emPlaceTeacher.getTeacherIdsIn() + ",";
                }
                if (StringUtils.isNotBlank(emPlaceTeacher.getTeacherIdsOut())) {
                    inspectorsTeacherName += emPlaceTeacher.getTeacherIdsOut() + ",";
                }
                inspectorsMap.put(emPlaceTeacher.getSubjectId(), inspectorsTeacherName);
            }
        }
        Map<String, String> placeNameMap = new LinkedHashMap<String, String>();
        Map<String, String> placeCodeMap = new LinkedHashMap<String, String>();
        //考场信息
        List<EmPlace> emPlaceList = emPlaceService.findByExamIdAndSchoolIdWithMaster(examId, unitId, true);
        for (EmPlace emPlace : emPlaceList) {
            placeCodeMap.put(emPlace.getExamPlaceCode(), emPlace.getPlaceName());
            placeNameMap.put(emPlace.getPlaceName(), emPlace.getId());
        }

        Map<String, EmSubjectInfo> subMap = new HashMap<String, EmSubjectInfo>();
        //考试科目信息
        List<EmSubjectInfo> findSByExamIdIn = emSubjectInfoService.findByExamId(examId);
        for (EmSubjectInfo emSubjectInfo : findSByExamIdIn) {
            subMap.put(emSubjectInfo.getSubjectId(), emSubjectInfo);
        }
        //学科名称Map
        Map<String, String> courseIdsMap = getCoursesMap(examId, findSByExamIdIn, false);

        Map<String, String> teacherInsMap = new LinkedHashMap<String, String>();
        Map<String, String> teacherOutsMap = new LinkedHashMap<String, String>();
        List<Teacher> teacherInLists = SUtils.dt(teacherRemoteService.findByUnitId(unitId), new TR<List<Teacher>>() {
        });
        List<EmOutTeacher> teacherOutLists = emOutTeacherService.findByExamIdAndSchoolId(examId, unitId);
        if (CollectionUtils.isNotEmpty(teacherOutLists)) {
            for (EmOutTeacher emOutTeacher : teacherOutLists) {
                teacherOutsMap.put(emOutTeacher.getTeacherName(), emOutTeacher.getId());
            }
        }
        if (CollectionUtils.isNotEmpty(teacherInLists)) {
            for (Teacher teacher : teacherInLists) {
                teacherInsMap.put(teacher.getTeacherName(), teacher.getId());
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
                errorData[1] = "考场编号";
                errorData[2] = "";
                errorData[3] = "不能为空";
                errorDataList.add(errorData);
                continue;
            }

            if (StringUtils.isBlank(arr[1])) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "考试场地";
                errorData[2] = "";
                errorData[3] = "不能为空";
                errorDataList.add(errorData);
                continue;
            }

            if (StringUtils.isBlank(arr[2])) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "科目";
                errorData[2] = "";
                errorData[3] = "不能为空";
                errorDataList.add(errorData);
                continue;
            }

            if (StringUtils.isBlank(arr[5])) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "监考老师";
                errorData[2] = "";
                errorData[3] = "不能为空";
                errorDataList.add(errorData);
                continue;
            }

            if (!placeCodeMap.containsKey(arr[0])) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "考场编号";
                errorData[2] = "";
                errorData[3] = "不存在";
                errorDataList.add(errorData);
                continue;
            }

            if (!placeNameMap.containsKey(arr[1])) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "考试场地";
                errorData[2] = "";
                errorData[3] = "不存在";
                errorDataList.add(errorData);
                continue;
            }

            if (!courseIdsMap.containsKey(arr[2])) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "科目";
                errorData[2] = "";
                errorData[3] = "不存在";
                errorDataList.add(errorData);
                continue;
            }

            if (!placeCodeMap.get(arr[0]).equals(arr[1])) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "考场编号";
                errorData[2] = "";
                errorData[3] = "考场编号和考试场地不一致";
                errorDataList.add(errorData);
                continue;
            }

            String[] teacherNames = arr[5].replaceAll("，", ",").split(",");
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
                errorData[1] = "监考老师";
                errorData[2] = wrongName1;
                errorData[3] = "老师不存在";
                errorDataList.add(errorData);
                continue;
            }

            if (teacherNamesSet.size() != teacherNames.length) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "监考老师";
                errorData[2] = wrongName1;
                errorData[3] = "老师不能重复";
                errorDataList.add(errorData);
                continue;
            }

            String subjectId = courseIdsMap.get(arr[2]);
            String invigilateNames = "";
            for (EmPlaceTeacher placeTeacher : invigilateList) {
                if (placeTeacher.getSubjectId().equals(subjectId)) {
                    if (StringUtils.isNotBlank(placeTeacher.getTeacherIdsIn())) {
                        invigilateNames += placeTeacher.getTeacherIdsIn() + ",";
                    }
                    if (StringUtils.isNotBlank(placeTeacher.getTeacherIdsOut())) {
                        invigilateNames += placeTeacher.getTeacherIdsOut() + ",";
                    }
                }
            }

            String teacherInIds = "";
            String teacherOutIds = "";
            boolean haveinspectors = false;
            boolean haveinvigilate = false;
            String wrongName2 = "";
            for (String str : teacherNames) {
                String inspectorsNames = inspectorsMap.get(subjectId);
                if (teacherInsMap.containsKey(str)) {
                    if (StringUtils.isNotBlank(invigilateNames)) {
                        if (invigilateNames.indexOf(teacherInsMap.get(str)) != -1) {
                            wrongName2 = str;
                            haveinvigilate = true;
                            break;
                        }
                    }
                    if (StringUtils.isNotBlank(inspectorsNames)) {
                        if (inspectorsNames.indexOf(teacherInsMap.get(str)) != -1) {
                            wrongName2 = str;
                            haveinspectors = true;
                            break;
                        }
                    }
                    teacherInIds += teacherInsMap.get(str) + ",";
                }
                if (teacherOutsMap.containsKey(str)) {
                    if (StringUtils.isNotBlank(invigilateNames)) {
                        if (invigilateNames.indexOf(teacherOutsMap.get(str)) != -1) {
                            wrongName2 = str;
                            haveinvigilate = true;
                            break;
                        }
                    }
                    if (StringUtils.isNotBlank(inspectorsNames)) {
                        if (inspectorsNames.indexOf(teacherOutsMap.get(str)) != -1) {
                            wrongName2 = str;
                            haveinspectors = true;
                            break;
                        }
                    }
                    teacherOutIds += teacherOutsMap.get(str) + ",";
                }
            }
            if (haveinvigilate) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "监考老师";
                errorData[2] = wrongName2;
                errorData[3] = "已设置该老师为当前时间段其他考场的监考老师";
                errorDataList.add(errorData);
                continue;
            }

            if (haveinspectors) {
                errorData[0] = String.valueOf(sequence);
                errorData[1] = "监考老师";
                errorData[2] = wrongName2;
                errorData[3] = "已设置该老师为当前时间段的巡考老师";
                errorDataList.add(errorData);
                continue;
            }


            successCount++;
            emPlaceTeacher = new EmPlaceTeacher();
            emPlaceTeacher.setUnitId(unitId);
            emPlaceTeacher.setExamId(examId);
            emPlaceTeacher.setExamPlaceId(placeNameMap.get(arr[1]));
            if (StringUtils.isNotBlank(teacherInIds)) {
                teacherInIds = teacherInIds.substring(0, teacherInIds.length() - 1);
                emPlaceTeacher.setTeacherIdsIn(teacherInIds);
            }
            if (StringUtils.isNotBlank(teacherOutIds)) {
                teacherOutIds = teacherOutIds.substring(0, teacherOutIds.length() - 1);
                emPlaceTeacher.setTeacherIdsOut(teacherOutIds);
            }
            emPlaceTeacher.setSubjectId(subjectId);
            emPlaceTeacher.setStartTime(subMap.get(subjectId).getStartDate());
            emPlaceTeacher.setEndTime(subMap.get(subjectId).getEndDate());
            emPlaceTeacher.setType(ExammanageConstants.TEACHER_TYPE1);
            invigilateList.add(emPlaceTeacher);
        }

        try {
            if (CollectionUtils.isNotEmpty(invigilateList)) {
                emPlaceTeacherService.saveAllAndDel(examId, invigilateList, ExammanageConstants.TEACHER_TYPE1);
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
        String examId = examCode;
        //考场信息
        List<EmPlace> emPlaceList = emPlaceService.findByExamIdAndSchoolIdWithMaster(examId, unitId, true);
        //考试科目信息
        List<EmSubjectInfo> findSByExamIdIn = emSubjectInfoService.findByExamId(examId);
        //学科名称Map
        Map<String, String> courseNamesMap = getCoursesMap(examId, findSByExamIdIn, true);

        List<EmPlaceTeacher> teacherList = emPlaceTeacherService.findByExamIdAndType(examId, ExammanageConstants.TEACHER_TYPE1);
        Map<String, EmPlaceTeacher> teachersMap = new LinkedHashMap<String, EmPlaceTeacher>();
        if (CollectionUtils.isNotEmpty(teacherList)) {
            Map<String, String> teacherInsMap = new LinkedHashMap<String, String>();
            Map<String, String> teacherOutsMap = new LinkedHashMap<String, String>();
            List<Teacher> teacherInLists = SUtils.dt(teacherRemoteService.findByUnitId(unitId), new TR<List<Teacher>>() {
            });
            List<EmOutTeacher> teacherOutLists = emOutTeacherService.findByExamIdAndSchoolId(examId, unitId);
            if (CollectionUtils.isNotEmpty(teacherOutLists)) {
                for (EmOutTeacher emOutTeacher : teacherOutLists) {
                    teacherOutsMap.put(emOutTeacher.getId(), emOutTeacher.getTeacherName());
                }
            }
            if (CollectionUtils.isNotEmpty(teacherInLists)) {
                for (Teacher teacher : teacherInLists) {
                    teacherInsMap.put(teacher.getId(), teacher.getTeacherName());
                }
            }
            Iterator<EmPlaceTeacher> it = teacherList.iterator();
            while (it.hasNext()) {
                EmPlaceTeacher teacher = it.next();
                if (StringUtils.isNotBlank(teacher.getTeacherIdsIn())) {
                    String[] teacherIdIns = teacher.getTeacherIdsIn().split(",");
                    String teacherNameIns = "";
                    for (int i = 0; i < teacherIdIns.length; i++) {
                        teacherNameIns = teacherNameIns + teacherInsMap.get(teacherIdIns[i]) + ",";
                    }
                    teacherNameIns = teacherNameIns.substring(0, teacherNameIns.length() - 1);
                    teacher.setTeacherInNames(teacherNameIns);
                }
                if (StringUtils.isNotBlank(teacher.getTeacherIdsOut())) {
                    String[] teacherIdOuts = teacher.getTeacherIdsOut().split(",");
                    String teacherNameOuts = "";
                    for (int i = 0; i < teacherIdOuts.length; i++) {
                        teacherNameOuts = teacherNameOuts + teacherOutsMap.get(teacherIdOuts[i]) + ",";
                    }
                    teacherNameOuts = teacherNameOuts.substring(0, teacherNameOuts.length() - 1);
                    teacher.setTeacherOutNames(teacherNameOuts);
                }
                if (StringUtils.isNotBlank(teacher.getTeacherIdsIn()) || StringUtils.isNotBlank(teacher.getTeacherIdsOut())) {
                    teachersMap.put(teacher.getExamPlaceId() + teacher.getSubjectId(), teacher);
                }
            }
        }
        List<Map<String, String>> templateList = new ArrayList<Map<String, String>>();
        Map<String, String> templateListMap = null;
        EmPlaceTeacher teacher = null;
        if (CollectionUtils.isNotEmpty(findSByExamIdIn) && CollectionUtils.isNotEmpty(emPlaceList)) {
            for (EmSubjectInfo emSubjectInfo : findSByExamIdIn) {
                for (EmPlace emPlace : emPlaceList) {
                    templateListMap = new LinkedHashMap<String, String>();
                    templateListMap.put("考场编号", emPlace.getExamPlaceCode());
                    templateListMap.put("考试场地", emPlace.getPlaceName());
                    templateListMap.put("科目", courseNamesMap.get(emSubjectInfo.getSubjectId()));
                    templateListMap.put("考试日期", new SimpleDateFormat("yyyy-MM-dd").format(emSubjectInfo.getStartDate()));
                    String time = new SimpleDateFormat("HH:mm").format(emSubjectInfo.getStartDate()) + "-" + new SimpleDateFormat("HH:mm").format(emSubjectInfo.getEndDate());
                    templateListMap.put("时段", time);
                    teacher = teachersMap.get(emPlace.getId() + emSubjectInfo.getSubjectId());
                    if (teacher != null) {
                        if (StringUtils.isNotBlank(teacher.getTeacherIdsIn()) && StringUtils.isNotBlank(teacher.getTeacherIdsOut())) {
                            templateListMap.put("监考老师", teacher.getTeacherInNames() + "," + teacher.getTeacherOutNames());
                        } else if (StringUtils.isNotBlank(teacher.getTeacherIdsIn()) && StringUtils.isBlank(teacher.getTeacherIdsOut())) {
                            templateListMap.put("监考老师", teacher.getTeacherInNames());
                        } else if (StringUtils.isBlank(teacher.getTeacherIdsIn()) && StringUtils.isNotBlank(teacher.getTeacherIdsOut())) {
                            templateListMap.put("监考老师", teacher.getTeacherOutNames());
                        }
                    }
                    templateList.add(templateListMap);
                }
            }
        }

        List<String> titleList = getRowTitleList();//表头
        Map<String, List<Map<String, String>>> sheetName2RecordListMap = new HashMap<String, List<Map<String, String>>>();
        sheetName2RecordListMap.put(getObjectName(), templateList);
        Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
        titleMap.put(getObjectName(), titleList);
        ExportUtils exportUtils = ExportUtils.newInstance();
        exportUtils.exportXLSFile("监考老师导入", titleMap, sheetName2RecordListMap, response);
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

    public Map<String, String> getCoursesMap(String examId, List<EmSubjectInfo> findSByExamIdIn, boolean courseId) {
        Map<String, String> courseNamesMap = new LinkedHashMap<String, String>();
        Map<String, String> courseIdsMap = new LinkedHashMap<String, String>();
        if (CollectionUtils.isNotEmpty(findSByExamIdIn)) {
            for (EmSubjectInfo emSubjectInfo : findSByExamIdIn) {
                courseNamesMap.put(emSubjectInfo.getSubjectId(), emSubjectInfo.getCourseName());
                courseIdsMap.put(emSubjectInfo.getCourseName(), emSubjectInfo.getSubjectId());
            }
        }
        if (courseId) {
            return courseNamesMap;
        } else {
            return courseIdsMap;
        }
    }
}
