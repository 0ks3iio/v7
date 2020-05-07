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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Date: 2019/03/28
 * 学科成绩 必修
 */
@Controller
@RequestMapping("/diathesis/compulsory/")
public class DiathesisSubjectCompulsoryAction extends DataImportAction {

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
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private CourseRemoteService courseRemoteService;
    @Autowired
    private GradeTeachingRemoteService gradeTeachingRemoteService;

    @RequestMapping("/classList")
    @ResponseBody
    public String getClassList(String gradeId) {
        if (StringUtils.isBlank(gradeId)) {
            return error("参数丢失");
        }
        String unitId = getLoginInfo().getUnitId();
        List<Clazz> classList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(unitId, gradeId), Clazz.class);
        List<DiathesisClassDto> classDtoList = new ArrayList<>();
        for (Clazz one : classList) {
            DiathesisClassDto tmp = new DiathesisClassDto();
            tmp.setClassId(one.getId());
            if (StringUtils.isNotBlank(one.getClassNameDynamic())) {
                tmp.setClassName(one.getClassNameDynamic());
            } else {
                tmp.setClassName(one.getClassName());
            }
            classDtoList.add(tmp);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("classList", classDtoList);
        return jsonObject.toJSONString();
    }


    @RequestMapping("/subject/gradeList")
    @ResponseBody
    public String getGradeList() {

        String unitId = getLoginInfo().getUnitId();

        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId, new Integer[]{BaseConstants.SECTION_PRIMARY, BaseConstants.SECTION_JUNIOR, BaseConstants.SECTION_HIGH_SCHOOL}), Grade.class);
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2, unitId), Semester.class);
        List<DiathesisScoreType> diathesisScoreTypeList = diathesisScoreTypeService.findByUnitIdAndGradeIdAndType(unitId, null, DiathesisConstant.INPUT_TYPE_1);
        Map<String, DiathesisScoreTypeDto> map = new HashMap<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        for (DiathesisScoreType one : diathesisScoreTypeList) {
            DiathesisScoreTypeDto tmp = new DiathesisScoreTypeDto();
            tmp.setScoreId(one.getId());
            tmp.setScoreType(one.getScoreType());
            tmp.setScoreName(one.getExamName());
            tmp.setImportTime(simpleDateFormat.format(one.getModifyTime()));
            map.put(one.getGradeId() + "_" + one.getGradeCode() + "_" + one.getSemester() + "_" + one.getScoreType(), tmp);
        }
        List<DiathesisGradeDto> diathesisGradeDtoList = new ArrayList<>();
        for (Grade one : gradeList) {
            DiathesisGradeDto tmp = new DiathesisGradeDto();
            tmp.setGradeId(one.getId());
            tmp.setGradeCode(one.getGradeCode());
            tmp.setYear(one.getOpenAcadyear().substring(0, 4) + "级");
            tmp.setSemester(semester.getSemester().toString());
            tmp.setGradeName(one.getGradeName());
            List<DiathesisScoreTypeDto> sub = new ArrayList<>();
            // 必修
            DiathesisScoreTypeDto compulsory = map.get(one.getId() + "_" + one.getGradeCode() + "_" + semester.getSemester() + "_" + DiathesisConstant.SCORE_TYPE_1);
            if (compulsory == null) {
                compulsory = new DiathesisScoreTypeDto();
                compulsory.setScoreId(BaseConstants.ZERO_GUID);
                compulsory.setScoreType(BaseConstants.SUBJECT_TYPE_BX);
                compulsory.setScoreName("无");

            }
            // 加上还有几门课没有导入
            List<DiathesisIdAndNameDto> usingSubjectType1 = getUsingSubject(one.getId(), "" + semester.getSemester(), semester.getAcadyear(), DiathesisConstant.SCORE_TYPE_1, unitId);
            compulsory.setUnCompletedSub(getUnCompletedSub(unitId, semester, one, DiathesisConstant.SCORE_TYPE_1));
            int usingSubjectType1Count = 0;
            int unCompletedSubType1Count = 0;
            if (usingSubjectType1 != null) {
                usingSubjectType1Count = usingSubjectType1.size();
            }
            if (compulsory.getUnCompletedSub() != null) {
                unCompletedSubType1Count = compulsory.getUnCompletedSub().size();
            }
            compulsory.setType(usingSubjectType1Count == unCompletedSubType1Count ? "0" : "1");
            sub.add(compulsory);
            // 选修
            DiathesisScoreTypeDto optional = map.get(one.getId() + "_" + one.getGradeCode() + "_" + semester.getSemester() + "_" + DiathesisConstant.SCORE_TYPE_2);
            if (optional == null) {
                optional = new DiathesisScoreTypeDto();
                optional.setScoreId(BaseConstants.ZERO_GUID);
                optional.setScoreType(BaseConstants.SUBJECT_TYPE_XX);
                optional.setScoreName("无");
            }
            //加上还有几门课没有导入
            optional.setUnCompletedSub(getUnCompletedSub(unitId, semester, one, DiathesisConstant.SCORE_TYPE_2));
            List<DiathesisIdAndNameDto> usingSubjectType2 = getUsingSubject(one.getId(), "" + semester.getSemester(), semester.getAcadyear(), DiathesisConstant.SCORE_TYPE_1, unitId);
            int usingSubjectType2Count = 0;
            int unCompletedSubType2Count = 0;
            if (usingSubjectType2 != null) {
                usingSubjectType2Count = usingSubjectType2.size();
            }
            if (optional.getUnCompletedSub() != null) {
                unCompletedSubType2Count = compulsory.getUnCompletedSub().size();
            }
            optional.setType(usingSubjectType2Count == unCompletedSubType2Count ? "0" : "1");
            sub.add(optional);
            tmp.setScoreList(sub);
            diathesisGradeDtoList.add(tmp);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("gradeList", diathesisGradeDtoList);
        return jsonObject.toJSONString();
    }

    private List<String> getUnCompletedSub(String unitId, Semester semester, Grade one, String scoreType) {
        List<DiathesisScoreType> typeList = diathesisScoreTypeService.findListByGradeId(unitId, one.getId(), one.getGradeCode(), "" + semester.getSemester(), DiathesisConstant.SCORE_TYPE_1);
        String[] typeIds = EntityUtils.getArray(typeList, x -> x.getId(), String[]::new);
        List<DiathesisScoreInfo> scoreInfoList = diathesisScoreInfoService.findByScoreTypeIdIn(typeIds);
        Set<String> subjectSet = EntityUtils.getSet(scoreInfoList, x -> x.getObjId());
        Map<String, GradeTeaching> gradeTeachingMap = SUtils.dt(gradeTeachingRemoteService.findBySearchMap(one.getSchoolId(), semester.getAcadyear(), semester.getSemester().toString(), one.getId()), new TR<Map<String, GradeTeaching>>() {
        });
        List<String> courseIds = gradeTeachingMap.values().stream().filter(e -> scoreType.equals(e.getSubjectType())).map(GradeTeaching::getSubjectId).collect(Collectors.toList());
        List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(courseIds.toArray(new String[0])), Course.class);
        return courseList.stream().filter(e -> !BaseConstants.ZERO_GUID.equals(e.getCourseTypeId())).filter(x -> !subjectSet.contains(x.getId())).map(x -> x.getSubjectName()).collect(Collectors.toList());
    }

    private List<DiathesisIdAndNameDto> getUsingSubject(String gradeId, String semester, String acadyear, String scoreType, String schoolId) {
        Map<String, GradeTeaching> gradeTeachingMap = SUtils.dt(gradeTeachingRemoteService.findBySearchMap(schoolId, acadyear, semester, gradeId), new TR<Map<String, GradeTeaching>>() {
        });
        List<String> courseIds = gradeTeachingMap.values().stream().filter(e -> scoreType.equals(e.getSubjectType())).map(GradeTeaching::getSubjectId).collect(Collectors.toList());
        List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(courseIds.toArray(new String[0])), Course.class);
        return courseList.stream().filter(e -> !BaseConstants.ZERO_GUID.equals(e.getCourseTypeId())).map(x -> {
            DiathesisIdAndNameDto dto = new DiathesisIdAndNameDto();
            dto.setId(x.getId());
            dto.setName(x.getSubjectName());
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * @param gradeId
     * @param classId
     * @param gradeCode
     * @param semester
     * @param studentCode
     * @param studentName
     * @return
     */

    @RequestMapping("/subject/detail")
    @ResponseBody
    public String getStudentDetail(String gradeId, String classId, String gradeCode, String semester
            , String studentCode, String studentName) {
        String scoreType = BaseConstants.SUBJECT_TYPE_BX;
        if (StringUtils.isBlank(gradeId) || StringUtils.isBlank(gradeCode) || StringUtils.isBlank(semester)) {
            return error("参数丢失");
        }

        Json json = new Json();
        String unitId = getLoginInfo().getUnitId();

        Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
        List<Clazz> classList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(unitId, gradeId), Clazz.class);
        Map<String, String> classNameMap = classList.stream()
                .collect(Collectors.toMap(e -> e.getId(), e -> (e.getClassNameDynamic() == null ? e.getClassName() : e.getClassNameDynamic())));
        //String acadyear = (Integer.valueOf(acadyearTmp[0]) + Integer.valueOf(gradeCode.substring(1)) - 1) + "-" + (Integer.valueOf(acadyearTmp[1]) + Integer.valueOf(gradeCode.substring(1)) - 1);
        //List<GradeTeaching> gradeTeachingList = SUtils.dt(gradeTeachingRemoteService.findBySearchList(unitId, acadyear, semester, gradeId, Integer.valueOf(scoreType)), GradeTeaching.class);
        List<DiathesisSetSubject> subSetList = diathesisSetSubjectService.findByGradeIdAndGradeCodeAndSemesterAndType(gradeId, gradeCode, Integer.parseInt(semester), DiathesisConstant.SUBJECT_FEILD_BX);
        List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(EntityUtils.getArray(subSetList, x -> x.getSubjectId(), String[]::new)), Course.class);


        List<DiathesisSubjectField> fieldList = diathesisSubjectFieldService.findUsingFieldByUnitIdAndSubjectType(unitId, DiathesisConstant.SUBJECT_FEILD_BX);

        //成绩字段的code
        String scoreFieldCode = DiathesisConstant.COMPULSORY_SCORE;

        List<DiathesisFieldDto> fieldResultList = getFieldList(fieldList);
        json.put("fieldList", fieldResultList);

        List<DiatheisSubjectScoreDto> title = courseList.stream().map(x -> {
            DiatheisSubjectScoreDto subDto = new DiatheisSubjectScoreDto();
            subDto.setSubjectId(x.getId());
            subDto.setSubjectName(x.getSubjectName());
            subDto.setFieldList(getFieldList(fieldList));
            return subDto;
        }).collect(Collectors.toList());

        json.put("title", title);

        //courseList = courseList.stream().filter(e -> !BaseConstants.ZERO_GUID.equals(e.getCourseTypeId())).collect(Collectors.toList());

        DiathesisScoreType diathesisScoreType = diathesisScoreTypeService.findByGradeIdAndTypeAndScoreTypeAndGradeCodeAndSemesterAndExamName(gradeId, DiathesisConstant.INPUT_TYPE_1, scoreType, gradeCode, semester, null);
        if (diathesisScoreType == null) return json.toJSONString();
        List<DiathesisScoreInfo> diathesisScoreInfoList = diathesisScoreInfoService.findByTypeAndScoreTypeId(scoreType, diathesisScoreType.getId());
        List<String> infoIds = EntityUtils.getList(diathesisScoreInfoList, x -> x.getId());
        List<DiathesisScoreInfoEx> infoExList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(infoIds)) {
            infoExList = diathesisScoreInfoExService.findListByScoreTypeId(diathesisScoreType.getId());
        } else {
            return json.toJSONString();
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


        Map<String, Student> stuMap = EntityUtils.getMap(students, x -> x.getId(), x -> x);

        //Map<String, String> courseMap = EntityUtils.getMap(courseList, x -> x.getId(), x -> x.getSubjectName());

        diathesisScoreInfoList = diathesisScoreInfoList.stream().filter(x -> stuMap.keySet().contains(x.getStuId())).collect(Collectors.toList());
        // 分页
        Pagination page = createPagination();
        //必修
        page.setMaxRowCount(students.size());
        int end = page.getPageIndex() * page.getPageSize();
        students = students.subList((page.getPageIndex() - 1) * page.getPageSize(), end > students.size() ? students.size() : end);
        json.put("page", page);

        Map<String, String> infoExMap = EntityUtils.getMap(infoExList, x -> x.getScoreInfoId() + "_" + x.getFieldCode(), x -> StringUtils.defaultString(x.getFieldValue(), ""));

        Map<String, String> scoreMap = new HashMap<>();
        for (DiathesisScoreInfo info : diathesisScoreInfoList) {
            scoreMap.put(info.getStuId() + "_" + info.getObjId() + "_" + scoreFieldCode, info.getScore());
            for (DiathesisFieldDto field : fieldResultList) {

                if (scoreFieldCode.equals(field.getFieldCode())) continue;
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
        json.put("stuList", result);
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

    @Override
    public List<String> getRowTitleList() {
        return null;
    }

    public Map<String, List<String>> getRowTitleList(String gradeId, String gradeCode, String semester) {
        String scoreType = BaseConstants.SUBJECT_TYPE_BX;
        List<String> titleList = new ArrayList<>();


        String unitId = getLoginInfo().getUnitId();
        List<DiathesisSubjectField> fieldList = diathesisSubjectFieldService.findUsingFieldByUnitIdAndSubjectType(unitId, DiathesisConstant.SUBJECT_FEILD_BX).stream()
                .filter(x -> !DiathesisConstant.COMPULSORY_GREDIT.equals(x.getFieldCode())).collect(Collectors.toList());

        List<DiathesisSetSubject> subList = diathesisSetSubjectService.findByGradeIdAndGradeCodeAndSemesterAndType(gradeId, gradeCode, Integer.parseInt(semester), DiathesisConstant.SUBJECT_FEILD_BX);

        List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(EntityUtils.getArray(subList, x -> x.getSubjectId(), String[]::new)), Course.class);


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

    /**
     * 必修导入
     *
     * @param filePath
     * @param params
     * @return
     */
    @Override
    @RequestMapping("/subject/import")
    @ResponseBody
    public String dataImport(String filePath, String params) {
        JSONObject jsStr = JSONObject.parseObject(params);
        JSONObject jsonObject = new JSONObject();
        String unitId = getLoginInfo().getUnitId();

        String gradeId = jsStr.getString("gradeId");
        String scoreType = BaseConstants.SUBJECT_TYPE_BX;
        String gradeCode = jsStr.getString("gradeCode");
        String semesterStr = jsStr.getString("semester");
        try {
            if (StringUtils.isBlank(gradeId) || StringUtils.isBlank(gradeCode) || StringUtils.isBlank(semesterStr)) {
                return error("参数缺失");
            }
            //防止并发 导入重复数据  用redis锁控制
            RedisUtils.hasLocked("diathesis_compulsory_import_" + unitId + gradeId + gradeCode + semesterStr + "_78,");

            List<DiathesisSetSubject> subjectList = diathesisSetSubjectService.findByGradeIdAndGradeCodeAndSemesterAndType(gradeId, gradeCode, Integer.parseInt(semesterStr), DiathesisConstant.SUBJECT_FEILD_BX);
            String[] subjects = EntityUtils.getArray(subjectList, x -> x.getSubjectId(), String[]::new);


            Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
            Semester semester;
            semester = new Semester();
            String[] acadyearTmp = grade.getOpenAcadyear().split("-");
            String acadyear = (Integer.valueOf(acadyearTmp[0]) + Integer.valueOf(gradeCode.substring(1)) - 1) + "-" + (Integer.valueOf(acadyearTmp[1]) + Integer.valueOf(gradeCode.substring(1)) - 1);
            semester.setAcadyear(acadyear);
            semester.setSemester(Integer.valueOf(semesterStr));
            List<String[]> datas = ExcelUtils.readExcelIgnoreDesc(filePath, getRowTitleList(grade.getId(), gradeCode, semesterStr).get("first").size());
            List<DiathesisSubjectField> fieldList = diathesisSubjectFieldService.findUsingFieldByUnitIdAndSubjectType(unitId, DiathesisConstant.SUBJECT_FEILD_BX);
            Map<String, String> fieldNameToCode = EntityUtils.getMap(fieldList, x -> x.getFieldName(), x -> x.getFieldCode());

            List<String> titleList = new ArrayList<>();
            List<String> fieldTypeList = new ArrayList<>();
            CollectionUtils.addAll(titleList, datas.get(0));
            CollectionUtils.addAll(fieldTypeList, datas.get(1));
            datas.remove(0);
            datas.remove(0);

            List<String[]> errorDataList = new ArrayList<>();
            if (CollectionUtils.isEmpty(datas)) {
                return error("没有导入数据");
            }

            Map<String, GradeTeaching> gradeTeachingMap = SUtils.dt(gradeTeachingRemoteService.findBySearchMap(grade.getSchoolId(), semester.getAcadyear(), semester.getSemester().toString(), grade.getId()), new TR<Map<String, GradeTeaching>>() {
            });
            List<String> courseIds = gradeTeachingMap.values().stream().filter(e -> scoreType.equals(e.getSubjectType())).map(GradeTeaching::getSubjectId).collect(Collectors.toList());
            List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(courseIds.toArray(new String[0])), Course.class);
            courseList = courseList.stream().filter(e -> !BaseConstants.ZERO_GUID.equals(e.getCourseTypeId())).collect(Collectors.toList());
            Map<String, String> courseNameToIdMap = courseList.stream().collect(Collectors.toMap(Course::getSubjectName, Course::getId));
            //学分
            Map<String, Integer> courseIdToCredit = EntityUtils.getMap(courseList, x -> x.getId(), x -> x.getInitCredit() == null ? 0 : x.getInitCredit());
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
                classList = SUtils.dt(classRemoteService.findListByIds(classIdSet.toArray(new String[0])), new TR<List<Clazz>>() {
                });
                for (Clazz one : classList) {
                    classIdToGradeIdMap.put(one.getId(), one.getGradeId());
                }
            }

            // 原有成绩信息
            Map<String, DiathesisScoreInfo> oldDiathesisScoreInfoMap = new HashMap<>();
            Map<String, DiathesisScoreInfoEx> oldScoreExMap = new HashMap<>();
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
                String typeName = BaseConstants.SUBJECT_TYPE_BX.equals(scoreType) ? "必修课" : "选修课";
                diathesisScoreType.setExamName(grade.getGradeName() + semester.getAcadyear() + "学年第" + semester.getSemester() + "学期" + typeName + "学科成绩");
                diathesisScoreType.setModifyTime(new Date());
                diathesisScoreType.setOperator(getLoginInfo().getOwnerId());
            } else {
                diathesisScoreType.setModifyTime(new Date());
                List<DiathesisScoreInfo> diathesisScoreInfoList = diathesisScoreInfoService.findByTypeAndScoreTypeId(scoreType, diathesisScoreType.getId());
                if (CollectionUtils.isNotEmpty(diathesisScoreInfoList)) {
                    oldDiathesisScoreInfoMap = diathesisScoreInfoList.stream().collect(Collectors.toMap(e -> (e.getStuId() + "_" + e.getObjId()), e -> e));
                }

                List<DiathesisScoreInfoEx> infoExList = diathesisScoreInfoExService.findListByScoreTypeId(diathesisScoreType.getId());
                if (CollectionUtils.isNotEmpty(infoExList)) {
                    oldScoreExMap = infoExList.stream().collect(Collectors.toMap(e -> (e.getScoreInfoId() + "_" + e.getFieldCode()), e -> e));
                }
            }


            // 错误数据序列号
            int successCount = 0;
            Set<String> arrangeStuId = new HashSet<>();
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
                for (int i = 2; i < fieldTypeList.size(); i++) {

                    String subName = getSubjectName(titleList, i);
                    String fieldName = fieldTypeList.get(i);
                    String scoreStr = arr[i] == null ? null : StringUtils.trim(arr[i]);
                    if (StringUtils.isBlank(scoreStr)) {
                        continue;
                    }
                    // 判断输入成绩是否正确
                    if ("成绩".equals(fieldName) && !pattern.matcher(scoreStr).matches()) {
                        errorData = new String[4];
                        errorData[0] = index + "";
                        errorData[1] = titleList.get(i);
                        errorData[2] = scoreStr;
                        errorData[3] = "分数：" + arr[i] + "格式不正确(最多3位整数，2位小数)!";
                        errorDataList.add(errorData);
                        flag = false;
                        break;
                    }
                    scoreMap.put(courseNameToIdMap.get(subName) + "__" + fieldNameToCode.get(fieldName) + "__" + ("成绩".equals(fieldName) ? 0 : 1), scoreStr);
                }

                //todo
                for (String sub : subjects) {
                    scoreMap.put(sub + "__" + fieldNameToCode.get("学分") + "__1", String.valueOf(courseIdToCredit.get(sub)));
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
                    String type = s[2];  //0:成绩  1: 其它

                    DiathesisScoreInfo tmp = null;
                    if (oldDiathesisScoreInfoMap != null && oldDiathesisScoreInfoMap.size() > 0) {
                        tmp = oldDiathesisScoreInfoMap.get(student.getId() + "_" + subId);
                    }
                    if (tmp != null) {
                        tmp.setModifyTime(new Date());
                        if (type.equals("0")) tmp.setScore(item.getValue().toString());
                    } else {
                        tmp = new DiathesisScoreInfo();
                        tmp.setId(UuidUtils.generateUuid());
                        tmp.setUnitId(unitId);
                        tmp.setType(scoreType);
                        tmp.setScoreTypeId(diathesisScoreType.getId());
                        tmp.setStuId(student.getId());
                        tmp.setObjId(subId);
                        if (type.equals("0")) tmp.setScore(item.getValue().toString());
                        tmp.setModifyTime(new Date());
                        oldDiathesisScoreInfoMap.put(student.getId() + "_" + subId, tmp);
                    }
                    insertList.add(tmp);
                    if (!type.equals("0")) {
                        DiathesisScoreInfoEx tmpEx = null;
                        if (oldScoreExMap != null && oldScoreExMap.size() > 0) {
                            tmpEx = oldScoreExMap.get(tmp.getId() + "_" + fieldCode);
                        }
                        //todo if(StringUtils.isNotBlank(item.getValue())){}
                        if (tmpEx != null) {
                            tmpEx.setFieldValue(item.getValue());
                            tmpEx.setModifyTime(new Date());
                        } else {
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

                CellRangeAddress RemarkCar = new CellRangeAddress(0, 0, 0, titleList.size() - 1);
                sheet.addMergedRegion(RemarkCar);
                HSSFRow remarkRow = sheet.createRow(0);
                HSSFCell remarkCell = remarkRow.createCell(0);
                remarkCell.setCellValue(new HSSFRichTextString("请勿移除此行"));
                remarkCell.setCellStyle(style);

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


                List<String> fieldNameList = fieldList.stream().filter(x -> !DiathesisConstant.COMPULSORY_GREDIT.equals(x.getFieldCode())).map(x -> x.getFieldName()).collect(Collectors.toList());
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

                    if (j == 0 || j == 1 || j == titleList.size() - 1 || j == titleList.size() - 2) {
                        CellRangeAddress c = new CellRangeAddress(1, 2, j, j);
                        sheet.addMergedRegion(c);
                    }
                    if ((j - 2) % fieldNameList.size() == fieldNameList.size()) {
                        CellRangeAddress c = new CellRangeAddress(1, 1, j, j - fieldNameList.size() + 1);
                        sheet.addMergedRegion(c);
                    }
                }


                for (int i = 0; i < errorDataList.size(); i++) {
                    HSSFRow row = sheet.createRow(i + 3);
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
                diathesisScoreTypeService.save(diathesisScoreType, insertList, insertExList);
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
            RedisUtils.unLock("diathesis_compulsory_import_" + unitId + gradeId + gradeCode + semesterStr + "_78,");
        }

        return jsonObject.toJSONString();
    }

    private String getSubjectName(List<String> titleList, int i) {

        for (int j = i; j >= 0; j--) {
            if (StringUtils.isNotBlank(titleList.get(j))) return titleList.get(j);
        }
        return null;
    }

    //必修excel 导入字段行
    private List<String> getRowTemplateList(Grade grade, String semester) {
        return null;
    }


    /**
     * 必修模版
     *
     * @param request
     * @param response
     */
    @Override
    @RequestMapping("/subject/template")
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) {
        String unitId = getLoginInfo().getUnitId();
        String gradeId = request.getParameter("gradeId");
        String scoreType = "";
        String gradeCode = request.getParameter("gradeCode");
        String semester = request.getParameter("semester");
        // String subjectIds = request.getParameter("subjectIds");

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
        List<DiathesisSubjectField> fieldList = diathesisSubjectFieldService.findUsingFieldByUnitIdAndSubjectType(unitId, DiathesisConstant.SUBJECT_FEILD_BX);
        List<String> fieldNameList = fieldList.stream().filter(x -> !DiathesisConstant.COMPULSORY_GREDIT.equals(x.getFieldCode())).map(x -> x.getFieldName()).collect(Collectors.toList());
        List<DiathesisSetSubject> setSub = diathesisSetSubjectService.findByGradeIdAndGradeCodeAndSemesterAndType(gradeId, gradeCode, Integer.parseInt(semester), DiathesisConstant.SUBJECT_FEILD_BX);
        String[] subIds = EntityUtils.getArray(setSub, x -> x.getSubjectId(), String[]::new);
        List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subIds), Course.class);
        List<String> subNameList = EntityUtils.getList(courseList, x -> x.getSubjectName());
        List<String> titleList = new ArrayList<>();
        titleList.add("学号");
        titleList.add("姓名");
        int length = subIds.length * fieldNameList.size() + 2;

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
        String fileName = grade.getGradeName() + acadyear + "学年第" + semester + "学期" + typeName + "学科成绩导入";

        ExportUtils.outputData(workbook, fileName, response);
    }


    /**
     * 模板校验
     *
     * @return
     */
    @RequestMapping("subject/validate")
    @ResponseBody
    public String validate(String filePath, String gradeId, String gradeCode, String semester, String subjectIds) {
        try {
            if (StringUtils.isBlank(filePath) || StringUtils.isBlank(gradeId) || StringUtils.isBlank(semester) || StringUtils.isBlank(gradeId)) {
                return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg(getLoginInfo().getRealName()).setDetailError("参数缺失"));
            }
            Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
            Semester semesterObj;

            semesterObj = new Semester();
            String[] acadyearTmp = grade.getOpenAcadyear().split("-");
            String acadyear = (Integer.valueOf(acadyearTmp[0]) + Integer.valueOf(gradeCode.substring(1)) - 1) + "-" + (Integer.valueOf(acadyearTmp[1]) + Integer.valueOf(gradeCode.substring(1)) - 1);
            semesterObj.setAcadyear(acadyear);
            semesterObj.setSemester(Integer.valueOf(semester));

            Map<String, List<String>> titleMap = getRowTitleList(gradeId, gradeCode, semester);
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
                        return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg(getLoginInfo().getRealName()).setDetailError("该年级未开设任何必修课"));
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

    @RequestMapping("/subject/exportErrorExcel")
    public void exportError(HttpServletRequest request, HttpServletResponse response) {
        exportErrorExcel(request, response);
    }

    private String getRemark() {
        return "填写注意：\n"
                + "1、重复导入将会覆盖之前存在数据;\n";
    }

    private static List<String> acadyearName(String gradeCode, Integer semester) {
        Integer section = Integer.valueOf(gradeCode.substring(0, 1));
        Integer index = Integer.valueOf(gradeCode.substring(1));
        if (BaseConstants.SECTION_HIGH_SCHOOL.equals(section) || BaseConstants.SECTION_COLLEGE.equals(index)) {
            int end = (index - 1) * 2 + semester;
            if (end > 6) {
                end = 6;
            }
            return DiathesisConstant.HIGH_ACADYEAR_LIST.subList(0, end);
        } else if (BaseConstants.SECTION_JUNIOR.equals(section)) {
            int end = (index - 1) * 2 + semester;
            if (end > 6) {
                end = 6;
            }
            return DiathesisConstant.JUNIOR_ACADYEAR_LIST.subList(0, end);
        } else if (BaseConstants.SECTION_PRIMARY.equals(section)) {
            int end = (index - 1) * 2 + semester;
            if (end > 12) {
                end = 12;
            }
            return DiathesisConstant.PRIMARY_ACADYEAR_LIST.subList(0, end);
        }
        return new ArrayList<>();
    }

    @GetMapping("/subject/getAllField")
    @ResponseBody
    public String getAllField() {
        return JSON.toJSONString(diathesisSubjectFieldService.findUsingField(getLoginInfo().getUnitId()));
    }


    /**
     * 从教务管理中获取成绩 学分,负责人,审核人
     *
     * @return
     */
    @RequestMapping("/getScore")
    @ResponseBody
    private String getScore(String examId, String gradeId, String gradeCode, String semester) {
        if (StringUtils.isBlank(examId) || StringUtils.isBlank(gradeId) || StringUtils.isBlank(gradeCode)
                || StringUtils.isBlank(semester)) {
            return error("参数错误");
        }
        String unitId = getLoginInfo().getUnitId();
        try {
            RedisUtils.hasLocked("diathesis_compulsory_import_" + unitId + gradeId + gradeCode + semester + "_78,");
            String scoreType = BaseConstants.SUBJECT_TYPE_BX;
            List<String> result = diathesisScoreTypeService.saveValueToDiathesis(unitId, gradeId, gradeCode, semester, scoreType, getLoginInfo().getRealName(), examId);
            if (CollectionUtils.isEmpty(result)) {
                return success("导入成功");
            } else {
                return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg(String.format("存在%d门科目导入不成功", result.size())).setDetailError(StringUtils.join(result, ",")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return error(e.getMessage());
        } finally {
            RedisUtils.unLock("diathesis_compulsory_import_" + unitId + gradeId + gradeCode + semester + "_78,");
        }
    }
}
