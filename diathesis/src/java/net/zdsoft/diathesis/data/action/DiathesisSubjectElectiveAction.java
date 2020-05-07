package net.zdsoft.diathesis.data.action;

import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.CourseType;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.CourseTypeRemoteService;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.diathesis.data.constant.DiathesisConstant;
import net.zdsoft.diathesis.data.dto.DiathesisFieldDto;
import net.zdsoft.diathesis.data.dto.DiathesisStuScoreDto;
import net.zdsoft.diathesis.data.entity.DiathesisScoreInfo;
import net.zdsoft.diathesis.data.entity.DiathesisScoreInfoEx;
import net.zdsoft.diathesis.data.entity.DiathesisScoreType;
import net.zdsoft.diathesis.data.entity.DiathesisSubjectField;
import net.zdsoft.diathesis.data.service.DiathesisScoreInfoExService;
import net.zdsoft.diathesis.data.service.DiathesisScoreInfoService;
import net.zdsoft.diathesis.data.service.DiathesisScoreTypeService;
import net.zdsoft.diathesis.data.service.DiathesisSubjectFieldService;
import net.zdsoft.framework.dataimport.DataImportAction;
import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.ExcelUtils;
import net.zdsoft.framework.utils.ExportUtils;
import net.zdsoft.framework.utils.Objects;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Date: 2019/08/16
 * 学科成绩 选修
 */
@Controller
@RequestMapping("/diathesis/elective/")
public class DiathesisSubjectElectiveAction extends DataImportAction {

    @Autowired
    private CourseTypeRemoteService courseTypeRemoteService;
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




    /**
     * 选修课成绩详情
     *  type 选修类型 不填为所有的 2:选修课  4 : 选修IA  5:选修IB  6 选修II
     */
    @RequestMapping("/subject/detail")
    @ResponseBody
    public String getStudentDetail(String gradeId, String classId, String gradeCode, String semester, String studentCode, String studentName,String type) {
        if (StringUtils.isBlank(gradeId) || StringUtils.isBlank(gradeCode) || StringUtils.isBlank(semester) ) {
            return error("参数丢失");
        }
        String scoreType=BaseConstants.SUBJECT_TYPE_XX;
        Json json = new Json();
        String unitId = getLoginInfo().getUnitId();
        List<DiathesisSubjectField> fieldList = diathesisSubjectFieldService.findUsingFieldByUnitIdAndSubjectType(unitId, DiathesisConstant.SUBJECT_FEILD_XX);

        //成绩字段的id
        String scoreFieldCode=DiathesisConstant.ELECTIVE_SCORE;
        //课程类型的id

        List<DiathesisFieldDto> fieldResultList = fieldList.stream().map(x -> {
            DiathesisFieldDto fieldDto = new DiathesisFieldDto();
            fieldDto.setFieldId(x.getId());
            fieldDto.setFieldCode(x.getFieldCode());
            fieldDto.setFieldName(x.getFieldName());
            return fieldDto;
        }).collect(Collectors.toList());
        json.put("fieldList",fieldResultList);

        Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
        String[] acadyearTmp = grade.getOpenAcadyear().split("-");

        DiathesisScoreType diathesisScoreType = diathesisScoreTypeService.findByGradeIdAndTypeAndScoreTypeAndGradeCodeAndSemesterAndExamName(gradeId, DiathesisConstant.INPUT_TYPE_1, scoreType, gradeCode, semester, null);
        if(diathesisScoreType==null) return json.toJSONString();
        List<DiathesisScoreInfo> diathesisScoreInfoList = diathesisScoreInfoService.findByTypeAndScoreTypeId(scoreType, diathesisScoreType.getId());

        List<DiathesisScoreInfoEx>infoExList=diathesisScoreInfoExService.findListByScoreTypeId(diathesisScoreType.getId());
        if(StringUtils.isNotBlank(type)){
            List<String> infoIdsFitler = infoExList.stream().filter(x -> DiathesisConstant.ELECTIVE_TYPE.equals(x.getFieldCode()) && type.equals(x.getFieldValue())).map(x -> x.getScoreInfoId()).collect(Collectors.toList());
            diathesisScoreInfoList=EntityUtils.filter2(diathesisScoreInfoList,x->infoIdsFitler.contains(x.getId()));
            infoExList=EntityUtils.filter2(infoExList,x->infoIdsFitler.contains(x.getScoreInfoId()));
        }


        //学生列表
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

        List<String> stuIds = EntityUtils.getList(students, x -> x.getId());
        diathesisScoreInfoList=EntityUtils.filter2(diathesisScoreInfoList,x->stuIds.contains(x.getStuId()));

        // 分页
        Pagination page = createPagination();

        page.setMaxRowCount(diathesisScoreInfoList.size());
        int end = page.getPageIndex() * page.getPageSize();
        diathesisScoreInfoList = diathesisScoreInfoList.subList((page.getPageIndex() - 1) * page.getPageSize(), end > diathesisScoreInfoList.size() ? diathesisScoreInfoList.size() : end);
        json.put("page", page);

        Map<String, String> infoExMap = EntityUtils.getMap(infoExList, x -> x.getScoreInfoId()+"_"+x.getFieldCode(), x -> StringUtils.defaultString(x.getFieldValue(),""));
        //课程类型
        String[] courseTypeIds = infoExList.stream().filter(x -> DiathesisConstant.ELECTIVE_COURSE_TYPE.equals(x.getFieldCode())).map(x -> StringUtils.defaultString(x.getFieldValue(),"")).distinct().toArray(String[]::new);
        Map<String,String> courseTypeMap=new HashMap<>();
        if(courseTypeIds!=null && courseTypeIds.length>0){
            courseTypeMap= EntityUtils.getMap(SUtils.dt(courseTypeRemoteService.findListByIds(courseTypeIds), CourseType.class),x->x.getId(),x->StringUtils.defaultString(x.getName(),""));
        }
        Map<String, String> scoreMap = new HashMap<>();
        for (DiathesisScoreInfo info : diathesisScoreInfoList) {
            scoreMap.put(info.getStuId()+"_"+info.getObjId()+"_"+scoreFieldCode,info.getScore());
            for (DiathesisFieldDto field : fieldResultList) {
                if(field.getFieldCode().equals(scoreFieldCode))continue;
                String score = infoExMap.get(info.getId() + "_" + field.getFieldCode());
                //课程类型 和选修类型 id转换成中文
               // ELECTIVE_COURSE_TYPE="ELECTIVE_COURSE_TYPE"; //课程类型(体育/语文)
                // String ELECTIVE_TYPE="ELECTIVE_TYPE";   //选修类型(选修I,选修II..)
                if(DiathesisConstant.ELECTIVE_COURSE_TYPE.equals(field.getFieldCode())){
                    score=courseTypeMap.get(score);
                }else if(DiathesisConstant.ELECTIVE_TYPE.equals(field.getFieldCode())){
                    if(BaseConstants.SUBJECT_TYPE_XX_4.equals(score))score="选修Ⅰ-A";
                    if(BaseConstants.SUBJECT_TYPE_XX_5.equals(score))score="选修Ⅰ-B";
                    if(BaseConstants.SUBJECT_TYPE_XX_6.equals(score))score="选修Ⅱ";
                    if(BaseConstants.SUBJECT_TYPE_XX.equals(score))score="选修";
                }
                scoreMap.put(info.getStuId()+"_"+info.getObjId()+"_"+field.getFieldCode(),score);
            }
        }

        List<DiathesisStuScoreDto> result=new ArrayList<>();

        Map<String, Student> stuMap = EntityUtils.getMap(students, x -> x.getId(), x -> x);

        List<Clazz> classList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(unitId, gradeId), Clazz.class);

        Map<String, String> classNameMap = classList.stream()
                .collect(Collectors.toMap(e -> e.getId(),  e -> (e.getClassNameDynamic() == null ? e.getClassName() : e.getClassNameDynamic())));
        String acadyear = (Integer.valueOf(acadyearTmp[0]) + Integer.valueOf(gradeCode.substring(1)) - 1) + "-" + (Integer.valueOf(acadyearTmp[1]) + Integer.valueOf(gradeCode.substring(1)) - 1);

       // List<String> xx_list = Arrays.asList(BaseConstants.SUBJECT_TYPE_XX, BaseConstants.SUBJECT_TYPE_XX_4, BaseConstants.SUBJECT_TYPE_XX_5, BaseConstants.SUBJECT_TYPE_XX_6);
        String[] objIds = EntityUtils.getSet(diathesisScoreInfoList, x -> x.getObjId()).toArray(new String[0]);
        List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(objIds), Course.class);
        Map<String, String> courseMap = EntityUtils.getMap(courseList, x -> x.getId(), x -> x.getSubjectName());

        for (DiathesisScoreInfo info : diathesisScoreInfoList) {
            Student one=stuMap.get(info.getStuId());
            DiathesisStuScoreDto temp = new DiathesisStuScoreDto();
            temp.setStuName(one.getStudentName());
            temp.setStuCode(one.getStudentCode());
            temp.setClazz(classNameMap.get(one.getClassId()));
            temp.setSex(Integer.valueOf(1).equals(one.getSex()) ? "男" : "女");
            temp.setSubName(courseMap.get(info.getObjId()));
            List<DiathesisFieldDto> fields=new ArrayList<>();
            for (DiathesisFieldDto field : fieldResultList) {
                DiathesisFieldDto dto = new DiathesisFieldDto();
                dto.setFieldName(field.getFieldName());
                String score = scoreMap.get(one.getId() + "_" + info.getObjId()+ "_" + field.getFieldCode());
                dto.setValue(score);
                fields.add(dto);
            }
            temp.setFieldList(fields);
            result.add(temp);
        }
        json.put("stuList",result);
        return json.toJSONString();
    }

    // ==========================================================
    // 成绩导入逻辑

    @RequestMapping("/subject/execute")
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

    /**
     * 选修课首行
     * @return
     */
    @Override
    public List<String> getRowTitleList() {
        List<String> fitler = Arrays.asList(DiathesisConstant.ELECTIVE_COURSE_TYPE, DiathesisConstant.ELECTIVE_TYPE
                , DiathesisConstant.ELECTIVE_TOTAL_HOURS, DiathesisConstant.ELECTIVE_GREDIT);
        List<String> fieldNames = diathesisSubjectFieldService.findUsingFieldByUnitIdAndSubjectType(getLoginInfo().getUnitId(), DiathesisConstant.SUBJECT_FEILD_XX)
                .stream().filter(x -> !fitler.contains(x.getFieldCode())).map(x -> x.getFieldName()).collect(Collectors.toList());
        List<String> titleList = new ArrayList<>();
        titleList.add("学号");
        titleList.add("姓名");
        titleList.add("科目");
        titleList.addAll(fieldNames);

        return titleList;
    }

    @Override
    @RequestMapping("/subject/import")
    @ResponseBody
    public String dataImport(String filePath, String params) {
        JSONObject jsStr = JSONObject.parseObject(params);
        JSONObject jsonObject = new JSONObject();
        String unitId = getLoginInfo().getUnitId();
        String gradeId = jsStr.getString("gradeId");
        String scoreType = BaseConstants.SUBJECT_TYPE_XX;
        String gradeCode = jsStr.getString("gradeCode");
        String semesterStr= jsStr.getString("semester");
        if(StringUtils.isBlank(gradeId)||StringUtils.isBlank(gradeCode)||StringUtils.isBlank(semesterStr)){
            return error("参数缺失");
        }
        try {
            //防止并发 导入重复数据  用redis锁控制
            RedisUtils.hasLocked("diathesis_subjectElective_import_" + unitId+gradeId+gradeCode+semesterStr + "_78,");
            Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
            Semester semester;
            semester = new Semester();
            String[] acadyearTmp = grade.getOpenAcadyear().split("-");
            String acadyear = (Integer.valueOf(acadyearTmp[0]) + Integer.valueOf(gradeCode.substring(1)) - 1) + "-" + (Integer.valueOf(acadyearTmp[1]) + Integer.valueOf(gradeCode.substring(1)) - 1);
            semester.setAcadyear(acadyear);
            semester.setSemester(Integer.valueOf(semesterStr));

            List<String[]> datas = ExcelUtils.readExcelIgnoreDesc(filePath, getRowTitleList().size());


            List<DiathesisSubjectField> fieldList = diathesisSubjectFieldService.findUsingFieldByUnitIdAndSubjectType(unitId, DiathesisConstant.SUBJECT_FEILD_XX).stream()
                    .filter(x -> !DiathesisConstant.COMPULSORY_GREDIT.equals(x.getFieldCode())).collect(Collectors.toList());
            Map<String, String> fieldNameToCode = EntityUtils.getMap(fieldList, x -> x.getFieldName(), x -> x.getFieldCode());
            List<String> rowTitleList = getRowTitleList();

            List<String> titleList = new ArrayList<>();
            CollectionUtils.addAll(titleList, datas.get(0));
            datas.remove(0);

            List<String[]> errorDataList = new ArrayList<>();
            if (CollectionUtils.isEmpty(datas)) {
                return error("没有导入数据");
            }

            List<String> xx_list = Arrays.asList(BaseConstants.SUBJECT_TYPE_XX, BaseConstants.SUBJECT_TYPE_XX_4, BaseConstants.SUBJECT_TYPE_XX_5, BaseConstants.SUBJECT_TYPE_XX_6);

            List<Course> courseList = SUtils.dt(courseRemoteService.findByUnitIdIn(unitId), Course.class)
                    .stream().filter(e -> !BaseConstants.ZERO_GUID.equals(e.getCourseTypeId()))
                    .filter(x->1==x.getIsUsing())
                    .filter(x->xx_list.contains(x.getType())).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(courseList)){
                return error("没有选修课程信息");
            }
            Map<String, Course> courseMap = EntityUtils.getMap(courseList, x -> x.getId(), x -> x);

            Map<String, String> courseNameToIdMap =new HashMap<>();
            for (Course course : courseList) {
                String courseId = courseNameToIdMap.get(course.getSubjectName());
                if(StringUtils.isBlank(courseId) ||
                        (StringUtils.isNotBlank(courseId) && gradeCode.substring(0,1).equals(course.getSection()))){
                    courseNameToIdMap.put(course.getSubjectName(),course.getId());
                }
            }

            courseList.stream().collect(Collectors.toMap(Course::getSubjectName, Course::getId));

            // 学生
            Set<String> studentCodeSet = new HashSet<>();
            for (String[] arr : datas) {
                if (StringUtils.isNotBlank(arr[0])) {
                    studentCodeSet.add(arr[0]);
                }
            }
            List<Student> studentList;
            Map<String, Student> studentMap = new HashMap<String, Student>();
            // 考虑学号重复问题
            Map<String, Set<String>> studentCodeToIdMap = new HashMap<String, Set<String>>();
            Set<String> classIdSet = new HashSet<>();
            if (CollectionUtils.isNotEmpty(studentCodeSet)) {
                studentList = SUtils.dt(studentRemoteService.findBySchIdStudentCodes(unitId, studentCodeSet.toArray(new String[0])), new TR<List<Student>>() {
                });
                for (Student one : studentList) {
                    studentMap.put(one.getId(), one);
                    if (!studentCodeToIdMap.containsKey(one.getStudentCode())) {
                        studentCodeToIdMap.put(one.getStudentCode(), new HashSet<>());
                    }
                    studentCodeToIdMap.get(one.getStudentCode()).add(one.getId());
                    classIdSet.add(one.getClassId());
                }
            }

            List<Clazz> classList;
            Map<String, String> classIdToGradeIdMap = new HashMap<String, String>();
            if (CollectionUtils.isNotEmpty(classIdSet)) {
                classList = SUtils.dt(classRemoteService.findListByIds(classIdSet.toArray(new String[0])), Clazz.class);
                for (Clazz one : classList) {
                    classIdToGradeIdMap.put(one.getId(), one.getGradeId());
                }
            }

            // 原有成绩信息
            Map<String, DiathesisScoreInfo> oldDiathesisScoreInfoMap = new HashMap<>();
            Map<String, DiathesisScoreInfoEx> oldScoreExMap = null;
            DiathesisScoreType diathesisScoreType = diathesisScoreTypeService.findByGradeIdAndTypeAndScoreTypeAndGradeCodeAndSemesterAndExamName(grade.getId(), DiathesisConstant.INPUT_TYPE_1, scoreType, gradeCode, semester.getSemester().toString(), null);
            if (diathesisScoreType == null) {
                diathesisScoreType = new DiathesisScoreType();
                diathesisScoreType.setId(UuidUtils.generateUuid());
                diathesisScoreType.setUnitId(unitId);
                diathesisScoreType.setGradeId(grade.getId());
                diathesisScoreType.setGradeCode(gradeCode);
                diathesisScoreType.setSemester(semester.getSemester());
                diathesisScoreType.setYear(grade.getOpenAcadyear().substring(0, 4));
                diathesisScoreType.setType(DiathesisConstant.INPUT_TYPE_1);
                diathesisScoreType.setScoreType(scoreType);
                String typeName = "选修课";
                diathesisScoreType.setExamName(grade.getGradeName() + semester.getAcadyear() + "学年第" + semester.getSemester() + "学期" + typeName + "学科成绩");
                diathesisScoreType.setModifyTime(new Date());
                diathesisScoreType.setOperator(getLoginInfo().getOwnerId());
            } else {
                diathesisScoreType.setModifyTime(new Date());
                List<DiathesisScoreInfo> diathesisScoreInfoList = diathesisScoreInfoService.findByTypeAndScoreTypeId(scoreType, diathesisScoreType.getId());
                if (CollectionUtils.isNotEmpty(diathesisScoreInfoList)) {
                    oldDiathesisScoreInfoMap = diathesisScoreInfoList.stream().collect(Collectors.toMap(e -> (e.getStuId() + "_" + e.getObjId()), e -> e));
                }

                List<DiathesisScoreInfoEx> infoExList=diathesisScoreInfoExService.findListByScoreTypeId(diathesisScoreType.getId());
                if (CollectionUtils.isNotEmpty(infoExList)) {
                    oldScoreExMap = infoExList.stream().collect(Collectors.toMap(e -> (e.getScoreInfoId() + "_" + e.getFieldCode()), e -> e));
                }
            }


            // 错误数据序列号
            int successCount = 0;
            Set<String> arrangeStuIdAndSubId = new HashSet<>();
            List<DiathesisScoreInfo> insertList = new ArrayList<>();
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
                    for (Student one : sameCodeAndNameList) {
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

                // 保存成绩 subId_fieldId
                boolean flag = true;
                Map<String, String> scoreMap = new HashMap<>();
                Pattern pattern = Pattern.compile("^(0|[1-9]\\d{0,2})(\\.\\d{1,2})?$");
                String subjectId=null;
                for (int i = 2; i < rowTitleList.size(); i++) {
                    String subName=rowTitleList.get(i);
                    String scoreStr = arr[i] == null ? null : StringUtils.trim(arr[i]);
                    if (StringUtils.isBlank(scoreStr)) {
                        continue;
                    }
                    // 判断输入成绩是否正确
                    if ("成绩".equals(subName) && !pattern.matcher(scoreStr).matches()) {
                        errorData = new String[4];
                        errorData[0] = index + "";
                        errorData[1] = titleList.get(i);
                        errorData[2] = scoreStr;
                        errorData[3] = "分数：" + arr[i] + "格式不正确(最多3位整数，2位小数)!";
                        errorDataList.add(errorData);
                        flag = false;
                        break;
                    }
                    if(i==2){
                        subjectId= courseNameToIdMap.get(scoreStr);
                        if(StringUtils.isBlank(subjectId)){
                            errorData = new String[4];
                            errorData[0] = index + "";
                            errorData[1] = titleList.get(i);
                            errorData[2] = scoreStr;
                            errorData[3] = "不存在这个科目";
                            errorDataList.add(errorData);
                            flag = false;
                            break;
                        }
                        //todo 系统默认字段从v7导入准备
                        Course course = courseMap.get(subjectId);
                        //学分
                        scoreMap.put(subjectId+"__"+fieldNameToCode.get("学分")+"__1", course.getInitCredit()==null?"":""+course.getInitCredit());
                        //课程类型   ELECTIVE_COURSE_TYPE
                        scoreMap.put(subjectId+"__"+fieldNameToCode.get("课程类型")+"__1", course.getCourseTypeId());
                        //选修类型  ELECTIVE_TYPE
                        scoreMap.put(subjectId+"__"+fieldNameToCode.get("选修类型")+"__1", course.getType());
                        //总学时      ELECTIVE_TOTAL_HOURS
                        scoreMap.put(subjectId+"__"+fieldNameToCode.get("总学时")+"__1", course.getTotalHours()==null?"":""+course.getTotalHours());

                    }else{
                        scoreMap.put(subjectId+"__"+fieldNameToCode.get(subName)+"__"+("成绩".equals(subName)?0:1), scoreStr);
                    }
                }
                if (!flag) {
                    continue;
                }
                // todo 加条件   && arrangeNameContian(这个项目)
                if (arrangeStuIdAndSubId.contains(student.getId()+"_"+subjectId) ) {
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
                    String type=s[2];  //0:成绩  1: 其它

                    DiathesisScoreInfo tmp = null;
                    if (oldDiathesisScoreInfoMap != null && oldDiathesisScoreInfoMap.size() > 0) {
                        tmp = oldDiathesisScoreInfoMap.get(student.getId() + "_" + subId);
                    }
                    if (tmp != null) {
                        tmp.setModifyTime(new Date());
                        if(type.equals("0")) tmp.setScore(item.getValue().toString());
                    } else {
                        tmp = new DiathesisScoreInfo();
                        tmp.setId(UuidUtils.generateUuid());
                        tmp.setUnitId(unitId);
                        tmp.setType(scoreType);
                        tmp.setScoreTypeId(diathesisScoreType.getId());
                        tmp.setStuId(student.getId());
                        tmp.setObjId(subId);
                        if(type.equals("0")) tmp.setScore(item.getValue().toString());
                        tmp.setModifyTime(new Date());
                        oldDiathesisScoreInfoMap.put(student.getId() + "_" + subId,tmp);
                    }
                    insertList.add(tmp);
                    if(!type.equals("0")) {
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
                }
                arrangeStuIdAndSubId.add(student.getId()+"_"+subjectId);
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

                CellRangeAddress RemarkCar = new CellRangeAddress(0, 0, 0, titleList.size() - 1);
                sheet.addMergedRegion(RemarkCar);
                HSSFRow remarkRow = sheet.createRow(0);
                HSSFCell remarkCell = remarkRow.createCell(0);
                remarkCell.setCellValue(new HSSFRichTextString("请勿移除此行"));
                remarkCell.setCellStyle(style);

                HSSFRow rowTitle = sheet.createRow(1);
                for (int j = 0; j < titleList.size(); j++) {
                    HSSFCell cell = rowTitle.createCell(j);
                    cell.setCellValue(new HSSFRichTextString(titleList.get(j)));
                    cell.setCellStyle(style);
                }

                for (int i = 0; i < errorDataList.size(); i++) {
                    HSSFRow row = sheet.createRow(i + 2);
                    String[] datasDetail = datas.get(Integer.parseInt(errorDataList.get(i)[0]) - 1);
                    for (int j = 0; j < titleList.size(); j++) {
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

            try {
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
            RedisUtils.unLock("diathesis_subjectElective_import_" + unitId+gradeId+gradeCode+semesterStr + "_78,");
        }

        return jsonObject.toJSONString();
    }



    @Override
    @RequestMapping("/subject/template")
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) {
        String unitId = getLoginInfo().getUnitId();
        String gradeId = request.getParameter("gradeId");
        String gradeCode = request.getParameter("gradeCode");
        String semester = request.getParameter("semester");
        String acadyear = diathesisScoreTypeService.getAcadyearBy(gradeId, gradeCode);
        Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();

        //字段获取
       List<String> titleList = getRowTitleList();
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
        CellRangeAddress car = new CellRangeAddress(0,0,0,titleList.size()-1);
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
        for(int i=0;i<titleList.size();i++){
            HSSFCell cell = rowTitle.createCell(i);
            cell.setCellValue(new HSSFRichTextString(titleList.get(i)));
        }
        String typeName = "选修课";
        String fileName = grade.getGradeName() + acadyear + "学年第" + semester + "学期" + typeName + "学科成绩导入";
        ExportUtils.outputData(workbook, fileName, response);
    }

    /**
     * 模板校验
     * @return
     */
    @RequestMapping("subject/validate")
    @ResponseBody
    public String validate(String filePath, String gradeId,  String gradeCode, String semester) {
        try {
            if (StringUtils.isBlank(filePath) || StringUtils.isBlank(gradeId)  || StringUtils.isBlank(semester) || StringUtils.isBlank(gradeId)) {
                return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg(getLoginInfo().getRealName()).setDetailError("参数缺失"));
            }
            String unitId = getLoginInfo().getUnitId();

            List<String> xx_list = Arrays.asList(BaseConstants.SUBJECT_TYPE_XX, BaseConstants.SUBJECT_TYPE_XX_4, BaseConstants.SUBJECT_TYPE_XX_5, BaseConstants.SUBJECT_TYPE_XX_6);
            List<Course> courseList = SUtils.dt(courseRemoteService.findByUnitIdIn(unitId), Course.class)
                    .stream().filter(e -> !BaseConstants.ZERO_GUID.equals(e.getCourseTypeId()))
                    .filter(x->1==x.getIsUsing())
                    .filter(x->xx_list.contains(x.getType())).collect(Collectors.toList());
            if(CollectionUtils.isEmpty(courseList)){
                return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg(getLoginInfo().getRealName()).setDetailError("没有选修课程信息"));
            }

            Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
            Semester semesterObj;

            semesterObj = new Semester();
            String[] acadyearTmp = grade.getOpenAcadyear().split("-");
            String acadyear = (Integer.valueOf(acadyearTmp[0]) + Integer.valueOf(gradeCode.substring(1)) - 1) + "-" + (Integer.valueOf(acadyearTmp[1]) + Integer.valueOf(gradeCode.substring(1)) - 1);
            semesterObj.setAcadyear(acadyear);
            semesterObj.setSemester(Integer.valueOf(semester));

            List<String> titleList = getRowTitleList();
            List<String[]> datas = ExcelUtils.readExcelIgnoreDesc(filePath, titleList.size());
            if (StringUtils.isBlank(datas.get(0)[1])) {
                datas.remove(0);
            }
            if (CollectionUtils.isNotEmpty(datas)) {
                String[] realTitles = datas.get(0);
                if (realTitles != null) {
                    for (int i = 0; i < realTitles.length; i++) {
                        if (!Objects.equals(realTitles[i], titleList.get(i))) {
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

    @RequestMapping("/subject/exportErrorExcel")
    public void exportError(HttpServletRequest request, HttpServletResponse response) {
        exportErrorExcel(request, response);
    }

    private String getRemark() {
        return "填写注意：\n"
                + "1、重复导入将会覆盖之前存在数据;\n";
    }



}
