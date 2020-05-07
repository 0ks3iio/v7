package net.zdsoft.exammanage.data.action;

import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.exammanage.data.constant.ExammanageConstants;
import net.zdsoft.exammanage.data.dto.EmExamInfoSearchDto;
import net.zdsoft.exammanage.data.dto.InvigilateTeacherDto;
import net.zdsoft.exammanage.data.entity.EmExamInfo;
import net.zdsoft.exammanage.data.entity.EmOutTeacher;
import net.zdsoft.exammanage.data.entity.EmPlace;
import net.zdsoft.exammanage.data.entity.EmPlaceTeacher;
import net.zdsoft.exammanage.data.service.EmOutTeacherService;
import net.zdsoft.exammanage.data.service.EmPlaceService;
import net.zdsoft.exammanage.data.service.EmPlaceTeacherService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
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

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 监考老师查询
 */
@Controller
@RequestMapping("/exammanage/teacherInvigilated")
public class TeacherInvigilatedAction extends EmExamCommonAction {

    @Autowired
    private EmPlaceTeacherService emPlaceTeacherService;
    @Autowired
    private TeacherRemoteService teacherRemoteService;
    @Autowired
    private EmOutTeacherService emOutTeacherService;
    @Autowired
    private EmPlaceService emPlaceService;

    @RequestMapping("/index/page")
    @ControllerInfo("进入监考老师查询1111")
    public String showIndex(ModelMap map) {
        return "/exammanage/teacherInvigilate/invigilatedIndex.ftl";
    }

    @RequestMapping("/head/page")
    @ControllerInfo("进入查询首页")
    public String showHead(HttpSession httpSession, ModelMap map) {
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {
        });
        if (CollectionUtils.isEmpty(acadyearList)) {
            return errorFtl(map, "学年学期不存在");
        }
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
        map.put("acadyearList", acadyearList);
        map.put("semester", semester);
        return "/exammanage/teacherInvigilate/invigilatedHead.ftl";
    }


    @ResponseBody
    @RequestMapping("/findExamList")
    @ControllerInfo(value = "考试名称列表")
    public List<EmExamInfo> findByAcadyearSemestar(String acadyear, String semester, ModelMap map) {
        String unitId = getLoginInfo().getUnitId();
        EmExamInfoSearchDto searchDto = new EmExamInfoSearchDto();
        searchDto.setSearchAcadyear(acadyear);
        searchDto.setSearchSemester(semester);
        List<EmExamInfo> list = emExamInfoService.findExamList(null, unitId, searchDto, false);
        return list;
    }

    @RequestMapping("/list/page")
    @ControllerInfo("进入列表")
    public String showList(String examId, HttpSession httpSession, ModelMap map) {
        LoginInfo login = getLoginInfo(httpSession);
        String teacherId = login.getOwnerId();
        String unitId = login.getUnitId();
        //监考
        List<InvigilateTeacherDto> invigilatorList1 = new ArrayList<InvigilateTeacherDto>();
        //巡考
        List<InvigilateTeacherDto> invigilatorList2 = new ArrayList<InvigilateTeacherDto>();

        makeInvigilateDto(unitId, examId, teacherId, invigilatorList1, invigilatorList2);
        map.put("invigilatorList1", invigilatorList1);
        map.put("invigilatorList2", invigilatorList2);
        return "/exammanage/teacherInvigilate/invigilatedList.ftl";
    }

    /**
     * @param unitId
     * @param examId
     * @param teacherId
     * @param invigilatorList1 监考
     * @param invigilatorList2 巡考
     */
    public void makeInvigilateDto(String unitId, String examId, String teacherId,
                                  List<InvigilateTeacherDto> invigilatorList1, List<InvigilateTeacherDto> invigilatorList2) {

        List<EmPlaceTeacher> emplaceTeacherList = emPlaceTeacherService.findByTeacherIn(unitId, examId, teacherId);
        //组装数据
        if (CollectionUtils.isNotEmpty(emplaceTeacherList)) {
            //监考
            List<EmPlaceTeacher> emplaceTeacherList1 = new ArrayList<EmPlaceTeacher>();
            //巡考
            List<EmPlaceTeacher> emplaceTeacherList2 = new ArrayList<EmPlaceTeacher>();
            Set<String> teacherIns = new HashSet<String>();
            Set<String> teacherOuts = new HashSet<String>();
            Set<String> emPlaceIds = new HashSet<String>();
            Set<String> subjectIds = new HashSet<String>();
            for (EmPlaceTeacher item : emplaceTeacherList) {
                if (ExammanageConstants.TEACHER_TYPE1.equals(item.getType())) {
                    emplaceTeacherList1.add(item);
                } else if (ExammanageConstants.TEACHER_TYPE2.equals(item.getType())) {
                    emplaceTeacherList2.add(item);
                }
                if (StringUtils.isNotBlank(item.getTeacherIdsIn())) {
                    String[] arr = item.getTeacherIdsIn().split(",");
                    for (String s : arr) {
                        if (StringUtils.isNotBlank(s)) {
                            teacherIns.add(s);
                        }
                    }
                }
                if (StringUtils.isNotBlank(item.getTeacherIdsOut())) {
                    String[] arr = item.getTeacherIdsOut().split(",");
                    for (String s : arr) {
                        if (StringUtils.isNotBlank(s)) {
                            teacherOuts.add(s);
                        }
                    }
                }
                if (StringUtils.isNotBlank(item.getExamPlaceId())) {
                    emPlaceIds.add(item.getExamPlaceId());
                }
                if (StringUtils.isNotBlank(item.getSubjectId())) {
                    subjectIds.add(item.getSubjectId());
                }
            }
            //在校老师
            Map<String, String> teacherNamesMap1 = new LinkedHashMap<String, String>();
            if (CollectionUtils.isNotEmpty(teacherIns)) {
                List<Teacher> teacherInLists = SUtils.dt(teacherRemoteService.findListByIds(teacherIns.toArray(new String[]{})), new TR<List<Teacher>>() {
                });
                if (CollectionUtils.isNotEmpty(teacherInLists)) {
                    for (Teacher teacher : teacherInLists) {
                        teacherNamesMap1.put(teacher.getId(), teacher.getTeacherName());
                    }
                }
            }
            //外校老师
            Map<String, String> teacherNamesMap2 = new LinkedHashMap<String, String>();
            if (CollectionUtils.isNotEmpty(teacherOuts)) {
                List<EmOutTeacher> teacherOutLists = emOutTeacherService.findListByIds(teacherOuts.toArray(new String[]{}));
                if (CollectionUtils.isNotEmpty(teacherOutLists)) {
                    for (EmOutTeacher teacher : teacherOutLists) {
                        teacherNamesMap2.put(teacher.getId(), teacher.getTeacherName());
                    }
                }
            }

            //考场信息
            Map<String, EmPlace> emPlaceMap = new LinkedHashMap<String, EmPlace>();
            if (CollectionUtils.isNotEmpty(emPlaceIds)) {
                List<EmPlace> emPlaceList = emPlaceService.findListByIds(emPlaceIds.toArray(new String[]{}));
                if (CollectionUtils.isNotEmpty(emPlaceList)) {
                    emPlaceService.makePlaceName(emPlaceList);
                    for (EmPlace emPlace : emPlaceList) {
                        emPlaceMap.put(emPlace.getId(), emPlace);
                    }
                }
            }
            //科目
            Map<String, String> courseNamesMap = new LinkedHashMap<String, String>();
            if (CollectionUtils.isNotEmpty(subjectIds)) {
                List<Course> courseList = SUtils.dt(courseRemoteService.findBySubjectIdIn(subjectIds.toArray(new String[]{})), new TR<List<Course>>() {
                });
                if (CollectionUtils.isNotEmpty(courseList)) {
                    courseNamesMap = EntityUtils.getMap(courseList, "id", "subjectName");
                }
            }
            makeDtoList(emplaceTeacherList1, emPlaceMap, courseNamesMap, teacherNamesMap1, teacherNamesMap2, invigilatorList1, true);
            makeDtoList(emplaceTeacherList2, emPlaceMap, courseNamesMap, teacherNamesMap1, teacherNamesMap2, invigilatorList2, false);
        }
    }

    //组装
    private void makeDtoList(List<EmPlaceTeacher> emplaceTeacherList, Map<String, EmPlace> emPlaceMap,
                             Map<String, String> courseNamesMap, Map<String, String> teacherNamesMap1, Map<String, String> teacherNamesMap2,
                             List<InvigilateTeacherDto> invigilatorList, boolean isMakePlace) {
        //组装监考
        if (CollectionUtils.isNotEmpty(emplaceTeacherList)) {
            InvigilateTeacherDto teacherDto = null;
            for (EmPlaceTeacher item : emplaceTeacherList) {
                String teacherNames = makeTeacherName(item.getTeacherIdsIn(), item.getTeacherIdsOut(), teacherNamesMap1, teacherNamesMap2);
                teacherDto = new InvigilateTeacherDto();
                if (isMakePlace) {
                    if (emPlaceMap.containsKey(item.getExamPlaceId())) {
                        EmPlace emPlace = emPlaceMap.get(item.getExamPlaceId());
                        teacherDto.setExamPlaceCode(emPlace.getExamPlaceCode());
                        teacherDto.setExamPlaceName(emPlace.getPlaceName());
                    }
                }

                if (courseNamesMap.containsKey(item.getSubjectId())) {
                    teacherDto.setSubjectName(courseNamesMap.get(item.getSubjectId()));
                }

                teacherDto.setStartTime(item.getStartTime());
                teacherDto.setEndTime(item.getEndTime());

                teacherDto.setTeacherNames(teacherNames);

                invigilatorList.add(teacherDto);
            }
        }
    }

    private String makeTeacherName(String inIds, String outIds, Map<String, String> teacherNamesMap1, Map<String, String> teacherNamesMap2) {
        String teacherNames = "";
        if (StringUtils.isNotBlank(inIds)) {
            String[] arr = inIds.split(",");
            for (String s : arr) {
                if (StringUtils.isNotBlank(s)) {
                    if (teacherNamesMap1.containsKey(s)) {
                        teacherNames = teacherNames + "," + teacherNamesMap1.get(s);
                    }
                }
            }
        }
        if (StringUtils.isNotBlank(outIds)) {
            String[] arr = outIds.split(",");
            for (String s : arr) {
                if (StringUtils.isNotBlank(s)) {
                    if (teacherNamesMap2.containsKey(s)) {
                        if (teacherNamesMap2.containsKey(s)) {
                            teacherNames = teacherNames + "," + teacherNamesMap2.get(s);
                        }
                    }
                }
            }
        }
        if (StringUtils.isNotBlank(teacherNames)) {
            teacherNames = teacherNames.substring(1);
        }
        return teacherNames;
    }

}
