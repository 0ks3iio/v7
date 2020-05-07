package net.zdsoft.exammanage.data.action;

import com.alibaba.fastjson.TypeReference;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.GradeRemoteService;
import net.zdsoft.exammanage.data.dto.EmExamGradeDto;
import net.zdsoft.exammanage.data.dto.EmExamStudentDto;
import net.zdsoft.exammanage.data.entity.*;
import net.zdsoft.exammanage.data.service.*;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/examquery")
public class EmExamQueryAction extends EmExamCommonAction {

    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private EmExamNumService emExamNumService;
    @Autowired
    private EmScoreInfoService emScoreInfoService;
    @Autowired
    private EmStatService emStatService;
    @Autowired
    private EmStatRangeService emStatRangeService;
    @Autowired
    private EmStatObjectService emStatObjectService;

    @RequestMapping("/examGrade/index/page")
    @ControllerInfo(value = "年级成绩查询")
    public String gradeShowIndex(ModelMap map) {
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(getLoginInfo().getUnitId()), new TR<List<Grade>>() {
        });
        map.put("gradeList", gradeList);
        return "/exammanage/examQuery/examGradeIndex.ftl";
    }

    @ResponseBody
    @RequestMapping("/examGrade/examList")
    @ControllerInfo(value = "考试名称列表")
    public List<EmExamInfo> examNameList(String gradeId, ModelMap map) {
        //根据年级id查询班级id
        List<Clazz> classIds = SUtils.dt(classRemoteService.findByGradeIdSortAll(gradeId), new TR<List<Clazz>>() {
        });
        List<EmExamInfo> infoList = new ArrayList<EmExamInfo>();
        if (CollectionUtils.isNotEmpty(classIds)) {
            Set<String> clsId = EntityUtils.getSet(classIds, e -> e.getId());
            List<String> examList = emClassInfoService.findExamIdByClassIds(clsId.toArray(new String[]{}));
            infoList = emExamInfoService.findListByIdsNoDel(examList.toArray(new String[]{}));

        }
        return infoList;
    }

    @ResponseBody
    @RequestMapping("/examGrade/subjectList")
    @ControllerInfo(value = "科目列表")
    public List<EmSubjectInfo> subjectList(String examId, ModelMap map) {
        List<EmSubjectInfo> subjectInfo = emSubjectInfoService.findByExamId(examId);
        return subjectInfo;
    }

    @RequestMapping("/examGrade/queriesList")
    @ControllerInfo(value = "年级成绩列表")
    public String queriesList(String gradeId, String examId, String subjectId, String queryName, String queryType, ModelMap map, HttpServletRequest request) {
        List<EmExamGradeDto> examGradeDtoList = new ArrayList<EmExamGradeDto>();
        boolean inputType = true;
        if (StringUtils.isNotBlank(gradeId) && StringUtils.isNotBlank(examId) && StringUtils.isNotBlank(subjectId)) {
            Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
            List<Clazz> clazzsList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(getLoginInfo().getUnitId(), grade.getId()), new TR<List<Clazz>>() {
            });
            Map<String, Clazz> clazzsMap = EntityUtils.getMap(clazzsList, "id");
            List<String> clazzIdsList = EntityUtils.getList(clazzsList, "id");
            //根据总分分页
            List<Student> studentList = new ArrayList<Student>();
            if (StringUtils.isNotBlank(queryName) && StringUtils.isNotBlank(queryType)) {
                studentList = SUtils.dt(studentRemoteService.findByClassIds(clazzIdsList.toArray(new String[0])), new TR<List<Student>>() {
                });
                if ("0".equals(queryType)) {
                    try {
                        queryName = URLDecoder.decode(queryName, "utf-8");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if ("1".equals(queryType) || "2".equals(queryType)) {
                    Iterator it = studentList.iterator();
                    while (it.hasNext()) {
                        Student student = (Student) it.next();
                        if ("1".equals(queryType)) {
                            if (!student.getStudentCode().equals(queryName)) {
                                it.remove();
                            }
                        } else {
                            if (!student.getIdentityCard().equals(queryName)) {
                                it.remove();
                            }
                        }
                    }
                }
                if ("0".equals(queryType)) {
                    Pattern pattern = Pattern.compile(queryName);
                    Iterator it = studentList.iterator();
                    while (it.hasNext()) {
                        Student student = (Student) it.next();
                        Matcher matcher = pattern.matcher(student.getStudentName());
                        if (!matcher.find()) {
                            it.remove();
                        }
                    }
                }
            } else {
                Pagination page = createPagination();
                studentList = Student.dt(studentRemoteService.findByClassIds(clazzIdsList.toArray(new String[0]), SUtils.s(page)), page);
                map.put("Pagination", page);
                sendPagination(request, map, page);
            }
            Map<String, String> examNumMap = emExamNumService.findBySchoolIdAndExamId(getLoginInfo().getUnitId(), examId);
            List<String> studentIdsList = EntityUtils.getList(studentList, "id");
            EmExamGradeDto dto = null;
            List<String> scoresList = null;
            if (CollectionUtils.isNotEmpty(studentList)) {
                if (subjectId.equals("all")) {
                    Map<String, EmScoreInfo> scoreInfoMap = new HashMap<String, EmScoreInfo>();
                    List<EmScoreInfo> scoreInfoList = emScoreInfoService.findByExamIdAndUnitId(examId, getLoginInfo().getUnitId());
                    for (EmScoreInfo emScoreInfo : scoreInfoList) {
                        scoreInfoMap.put(emScoreInfo.getStudentId() + "_" + emScoreInfo.getSubjectId(), emScoreInfo);
                    }
                    List<EmSubjectInfo> subjectInfoList = emSubjectInfoService.findByExamId(examId);
                    map.put("subjectInfoList", subjectInfoList);
                    Map<String, Map<String, McodeDetail>> mcodeDetailMap = new HashMap<String, Map<String, McodeDetail>>();
                    for (EmSubjectInfo emSubjectInfo : subjectInfoList) {
                        if (emSubjectInfo.getInputType().equals(EmScoreInfo.ACHI_GRADE) && StringUtils.isNotBlank(emSubjectInfo.getGradeType())) {
                            inputType = false;
                            List<McodeDetail> gradeTypeList = SUtils.dt(mcodeRemoteService.findByMcodeIds(emSubjectInfo.getGradeType()), new TypeReference<List<McodeDetail>>() {
                            });
                            Map<String, McodeDetail> mcodeMap = EntityUtils.getMap(gradeTypeList, "thisId");
                            mcodeDetailMap.put(emSubjectInfo.getSubjectId(), mcodeMap);
                        }
                    }
                    double maxScore;
                    for (Student student : studentList) {
                        maxScore = 0;
                        dto = new EmExamGradeDto();
                        scoresList = new ArrayList<String>();
                        dto.setStudentName(student.getStudentName());
                        dto.setStudentCode(student.getStudentCode());
                        dto.setIdentityCard(student.getIdentityCard());
                        dto.setClassName(clazzsMap.get(student.getClassId()).getClassNameDynamic());
                        dto.setExamNumber(examNumMap.get(student.getId()));
                        for (EmSubjectInfo emSubjectInfo : subjectInfoList) {
                            if (emSubjectInfo.getInputType().equals(EmScoreInfo.ACHI_GRADE)) {
                                Map<String, McodeDetail> mcodeMap = mcodeDetailMap.get(emSubjectInfo.getSubjectId());
                                EmScoreInfo socreInfo = scoreInfoMap.get(student.getId() + "_" + emSubjectInfo.getSubjectId());
                                if (socreInfo != null) {
                                    if (socreInfo.getScoreStatus().equals("1")) {
                                        scoresList.add("0");
                                    } else if (socreInfo.getScoreStatus().equals("2")) {
                                        scoresList.add("\\");
                                    } else {
                                        scoresList.add(mcodeMap.get(socreInfo.getScore()).getMcodeContent());
                                    }
                                } else {
                                    scoresList.add(null);
                                }
                            } else {
                                EmScoreInfo socreInfo = scoreInfoMap.get(student.getId() + "_" + emSubjectInfo.getSubjectId());
                                if (socreInfo != null) {
                                    if (socreInfo.getScoreStatus().equals("1")) {
                                        scoresList.add("0");
                                    } else if (socreInfo.getScoreStatus().equals("2")) {
                                        scoresList.add("\\");
                                    } else {
                                        maxScore += Double.parseDouble(socreInfo.getScore());
                                        scoresList.add(socreInfo.getScore());
                                    }
                                } else {
                                    scoresList.add(null);
                                }
                            }
                        }
                        if (inputType) {
                            dto.setTotal(String.valueOf(maxScore));
                        }
                        dto.setScoresList(scoresList);
                        examGradeDtoList.add(dto);
                    }
                } else {
                    List<EmSubjectInfo> emSubjectList = emSubjectInfoService.findByExamIdAndSubjectId(examId, subjectId);
                    EmSubjectInfo info = emSubjectList.get(0);
                    map.put("subjectName", info.getCourseName());
                    List<McodeDetail> gradeTypeList = new ArrayList<McodeDetail>();
                    if (info.getInputType().equals(EmScoreInfo.ACHI_GRADE) && StringUtils.isNotBlank(info.getGradeType())) {
                        gradeTypeList = SUtils.dt(mcodeRemoteService.findByMcodeIds(info.getGradeType()), new TypeReference<List<McodeDetail>>() {
                        });
                    }
                    Map<String, McodeDetail> mcodeDetailMap = EntityUtils.getMap(gradeTypeList, "thisId");
                    Map<String, EmScoreInfo> emScoreInfoMap = emScoreInfoService.findByStudent(examId, subjectId, studentIdsList.toArray(new String[0]));
                    for (Student student : studentList) {
                        dto = new EmExamGradeDto();
                        scoresList = new ArrayList<String>();
                        dto.setStudentName(student.getStudentName());
                        dto.setStudentCode(student.getStudentCode());
                        dto.setIdentityCard(student.getIdentityCard());
                        dto.setClassName(clazzsMap.get(student.getClassId()).getClassNameDynamic());
                        dto.setExamNumber(examNumMap.get(student.getId()));
                        EmScoreInfo emScoreInfo = emScoreInfoMap.get(student.getId());
                        if (info.getInputType().equals(EmScoreInfo.ACHI_GRADE)) {
                            if (emScoreInfo != null) {
                                if (emScoreInfo.getScoreStatus().equals("1")) {
                                    dto.setTotal("0");
                                    scoresList.add("0");
                                } else if (emScoreInfo.getScoreStatus().equals("2")) {
                                    dto.setTotal("\\");
                                    scoresList.add("\\");
                                } else {
                                    dto.setTotal(mcodeDetailMap.get(emScoreInfo.getScore()).getMcodeContent());
                                    scoresList.add(mcodeDetailMap.get(emScoreInfo.getScore()).getMcodeContent());
                                }
                            } else {
                                dto.setTotal(null);
                                scoresList.add(null);
                            }
                        } else {
                            if (emScoreInfo != null) {
                                if (emScoreInfo.getScoreStatus().equals("1")) {
                                    dto.setTotal("0");
                                    scoresList.add("0");
                                } else if (emScoreInfo.getScoreStatus().equals("2")) {
                                    dto.setTotal("\\");
                                    scoresList.add("\\");
                                } else {
                                    dto.setTotal(emScoreInfo.getScore());
                                    scoresList.add(emScoreInfo.getScore());
                                }
                            } else {
                                dto.setTotal("-1");
                                scoresList.add(null);
                            }
                        }
                        dto.setScoresList(scoresList);
                        examGradeDtoList.add(dto);
                    }
                    if (info.getInputType().equals(EmScoreInfo.ACHI_GRADE)) {
                        inputType = false;
                    }
                }
            }
        }
        map.put("examGradeDtoList", examGradeDtoList);
        map.put("inputType", inputType);
        map.put("type", subjectId);
        if (StringUtils.isBlank(queryName)) {
            queryName = "no";
        }
        map.put("queryName", queryName);
        return "/exammanage/examQuery/examGradeList.ftl";
    }

    @RequestMapping("/examClass/index/page")
    @ControllerInfo(value = "班级成绩查询")
    public String classShowIndex(ModelMap map) {
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchoolId(getLoginInfo().getUnitId()), new TR<List<Grade>>() {
        });
        map.put("gradeList", gradeList);
        return "/exammanage/examQuery/examClassIndex.ftl";
    }

    @ResponseBody
    @RequestMapping("/examClass/classList")
    @ControllerInfo(value = "班级列表")
    public List<Clazz> classList(String gradeId, ModelMap map) {
        Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
        List<Clazz> clazzsList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(getLoginInfo().getUnitId(), grade.getId()), new TR<List<Clazz>>() {
        });
        return clazzsList;
    }

    @RequestMapping("/examClass/queriesList")
    @ControllerInfo(value = "班级成绩列表")
    public String queriesClassList(String checked, String gradeId, String examId, String subjectId, String classId, String queryName, String queryType, ModelMap map) {
        List<EmExamGradeDto> examGradeDtoList = new ArrayList<EmExamGradeDto>();
        boolean inputType = true;
        if (StringUtils.isNotBlank(gradeId) && StringUtils.isNotBlank(examId) && StringUtils.isNotBlank(subjectId) && StringUtils.isNotBlank(classId)) {
            Grade grade = SUtils.dc(gradeRemoteService.findOneById(gradeId), Grade.class);
            List<Clazz> clazzsList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(getLoginInfo().getUnitId(), grade.getId()), new TR<List<Clazz>>() {
            });
            Map<String, Clazz> clazzsMap = EntityUtils.getMap(clazzsList, "id");
            List<String> clazzIdsList = EntityUtils.getList(clazzsList, "id");
            List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(clazzIdsList.toArray(new String[0])), new TR<List<Student>>() {
            });
            Map<String, String> examNumMap = emExamNumService.findBySchoolIdAndExamId(getLoginInfo().getUnitId(), examId);
            List<String> studentIdsList = EntityUtils.getList(studentList, "id");
            EmExamGradeDto dto = null;
            List<String> scoresList = null;
            boolean Cheat;
            boolean Reforming;
            boolean Missing;
            if (CollectionUtils.isNotEmpty(studentList)) {
                if (subjectId.equals("all")) {
                    Map<String, EmScoreInfo> scoreInfoMap = new HashMap<String, EmScoreInfo>();
                    List<EmScoreInfo> scoreInfoList = emScoreInfoService.findByExamIdAndUnitId(examId, getLoginInfo().getUnitId());
                    for (EmScoreInfo emScoreInfo : scoreInfoList) {
                        scoreInfoMap.put(emScoreInfo.getStudentId() + "_" + emScoreInfo.getSubjectId(), emScoreInfo);
                    }
                    List<EmSubjectInfo> subjectInfoList = emSubjectInfoService.findByExamId(examId);
                    map.put("subjectInfoList", subjectInfoList);
                    Map<String, Map<String, McodeDetail>> mcodeDetailMap = new HashMap<String, Map<String, McodeDetail>>();
                    for (EmSubjectInfo emSubjectInfo : subjectInfoList) {
                        if (emSubjectInfo.getInputType().equals(EmScoreInfo.ACHI_GRADE) && StringUtils.isNotBlank(emSubjectInfo.getGradeType())) {
                            inputType = false;
                            List<McodeDetail> gradeTypeList = SUtils.dt(mcodeRemoteService.findByMcodeIds(emSubjectInfo.getGradeType()), new TypeReference<List<McodeDetail>>() {
                            });
                            Map<String, McodeDetail> mcodeMap = EntityUtils.getMap(gradeTypeList, "thisId");
                            mcodeDetailMap.put(emSubjectInfo.getSubjectId(), mcodeMap);
                        }
                    }
                    double maxScore;
                    for (Student student : studentList) {
                        Cheat = false;
                        Reforming = false;
                        Missing = false;
                        maxScore = 0;
                        dto = new EmExamGradeDto();
                        scoresList = new ArrayList<String>();
                        dto.setStudentName(student.getStudentName());
                        dto.setStudentCode(student.getStudentCode());
                        dto.setIdentityCard(student.getIdentityCard());
                        dto.setClassName(clazzsMap.get(student.getClassId()).getClassNameDynamic());
                        dto.setClassId(student.getClassId());
                        dto.setExamNumber(examNumMap.get(student.getId()));
                        for (EmSubjectInfo emSubjectInfo : subjectInfoList) {
                            EmScoreInfo emScoreInfo = scoreInfoMap.get(student.getId() + "_" + emSubjectInfo.getSubjectId());
                            if (emScoreInfo != null) {
                                if (!Cheat) {
                                    Cheat = emScoreInfo.getScoreStatus().equals("2");
                                }
                                if (!Reforming) {
                                    Reforming = "0".equals(emScoreInfo.getFacet());
                                }
                                if (!Missing) {
                                    Missing = emScoreInfo.getScoreStatus().equals("1");
                                }
                            }
                            if (emScoreInfo != null) {
                                if (emSubjectInfo.getInputType().equals(EmScoreInfo.ACHI_GRADE)) {
                                    Map<String, McodeDetail> mcodeMap = mcodeDetailMap.get(emSubjectInfo.getSubjectId());
                                    if (StringUtils.isNotBlank(emScoreInfo.getScore())) {
                                        if (emScoreInfo.getScoreStatus().equals("1")) {
                                            scoresList.add("0");
                                        } else if (emScoreInfo.getScoreStatus().equals("2")) {
                                            scoresList.add("\\");
                                        } else {
                                            scoresList.add(mcodeMap.get(emScoreInfo.getScore()).getMcodeContent());
                                        }
                                    } else {
                                        scoresList.add(null);
                                    }
                                } else {
                                    if (StringUtils.isNotBlank(emScoreInfo.getScore())) {
                                        if (emScoreInfo.getScoreStatus().equals("1")) {
                                            scoresList.add("0");
                                        } else if (emScoreInfo.getScoreStatus().equals("2")) {
                                            scoresList.add("\\");
                                        } else {
                                            maxScore += Double.parseDouble(emScoreInfo.getScore());
                                            scoresList.add(emScoreInfo.getScore());
                                        }
                                    } else {
                                        scoresList.add(null);
                                    }
                                }
                            } else {
                                scoresList.add(null);
                            }
                        }
                        dto.setCheat(Cheat);
                        dto.setReforming(Reforming);
                        dto.setMissing(Missing);
                        if (inputType) {
                            dto.setTotal(String.valueOf(maxScore));
                        }
                        dto.setScoresList(scoresList);
                        examGradeDtoList.add(dto);
                    }
                    if (inputType) {
                        sortList(examGradeDtoList);
                    }
                } else {
                    List<EmSubjectInfo> emSubjectList = emSubjectInfoService.findByExamIdAndSubjectId(examId, subjectId);
                    EmSubjectInfo info = emSubjectList.get(0);
                    map.put("subjectName", info.getCourseName());
                    List<McodeDetail> gradeTypeList = new ArrayList<McodeDetail>();
                    if (info.getInputType().equals(EmScoreInfo.ACHI_GRADE) && StringUtils.isNotBlank(info.getGradeType())) {
                        gradeTypeList = SUtils.dt(mcodeRemoteService.findByMcodeIds(info.getGradeType()), new TypeReference<List<McodeDetail>>() {
                        });
                    }
                    Map<String, McodeDetail> mcodeDetailMap = EntityUtils.getMap(gradeTypeList, "thisId");
                    Map<String, EmScoreInfo> emScoreInfoMap = emScoreInfoService.findByStudent(examId, subjectId, studentIdsList.toArray(new String[0]));
                    for (Student student : studentList) {
                        Cheat = false;
                        Reforming = false;
                        Missing = false;
                        EmScoreInfo emScoreInfo = emScoreInfoMap.get(student.getId());
                        if (emScoreInfo != null) {
                            Cheat = emScoreInfo.getScoreStatus().equals("2");
//							Reforming = emScoreInfo.getFacet().equals("0");
                            Reforming = !"1".equals(emScoreInfo.getFacet());
                            Missing = emScoreInfo.getScoreStatus().equals("1");
                        }
                        dto = new EmExamGradeDto();
                        scoresList = new ArrayList<String>();
                        dto.setCheat(Cheat);
                        dto.setReforming(Reforming);
                        dto.setMissing(Missing);
                        dto.setStudentName(student.getStudentName());
                        dto.setStudentCode(student.getStudentCode());
                        dto.setIdentityCard(student.getIdentityCard());
                        dto.setClassName(clazzsMap.get(student.getClassId()).getClassNameDynamic());
                        dto.setClassId(student.getClassId());
                        dto.setExamNumber(examNumMap.get(student.getId()));
                        if (info.getInputType().equals(EmScoreInfo.ACHI_GRADE)) {
                            if (emScoreInfo != null) {
                                if (emScoreInfo.getScoreStatus().equals("1")) {
                                    dto.setTotal("0");
                                    scoresList.add("0");
                                } else if (emScoreInfo.getScoreStatus().equals("2")) {
                                    dto.setTotal("\\");
                                    scoresList.add("\\");
                                } else {
                                    dto.setTotal(mcodeDetailMap.get(emScoreInfo.getScore()).getMcodeContent());
                                    scoresList.add(mcodeDetailMap.get(emScoreInfo.getScore()).getMcodeContent());
                                }
                            } else {
                                dto.setTotal(null);
                                scoresList.add(null);
                            }
                        } else {
                            if (emScoreInfo != null) {
                                if (emScoreInfo.getScoreStatus().equals("1")) {
                                    dto.setTotal("0");
                                    scoresList.add("0");
                                } else if (emScoreInfo.getScoreStatus().equals("2")) {
                                    dto.setTotal("\\");
                                    scoresList.add("\\");
                                } else {
                                    dto.setTotal(emScoreInfo.getScore());
                                    scoresList.add(emScoreInfo.getScore());
                                }
                            } else {
                                dto.setTotal("-1");
                                scoresList.add(null);
                            }
                        }
                        dto.setScoresList(scoresList);
                        examGradeDtoList.add(dto);
                    }
                    if (info.getInputType().equals(EmScoreInfo.ACHI_GRADE)) {
                        inputType = false;
                    } else {
                        sortList(examGradeDtoList);
                    }
                }
            }
        }
        //学生查询
        if (StringUtils.isNotBlank(queryName) && StringUtils.isNotBlank(queryType) && CollectionUtils.isNotEmpty(examGradeDtoList)) {
            findStudents(queryName, queryType, examGradeDtoList);
        }
        //更多条件的显示
        if (StringUtils.isNotBlank(checked) && CollectionUtils.isNotEmpty(examGradeDtoList)) {
            isShow(checked, examGradeDtoList);
        }
        //班级筛选
        if (StringUtils.isNotBlank(classId) && CollectionUtils.isNotEmpty(examGradeDtoList)) {
            Iterator it = examGradeDtoList.iterator();
            while (it.hasNext()) {
                EmExamGradeDto emExamGradeDto = (EmExamGradeDto) it.next();
                if (!emExamGradeDto.getClassId().equals(classId)) {
                    it.remove();
                }
            }
        }
        map.put("examGradeDtoList", examGradeDtoList);
        map.put("inputType", inputType);
        map.put("type", subjectId);
        return "/exammanage/examQuery/examClassList.ftl";
    }


    @RequestMapping("/examStudent/index/page")
    @ControllerInfo(value = "个人成绩查询")
    public String studentShowIndex(ModelMap map) {
        return "/exammanage/examQuery/examStudentIndex.ftl";
    }

    @RequestMapping("/examStudent/Tab/page")
    @ControllerInfo(value = "个人成绩查询Tab")
    public String studentShowTab(String queryType, String queryContent, ModelMap map) {
        Student student = null;
        List<EmExamInfo> examList = new ArrayList<EmExamInfo>();
        if ("0".equals(queryType)) {
            try {
                queryContent = URLDecoder.decode(queryContent, "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            List<Student> studentList = SUtils.dt(studentRemoteService.findBySchoolIdIn(queryContent, new String[]{getLoginInfo().getUnitId()}), new TR<List<Student>>() {
            });
            if (CollectionUtils.isNotEmpty(studentList)) {
                student = studentList.get(0);
            }
        } else {
            student = SUtils.dc(studentRemoteService.findBySchIdStudentCode(getLoginInfo().getUnitId(), queryContent), Student.class);
        }
        if (student != null) {
            Clazz clazz = SUtils.dc(classRemoteService.findOneById(student.getClassId()), Clazz.class);
            Grade grade = SUtils.dc(gradeRemoteService.findOneById(clazz.getGradeId()), Grade.class);
            //根据年级id查询班级id
            List<Clazz> classIds = SUtils.dt(classRemoteService.findByGradeIdSortAll(grade.getId()), new TR<List<Clazz>>() {
            });
            if (CollectionUtils.isNotEmpty(classIds)) {
                Set<String> clsId = EntityUtils.getSet(classIds, e -> e.getId());
                List<String> examClasInfoList = emClassInfoService.findExamIdByClassIds(clsId.toArray(new String[]{}));
                examList = emExamInfoService.findListByIdsNoDel(examClasInfoList.toArray(new String[]{}));
            }
            map.put("studentName", student.getStudentName());
            map.put("studentId", student.getId());
            map.put("clazzId", clazz.getId());
            map.put("gradeId", grade.getId());
        } else {
            map.put("studentName", "");
        }
        map.put("examList", examList);
        return "/exammanage/examQuery/examStudentTab.ftl";
    }

    @RequestMapping("/examStudent/List/page")
    @ControllerInfo(value = "个人成绩查询List")
    public String studentShowList(String examId, String studentId, String clazzId, String gradeId, ModelMap map) {
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
//			return errorFtl(map,"没有统计数据");
        }
        List<EmExamStudentDto> examStudentDtoList = new ArrayList<EmExamStudentDto>();
        List<EmSubjectInfo> subjectInfo = emSubjectInfoService.findByExamId(examId);
        if (subjectInfo.size() != 0) {
            Map<String, Map<String, McodeDetail>> mcodeDetailMap = new HashMap<String, Map<String, McodeDetail>>();
            boolean stype = true;
            for (EmSubjectInfo emSubjectInfo : subjectInfo) {
                if (emSubjectInfo.getInputType().equals(EmScoreInfo.ACHI_GRADE) && StringUtils.isNotBlank(emSubjectInfo.getGradeType())) {
                    stype = false;
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
            Map<String, EmScoreInfo> scoreInfoMap = new HashMap<String, EmScoreInfo>();
            List<EmScoreInfo> scoreInfoList = emScoreInfoService.findByExamIdAndUnitId(examId, getLoginInfo().getUnitId());
            for (EmScoreInfo emScoreInfo : scoreInfoList) {
                scoreInfoMap.put(emScoreInfo.getStudentId() + "_" + emScoreInfo.getSubjectId(), emScoreInfo);
            }
            EmExamStudentDto emExamStudentDto = null;
            for (EmSubjectInfo emSubjectInfo : subjectInfo) {
                emExamStudentDto = new EmExamStudentDto();
                emExamStudentDto.setSubjectName(emSubjectInfo.getCourseName());
                EmStat emStat = emStatService.findByExamIdAndSubjectIdAndStudentId(statObject.getId(), examId, emSubjectInfo.getSubjectId(), studentId);
                EmStatRange classScoreInfo = emStatRangeService.findByExamIdAndSubIdAndRangIdAndType1(statObject.getId(), examId, emSubjectInfo.getSubjectId(), clazzId, EmStatRange.RANGE_CLASS);
                EmStatRange schoolScoreInfo = emStatRangeService.findByExamIdAndSubIdAndRangIdAndType1(statObject.getId(), examId, emSubjectInfo.getSubjectId(), getLoginInfo().getUnitId(), EmStatRange.RANGE_SCHOOL);
                EmScoreInfo emScoreInfo = scoreInfoMap.get(studentId + "_" + emSubjectInfo.getSubjectId());
                if (emSubjectInfo.getInputType().equals(EmScoreInfo.ACHI_GRADE)) {
                    if (emScoreInfo == null) {
                        emExamStudentDto.setTotal(null);
                    } else {
                        if (emScoreInfo.getScoreStatus().equals("1")) {
                            emExamStudentDto.setTotal("0");
                        } else if (emScoreInfo.getScoreStatus().equals("2")) {
                            emExamStudentDto.setTotal("\\");
                        } else {
                            emExamStudentDto.setTotal(mcodeDetailMap.get(emSubjectInfo.getGradeType()).get(emScoreInfo.getScore()).getMcodeContent());
                        }
                    }
                } else {
                    if (emScoreInfo == null) {
                        emExamStudentDto.setTotal(null);
                    } else {
                        if (emScoreInfo.getScoreStatus().equals("1")) {
                            emExamStudentDto.setTotal("0");
                            emExamStudentDto.setGradeRanking(null);
                            emExamStudentDto.setClassRanking(null);
                        } else if (emScoreInfo.getScoreStatus().equals("2")) {
                            emExamStudentDto.setTotal("\\");
                            emExamStudentDto.setGradeRanking(null);
                            emExamStudentDto.setClassRanking(null);
                        } else {
                            emExamStudentDto.setTotal(emScoreInfo.getScore());
                            if (emStat != null) {
                                emExamStudentDto.setGradeRanking(String.valueOf(emStat.getGradeRank()));
                                emExamStudentDto.setClassRanking(String.valueOf(emStat.getClassRank()));
                            }
                        }
                        if (classScoreInfo != null) {
                            emExamStudentDto.setClassAverage(String.valueOf(classScoreInfo.getAvgScore()));
                            emExamStudentDto.setClassMax(String.valueOf(classScoreInfo.getMaxScore()));
                            emExamStudentDto.setClassMin(String.valueOf(classScoreInfo.getMinScore()));
                        }
                        if (schoolScoreInfo != null) {
                            emExamStudentDto.setGradeAverage(String.valueOf(schoolScoreInfo.getAvgScore()));
                        }
                    }
                }
                examStudentDtoList.add(emExamStudentDto);
            }
        }
        map.put("examStudentDtoList", examStudentDtoList);
        return "/exammanage/examQuery/examStudentList.ftl";
    }

    public List<EmExamGradeDto> findStudents(String queryName, String queryType, List<EmExamGradeDto> examGradeDtoList) {
        if ("0".equals(queryType)) {
            try {
                queryName = URLDecoder.decode(queryName, "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if ("1".equals(queryType) || "2".equals(queryType)) {
            Iterator it = examGradeDtoList.iterator();
            while (it.hasNext()) {
                EmExamGradeDto emexamGradeDto = (EmExamGradeDto) it.next();
                if ("1".equals(queryType)) {
                    if (!emexamGradeDto.getStudentCode().equals(queryName)) {
                        it.remove();
                    }
                } else {
                    if (!emexamGradeDto.getIdentityCard().equals(queryName)) {
                        it.remove();
                    }
                }
            }
        }
        if ("0".equals(queryType)) {
            Pattern pattern = Pattern.compile(queryName);
            Iterator it = examGradeDtoList.iterator();
            while (it.hasNext()) {
                EmExamGradeDto emexamGradeDto = (EmExamGradeDto) it.next();
                Matcher matcher = pattern.matcher(emexamGradeDto.getStudentName());
                if (!matcher.find()) {
                    it.remove();
                }
            }
        }
        return examGradeDtoList;
    }

    public void sortList(List<EmExamGradeDto> examGradeDtoList) {
        Collections.sort(examGradeDtoList, new Comparator<EmExamGradeDto>() {
            @Override
            public int compare(EmExamGradeDto o1, EmExamGradeDto o2) {
                String o1val = o1.getTotal(), o2val = o2.getTotal();
                if ("\\".equals(o1.getTotal())) {
                    o1val = "0";
                }
                if ("\\".equals(o2.getTotal())) {
                    o2val = "0";
                }
                double d = Double.parseDouble(o1val) - Double.parseDouble(o2val);
                if (0 < d && d < 1) {
                    return 1;
                } else if (-1 < d && d < 0) {
                    return -1;
                } else {
                    return (int) d;
                }
            }
        });
    }

    public void isShow(String checked, List<EmExamGradeDto> examGradeDtoList) {
        List<String> checkedList = Arrays.asList(checked.split(","));
        boolean isCheat;
        boolean isReforming;
        boolean isMissing;
        Iterator it = examGradeDtoList.iterator();
        while (it.hasNext()) {
            EmExamGradeDto emexamGradeDto = (EmExamGradeDto) it.next();
            if (checkedList.contains("0")) {
                isCheat = emexamGradeDto.isCheat();
            } else {
                isCheat = true;
            }
            if (checkedList.contains("1")) {
                isReforming = emexamGradeDto.isReforming();
            } else {
                isReforming = true;
            }
            if (checkedList.contains("2")) {
                isMissing = emexamGradeDto.isMissing();
            } else {
                isMissing = true;
            }
            if (isCheat && isReforming && isMissing) {
                continue;
            }
            it.remove();
        }
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
}
