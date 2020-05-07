package net.zdsoft.diathesis.data.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.diathesis.data.constant.DiathesisConstant;
import net.zdsoft.diathesis.data.dto.*;
import net.zdsoft.diathesis.data.entity.*;
import net.zdsoft.diathesis.data.service.*;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.JsonArray;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.Objects;
import net.zdsoft.framework.utils.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Date: 2019/04/02
 * 学业水平
 */
@Controller
@RequestMapping("/diathesis")
public class DiathesisScholasticAction extends DataImportAction {

    @Autowired
    private DiathesisSetSubjectService diathesisSetSubjectService;
    @Autowired
    private DiathesisSubjectFieldService diathesisSubjectFieldService;
    @Autowired
    private DiathesisScoreInfoExService diathesisScoreInfoExService;
    @Autowired
    private DiathesisScoreTypeService diathesisScoreTypeService;
    @Autowired
    private DiathesisScoreInfoService diathesisScoreInfoService;
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private CourseRemoteService courseRemoteService;
    @Autowired
    private SemesterRemoteService semesterRemoteService;

    @RequestMapping("/scholastic/classList")
    @ResponseBody
    public String getClassList(String gradeId) {
        if (StringUtils.isBlank(gradeId)) {
            return error("未选择年级");
        }
        JSONObject jsonObject = new JSONObject();
        String unitId = getLoginInfo().getUnitId();
        // 班级列表
        List<Clazz> classList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(unitId, gradeId), Clazz.class);
        List<DiathesisClassDto> classDtoList = classList.stream().map(e -> {
            DiathesisClassDto tmp = new DiathesisClassDto();
            tmp.setClassId(e.getId());
            if (StringUtils.isNotBlank(e.getClassNameDynamic())) {
                tmp.setClassName(e.getClassNameDynamic());
            } else {
                tmp.setClassName(e.getClassName());
            }
            return tmp;
        }).collect(Collectors.toList());
        jsonObject.put("classList", classDtoList);

        // 成绩列表
        List<DiathesisScoreType> diathesisScoreTypeList = diathesisScoreTypeService.findByUnitIdAndGradeIdAndType(unitId, gradeId, DiathesisConstant.INPUT_TYPE_2);
        List<DiathesisScoreTypeDto> diathesisScoreTypeDtoList = new ArrayList<>();
        for (DiathesisScoreType one : diathesisScoreTypeList) {
            DiathesisScoreTypeDto tmp = new DiathesisScoreTypeDto();
            tmp.setScoreId(one.getId());
            tmp.setScoreName(one.getExamName());
            diathesisScoreTypeDtoList.add(tmp);
        }
        jsonObject.put("scholasticList", diathesisScoreTypeDtoList);
        return jsonObject.toJSONString();
    }

    @RequestMapping("/scholastic/gradeList")
    @ResponseBody
    public String getGradeList() {
        String unitId = getLoginInfo().getUnitId();
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId, new Integer[]{BaseConstants.SECTION_PRIMARY, BaseConstants.SECTION_JUNIOR, BaseConstants.SECTION_HIGH_SCHOOL}), Grade.class);
        List<DiathesisScoreType> diathesisScoreTypeList = diathesisScoreTypeService.findByUnitIdAndGradeIdAndType(unitId, null, DiathesisConstant.INPUT_TYPE_2);
        Map<String, List<DiathesisScoreTypeDto>> map = new HashMap<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (DiathesisScoreType one : diathesisScoreTypeList) {
            DiathesisScoreTypeDto tmp = new DiathesisScoreTypeDto();
            tmp.setScoreId(one.getId());
            tmp.setScoreName(one.getExamName());
            tmp.setImportTime(simpleDateFormat.format(one.getModifyTime()));
            if (map.get(one.getGradeId()) == null) {
                map.put(one.getGradeId(), new ArrayList<>());
            }
            map.get(one.getGradeId()).add(tmp);
        }
        List<DiathesisGradeDto> diathesisGradeDtoList = new ArrayList<>();
        for (Grade one : gradeList) {
            DiathesisGradeDto tmp = new DiathesisGradeDto();
            tmp.setGradeId(one.getId());
            tmp.setYear(one.getOpenAcadyear().substring(0, 4) + "级");
            tmp.setGradeName(one.getGradeName());
            tmp.setScoreList(map.get(one.getId()) == null ? new ArrayList<>() : map.get(one.getId()));
            diathesisGradeDtoList.add(tmp);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("gradeList", diathesisGradeDtoList);
        return jsonObject.toJSONString();
    }

    /**
     * 学业水平
     * @param request
     * @param gradeId
     * @param classId
     * @param studentCode
     * @param studentName
     * @return
     */
    @RequestMapping("/scholastic/detail")
    @ResponseBody
    public String getStudentDetail(HttpServletRequest request, String gradeId, String classId, String gradeCode,String semester, String studentCode, String studentName) {
        if (StringUtils.isBlank(gradeId) || StringUtils.isBlank(gradeCode) || StringUtils.isBlank(semester)) {
            return error("参数丢失");
        }
        String unitId = getLoginInfo().getUnitId();
        JSONObject json = new JSONObject();
        Pagination page = createPagination(request);
        Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);

        List<DiathesisSubjectField> fieldList = diathesisSubjectFieldService.findUsingFieldByUnitIdAndSubjectType(unitId, DiathesisConstant.SUBJECT_FEILD_XY);
        List<DiathesisSetSubject> subSetList = diathesisSetSubjectService.findByGradeIdAndGradeCodeAndSemesterAndType(gradeId, gradeCode, Integer.parseInt(semester), DiathesisConstant.SUBJECT_FEILD_XY);
        if(CollectionUtils.isEmpty(subSetList))return json.toJSONString();
        List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(EntityUtils.getArray(subSetList, x->x.getSubjectId(), String[]::new)), Course.class);

        List<DiathesisFieldDto> fieldResultList = getFieldList(fieldList);
        json.put("fieldList",fieldResultList);

        List<DiatheisSubjectScoreDto> title = courseList.stream().map(x -> {
            DiatheisSubjectScoreDto subDto = new DiatheisSubjectScoreDto();
            subDto.setSubjectId(x.getId());
            subDto.setSubjectName(x.getSubjectName());

            subDto.setFieldList(getFieldList(fieldList));
            return subDto;
        }).collect(Collectors.toList());

        json.put("title",title);


        DiathesisScoreType diathesisScoreType = diathesisScoreTypeService.findByGradeIdAndTypeAndScoreTypeAndGradeCodeAndSemesterAndExamName(gradeId, DiathesisConstant.INPUT_TYPE_2, BaseConstants.SUBJECT_TYPE_BX, gradeCode, semester, null);
        if(diathesisScoreType==null) return json.toJSONString();

        List<Clazz> classList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(unitId, gradeId), Clazz.class);
        Map<String, String> classNameMap = classList.stream()
                .collect(Collectors.toMap(e -> e.getId(), e -> (e.getClassNameDynamic() == null ? e.getClassName() : e.getClassNameDynamic())));

        List<DiathesisScoreInfo> diathesisScoreInfoList = diathesisScoreInfoService.findByTypeAndScoreTypeId(DiathesisConstant.SCORE_TYPE_1, diathesisScoreType.getId());
        List<DiathesisScoreInfoEx>infoExList=diathesisScoreInfoExService.findListByScoreTypeId(diathesisScoreType.getId());



        List<Student> students;
        if (StringUtils.isNotBlank(classId)) {
            students = SUtils.dt(studentRemoteService.findByClassIds(new String[]{classId}), Student.class);
        } else {
            students = SUtils.dt(studentRemoteService.findByGradeIds(new String[]{gradeId}), Student.class);
        }
        if (StringUtils.isNotBlank(studentCode)) {
            students = students.stream().filter(e -> e.getStudentCode() != null && e.getStudentCode().indexOf(studentCode) > -1).collect(Collectors.toList());
        }
        if (StringUtils.isNotBlank(studentName)) {
            students = students.stream().filter(e -> e.getStudentName() != null && e.getStudentName().indexOf(studentName) > -1).collect(Collectors.toList());
        }

        // 分页
        page.setMaxRowCount(students.size());
        int end = page.getPageIndex() * page.getPageSize();
        students = students.subList((page.getPageIndex() - 1) * page.getPageSize(), end > students.size() ? students.size() : end);
        page.initialize();


        Map<String, String> infoExMap = EntityUtils.getMap(infoExList, x -> x.getScoreInfoId() + "_" + x.getFieldCode(), x -> x.getFieldValue());

        Map<String, String> scoreMap = new HashMap<>();
        for (DiathesisScoreInfo info : diathesisScoreInfoList) {
            for (DiathesisFieldDto field : fieldResultList) {
                String score = infoExMap.get(info.getId() + "_" + field.getFieldCode());
                scoreMap.put(info.getStuId() + "_" + info.getObjId() + "_" + field.getFieldCode(), score);
            }
        }
        List<DiathesisStuScoreDto> result = new ArrayList<>();
        //必修
        for (Student one : students) {
            DiathesisStuScoreDto temp = new DiathesisStuScoreDto();
            temp.setStuName(one.getStudentName());
            temp.setStuCode(one.getStudentCode());
            temp.setClazz(classNameMap.get(one.getClassId()));
            temp.setSex(Integer.valueOf(1).equals(one.getSex()) ? "男" : "女");
            List<DiatheisSubjectScoreDto> subjectList = new ArrayList<>();
            for (Course sub : courseList) {
                DiatheisSubjectScoreDto subDto = new DiatheisSubjectScoreDto();
                subDto.setSubjectId(sub.getId());
                subDto.setSubjectName(sub.getSubjectName());
                List<DiathesisFieldDto> fields = new ArrayList<>();
                for (DiathesisFieldDto field : fieldResultList) {
                    DiathesisFieldDto fieldScoreDto = new DiathesisFieldDto();
                    String score = scoreMap.get(one.getId() + "_" + sub.getId() + "_" + field.getFieldCode());
                    fieldScoreDto.setFieldName(field.getFieldName());
                    fieldScoreDto.setValue(score == null ? "" : score);
                    fields.add(fieldScoreDto);
                }
                subDto.setFieldList(fields);
                subjectList.add(subDto);
            }
            temp.setSubjectList(subjectList);
            result.add(temp);
        }
        json.put("stuList",result);
        json.put("page", page);
        return json.toJSONString();
    }

    private List<DiathesisFieldDto> getFieldList(List<DiathesisSubjectField> fieldList) {
        return fieldList.stream().map(x -> {
                DiathesisFieldDto fieldDto = new DiathesisFieldDto();
                fieldDto.setFieldId(x.getId());
                fieldDto.setFieldCode(x.getFieldCode());
                fieldDto.setFieldName(x.getFieldName());
                return fieldDto;
            }).collect(Collectors.toList());
    }

    @RequestMapping("/scholastic/delete")
    @ResponseBody
    public String scholasticDelete(String scoreId) {
        if (StringUtils.isBlank(scoreId)) {
            return error("参数丢失");
        }
        try {
            diathesisScoreTypeService.deleteById(scoreId);
        } catch (Exception e) {
            e.printStackTrace();
            return error("删除失败");
        }
        return success("删除成功");
    }

    // ==========================================================
    // 学业水平导入逻辑

    @RequestMapping("/scholastic/execute")
    @ResponseBody
    public String execute() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("businessKey", UuidUtils.generateUuid());
        return jsonObject.toJSONString();
    }

    @Override
    public String getObjectName() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public List<String> getRowTitleList() {
        return null;
    }

    public Map<String, List<String>> getRowTitleList(String gradeId,String gradeCode,String semester) {
        List<String> titleList = new ArrayList<>();
        String unitId = getLoginInfo().getUnitId();
        List<DiathesisSetSubject> setSub = diathesisSetSubjectService.findByGradeIdAndGradeCodeAndSemesterAndType(gradeId, gradeCode, Integer.parseInt(semester), DiathesisConstant.SUBJECT_FEILD_XY);
        String[] subjectIds = EntityUtils.getArray(setSub,x->x.getSubjectId(),String[]::new);
        List<DiathesisSubjectField> fieldList = diathesisSubjectFieldService.findUsingFieldByUnitIdAndSubjectType(unitId, DiathesisConstant.SUBJECT_FEILD_XY);
        List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIds), Course.class);
        titleList.add("学号");
        titleList.add("姓名");
        List<String> fieldImportList = new ArrayList<>();
        fieldImportList.add(null);
        fieldImportList.add(null);
        for (Course one : courseList) {
            for (int i = 0; i < fieldList.size(); i++) {
                titleList.add(i == 0 ? one.getSubjectName() : null);
                fieldImportList.add(fieldList.get(i).getFieldName());
            }
        }
        HashMap<String, List<String>> map = new HashMap<>();
        map.put("first", titleList);
        map.put("second", fieldImportList);
        return map;
    }


    @Override
    @RequestMapping("/scholastic/import")
    @ResponseBody
    public String dataImport(String filePath, String params) {
        params = params.replace("&quot;", "\"");
        JSONObject jsStr = JSONObject.parseObject(params);
        JSONObject jsonObject = new JSONObject();
        String unitId = getLoginInfo().getUnitId();
        String gradeId = jsStr.getString("gradeId");
        String gradeCode = jsStr.getString("gradeCode");
        String semesterStr = jsStr.getString("semester");
        if(StringUtils.isBlank(gradeId)|| StringUtils.isBlank(gradeCode)|| StringUtils.isBlank(semesterStr)){
            return error("参数缺数");
        }
        //防止并发 导入重复数据  用redis锁控制
        RedisUtils.hasLocked("diathesis_scholastic_import_" + unitId+gradeId+gradeCode+semesterStr + "_78,");

        try {
            Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
            Semester semester;
            semester = new Semester();
            String[] acadyearTmp = grade.getOpenAcadyear().split("-");
            String acadyear = (Integer.valueOf(acadyearTmp[0]) + Integer.valueOf(gradeCode.substring(1)) - 1) + "-" + (Integer.valueOf(acadyearTmp[1]) + Integer.valueOf(gradeCode.substring(1)) - 1);
            semester.setAcadyear(acadyear);
            semester.setSemester(Integer.valueOf(semesterStr));

            List<String[]> datas = ExcelUtils.readExcelIgnoreDesc(filePath, getRowTitleList(gradeId,gradeCode,semesterStr).get("first").size());

            List<DiathesisSubjectField> fieldList = diathesisSubjectFieldService.findUsingFieldByUnitIdAndSubjectType(unitId, DiathesisConstant.SUBJECT_FEILD_XY);
            Map<String, String> fieldNameToCode = EntityUtils.getMap(fieldList, x -> x.getFieldName(), x -> x.getFieldCode());

       /* // 获取学业水平名称
        if (StringUtils.isBlank(datas.get(1)[1])) {
            datas.remove(0);
        }
        String headTitle = datas.get(0)[0];
        datas.remove(0);*/

            // 获取标题行
            List<String> titleList = new ArrayList<>();
            List<String> fieldTypeList = new ArrayList<>();
            CollectionUtils.addAll(titleList, datas.get(0));
            CollectionUtils.addAll(fieldTypeList, datas.get(1));
            datas.remove(0);
            datas.remove(0);

            List<String[]> errorDataList=new ArrayList<>();
            if (CollectionUtils.isEmpty(datas)){
                return error("没有导入数据");
            }

            List<Course> courseList = SUtils.dt(courseRemoteService.findByUnitIdAndTypeAndLikeSection(unitId, BaseConstants.SUBJECT_TYPE_BX, gradeCode.substring(0, 1)), Course.class);
            courseList = courseList.stream()
                    .filter(e -> BaseConstants.SUBJECT_TYPE_BX.equals(e.getType()))
                    .collect(Collectors.toList());
            Map<String,String> courseNameToIdMap = courseList.stream().collect(Collectors.toMap(Course::getSubjectName, Course::getId));

            // 学生
            Set<String> studentCodeSet = new HashSet<>();
            for (String[] arr : datas){
                if (StringUtils.isNotBlank(arr[0])){
                    studentCodeSet.add(arr[0]);
                }
            }
            List<Student> studentList;
            Map<String, Student> studentMap = new HashMap<String, Student>();
            // 考虑学号重复问题
            Map<String, Set<String>> studentCodeToIdMap = new HashMap<String, Set<String>>();
            Set<String> classIdSet = new HashSet<>();
            if (CollectionUtils.isNotEmpty(studentCodeSet)){
                studentList = SUtils.dt(studentRemoteService.findBySchIdStudentCodes(unitId, studentCodeSet.toArray(new String[0])), new TR<List<Student>>() {});
                for (Student one : studentList){
                    studentMap.put(one.getId(), one);
                    if (!studentCodeToIdMap.containsKey(one.getStudentCode())){
                        studentCodeToIdMap.put(one.getStudentCode(), new HashSet<String>());
                    }
                    studentCodeToIdMap.get(one.getStudentCode()).add(one.getId());
                    classIdSet.add(one.getClassId());
                }
            }

            List<Clazz> classList;
            Map<String, String> classIdToGradeIdMap = new HashMap<String, String>();
            if (CollectionUtils.isNotEmpty(classIdSet)){
                classList = SUtils.dt(classRemoteService.findListByIds(classIdSet.toArray(new String[0])), new TR<List<Clazz>>(){});
                for (Clazz one : classList){
                    classIdToGradeIdMap.put(one.getId(), one.getGradeId());
                }
            }

            // 原有成绩信息
            //repeat
            Map<String,DiathesisScoreInfo> oldDiathesisScoreInfoMap =new HashMap<>();
            Map<String, DiathesisScoreInfoEx> oldScoreExMap =new HashMap<>();
            DiathesisScoreType diathesisScoreType = diathesisScoreTypeService.findByGradeIdAndTypeAndScoreTypeAndGradeCodeAndSemesterAndExamName(grade.getId(), DiathesisConstant.INPUT_TYPE_2, DiathesisConstant.SCORE_TYPE_1, gradeCode, semester.getSemester().toString(), null);
            if (diathesisScoreType == null) {
                diathesisScoreType = new DiathesisScoreType();
                diathesisScoreType.setId(UuidUtils.generateUuid());
                diathesisScoreType.setUnitId(unitId);
                diathesisScoreType.setGradeId(grade.getId());
                diathesisScoreType.setGradeCode(gradeCode);
                diathesisScoreType.setSemester(semester.getSemester());
                diathesisScoreType.setYear(grade.getOpenAcadyear().substring(0, 4));
                diathesisScoreType.setType(DiathesisConstant.INPUT_TYPE_2);
                diathesisScoreType.setScoreType(DiathesisConstant.SCORE_TYPE_1);
                diathesisScoreType.setExamName(grade.getGradeName() + semester.getAcadyear() + "学年第" + semester.getSemester() + "学期学业水平");
                diathesisScoreType.setModifyTime(new Date());
                diathesisScoreType.setOperator(getLoginInfo().getOwnerId());
            } else {
                List<DiathesisScoreInfo> diathesisScoreInfoList = diathesisScoreInfoService.findByTypeAndScoreTypeId(DiathesisConstant.SCORE_TYPE_1, diathesisScoreType.getId());
                if (CollectionUtils.isNotEmpty(diathesisScoreInfoList)) {
                    oldDiathesisScoreInfoMap = diathesisScoreInfoList.stream().collect(Collectors.toMap(e -> (e.getStuId() + "_" + e.getObjId()), e -> e));
                }
                List<DiathesisScoreInfoEx> infoExList=diathesisScoreInfoExService.findListByScoreTypeId(diathesisScoreType.getId());
                if (CollectionUtils.isNotEmpty(infoExList)) {
                    oldScoreExMap = infoExList.stream().collect(Collectors.toMap(e -> (e.getScoreInfoId() + "_" + e.getFieldCode()), e -> e));
                }
            }

            // 错误数据序列号
            int successCount=0;
            Set<String> arrangeStuId=new HashSet<>();
            List<DiathesisScoreInfo> insertList=new ArrayList<>();
            List<DiathesisScoreInfoEx> insertExList = new ArrayList<>();
            int index = 0;
            for (String[] arr : datas) {
                index++;
                String[] errorData = new String[4];
                String studentCodeData = arr[0] == null ? null : StringUtils.trim(arr[0]);
                String studentNameData = arr[1] == null ? null : StringUtils.trim(arr[1]);

                if (StringUtils.isBlank(studentCodeData)) {
                    errorData[0] = index + "";
                    errorData[1] = "学号";
                    errorData[2] = "";
                    errorData[3] = "不能为空";
                    errorDataList.add(errorData);
                    continue;
                }

                if (StringUtils.isBlank(studentNameData)) {
                    errorData[0] = index + "";
                    errorData[1] = "姓名";
                    errorData[2] = "";
                    errorData[3] = "不能为空";
                    errorDataList.add(errorData);
                    continue;
                }

                //判断学生是否存在
                Set<String> studentIdSet = studentCodeToIdMap.get(studentCodeData);
                Student student;
                if (CollectionUtils.isEmpty(studentIdSet)) {
                    errorData[0] = index + "";
                    errorData[1] = "学号";
                    errorData[2] = studentCodeData;
                    errorData[3] = "学号不存在";
                    errorDataList.add(errorData);
                    continue;
                } else {
                    List<Student> sameCodeAndNameList = new ArrayList<>();
                    for (String one : studentIdSet) {
                        if (studentMap.get(one).getStudentName() != null && studentMap.get(one).getStudentName().equals(studentNameData)) {
                            sameCodeAndNameList.add(studentMap.get(one));
                        }
                    }
                    if (CollectionUtils.isEmpty(sameCodeAndNameList)) {
                        errorData[0] = index + "";
                        errorData[1] = "姓名";
                        errorData[2] = studentNameData;
                        errorData[3] = "学号所对应的学生姓名错误";
                        errorDataList.add(errorData);
                        continue;
                    }
                    // 判断是不是该年级下的
                    List<Student> sameCodeAndNameInGrade = new ArrayList<>();
                    for (Student one : sameCodeAndNameList){
                        if (gradeId.equals(classIdToGradeIdMap.get(one.getClassId()))) {
                            sameCodeAndNameInGrade.add(one);
                        }
                    }
                    if (CollectionUtils.isEmpty(sameCodeAndNameInGrade)) {
                        errorData[0] = index + "";
                        errorData[1] = "姓名";
                        errorData[2] = studentNameData;
                        errorData[3] = "该学生不属于" + grade.getGradeName() + "年级";
                        errorDataList.add(errorData);
                        continue;
                    }
                    if (sameCodeAndNameInGrade.size() > 1) {
                        errorData[0] = index + "";
                        errorData[1] = "姓名";
                        errorData[2] = studentNameData;
                        errorData[3] = grade.getGradeName() + "年级下存在多个匹配学号姓名的学生";
                        errorDataList.add(errorData);
                        continue;
                    }
                    student = sameCodeAndNameInGrade.get(0);
                }
                if (student == null) {
                    continue;
                }

                // 保存成绩
                boolean flag = true;
                Map<String, String> scoreMap = new HashMap<>();
                Pattern pattern = Pattern.compile("^[A-D]$"); // 合格性考试  等级性考试  操作测试

                for (int i=2; i < titleList.size(); i++) {
                    String subjectName = getSubjectName(titleList, i);
                    String fieldName=fieldTypeList.get(i);
                    String scoreStr = arr[i] == null ? null : StringUtils.trim(arr[i]);
                    if (StringUtils.isBlank(scoreStr)) {
                        continue;
                    }
                    // 判断输入成绩是否正确
                    if (!pattern.matcher(scoreStr).matches()) {
                        errorData = new String[4];
                        errorData[0] = index + "";
                        errorData[1] = titleList.get(i);
                        errorData[2] = scoreStr;
                        errorData[3] = "请输入正确等第";
                        errorDataList.add(errorData);
                        flag = false;
                        break;
                    }

                    scoreMap.put(courseNameToIdMap.get(subjectName)+"__"+fieldNameToCode.get(fieldName), scoreStr);
                }
                if (!flag) {
                    continue;
                }
                if (arrangeStuId.contains(student.getId())) {
                    errorData[0] = index + "";
                    errorData[1] = "姓名";
                    errorData[2] = studentNameData;
                    errorData[3] = "之前已有该学生成绩，重复录入";
                    errorDataList.add(errorData);
                    continue;
                }

                //学生科目成绩
                for (Map.Entry<String, String> item : scoreMap.entrySet()) {
                    String[] s = item.getKey().split("__");
                    String subId = s[0];
                    String fieldCode = s[1];
                    DiathesisScoreInfo tmp = null;
                    if (oldDiathesisScoreInfoMap != null && oldDiathesisScoreInfoMap.size() > 0) {
                        tmp = oldDiathesisScoreInfoMap.get(student.getId() + "_" + subId);
                    }
                    if (tmp == null){
                        tmp = new DiathesisScoreInfo();
                        tmp.setId(UuidUtils.generateUuid());
                        tmp.setUnitId(unitId);
                        tmp.setType(DiathesisConstant.SCORE_TYPE_1);
                        tmp.setScoreTypeId(diathesisScoreType.getId());
                        tmp.setStuId(student.getId());
                        tmp.setObjId(subId);
                        //tmp.setScore(item.getValue());
                        tmp.setModifyTime(new Date());
                        insertList.add(tmp);
                        oldDiathesisScoreInfoMap.put(student.getId() + "_" + subId,tmp);
                    }
                    DiathesisScoreInfoEx tmpEx = null;
                    if (oldScoreExMap != null && oldScoreExMap.size() > 0) {
                        tmpEx = oldScoreExMap.get(tmp.getId() + "_" + fieldCode);
                    }
                    if(tmpEx!=null){
                        tmpEx.setFieldValue(item.getValue());
                        tmpEx.setModifyTime(new Date());
                    }else{
                        tmpEx = new DiathesisScoreInfoEx();
                        tmpEx.setFieldCode(fieldCode);
                        tmpEx.setId(UuidUtils.generateUuid());
                        tmpEx.setScoreTypeId(diathesisScoreType.getId());
                        tmpEx.setScoreInfoId(tmp.getId());
                        tmpEx.setFieldValue(item.getValue());
                        tmpEx.setModifyTime(new Date());
                    }
                    insertExList.add(tmpEx);
                }
                arrangeStuId.add(student.getId());
                successCount++;
            }

            // 错误数据Excel导出
            String errorExcelPath = "";
            if (CollectionUtils.isNotEmpty(errorDataList)) {
                HSSFWorkbook workbook = new HSSFWorkbook();
                HSSFSheet sheet = workbook.createSheet();

                titleList.add("错误数据");
                titleList.add("错误原因");

                // HSSFCell----单元格样式
                HSSFCellStyle style = workbook.createCellStyle();
                style.setAlignment(HorizontalAlignment.LEFT);
                style.setVerticalAlignment(VerticalAlignment.CENTER);
                // 自动换行
                style.setWrapText(true);

                HSSFCellStyle errorStyle = workbook.createCellStyle();
                errorStyle.setAlignment(HorizontalAlignment.CENTER);
                errorStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                errorStyle.setWrapText(true);
                HSSFFont font = workbook.createFont();
                font.setColor(HSSFFont.COLOR_RED);
                errorStyle.setFont(font);

                CellRangeAddress RemarkCar = new CellRangeAddress(0,0,0,titleList.size()-1);
                sheet.addMergedRegion(RemarkCar);
                HSSFRow remarkRow = sheet.createRow(0);
                HSSFCell remarkCell = remarkRow.createCell(0);
                remarkCell.setCellValue(new HSSFRichTextString(getRemark()));
                remarkCell.setCellStyle(style);

            /*    HSSFRow rowTitle = sheet.createRow(1);
                for (int j=0; j < titleList.size(); j++) {
                    HSSFCell cell = rowTitle.createCell(j);
                    cell.setCellValue(new HSSFRichTextString(titleList.get(j)));
                    cell.setCellStyle(style);
                }
    */

                HSSFCellStyle mystyle = workbook.createCellStyle();
                mystyle.setBorderTop(BorderStyle.THIN);
                mystyle.setBorderBottom(BorderStyle.THIN);
                mystyle.setBorderLeft(BorderStyle.THIN);
                mystyle.setBorderRight(BorderStyle.THIN);
                mystyle.setTopBorderColor((short) 8);
                mystyle.setLeftBorderColor((short) 8);
                mystyle.setRightBorderColor((short) 8);
                mystyle.setBottomBorderColor((short) 8);
                mystyle.setAlignment(HorizontalAlignment.CENTER);
                mystyle.setVerticalAlignment(VerticalAlignment.CENTER);


                HSSFRow rowTitle = sheet.createRow(1);
                HSSFRow fieldTitle = sheet.createRow(2);
                fieldTypeList.add("");
                fieldTypeList.add("");
                for (int j = 0; j < titleList.size(); j++) {
                    HSSFCell cell = rowTitle.createCell(j);
                    cell.setCellValue(new HSSFRichTextString(titleList.get(j)));
                    cell.setCellStyle(mystyle);
                    HSSFCell cell1 = fieldTitle.createCell(j);
                    cell1.setCellValue(new HSSFRichTextString(fieldTypeList.get(j)));
                    cell1.setCellStyle(mystyle);

                    if(j==0 || j==1 || j==titleList.size()-1 || j==titleList.size()-2){
                        CellRangeAddress c = new CellRangeAddress(1, 2, j, j);
                        sheet.addMergedRegion(c);
                    }
                    if((j-2)%fieldList.size()==fieldList.size()){
                        CellRangeAddress c = new CellRangeAddress(1, 1, j, j-fieldList.size()+1);
                        sheet.addMergedRegion(c);
                    }
                }


                for (int i=0; i < errorDataList.size(); i++) {
                    HSSFRow row = sheet.createRow(i + 3);
                    String[] datasDetail = datas.get(Integer.parseInt(errorDataList.get(i)[0]) - 1);
                    for (int j=0; j < titleList.size(); j++) {
                        HSSFCell cell = row.createCell(j);
                        if (j < titleList.size() - 2) {
                            cell.setCellValue(new HSSFRichTextString(datasDetail[j]));
                            cell.setCellStyle(style);
                        } else if (j == titleList.size() - 2) {
                            cell.setCellValue(new HSSFRichTextString(errorDataList.get(i)[1]));
                            cell.setCellStyle(errorStyle);
                        } else {
                            cell.setCellValue(new HSSFRichTextString(errorDataList.get(i)[3]));
                            cell.setCellStyle(errorStyle);
                        }
                    }
                }
                errorExcelPath = saveErrorExcel(filePath, workbook);
            }

            try{
                diathesisScoreTypeService.save(diathesisScoreType, insertList,insertExList);

            } catch (Exception e) {
                e.printStackTrace();
                return error(e.getMessage());
            }

            jsonObject.put("totalCount", datas.size());
            jsonObject.put("successCount", successCount);
            jsonObject.put("errorCount", errorDataList.size());
            jsonObject.put("errorExcelPath", errorExcelPath);
        } finally {
            //防止并发 导入重复数据  用redis锁控制
            RedisUtils.unLock("diathesis_scholastic_import_" + unitId+gradeId+gradeCode+semesterStr + "_78,");
        }

        return jsonObject.toJSONString();
    }

    private String getSubjectName(List<String> titleList, int i) {
        for (int j = i; j >=0 ; j--) {
            if(StringUtils.isNotBlank(titleList.get(j)))return titleList.get(j);
        }
        return null;
    }

    @Override
    @RequestMapping("/scholastic/template")
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) {
        String unitId = getLoginInfo().getUnitId();
        String gradeId = request.getParameter("gradeId");
        String gradeCode = request.getParameter("gradeCode");
        String semester = request.getParameter("semester");
       // String subjects = request.getParameter("subjects");
        List<DiathesisSetSubject> setSub = diathesisSetSubjectService.findByGradeIdAndGradeCodeAndSemesterAndType(gradeId, gradeCode, Integer.parseInt(semester), DiathesisConstant.SUBJECT_FEILD_XY);

        String[] subjectIds = EntityUtils.getArray(setSub,x->x.getSubjectId(),String[]::new);

        String acadyear = diathesisScoreTypeService.getAcadyearBy(gradeId, gradeCode);
        Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();

        //样式--科目
        HSSFCellStyle mystyle = workbook.createCellStyle();
        mystyle.setBorderTop(BorderStyle.THIN);
        mystyle.setBorderBottom(BorderStyle.THIN);
        mystyle.setBorderLeft(BorderStyle.THIN);
        mystyle.setBorderRight(BorderStyle.THIN);
        mystyle.setTopBorderColor((short) 8);
        mystyle.setLeftBorderColor((short) 8);
        mystyle.setRightBorderColor((short) 8);
        mystyle.setBottomBorderColor((short) 8);
        mystyle.setAlignment(HorizontalAlignment.CENTER);
        mystyle.setVerticalAlignment(VerticalAlignment.CENTER);

        //获得字段
        List<DiathesisSubjectField> fieldList = diathesisSubjectFieldService.findUsingFieldByUnitIdAndSubjectType(unitId, DiathesisConstant.SUBJECT_FEILD_XY);
        List<String> fieldNameList = fieldList.stream().map(x -> x.getFieldName()).collect(Collectors.toList());

        List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIds),Course.class);

        List<String> subNameList = EntityUtils.getList(courseList, x -> x.getSubjectName());

        List<String> titleList = new ArrayList<>();
        titleList.add("学号");
        titleList.add("姓名");
        int length = subNameList.size() * fieldNameList.size() + 2;

        // 单元格样式
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont headfont = workbook.createFont();
        headfont.setFontHeightInPoints((short) 9);
        headfont.setColor(HSSFFont.COLOR_RED);
        style.setFont(headfont);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);

        // 顶部提示
        CellRangeAddress car = new CellRangeAddress(0, 0, 0, length - 1);
        sheet.addMergedRegion(car);
        HSSFRow remarkRow = sheet.createRow(0);
        remarkRow.setHeightInPoints(4 * sheet.getDefaultRowHeightInPoints());
        HSSFCell remarkCell = remarkRow.createCell(0);
        remarkCell.setCellStyle(style);
        // 注意事项
        String remark = getRemark();
        remarkCell.setCellValue(new HSSFRichTextString(remark));
        remarkCell.setCellStyle(style);

        // 表头行
        HSSFRow rowTitle = sheet.createRow(1);
        HSSFRow rowfield = sheet.createRow(2);
        //学号姓名
        for (int i = 0; i < titleList.size(); i++) {
            HSSFCell cellTitle = rowTitle.createCell(i);
            HSSFCell cellField = rowfield.createCell(i);
            cellTitle.setCellValue(new HSSFRichTextString(titleList.get(i)));
            cellTitle.setCellStyle(mystyle);
            cellField.setCellStyle(mystyle);
            CellRangeAddress region = new CellRangeAddress(1, 2, i, i);
            sheet.addMergedRegion(region);
        }
        //科目 和字段
        for (int i = 2; i < subNameList.size() * fieldNameList.size() + 2; i++) {
            HSSFCell cell = rowTitle.createCell(i);
            cell.setCellStyle(mystyle);
            if ((i - 2) % fieldNameList.size() == 0) {
                cell.setCellValue(new HSSFRichTextString(subNameList.get((i - 2) / fieldNameList.size())));
                CellRangeAddress region = new CellRangeAddress(1, 1, i, i + fieldNameList.size() - 1);
                sheet.addMergedRegion(region);
            }
            HSSFCell fieldCell = rowfield.createCell(i);
            fieldCell.setCellValue(new HSSFRichTextString(fieldNameList.get((i - 2) % fieldNameList.size())));
            fieldCell.setCellStyle(mystyle);
        }
        String typeName = "必修课";
        String fileName = grade.getGradeName() + acadyear + "学年第" + semester + "学期" + typeName + "学业水平导入";

        ExportUtils.outputData(workbook, fileName, response);
    }

    private List<Course> getCourses(String unitId, String gradeCode) {
        List<Course> courseList = SUtils.dt(courseRemoteService.findByUnitIdAndTypeAndLikeSection(unitId, BaseConstants.SUBJECT_TYPE_BX, gradeCode.substring(0,1)), Course.class);
        courseList = courseList.stream()
                .filter(e -> BaseConstants.SUBJECT_TYPE_BX.equals(e.getType()) && Integer.valueOf(1).equals(e.getIsUsing()) && !BaseConstants.ZERO_GUID.equals(e.getCourseTypeId()))
                .collect(Collectors.toList());
        return courseList;
    }

    /**
     * 模板校验
     * @return
     */
    @RequestMapping("/scholastic/validate")
    @ResponseBody
    public String validate(String filePath, String gradeId,String gradeCode,String semester){
        try {
            if (StringUtils.isBlank(filePath) || StringUtils.isBlank(gradeId)) {
                return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg(getLoginInfo().getRealName()).setDetailError("参数缺失"));
            }

            Map<String, List<String>> titleMap = getRowTitleList(gradeId,gradeCode,semester);
            List<String> first = titleMap.get("first");
            List<String> second = titleMap.get("second");
            //List<String> templateTitle = getRowTitleList(grade, semesterObj, subjects);
            List<String[]> datas = ExcelUtils.readExcelIgnoreDesc(filePath, first.size());
            if (StringUtils.isBlank(datas.get(0)[1])) {
                datas.remove(0);
            }
            if (CollectionUtils.isNotEmpty(datas)) {
                String[] realTitles = datas.get(0);
                String[] fieldTitles = datas.get(1);
                if (realTitles != null) {
                    if (first.size() <= 2) {
                        return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg(getLoginInfo().getRealName()).setDetailError("该年级未开设任何必修课" ));
                    }
                    for (int i = 0; i < realTitles.length; i++) {

                        if (!Objects.equals(realTitles[i], first.get(i))) {
                            return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg(getLoginInfo().getRealName()).setDetailError("模板中不存在列名：" + realTitles[i]));
                        }
                        if (!Objects.equals(fieldTitles[i], second.get(i))) {
                            return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg(getLoginInfo().getRealName()).setDetailError("模板中不存在列名：" + realTitles[i]));
                        }

                    }
                }
            } else {
                return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg(getLoginInfo().getRealName()).setDetailError("模板中不存在数据"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg(getLoginInfo().getRealName()).setDetailError("上传的文件模板不符合要求"));
        }
        return success(getLoginInfo().getRealName());
    }

    @RequestMapping("/scholastic/exportErrorExcel")
    public void exportError(HttpServletRequest request, HttpServletResponse response) {
        exportErrorExcel(request, response);
    }

    private String getRemark() {
        return "填写注意：\n"
                + "1、重复导入将会覆盖之前存在数据\n"
                + "2、学业水平结果只能为等第\n"
                + "3、请勿移除或改动此行";
    }


    /**
     * 学业水平统计
     */
    @RequestMapping("/scholastic/statistics")
    @ResponseBody
    public String getStatistics(String studentId){
        if(StringUtils.isBlank(studentId))return error("参数不能为空");
        Json result = new Json();
        Student student = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
        String unitId = getLoginInfo().getUnitId();
        Clazz clazz = SUtils.dc(classRemoteService.findOneById(student.getClassId()), Clazz.class);
        String gradeId = clazz.getGradeId();
        Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
        String gradeCode=grade.getGradeCode();
        List<DiathesisScoreType> scoreTypeList=diathesisScoreTypeService.findListByUnitIdAndGradeIdAndType(unitId,gradeId,DiathesisConstant.INPUT_TYPE_2);
        List<DiathesisScoreInfo> infoList = diathesisScoreInfoService.findByStudentIdAndScoreTypeIdIn(studentId, EntityUtils.getArray(scoreTypeList, x -> x.getId(), String[]::new));
        if(CollectionUtils.isEmpty(infoList)){
            return result.toJSONString();
        }
        Map<String, String> infoMap = EntityUtils.getMap(infoList, x -> x.getId(), x -> x.getObjId());
        Map<String, DiathesisScoreType> infoTypeMap = EntityUtils.getMap(scoreTypeList, x -> x.getId(), x -> x);

        List<DiathesisScoreInfoEx> infoExList = diathesisScoreInfoExService.findListByInfoTypeIdIn(EntityUtils.getList(scoreTypeList,x->x.getId()));

        List<DiathesisSubjectField> fieldList = diathesisSubjectFieldService.findUsingFieldByUnitIdAndSubjectType(unitId, DiathesisConstant.SUBJECT_FEILD_XY);

        List<DiathesisSetSubject> setSubjectList=diathesisSetSubjectService.findByGradeIdAndType(gradeId,DiathesisConstant.SUBJECT_FEILD_XY);
        if(CollectionUtils.isEmpty(setSubjectList) || CollectionUtils.isEmpty(scoreTypeList)){
            return error("该年级暂无数据");
        }
        List<String> subIds = setSubjectList.stream().map(x -> x.getSubjectId()).distinct().collect(Collectors.toList());
        List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subIds.toArray(new String[0])), Course.class);
        HashMap<String, String> scoreMap = new HashMap<>();
        for (DiathesisScoreInfoEx infoEx : infoExList) {
            DiathesisScoreType type = infoTypeMap.get(infoEx.getScoreTypeId());
            String objId = infoMap.get(infoEx.getScoreInfoId());
            String key=type.getGradeCode()+"_"+type.getSemester()+"_"+objId+"_"+infoEx.getFieldCode();
            scoreMap.put(key,infoEx.getFieldValue());
        }
        String[] numList = {"一", "二", "三","四","五","六"};
        String[] levelList = {"小", "初", "高"};
        Semester semesterEntity = SUtils.dc(semesterRemoteService.getCurrentSemester(1, unitId), Semester.class);
        String gradePrefix=levelList[Integer.parseInt(gradeCode.substring(0,1))-1];    // {"小", "初", "高"}
        String fileName=semesterEntity.getAcadyear()+"学年"+gradePrefix+numList[Integer.parseInt(gradeCode.substring(1))-1]+
                "第"+numList[semesterEntity.getSemester()]+"学期学业水平统计结果";
        result.put("fileName",fileName);
        //subjectId_fieldId
        Map<String,String> maxValue=new HashMap<>();
        //学期明细
        JsonArray semester=new JsonArray();
        Integer level = Integer.parseInt(gradeCode.substring(1));
        for (int i = 0; i <level ; i++) {
            Integer j=(i==level-1 && semesterEntity.getSemester()==1)? 1:2;
            String gradeLevel=numList[i];
            for (int k = 0; k <j ; k++) {
                String semesterName = numList[k];
                Json json = new Json();
                json.put("garde",gradePrefix+gradeLevel);
                json.put("semester",String.format("第%s学期",semesterName));
                json.put("className",gradePrefix+gradeLevel+clazz.getClassName());
                JsonArray arrCourse = new JsonArray();
                for (Course course : courseList) {
                    Json subject = new Json();
                    JsonArray arrField = new JsonArray();
                    for (DiathesisSubjectField field : fieldList) {
                        Json f = new Json();
                        f.put("fieldName",field.getFieldName());
                        String value = scoreMap.get(gradeCode.substring(0,1)+(i+1) + "_" + (k + 1) + "_" + course.getId() + "_" + field.getFieldCode());
                        f.put("value",StringUtils.defaultString(value,""));
                        arrField.add(f);

                        String max = maxValue.get(course.getId() + "_" + field.getFieldCode());
                        if(StringUtils.isBlank(max) || (StringUtils.isNotBlank(value) && value.compareTo(max)<0)){
                            maxValue.put(course.getId() + "_" + field.getFieldCode(),value);
                        }
                    }
                    subject.put("subjectName",course.getSubjectName());
                    subject.put("fieldList",arrField);

                    arrCourse.add(subject);
                }
                json.put("subjectList",arrCourse);
                semester.add(json);
            }
        }



        Json all = new Json();
        all.put("stuName",student.getStudentName());
        all.put("stuCode",student.getStudentCode());
        all.put("sex","0".equals(student.getSex())?"女":"男");
        JsonArray allCourse = new JsonArray();
        for (Course course : courseList) {
            Json sub = new Json();
            sub.put("subjectName",course.getSubjectName());
            JsonArray arrField = new JsonArray();
            for (DiathesisSubjectField field : fieldList) {
                Json f = new Json();
                f.put("fieldName",field.getFieldName());
                String value = maxValue.get(course.getId() + "_" + field.getFieldCode());
                f.put("value",value==null?"":value);
                arrField.add(f);
            }
            sub.put("fieldList",arrField);
            allCourse.add(sub);
        }
        all.put("subjectList",allCourse);


        result.put("semesterResult",semester);
        result.put("allResult",all);

        return result.toJSONString();
    }

    /**
     * 学业水平年级统计导出
     */
    @RequestMapping("/scholastic/gradeStatistics")
    @ResponseBody
    public String getGradeStatistice(String gradeId){
       if(StringUtils.isBlank(gradeId)){
           return error("参数缺失");
       }
        Json result = new Json();
        String unitId = getLoginInfo().getUnitId();
        Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
        Semester semesterEntity = SUtils.dc(semesterRemoteService.getCurrentSemester(1), Semester.class);
        List<DiathesisSubjectField> fieldList = diathesisSubjectFieldService.findUsingFieldByUnitIdAndSubjectType(unitId, DiathesisConstant.SUBJECT_FEILD_XY);
        List<DiathesisScoreType> scoreTypeList=diathesisScoreTypeService.findListByUnitIdAndGradeIdAndType(unitId,gradeId,DiathesisConstant.INPUT_TYPE_2);
        List<DiathesisSetSubject> setSubjectList=diathesisSetSubjectService.findByGradeIdAndType(gradeId,DiathesisConstant.SUBJECT_FEILD_XY);
        if(CollectionUtils.isEmpty(setSubjectList) || CollectionUtils.isEmpty(scoreTypeList)){
            return error("该年级暂无数据");
        }
        List<String> subIds = setSubjectList.stream().map(x -> x.getSubjectId()).distinct().collect(Collectors.toList());
        List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subIds.toArray(new String[0])), Course.class);
        String[] scoreTypeIds = EntityUtils.getArray(scoreTypeList, x -> x.getId(), String[]::new);

        List<DiathesisScoreInfo> infoList = diathesisScoreInfoService.findByScoreTypeIdIn(scoreTypeIds);

        List<DiathesisScoreInfoEx> infoExList = diathesisScoreInfoExService.findListByInfoTypeIdIn(EntityUtils.getList(scoreTypeList, x -> x.getId()));
        if(CollectionUtils.isEmpty(infoList) || CollectionUtils.isEmpty(infoExList)){
            return error("该年级暂无数据");
        }
        Map<String, List<DiathesisScoreInfo>> infoMap = infoList.stream().collect(Collectors.groupingBy(x -> x.getScoreTypeId()));
        Map<String, List<DiathesisScoreInfoEx>> infoExMap = infoExList.stream().collect(Collectors.groupingBy(x -> x.getScoreInfoId()));
        HashMap<String, String> scoreMap = new HashMap<>();

        for (DiathesisScoreType scoreType : scoreTypeList) {
            List<DiathesisScoreInfo> infos = infoMap.get(scoreType.getId());
            if(CollectionUtils.isEmpty(infos))continue;
            for (DiathesisScoreInfo info : infos) {
                List<DiathesisScoreInfoEx> infoEx = infoExMap.get(info.getId());
                if(CollectionUtils.isEmpty(infoEx))continue;
                for (DiathesisScoreInfoEx ex : infoEx) {
                    scoreMap.put(info.getStuId()+"_"+info.getObjId()+"_"+ex.getFieldCode()+"_"+scoreType.getGradeCode()+"_"+scoreType.getSemester(),ex.getFieldValue());
                }
            }
        }
        List<Clazz> classList = SUtils.dt(classRemoteService.findByGradeIdSortAll(gradeId), Clazz.class);
        Map<String, String> classIdMap = EntityUtils.getMap(classList, x -> x.getId(),x->x.getClassName());
        List<Student> stuList = SUtils.dt(studentRemoteService.findByClassIds(EntityUtils.getArray(classList, x -> x.getId(), String[]::new)), Student.class);
        Map<String, String> stuClassMap = EntityUtils.getMap(stuList, x -> x.getId(), x -> classIdMap.get(x.getClassId()));
        Map<String, List<Json>> classMap = stuList.stream().collect(Collectors.groupingBy(x -> x.getClassId()
                , Collectors.mapping(x -> getStuJson(x,scoreMap,courseList,fieldList,grade.getGradeCode(),stuClassMap), Collectors.toList())));


        List<Json> classResultList = classList.stream().map(x -> {
            Json json = new Json();
            json.put("className", x.getClassNameDynamic());
            json.put("stuList", classMap.get(x.getId()));
            return json;
        }).collect(Collectors.toList());
        result.put("gradeName",semesterEntity.getAcadyear()+"学年"+grade.getGradeName());
        result.put("classList",classResultList);

        JsonArray semesterTitle = new JsonArray();
        Json name = new Json();
        name.put("name","学生姓名");
        Json code = new Json();
        code.put("name","学号");
        Json sex = new Json();
        sex.put("name","性别");
        semesterTitle.add(name);
        semesterTitle.add(code);
        semesterTitle.add(sex);

        setSublist(fieldList, courseList,semesterTitle);

        JsonArray allTitle = new JsonArray();
        Json grade1 = new Json();
        grade1.put("name","年级");
        Json semester1 = new Json();
        semester1.put("name","学期");
        Json class1 = new Json();
        class1.put("name","班级");

        allTitle.add(grade1);
        allTitle.add(semester1);
        allTitle.add(class1);

        setSublist(fieldList, courseList, allTitle);

        result.put("semesterTitle",semesterTitle);
        result.put("allTitle",allTitle);

        return JSON.toJSONString(result);
    }

    private void setSublist(List<DiathesisSubjectField> fieldList, List<Course> courseList, JsonArray semesterTitle) {
        for (Course course : courseList) {
            Json json = new Json();
            JsonArray arr = new JsonArray();
            for (DiathesisSubjectField field : fieldList) {
                Json json1 = new Json();
                json1.put("name",field.getFieldName());
                arr.add(json1);
            }
            json.put("name",course.getSubjectName());
            json.put("fieldList",arr);
            semesterTitle.add(json);
        }
    }

    private Json getStuJson(Student student, HashMap<String, String> scoreMap
            ,List<Course> courseList,List<DiathesisSubjectField> fieldList
            ,String gradeCode,Map<String, String> stuClassMap) {
        Json json = new Json();
        Semester semesterEntity = SUtils.dc(semesterRemoteService.getCurrentSemester(1), Semester.class);
        String[] numList = {"一", "二", "三","四","五","六"};
        String[] levelList = {"小", "初", "高"};

        //key subId_fieldCode
        HashMap<String, String> maxScore = new HashMap<>();
        List<List<String>> semesterResult=new ArrayList<>();
        String sectionName =levelList[Integer.parseInt(gradeCode.substring(0, 1))-1];

        int level = Integer.parseInt(gradeCode.substring(1));
        for (int i = 0; i < level; i++) {
            Integer j=(i==level-1 && semesterEntity.getSemester()==1)? 1:2;
            String gradeLevel=numList[i];
            for (int k = 0; k <j ; k++) {
                List<String> per=new ArrayList<>();
                per.add(sectionName+gradeLevel);  //年级
                per.add(String.format("第%s学期",numList[k]));  //学期
                per.add(sectionName+gradeLevel+stuClassMap.get(student.getId()));  //班级
                for (Course course : courseList) {
                    for (DiathesisSubjectField field : fieldList) {
                        String score = scoreMap.get(student.getId() + "_" + course.getId() + "_" + field.getFieldCode() + "_" + gradeCode.substring(0, 1) + (i + 1) + "_" + (k + 1));
                        per.add(StringUtils.defaultString(score,""));
                        String max = maxScore.get(course.getId() + "_" + field.getFieldCode());
                        if(StringUtils.isBlank(max) || (StringUtils.isNotBlank(score) && score.compareTo(max)<0)){
                            maxScore.put(course.getId() + "_" + field.getFieldCode(),score);
                        }
                    }
                }
                semesterResult.add(per);
            }
        }

        List<String> allResult = new ArrayList<>();
        allResult.add(student.getStudentName());
        allResult.add(student.getStudentCode());
        allResult.add(student.getSex()==0?"女":"男");
        for (Course course : courseList) {
            for (DiathesisSubjectField field : fieldList) {
                String score = maxScore.get( course.getId() + "_" + field.getFieldCode());
                allResult.add(StringUtils.defaultString(score,""));
            }
        }
        json.put("semesterResult", semesterResult);
        json.put("allResult", allResult);
        return json;
    }




  /*  @RequestMapping("/scholastic/getAllSubject")
    @ResponseBody
    public String getAllSubject(String gradeCode){
        String unitId = getLoginInfo().getUnitId();
        List<Course> courseList = SUtils.dt(courseRemoteService.findByUnitIdAndTypeAndLikeSection(unitId, BaseConstants.SUBJECT_TYPE_BX, gradeCode.substring(0, 1)), Course.class);

        return Json.toJSONString(courseList.stream()
                .filter(e -> BaseConstants.SUBJECT_TYPE_BX.equals(e.getType()) && Integer.valueOf(1).equals(e.getIsUsing()) && !BaseConstants.ZERO_GUID.equals(e.getCourseTypeId()))
                .map(x->{
                    Json json = new Json();
                    json.put("subjectId",x.getId());
                    json.put("subjectName",x.getSubjectName());
                    return json;
                }).collect(Collectors.toList()));
    }*/
}