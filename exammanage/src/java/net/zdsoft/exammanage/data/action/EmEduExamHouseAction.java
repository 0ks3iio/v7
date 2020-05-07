package net.zdsoft.exammanage.data.action;

import com.google.common.collect.Lists;

import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.exammanage.data.dto.EmGoodCountDto;
import net.zdsoft.exammanage.data.dto.EmHouseDto;
import net.zdsoft.exammanage.data.dto.EmSportsScoreDto;
import net.zdsoft.exammanage.data.entity.*;
import net.zdsoft.exammanage.data.service.*;
import net.zdsoft.exammanage.data.utils.ExamUtils;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.text.Collator;
import java.util.*;

@Controller
@RequestMapping("/exammanage/edu")
public class EmEduExamHouseAction extends BaseAction {
    @Autowired
    private EmExamInfoService emExamInfoService;
    @Autowired
    private SemesterRemoteService semesterRemoteService;
    @Autowired
    private ExammanageHouseRegisterService exammanageHouseRegisterService;
    @Autowired
    private EmEnrollStudentService emEnrollStudentService;//报名学生
    @Autowired
    private UnitRemoteService unitRemoteService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private SchoolRemoteService schoolRemoteService;
    @Autowired
    private EmJoinexamschInfoService emJoinexamschInfoService;
    @Autowired
    private ExammanageSportsScoreService exammanageSportsScoreService;

    @RequestMapping("/house/page")
    @ControllerInfo(value = "Tab页")
    public String asessTab(ModelMap map) {
        return "/exammanage/house/houseTab.ftl";
    }

    @ResponseBody
    @RequestMapping("/house/examnamelist")
    @ControllerInfo(value = "考试名称列表")
    public List<EmExamInfo> examNameList(String acadyear, String searchSemester, ModelMap map) {
        LoginInfo loginInfo = getLoginInfo();
        String unitId = loginInfo.getUnitId();
        List<EmExamInfo> emExamInfos = emExamInfoService.findByUnitIdAndAcadyear(unitId, acadyear, searchSemester);
        return emExamInfos;
    }

    @ResponseBody
    @RequestMapping("/house/stu")
    @ControllerInfo(value = "考试名称列表")
    public Student stuList(String studentId, ModelMap map) {
        Student student = new Student();
        if (StringUtils.isNotBlank(studentId)) {
            student = studentRemoteService.findOneObjectById(studentId);
            Unit unit = unitRemoteService.findOneObjectById(student.getSchoolId());
            Unit unitPar = unitRemoteService.findOneObjectById(unit.getParentId());
            student.setSchoolName(unit.getUnitName());
            student.setClassName(unitPar.getUnitName());//上级教育局单位
        }
        return student;
    }

    @RequestMapping("/house/index")
    @ControllerInfo(value = "报名学生名单Tab")
    public String houseIndex(ModelMap map) {
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {
        });
        if (CollectionUtils.isEmpty(acadyearList)) {
            return errorFtl(map, "学年学期不存在");
        }
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
        map.put("acadyearList", acadyearList);
        map.put("semester", semester);
        return "/exammanage/house/houseIndex.ftl";
    }

    @RequestMapping("/house/list")
    @ControllerInfo(value = "报名学生名单Tab")
    public String houseList(String acadyear, String semester, String examId, ModelMap map, HttpServletRequest request) {
        Pagination page = createPagination();
        List<ExammanageHouseRegister> exammanageHouseRegisters = exammanageHouseRegisterService.getExammanageHouseRegisterByAcadyearAndSemesterAndExamId(acadyear, semester, examId, page);
        List<ExammanageHouseRegister> exammanageHouseRegisters2=new ArrayList<>();
        exammanageHouseRegisters2.addAll(exammanageHouseRegisters);
        if(CollectionUtils.isNotEmpty(exammanageHouseRegisters2)){
        	Collections.sort(exammanageHouseRegisters2, new Comparator<ExammanageHouseRegister>() {  

                @Override  
                public int compare(ExammanageHouseRegister o1, ExammanageHouseRegister o2) {
                	if(o1.getOldSchoolId().equals(o2.getOldSchoolId())){
                		Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);  
                		return com.compare(o1.getStuName(), o2.getStuName());  
                	}
                	return 0;
                }  

            });
        }
        map.put("exammanageHouseRegisters", exammanageHouseRegisters2);
        sendPagination(request, map, page);
        return "/exammanage/house/houseList.ftl";
    }

    @RequestMapping("/house/edit")
    @ControllerInfo(value = "新增或修改")
    public String houseEdit(String id, String examId, String acadyear, String semester, ModelMap map, HttpSession httpSession) {
        LoginInfo loginInfo = getLoginInfo();
        String unitId = loginInfo.getUnitId();
        List<EmEnrollStudent> enrollStudentList = Lists.newArrayList();
        List<Unit> units = Lists.newArrayList();
        enrollStudentList = emEnrollStudentService.findByIdsAndstate(examId, "all", null, "1", null);
        if (CollectionUtils.isNotEmpty(enrollStudentList)) {
            Set<String> schoolIdSet = EntityUtils.getSet(enrollStudentList, "schoolId");
            Map<String, String> schoolNameMap = getSchoolNameMap(schoolIdSet);
            Set<String> studentIds = EntityUtils.getSet(enrollStudentList, "studentId");
            List<Student> students = SUtils.dt(studentRemoteService.findListByIds(studentIds.toArray(new String[0])), new TR<List<Student>>() {
            });
            Map<String, Student> studentNameMap = EntityUtils.getMap(students, "id");
            Set<String> classIds = EntityUtils.getSet(students, "classId");
            List<Clazz> clazzes = SUtils.dt(classRemoteService.findClassListByIds(classIds.toArray(new String[0])), new TR<List<Clazz>>() {
            });
            Map<String, Clazz> clazzesNameMap = EntityUtils.getMap(clazzes, "id");
            Iterator<EmEnrollStudent> it = enrollStudentList.iterator();
            while (it.hasNext()) {
                EmEnrollStudent emEnrollStudent = it.next();
                emEnrollStudent.setSchoolName(schoolNameMap.get(emEnrollStudent.getSchoolId()));
                Student student = studentNameMap.get(emEnrollStudent.getStudentId());
                emEnrollStudent.setStudent(student);
                if (student != null) {
                    if (clazzesNameMap.get(student.getClassId()) == null) {
                        it.remove();
                    }
                    emEnrollStudent.setClazz(clazzesNameMap.get(student.getClassId()));
                }
            }
        }
        ExammanageHouseRegister exammanageHouseRegister = null;
        if (StringUtils.isNotBlank(id)) {
            exammanageHouseRegister = exammanageHouseRegisterService.findOneWithMaster(id);
        } else {
            exammanageHouseRegister = new ExammanageHouseRegister();
            exammanageHouseRegister.setAcadyear(acadyear);
            exammanageHouseRegister.setSemester(semester);
            exammanageHouseRegister.setExamId(examId);
        }
        units = SUtils.dt(unitRemoteService.findDirectUnits(unitId, 1), Unit.class);
        map.put("exammanageHouseRegister", exammanageHouseRegister);
        map.put("enrollStudentList", enrollStudentList);
        map.put("units", units);
        return "/exammanage/house/houseAdd.ftl";
    }

    @ResponseBody
    @RequestMapping("/house/save")
    @ControllerInfo(value = "保存户籍")
    public String doSaveExam(ExammanageHouseRegister exammanageHouseRegister) {
        try {
            if (StringUtils.isBlank(exammanageHouseRegister.getId())) {
                exammanageHouseRegister.setId(UuidUtils.generateUuid());
                Student student = studentRemoteService.findOneObjectById(exammanageHouseRegister.getStudentId());
                exammanageHouseRegister.setOldSchoolId(student.getSchoolId());
                Unit unit = unitRemoteService.findOneObjectById(student.getSchoolId());
                exammanageHouseRegister.setOldUnitId(unit.getParentId());
            }
            List<ExammanageHouseRegister> objectList = exammanageHouseRegisterService.getExammanageHouseRegisterByExamIdAndStudentId(exammanageHouseRegister.getExamId(), exammanageHouseRegister.getStudentId());
            if (CollectionUtils.isNotEmpty(objectList)) {
                return error("该学生已转入转出！");
            }
            exammanageHouseRegisterService.save(exammanageHouseRegister);
        } catch (Exception e) {
            e.printStackTrace();
            return returnError();
        }
        return returnSuccess();
    }

    @ResponseBody
    @RequestMapping("/house/delete")
    @ControllerInfo("删除考试")
    public String doDeleteHouse(String id) {
        try {
            exammanageHouseRegisterService.delete(id);
            ;
        } catch (Exception e) {
            e.printStackTrace();
            return error("操作失败！" + e.getMessage());
        }
        return returnSuccess();
    }

    @RequestMapping("/house/count")
    @ControllerInfo(value = "报名学生名单Tab")
    public String houseCount(ModelMap map) {
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {
        });
        if (CollectionUtils.isEmpty(acadyearList)) {
            return errorFtl(map, "学年学期不存在");
        }
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
        map.put("acadyearList", acadyearList);
        map.put("semester", semester);
        return "/exammanage/house/houseCount.ftl";
    }

    @RequestMapping("/house/countList")
    @ControllerInfo(value = "报名学生名单Tab")
    public String houseCountList(String acadyear, String semester, String examId, ModelMap map) {
        List<EmHouseDto> emHouseDtos = exammanageHouseRegisterService.getExammanageHouseRegistersByAcadyearAndSemesterAndExamId(acadyear, semester, examId);
        map.put("emHouseDtos", emHouseDtos);
        return "/exammanage/house/houseCountList.ftl";
    }

    @RequestMapping("/goodStu/page")
    @ControllerInfo(value = "优秀生Tab页")
    public String enrollstuTab(ModelMap map) {
        return "/exammanage/goodStu/goodStuTab.ftl";
    }

    @ResponseBody
    @RequestMapping("/goodStu/schoolnamelist")
    @ControllerInfo(value = "学校名称列表")
    public List<School> schoolNameList(String examId, ModelMap map) {
        List<EmJoinexamschInfo> emJoinexamschInfos = emJoinexamschInfoService.findByExamId(examId);
        Set<String> schoolIds = EntityUtils.getSet(emJoinexamschInfos, "schoolId");
        List<School> schools = SUtils.dt(schoolRemoteService.findListByIds(schoolIds.toArray(new String[0])), new TR<List<School>>() {
        });
        return schools;
    }

    @RequestMapping("/goodStu/index")
    @ControllerInfo(value = "报名学生名单Tab")
    public String enrollstuIndex(ModelMap map) {
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {
        });
        if (CollectionUtils.isEmpty(acadyearList)) {
            return errorFtl(map, "学年学期不存在");
        }
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
        map.put("acadyearList", acadyearList);
        map.put("semester", semester);
        return "/exammanage/goodStu/goodStuIndex.ftl";
    }

    @RequestMapping("/goodStu/list")
    @ControllerInfo(value = "报名学生列表")
    public String enrollstuList(String examId, String schoolId, String hasGood, ModelMap map, HttpServletRequest request) {
        String url = "/exammanage/goodStu/goodStuList.ftl";
        if (StringUtils.isEmpty(examId) || StringUtils.isEmpty(schoolId)) {
            return url;
        }
        List<EmEnrollStudent> enrollStudentList = Lists.newArrayList();
        Pagination page = createPagination();
        enrollStudentList = emEnrollStudentService.findByIdsAndGood(examId, schoolId, hasGood, page);
        if (CollectionUtils.isNotEmpty(enrollStudentList)) {
            Set<String> schoolIdSet = EntityUtils.getSet(enrollStudentList, "schoolId");
            Map<String, String> schoolNameMap = getSchoolNameMap(schoolIdSet);
            Set<String> studentIds = EntityUtils.getSet(enrollStudentList, "studentId");
            List<Student> students = SUtils.dt(studentRemoteService.findListByIds(studentIds.toArray(new String[0])), new TR<List<Student>>() {
            });
            Map<String, Student> studentNameMap = EntityUtils.getMap(students, "id");
            Set<String> classIds = EntityUtils.getSet(students, "classId");
            List<Clazz> clazzes = SUtils.dt(classRemoteService.findClassListByIds(classIds.toArray(new String[0])), new TR<List<Clazz>>() {
            });
            Map<String, Clazz> clazzesNameMap = EntityUtils.getMap(clazzes, "id");
            Iterator<EmEnrollStudent> it = enrollStudentList.iterator();
            while (it.hasNext()) {
                EmEnrollStudent emEnrollStudent = it.next();
                emEnrollStudent.setSchoolName(schoolNameMap.get(emEnrollStudent.getSchoolId()));
                Student student = studentNameMap.get(emEnrollStudent.getStudentId());
                emEnrollStudent.setStudent(student);
                if (student != null) {
                    if (clazzesNameMap.get(student.getClassId()) == null) {
                        it.remove();
                    }
                    emEnrollStudent.setClazz(clazzesNameMap.get(student.getClassId()));
                    emEnrollStudent.setShowPictrueUrl(ExamUtils.showPicUrl(student.getDirId(), student.getFilePath(), student.getSex()));
                }
            }

        }

        map.put("enrollStudentList", enrollStudentList);
        sendPagination(request, map, page);
        return url;
    }

    @ResponseBody
    @RequestMapping("/goodStu/save")
    @ControllerInfo(value = "优秀生设置")
    public String enrollstuSave(String ids, String hasGood, ModelMap map) {
        String[] emIds = null;
        emIds = ids.split(",");
        if (emIds.length == 0) {
            return error("请先选择学生！");
        }
        try {
            List<EmEnrollStudent> emEnrollStudents = emEnrollStudentService.findListByIdsWithMaster(emIds);
            for (EmEnrollStudent emEnrollStudent : emEnrollStudents) {
                emEnrollStudent.setHasGood(hasGood);
            }
            emEnrollStudentService.saveAll(emEnrollStudents.toArray(new EmEnrollStudent[]{}));
        } catch (Exception e) {
            e.printStackTrace();
            return error(e.getMessage());
        }
        return success("设置成功");
    }

    @RequestMapping("/goodStuStatistics/index")
    @ControllerInfo(value = "优秀学生统计index")
    public String stuStatisticsIndex(ModelMap map) {
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {
        });
        if (CollectionUtils.isEmpty(acadyearList)) {
            return errorFtl(map, "学年学期不存在");
        }
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
        map.put("acadyearList", acadyearList);
        map.put("semester", semester);
        return "/exammanage/goodStu/goodStuStatisticsIndex.ftl";
    }

    @RequestMapping("/goodStuStatistics/list")
    @ControllerInfo(value = "优秀学生统计列表")
    public String stuStatisticsList(String examId, ModelMap map, HttpServletRequest request) {
        Map<String, Integer> emMap = emEnrollStudentService.findByExamIdAndHasPassAndHasGood(examId, "1", "1");
        Set<String> unitTopSet = new HashSet<>();
        List<EmGoodCountDto> emEnrollCountDtos = new ArrayList<>();
        List<EmJoinexamschInfo> emJoinexamschInfos = emJoinexamschInfoService.findByExamId(examId);
        Set<String> schoolIds = EntityUtils.getSet(emJoinexamschInfos, "schoolId");
        List<Unit> schools = SUtils.dt(unitRemoteService.findListByIds(schoolIds.toArray(new String[0])), new TR<List<Unit>>() {
        });
        for (Unit unit : schools) {
            unitTopSet.add(unit.getParentId());
        }
        Map<String, String> unitTopNameMap = getSchoolNameMap(unitTopSet);
        for (Unit school : schools) {
            EmGoodCountDto emGoodCountDto = new EmGoodCountDto();
            emGoodCountDto.setSchoolName(school.getUnitName());
            if (MapUtils.isNotEmpty(emMap) && emMap.containsKey(school.getId())) {
                emGoodCountDto.setCount(emMap.get(school.getId()));
            }
            if (unitTopNameMap.containsKey(school.getParentId())) {
                emGoodCountDto.setUnitTopName(unitTopNameMap.get(school.getParentId()));
            }
            emEnrollCountDtos.add(emGoodCountDto);
        }
        map.put("emEnrollCountDtos", emEnrollCountDtos);
        return "/exammanage/goodStu/goodStuStatisticsList.ftl";
    }

    @RequestMapping("/sports/page")
    @ControllerInfo(value = "体育成绩Tab")
    public String sportsScoreIndex(ModelMap map) {
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {
        });
        if (CollectionUtils.isEmpty(acadyearList)) {
            return errorFtl(map, "学年学期不存在");
        }
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
        map.put("acadyearList", acadyearList);
        map.put("semester", semester);
        return "/exammanage/goodStu/sportsScoreIndex.ftl";
    }

    @RequestMapping("/sports/list")
    @ControllerInfo(value = "体育成绩list")
    public String sportsScoreList(String examId, String schoolId, String seachType, String title, ModelMap map, HttpServletRequest request) {
        Pagination page = createPagination();
        List<ExammanageSportsScore> exammanageSportsScores = exammanageSportsScoreService.findByList(examId, schoolId, seachType, title, page);
        map.put("exammanageSportsScores", exammanageSportsScores);
        sendPagination(request, map, page);
        return "/exammanage/goodStu/sportsScoreList.ftl";
    }

    @ResponseBody
    @RequestMapping("/sports/save")
    @ControllerInfo(value = "保存")
    public String qualityScore(EmSportsScoreDto dto, String acadyear, String semester, String examId) {
        try {
            if (CollectionUtils.isEmpty(dto.getDtos())) {
                return error("无数据");
            }
            exammanageSportsScoreService.batchSave(acadyear, semester, examId, dto);
        } catch (Exception e) {
            e.printStackTrace();
            return error("操作失败");
        }
        return success("操作成功");
    }

    private Map<String, String> getSchoolNameMap(Set<String> schoolIds) {
        List<Unit> schools = SUtils.dt(unitRemoteService.findListByIds(schoolIds.toArray(new String[0])), new TR<List<Unit>>() {
        });
        Map<String, String> schoolNameMap = EntityUtils.getMap(schools, "id", "unitName");
        return schoolNameMap;
    }
}
