package net.zdsoft.exammanage.data.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.StudentSelectSubjectRemoteService;
import net.zdsoft.exammanage.data.constant.ExammanageConstants;
import net.zdsoft.exammanage.data.entity.*;
import net.zdsoft.exammanage.data.service.*;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/exammanage")
public class EmExamStatAction extends EmExamCommonAction {
    @Autowired
    private EmStatParmService emStatParmService;
    @Autowired
    private EmScoreInfoService emScoreInfoService;
    @Autowired
    private EmFiltrationService emFiltrationService;
    @Autowired
    private EmStatService emStatService;
    @Autowired
    private EmStatObjectService emStatObjectService;
    @Autowired
    private StudentSelectSubjectRemoteService studentSelectSubjectRemoteService;

    @RequestMapping("/scoreStat/index/page")
    @ControllerInfo(value = "成绩处理")
    public String showIndex(ModelMap map) {
        return "/exammanage/scoreStat/scoreStatIndex.ftl";
    }

    @RequestMapping("/scoreStat/head/page")
    @ControllerInfo(value = "成绩处理设置")
    public String showHead(ModelMap map, HttpSession httpSession) {
        String url = "/exammanage/scoreStat/scoreStatHead.ftl";
        return showHead(map, httpSession, url);
    }

    @RequestMapping("/scoreStat/list1/page")
    @ControllerInfo("30天考试列表")
    public String showListIn(ModelMap map, HttpSession httpSession) {
        String url = "/exammanage/scoreStat/scoreStatList.ftl";
        showExamInfoIn(map, httpSession, url, true);
        List<EmExamInfo> examInfoList = (List<EmExamInfo>) map.get("examInfoList");
        makeIsStat(examInfoList);
        map.put("examInfoList", examInfoList);
        return url;
    }

    @RequestMapping("/scoreStat/list2/page")
    @ControllerInfo("30天前考试列表")
    public String showListBefore(String searchAcadyear, String searchSemester, String searchType, String searchGradeCode, ModelMap map, HttpSession httpSession) {
        String url = "/exammanage/scoreStat/scoreStatList.ftl";
        showExamInfoBefore(searchAcadyear, searchSemester, searchType, searchGradeCode, map, httpSession, url, true);
        List<EmExamInfo> examInfoList = (List<EmExamInfo>) map.get("examInfoList");
        makeIsStat(examInfoList);
        map.put("examInfoList", examInfoList);
        return url;
    }


    private void makeIsStat(List<EmExamInfo> examInfoList) {
        if (CollectionUtils.isNotEmpty(examInfoList)) {
            //本单位直接是否统计
            List<EmStatObject> objList = emStatObjectService.findByUnitId(getLoginInfo().getUnitId());
            if (CollectionUtils.isNotEmpty(objList)) {
                Map<String, EmStatObject> objByExamId = new HashMap<String, EmStatObject>();
                for (EmStatObject obj : objList) {
                    objByExamId.put(obj.getExamId(), obj);
                }
                for (EmExamInfo info : examInfoList) {
                    if (objByExamId.containsKey(info.getId())) {
                        info.setIsStat(objByExamId.get(info.getId()).getIsStat());
                    } else {
                        info.setIsStat("0");
                    }
                }
            }
        }

    }

    @RequestMapping("/scoreStat/setStat/page")
    @ControllerInfo("去统计")
    public String setStatIndex(String examId, ModelMap map, HttpSession httpSession) {
        map.put("examId", examId);
        String unitId = getLoginInfo().getUnitId();
        final String typeKey = unitId + "_" + examId + "_type";
        String type = RedisUtils.get(typeKey);
        if (type != null) {
            if ("校验".equals(type)) {
                map.put("type", "1");
            } else {
                map.put("type", "2");
            }
        }
        map.put("type", "0");
        return "/exammanage/scoreStat/scoreStat.ftl";
    }

    @RequestMapping("/scoreStat/setStatParm/page")
    @ControllerInfo("统计参数")
    public String setStatParmIndex(String examId, String isView, ModelMap map, HttpSession httpSession) {
        //统计参数
        List<EmSubjectInfo> subjectInfoList = emSubjectInfoService.findByExamId(examId);
        if (CollectionUtils.isEmpty(subjectInfoList)) {
            return promptFlt(map, "无考试科目数据");
        }
        //去除等第统计
        subjectInfoList = takeCanStat(subjectInfoList);
        map.put("subjectInfoList", subjectInfoList);
        map.put("examId", examId);
        map.put("isView", isView);
        EmExamInfo exam = emExamInfoService.findOne(examId);

        if (StringUtils.equals(exam.getIsgkExamType(), "1")) {
            map.put("isgk", "1");
        } else {
            map.put("isgk", "0");
        }
        return "/exammanage/scoreStat/scoreSubjectTab.ftl";
    }

    @RequestMapping("/scoreStat/showParmBySubject/page")
    @ControllerInfo("统计")
    public String showParmBySubject(String examId, String subjectId, String isView, ModelMap map, HttpSession httpSession) {
        String unitId = getLoginInfo().getUnitId();
        if ("all".equals(subjectId) || BaseConstants.ZERO_GUID.equals(subjectId)) {
            subjectId = BaseConstants.ZERO_GUID;
        }
        if ("conAll".equals(subjectId) || ExammanageConstants.CON_SUM_ID.equals(subjectId)) {
            subjectId = ExammanageConstants.CON_SUM_ID;//语数英+选考
        }
        EmStatParm emStatParm = emStatParmService.findBySubjectId(unitId, examId, subjectId);
        float maxScore = 0;
        if (BaseConstants.ZERO_GUID.equals(subjectId)) {
            List<EmSubjectInfo> subjectInfoList = emSubjectInfoService.findByExamId(examId);
            //去除等第统计
            subjectInfoList = takeCanStat(subjectInfoList);
            for (EmSubjectInfo s : subjectInfoList) {
                maxScore = maxScore + s.getFullScore();
            }
        } else if (ExammanageConstants.CON_SUM_ID.equals(subjectId)) {
            List<EmSubjectInfo> subjectInfoList = emSubjectInfoService.findByExamId(examId);
            //去除等第统计
            subjectInfoList = takeCanStat(subjectInfoList);
            List<Course> courseList73 = SUtils.dt(courseRemoteService.findByCodes73(unitId), new TR<List<Course>>() {
            });
            setYsyFlag(subjectInfoList, courseList73);
            for (EmSubjectInfo s : subjectInfoList) {
                if (s.isYsy()) {
                    maxScore = maxScore + s.getFullScore();
                } else {
                    if (!StringUtils.equals(s.getGkSubType(), "2")) {
                        maxScore = maxScore + s.getFullScore();
                    }
                }
            }
        } else {
            //取得最大分数
            List<EmSubjectInfo> ll = emSubjectInfoService.findByExamIdAndSubjectId(examId, subjectId);
            if (CollectionUtils.isEmpty(ll) || ll.size() > 1) {
                //不存在或者设置有错误 科目重复

            }
            EmSubjectInfo info = ll.get(0);
            if (!"S".equals(info.getInputType())) {
                //不是分数
            }
            maxScore = info.getFullScore();
        }
        if (maxScore == 0) {
            maxScore = 100;
        }
        //微代码DM-XSLY
        Map<String, String> sourceMap = new LinkedHashMap<String, String>();
        List<McodeDetail> mlist = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-XSLY"), new TR<List<McodeDetail>>() {
        });
        String allSource = "";
        if (CollectionUtils.isNotEmpty(mlist)) {
            for (McodeDetail m : mlist) {
                sourceMap.put(m.getThisId(), m.getMcodeContent());
                allSource = allSource + "," + m.getThisId();
            }
            if (StringUtils.isNotBlank(allSource)) {
                allSource = allSource.substring(1);
            }
        }
        map.put("sourceMap", sourceMap);
        if (emStatParm == null) {
            emStatParm = new EmStatParm();
            emStatParm.setSubjectId(subjectId);
            emStatParm.setExamId(examId);
            emStatParm.setStatSpaceType(EmStatParm.SPACE_PASSIVE);
            emStatParm.setLowScore(0f);
            emStatParm.setUpScore(maxScore);
            emStatParm.setSourseType(allSource);//默认全选
            emStatParm.setIsCheat("1");
            emStatParm.setIsMiss("1");
            emStatParm.setIsZero("1");
            emStatParm.setIsOnlystat("0");
            emStatParm.setNeedLineStat("0");
        }
        List<String> lines = new ArrayList<>();
        if (StringUtils.equals(emStatParm.getNeedLineStat(), "1")) {
            String[] lineArrs = emStatParm.getLineSpaces().split(",");
            for (String line : lineArrs) {
                lines.add(line);
            }
        }
        map.put("lines", lines);
        if ("1".equals(isView)) {
            //仅查看
            map.put("maxScore", maxScore);
            map.put("subjectId", subjectId);
            map.put("examId", examId);
            map.put("emStatParm", emStatParm);
            return "/exammanage/scoreStat/scoreParmShow.ftl";
        }
        map.put("maxScore", maxScore);
        map.put("subjectId", subjectId);
        map.put("examId", examId);
        map.put("emStatParm", emStatParm);
        return "/exammanage/scoreStat/scoreParm.ftl";
    }


    @ResponseBody
    @RequestMapping("/scoreStat/saveStatParm")
    @ControllerInfo("统计参数保存")
    public String saveStatParm(EmStatParm dto, String lines, ModelMap map, HttpSession httpSession) {
        //统计参数
        if (dto == null) {
            return error("保存失败");
        }
        EmExamInfo exam = emExamInfoService.findOne(dto.getExamId());
        if (exam == null) {
            return error("该考试已经不存在");
        }
        boolean isSingle = true;

        if (exam.getUnitId().equals(getLoginInfo().getUnitId()) || getLoginInfo().getUnitClass() != 2) {
            isSingle = false;
        }
        String unitId = getLoginInfo().getUnitId();
        if ("all".equals(dto.getSubjectId())) {
            dto.setSubjectId(BaseConstants.ZERO_GUID);
        }
        if ("conAll".equals(dto.getSubjectId())) {
            dto.setSubjectId(ExammanageConstants.CON_SUM_ID);
        }
        if (StringUtils.isNotBlank(lines) && StringUtils.equals(dto.getNeedLineStat(), "1")) {
            dto.setLineSpaces(lines);
        }
        try {
            emStatParmService.saveParm(unitId, dto, isSingle);
        } catch (Exception e) {
            return error("保存失败");
        }
        return success("");
    }

    @ResponseBody
    @RequestMapping("/scoreStat/verificationData")
    @ControllerInfo("校验")
    public String verificationData(final String examId, ModelMap map, HttpSession httpSession) {
        //统计参数
        final String unitId = getLoginInfo().getUnitId();
        final Integer unitClass = getLoginInfo().getUnitClass();
        final String key = unitId + "_" + examId;
        final String typekey = unitId + "_" + examId + "_type";
        final String messKey = unitId + "_" + examId + "_mess";
        if (RedisUtils.get(key) == null) {
            RedisUtils.set(typekey, "校验");
            RedisUtils.set(key, "start");
            RedisUtils.set(messKey, "开始校验数据！");
        } else {
            JSONObject jsonObject = new JSONObject();
            ;
            jsonObject.put("type", RedisUtils.get(key));
            jsonObject.put("mess", RedisUtils.get(messKey));
            if ("success".equals(RedisUtils.get(key)) || "error".equals(RedisUtils.get(key))) {
                RedisUtils.del(typekey);
                RedisUtils.del(key);
                RedisUtils.del(messKey);
            }
//			RedisUtils.del(typekey);
//			RedisUtils.del(key);
//			RedisUtils.del(messKey);
            return JSON.toJSONString(jsonObject);
        }

        new Thread(new Runnable() {
            public void run() {
                RedisUtils.set(messKey, "科目统计参数数据校验中...");
                //统计参数
                List<EmSubjectInfo> subjectInfoList = emSubjectInfoService.findByExamId(examId);
                EmExamInfo exam = emExamInfoService.findOne(examId);
                //去除等第统计
                subjectInfoList = takeCanStat(subjectInfoList);
                Map<String, EmStatParm> map = emStatParmService.findMapByUnitId(unitId, examId);
                if (map == null || map.size() <= 0) {
                    RedisUtils.set(messKey, "科目统计参数数据校验失败：没有设置各科目统计参数！");
                    RedisUtils.set(key, "error");
                    return;
                }
                for (EmSubjectInfo info : subjectInfoList) {
                    if (!map.containsKey(info.getSubjectId())) {
                        RedisUtils.set(messKey, "科目统计参数数据校验失败：" + info.getCourseName() + "没有设置统计参数！");
                        RedisUtils.set(key, "error");
                        return;
                    }

                }
                if (!map.containsKey(BaseConstants.ZERO_GUID)) {
                    RedisUtils.set(messKey, "总分统计参数数据校验失败：没有设置统计参数！");
                    RedisUtils.set(key, "error");
                    return;
                }
                if (StringUtils.equals(exam.getIsgkExamType(), "1")) {
                    if (!map.containsKey(ExammanageConstants.CON_SUM_ID)) {
                        RedisUtils.set(messKey, "高考赋分总分统计参数数据校验失败：没有设置统计参数！");
                        RedisUtils.set(key, "error");
                        return;
                    }
                }
                //将参数控制
                Collection<EmStatParm> valueCollection = map.values();
                List<EmStatParm> valueList = new ArrayList<EmStatParm>(valueCollection);
                Set<String> ids = EntityUtils.getSet(valueList, EmStatParm::getId);
                //锁住统计参数
                try {
                    emStatParmService.updateStat(ids.toArray(new String[]{}), "1");
                } catch (Exception e) {
                    RedisUtils.set(messKey, "统计参数数据校验失败！");
                    RedisUtils.set(key, "error");
                    return;
                }
                //锁住考试下所有科目
                try {
                    emSubjectInfoService.updateIsLock(examId, "1");
                } catch (Exception e) {
                    RedisUtils.set(messKey, "统计参数数据校验失败！");
                    RedisUtils.set(key, "error");
                    return;
                }
                //参加考试的学生
                String mm = checkStuScore(unitId, examId, subjectInfoList, unitClass);
                if (StringUtils.isNotEmpty(mm)) {
                    RedisUtils.set(messKey, mm);
                    RedisUtils.set(key, "error");
                    //解锁统计参数
                    try {
                        emStatParmService.updateStat(ids.toArray(new String[]{}), "0");
                    } catch (Exception e) {
                        RedisUtils.set(messKey, "校验失败！");
                        RedisUtils.set(key, "error");
                        return;
                    }
                    //解锁考试下所有科目
                    try {
                        emSubjectInfoService.updateIsLock(examId, "0");
                    } catch (Exception e) {
                        RedisUtils.set(messKey, "统计参数数据校验失败！");
                        RedisUtils.set(key, "error");
                        return;
                    }
                    return;
                }
                RedisUtils.set(messKey, "校验成功");
                RedisUtils.set(key, "success");
                return;
            }
        }).start();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", RedisUtils.get(key));
        jsonObject.put("mess", RedisUtils.get(messKey));
        if ("success".equals(RedisUtils.get(key)) || "error".equals(RedisUtils.get(key))) {
            RedisUtils.del(typekey);
            RedisUtils.del(key);
            RedisUtils.del(messKey);
        }
        return JSON.toJSONString(jsonObject);
    }

    private String checkStuScore(String unitId, String examId, List<EmSubjectInfo> subjectInfoList, int unitClass) {
        EmExamInfo exam = emExamInfoService.findOne(examId);
        if (exam == null) {
            return "该考试已经不存在";
        }
        boolean isSingle = true;
        if (exam.getUnitId().equals(unitId) || unitClass != 2) {
            isSingle = false;
        }
        if (isSingle) {
            return checkBySchoolId(examId, unitId, true, subjectInfoList);
        } else {
            //统计所有
            List<EmClassInfo> classInfoList = emClassInfoService.findByExamIdAndSchoolId(examId, null);
            //包括所有单位
            if (CollectionUtils.isEmpty(classInfoList)) {
                return "该考试下没有维护考试班级信息";
            }
            Set<String> schoolIds = EntityUtils.getSet(classInfoList, EmClassInfo::getSchoolId);
            for (String school : schoolIds) {
                //循环每个单位
                if (unitId.equals(school)) {
                    String ff = checkBySchoolId(examId, school, true, subjectInfoList);
                    if (StringUtils.isNotBlank(ff)) {
                        return ff;
                    }
                } else {
                    String ff = checkBySchoolId(examId, school, false, subjectInfoList);
                    if (StringUtils.isNotBlank(ff)) {
                        return ff;
                    }
                }
            }

        }

        return null;

    }

    private String checkBySchoolId(String examId, String schoolId, boolean isSelf, List<EmSubjectInfo> subjectInfoList) {
        //统计部分
        Unit school = SUtils.dc(unitRemoteService.findOneById(schoolId), Unit.class);
        String perName = school == null ? "" : (school.getUnitName() + "：");
        if (isSelf) {
            perName = "";
        }
        //1:拿到所有学生成绩
        //2:参加总学生（班级下学生-不排考学生）
        //3:成绩是否符合
        List<EmScoreInfo> scoreList = emScoreInfoService.findByExamIdAndUnitId(examId, schoolId);
        if (CollectionUtils.isEmpty(scoreList)) {
            return perName + "没有学生的成绩数据。请先去维护学生数据";
        }
        //key:studentId_subjectId
        Map<String, Float> map = new HashMap<>();
        //不排考学生
        Map<String, String> filtMap = emFiltrationService.findByExamIdAndSchoolIdAndType(examId, schoolId, ExammanageConstants.FILTER_TYPE1);
        for (EmScoreInfo o : scoreList) {
            try {
                if (!ExammanageConstants.ACHI_GRADE.equals(o.getInputType())) {
                    map.put(o.getStudentId() + "_" + o.getSubjectId(), Float.parseFloat(o.getScore()));
                }
            } catch (Exception e) {
                return perName + "存在学生成绩数据录入值不是数值型";
            }
        }
        EmExamInfo exam = emExamInfoService.findOne(examId);
        Map<String, Integer> subjectNum = new HashMap<String, Integer>();
        Set<String> subIds1 = new HashSet<>();
        Set<String> subIds2 = new HashSet<>();
        Set<String> subIds0 = new HashSet<>();
        for (EmSubjectInfo ss : subjectInfoList) {
            if (StringUtils.equals(ss.getGkSubType(), "1")) {
                subIds1.add(ss.getSubjectId());
            } else if (StringUtils.equals(ss.getGkSubType(), "2")) {
                subIds2.add(ss.getSubjectId());
            } else {
                subIds0.add(ss.getSubjectId());
            }
        }
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findByUnitIdAndGradeCode(exam.getUnitId(), exam.getGradeCodes()), new TR<List<Grade>>() {
        });
        Grade grade = gradeList.get(0);
        if (CollectionUtils.isNotEmpty(subIds1)) {
            //选考科目
            Map<String, Set<String>> xuankaoStuIdMap = SUtils.dt(studentSelectSubjectRemoteService.getStuSelectByGradeId(exam.getAcadyear(), exam.getSemester(), grade.getId(), true, subIds1.toArray(new String[0])), new TR<Map<String, Set<String>>>() {
            });
            if (xuankaoStuIdMap == null)
                xuankaoStuIdMap = new HashMap<>();
            for (EmSubjectInfo ss : subjectInfoList) {
                if (!subIds1.contains(ss.getSubjectId()) || !xuankaoStuIdMap.containsKey(ss.getSubjectId())) {
                    continue;
                }
                Set<String> stuIds = xuankaoStuIdMap.get(ss.getSubjectId());
                stuIds.size();
                for (String stuId : stuIds) {
                    if (filtMap.containsKey(stuId)) {
                        continue;
                    }
                    if (!map.containsKey(stuId + "_" + ss.getSubjectId())) {
                        if (subjectNum.containsKey(ss.getCourseName() + "选考")) {
                            subjectNum.put(ss.getCourseName() + "选考", subjectNum.get(ss.getCourseName() + "选考") + 1);
                        } else {
                            subjectNum.put(ss.getCourseName() + "选考", 1);
                        }
                    } else {
                        Float score = map.get(stuId + "_" + ss.getSubjectId());
                        if (score > ss.getFullScore() || score < 0) {
                            return perName + ss.getCourseName() + "选考：存在学生成绩数据大于满分值或者小于0";
                        }
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(subIds1)) {
            //学考科目
            Map<String, Set<String>> xuekaoStuIdMap = SUtils.dt(studentSelectSubjectRemoteService.getStuSelectByGradeId(exam.getAcadyear(), exam.getSemester(), grade.getId(), false, subIds2.toArray(new String[0])), new TR<Map<String, Set<String>>>() {
            });
            if (xuekaoStuIdMap == null)
                xuekaoStuIdMap = new HashMap<>();
            for (EmSubjectInfo ss : subjectInfoList) {
                if (!subIds2.contains(ss.getSubjectId()) || !xuekaoStuIdMap.containsKey(ss.getSubjectId())) {
                    continue;
                }
                Set<String> stuIds = xuekaoStuIdMap.get(ss.getSubjectId());
                for (String stuId : stuIds) {
                    if (filtMap.containsKey(stuId)) {
                        continue;
                    }
                    if (!map.containsKey(stuId + "_" + ss.getSubjectId())) {
                        if (subjectNum.containsKey(ss.getCourseName() + "学考")) {
                            subjectNum.put(ss.getCourseName() + "学考", subjectNum.get(ss.getCourseName() + "学考") + 1);
                        } else {
                            subjectNum.put(ss.getCourseName() + "学考", 1);
                        }
                    } else {
                        Float score = map.get(stuId + "_" + ss.getSubjectId());
                        if (score > ss.getFullScore() || score < 0) {
                            return perName + ss.getCourseName() + "学考：存在学生成绩数据大于满分值或者小于0";
                        }
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(subIds0)) {
            //TODO 总学生
            List<EmClassInfo> classInfoList = emClassInfoService.findByExamIdAndSchoolId(examId, schoolId);
            if (CollectionUtils.isEmpty(classInfoList)) {
                return perName + "没有维护这场考试的班级";
            }
            //班级下所有学生
            Set<String> classIds = EntityUtils.getSet(classInfoList, EmClassInfo::getClassId);

            List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(classIds.toArray(new String[]{})), new TR<List<Student>>() {
            });
            if (CollectionUtils.isEmpty(studentList)) {
                return perName + "该考试下对应班级下没有找到学生";
            }
            for (Student s : studentList) {
                if (filtMap.containsKey(s.getId())) {
                    continue;
                }
                for (EmSubjectInfo ss : subjectInfoList) {
                    if (!subIds0.contains(ss.getSubjectId())) {
                        continue;
                    }
                    if (!map.containsKey(s.getId() + "_" + ss.getSubjectId())) {
                        if (subjectNum.containsKey(ss.getCourseName())) {
                            subjectNum.put(ss.getCourseName(), subjectNum.get(ss.getCourseName()) + 1);
                        } else {
                            subjectNum.put(ss.getCourseName(), 1);
                        }
                    } else {
                        Float score = map.get(s.getId() + "_" + ss.getSubjectId());
                        if (score > ss.getFullScore() || score < 0) {
                            return perName + ss.getCourseName() + "：存在学生成绩数据大于满分值或者小于0";
                        }
                    }
                }
            }
        }

        if (subjectNum.size() > 0) {
//			if(isSelf){
//				String dd="";
//				for(String key:subjectNum.keySet()){
//					dd=dd+";"+key+":"+subjectNum.get(key)+"人";
//				}
//				if(StringUtils.isNotBlank(dd)){
//					dd=dd.substring(1);
//					dd="存在学生成绩未完全录入："+dd;
//					return dd;
//				}
//			}else{
//				return perName+"存在学生成绩未录入";
//			}
        }
        return null;
    }


    @ResponseBody
    @RequestMapping("/scoreStat/analysisData")
    @ControllerInfo("统计")
    public String saveStat(final String examId, ModelMap map, HttpSession httpSession) {
        //统计参数
        final String unitId = getLoginInfo().getUnitId();
        final String key = unitId + "_" + examId;
        final String typeKey = unitId + "_" + examId + "_type";
        final String messKey = unitId + "_" + examId + "_mess";
        final Integer unitClass = getLoginInfo().getUnitClass();
        if (RedisUtils.get(key) == null) {
            RedisUtils.set(typeKey, "统计");
            RedisUtils.set(key, "start");
            RedisUtils.set(messKey, " 开始统计数据！");
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", RedisUtils.get(key));
            jsonObject.put("mess", RedisUtils.get(messKey));
            if ("success".equals(RedisUtils.get(key)) || "error".equals(RedisUtils.get(key))) {
                RedisUtils.del(typeKey);
                RedisUtils.del(key);
                RedisUtils.del(messKey);
            }
            return JSON.toJSONString(jsonObject);
        }

        new Thread(new Runnable() {
            public void run() {
                RedisUtils.set(messKey, "数据统计中...");
                String mess = "";
                try {
                    mess = emStatService.saveStatByExamId(examId, unitId, unitClass);
                } catch (Exception e) {
                    e.printStackTrace();
                    RedisUtils.set(key, "error");
                    RedisUtils.set(messKey, "统计数据失败！");

                    Map<String, EmStatParm> map = emStatParmService.findMapByUnitId(unitId, examId);
                    Collection<EmStatParm> valueCollection = map.values();
                    List<EmStatParm> valueList = new ArrayList<>(valueCollection);
                    Set<String> ids = EntityUtils.getSet(valueList, EmStatParm::getId);
                    emStatParmService.updateStat(ids.toArray(new String[]{}), "0");
                    //解锁考试下所有科目
                    emSubjectInfoService.updateIsLock(examId, "0");
                    return;
                }
                if (StringUtils.isNotBlank(mess)) {
                    RedisUtils.set(key, "error");
                    RedisUtils.set(messKey, mess);
                    return;
                }
                RedisUtils.set(key, "success");
                RedisUtils.set(messKey, "统计数据成功！");
                return;
            }
        }).start();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", RedisUtils.get(key));
        jsonObject.put("mess", RedisUtils.get(messKey));
        if ("success".equals(RedisUtils.get(key)) || "error".equals(RedisUtils.get(key))) {
            RedisUtils.del(typeKey);
            RedisUtils.del(key);
            RedisUtils.del(messKey);
        }
        return JSON.toJSONString(jsonObject);
    }


    @ResponseBody
    @RequestMapping("/scoreStat/removeStat")
    @ControllerInfo("删除统计结果")
    public String removeStat(String examId, ModelMap map, HttpSession httpSession) {
        EmStatObject obj = emStatObjectService.findByUnitIdExamId(getLoginInfo().getUnitId(), examId);
        if (obj == null) {
            return success("");
        }
        try {
            emStatService.deleteByStatObjectId(obj.getId());
            emSubjectInfoService.updateIsLock(examId, "0");
        } catch (Exception e) {
            e.printStackTrace();
            return error("删除统计结果失败");
        }
        return success("");
    }


    @RequestMapping("/scoreStat/showSuccess/page")
    @ControllerInfo("统计成功")
    public String showSuccess(String examId, ModelMap map, HttpSession httpSession) {
        map.put("examId", examId);
        return "/exammanage/scoreStat/statSuccessFtl.ftl";
    }

}
