package net.zdsoft.exammanage.data.action;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.exammanage.data.constant.ExammanageConstants;
import net.zdsoft.exammanage.data.dto.*;
import net.zdsoft.exammanage.data.entity.*;
import net.zdsoft.exammanage.data.service.*;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
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
import java.util.*;


@Controller
@RequestMapping("/examanalysis")
public class EmExamScoreReportAction extends EmExamCommonAction {
    @Autowired
    private EmScoreInfoService emScoreInfoService;
    @Autowired
    private EmStatService emStatService;
    @Autowired
    private EmStatObjectService emStatObjectService;
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private EmStatRangeService emStatRangeService;
    @Autowired
    private EmSpaceItemService emSpaceItemService;
    @Autowired
    private EmStatSpaceService emStatSpaceService;
    @Autowired
    private EmStatParmService emStatParmService;
    @Autowired
    private EmExamNumService emExamNumService;

    @RequestMapping("/examStudent/index/page")
    @ControllerInfo("学生成绩分析Index")
    public String examStudentIndex(ModelMap map) {
        String unitId = getLoginInfo().getUnitId();
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId), new TR<List<Grade>>() {
        });
        map.put("gradeList", gradeList);
        return "/examanalysis/examStudent/examStudentIndex.ftl";
    }

    @RequestMapping("/examStudent/List/page")
    @ControllerInfo("学生成绩分析List")
    public String examStudentList(String examId, String classId, String gradeId, ModelMap map) {
        if (StringUtils.isBlank(examId)) {
            return promptFlt(map, "请先选择某一个考试");
        }
        EmStatObject statObject = findStatObject(examId);
        if (statObject == null) {
            //没有统计--返回错误页面
            return promptFlt(map, "暂时没有统计的数据；请先去统计数据");
//			return errorFtl(map,"没有统计数据");
        }
        List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(classId), new TR<List<Student>>() {
        });
        List<EmSubjectInfo> subjectInfoList = emSubjectInfoService.findByExamId(examId);
        Map<String, Map<String, McodeDetail>> mcodeDetailMap = new HashMap<String, Map<String, McodeDetail>>();
        for (EmSubjectInfo emSubjectInfo : subjectInfoList) {
            if (emSubjectInfo.getInputType().equals(EmScoreInfo.ACHI_GRADE) && StringUtils.isNotBlank(emSubjectInfo.getGradeType())) {
                if (mcodeDetailMap.containsKey(emSubjectInfo.getGradeType())) {
                    continue;
                } else {
                    List<McodeDetail> gradeTypeList = SUtils.dt(mcodeRemoteService.findByMcodeIds(emSubjectInfo.getGradeType()), new TypeReference<List<McodeDetail>>() {
                    });
                    Map<String, McodeDetail> mcodeMap = EntityUtils.getMap(gradeTypeList, "thisId");
                    mcodeDetailMap.put(emSubjectInfo.getGradeType(), mcodeMap);
                }
            }
        }
        List<String> studentIds = EntityUtils.getList(studentList, "id");
        Map<String, EmStat> emStatMap = new LinkedHashMap<String, EmStat>();
        List<EmStat> emStatList = emStatService.findByStudentIds(statObject.getId(), examId, null, studentIds.toArray(new String[0]));
        for (EmStat emStat : emStatList) {
            emStatMap.put(emStat.getStudentId() + "_" + emStat.getSubjectId(), emStat);
        }
        Map<String, EmScoreInfo> scoreInfoMap = new HashMap<String, EmScoreInfo>();
        if (MapUtils.isNotEmpty(mcodeDetailMap)) {
            List<EmScoreInfo> scoreInfoList = emScoreInfoService.findByExamIdAndUnitId(examId, getLoginInfo().getUnitId());
            for (EmScoreInfo emScoreInfo : scoreInfoList) {
                if (emScoreInfo.getInputType().equals(EmScoreInfo.ACHI_GRADE)) {
                    scoreInfoMap.put(emScoreInfo.getStudentId() + "_" + emScoreInfo.getSubjectId(), emScoreInfo);
                }
            }
        }
        List<EmStudentStatDto> studentStatDtoList = new ArrayList<EmStudentStatDto>();
        EmStudentStatDto dto = null;
        List<String> scoresList = null;
        for (Student student : studentList) {
            dto = new EmStudentStatDto();
            scoresList = new ArrayList<String>();
            dto.setStudentId(student.getId());
            dto.setStudentName(student.getStudentName());
            dto.setStudentCode(student.getStudentCode());
            EmStat emStat = emStatMap.get(student.getId() + "_" + EmStat.STAT_TOTAL);
            if (emStat != null) {
                dto.setClassRank(emStat.getClassRank());
                dto.setGradeRank(emStat.getGradeRank());
                scoresList.add(String.valueOf(emStat.getScore()));
            } else {
                scoresList.add("\\");
            }
            for (EmSubjectInfo emSubjectInfo : subjectInfoList) {
                if (emSubjectInfo.getInputType().equals(EmScoreInfo.ACHI_GRADE)) {
                    EmScoreInfo emScoreInfo = scoreInfoMap.get(student.getId() + "_" + emSubjectInfo.getSubjectId());
                    scoresList.add(mcodeDetailMap.get(emSubjectInfo.getGradeType()).get(emScoreInfo.getScore()).getMcodeContent());
                } else {
                    EmStat stat = emStatMap.get(student.getId() + "_" + emSubjectInfo.getSubjectId());
                    if (stat != null) {
                        scoresList.add(String.valueOf(stat.getScore()));
                    } else {
                        scoresList.add("\\");
                    }
                }
            }
            dto.setScoresList(scoresList);
            studentStatDtoList.add(dto);
        }
        final Integer lengths = studentStatDtoList.size() + 1;
        Collections.sort(studentStatDtoList, new Comparator<EmStudentStatDto>() {
            @Override
            public int compare(EmStudentStatDto o1, EmStudentStatDto o2) {
                Integer dto1 = o1.getClassRank();
                Integer dto2 = o2.getClassRank();
                if (o1.getClassRank() == 0) {
                    dto1 = lengths;
                }
                if (o2.getClassRank() == 0) {
                    dto2 = lengths;
                }
                Integer d = dto1 - dto2;
                return d;
            }
        });
        map.put("subjectInfoList", subjectInfoList);
        map.put("studentStatDtoList", studentStatDtoList);
        return "/examanalysis/examStudent/examStudentList.ftl";
    }

    @RequestMapping("/examStudent/examScores/page")
    @ControllerInfo("详情查看")
    public String examScoresList(String examId, String studentId, ModelMap map) {
        map.put("examId", examId);
        map.put("studentId", studentId);
        return "/examanalysis/examStudent/examStudentTab.ftl";
    }

    @RequestMapping("/examStudent/thisExamScore/page")
    @ControllerInfo("当前考试成绩")
    public String thisExamScore(String examId, String studentId, String classId, ModelMap map) {
        EmStatObject statObject = findStatObject(examId);
        String unitId = getLoginInfo().getUnitId();
        Student student = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
        List<EmSubjectInfo> subjectInfoList = emSubjectInfoService.findByExamId(examId);
        String subjectNames = "";
        String gradeAverage = "";
        String classAverage = "";
        String ownScores = "";
        List<EmStat> emStatList = emStatService.findByStudentIds(statObject.getId(), examId, null, new String[]{studentId});
        Map<String, EmStat> emStatMap = EntityUtils.getMap(emStatList, "subjectId");
        List<EmExamStudentDto> examStudentDtosList = new ArrayList<EmExamStudentDto>();
        EmExamStudentDto dto = null;
        if (CollectionUtils.isNotEmpty(subjectInfoList)) {
            EmStat emStatAll = emStatMap.get(EmStat.STAT_TOTAL);
            EmStatRange classStatRangeAll = emStatRangeService.findByExamIdAndSubIdAndRangIdAndType(statObject.getId(), examId, EmStat.STAT_TOTAL, classId, EmStatRange.RANGE_CLASS);
            EmStatRange schoolStatRangeAll = emStatRangeService.findByExamIdAndSubIdAndRangIdAndType(statObject.getId(), examId, EmStat.STAT_TOTAL, unitId, EmStatRange.RANGE_SCHOOL);
            EmExamStudentDto stuDto = new EmExamStudentDto();
            stuDto.setSubjectName("总分");
            if (emStatAll != null) {
                stuDto.setTotal(String.valueOf(emStatAll.getScore()));
                stuDto.setGradeRanking(String.valueOf(emStatAll.getGradeRank()));
                stuDto.setClassRanking(String.valueOf(emStatAll.getClassRank()));
            }
            if (schoolStatRangeAll != null) {
                stuDto.setGradeAverage(String.valueOf(schoolStatRangeAll.getAvgScore()));
            }
            if (classStatRangeAll != null) {
                stuDto.setClassAverage(String.valueOf(classStatRangeAll.getAvgScore()));
                stuDto.setClassMax(String.valueOf(classStatRangeAll.getMaxScore()));
                stuDto.setClassMin(String.valueOf(classStatRangeAll.getMinScore()));
            }
            examStudentDtosList.add(stuDto);
            for (EmSubjectInfo emSubjectInfo : subjectInfoList) {
                if (emSubjectInfo.getInputType().equals(EmScoreInfo.ACHI_GRADE)) {
                    continue;
                }
                dto = new EmExamStudentDto();
                dto.setSubjectName(emSubjectInfo.getCourseName());
                EmStatRange classStatRange = emStatRangeService.findByExamIdAndSubIdAndRangIdAndType(statObject.getId(), examId, emSubjectInfo.getSubjectId(), classId, EmStatRange.RANGE_CLASS);
                EmStatRange schoolStatRange = emStatRangeService.findByExamIdAndSubIdAndRangIdAndType(statObject.getId(), examId, emSubjectInfo.getSubjectId(), unitId, EmStatRange.RANGE_SCHOOL);
                EmStat emStat = emStatMap.get(emSubjectInfo.getSubjectId());
                subjectNames += "'" + emSubjectInfo.getCourseName() + "',";
                if (schoolStatRange != null) {
                    gradeAverage += schoolStatRange.getAvgScore() + ",";
                    dto.setGradeAverage(String.valueOf(schoolStatRange.getAvgScore()));
                } else {
                    gradeAverage += 0 + ",";
                }
                if (classStatRange != null) {
                    classAverage += classStatRange.getAvgScore() + ",";
                    dto.setClassAverage(String.valueOf(classStatRange.getAvgScore()));
                    dto.setClassMax(String.valueOf(classStatRange.getMaxScore()));
                    dto.setClassMin(String.valueOf(classStatRange.getMinScore()));
                } else {
                    classAverage += 0 + ",";
                }
                if (emStat != null) {
                    ownScores += emStat.getScore() + ",";
                    dto.setTotal(String.valueOf(emStat.getScore()));
                    dto.setGradeRanking(String.valueOf(emStat.getGradeRank()));
                    dto.setClassRanking(String.valueOf(emStat.getClassRank()));
                } else {
                    ownScores += 0 + ",";
                }
                examStudentDtosList.add(dto);
            }
            subjectNames = subjectNames.substring(0, subjectNames.length() - 1);
            gradeAverage = gradeAverage.substring(0, gradeAverage.length() - 1);
            classAverage = classAverage.substring(0, classAverage.length() - 1);
            ownScores = ownScores.substring(0, ownScores.length() - 1);
        }
        map.put("studnetName", student.getStudentName());
        map.put("examStudentDtosList", examStudentDtosList);
        map.put("subjectNames", "[" + subjectNames + "]");
        map.put("gradeAverage", "[" + gradeAverage + "]");
        map.put("classAverage", "[" + classAverage + "]");
        map.put("ownScores", "[" + ownScores + "]");
        return "/examanalysis/examStudent/examStudentTes.ftl";
    }

    @RequestMapping("/examStudent/otherExamScore/page")
    @ControllerInfo("本学期历次考试成绩")
    public String otherExamScore(String gradeId, String studentId, String classId, ModelMap map) {
        Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
        List<EmExamInfo> examInfoList = getThisExamInfoList(gradeId);
        Set<String> unitIds = new HashSet<String>();
        Unit djunit = SUtils.dc(unitRemoteService.findTopUnit(getLoginInfo().getUnitId()), Unit.class);
        unitIds.add(getLoginInfo().getUnitId());
        if (djunit != null) {
            unitIds.add(djunit.getId());
        }
        List<Course> courseList = SUtils.dt(courseRemoteService.findByUnitIdIn(unitIds.toArray(new String[]{}), grade.getGradeCode().substring(0, 1)), new TR<List<Course>>() {
        });
        map.put("courseList", courseList);
        map.put("examInfoList", examInfoList);
        map.put("studentId", studentId);
        map.put("classId", classId);
        return "/examanalysis/examStudent/examStudentOes.ftl";
    }


    @RequestMapping("/examStudent/otherAllExamScoreDiv/page")
    @ControllerInfo("本学期历次考试成绩mychart02Div")
    public String otherAllExamScoreDiv(String gradeId, String studentId, String classId, String analysisType, ModelMap map) {
        Student student = SUtils.dc(studentRemoteService.findOneById(studentId), Student.class);
        List<EmExamInfo> examInfoList = getThisExamInfoList(gradeId);
        String examNames = "";
        String scores = "";
        String titleName = "";
        if ("classRank".equals(analysisType) || "gradeRank".equals(analysisType)) {
            titleName = "排名";
        } else {
            titleName = "成绩";
        }
        if (examInfoList != null) {
            for (int i = examInfoList.size() - 1; i >= 0; i--) {
                EmExamInfo emExamInfo = examInfoList.get(i);
                EmStatObject statObject = findStatObject(emExamInfo.getId());
                if (statObject == null) {
                    continue;
                }
                if ("all".equals(analysisType) || "classRank".equals(analysisType) || "gradeRank".equals(analysisType)) {
                    EmStat emStat = emStatService.findByExamIdAndSubjectIdAndStudentId(statObject.getId(), emExamInfo.getId(), EmStat.STAT_TOTAL, studentId);
                    if (emStat != null) {
                        if ("all".equals(analysisType)) {
                            scores += emStat.getScore() + ",";
                        }
                        if ("classRank".equals(analysisType)) {
                            scores += emStat.getClassRank() + ",";
                        }
                        if ("gradeRank".equals(analysisType)) {
                            scores += emStat.getGradeRank() + ",";
                        }
                    } else {
                        continue;
                    }
                } else {
                    EmStat emStat = emStatService.findByExamIdAndSubjectIdAndStudentId(statObject.getId(), emExamInfo.getId(), analysisType, studentId);
                    if (emStat != null) {
                        scores += emStat.getScore() + ",";
                    } else {
                        continue;
                    }
                }
                examNames += "'" + emExamInfo.getExamName() + "',";
            }
            if (StringUtils.isNotBlank(examNames) && StringUtils.isNotBlank(scores)) {
                examNames = examNames.substring(0, examNames.length() - 1);
                scores = scores.substring(0, scores.length() - 1);
            }
        }
        map.put("studentName", student.getStudentName());
        map.put("examNames", "[" + examNames + "]");
        map.put("scores", "[" + scores + "]");
        map.put("titleName", titleName);
        return "/examanalysis/examStudent/examStudentTypeDiv.ftl";
    }

    @RequestMapping("/examStudent/otherExamScoreDiv/page")
    @ControllerInfo("本学期历次考试成绩TableDiv")
    public String otherExamScoreDiv(String examId, String studentId, String classId, ModelMap map) {
        if (StringUtils.isBlank(examId)) {
            return promptFlt(map, "请先选择某一个考试");
        }
        EmStatObject statObject = findStatObject(examId);
        if (statObject == null) {
            //没有统计--返回错误页面
            return promptFlt(map, "暂时没有统计的数据；请先去统计数据");
//			return errorFtl(map,"没有统计数据");
        }
        String unitId = getLoginInfo().getUnitId();
        List<EmSubjectInfo> subjectInfoList = emSubjectInfoService.findByExamId(examId);
        List<EmStat> emStatList = emStatService.findByStudentIds(statObject.getId(), examId, null, new String[]{studentId});
        Map<String, EmStat> emStatMap = EntityUtils.getMap(emStatList, "subjectId");
        List<EmExamStudentDto> examStudentDtoList = new ArrayList<EmExamStudentDto>();
        EmExamStudentDto dto = null;
        if (CollectionUtils.isNotEmpty(subjectInfoList)) {
            EmStat emStatAll = emStatMap.get(EmStat.STAT_TOTAL);
            EmStatRange classStatRangeAll = emStatRangeService.findByExamIdAndSubIdAndRangIdAndType(statObject.getId(), examId, EmStat.STAT_TOTAL, classId, EmStatRange.RANGE_CLASS);
            EmStatRange schoolStatRangeAll = emStatRangeService.findByExamIdAndSubIdAndRangIdAndType(statObject.getId(), examId, EmStat.STAT_TOTAL, unitId, EmStatRange.RANGE_SCHOOL);
            EmExamStudentDto stuDto = new EmExamStudentDto();
            stuDto.setSubjectName("总分");
            if (emStatAll != null) {
                stuDto.setTotal(String.valueOf(emStatAll.getScore()));
                stuDto.setGradeRanking(String.valueOf(emStatAll.getGradeRank()));
                stuDto.setClassRanking(String.valueOf(emStatAll.getClassRank()));
            }
            if (schoolStatRangeAll != null) {
                stuDto.setGradeAverage(String.valueOf(schoolStatRangeAll.getAvgScore()));
            }
            if (classStatRangeAll != null) {
                stuDto.setClassAverage(String.valueOf(classStatRangeAll.getAvgScore()));
                stuDto.setClassMax(String.valueOf(classStatRangeAll.getMaxScore()));
                stuDto.setClassMin(String.valueOf(classStatRangeAll.getMinScore()));
            }
            examStudentDtoList.add(stuDto);
            for (EmSubjectInfo emSubjectInfo : subjectInfoList) {
                if (emSubjectInfo.getInputType().equals(EmScoreInfo.ACHI_GRADE)) {
                    continue;
                }
                dto = new EmExamStudentDto();
                dto.setSubjectName(emSubjectInfo.getCourseName());
                EmStatRange classStatRange = emStatRangeService.findByExamIdAndSubIdAndRangIdAndType(statObject.getId(), examId, emSubjectInfo.getSubjectId(), classId, EmStatRange.RANGE_CLASS);
                EmStatRange schoolStatRange = emStatRangeService.findByExamIdAndSubIdAndRangIdAndType(statObject.getId(), examId, emSubjectInfo.getSubjectId(), unitId, EmStatRange.RANGE_SCHOOL);
                EmStat emStat = emStatMap.get(emSubjectInfo.getSubjectId());
                if (schoolStatRange != null) {
                    dto.setGradeAverage(String.valueOf(schoolStatRange.getAvgScore()));
                }
                if (classStatRange != null) {
                    dto.setClassAverage(String.valueOf(classStatRange.getAvgScore()));
                    dto.setClassMax(String.valueOf(classStatRange.getMaxScore()));
                    dto.setClassMin(String.valueOf(classStatRange.getMinScore()));
                }
                if (emStat != null) {
                    dto.setTotal(String.valueOf(emStat.getScore()));
                    dto.setGradeRanking(String.valueOf(emStat.getGradeRank()));
                    dto.setClassRanking(String.valueOf(emStat.getClassRank()));
                }
                examStudentDtoList.add(dto);
            }
        }
        map.put("examStudentDtoList", examStudentDtoList);
        return "/examanalysis/examStudent/examStudentTableDiv.ftl";
    }

    public List<EmExamInfo> getThisExamInfoList(String gradeId) {
        Semester semesterObj = SUtils.dc(semesterRemoteService.getCurrentSemester(0), Semester.class);
        List<Clazz> classIds = SUtils.dt(classRemoteService.findByGradeIdSortAll(gradeId), new TR<List<Clazz>>() {
        });
        List<EmExamInfo> examInfoList = new ArrayList<EmExamInfo>();
        if (CollectionUtils.isNotEmpty(classIds)) {
            Set<String> clsId = EntityUtils.getSet(classIds, "id");
            List<String> examList = emClassInfoService.findExamIdByClassIds(clsId.toArray(new String[]{}));
            examInfoList = emExamInfoService.findListByIds(examList.toArray(new String[]{}));
        }
        if (semesterObj != null && CollectionUtils.isNotEmpty(examInfoList)) {
            Iterator it = examInfoList.iterator();
            while (it.hasNext()) {
                EmExamInfo emExamInfo = (EmExamInfo) it.next();
                if (!(emExamInfo.getAcadyear().equals(semesterObj.getAcadyear()) && emExamInfo.getSemester().equals(String.valueOf(semesterObj.getSemester())))) {
                    it.remove();
                }
            }
        }
        return examInfoList;
    }

    public List<EmExamStudentDto> getExamStudentDto(String examId, String studentId, String classId) {
        EmStatObject statObject = findStatObject(examId);
        String unitId = getLoginInfo().getUnitId();
        List<EmSubjectInfo> subjectInfoList = emSubjectInfoService.findByExamId(examId);
        List<EmStat> emStatList = emStatService.findByStudentIds(statObject.getId(), null, examId, new String[]{studentId});
        Map<String, EmStat> emStatMap = EntityUtils.getMap(emStatList, "subjectId");
        List<EmExamStudentDto> examStudentDtosList = new ArrayList<EmExamStudentDto>();
        EmExamStudentDto dto = null;
        if (CollectionUtils.isNotEmpty(subjectInfoList)) {
            EmStat emStatAll = emStatMap.get(EmStat.STAT_TOTAL);
            EmStatRange classStatRangeAll = emStatRangeService.findByExamIdAndSubIdAndRangIdAndType(statObject.getId(), examId, EmStat.STAT_TOTAL, classId, EmStatRange.RANGE_CLASS);
            EmStatRange schoolStatRangeAll = emStatRangeService.findByExamIdAndSubIdAndRangIdAndType(statObject.getId(), examId, EmStat.STAT_TOTAL, unitId, EmStatRange.RANGE_SCHOOL);
            EmExamStudentDto stuDto = new EmExamStudentDto();
            stuDto.setSubjectName("总分");
            if (emStatAll != null) {
                stuDto.setTotal(String.valueOf(emStatAll.getScore()));
                stuDto.setGradeRanking(String.valueOf(emStatAll.getGradeRank()));
                stuDto.setClassRanking(String.valueOf(emStatAll.getClassRank()));
            }
            if (schoolStatRangeAll != null) {
                stuDto.setGradeAverage(String.valueOf(schoolStatRangeAll.getAvgScore()));
            }
            if (classStatRangeAll != null) {
                stuDto.setClassAverage(String.valueOf(classStatRangeAll.getAvgScore()));
                stuDto.setClassMax(String.valueOf(classStatRangeAll.getMaxScore()));
                stuDto.setClassMin(String.valueOf(classStatRangeAll.getMinScore()));
            }
            examStudentDtosList.add(stuDto);
            for (EmSubjectInfo emSubjectInfo : subjectInfoList) {
                if (emSubjectInfo.getInputType().equals(EmScoreInfo.ACHI_GRADE)) {
                    continue;
                }
                dto = new EmExamStudentDto();
                dto.setSubjectName(emSubjectInfo.getCourseName());
                EmStatRange classStatRange = emStatRangeService.findByExamIdAndSubIdAndRangIdAndType(statObject.getId(), examId, emSubjectInfo.getSubjectId(), classId, EmStatRange.RANGE_CLASS);
                EmStatRange schoolStatRange = emStatRangeService.findByExamIdAndSubIdAndRangIdAndType(statObject.getId(), examId, emSubjectInfo.getSubjectId(), unitId, EmStatRange.RANGE_SCHOOL);
                EmStat emStat = emStatMap.get(emSubjectInfo.getSubjectId());
                if (schoolStatRange != null) {
                    dto.setGradeAverage(String.valueOf(schoolStatRange.getAvgScore()));
                }
                if (classStatRange != null) {
                    dto.setClassAverage(String.valueOf(classStatRange.getAvgScore()));
                    dto.setClassMax(String.valueOf(classStatRange.getMaxScore()));
                    dto.setClassMin(String.valueOf(classStatRange.getMinScore()));
                }
                if (emStat != null) {
                    dto.setTotal(String.valueOf(emStat.getScore()));
                    dto.setGradeRanking(String.valueOf(emStat.getGradeRank()));
                    dto.setClassRanking(String.valueOf(emStat.getClassRank()));
                }
                examStudentDtosList.add(dto);
            }
        }
        return examStudentDtosList;
    }

    @ResponseBody
    @RequestMapping("/findClassIdByGradeId")
    @ControllerInfo("根据年级取得班级列表")
    public List<Clazz> findClassIdByGradeId(String gradeId) {
        String unitId = getLoginInfo().getUnitId();
        List<Clazz> clazzsList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(unitId, gradeId), new TR<List<Clazz>>() {
        });
        return clazzsList;
    }

    @RequestMapping("/examGrade/index/page")
    @ControllerInfo("班级成绩查询统计结果")
    public String showGradeScoreIndex(ModelMap map) {
        String unitId = getLoginInfo().getUnitId();
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId), new TR<List<Grade>>() {
        });
        map.put("gradeList", gradeList);
        return "/examanalysis/examGrade/examGradeIndex.ftl";

    }

    @ResponseBody
    @RequestMapping("/findExamIdByGradeId")
    @ControllerInfo("根据年级取得考试列表")
    public List<EmExamInfo> findExamIdByGradeId(String gradeId) {
        //根据年级id查询班级id
        List<Clazz> classIds = SUtils.dt(classRemoteService.findByGradeIdSortAll(gradeId), new TR<List<Clazz>>() {
        });
        List<EmExamInfo> infoList = new ArrayList<EmExamInfo>();
        if (CollectionUtils.isNotEmpty(classIds)) {
            Set<String> clsId = EntityUtils.getSet(classIds, "id");
            List<String> examList = emClassInfoService.findExamIdByClassIds(clsId.toArray(new String[]{}));
            infoList = emExamInfoService.findListByIdsNoDel(examList.toArray(new String[]{}));
        }
        return infoList;
    }

    @ResponseBody
    @RequestMapping("/findClassIdByExamId")
    @ControllerInfo("根据年级考试取得班级列表")
    public List<Clazz> findClassIdByExamId(String gradeId, String examId) {
        //根据年级id查询班级id
        List<Clazz> classIds = SUtils.dt(classRemoteService.findByGradeIdSortAll(gradeId), new TR<List<Clazz>>() {
        });
        List<Clazz> infoList = new ArrayList<Clazz>();
        if (CollectionUtils.isNotEmpty(classIds)) {
            List<EmClassInfo> emClassInfoList = emClassInfoService.findByExamIdAndSchoolId(examId, classIds.get(0).getSchoolId());
            if (CollectionUtils.isNotEmpty(emClassInfoList)) {
                Set<String> clsId = EntityUtils.getSet(emClassInfoList, "classId");
                for (Clazz clazz : classIds) {
                    if (clsId.contains(clazz.getId())) {
                        infoList.add(clazz);
                    }
                }
            }
        }
        return infoList;
    }

    @ResponseBody
    @RequestMapping("/findSubjectByExamId")
    @ControllerInfo("根据考试取得科目列表")
    public List<EmSubjectInfo> findSubjectByExamId(String examId) {
        List<EmSubjectInfo> subjectList = emSubjectInfoService.findByExamId(examId);
        return subjectList;
    }

    @RequestMapping("/examGrade/showScoreByGrade/page")
    @ControllerInfo("年级统计结果")
    public String showScoreByGrade(String examId, String gradeId, String type, ModelMap map, HttpSession httpSession) {
        if (StringUtils.isBlank(gradeId) || StringUtils.isBlank(examId)) {
            return promptFlt(map, "请先选择某一个考试");
        }
        EmStatObject statObject = findStatObject(examId);
        if (statObject == null) {
            //没有统计--返回错误页面
            return promptFlt(map, "暂时没有统计的数据；请先去统计数据");
//			return errorFtl(map,"没有统计数据");
        }
//		EmStatObject statObject = findStatObject(examId);
//		if(StringUtils.isBlank(gradeId) || StringUtils.isBlank(examId)){
//			//显示空
//			return errorFtl(map,"请先选择考试");
//		}else{
//			if(statObject==null){
//				//没有统计--返回错误页面
//				return errorFtl(map,"没有统计数据");
//			}
//		}
        if (StringUtils.isBlank(type)) {
            type = "1";//默认1
        }
        if ("1".equals(type)) {
            //年级成绩概况
            //头部--平均分---根据名称区分
            List<String> avgTitle = new ArrayList<String>();
            List<EmRangeStatDto> dtoList = new ArrayList<EmRangeStatDto>();
            if (StringUtils.isBlank(gradeId) || StringUtils.isBlank(examId)) {
                map.put("avgTitle", avgTitle);
                map.put("dtoList", dtoList);
                return "/examanalysis/examGrade/examGradeGeneral.ftl";
            }
            //年级统计结果--保存的是学校id
            String schoolId = getLoginInfo().getUnitId();
            List<EmStatRange> rangeList = emStatRangeService.findByObjIdAndRangeId(statObject.getId(), examId, schoolId, EmStatRange.RANGE_SCHOOL);

            Map<String, String> spaceItemIdByName = new HashMap<String, String>();


            EmRangeStatDto dto = null;
            Set<String> rangeIds = new HashSet<String>();
            Set<String> subjectIds = new HashSet<String>();

            Set<String> parmTypes = new HashSet<String>();
            parmTypes.add(EmSpaceItem.PARM_PERCENT_B);
            parmTypes.add(EmSpaceItem.PARM_PERCENT_F);
            Map<String, List<EmStatSpace>> spaceByRangeId = new HashMap<String, List<EmStatSpace>>();

            if (CollectionUtils.isNotEmpty(rangeList)) {
                for (EmStatRange r : rangeList) {
                    rangeIds.add(r.getId());
                    dto = new EmRangeStatDto();
                    dto.setAverageScore(r.getAvgScore());
                    dto.setAvgMap(new HashMap<String, Float>());
                    dto.setMaxScore(r.getMaxScore());
                    dto.setMinScore(r.getMinScore());
                    dto.setStatRangeId(r.getId());
                    dto.setStatStuNum(r.getStatNum());
                    dto.setSubjectId(r.getSubjectId());
                    subjectIds.add(r.getSubjectId());
                    dtoList.add(dto);
                }

                //年级统计分段结果
                List<EmStatSpace> emStatSpaceList = emStatSpaceService.findByStatRangeIdIn(rangeIds.toArray(new String[]{}));
                Set<String> spaceIds = new HashSet<String>();
                if (CollectionUtils.isNotEmpty(emStatSpaceList)) {
                    for (EmStatSpace s : emStatSpaceList) {
                        spaceIds.add(s.getSpaceItemId());
                        if (!spaceByRangeId.containsKey(s.getStatRangeId())) {
                            spaceByRangeId.put(s.getStatRangeId(), new ArrayList<EmStatSpace>());
                        }
                        spaceByRangeId.get(s.getStatRangeId()).add(s);
                    }

                    spaceItemIdByName = findStatSpaceItem(spaceIds.toArray(new String[]{}), parmTypes, avgTitle);
                }
            }

            Collections.sort(avgTitle);
            if (CollectionUtils.isNotEmpty(dtoList)) {
                Map<String, Course> courseNameMap = new HashMap<String, Course>();
                if (subjectIds.size() > 0) {
                    List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[]{})), new TR<List<Course>>() {
                    });
                    courseNameMap = EntityUtils.getMap(courseList, "id");
                }
                //根据subjectId排序
                Collections.sort(dtoList, new Comparator<EmRangeStatDto>() {

                    @Override
                    public int compare(EmRangeStatDto o1, EmRangeStatDto o2) {
                        return o1.getSubjectId().compareTo(o2.getSubjectId());
                    }

                });

                for (EmRangeStatDto dd : dtoList) {
                    if (BaseConstants.ZERO_GUID.equals(dd.getSubjectId())) {
                        dd.setSubjectName("总分");
                    } else if (courseNameMap.containsKey(dd.getSubjectId())) {
                        dd.setSubjectName(courseNameMap.get(dd.getSubjectId()).getSubjectName());
                    }
                    if (spaceByRangeId.containsKey(dd.getStatRangeId())) {
                        List<EmStatSpace> llist = spaceByRangeId.get(dd.getStatRangeId());
                        for (EmStatSpace sss : llist) {
                            if (spaceItemIdByName.containsKey(sss.getSpaceItemId())) {
                                dd.getAvgMap().put(spaceItemIdByName.get(sss.getSpaceItemId()), Float.parseFloat(sss.getScoreNum()));
                            }
                        }
                    }
                }
            }


            map.put("avgTitle", avgTitle);
            map.put("dtoList", dtoList);
            return "/examanalysis/examGrade/examGradeGeneral.ftl";

        } else if ("2".equals(type)) {
            //成绩分布分析
            //科目
            List<Course> courseList = toSearchCourseList(examId, true);
            map.put("courseList", courseList);
            return "/examanalysis/examGrade/examGradeRank.ftl";
        } else if ("3".equals(type)) {
            //拐点分析
            List<Course> courseList = toSearchCourseList(examId, false);
            map.put("courseList", courseList);
            return "/examanalysis/examGrade/examGradeInflection.ftl";
        } else if ("4".equals(type)) {
            //班级列表
            List<Course> courseList = toSearchCourseList(examId, true);
            map.put("courseList", courseList);
            //班级列表
            List<EmClassInfo> classInfoList = emClassInfoService.findByExamIdAndSchoolId(examId, getLoginInfo().getUnitId());
            Set<String> classIds = EntityUtils.getSet(classInfoList, "classId");
            if (classIds.size() > 0) {
                List<Clazz> clazzList = SUtils.dt(classRemoteService.findListByIds(classIds.toArray(new String[]{})), new TR<List<Clazz>>() {
                });
                map.put("clazzList", clazzList);
            }
            return "/examanalysis/examGrade/examGradeCompareIndex.ftl";

        } else if ("5".equals(type)) {
            //成绩分布分析
            List<EmClassInfo> classInfoList = emClassInfoService.findByExamIdAndSchoolId(examId, getLoginInfo().getUnitId());
            Set<String> classIds = EntityUtils.getSet(classInfoList, "classId");
            if (classIds.size() > 0) {
                List<Clazz> clazzList = SUtils.dt(classRemoteService.findListByIds(classIds.toArray(new String[]{})), new TR<List<Clazz>>() {
                });
                map.put("clazzList", clazzList);
            }
            List<Course> courseList = toSearchCourseList(examId, true);
            map.put("courseList", courseList);

            List<String> avgTitle = new ArrayList<String>();
            //默认  statObject 统计参数的值
            List<EmStatParm> parmList = emStatParmService.findByStatObjectIdAndExamId(statObject.getId(), examId);
            Set<String> typeSet = new HashSet<String>();
            typeSet.add(EmSpaceItem.PARM_PERCENT_B);
            typeSet.add(EmSpaceItem.PARM_PERCENT_F);
            if (CollectionUtils.isNotEmpty(parmList)) {
                Set<String> parmIds = EntityUtils.getSet(parmList, "id");
                List<EmSpaceItem> spaceList = emSpaceItemService.findByStatParmIdIn(parmIds.toArray(new String[]{}));
                if (CollectionUtils.isNotEmpty(spaceList)) {
                    for (EmSpaceItem item : spaceList) {
                        if (!typeSet.contains(item.getParmType())) {
                            continue;
                        }
                        if (!avgTitle.contains(item.getName())) {
                            avgTitle.add(item.getName());
                        }
                    }
                }
            }


            map.put("avgTitle", avgTitle);
            return "/examanalysis/examGrade/examGradeCompareChartIndex.ftl";
        }

        return promptFlt(map, "查询不存在；请刷新后再次操作");
//		return errorFtl(map,"查询不存在");
    }

    @RequestMapping("/examGrade/compareChart/page")
    @ControllerInfo("成绩分布分析")
    public String showCompareChart(String type, String examId, String subjectId, String classIds, String analysisType, ModelMap map) {
        if (StringUtils.isBlank(type)) {
            if (StringUtils.isNotBlank(analysisType)) {
                type = "2";
            } else {
                type = "1";
            }
        }
        if (StringUtils.isBlank(examId)) {
            return promptFlt(map, "请先选择某一个考试");
        }
        EmStatObject statObject = findStatObject(examId);
        if (statObject == null) {
            //没有统计--返回错误页面
            return promptFlt(map, "暂时没有统计的数据；请先去统计数据");
            //return errorFtl(map,"没有统计数据");
        }
        String unitId = getLoginInfo().getUnitId();
        String[] classIdArr = classIds.split(",");
        List<Clazz> clazzList = SUtils.dt(classRemoteService.findListByIds(classIdArr), new TR<List<Clazz>>() {
        });

        if ("1".equals(type)) {
            //各科成绩分布分析
            //拿到统计参数的分数段

            EmStatParm emStatParm = emStatParmService.findBySubjectId(unitId, examId, subjectId);
            List<EmSpaceItem> emSpaceItemList = new ArrayList<EmSpaceItem>();
            //key:statRangeId key:EmStatSpaceid
            Map<String, Map<String, EmStatSpace>> allMap = new HashMap<String, Map<String, EmStatSpace>>();
            Map<String, String> rangeIdByclassId = new HashMap<String, String>();

            if (emStatParm != null && CollectionUtils.isNotEmpty(emStatParm.getEmSpaceItemList1())) {
                emSpaceItemList = emStatParm.getEmSpaceItemList1();
                if (CollectionUtils.isEmpty(emSpaceItemList)) {
                    map.put("message", "没有设置分数段");
                    return "/examanalysis/examGrade/gradeCompareChart.ftl";
                }
                Set<String> spaceIds = EntityUtils.getSet(emSpaceItemList, "id");
                List<EmStatRange> emStatRangeList = emStatRangeService.findByExamIdAndRangeIdIn(statObject.getId(), examId, subjectId, EmStatRange.RANGE_CLASS, classIdArr);
                Set<String> statRangeIds = new HashSet<String>();
                if (CollectionUtils.isNotEmpty(emStatRangeList)) {
                    for (EmStatRange e : emStatRangeList) {
                        statRangeIds.add(e.getId());
                        rangeIdByclassId.put(e.getRangeId(), e.getId());
                    }
                    List<EmStatSpace> list = emStatSpaceService.findByStatRangeIdIn(statRangeIds.toArray(new String[]{}));
                    if (CollectionUtils.isNotEmpty(list)) {
                        for (EmStatSpace ss : list) {
                            if (!spaceIds.contains(ss.getSpaceItemId())) {
                                continue;
                            }
                            if (!allMap.containsKey(ss.getStatRangeId())) {
                                allMap.put(ss.getStatRangeId(), new HashMap<String, EmStatSpace>());
                            }
                            allMap.get(ss.getStatRangeId()).put(ss.getSpaceItemId(), ss);
                        }
                    }
                }
            }
            map.put("type", "1");
            if (CollectionUtils.isEmpty(emSpaceItemList)) {
                map.put("message", "没有设置分数段");
                return "/examanalysis/examGrade/gradeCompareChart.ftl";
            }
            JSONObject json = new JSONObject();
            String[] xAxisData = new String[clazzList.size()];
            String[] legendData = new String[emSpaceItemList.size()];
            Integer[][] loadingData = new Integer[emSpaceItemList.size()][clazzList.size()];
            for (int i = 0; i < clazzList.size(); i++) {
                xAxisData[i] = clazzList.get(i).getClassNameDynamic();
            }
            for (int i = 0; i < emSpaceItemList.size(); i++) {
                legendData[i] = emSpaceItemList.get(i).getName();
            }

            for (int i = 0; i < emSpaceItemList.size(); i++) {
                for (int j = 0; j < clazzList.size(); j++) {
                    if (!rangeIdByclassId.containsKey(clazzList.get(j).getId())) {
                        loadingData[i][j] = 0;
                        continue;
                    }
                    String rangeIdKey = rangeIdByclassId.get(clazzList.get(j).getId());
                    if (!allMap.containsKey(rangeIdKey)) {
                        loadingData[i][j] = 0;
                        continue;
                    }
                    if (!allMap.get(rangeIdKey).containsKey(emSpaceItemList.get(i).getId())) {
                        loadingData[i][j] = 0;
                        continue;
                    }
                    loadingData[i][j] = Integer.parseInt(allMap.get(rangeIdKey).get(emSpaceItemList.get(i).getId()).getScoreNum());
                }
            }
            json.put("xAxisData", xAxisData);
            json.put("loadingData", loadingData);
            json.put("legendData", legendData);
            String jsonStringData = json.toString();
            map.put("jsonStringData", jsonStringData);


            map.put("width", toMakeChartWidth(xAxisData.length, "1"));


            return "/examanalysis/examGrade/gradeCompareChart.ftl";
        } else if ("2".equals(type)) {
            //成绩对比分析
            List<EmStatRange> emStatRangeList = emStatRangeService.findByExamIdAndRangeIdIn(statObject.getId(), examId, subjectId, EmStatRange.RANGE_CLASS, classIdArr);
            Map<String, Float> scoreByClassId = new HashMap<String, Float>();
            if (CollectionUtils.isEmpty(emStatRangeList)) {
                //
            }
            if (analysisType.startsWith("0_")) {
                if ("0_allAvg".equals(analysisType)) {
                    for (EmStatRange e : emStatRangeList) {
                        scoreByClassId.put(e.getRangeId(), e.getAvgScore());
                    }

                } else if ("0_max".equals(analysisType)) {
                    for (EmStatRange e : emStatRangeList) {
                        scoreByClassId.put(e.getRangeId(), e.getMaxScore());
                    }

                } else if ("0_min".equals(analysisType)) {
                    for (EmStatRange e : emStatRangeList) {
                        scoreByClassId.put(e.getRangeId(), e.getMinScore());
                    }

                }
            } else if (analysisType.startsWith("1_")) {
                String name = analysisType.substring(2);
                //根据名称
                Set<String> rangeIds = EntityUtils.getSet(emStatRangeList, "id");
                //年级统计分段结果
                List<EmStatSpace> emStatSpaceList = emStatSpaceService.findByStatRangeIdIn(rangeIds.toArray(new String[]{}));
                Set<String> spaceIds = new HashSet<String>();
                Map<String, List<EmStatSpace>> spaceByRangeId = new HashMap<String, List<EmStatSpace>>();

                if (CollectionUtils.isNotEmpty(emStatSpaceList)) {
                    for (EmStatSpace s : emStatSpaceList) {
                        spaceIds.add(s.getSpaceItemId());
                        if (!spaceByRangeId.containsKey(s.getStatRangeId())) {
                            spaceByRangeId.put(s.getStatRangeId(), new ArrayList<EmStatSpace>());
                        }
                        spaceByRangeId.get(s.getStatRangeId()).add(s);
                    }
                    List<EmSpaceItem> spaceList = emSpaceItemService.findByNameAndIdIn(name, spaceIds.toArray(new String[]{}));
                    //符合条件
                    spaceIds = EntityUtils.getSet(spaceList, "id");
                }


                for (EmStatRange r : emStatRangeList) {

                    if (spaceByRangeId.containsKey(r.getId())) {
                        List<EmStatSpace> ll = spaceByRangeId.get(r.getId());
                        for (EmStatSpace e : ll) {
                            if (spaceIds.contains(e.getSpaceItemId())) {
                                scoreByClassId.put(r.getRangeId(), Float.parseFloat(e.getScoreNum()));
                                break;
                            }
                        }
                    }

                }


            }

            JSONObject json = new JSONObject();
            String[] xAxisData = new String[clazzList.size()];
            Float[][] loadingData = new Float[1][clazzList.size()];
            for (int i = 0; i < clazzList.size(); i++) {
                xAxisData[i] = clazzList.get(i).getClassNameDynamic();
                if (scoreByClassId.containsKey(clazzList.get(i).getId())) {
                    loadingData[0][i] = scoreByClassId.get(clazzList.get(i).getId());
                } else {
                    loadingData[0][i] = 0f;
                }
            }


            json.put("xAxisData", xAxisData);
            json.put("loadingData", loadingData);
            json.put("legendData", new String[]{"平均分"});
            String jsonStringData = json.toString();
            map.put("jsonStringData", jsonStringData);
            map.put("type", "2");
            map.put("width", toMakeChartWidth(xAxisData.length, "1"));

            return "/examanalysis/examGrade/gradeCompareChart.ftl";

        }

        return null;
    }


    @RequestMapping("/examGrade/compareList/page")
    @ControllerInfo("对比分析状况列表")
    public String showInflectionList(String examId, String subjectId, String classIds, ModelMap map) {
//		EmStatObject statObject = findStatObject(examId);
//		if(statObject==null){
//			//没有统计--返回错误页面
//			return errorFtl(map,"没有统计数据");
//		}
        if (StringUtils.isBlank(examId)) {
            return promptFlt(map, "请先选择某一个考试");
        }
        EmStatObject statObject = findStatObject(examId);
        if (statObject == null) {
            //没有统计--返回错误页面
            return promptFlt(map, "暂时没有统计的数据；请先去统计数据");
            //return errorFtl(map,"没有统计数据");
        }
        EmStatRange gradeRange = emStatRangeService.findByExamIdAndSubIdAndRangIdAndType(statObject.getId(), examId, subjectId, getLoginInfo().getUnitId(), EmStatRange.RANGE_SCHOOL);
        Set<String> typeSet = new HashSet<String>();
        typeSet.add(EmSpaceItem.PARM_PERCENT_B);
        typeSet.add(EmSpaceItem.PARM_PERCENT_F);
        if (gradeRange != null) {
            List<EmStatSpace> statSpaceList = emStatSpaceService.findByStatRangeIdIn(new String[]{gradeRange.getId()});
            if (CollectionUtils.isNotEmpty(statSpaceList)) {
                Set<String> spaceIds = EntityUtils.getSet(statSpaceList, "spaceItemId");
                List<EmSpaceItem> itemList = emSpaceItemService.findListByIdIn(spaceIds.toArray(new String[]{}));
                Map<String, EmSpaceItem> itemMap = EntityUtils.getMap(itemList, "id");
                List<EmStatSpace> returnList = new ArrayList<EmStatSpace>();
                for (EmStatSpace s : statSpaceList) {
                    if (!itemMap.containsKey(s.getSpaceItemId())) {
                        continue;
                    }
                    EmSpaceItem item = itemMap.get(s.getSpaceItemId());
                    if (!typeSet.contains(item.getParmType())) {
                        continue;
                    }
                    s.setSpaceItemName(item.getName());
                    returnList.add(s);
                }
                map.put("statSpaceList", returnList);
            }

        }
        String subjectName = "";
        if (BaseConstants.ZERO_GUID.equals(subjectId)) {
            subjectName = "总分";
        } else {
            Course course = SUtils.dc(courseRemoteService.findOneById(subjectId), Course.class);
            if (course != null) {
                subjectName = course.getSubjectName();
            }
        }

        if (StringUtils.isNotEmpty(classIds)) {
            String[] classIdArr = classIds.split(",");
            List<Clazz> clazzList = SUtils.dt(classRemoteService.findListByIds(classIdArr), new TR<List<Clazz>>() {
            });
            Map<String, Clazz> classMap = EntityUtils.getMap(clazzList, "id");

            List<EmStatRange> emStatRangeList = emStatRangeService.findByExamIdAndRangeIdIn(statObject.getId(), examId, subjectId, EmStatRange.RANGE_CLASS, classIdArr);
            if (CollectionUtils.isNotEmpty(emStatRangeList)) {
                for (EmStatRange range : emStatRangeList) {
                    if (classMap.containsKey(range.getRangeId())) {
                        range.setRangeName(classMap.get(range.getRangeId()).getClassNameDynamic());
                    }
                    range.setSubjectName(subjectName);
                }
            }
            map.put("emStatRangeList", emStatRangeList);

        }

        return "/examanalysis/examGrade/examGradeCompareList.ftl";
    }

    @RequestMapping("/examGrade/inflectionList/page")
    @ControllerInfo("拐点列表")
    public String showInflectionList(String examId, String subjectId, int rank1, int rank2, ModelMap map, HttpServletRequest request) {
//		EmStatObject statObject = findStatObject(examId);
//		if(statObject==null){
//			//没有统计--返回错误页面
//			return errorFtl(map,"没有统计数据");
//		}
        if (StringUtils.isBlank(examId)) {
            return promptFlt(map, "请先选择某一个考试");
        }
        EmStatObject statObject = findStatObject(examId);
        if (statObject == null) {
            //没有统计--返回错误页面
            return promptFlt(map, "暂时没有统计的数据；请先去统计数据");
            //return errorFtl(map,"没有统计数据");
        }
        String schoolId = getLoginInfo().getUnitId();
        //根据总分分页
        Pagination page = createPagination();
        List<EmStat> statlist = emStatService.findBySchoolRank(statObject.getId(), examId, schoolId, BaseConstants.ZERO_GUID, rank1, rank2, page);

        map.put("Pagination", page);

        sendPagination(request, map, page);
        //组装
        Set<String> subjectIds = new HashSet<String>();
        if (StringUtils.isNotEmpty(subjectId)) {
            //单科
            subjectIds.add(subjectId);
            List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[]{})), new TR<List<Course>>() {
            });
            map.put("courseList", courseList);
        } else {
            List<Course> courseList = toSearchCourseList(examId, true);
            map.put("courseList", courseList);
        }

        List<EmStudentStatDto> studentDtoList = new ArrayList<EmStudentStatDto>();
        if (CollectionUtils.isNotEmpty(statlist)) {
            Set<String> studentIds = EntityUtils.getSet(statlist, "studentId");
            List<Student> studentList = SUtils.dt(studentRemoteService.findListByIds(studentIds.toArray(new String[]{})), new TR<List<Student>>() {
            });
            Map<String, Student> studentMap = EntityUtils.getMap(studentList, "id");
            //考号
            Map<String, String> examNumByStudnetId = emExamNumService.findByExamIdAndStudentIdIn(examId, studentIds.toArray(new String[]{}));
            Set<String> classIdSet = EntityUtils.getSet(statlist, "classId");
            List<Clazz> clazzList = SUtils.dt(classRemoteService.findListByIds(classIdSet.toArray(new String[]{})), new TR<List<Clazz>>() {
            });
            Map<String, Clazz> classMap = EntityUtils.getMap(clazzList, "id");
            //学生所有成绩
            List<EmStat> stuStatList = emStatService.findByStudentIds(statObject.getId(), examId, subjectId, studentIds.toArray(new String[]{}));
            Map<String, EmStudentStatDto> dtoMap = new HashMap<String, EmStudentStatDto>();
            if (CollectionUtils.isNotEmpty(stuStatList)) {
                for (EmStat emStat : stuStatList) {
                    if (!dtoMap.containsKey(emStat.getStudentId())) {
                        EmStudentStatDto stuDto = new EmStudentStatDto();
                        stuDto.setStatBySubjectMap(new HashMap<String, Float>());
                        stuDto.getStatBySubjectMap().put(emStat.getSubjectId(), emStat.getScore());
                        stuDto.setGradeRankBySubjectMap(new HashMap<String, Integer>());
                        stuDto.getGradeRankBySubjectMap().put(emStat.getSubjectId(), emStat.getGradeRank());
                        dtoMap.put(emStat.getStudentId(), stuDto);

                    } else {
                        EmStudentStatDto stuDto = dtoMap.get(emStat.getStudentId());
                        stuDto.getStatBySubjectMap().put(emStat.getSubjectId(), emStat.getScore());
                        stuDto.getGradeRankBySubjectMap().put(emStat.getSubjectId(), emStat.getGradeRank());
                    }
                }
            }

            for (EmStat item : statlist) {
                EmStudentStatDto dtto = dtoMap.get(item.getStudentId());
                if (dtto == null) {
                    dtto = new EmStudentStatDto();
                    dtto.setStatBySubjectMap(new HashMap<String, Float>());
                    dtto.getStatBySubjectMap().put(item.getSubjectId(), item.getScore());
                    dtto.setGradeRankBySubjectMap(new HashMap<String, Integer>());
                    dtto.getGradeRankBySubjectMap().put(item.getSubjectId(), item.getGradeRank());
                }
                dtto.setStudentId(item.getStudentId());
                if (studentMap.containsKey(item.getStudentId())) {
                    dtto.setStudentName(studentMap.get(item.getStudentId()).getStudentName());
                }
                if (examNumByStudnetId.containsKey(item.getStudentId())) {
                    dtto.setExamCode(examNumByStudnetId.get(item.getStudentId()));
                }
                if (classMap.containsKey(item.getClassId())) {
                    dtto.setClassName(classMap.get(item.getClassId()).getClassNameDynamic());
                }
                dtto.setGradeRank(item.getGradeRank());
                studentDtoList.add(dtto);
            }

        }

        map.put("studentDtoList", studentDtoList);
        return "/examanalysis/examGrade/gradeInflectionList.ftl";
    }


    /**
     * 科目列表
     *
     * @param examId
     * @param isContainZF 是否增加总分
     * @return
     */
    private List<Course> toSearchCourseList(String examId, boolean isContainZF) {
        List<EmSubjectInfo> subjectInfoList = findSubjectByExamId(examId);
        subjectInfoList = takeCanStat(subjectInfoList);
        Set<String> subjectIds = EntityUtils.getSet(subjectInfoList, "subjectId");
        List<Course> courseList = new ArrayList<Course>();
        if (isContainZF) {
            //添加总分
            Course c = new Course();
            c.setId(BaseConstants.ZERO_GUID);
            c.setSubjectName("总分");
            courseList.add(c);
        }
        if (subjectIds.size() > 0) {
            List<Course> courseList1 = SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[]{})), new TR<List<Course>>() {
            });
            courseList.addAll(courseList1);
        }
        return courseList;
    }

    @RequestMapping("/examGrade/sectionChart/page")
    @ControllerInfo("各科成绩分布")
    public String showSectionChart(String examId, String subjectId, ModelMap map) {
        EmStatObject statObject = findStatObject(examId);
        if (statObject == null) {
            map.put("viewType", "");
            map.put("message", "还没有进行统计");
            return "/examanalysis/examGrade/rankChartOrList.ftl";
        }
        map.put("viewType", "3");
        //根据subjectId examId
        EmStatParm statParm = emStatParmService.findBySubjectId(statObject.getUnitId(), examId, subjectId);
        if (statParm == null) {
            map.put("viewType", "");
            map.put("message", "还没有进行统计");
            return "/examanalysis/examGrade/rankChartOrList.ftl";
        }
        List<EmSpaceItem> emSpaceItemList1 = statParm.getEmSpaceItemList1();
        if (CollectionUtils.isEmpty(emSpaceItemList1)) {
            map.put("message", "没有设置分数段");
            return "/examanalysis/examGrade/rankChartOrList.ftl";
        }

        if (StringUtils.isBlank(statParm.getStatSpaceType())) {
            map.put("jsonStringData", "");
            return "/examanalysis/examGrade/rankChartOrList.ftl";
        }
        Set<String> spaceIds = EntityUtils.getSet(emSpaceItemList1, "id");
        //统计结果
        EmStatRange emStatRange = emStatRangeService.findByExamIdAndSubIdAndRangIdAndType(statObject.getId(), examId, subjectId, getLoginInfo().getUnitId(), EmStatRange.RANGE_SCHOOL);
        if (emStatRange == null) {
            map.put("jsonStringData", "");
            return "/examanalysis/examGrade/rankChartOrList.ftl";
        }
        List<EmStatSpace> statSpaceList = emStatSpaceService.findByStatRangeIdAndSpaceItemIdIn(emStatRange.getId(), spaceIds.toArray(new String[]{}));
        if (CollectionUtils.isEmpty(statSpaceList)) {
            map.put("message", "没有设置分数段");
            return "/examanalysis/examGrade/rankChartOrList.ftl";
        }
        Map<String, EmStatSpace> spaceMap = new HashMap<String, EmStatSpace>();
        for (EmStatSpace s : statSpaceList) {
            spaceMap.put(s.getSpaceItemId(), s);
        }

        JSONObject json = new JSONObject();
        String[] xAxisData = new String[spaceMap.size()];
        Integer[][] loadingData = new Integer[1][spaceMap.size()];
        int j = 0;
        for (int i = 0; i < emSpaceItemList1.size(); i++) {
            if (!spaceMap.containsKey(emSpaceItemList1.get(i).getId())) {
                continue;
            }
            xAxisData[j] = emSpaceItemList1.get(i).getName();
            loadingData[0][j] = Integer.parseInt(spaceMap.get(emSpaceItemList1.get(i).getId()).getScoreNum());
            j++;
        }
        json.put("xAxisData", xAxisData);
        json.put("loadingData", loadingData);
        json.put("legendData", new String[]{"人数"});
        String jsonStringData = json.toString();
        map.put("jsonStringData", jsonStringData);
        map.put("width", toMakeChartWidth(xAxisData.length, "3"));

        return "/examanalysis/examGrade/rankChartOrList.ftl";
    }


    @RequestMapping("/examGrade/rankNumChartOrList/page")
    @ControllerInfo("成绩排名分布")
    public String showRankNumChartOrList(String examId, String viewType, String subjectId, int rank1, int rank2, ModelMap map) {
        EmStatObject statObject = findStatObject(examId);
        if (statObject == null) {
            map.put("viewType", "");
            map.put("message", "还没有进行统计");
            return "/examanalysis/examGrade/rankChartOrList.ftl";
        }
        String schoolId = getLoginInfo().getUnitId();
        List<EmClassInfo> classInfoList = emClassInfoService.findByExamIdAndSchoolId(examId, schoolId);
        //暂时都是行政班
        Set<String> classIds = EntityUtils.getSet(classInfoList, "classId");
        if (classIds.size() <= 0) {
            //没有参与
        }
        List<Clazz> clazzList = SUtils.dt(classRemoteService.findListByIds(classIds.toArray(new String[]{})), new TR<List<Clazz>>() {
        });

        //查某次考试排名
        List<EmStat> statList = emStatService.findBySchoolRank(statObject.getId(), examId, schoolId, subjectId, rank1, rank2, null);
        if ("1".equals(viewType)) {
            //图表
            Map<String, Integer> stuNumByClassId = new HashMap<String, Integer>();
            if (CollectionUtils.isNotEmpty(statList)) {
                for (EmStat emStat : statList) {
                    if (stuNumByClassId.containsKey(emStat.getClassId())) {
                        stuNumByClassId.put(emStat.getClassId(), stuNumByClassId.get(emStat.getClassId()) + 1);
                    } else {
                        stuNumByClassId.put(emStat.getClassId(), 1);
                    }
                }
            }

            JSONObject json = new JSONObject();
            String[] xAxisData = new String[clazzList.size()];
            Integer[][] loadingData = new Integer[1][clazzList.size()];
            for (int i = 0; i < clazzList.size(); i++) {
                xAxisData[i] = clazzList.get(i).getClassNameDynamic();
                if (stuNumByClassId.containsKey(clazzList.get(i).getId())) {
                    loadingData[0][i] = stuNumByClassId.get(clazzList.get(i).getId());
                } else {
                    loadingData[0][i] = 0;
                }

            }
            json.put("text", "名次" + rank1 + "-" + rank2 + "人数分布");
            json.put("xAxisData", xAxisData);
            json.put("loadingData", loadingData);
            json.put("legendData", new String[]{"人数"});
            String jsonStringData = json.toString();
            map.put("jsonStringData", jsonStringData);
            map.put("viewType", "1");
            map.put("width", toMakeChartWidth(xAxisData.length, "1"));
        } else {
            //列表
            map.put("viewType", "2");
            //头部
            List<Course> courseList = toSearchCourseList(examId, true);
            map.put("courseList", courseList);

            List<EmClassStatDto> classStatList = new ArrayList<EmClassStatDto>();
            Map<String, EmClassStatDto> dtoMap = new HashMap<String, EmClassStatDto>();


            EmClassStatDto dto = null;
            Set<String> studentIds = EntityUtils.getSet(statList, "studentId");
            List<Student> studentList = SUtils.dt(studentRemoteService.findListByIds(studentIds.toArray(new String[]{})), new TR<List<Student>>() {
            });
            Map<String, Student> studentMap = EntityUtils.getMap(studentList, "id");
            //考号
            Map<String, String> examNumByStudnetId = emExamNumService.findByExamIdAndStudentIdIn(examId, studentIds.toArray(new String[]{}));
            Set<String> classIdSet = EntityUtils.getSet(statList, "classId");
            for (Clazz clazz : clazzList) {
                //排除没有人的
                if (!classIdSet.contains(clazz.getId())) {
                    continue;
                }
                dto = new EmClassStatDto();
                dto.setClassId(clazz.getId());
                dto.setClassName(clazz.getClassNameDynamic());
                dto.setStuNum(0);
                dto.setStuDtoList(new ArrayList<EmStudentStatDto>());
                classStatList.add(dto);
                dtoMap.put(clazz.getId(), dto);
            }

            Map<String, EmStudentStatDto> stuDtoMap = new HashMap<String, EmStudentStatDto>();
            List<EmStat> stuStatList = emStatService.findByStudentIds(statObject.getId(), examId, null, studentIds.toArray(new String[]{}));
            for (EmStat item : stuStatList) {
                if (!dtoMap.containsKey(item.getClassId())) {
                    continue;
                }
                EmClassStatDto classDto = dtoMap.get(item.getClassId());
                if (!stuDtoMap.containsKey(item.getStudentId())) {
                    EmStudentStatDto stuDto = new EmStudentStatDto();
                    stuDto.setStudentId(item.getStudentId());
                    if (studentMap.containsKey(item.getStudentId())) {
                        stuDto.setStudentName(studentMap.get(item.getStudentId()).getStudentName());
                    }
                    if (examNumByStudnetId.containsKey(item.getStudentId())) {
                        stuDto.setExamCode(examNumByStudnetId.get(item.getStudentId()));
                    }
                    stuDto.setStatBySubjectMap(new HashMap<String, Float>());
                    stuDto.getStatBySubjectMap().put(item.getSubjectId(), item.getScore());
                    if (subjectId.equals(item.getSubjectId())) {
                        stuDto.setGradeRank(item.getGradeRank());
                    }
                    stuDtoMap.put(stuDto.getStudentId(), stuDto);
                    classDto.getStuDtoList().add(stuDto);
                    classDto.setStuNum(classDto.getStuNum() + 1);
                } else {
                    EmStudentStatDto stuDto = stuDtoMap.get(item.getStudentId());
                    stuDto.getStatBySubjectMap().put(item.getSubjectId(), item.getScore());
                    if (subjectId.equals(item.getSubjectId())) {
                        stuDto.setGradeRank(item.getGradeRank());
                    }
                }

            }
            for (EmClassStatDto item : classStatList) {
                if (CollectionUtils.isNotEmpty(item.getStuDtoList())) {
                    Collections.sort(item.getStuDtoList(), new Comparator<EmStudentStatDto>() {

                        @Override
                        public int compare(EmStudentStatDto o1,
                                           EmStudentStatDto o2) {
                            return o1.getGradeRank() - o2.getGradeRank();
                        }
                    });
                }

            }
            map.put("classStatList", classStatList);
        }
        return "/examanalysis/examGrade/rankChartOrList.ftl";

    }

    @RequestMapping("/examGrade/generalChart/page")
    @ControllerInfo("年级成绩概况图表")
    public String showGeneralChart(String examId, String gradeId, String analysisType, String subjectId, ModelMap map) {
        //当前学期下
        EmExamInfo exam = emExamInfoService.findOne(examId);
        if (exam == null) {
            return errorFtl(map, "该考试已不存在");
        }
        List<EmExamInfo> examList = findExamIdByGradeId(gradeId, exam);
        //年级统计结果--保存的是学校id
        String schoolId = getLoginInfo().getUnitId();
        toMakeChart(examList, schoolId, EmStatRange.RANGE_SCHOOL, subjectId, analysisType, map);

        return "/examanalysis/examGrade/generalChart.ftl";
    }


    /**
     * 选择某个统计对象（优先学校，再选择教育局结果）
     * key:examId value emStatObject
     *
     * @param examIds
     * @return
     */
    private Map<String, EmStatObject> findStatObject(String[] examIds) {
        String unitId = getLoginInfo().getUnitId();
        List<EmStatObject> list = emStatObjectService.findByUnitIdAndExamIdIn(unitId, examIds);
        Map<String, EmStatObject> map = new HashMap<String, EmStatObject>();
        if (CollectionUtils.isEmpty(list)) {
            return map;
        }
        for (EmStatObject obj : list) {
            map.put(obj.getExamId(), obj);
        }
        //examIds 排除map.key
        List<String> otherExamId = new ArrayList<String>();
        for (String examid : examIds) {
            if (map.containsKey(examid)) {
                continue;
            }
            otherExamId.add(examid);
        }
        if (CollectionUtils.isEmpty(otherExamId)) {
            return map;
        }
        List<EmExamInfo> eelist = emExamInfoService.findListByIds(otherExamId.toArray(new String[]{}));
        for (EmExamInfo e : eelist) {
            if (!e.getUnitId().equals(unitId)) {
                EmStatObject statObject = emStatObjectService.findByUnitIdExamId(e.getUnitId(), e.getId());
                if (statObject != null) {
                    map.put(e.getId(), statObject);
                }
            }
        }
        return map;
    }

    /**
     * 查询当前考试对应学期下该年级的所有考试
     *
     * @param gradeId
     * @param exam
     * @return
     */
    private List<EmExamInfo> findExamIdByGradeId(String gradeId, EmExamInfo exam) {
        //根据年级id查询班级id
        List<EmExamInfo> infoList = findExamIdByGradeId(gradeId);
        List<EmExamInfo> returnList = new ArrayList<EmExamInfo>();
        if (CollectionUtils.isNotEmpty(infoList)) {
            for (EmExamInfo ee : infoList) {
                if (ee.getAcadyear().equals(exam.getAcadyear()) && ee.getSemester().equals(exam.getSemester())) {
                    returnList.add(ee);
                }
            }
        }
        return returnList;
    }

    /**
     * 查询当前考试对应学期下该班级的所有考试
     *
     * @param gradeId
     * @param exam
     * @return
     */
    private List<EmExamInfo> findExamIdByClassId(String classId, EmExamInfo exam) {
        //根据年级id查询班级id
        List<String> examList = emClassInfoService.findExamIdByClassIds(new String[]{classId});
        List<EmExamInfo> infoList = emExamInfoService.findListByIds(examList.toArray(new String[]{}));
        List<EmExamInfo> returnList = new ArrayList<EmExamInfo>();
        if (CollectionUtils.isNotEmpty(infoList)) {
            for (EmExamInfo ee : infoList) {
                if (ee.getAcadyear().equals(exam.getAcadyear()) && ee.getSemester().equals(exam.getSemester())) {
                    returnList.add(ee);
                }
            }
        }
        return returnList;
    }


    /**
     * key:id value:name
     *
     * @param spaceIds
     * @param parmTypes
     * @return
     */
    private Map<String, String> findStatSpaceItem(String[] spaceIds, Set<String> parmTypes, List<String> avgTitle) {
        Map<String, String> spaceItemIdByName = new HashMap<String, String>();
        List<EmSpaceItem> spaceItemList = emSpaceItemService.findListByIds(spaceIds);
        if (CollectionUtils.isNotEmpty(spaceItemList)) {
            for (EmSpaceItem e : spaceItemList) {
                if (!parmTypes.contains(e.getParmType())) {
                    continue;
                }

                spaceItemIdByName.put(e.getId(), e.getName());
                if (!avgTitle.contains(e.getName())) {
                    avgTitle.add(e.getName());
                }
            }
        }
        return spaceItemIdByName;
    }

    /**
     * 选择某个统计对象（优先学校，再选择教育局结果）
     *
     * @param examId
     * @return
     */
    private EmStatObject findStatObject(String examId) {
        String unitId = getLoginInfo().getUnitId();
        EmStatObject statObject = emStatObjectService.findByUnitIdExamId(unitId, examId);
        if (statObject == null) {
            EmExamInfo exam = emExamInfoService.findOne(examId);
            if (exam != null && !exam.getUnitId().equals(unitId)) {
                statObject = emStatObjectService.findByUnitIdExamId(exam.getUnitId(), examId);
            }
        }
        return statObject;
    }

    @RequestMapping("/examClass/index/page")
    @ControllerInfo("班级成绩统计结果")
    public String showClassIndex(ModelMap map) {
        String unitId = getLoginInfo().getUnitId();
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(unitId), new TR<List<Grade>>() {
        });
        map.put("gradeList", gradeList);
        return "/examanalysis/examClass/examClassIndex.ftl";
    }

    @RequestMapping("/examClass/ScoreDistribution/page")
    @ControllerInfo("班级成绩统计情况")
    public String scoreDistribution(String examId, String gradeId, String classId, String subjectId, ModelMap map, HttpSession httpSession) {
        EmStatObject statObject = findStatObject(examId);
        if (statObject == null) {
            map.put("message", "还没有进行统计");
            return "/examanalysis/examClass/examClassScoreDistributionPicture.ftl";
        }
        //根据subjectId examId
        EmStatParm statParm = emStatParmService.findBySubjectId(statObject.getUnitId(), examId, subjectId);
        if (statParm == null) {
            map.put("message", "还没有进行统计");
            return "/examanalysis/examClass/examClassScoreDistributionPicture.ftl";
        }
        List<EmSpaceItem> emSpaceItemList1 = statParm.getEmSpaceItemList1();
        if (CollectionUtils.isEmpty(emSpaceItemList1)) {
            map.put("message", "没有设置分数段");
            return "/examanalysis/examClass/examClassScoreDistributionPicture.ftl";
        }
        if (StringUtils.isBlank(statParm.getStatSpaceType())) {
            map.put("jsonStringData", "");
            return "/examanalysis/examClass/examClassScoreDistributionPicture.ftl";
        }
        Set<String> spaceIds = EntityUtils.getSet(emSpaceItemList1, "id");
        //统计结果
        EmStatRange emStatRange = emStatRangeService.findByExamIdAndSubIdAndRangIdAndType(statObject.getId(), examId, subjectId, classId, EmStatRange.RANGE_CLASS);
        if (emStatRange == null) {
            map.put("jsonStringData", "");
            return "/examanalysis/examClass/examClassScoreDistributionPicture.ftl";
        }
        List<EmStatSpace> statSpaceList = emStatSpaceService.findByStatRangeIdAndSpaceItemIdIn(emStatRange.getId(), spaceIds.toArray(new String[]{}));
        if (CollectionUtils.isEmpty(statSpaceList)) {
            map.put("message", "没有设置分数段");
            return "/examanalysis/examClass/examClassScoreDistributionPicture.ftl";
        }
        Map<String, EmStatSpace> spaceMap = new HashMap<String, EmStatSpace>();
        for (EmStatSpace s : statSpaceList) {
            spaceMap.put(s.getSpaceItemId(), s);
        }

        JSONObject json = new JSONObject();
        String[] xAxisData = new String[spaceMap.size()];
        Integer[][] loadingData = new Integer[1][spaceMap.size()];
        int j = 0;
        for (int i = 0; i < emSpaceItemList1.size(); i++) {
            if (!spaceMap.containsKey(emSpaceItemList1.get(i).getId())) {
                continue;
            }
            xAxisData[j] = emSpaceItemList1.get(i).getName();
            loadingData[0][j] = Integer.parseInt(spaceMap.get(emSpaceItemList1.get(i).getId()).getScoreNum());
            j++;
        }
        json.put("xAxisData", xAxisData);
        json.put("loadingData", loadingData);
        json.put("legendData", new String[]{"人数"});
        String jsonStringData = json.toString();
        map.put("jsonStringData", jsonStringData);
        map.put("width", toMakeChartWidth(xAxisData.length, "3"));
        return "/examanalysis/examClass/examClassScoreDistributionPicture.ftl";
    }

    @RequestMapping("/examClass/showClassScore/page")
    @ControllerInfo("班级成绩统计情况")
    public String showClassScore(String examId, String gradeId, String type, String classId, ModelMap map, HttpSession httpSession) {
//		EmStatObject statObject = findStatObject(examId);
//		if(statObject==null){
//			return errorFtl(map,"没有统计");
//		}
        if (StringUtils.isBlank(examId)) {
            return promptFlt(map, "请先选择某一个考试");
        }
        EmStatObject statObject = findStatObject(examId);
        if (statObject == null) {
            //没有统计--返回错误页面
            return promptFlt(map, "暂时没有统计的数据；请先去统计数据");
            //return errorFtl(map,"没有统计数据");
        }
        if ("2".equals(type)) {
            List<Course> coursesList = toSearchCourseList(examId, true);
            List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(classId), new TR<List<Student>>() {
            });
            Map<String, Student> studentMap = EntityUtils.getMap(studentList, "id");
            Map<String, List<String>> scoresRankMap = new LinkedHashMap<String, List<String>>();
            List<String> rankList = null;
            //学生排名
            for (Course course : coursesList) {
                rankList = new ArrayList<String>();
                List<EmStat> statList = emStatService.findByClassRank(statObject.getId(), examId, classId, course.getId(), 1, 5);
                for (int j = 1; j <= 5; j++) {
                    String studentName = "";
                    String scores = "";
                    for (EmStat emStat : statList) {
                        if (emStat.getClassRank() == j) {
                            studentName += studentMap.get(emStat.getStudentId()).getStudentName() + ",";
                            scores = String.valueOf(emStat.getScore());
                        }
                    }
                    if (StringUtils.isNotBlank(studentName)) {
                        studentName = studentName.substring(0, studentName.length() - 1);
                    } else {
                        studentName = "\\";
                    }
                    if (StringUtils.isBlank(scores)) {
                        scores = "\\";
                    }
                    rankList.add(studentName + "_" + scores);
                }
                scoresRankMap.put(course.getSubjectName(), rankList);
            }
            map.put("scoresRankMap", scoresRankMap);
            map.put("courseList", coursesList);
            map.put("examId", examId);
            map.put("gradeId", gradeId);
            map.put("classId", classId);
            return "/examanalysis/examClass/examClassScoreDistribution.ftl";
        } else if ("1".equals(type)) {
            //年级结果
            List<EmStatRange> gradeStatList = emStatRangeService.findByObjIdAndRangeId(statObject.getId(), examId, getLoginInfo().getUnitId(), EmStatRange.RANGE_SCHOOL);
            Map<String, Float> gradeAvgMap = new HashMap<String, Float>();
            if (CollectionUtils.isNotEmpty(gradeStatList)) {
                for (EmStatRange item : gradeStatList) {
                    gradeAvgMap.put(item.getSubjectId(), item.getAvgScore());
                }
            }

            //班级结果
            List<EmStatRange> classStatList = emStatRangeService.findByObjIdAndRangeId(statObject.getId(), examId, classId, EmStatRange.RANGE_CLASS);


            //当前考试成绩概况
            List<EmSubjectStatDto> subjectStatDto = new ArrayList<EmSubjectStatDto>();
            if (CollectionUtils.isNotEmpty(classStatList)) {
                Set<String> ids = EntityUtils.getSet(classStatList, "id");
                Set<String> subjectIds = EntityUtils.getSet(classStatList, "subjectId");
                Map<String, Course> courseNameMap = new HashMap<String, Course>();
                List<Course> courseList = new ArrayList<Course>();
                Course course = new Course();
                course.setId(BaseConstants.ZERO_GUID);
                course.setSubjectName("总分");
                courseList.add(course);
                if (subjectIds.size() > 0) {
                    List<Course> courseList1 = SUtils.dt(courseRemoteService.findListByIds(subjectIds.toArray(new String[]{})), new TR<List<Course>>() {
                    });
                    courseList.addAll(courseList1);
                }
                courseNameMap = EntityUtils.getMap(courseList, "id");
                List<EmStatSpace> emstatSpaceList = emStatSpaceService.findByStatRangeIdIn(ids.toArray(new String[]{}));
                Map<String, Map<String, Float>> spacemap = new HashMap<String, Map<String, Float>>();
                Set<String> spaceIds = new HashSet<String>();
                for (EmStatSpace em : emstatSpaceList) {
                    if (!spacemap.containsKey(em.getStatRangeId())) {
                        spacemap.put(em.getStatRangeId(), new HashMap<String, Float>());
                    }
                    spacemap.get(em.getStatRangeId()).put(em.getSpaceItemId(), Float.parseFloat(em.getScoreNum()));
                    spaceIds.add(em.getSpaceItemId());
                }
                Set<String> parmTypes = new HashSet<String>();
                parmTypes.add(EmSpaceItem.PARM_PERCENT_B);
                parmTypes.add(EmSpaceItem.PARM_PERCENT_F);

                List<EmSpaceItem> spaceList = emSpaceItemService.findListByIds(spaceIds.toArray(new String[]{}));
                List<String> titleList = new ArrayList<String>();
                Map<String, String> nameByIdMap = new HashMap<String, String>();
                for (EmSpaceItem item : spaceList) {
                    if (!parmTypes.contains(item.getParmType())) {
                        continue;
                    }
                    if (!titleList.contains(item.getName())) {
                        titleList.add(item.getName());
                    }
                    nameByIdMap.put(item.getId(), item.getName());
                }

                for (EmStatRange item : classStatList) {
                    if (StringUtils.equals(item.getSubjectId(), ExammanageConstants.CON_YSY_ID)) {
                        //一般考试，过滤掉语数英统计
                        continue;
                    }
                    EmSubjectStatDto dto = new EmSubjectStatDto();
                    dto.setClassAvg(item.getAvgScore());
                    dto.setClassMax(item.getMaxScore());
                    dto.setClassMin(item.getMinScore());
                    dto.setStatStuNum(item.getStatNum());
                    dto.setRank(item.getRank());
                    dto.setSpaceMap(new HashMap<String, Float>());
                    dto.setSubjectId(item.getSubjectId());
                    if (courseNameMap.containsKey(dto.getSubjectId())) {
                        dto.setSubjectName(courseNameMap.get(dto.getSubjectId()).getSubjectName());
                    }
                    Map<String, Float> spaceByRangeId = spacemap.get(item.getId());
                    for (String spaceItemId : spaceByRangeId.keySet()) {
                        if (nameByIdMap.containsKey(spaceItemId)) {
                            dto.getSpaceMap().put(nameByIdMap.get(spaceItemId), spaceByRangeId.get(spaceItemId));
                        }
                    }
                    if (gradeAvgMap.containsKey(item.getSubjectId())) {
                        dto.setGradeAvg(gradeAvgMap.get(item.getSubjectId()));
                    }
                    subjectStatDto.add(dto);
                }
                Collections.sort(titleList);
                map.put("titleList", titleList);
                map.put("courseList", courseList);

            }
            map.put("subjectStatDto", subjectStatDto);

            return "/examanalysis/examClass/classScoreList.ftl";
        }

        return promptFlt(map, "查询不存在；请刷新后再次操作");
//		return errorFtl(map,"查询不存在");
    }


    @RequestMapping("/examClass/generalChart/page")
    @ControllerInfo("当前学期历次考试概况")
    public String showClassGeneralChart(String examId, String classId, String analysisType, String subjectId, ModelMap map) {
        //当前学期下
        EmExamInfo exam = emExamInfoService.findOne(examId);
        if (exam == null) {
            return errorFtl(map, "该考试已不存在");
        }
        List<EmExamInfo> examList = findExamIdByClassId(classId, exam);
        toMakeChart(examList, classId, EmStatRange.RANGE_CLASS, subjectId, analysisType, map);
        return "/examanalysis/examGrade/generalChart.ftl";
    }

    /**
     * 科目、分析类型的折线图表
     *
     * @return
     */
    private void toMakeChart(List<EmExamInfo> examList, String rangeId, String rangeType, String subjectId, String analysisType, ModelMap map) {
        //查询
        Set<String> examIds = EntityUtils.getSet(examList, "id");
        List<EmStatRange> rangeList = emStatRangeService.findByRangeIdAndSubjectId(rangeId, rangeType, subjectId, examIds.toArray(new String[]{}));

        Map<String, EmStatObject> objmap = findStatObject(examIds.toArray(new String[]{}));

        Map<String, Float> scoreByExamId = new HashMap<String, Float>();
        String mess = "";
        if (CollectionUtils.isNotEmpty(rangeList)) {
            if (analysisType.startsWith("0_")) {
                if ("0_allAvg".equals(analysisType)) {
                    for (EmStatRange r : rangeList) {
                        if (objmap.containsKey(r.getExamId()) && objmap.get(r.getExamId()).getId().equals(r.getStatObjectId())) {
                            scoreByExamId.put(r.getExamId(), r.getAvgScore());
                        }
                    }
                    mess = "各次考试总体平均分情况";
                } else if ("0_max".equals(analysisType)) {
                    for (EmStatRange r : rangeList) {
                        if (objmap.containsKey(r.getExamId()) && objmap.get(r.getExamId()).getId().equals(r.getStatObjectId())) {
                            scoreByExamId.put(r.getExamId(), r.getMaxScore());
                        }
                    }
                    mess = "各次考试成绩最大值情况";
                } else if ("0_min".equals(analysisType)) {
                    for (EmStatRange r : rangeList) {
                        if (objmap.containsKey(r.getExamId()) && objmap.get(r.getExamId()).getId().equals(r.getStatObjectId())) {
                            scoreByExamId.put(r.getExamId(), r.getMinScore());
                        }
                    }
                    mess = "各次考试成绩最小值情况";
                }
            } else if (analysisType.startsWith("1_")) {
                String name = analysisType.substring(2);
                //根据名称
                Set<String> rangeIds = EntityUtils.getSet(rangeList, "id");
                //年级统计分段结果
                List<EmStatSpace> emStatSpaceList = emStatSpaceService.findByStatRangeIdIn(rangeIds.toArray(new String[]{}));
                Set<String> spaceIds = new HashSet<String>();
                Map<String, List<EmStatSpace>> spaceByRangeId = new HashMap<String, List<EmStatSpace>>();

                if (CollectionUtils.isNotEmpty(emStatSpaceList)) {
                    for (EmStatSpace s : emStatSpaceList) {
                        spaceIds.add(s.getSpaceItemId());
                        if (!spaceByRangeId.containsKey(s.getStatRangeId())) {
                            spaceByRangeId.put(s.getStatRangeId(), new ArrayList<EmStatSpace>());
                        }
                        spaceByRangeId.get(s.getStatRangeId()).add(s);
                    }
                    List<EmSpaceItem> spaceList = emSpaceItemService.findByNameAndIdIn(name, spaceIds.toArray(new String[]{}));
                    //符合条件
                    spaceIds = EntityUtils.getSet(spaceList, "id");
                }


                for (EmStatRange r : rangeList) {
                    if (objmap.containsKey(r.getExamId()) && objmap.get(r.getExamId()).getId().equals(r.getStatObjectId())) {
                        if (spaceByRangeId.containsKey(r.getId())) {
                            List<EmStatSpace> ll = spaceByRangeId.get(r.getId());
                            for (EmStatSpace e : ll) {
                                if (spaceIds.contains(e.getSpaceItemId())) {
                                    scoreByExamId.put(r.getExamId(), Float.parseFloat(e.getScoreNum()));
                                    break;
                                }
                            }
                        }
                    }
                }
                mess = "各次考试" + name + "平均分情况";

            }
        }

        JSONObject json = new JSONObject();
        List<String> xAxisDataList = new ArrayList<String>();
        List<String> xAxisIdList = new ArrayList<String>();
        for (EmExamInfo info : examList) {
            xAxisDataList.add(info.getExamName());
            xAxisIdList.add(info.getId());
        }
        String[] xAxisData = xAxisDataList.toArray(new String[]{});
        Float[][] loadingData = new Float[1][xAxisData.length];
        for (int i = 0; i < xAxisIdList.size(); i++) {
            if (scoreByExamId.containsKey(xAxisIdList.get(i))) {
                loadingData[0][i] = scoreByExamId.get(xAxisIdList.get(i));
            } else {
                loadingData[0][i] = 0f;
            }

        }
        if (StringUtils.isNotBlank(mess)) {
            json.put("text", mess);
        }
        json.put("xAxisData", xAxisData);
        json.put("loadingData", loadingData);
        json.put("legendData", new String[]{"分数"});
        String jsonStringData = json.toString();
        map.put("jsonStringData", jsonStringData);
        map.put("width", toMakeChartWidth(xAxisData.length, "2"));
    }

    /**
     * 计算图表宽度 每个 班级 80 考试 150 分数段100
     *
     * @param size
     * @param type 班级 1 考试 2 分数段3
     * @return 最小值 1000
     */
    private int toMakeChartWidth(int size, String type) {
        int width = 0;
        if ("1".equals(type)) {
            width = size * 80;
        } else if ("2".equals(type)) {
            width = size * 150;
        } else if ("3".equals(type)) {
            width = size * 100;
        }
        if (width >= 1000) {
            return width;
        } else {
            return 1000;
        }
    }
}
