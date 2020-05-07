package net.zdsoft.exammanage.data.action;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.exammanage.data.constant.ExammanageConstants;
import net.zdsoft.exammanage.data.dto.ScoreInputDto;
import net.zdsoft.exammanage.data.dto.ScoreInputSearchDto;
import net.zdsoft.exammanage.data.entity.EmEnrollStudent;
import net.zdsoft.exammanage.data.entity.EmExamInfo;
import net.zdsoft.exammanage.data.entity.EmScoreInfo;
import net.zdsoft.exammanage.data.entity.EmSubjectInfo;
import net.zdsoft.exammanage.data.service.EmEnrollStudentService;
import net.zdsoft.exammanage.data.service.EmPlaceStudentService;
import net.zdsoft.exammanage.data.service.EmScoreInfoService;
import net.zdsoft.exammanage.data.service.EmSubjectInfoService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/exammanage/edu/score")
public class EmEduScoreAction extends EmExamCommonAction {
    @Autowired
    private EmScoreInfoService emScoreInfoService;
    @Autowired
    private EmEnrollStudentService emEnrollStudentService;
    @Autowired
    private EmSubjectInfoService emSubjectInfoService;
    @Autowired
    private CourseRemoteService courseRemoteService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private EmPlaceStudentService emPlaceStudentService;

    @RequestMapping("/page")
    @ControllerInfo(value = "成绩录入index")
    public String index(ModelMap map) {
        List<String> acadyearList = SUtils.dt(semesterRemoteService.findAcadeyearList(), new TR<List<String>>() {
        });
        if (CollectionUtils.isEmpty(acadyearList)) {
            return errorFtl(map, "学年学期不存在");
        }
        Semester semester = SUtils.dc(semesterRemoteService.getCurrentSemester(2), Semester.class);
        map.put("acadyearList", acadyearList);
        map.put("semester", semester);
        ScoreInputSearchDto searchDto = new ScoreInputSearchDto();
        map.put("type", 1);
        map.put("searchDto", searchDto);
        map.put("unitId", getLoginInfo().getUnitId());
        return "/exammanage/edu/score/scoreIndex.ftl";
    }

    @RequestMapping("/editlist")
    @ControllerInfo(value = "成绩录入index")
    public String editlist(String examId, String classId, String subjectId, ModelMap map) {
        if (StringUtils.isBlank(classId)) {
            classId = "null";
        }
        List<EmEnrollStudent> enStulist = emEnrollStudentService.findByIdsAndstate(examId, "all", classId, "1", null);
        List<ScoreInputDto> dtoList = new ArrayList<>();
        if (CollectionUtils.isEmpty(enStulist)) {
            return "/exammanage/edu/score/scoreList.ftl";
        }
        Set<String> stuIds = EntityUtils.getSet(enStulist, EmEnrollStudent::getStudentId);
        List<Student> stuList = SUtils.dt(studentRemoteService.findListByIds(stuIds.toArray(new String[0])), new TR<List<Student>>() {
        });
        List<EmSubjectInfo> subjectInfos = emSubjectInfoService.findByExamIdAndSubjectId(examId, subjectId);
        if (CollectionUtils.isNotEmpty(subjectInfos)) {
            map.put("subjectInfo", subjectInfos.get(0));
        }
        //学生考号
        Map<String, String> stuNumMap = emPlaceStudentService.findByExamIdAndStuIds(examId, stuIds.toArray(new String[0]));
        //已有的成绩 一个科目 一个学生 一个成绩
        Map<String, EmScoreInfo> infoMap = emScoreInfoService.findByStudent(examId, subjectId, stuIds.toArray(new String[0]));
        for (Student item : stuList) {
            ScoreInputDto dto = new ScoreInputDto();
            dto.setStuId(item.getId());
            dto.setStuName(item.getStudentName());
            dto.setStuCode(item.getUnitiveCode());
            dto.setClassName(item.getIdentityCard());
            dto.setClassId(item.getClassId());
            if (stuNumMap.containsKey(item.getId())) {
                dto.setStuExamNum(stuNumMap.get(item.getId()));
            }
            EmScoreInfo scoreInfo = infoMap.get(item.getId());
            if (scoreInfo != null) {
                dto.setScore(scoreInfo.getScore());
                dto.setScoreStatus(scoreInfo.getScoreStatus());
                dto.setToScore(scoreInfo.getToScore());
                dto.setScoreId(scoreInfo.getId());
            }
            dtoList.add(dto);
        }
        ;
        map.put("dtoList", dtoList);
        map.put("subjectId", subjectId);
        return "/exammanage/edu/score/scoreList.ftl";
    }

    @RequestMapping("/tablist")
    @ControllerInfo(value = "科目tab")
    public String showTablist(ModelMap map, ScoreInputSearchDto searchDto) {
        List<EmSubjectInfo> subInfos = emSubjectInfoService.findByExamId(searchDto.getExamId());
        Set<String> subIds = EntityUtils.getSet(subInfos, EmSubjectInfo::getSubjectId);
        if (CollectionUtils.isEmpty(subIds)) {
            return errorFtl(map, "考试科目不存在");
        }
        List<Course> sublist = SUtils.dt(courseRemoteService.findListByIds(subIds.toArray(new String[0])), new TR<List<Course>>() {
        });
        map.put("sublist", sublist);
        map.put("subjectId", sublist.get(0).getId());
        return "/exammanage/edu/score/scoreTablist.ftl";
    }


    @ResponseBody
    @RequestMapping("/save")
    @ControllerInfo(value = "保存成绩")
    public String lockTheExamScore(ScoreInputDto dto, String state) {
        try {
            LoginInfo loginInfo = getLoginInfo();
            String examId = dto.getExamId();
            String subjectId = dto.getSubjectId();
            List<EmScoreInfo> dtoList = dto.getDtoList();
            if (dto == null || CollectionUtils.isEmpty(dtoList)) {
                return error("没有能添加的数据");
            }
            EmExamInfo exam = emExamInfoService.findOne(examId);
            //获取科目信息
            List<EmSubjectInfo> subjectInfoList = emSubjectInfoService.findByExamIdAndSubjectId(examId, subjectId);
            if (CollectionUtils.isEmpty(subjectInfoList))
                return error("该考试科目已删除");
            EmSubjectInfo subjectInfo = subjectInfoList.get(0);
            if (!subjectInfo.getInputType().equals(dto.getInputType())) {
                return error("该考试科目信息已经改变");
            }

            Set<String> stuIds = dtoList.stream().map(EmScoreInfo::getStudentId).collect(Collectors.toSet());
            Set<String> subjectIds = new HashSet<>();
            subjectIds.add(subjectInfo.getSubjectId());

            Map<String, EmScoreInfo> infoMap = emScoreInfoService.findByStudent(examId, subjectId, stuIds.toArray(new String[0]));
            List<EmScoreInfo> insertOrupdateList = new ArrayList<>();
            EmScoreInfo newScoreInfo = null;
            String opoUserId = loginInfo.getOwnerId();
            if (ExammanageConstants.ACHI_GRADE.equals(subjectInfo.getInputType())) {
                //等第
                if (!subjectInfo.getGradeType().equals(dto.getGradeType())) {
                    return error("该考试科目信息已经改变");
                }
                for (EmScoreInfo scoreInfo : dtoList) {
                    if ("1".equals(scoreInfo.getScoreStatus())) {
                        scoreInfo.setScore("");
                    }
                    if (infoMap.containsKey(scoreInfo.getStudentId())) {
                        //修改
                        newScoreInfo = infoMap.get(scoreInfo.getStudentId());
                        newScoreInfo.setOperatorId(opoUserId);
                        newScoreInfo.setModifyTime(new Date());
                    } else {
                        newScoreInfo = makeScoreInfo(exam, subjectInfo, subjectId, opoUserId, loginInfo.getUnitId());
                    }
                    newScoreInfo.setStudentId(scoreInfo.getStudentId());
                    newScoreInfo.setClassId(scoreInfo.getClassId());
                    newScoreInfo.setScore(scoreInfo.getScore());
                    newScoreInfo.setScoreStatus(scoreInfo.getScoreStatus());
                    insertOrupdateList.add(newScoreInfo);
                }
            } else {
                //分数
                for (EmScoreInfo scoreInfo : dtoList) {
//                    if ("1".equals(scoreInfo.getScoreStatus())) {
//                        scoreInfo.setScore("0");
//                    }
                    if (infoMap.containsKey(scoreInfo.getStudentId())) {
                        //修改
                        newScoreInfo = infoMap.get(scoreInfo.getStudentId());
                        newScoreInfo.setOperatorId(opoUserId);
                        newScoreInfo.setModifyTime(new Date());
                        //分数--防止之前的数据是等第
                        newScoreInfo.setInputType(subjectInfo.getInputType());
                    } else {
                        newScoreInfo = makeScoreInfo(exam, subjectInfo, subjectId, opoUserId, loginInfo.getUnitId());
                    }
                    newScoreInfo.setStudentId(scoreInfo.getStudentId());
                    newScoreInfo.setClassId(scoreInfo.getClassId());
                    newScoreInfo.setScore(scoreInfo.getScore());
                    newScoreInfo.setScoreStatus(scoreInfo.getScoreStatus());
                    insertOrupdateList.add(newScoreInfo);
                }
            }
            if (CollectionUtils.isNotEmpty(insertOrupdateList)) {
                emScoreInfoService.saveAll(insertOrupdateList.toArray(new EmScoreInfo[]{}));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return error("操作失败");
        }
        return success("操作成功");
    }


    private EmScoreInfo makeScoreInfo(EmExamInfo exam, EmSubjectInfo subjectInfo,
                                      String subjectId, String userId, String unitId) {
        EmScoreInfo info = new EmScoreInfo();
        info.setAcadyear(exam.getAcadyear());
        info.setSemester(exam.getSemester());
        info.setSubjectId(subjectId);
        info.setCreationTime(new Date());
        info.setExamId(exam.getId());
        info.setId(UuidUtils.generateUuid());
        info.setModifyTime(new Date());
        info.setOperatorId(userId);
        info.setUnitId(unitId);
        info.setInputType(subjectInfo.getInputType());
        return info;
    }

    @ResponseBody
    @RequestMapping("/clsList")
    public List<Clazz> queryClsList(String acadyear, String semester, String examId, String schoolId) {
        List<Clazz> clslist = new ArrayList<>();
        if (StringUtils.isBlank(schoolId) || StringUtils.isBlank(examId)) {
            return clslist;
        }
        clslist = emEnrollStudentService.findClsByExamIdAndSchId(acadyear, examId, schoolId);
        return clslist;
    }

}
