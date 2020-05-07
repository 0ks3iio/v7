package net.zdsoft.exammanage.data.action;

import com.google.common.collect.Lists;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.exammanage.data.entity.EmEnrollStuCount;
import net.zdsoft.exammanage.data.entity.EmEnrollStudent;
import net.zdsoft.exammanage.data.entity.EmExamInfo;
import net.zdsoft.exammanage.data.entity.EmJoinexamschInfo;
import net.zdsoft.exammanage.data.service.EmEnrollStuCountService;
import net.zdsoft.exammanage.data.service.EmEnrollStudentService;
import net.zdsoft.exammanage.data.service.EmExamInfoService;
import net.zdsoft.exammanage.data.service.EmJoinexamschInfoService;
import net.zdsoft.exammanage.data.utils.ExamUtils;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/exammanage/edu/examStu")
public class EmEnrollStudentsAction extends EmExamCommonAction {

    @Autowired
    private EmEnrollStudentService emEnrollStudentService;
    @Autowired
    private SchoolRemoteService schoolRemoteService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private EmExamInfoService emExamInfoService;
    @Autowired
    private EmJoinexamschInfoService emJoinexamschInfoService;
    @Autowired
    private EmEnrollStuCountService emEnrollStuCountService;

    @RequestMapping("index/page")
    @ControllerInfo(value = "学生报名Tab页")
    public String enrollstuTab(ModelMap map) {
        return "/exammanage/edu/enrollStudent/enrollStudentTab.ftl";
    }

    @ResponseBody
    @RequestMapping("/enrollstu/examnamelist")
    @ControllerInfo(value = "考试名称列表")
    public List<EmExamInfo> examNameList(String acadyear, String searchSemester, ModelMap map) {
        LoginInfo loginInfo = getLoginInfo();
        String unitId = loginInfo.getUnitId();
        List<EmExamInfo> emExamInfos = emExamInfoService.findByUnitIdAndAcadyear(unitId, acadyear, searchSemester);
        return emExamInfos;
    }

    @ResponseBody
    @RequestMapping("/enrollstu/schoolnamelist")
    @ControllerInfo(value = "学校名称列表")
    public List<School> schoolNameList(String examId, ModelMap map) {
        List<EmJoinexamschInfo> emJoinexamschInfos = emJoinexamschInfoService.findByExamId(examId);
        Set<String> schoolIds = EntityUtils.getSet(emJoinexamschInfos, "schoolId");
        List<School> schools = SUtils.dt(schoolRemoteService.findListByIds(schoolIds.toArray(new String[0])), new TR<List<School>>() {
        });
        return schools;
    }

    @RequestMapping("/enrollstu/index")
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
        return "/exammanage/edu/enrollStudent/enrollStudentIndex.ftl";
    }

    @RequestMapping("/enrollstu/list")
    @ControllerInfo(value = "报名学生列表")
    public String enrollstuList(String examId, String schoolId, String state, ModelMap map, HttpServletRequest request) {
        String url = "/exammanage/edu/enrollStudent/enrollStudentList.ftl";
        if (StringUtils.isEmpty(examId) || StringUtils.isEmpty(schoolId)) {
            return url;
        }
        List<EmEnrollStudent> enrollStudentList = Lists.newArrayList();
//		List<EmEnrollStudent> enrollStudentNewList = Lists.newArrayList();
        Pagination page = createPagination();
        enrollStudentList = emEnrollStudentService.findByIdsAndstate(examId, schoolId, null, state, page);
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

            //排序
//			Collections.sort(enrollStudentList,new Comparator<EmEnrollStudent>() {
//				Collator collator = Collator.getInstance(Locale.CHINA);
//				
//				@Override
//				public int compare(EmEnrollStudent o1, EmEnrollStudent o2) {
//					if (o1.getStudent() == null || o2.getStudent() == null) {
//						return 0;
//					}
//					
//					if (o1.getClass() == null || o2.getClass() == null) {
//						return 0;
//					}
//					
//					if (o1.getSchoolId().equals(o2.getSchoolId())) {
//						if (o1.getClazz().getSection().equals(o2.getClazz().getSection())) {
//							if (o1.getClazz().getAcadyear().equals(o2.getClazz().getAcadyear())) {
//								if (o1.getClazz().getClassCode().equals(o2.getClazz().getClassCode())) {
//									if (o1.getStudent().getUnitiveCode().equals(o2.getStudent().getUnitiveCode())) {
//										return 0;
//									} else {
//										return o1.getStudent().getUnitiveCode().compareTo(o2.getStudent().getUnitiveCode());
//									}
//								} else {
//									return Integer.parseInt(o1.getClazz().getClassCode()) - Integer.parseInt(o2.getClazz().getClassCode());
//								}
//							} else {
//								return o2.getClazz().getAcadyear().compareTo(o1.getClazz().getAcadyear());
//							}
//						} else {
//							return o1.getClazz().getSection()-o2.getClazz().getSection();
//						}
//					} else {
//						return collator.getCollationKey(o1.getSchoolName()).compareTo(collator.getCollationKey(o2.getSchoolName()));
//					}
//				}
//				
//			});

            //假分页
//			page.setMaxRowCount(enrollStudentList.size());
//			Integer pageSize = page.getPageSize();
//			Integer pageIndex = page.getPageIndex();
//			for(int i=pageSize*(pageIndex-1);i<enrollStudentList.size();i++){
//				if(i<pageSize*pageIndex&&i>=pageSize*(pageIndex-1)){
//					enrollStudentNewList.add(enrollStudentList.get(i));
//				} else {
//					break;
//				}
//			}
        }

        map.put("enrollStudentList", enrollStudentList);
        sendPagination(request, map, page);
        return url;
    }


    @ResponseBody
    @RequestMapping("/enrollstu/save")
    @ControllerInfo(value = "审批保存")
    public String enrollstuSave(String examId, String stuIds, String state, ModelMap map) {
        String[] studentIds = null;
        if ("allpass".equals(state)) {
            List<EmEnrollStudent> emEnrollStudents = emEnrollStudentService.findByExamId(examId);
            studentIds = EntityUtils.getList(emEnrollStudents, "studentId").toArray(new String[emEnrollStudents.size()]);
        } else {
            studentIds = stuIds.split(",");
        }
        if (studentIds.length == 0) {
            return error("请先导入报名学生！");
        }
        try {
            emEnrollStudentService.saveByIds(examId, state, studentIds);
        } catch (Exception e) {
            e.printStackTrace();
            return error(e.getMessage());
        }
        return success("保存成功");
    }

    @RequestMapping("/stustatistics/index")
    @ControllerInfo(value = "报名学生统计index")
    public String stuStatisticsIndex(ModelMap map) {
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {
        });
        if (CollectionUtils.isEmpty(acadyearList)) {
            return errorFtl(map, "学年学期不存在");
        }
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
        map.put("acadyearList", acadyearList);
        map.put("semester", semester);
        return "/exammanage/edu/enrollStudent/stuStatisticsIndex.ftl";
    }

    @RequestMapping("/stustatistics/list")
    @ControllerInfo(value = "报名学生统计列表")
    public String stuStatisticsList(String examId, ModelMap map, HttpServletRequest request) {
        Pagination page = createPagination();
        List<EmEnrollStuCount> emEnrollStuCounts = emEnrollStuCountService.findByExamId(examId, page);
        Set<String> schoolIdSet = EntityUtils.getSet(emEnrollStuCounts, "schoolId");
        Map<String, String> schoolNameMap = getSchoolNameMap(schoolIdSet);
        for (EmEnrollStuCount count : emEnrollStuCounts) {
            count.setOtherNum(count.getSumCount() - count.getPassNum() - count.getNotPassNum());
            count.setSchoolName(schoolNameMap.get(count.getSchoolId()));
        }
        map.put("emEnrollStuCounts", emEnrollStuCounts);
        map.put("pagination", page);
        sendPagination(request, map, page);
        return "/exammanage/edu/enrollStudent/stuStatisticsList.ftl";
    }

    private Map<String, String> getSchoolNameMap(Set<String> schoolIds) {
        List<School> schools = SUtils.dt(schoolRemoteService.findListByIds(schoolIds.toArray(new String[0])), new TR<List<School>>() {
        });
        Map<String, String> schoolNameMap = EntityUtils.getMap(schools, "id", "schoolName");
        return schoolNameMap;
    }
}
