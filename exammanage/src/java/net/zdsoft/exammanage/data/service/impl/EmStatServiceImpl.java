package net.zdsoft.exammanage.data.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.constant.ExammanageConstants;
import net.zdsoft.exammanage.data.dao.EmStatDao;
import net.zdsoft.exammanage.data.dto.EmScoreSearchDto;
import net.zdsoft.exammanage.data.entity.*;
import net.zdsoft.exammanage.data.service.*;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Service("emStatService")
public class EmStatServiceImpl extends BaseServiceImpl<EmStat, String> implements EmStatService {

    @Autowired
    private EmStatDao emStatDao;
    @Autowired
    private EmStatObjectService emStatObjectService;
    @Autowired
    private EmStatParmService emStatParmService;
    @Autowired
    private EmScoreInfoService emScoreInfoService;
    @Autowired
    private EmSubjectInfoService emSubjectInfoService;
    @Autowired
    private EmStatRangeService emStatRangeService;
    @Autowired
    private EmExamInfoService emExamInfoService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private EmStatSpaceService emStatSpaceService;
    @Autowired
    private EmStatLineService emStatLineService;
    @Autowired
    private CourseRemoteService courseRemoteService;

    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private TeachClassRemoteService teachClassRemoteService;
    @Autowired
    private EmExamNumService emExamNumService;
    @Autowired
    private CourseRemoteService CourseRemoteService;
    @Autowired
    private McodeRemoteService mcodeRemoteService;
    @Autowired
    private StudentSelectSubjectRemoteService studentSelectSubjectRemoteService;

    private static void StatLine(EmStatParm emStatParm, Map<String, List<EmStat>> statLineMap,
                                 List<EmStatLine> lineList, List<String> types) {
        // 统计上线人数
        if (StringUtils.equals(emStatParm.getNeedLineStat(), "0")) {
            return;
        }
        String[] lineArrs = emStatParm.getLineSpaces().split(",");
        if (lineArrs.length == 0) {
            return;
        }
        boolean needDouble = !BaseConstants.ZERO_GUID.equals(emStatParm.getSubjectId()) && !ExammanageConstants.CON_SUM_ID.equals(emStatParm.getSubjectId());
        Map<String, EmStat> emMap1 = new HashMap<>();
        Map<String, EmStat> emMap2 = new HashMap<>();
        String subId = emStatParm.getSubjectId();
        for (String type : types) {
            if (!statLineMap.containsKey(subId + "," + type)) {
                continue;
            }
            if (needDouble) {
                String aString = type;
                if (StringUtils.equals(type, "3")) {
                    aString = "1";
                }
                List<EmStat> l1 = statLineMap.get(BaseConstants.ZERO_GUID + "," + subId + aString);
                emMap1 = EntityUtils.getMap(l1, EmStat::getStudentId);
                List<EmStat> l2 = statLineMap.get(ExammanageConstants.CON_SUM_ID + "," + subId + aString);
                emMap2 = EntityUtils.getMap(l2, EmStat::getStudentId);
            }

            Map<String, String> clsMap = new HashMap<>();
            Map<String, Integer> map = new HashMap<>();
            Map<String, Integer> mapDouble1 = new HashMap<>();
            Map<String, Integer> mapDouble2 = new HashMap<>();
            List<EmStat> statlist = statLineMap.get(subId + "," + type);
            int stuCount = statlist.size();
            for (EmStat emStat : statlist) {
                clsMap.put(emStat.getClassId(), emStat.getClassName() + "," + emStat.getClassType());
                for (String line : lineArrs) {
                    if (!map.containsKey(emStat.getClassId() + "," + line)) {
                        map.put(emStat.getClassId() + "," + line, 0);
                    }
                    if (!mapDouble1.containsKey(emStat.getClassId() + "," + line)) {
                        mapDouble1.put(emStat.getClassId() + "," + line, 0);
                    }
                    if (!mapDouble2.containsKey(emStat.getClassId() + "," + line)) {
                        mapDouble2.put(emStat.getClassId() + "," + line, 0);
                    }
                    int lineInt = NumberUtils.toInt(line);
                    if (emStat.getGradeRank() <= lineInt) {
                        map.put(emStat.getClassId() + "," + line, map.get(emStat.getClassId() + "," + line) + 1);
                        if (needDouble) {
                            //单科情况下需要双上线统计：采用单科设置的线
                            if (emMap1.containsKey(emStat.getStudentId())) {
                                if (emMap1.get(emStat.getStudentId()).getGradeRank() <= lineInt) {
                                    mapDouble1.put(emStat.getClassId() + "," + line, mapDouble1.get(emStat.getClassId() + "," + line) + 1);
                                }
                            }
                            if (emMap2.containsKey(emStat.getStudentId())) {
                                if (emMap2.get(emStat.getStudentId()).getGradeRank() <= lineInt) {
                                    mapDouble2.put(emStat.getClassId() + "," + line, mapDouble2.get(emStat.getClassId() + "," + line) + 1);
                                }
                            }
                        }
                    }
                }
            }
            for (String line : lineArrs) {
                List<EmStatLine> list = new ArrayList<>();
                List<EmStatLine> list1 = new ArrayList<>();
                List<EmStatLine> list2 = new ArrayList<>();
                for (String clsId : clsMap.keySet()) {
                    String clsName = clsMap.get(clsId).split(",")[0];
                    String clsType = clsMap.get(clsId).split(",")[1];
                    EmStatLine e = toEmStatLine(emStatParm, clsId, clsType, clsName, type, line);
                    int num = map.get(clsId + "," + line);
                    e.setBlance((float) num * 100 / stuCount);
                    e.setScoreNum(num + "");
                    e.setIsDouble("0");
                    list.add(e);
                    if (needDouble) {
                        EmStatLine e1 = toEmStatLine(emStatParm, clsId, clsType, clsName, type, line);
                        int num1 = mapDouble1.get(clsId + "," + line);
                        e1.setBlance((float) num1 * 100 / stuCount);
                        e1.setScoreNum(num1 + "");
                        e1.setIsDouble("1");
                        e1.setDoubleType("1");
                        list1.add(e1);
                        EmStatLine e2 = toEmStatLine(emStatParm, clsId, clsType, clsName, type, line);
                        int num2 = mapDouble2.get(clsId + "," + line);
                        e2.setBlance((float) num2 * 100 / stuCount);
                        e2.setScoreNum(num2 + "");
                        e2.setIsDouble("1");
                        e2.setDoubleType("2");
                        list2.add(e2);
                    }
                }
                //	TODO
                sortLineList(list, lineList);
                if (needDouble) {
                    sortLineList(list1, lineList);
                    sortLineList(list2, lineList);
                }
            }
        }
    }

    private static void sortLineList(List<EmStatLine> list, List<EmStatLine> lineList) {
        //上线人数排序
        Collections.sort(list, new Comparator<EmStatLine>() {
            @Override
            public int compare(EmStatLine o1, EmStatLine o2) {
                int mm = NumberUtils.toInt(o2.getScoreNum()) - NumberUtils.toInt(o1.getScoreNum());
                if (mm > 0) {
                    return 1;
                } else if (mm < 0) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        int index = 1;// 排名
        int numpp = Integer.MAX_VALUE;
        for (int i = 0; i < list.size(); i++) {
            EmStatLine s = list.get(i);
            if (NumberUtils.toInt(s.getScoreNum()) < numpp) {
                numpp = NumberUtils.toInt(s.getScoreNum());
                index = i + 1;
            }
            s.setRank(index);
            lineList.add(s);
        }
    }

    private static EmStatLine toEmStatLine(EmStatParm parm, String clsId, String clsType, String clsName, String type, String line) {
        EmStatLine info = new EmStatLine();
        info.setStatObjectId(parm.getStatObjectId());
        info.setStatParmId(parm.getId());
        info.setExamId(parm.getExamId());
        info.setClassId(clsId);
        info.setClassType(clsType);
        info.setClassName(clsName);
        info.setType(type);
        info.setSubjectId(parm.getSubjectId());
        info.setLine(line);
        return info;
    }

    /**
     * 标准差
     **/
    private static float StandardDiviation(Float[] x) {
        int m = x.length;
        float sum = 0;
        for (int i = 0; i < m; i++) {//求和
            sum += x[i];
        }
        float dAve = sum / m;//求平均值
        float dVar = 0;
        for (int i = 0; i < m; i++) {//求方差
            dVar += (x[i] - dAve) * (x[i] - dAve);
        }
        return (float) Math.sqrt(dVar / m);
    }

    public static void main(String[] args) {
        Float aFloat = null;
        Float a = 0.0f;
        a += aFloat;
        System.out.println(a);
    }

    @Override
    protected BaseJpaRepositoryDao<EmStat, String> getJpaDao() {
        return emStatDao;
    }

    @Override
    protected Class<EmStat> getEntityClass() {
        return EmStat.class;
    }

    public String statByExamId(String examId, String unitId, Integer unitClass) throws Exception {
        EmExamInfo exam = emExamInfoService.findOne(examId);
        if (exam == null) {
            return "该考试已经不存在";
        }
        //是否单个学校统计
        boolean isSingle = true;
        if (exam.getUnitId().equals(unitId) || unitClass != 2) {
            isSingle = false;
        }
        //是否新高考考试
        boolean isGkType = false;
        if (StringUtils.equals(exam.getIsgkExamType(), "1")) {
            isGkType = true;
        }
        //科目统计条件
        Map<String, EmStatParm> parmMap = emStatParmService.findMapByUnitId(unitId, examId);
        //学生成绩
        List<EmScoreInfo> scoreList = new ArrayList<>();
        if (isSingle) {
            //单个
            scoreList = emScoreInfoService.findByExamIdAndUnitId(examId, unitId);
        } else {
            //所有
            scoreList = emScoreInfoService.findByExamIdAndUnitId(examId, null);
        }
        //非7选3科目
        List<Course> courseList73 = SUtils.dt(courseRemoteService.findByCodes73(unitId), new TR<List<Course>>() {
        });
        Set<String> subIds73 = EntityUtils.getSet(courseList73, Course::getId);
        //统计的数据 ，不包括总分 key:科目
        Map<String, List<EmScoreInfo>> scoreListBysubject = new HashMap<>();
        /** 用于总分的判断
         作弊学生（含一门也算）1
         缺考 （含一门也算）2
         0分（含一门也算）3
         不统分学生（含一门也算）4*/
        Map<String, Set<String>> noStuIdMap = new HashMap<>();
        noStuIdMap.put("1", new HashSet<>());
        noStuIdMap.put("2", new HashSet<>());
        noStuIdMap.put("3", new HashSet<>());
        noStuIdMap.put("4", new HashSet<>());
        Set<String> classIds = new HashSet<>();
        Set<String> stuIds = new HashSet<>();
        Map<String, String> stuClsMap = new HashMap<>();
        Map<String, Set<String>> lineStuMap = new HashMap<>();
        for (EmScoreInfo em : scoreList) {
            //剔除不是统计科目
            stuClsMap.put(em.getStudentId(), em.getClassId());
            if (!parmMap.containsKey(em.getSubjectId())) {
                continue;
            }
            if (!scoreListBysubject.containsKey(em.getSubjectId())) {
                scoreListBysubject.put(em.getSubjectId(), new ArrayList<EmScoreInfo>());
            }
            em.setShowScore(em.getScore() == null ? 0 : Float.parseFloat(em.getScore()));
            scoreListBysubject.get(em.getSubjectId()).add(em);
            if (EmScoreInfo.SCORE_TYPE_2.equals(em.getScoreStatus())) {
                noStuIdMap.get("1").add(em.getStudentId());//作弊
            } else if (EmScoreInfo.SCORE_TYPE_1.equals(em.getScoreStatus())) {
                noStuIdMap.get("2").add(em.getStudentId());//缺考
            } else {
                if (em.getShowScore() == null || em.getShowScore() == 0) {
                    noStuIdMap.get("3").add(em.getStudentId());
                }
            }
            if ("0".equals(em.getFacet())) {
                noStuIdMap.get("4").add(em.getStudentId());
            }
            classIds.add(em.getClassId());
            stuIds.add(em.getStudentId());
            if (!lineStuMap.containsKey(em.getSubjectId() + em.getGkSubType())) {
                lineStuMap.put(em.getSubjectId() + em.getGkSubType(), new HashSet<>());
            }
            lineStuMap.get(em.getSubjectId() + em.getGkSubType()).add(em.getStudentId());
        }

        List<Student> studentList = SUtils.dt(studentRemoteService.findByClassIds(classIds.toArray(new String[]{})), new TR<List<Student>>() {
        });
        Set<String> stuIds1 = EntityUtils.getSet(studentList, Student::getId);
        stuIds.removeAll(stuIds1);
        if (stuIds.size() > 0) {
            List<Student> studentList1 = SUtils.dt(studentRemoteService.findListByIds(stuIds.toArray(new String[]{})), new TR<List<Student>>() {
            });
            studentList.addAll(studentList1);
        }
        //学生来源
        Map<String, String> sourceByStuIdMap = new HashMap<>();
        for (Student s : studentList) {
            if (StringUtils.isBlank(s.getSource())) {
                //算正常
                sourceByStuIdMap.put(s.getId(), "1");
            } else {
                sourceByStuIdMap.put(s.getId(), s.getSource());
            }
        }

        //学生统计
        List<EmStat> insertStatList = new ArrayList<>();
        //整体统计
        List<EmStatRange> insertRangeList = new ArrayList<>();
        //间距统计
        List<EmStatSpace> insertStatSpace = new ArrayList<>();

        //上线统计
        List<EmStatLine> insertStatLine = new ArrayList<>();
        Map<String, List<EmStat>> statLineMap = new HashMap<>();
        List<EmSubjectInfo> subInfolist = emSubjectInfoService.findByExamId(examId);
        Map<String, EmSubjectInfo> subInfoMap = EntityUtils.getMap(subInfolist, EmSubjectInfo::getSubjectId);
        // 单科统计
        for (String key : parmMap.keySet()) {
            if (!scoreListBysubject.containsKey(key) || BaseConstants.ZERO_GUID.equals(key)
                    || ExammanageConstants.CON_SUM_ID.equals(key)) {
                continue;
            }
            //学生排名+班级统计+学校统计
            String subType = null;
            if (isGkType) {
                EmSubjectInfo info = subInfoMap.get(key);
                if (!subIds73.contains(key)) {
                    subType = "0";
                } else {
                    if (StringUtils.equals(info.getGkSubType(), "2")) {
                        subType = "2";//学考
                    } else if (StringUtils.equals(info.getGkSubType(), "1")) {
                        subType = "1";//选考
                    } else {
                        subType = "3";//选考+学考
                    }
                }
            } else {
                subType = "0";
            }
            statBySingleSub(parmMap.get(key), sourceByStuIdMap, noStuIdMap, scoreListBysubject.get(key),
                    unitId, insertStatList, insertRangeList, insertStatSpace, statLineMap, subType, true, null);
        }

        //总分
        EmStatParm zeroParm = parmMap.get(BaseConstants.ZERO_GUID);
        //语数英+赋分
        EmStatParm conParm = parmMap.get(ExammanageConstants.CON_SUM_ID);
        EmStatParm ysyParm = new EmStatParm();
        if (zeroParm != null) {
            ysyParm.setExamId(examId);
            ysyParm.setId(BaseConstants.ZERO_GUID);
            ysyParm.setIsCheat(zeroParm.getIsCheat());
            ysyParm.setIsMiss(zeroParm.getIsMiss());
            ysyParm.setIsZero(zeroParm.getIsZero());
            ysyParm.setSubjectId(ExammanageConstants.CON_YSY_ID);
            ysyParm.setStatObjectId(zeroParm.getStatObjectId());
        }
        Map<String, EmScoreInfo> allScoreMap = new HashMap<>();//总分
        Map<String, EmScoreInfo> allConScoreMap = new HashMap<>();//语数英+赋分
        Map<String, EmScoreInfo> allYSYScoreMap = new HashMap<>();//语数英
        boolean flag1 = false;
        boolean flag2 = false;
        DecimalFormat df = new DecimalFormat("#.0");
        for (EmStat stat : insertStatList) {
            if (StringUtils.equals(stat.getSubType(), ExammanageConstants.SUB_TYPE_3)) {
                //因为选考成绩会有2条统计结果（分数和赋分），需要过滤掉一条
                continue;
            }
            if (StringUtils.equals(parmMap.get(stat.getSubjectId()).getJoinSum(), "0")) {
                //不算入总分的科目成绩不计入总分统计
                continue;
            }
            if (conParm != null && !StringUtils.equals(stat.getSubType(), ExammanageConstants.SUB_TYPE_2)) {
                if ("0".equals(conParm.getIsCheat()) && noStuIdMap.get("1").contains(stat.getStudentId())) {
                    continue;
                }
                if ("0".equals(conParm.getIsMiss()) && noStuIdMap.get("2").contains(stat.getStudentId())) {
                    continue;
                }
                if ("0".equals(conParm.getIsZero()) && noStuIdMap.get("3").contains(stat.getStudentId())) {
                    continue;
                }
                if (!allConScoreMap.containsKey(stat.getStudentId())) {
                    EmScoreInfo e = toEmConScoreInfo(stat);
                    e.setClassId(stuClsMap.get(stat.getStudentId()));
                    allConScoreMap.put(stat.getStudentId(), e);
                } else {
                    if (StringUtils.equals(stat.getSubType(), ExammanageConstants.SUB_TYPE_0)) {
                        float score = allConScoreMap.get(stat.getStudentId()).getShowScore() + stat.getScore();
                        allConScoreMap.get(stat.getStudentId()).setShowScore(Float.parseFloat(df.format(score)));
                    } else {
                        float score = allConScoreMap.get(stat.getStudentId()).getShowScore() + stat.getConScore();
                        allConScoreMap.get(stat.getStudentId()).setShowScore(Float.parseFloat(df.format(score)));
                    }
                }
                if (!flag1) {
                    flag1 = true;
                }
            }
            if (zeroParm != null) {
                if ("0".equals(zeroParm.getIsCheat()) && noStuIdMap.get("1").contains(stat.getStudentId())) {
                    continue;
                }
                if ("0".equals(zeroParm.getIsMiss()) && noStuIdMap.get("2").contains(stat.getStudentId())) {
                    continue;
                }
                if ("0".equals(zeroParm.getIsZero()) && noStuIdMap.get("3").contains(stat.getStudentId())) {
                    continue;
                }
                if (!allScoreMap.containsKey(stat.getStudentId())) {
                    EmScoreInfo e = toEmScoreInfo(stat);
                    e.setClassId(stuClsMap.get(stat.getStudentId()));
                    allScoreMap.put(stat.getStudentId(), e);
                } else {
                    float score = allScoreMap.get(stat.getStudentId()).getShowScore() + stat.getScore();
                    allScoreMap.get(stat.getStudentId()).setShowScore(Float.parseFloat(df.format(score)));
                }
                if ("0".equals(stat.getSubType())) {
                    if (!allYSYScoreMap.containsKey(stat.getStudentId())) {
                        EmScoreInfo e = toEmScoreInfo(stat);
                        e.setClassId(stuClsMap.get(stat.getStudentId()));
                        e.setSubjectId(ExammanageConstants.CON_YSY_ID);
                        allYSYScoreMap.put(stat.getStudentId(), e);
                    } else {
                        float score = allYSYScoreMap.get(stat.getStudentId()).getShowScore() + stat.getScore();
                        allYSYScoreMap.get(stat.getStudentId()).setShowScore(Float.parseFloat(df.format(score)));
                    }
                }
                if (!flag2) {
                    flag2 = true;
                }
            }
        }
        List<EmStatRange> insertRangeList1 = new ArrayList<>();
        if (zeroParm != null && flag2) {
            List<EmScoreInfo> zongScoreList = new ArrayList<>(allScoreMap.values());
            statBySingleSub(zeroParm, null, noStuIdMap, zongScoreList, unitId, insertStatList, insertRangeList1, insertStatSpace, statLineMap, "0", false, lineStuMap);
            parmMap.put(zeroParm.getSubjectId(), zeroParm);
        }
        if (conParm != null && flag1) {
            List<EmScoreInfo> zongConScoreList = new ArrayList<>(allConScoreMap.values());
            statBySingleSub(conParm, null, noStuIdMap, zongConScoreList, unitId, insertStatList, insertRangeList1, insertStatSpace, statLineMap, "0", false, lineStuMap);
            parmMap.put(conParm.getSubjectId(), conParm);
        }
        if (ysyParm != null && flag2) {
            List<EmScoreInfo> zongConScoreList = new ArrayList<>(allYSYScoreMap.values());
            statBySingleSub(ysyParm, null, noStuIdMap, zongConScoreList, unitId, insertStatList, insertRangeList1, insertStatSpace, null, "0", false, null);
        }
        if (CollectionUtils.isNotEmpty(insertRangeList1)) {
            insertRangeList.addAll(insertRangeList1);
        }
        for (String key : parmMap.keySet()) {
            List<String> types = new ArrayList<>();
            if (!scoreListBysubject.containsKey(key) || BaseConstants.ZERO_GUID.equals(key)
                    || ExammanageConstants.CON_SUM_ID.equals(key)) {
                types.add("0");
            } else {
                if (isGkType) {
                    EmSubjectInfo info = subInfoMap.get(key);
                    if (!subIds73.contains(key)) {
                        types.add("0");
                    } else {
                        if (StringUtils.equals(info.getGkSubType(), "2")) {
                            //学考原始分
                            types.add("2");
                        } else if (StringUtils.equals(info.getGkSubType(), "1")) {
                            //选考原始分
                            types.add("3");
                        } else {
                            //选考+学考
                            types.add("2");
                            types.add("3");
                        }
                    }
                } else {
                    types.add("0");
                }
            }
            StatLine(parmMap.get(key), statLineMap, insertStatLine, types);
        }
//		List<String> types1 = new ArrayList<>();
//		types1.add("0");
//		if(zeroParm!=null && flag2) {
//			StatLine(zeroParm, statLineMap, insertStatLine, types1);
//		}
//		if(conParm!=null && flag1) {
//			StatLine(conParm, statLineMap, insertStatLine, types1);
//		}
        //先删除
        EmStatObject emStatObject = emStatObjectService.findByUnitIdExamId(unitId, examId);
        if (emStatObject != null) {
            deleteByStatObjectId(emStatObject.getId());
        }
        if (CollectionUtils.isNotEmpty(insertStatList)) {
            this.saveAllEntitys(insertStatList.toArray(new EmStat[]{}));
            saveAbiEmStatList(null, examId, emStatObject.getId(), insertStatList);
        }
        if (CollectionUtils.isNotEmpty(insertRangeList)) {
            emStatRangeService.saveAllEntitys(insertRangeList.toArray(new EmStatRange[]{}));
        }
        if (CollectionUtils.isNotEmpty(insertStatSpace)) {
            emStatSpaceService.saveAllEntitys(insertStatSpace.toArray(new EmStatSpace[]{}));
        }
        if (CollectionUtils.isNotEmpty(insertStatLine)) {
            emStatLineService.saveAllEntitys(insertStatLine.toArray(new EmStatLine[]{}));
        }

        emStatObjectService.updateIsStat("1", emStatObject.getId());
        if (isSingle) {
            //科目录入解锁
            emSubjectInfoService.updateIsLock(examId, "0");
        }
        return null;
    }

    /**
     * noStuIdsMap:本次考试，有过（作弊、缺考...）等情况的学生
     * 单科统计：
     * 行政班/教学班/年级：学生成绩排名（赋分排名）、（赋分）最高分、（赋分）最低分、（赋分）平均分、（赋分）标准差
     * 区间百分比
     *
     * @param lineStuMap
     * @param isGk
     **/
    private void statBySingleSub(EmStatParm parm, Map<String, String> sourceByStuIdMap, Map<String, Set<String>> noStuIdsMap,
                                 List<EmScoreInfo> infoList, String unitId, List<EmStat> insertStatListAll, List<EmStatRange> insertRangeListAll,
                                 List<EmStatSpace> insertStatSpaceAll, Map<String, List<EmStat>> lineStatMap, String subType, boolean isSingle, Map<String, Set<String>> lineStuMap) {
        //1:按成绩排序
        Collections.sort(infoList, new Comparator<EmScoreInfo>() {
            @Override
            public int compare(EmScoreInfo o1, EmScoreInfo o2) {
                float mm = o2.getShowScore() - o1.getShowScore();
                if (mm > 0) {
                    return 1;
                } else if (mm < 0) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        //key:unitId value:classIds_classType
        Map<String, Set<String>> classIdsByUnitId = new HashMap<>();
        //key:classId key:类型 value:stuId
        Map<String, Map<String, Set<String>>> noStuIdByclassIdMap = new HashMap<>();
        //各班级参加考试人数
        Map<String, Integer> stuNumByClassId = new HashMap<>();
        //最终统计的原始成绩数据
        List<EmScoreInfo> factList = new ArrayList<>();
        Map<String, List<EmScoreInfo>> lineListMap = new HashMap<>();

        //整体--所有
        Map<String, Set<String>> noStuIdMap = new HashMap<String, Set<String>>();
        noStuIdMap.put("1", new HashSet<String>());
        noStuIdMap.put("2", new HashSet<String>());
        noStuIdMap.put("3", new HashSet<String>());
        noStuIdMap.put("4", new HashSet<String>());

        boolean noisCheat = "0".equals(parm.getIsCheat());
        boolean noisMiss = "0".equals(parm.getIsMiss());
        boolean noisZero = "0".equals(parm.getIsZero());
        boolean noisStat = "0".equals(parm.getIsOnlystat());
        if (isSingle) {
            //单科
            for (EmScoreInfo em : infoList) {
                if (StringUtils.isBlank(em.getGkSubType())) {
                    continue;
                }
                if (!classIdsByUnitId.containsKey(em.getUnitId())) {
                    classIdsByUnitId.put(em.getUnitId(), new HashSet<String>());
                }
                if (StringUtils.isNotBlank(em.getTeachClassId())) {
                    classIdsByUnitId.get(em.getUnitId()).add(em.getTeachClassId() + "_2");
                }
                classIdsByUnitId.get(em.getUnitId()).add(em.getClassId() + "_1");

                if (!noStuIdByclassIdMap.containsKey(em.getClassId())) {
                    noStuIdByclassIdMap.put(em.getClassId(), new HashMap<String, Set<String>>());
                    noStuIdByclassIdMap.get(em.getClassId()).put("1", new HashSet<String>());
                    noStuIdByclassIdMap.get(em.getClassId()).put("2", new HashSet<String>());
                    noStuIdByclassIdMap.get(em.getClassId()).put("3", new HashSet<String>());
                    noStuIdByclassIdMap.get(em.getClassId()).put("4", new HashSet<String>());
                    stuNumByClassId.put(em.getClassId(), 0);
                }
                if (StringUtils.isNotBlank(em.getTeachClassId())
                        && !noStuIdByclassIdMap.containsKey(em.getTeachClassId())) {
                    stuNumByClassId.put(em.getTeachClassId(), 0);
                    noStuIdByclassIdMap.put(em.getTeachClassId(), new HashMap<String, Set<String>>());
                    noStuIdByclassIdMap.get(em.getTeachClassId()).put("1", new HashSet<String>());
                    noStuIdByclassIdMap.get(em.getTeachClassId()).put("2", new HashSet<String>());
                    noStuIdByclassIdMap.get(em.getTeachClassId()).put("3", new HashSet<String>());
                    noStuIdByclassIdMap.get(em.getTeachClassId()).put("4", new HashSet<String>());
                }
                stuNumByClassId.put(em.getClassId(), stuNumByClassId.get(em.getClassId()) + 1);
                if (StringUtils.isNotBlank(em.getTeachClassId())) {
                    stuNumByClassId.put(em.getTeachClassId(), stuNumByClassId.get(em.getTeachClassId()) + 1);
                }
                if (EmScoreInfo.SCORE_TYPE_2.equals(em.getScoreStatus())) {
                    noStuIdByclassIdMap.get(em.getClassId()).get("1").add(em.getStudentId());
                    if (StringUtils.isNotBlank(em.getTeachClassId())) {
                        noStuIdByclassIdMap.get(em.getTeachClassId()).get("1").add(em.getStudentId());
                    }
                    noStuIdMap.get("1").add(em.getStudentId());
                } else if (EmScoreInfo.SCORE_TYPE_1.equals(em.getScoreStatus())) {
                    noStuIdByclassIdMap.get(em.getClassId()).get("2").add(em.getStudentId());
                    if (StringUtils.isNotBlank(em.getTeachClassId())) {
                        noStuIdByclassIdMap.get(em.getTeachClassId()).get("2").add(em.getStudentId());
                    }
                    noStuIdMap.get("2").add(em.getStudentId());
                } else {
                    if (em.getShowScore() == null || em.getShowScore() == 0) {
                        noStuIdByclassIdMap.get(em.getClassId()).get("3").add(em.getStudentId());
                        if (StringUtils.isNotBlank(em.getTeachClassId())) {
                            noStuIdByclassIdMap.get(em.getTeachClassId()).get("3").add(em.getStudentId());
                        }
                        noStuIdMap.get("3").add(em.getStudentId());
                    }
                }
                if ("0".equals(em.getFacet())) {
                    noStuIdByclassIdMap.get(em.getClassId()).get("4").add(em.getStudentId());
                    if (StringUtils.isNotBlank(em.getTeachClassId())) {
                        noStuIdByclassIdMap.get(em.getTeachClassId()).get("4").add(em.getStudentId());
                    }
                    noStuIdMap.get("4").add(em.getStudentId());
                }

                //学生来源包括其中之一
                boolean f = false;
                if (parm.getSourseType().indexOf("1") >= 0 && "1".equals(sourceByStuIdMap.get(em.getStudentId()))) {
                    f = true;
                } else if (parm.getSourseType().indexOf("2") >= 0 && "2".equals(sourceByStuIdMap.get(em.getStudentId()))) {
                    f = true;
                } else if (parm.getSourseType().indexOf("3") >= 0 && "3".equals(sourceByStuIdMap.get(em.getStudentId()))) {
                    f = true;
                } else if (parm.getSourseType().indexOf("4") >= 0 && "4".equals(sourceByStuIdMap.get(em.getStudentId()))) {
                    f = true;
                } else if (parm.getSourseType().indexOf("9") >= 0 && "9".equals(sourceByStuIdMap.get(em.getStudentId()))) {
                    f = true;
                }
                if (!f) {
                    continue;
                }
                if (noisCheat && EmScoreInfo.SCORE_TYPE_2.equals(em.getScoreStatus())) {
                    continue;
                }
                if (noisMiss && EmScoreInfo.SCORE_TYPE_1.equals(em.getScoreStatus())) {
                    continue;
                }
                if (noisZero && (em.getShowScore() == null || em.getShowScore() == 0)) {
                    continue;
                }
                if (noisStat && "0".equals(em.getFacet())) {
                    continue;
                }
                factList.add(em);
            }
        } else {
            //总分
            for (EmScoreInfo em : infoList) {
                if (!classIdsByUnitId.containsKey(em.getUnitId())) {
                    classIdsByUnitId.put(em.getUnitId(), new HashSet<String>());
                }
                classIdsByUnitId.get(em.getUnitId()).add(em.getClassId());
                if (!noStuIdByclassIdMap.containsKey(em.getClassId())) {
                    noStuIdByclassIdMap.put(em.getClassId(), new HashMap<String, Set<String>>());
                    noStuIdByclassIdMap.get(em.getClassId()).put("1", new HashSet<String>());
                    noStuIdByclassIdMap.get(em.getClassId()).put("2", new HashSet<String>());
                    noStuIdByclassIdMap.get(em.getClassId()).put("3", new HashSet<String>());
                    noStuIdByclassIdMap.get(em.getClassId()).put("4", new HashSet<String>());
                    stuNumByClassId.put(em.getClassId(), 0);
                }
                stuNumByClassId.put(em.getClassId(), stuNumByClassId.get(em.getClassId()) + 1);
                if (noStuIdsMap.get("1").contains(em.getStudentId())) {
                    noStuIdByclassIdMap.get(em.getClassId()).get("1").add(em.getStudentId());
                    noStuIdMap.get("1").add(em.getStudentId());
                }
                if (noStuIdsMap.get("2").contains(em.getStudentId())) {
                    noStuIdByclassIdMap.get(em.getClassId()).get("2").add(em.getStudentId());
                    noStuIdMap.get("2").add(em.getStudentId());
                } else {
                    if (noStuIdsMap.get("3").contains(em.getStudentId())) {
                        noStuIdByclassIdMap.get(em.getClassId()).get("3").add(em.getStudentId());
                        noStuIdMap.get("3").add(em.getStudentId());
                    }
                }
                if (noStuIdsMap.get("4").contains(em.getStudentId())) {
                    noStuIdByclassIdMap.get(em.getClassId()).get("4").add(em.getStudentId());
                    noStuIdMap.get("4").add(em.getStudentId());
                }
                factList.add(em);
                if (lineStuMap != null) {
                    for (String key : lineStuMap.keySet()) {
//						String subId = key.substring(0, key.length()-1);
                        if (lineStuMap.get(key).contains(em.getStudentId())) {
                            if (!lineListMap.containsKey(key)) {
                                lineListMap.put(key, new ArrayList<>());
                            }
                            lineListMap.get(key).add(em);
                        }
                    }

                }
            }
        }

        //单个学生---排名  如果是学校 那么eduRank 还是学校
        List<EmStat> insertStatList = makeStudentRank(parm, factList, subType);
        if (!isSingle && CollectionUtils.isNotEmpty(lineListMap.keySet())) {
            for (String key : lineListMap.keySet()) {
                List<EmStat> list = makeStudentRank(parm, lineListMap.get(key), "0");
                for (EmStat s : list) {
                    if (lineStatMap != null) {
                        if (!lineStatMap.containsKey(s.getSubjectId() + "," + key)) {
                            lineStatMap.put(s.getSubjectId() + "," + key, new ArrayList<>());
                        }
                        lineStatMap.get(s.getSubjectId() + "," + key).add(s);
                    }
                }
            }
        }
        //各班级参与统计学生成绩
        Set<String> classIds = new HashSet<>();
        Set<String> teaClassIds = new HashSet<>();
        Map<String, Map<String, List<EmStat>>> statMap = new HashMap<>();
        Map<String, Set<String>> teaMap = new HashMap<>();
        for (EmStat s : insertStatList) {
            if (!isSingle) {
                s.setSubType("0");
            }
            // 2019/1/9 add nizq 新增加统计每个班的单科和总分上线统计情况
            if (lineStatMap != null) {
                if (!lineStatMap.containsKey(s.getSubjectId() + "," + s.getSubType())) {
                    lineStatMap.put(s.getSubjectId() + "," + s.getSubType(), new ArrayList<>());
                }
                lineStatMap.get(s.getSubjectId() + "," + s.getSubType()).add(s);
            }
            if (StringUtils.equals(s.getClassType(), "1")) {
                classIds.add(s.getClassId());
            } else {
                teaClassIds.add(s.getClassId());
            }
            if (!statMap.containsKey(s.getClassId())) {
                Map<String, List<EmStat>> map = new HashMap<>();
                map.put(s.getSubType(), new ArrayList<>());
                statMap.put(s.getClassId(), map);
            } else {
                if (!statMap.get(s.getClassId()).containsKey(s.getSubType())) {
                    statMap.get(s.getClassId()).put(s.getSubType(), new ArrayList<>());
                }
            }
            statMap.get(s.getClassId()).get(s.getSubType()).add(s);
        }
        if (!StringUtils.equals(parm.getSubjectId(), ExammanageConstants.CON_SUM_ID)
                && !StringUtils.equals(parm.getSubjectId(), ExammanageConstants.CON_YSY_ID)
                && !StringUtils.equals(parm.getSubjectId(), ExammanageConstants.ZERO32)) {
            teaMap = emSubjectInfoService.findTeacher(parm.getExamId(), parm.getSubjectId(), unitId, classIds, teaClassIds);
        }
        String[] types = null;
        if ("0".equals(subType)) {
            types = new String[]{"0"};
        } else if ("1".equals(subType)) {
            types = new String[]{"1"};//选考
        } else if ("2".equals(subType)) {
            types = new String[]{"2"};
        } else {
            types = new String[]{"1", "2"};
        }
        //整体的最高分、最低分、平均分、标准差
        List<EmStatRange> insertRangeList = new ArrayList<>();

        Map<String, List<EmStatRange>> rangeMap = new HashMap<>();
        Map<String, Map<String, Integer>> numMap = new HashMap<>();//学校
        for (String type : types) {
            List<EmStatRange> schoolRangeList = new ArrayList<>();//学校
            for (String unitIdKey : classIdsByUnitId.keySet()) {
                //每个年级
                Set<String> set = classIdsByUnitId.get(unitIdKey);
                List<EmStatRange> classRangeList = new ArrayList<>();//班级
                List<EmStat> schoolStatList = new ArrayList<>();//一个学校统计结果
                int aaa = 0;
                for (String classIdType : set) {
                    String classId = classIdType.split("_")[0];
                    if (!statMap.containsKey(classId)) {
                        continue;
                    }
                    //每个班级
                    Map<String, List<EmStat>> map = statMap.get(classId);
                    if (map == null || !map.containsKey(type)) {
                        continue;
                    }
                    List<EmStat> statlist = map.get(type);
                    aaa = aaa + statlist.size();
                }
                for (String classIdType : set) {
                    String classId = classIdType.split("_")[0];
                    if (!statMap.containsKey(classId)) {
                        continue;
                    }
                    //每个班级
                    Map<String, List<EmStat>> map = statMap.get(classId);
                    if (map == null || !map.containsKey(type)) {
                        continue;
                    }
                    List<EmStat> statlist = map.get(type);
                    EmStatRange statRange = makeStatRange(parm, classId, EmStatRange.RANGE_CLASS);
                    statRange.setSubType(type);
                    statRange.setJoinNum(stuNumByClassId.get(classId));
                    statRange.setStatNum(statlist.size());
                    Map<String, Set<String>> noStuMap = noStuIdByclassIdMap.get(classId);
                    statRange.setCheatNum(noStuMap.get("1").size());
                    statRange.setMissNum(noStuMap.get("2").size());
                    if (teaMap.containsKey(classId)) {
                        Set<String> teaIds = teaMap.get(classId);
                        String id = "";
                        for (String e : teaIds) {
                            id = StringUtils.isBlank(id) ? e : id + "," + e;
                        }
                        statRange.setTeacherIds(id);
                    }
                    makeRangeSpace(statRange, statlist, parm, insertStatSpaceAll, aaa);
                    if (!rangeMap.containsKey(type)) {
                        rangeMap.put(type, new ArrayList<>());
                    }
                    rangeMap.get(type).add(statRange);

                    if (!numMap.containsKey(type)) {
                        Map<String, Integer> map1 = new HashMap<>();
                        map1.put("statNum", 0);
                        map1.put("joinNum", 0);
                        map1.put("cheatNum", 0);
                        map1.put("missNum", 0);
                        numMap.put(type, map1);
                    }
                    numMap.get(type).put("statNum", numMap.get(type).get("statNum") + statRange.getStatNum());
                    numMap.get(type).put("joinNum", numMap.get(type).get("joinNum") + statRange.getJoinNum());
                    numMap.get(type).put("cheatNum", numMap.get(type).get("cheatNum") + statRange.getCheatNum());
                    numMap.get(type).put("missNum", numMap.get(type).get("missNum") + statRange.getMissNum());


                    classRangeList.add(statRange);
                    schoolStatList.addAll(statlist);
                }
                if (CollectionUtils.isEmpty(classRangeList)) {
                    continue;
                }
                //计算平均分排名
                makeRangeRank(classRangeList);
                if (type.equals("1")) {
                    makeRangeConRank(classRangeList);
                }
                //班级
                insertRangeList.addAll(classRangeList);
                //学校
                EmStatRange statRange = makeStatRange(parm, unitIdKey, EmStatRange.RANGE_SCHOOL);
                statRange.setSubType(type);
                statRange.setJoinNum(numMap.get(type).get("joinNum"));
                statRange.setStatNum(numMap.get(type).get("statNum"));
                statRange.setCheatNum(numMap.get(type).get("cheatNum"));
                statRange.setMissNum(numMap.get(type).get("missNum"));
                makeRangeSpace(statRange, schoolStatList, parm, insertStatSpaceAll, 0);
                schoolRangeList.add(statRange);
            }
            makeRangeRank(schoolRangeList);
            if (type.equals("1")) {
                makeRangeConRank(schoolRangeList);
            }
            insertRangeList.addAll(schoolRangeList);
            //整体
            EmStatRange statRange = makeStatRange(parm, unitId, EmStatRange.RANGE_EDU);
            statRange.setJoinNum(infoList.size());
            statRange.setStatNum(insertStatList.size());
            statRange.setCheatNum(noStuIdMap.get("1").size());
            statRange.setMissNum(noStuIdMap.get("2").size());
            statRange.setSubType(type);
            makeRangeSpace(statRange, insertStatList, parm, insertStatSpaceAll, 0);
            statRange.setRank(1);
            if (type.equals("1")) {
                statRange.setConAvgRank(1);
            }
            insertRangeList.add(statRange);
        }
        insertRangeListAll.addAll(insertRangeList);
        insertStatListAll.addAll(insertStatList);
    }

    @Override
    public String saveStatByExamId(String examId, String unitId,
                                   Integer unitClass) throws Exception {
        statByExamId(examId, unitId, unitClass);
        return null;
    }

    /**
     * 组装总分成绩
     *
     * @param stat 根据结果
     * @return
     */
    private EmScoreInfo toEmScoreInfo(EmStat stat) {
        EmScoreInfo info = new EmScoreInfo();
        info.setStudentId(stat.getStudentId());
        info.setClassId(stat.getClassId());
        info.setShowScore(stat.getScore());
        info.setUnitId(stat.getSchoolId());
        info.setExamId(stat.getExamId());
        info.setSubjectId(BaseConstants.ZERO_GUID);
        return info;
    }

    /**
     * 组装语数英+赋分成绩
     *
     * @param stat 根据结果
     * @return
     */
    private EmScoreInfo toEmConScoreInfo(EmStat stat) {
        EmScoreInfo info = new EmScoreInfo();
        info.setStudentId(stat.getStudentId());
        info.setClassId(stat.getClassId());
        if (StringUtils.equals(stat.getSubType(), ExammanageConstants.SUB_TYPE_0)) {
            info.setShowScore(stat.getScore());
        } else if (StringUtils.equals(stat.getSubType(), ExammanageConstants.SUB_TYPE_1)) {
            info.setShowScore(stat.getConScore());
        }
        info.setUnitId(stat.getSchoolId());
        info.setExamId(stat.getExamId());
        info.setSubjectId(ExammanageConstants.CON_SUM_ID);
        return info;
    }

    /**
     * 按平均分排名
     *
     * @param insertRangeList
     */
    private void makeRangeRank(List<EmStatRange> insertRangeList) {
        //1:按成绩排序
        Collections.sort(insertRangeList, new Comparator<EmStatRange>() {

            @Override
            public int compare(EmStatRange o1, EmStatRange o2) {
                float mm = o2.getAvgScore() - o1.getAvgScore();
                if (mm > 0) {
                    return 1;
                } else if (mm < 0) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        float before = 0;
        int classNum = 0;
        int rank = 0;
        for (EmStatRange s : insertRangeList) {
            if (rank == 0) {
                before = s.getAvgScore();
                rank = 1;
                classNum = 1;
            } else {
                if (before > s.getAvgScore()) {
                    rank = classNum + 1;
                    before = s.getAvgScore();
                }
                classNum++;
            }
            s.setRank(rank);
        }
    }

    /**
     * 按赋分平均分排名
     *
     * @param insertRangeList
     */
    private void makeRangeConRank(List<EmStatRange> insertRangeList) {
        //1:按成绩排序
        Collections.sort(insertRangeList, new Comparator<EmStatRange>() {

            @Override
            public int compare(EmStatRange o1, EmStatRange o2) {
                float mm = o2.getConAvgScore() - o1.getConAvgScore();
                if (mm > 0) {
                    return 1;
                } else if (mm < 0) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        float before = 0;
        int classNum = 0;
        int rank = 0;
        for (EmStatRange s : insertRangeList) {
            if (rank == 0) {
                before = s.getConAvgScore();
                rank = 1;
                classNum = 1;
            } else {
                if (before > s.getConAvgScore()) {
                    rank = classNum + 1;
                    before = s.getConAvgScore();
                }
                classNum++;
            }
            s.setConAvgRank(rank);
        }
    }

    /**
     * 整体统计 分数段 百分比 最大值 最小值，如果是整体是赋分（subType=1），则需要对赋分等级也要做出统计
     *
     * @param statRange
     * @param classScoreList
     * @param parm
     * @param aaa
     */
    private void makeRangeSpace(EmStatRange statRange, List<EmStat> classScoreList, EmStatParm parm, List<EmStatSpace> insertStatSpaceList, int aaa) {
        String subType = statRange.getSubType();
        List<EmSpaceItem> emSpaceItemList1 = parm.getEmSpaceItemList1();
        List<EmSpaceItem> emSpaceItemList2 = parm.getEmSpaceItemList2();
        List<EmSpaceItem> emSpaceItemList3 = parm.getEmSpaceItemList3();
        List<EmSpaceItem> emSpaceItemList9 = parm.getEmSpaceItemList9();
        EmStatSpace space = null;
        if (CollectionUtils.isEmpty(classScoreList)) {
            statRange.setMaxScore(0f);
            statRange.setMinScore(0f);
            statRange.setAvgScore(0f);
            if (CollectionUtils.isNotEmpty(emSpaceItemList1)) {
                insertStatSpaceList.addAll(makeZeroEmStatSpace(statRange.getId(), statRange.getStatObjectId(), emSpaceItemList1));
            }
            if (CollectionUtils.isNotEmpty(emSpaceItemList2)) {
                insertStatSpaceList.addAll(makeZeroEmStatSpace(statRange.getId(), statRange.getStatObjectId(), emSpaceItemList2));
            }
            if (CollectionUtils.isNotEmpty(emSpaceItemList3)) {
                insertStatSpaceList.addAll(makeZeroEmStatSpace(statRange.getId(), statRange.getStatObjectId(), emSpaceItemList3));
            }
            if (CollectionUtils.isNotEmpty(emSpaceItemList9)) {
                insertStatSpaceList.addAll(makeZeroEmStatSpace(statRange.getId(), statRange.getStatObjectId(), emSpaceItemList9));
            }
            return;
        }
        Collections.sort(classScoreList, new Comparator<EmStat>() {
            @Override
            public int compare(EmStat o1, EmStat o2) {
                float mm = o2.getScore() - o1.getScore();
                if (mm > 0) {
                    return 1;
                } else if (mm < 0) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        statRange.setMaxScore(classScoreList.get(0).getScore());
        statRange.setMinScore(classScoreList.get(classScoreList.size() - 1).getScore());
        if ("1".equals(subType)) {
            Collections.sort(classScoreList, new Comparator<EmStat>() {
                @Override
                public int compare(EmStat o1, EmStat o2) {
                    float mm = o2.getConScore() - o1.getConScore();
                    if (mm > 0) {
                        return 1;
                    } else if (mm < 0) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });
            statRange.setConScoreUp(classScoreList.get(0).getConScore());
            statRange.setConScoreLow(classScoreList.get(classScoreList.size() - 1).getConScore());
        }
        float allScore = 0;
        float allConScore = 0;
        float allScoreT = 0f;
        Float[] conScoreArr = new Float[classScoreList.size()];
        Float[] scoreArr = new Float[classScoreList.size()];
        int goodNum = 0;
        int fieldNum = 0;
        for (int i = 0; i < classScoreList.size(); i++) {
            EmStat t = classScoreList.get(i);
            if ("1".equals(subType)) {
                allConScore = allScore + t.getConScore();
                conScoreArr[i] = t.getConScore();
            }
            allScore = allScore + t.getScore();
            scoreArr[i] = t.getScore();
            allScoreT = allScoreT + t.getScoreT();
            if (StringUtils.equals(parm.getNeedGoodLine(), "1")) {
                if (t.getScore() >= parm.getGoodLine()) {
                    goodNum++;
                }
                if (t.getScore() < parm.getFailedLine()) {
                    fieldNum++;
                }
            }
        }
        DecimalFormat df = new DecimalFormat("#.00");
        float avgScore = allScore / statRange.getStatNum();
        avgScore = Float.parseFloat(df.format(avgScore));
        statRange.setAvgScore(avgScore);
        statRange.setNormDeviation(Float.parseFloat(df.format(StandardDiviation(scoreArr))));
        statRange.setSumScore(allScore);
        statRange.setAvgScoreT(allScoreT / statRange.getStatNum());
        statRange.setGoodNumber(goodNum);
        statRange.setFailedNumber(fieldNum);
        if (aaa == 0) {
            aaa = classScoreList.size();
        }
        if ("1".equals(subType)) {
            statRange.setConSumScore(allConScore);
            float avgConScore = allConScore / statRange.getStatNum();
            avgConScore = Float.parseFloat(df.format(avgConScore));
            statRange.setConAvgScore(avgConScore);
            statRange.setConNormDeviation(Float.parseFloat(df.format(StandardDiviation(conScoreArr))));
            Map<String, List<EmStat>> scoreRankMap = new HashMap<>();
            for (EmStat t : classScoreList) {
                if (!scoreRankMap.containsKey(t.getConScoreRank())) {
                    scoreRankMap.put(t.getConScoreRank(), new ArrayList<>());
                }
                scoreRankMap.get(t.getConScoreRank()).add(t);
            }
//			int b = classScoreList.size();
            for (String scoreRank : scoreRankMap.keySet()) {
                List<EmStat> list = scoreRankMap.get(scoreRank);
                space = makeEmStatSpace(statRange.getId(), BaseConstants.ZERO_GUID, statRange.getStatObjectId(), list.size() + "");
                int a = list.size();
                float b1 = (float) (Math.round((float) a / aaa * 100 * 100)) / 100;
                space.setBlance(b1);
                space.setIsCon("1");
                space.setScoreRank(NumberUtils.toInt(scoreRank));
                insertStatSpaceList.add(space);
            }
        }
//		int count = classScoreList.size();
        //TODO 需要名次段统计
        if (StringUtils.equals(parm.getNeedRankStat(), "1")) {
            if (CollectionUtils.isNotEmpty(emSpaceItemList9)) {
                for (EmSpaceItem item : emSpaceItemList9) {
                    int i = 0;
                    for (EmStat t : classScoreList) {
                        //当前判断方式会让边界学生重复计算100
                        if (item.getLowScore() < t.getEduRank()) {
                            break;
                        } else {
                            if (item.getUpScore() > t.getEduRank()) {//50
                                continue;
                            }
                            i++;
                        }
                    }
                    //人数
                    space = makeEmStatSpace(statRange.getId(), item.getId(), statRange.getStatObjectId(), i + "");
                    float b1 = (float) (Math.round((float) i / aaa * 100 * 100)) / 100;
                    space.setBlance(b1);
                    insertStatSpaceList.add(space);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(emSpaceItemList1)) {
            for (EmSpaceItem item : emSpaceItemList1) {
                int i = 0;
                for (EmStat t : classScoreList) {
                    //当前判断方式会让边界学生重复计算
                    if (item.getUpScore() < t.getScore()) {
                        //比分数段最大值还大
                        continue;
                    } else {
                        if (item.getLowScore() > t.getScore()) {
                            //比分数段最小值还小
                            break;
                        }
                        i++;
                    }
                }
                //人数
                space = makeEmStatSpace(statRange.getId(), item.getId(), statRange.getStatObjectId(), i + "");
                float b1 = (float) (Math.round((float) i / aaa * 100 * 100)) / 100;
                space.setBlance(b1);
                insertStatSpaceList.add(space);
                //rs=rs+","+i;
            }
            //statRange.setSpaceNum(rs.substring(1));
        }
        if (CollectionUtils.isNotEmpty(emSpaceItemList2)) {
            for (EmSpaceItem item : emSpaceItemList2) {
                Float lowNum = (statRange.getStatNum() == null ? 0 : statRange.getStatNum()) * (1.0f) * item.getLowScore() / (100.0f);
                int lowNumInt = Math.round(lowNum);
                Float upNum = (statRange.getStatNum() == null ? 0 : statRange.getStatNum()) * (1.0f) * item.getUpScore() / (100.0f);
                int upNumInt = Math.round(upNum);
                if (lowNumInt == upNumInt) {
                    space = makeEmStatSpace(statRange.getId(), item.getId(), statRange.getStatObjectId(), 0 + "");
                    insertStatSpaceList.add(space);
                    continue;
                }
                List<EmStat> list = classScoreList.subList(lowNumInt, upNumInt);
                if (CollectionUtils.isNotEmpty(list)) {
                    float avg = 0;
                    for (EmStat ss : list) {
                        avg = avg + ss.getScore();
                    }
                    avg = avg * (1.0f) / list.size();
                    avg = Float.parseFloat(df.format(avg));
                    space = makeEmStatSpace(statRange.getId(), item.getId(), statRange.getStatObjectId(), avg + "");
                    insertStatSpaceList.add(space);
                } else {
                    space = makeEmStatSpace(statRange.getId(), item.getId(), statRange.getStatObjectId(), "0");
                    insertStatSpaceList.add(space);
                }
            }
            //statRange.setFrontAvgscore(rs.substring(1));
        }
        if (CollectionUtils.isNotEmpty(emSpaceItemList3)) {
            for (EmSpaceItem item : emSpaceItemList3) {
                Float upNum = (statRange.getStatNum() == null ? 0 : statRange.getStatNum()) * (1.0f) * (100f - item.getLowScore()) / (100.0f);
                int upNumInt = Math.round(upNum);
                Float lowNum = (statRange.getStatNum() == null ? 0 : statRange.getStatNum()) * (1.0f) * (100f - item.getUpScore()) / (100.0f);
                int lowNumInt = Math.round(lowNum);
                if (lowNumInt == upNumInt) {
                    space = makeEmStatSpace(statRange.getId(), item.getId(), statRange.getStatObjectId(), 0 + "");
                    insertStatSpaceList.add(space);
                    continue;
                }
                List<EmStat> list = classScoreList.subList(lowNumInt, upNumInt);
                if (CollectionUtils.isNotEmpty(list)) {
                    float avg = 0;
                    for (EmStat ss : list) {
                        avg = avg + ss.getScore();
                    }
                    avg = avg * (1.0f) / list.size();
                    avg = Float.parseFloat(df.format(avg));
                    //rs=rs+","+avg;
                    space = makeEmStatSpace(statRange.getId(), item.getId(), statRange.getStatObjectId(), avg + "");
                    insertStatSpaceList.add(space);
                } else {
                    //rs=rs+",0";
                    space = makeEmStatSpace(statRange.getId(), item.getId(), statRange.getStatObjectId(), "0");
                    insertStatSpaceList.add(space);
                }
            }
            //statRange.setBackAvgscore(rs.substring(1));
        }
    }

    /**
     * 初始化单个整体
     *
     * @param parm
     * @param rangeId
     * @param rangeType
     * @return
     */
    private EmStatRange makeStatRange(EmStatParm parm, String rangeId, String rangeType) {
        EmStatRange statRange = new EmStatRange();
        statRange.setId(UuidUtils.generateUuid());
        statRange.setRangeId(rangeId);
        statRange.setRangeType(rangeType);
        statRange.setExamId(parm.getExamId());
        statRange.setSubjectId(parm.getSubjectId());
        statRange.setStatObjectId(parm.getStatObjectId());
        return statRange;
    }

    /**
     * 学生排名
     *
     * @param parm
     * @param factList
     * @param isXK
     * @return List<EmStat> 学生统计信息:个人排名只统计该科目成绩在行政班（教学班）中的排名，学校（年级）排名，教育局排名,标准分Z,标准分T,综合能力分（排名）
     */
    public List<EmStat> makeStudentRank(EmStatParm parm, List<EmScoreInfo> factList, String subType) {
        EmExamInfo exam = emExamInfoService.findOne(parm.getExamId());
        String gradeCode = exam.getGradeCodes();
        String section = gradeCode.substring(0, 1);
        String thisId = gradeCode.substring(1, 2);
        McodeDetail mc = SUtils.dt(mcodeRemoteService.findByMcodeAndThisId("DM-RKXD-" + section, thisId), new TypeReference<McodeDetail>() {
        });
        String gradeName = "";
        if (mc != null) {
            gradeName = mc.getMcodeContent();
        }
        Set<String> clsIds = EntityUtils.getSet(factList, EmScoreInfo::getClassId);
        Set<String> teaClsIds = EntityUtils.getSet(factList, EmScoreInfo::getTeachClassId);
        Map<String, String> clsNameMap = new HashMap<>();
        Map<String, String> teaClsNameMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(clsIds)) {
            clsNameMap = EntityUtils.getMap(SUtils.dt(classRemoteService.findListByIds(clsIds.toArray(new String[0])), new TR<List<Clazz>>() {
            }), Clazz::getId, Clazz::getClassName);
        }
        if (CollectionUtils.isNotEmpty(teaClsIds)) {
            teaClsNameMap = EntityUtils.getMap(SUtils.dt(teachClassRemoteService.findListByIds(teaClsIds.toArray(new String[0])), new TR<List<TeachClass>>() {
            }), TeachClass::getId, TeachClass::getName);
        }
        List<EmStat> insertStatList = new ArrayList<>();
        String[] types = null;
        if ("0".equals(subType)) {
            types = new String[]{"0"};
        } else if ("1".equals(subType)) {
            types = new String[]{"1", "3"};//选考：1赋分，3选考成绩
        } else if ("2".equals(subType)) {
            types = new String[]{"2"};//学考成绩
        } else {
            types = new String[]{"1", "2", "3"};
        }
        float allScore0 = 0;
        List<Float> scoreArr0 = new ArrayList<>();
        float allScore1 = 0;
        List<Float> scoreArr1 = new ArrayList<>();
        /**
         float[] scoreArr = new float[factList.size()];
         for(int i=0;i<factList.size();i++) {
         EmScoreInfo t = factList.get(i);
         allScore=allScore+t.getShowScore();
         scoreArr[i] = t.getShowScore();
         }
         **/

        for (String type : types) {
            if (StringUtils.equals(type, "3")) {
                continue;
            }
            for (int i = 0; i < factList.size(); i++) {
                EmScoreInfo t = factList.get(i);
                if (!StringUtils.equals("1", type)) {
                    //分数
                    if (StringUtils.equals("0", type)) {
                        if (StringUtils.isNotBlank(t.getGkSubType())
                                && !StringUtils.equals(t.getGkSubType(), ExammanageConstants.SUB_TYPE_0)) {
                            continue;
                        }
                    } else if (StringUtils.equals("2", type)) {
                        if (!StringUtils.equals(t.getGkSubType(), ExammanageConstants.SUB_TYPE_2)) {
                            continue;
                        }
                    }
                    allScore0 = allScore0 + t.getShowScore();
                    scoreArr0.add(t.getShowScore());
                } else {
                    //赋分
                    if (!StringUtils.equals(t.getGkSubType(), ExammanageConstants.SUB_TYPE_1)) {
                        continue;
                    }
                    allScore1 = allScore1 + t.getShowScore();
                    scoreArr1.add(t.getShowScore());
                }
            }
        }
        DecimalFormat df = new DecimalFormat("#.00");
        float avgScore0 = 0;
        float divScore0 = 0;
        float avgScore1 = 0;
        float divScore1 = 0;
        if (scoreArr0.size() > 0) {
            avgScore0 = allScore0 / scoreArr0.size();
            avgScore0 = Float.parseFloat(df.format(avgScore0));
            divScore0 = Float.parseFloat(df.format(StandardDiviation(scoreArr0.toArray(new Float[]{}))));
        }
        if (scoreArr1.size() > 0) {
            avgScore1 = allScore1 / scoreArr1.size();
            avgScore1 = Float.parseFloat(df.format(avgScore1));
            divScore1 = Float.parseFloat(df.format(StandardDiviation(scoreArr1.toArray(new Float[]{}))));
        }
        for (String type : types) {
            float edubefore = 0;
            int eduRank = 0;
            int stuStatNum = 0;
            Map<String, Integer> classRankMap = new HashMap<>();
            Map<String, Integer> gradeRankMap = new HashMap<>();

            Map<String, Float> classScoreMap = new HashMap<>();
            Map<String, Float> gradeScoreMap = new HashMap<>();

            Map<String, Integer> classStuStatNumMap = new HashMap<>();
            Map<String, Integer> gradeStuStatNumMap = new HashMap<>();

            EmStat stat = null;

            for (EmScoreInfo ee : factList) {
                stat = makeStat(ee, parm);
                stat.setGradeName(gradeName);
                float showScore = 0f;
                String clsId = ee.getClassId();
                String classType = ExammanageConstants.CLASS_TYPE1;
                if (!StringUtils.equals("1", type)) {
                    //分数
                    if (StringUtils.equals("0", type)) {
                        if (StringUtils.isNotBlank(ee.getGkSubType())
                                && !StringUtils.equals(ee.getGkSubType(), ExammanageConstants.SUB_TYPE_0)) {
                            continue;
                        }
                    } else if (StringUtils.equals("2", type)) {
                        if (!StringUtils.equals(ee.getGkSubType(), ExammanageConstants.SUB_TYPE_2)) {
                            continue;
                        }
                    } else if (StringUtils.equals("3", type)) {
                        if (!StringUtils.equals(ee.getGkSubType(), ExammanageConstants.SUB_TYPE_1)) {
                            continue;
                        }
                    }
                    if (StringUtils.isNotBlank(ee.getTeachClassId())) {
                        clsId = ee.getTeachClassId();
                        classType = ExammanageConstants.CLASS_TYPE2;
                    }
                    showScore = ee.getShowScore();
                    if (StringUtils.equals(ee.getGkSubType(), ExammanageConstants.SUB_TYPE_1)) {
                        stat.setSubType(ExammanageConstants.SUB_TYPE_3);
                    } else {
                        stat.setSubType(ee.getGkSubType());
                    }
                } else {
                    //赋分
                    if (!StringUtils.equals(ee.getGkSubType(), ExammanageConstants.SUB_TYPE_1)) {
                        continue;
                    }
                    if (StringUtils.isNotBlank(ee.getTeachClassId())) {
                        clsId = ee.getTeachClassId();
                        classType = ExammanageConstants.CLASS_TYPE2;
                    }
                    showScore = Float.parseFloat(ee.getConScore());
                    stat.setSubType(ExammanageConstants.SUB_TYPE_1);
                }

                stat.setClassId(clsId);
                stat.setClassType(classType);
                if (StringUtils.equals(classType, "1")) {
                    stat.setClassName(gradeName + clsNameMap.get(clsId));
                } else {
                    stat.setClassName(teaClsNameMap.get(clsId));
                }
                if (eduRank == 0) {
                    edubefore = showScore;
                    eduRank = 1;
                    stuStatNum = 1;
                } else {
                    if (edubefore > showScore) {
                        eduRank = stuStatNum + 1;
                        edubefore = showScore;
                    }
                    stuStatNum++;
                }
                stat.setEduRank(eduRank);
                if (!classRankMap.containsKey(clsId)) {
                    classRankMap.put(clsId, 1);
                    classScoreMap.put(clsId, showScore);
                    classStuStatNumMap.put(clsId, 1);
                } else {
                    if (classScoreMap.get(clsId) > showScore) {
                        classRankMap.put(clsId, classStuStatNumMap.get(clsId) + 1);
                        classScoreMap.put(clsId, showScore);
                    }
                    classStuStatNumMap.put(clsId, classStuStatNumMap.get(clsId) + 1);
                }
                stat.setClassRank(classRankMap.get(clsId));
                if (!gradeRankMap.containsKey(ee.getUnitId())) {
                    gradeRankMap.put(ee.getUnitId(), 1);
                    gradeScoreMap.put(ee.getUnitId(), showScore);
                    gradeStuStatNumMap.put(ee.getUnitId(), 1);
                } else {
                    if (gradeScoreMap.get(ee.getUnitId()) > showScore) {
                        gradeRankMap.put(ee.getUnitId(), gradeStuStatNumMap.get(ee.getUnitId()) + 1);
                        gradeScoreMap.put(ee.getUnitId(), showScore);
                    }
                    gradeStuStatNumMap.put(ee.getUnitId(), gradeStuStatNumMap.get(ee.getUnitId()) + 1);
                }
                stat.setGradeRank(gradeRankMap.get(ee.getUnitId()));
                if (!StringUtils.equals("1", type) && !StringUtils.equals("3", type)) {
                    stat.setScoreLevel(100f - (100f * stat.getGradeRank() - 50f) / scoreArr0.size());
                    if (divScore0 == 0f) {
                        stat.setScoreZ(0f);
                    } else {
                        stat.setScoreZ((ee.getShowScore() - avgScore0) / divScore0);
                    }
//					System.out.println("divScore0="+divScore0);
                } else {
                    stat.setScoreLevel(100f - (100f * stat.getGradeRank() - 50f) / scoreArr1.size());
                    if (divScore1 == 0f) {
                        stat.setScoreZ(0f);
                    } else {
                        stat.setScoreZ((ee.getShowScore() - avgScore1) / divScore1);
                    }
//					System.out.println("divScore1="+divScore1);
                }
                stat.setScoreT(10f * stat.getScoreZ() + 50f);
                insertStatList.add(stat);
            }
        }
        return insertStatList;
    }

    /**
     * 初始化单个学生统计
     *
     * @param emScoreInfo
     * @param parm
     * @return
     */
    private EmStat makeStat(EmScoreInfo emScoreInfo, EmStatParm parm) {
        EmStat stat = new EmStat();
        stat.setId(UuidUtils.generateUuid());
        stat.setClassId(emScoreInfo.getClassId());
        stat.setExamId(emScoreInfo.getExamId());
        stat.setSchoolId(emScoreInfo.getUnitId());
        stat.setStatObjectId(parm.getStatObjectId());
        stat.setStudentId(emScoreInfo.getStudentId());
        stat.setSubjectId(parm.getSubjectId());
        stat.setScore(emScoreInfo.getShowScore());
        if (StringUtils.equals(emScoreInfo.getGkSubType(), "1") && StringUtils.isNotBlank(emScoreInfo.getConScore())) {
            stat.setConScore(Float.parseFloat(emScoreInfo.getConScore()));
            stat.setConScoreRank(emScoreInfo.getScoreRank());
        }
        stat.setSubType(emScoreInfo.getGkSubType());
        return stat;
    }

    private EmStatSpace makeEmStatSpace(String statRangeId, String spaceItemId, String statObjectId, String scoreNum) {
        EmStatSpace space = new EmStatSpace();
        space.setId(UuidUtils.generateUuid());
        space.setStatRangeId(statRangeId);
        space.setSpaceItemId(spaceItemId);
        space.setStatObjectId(statObjectId);
        space.setScoreNum(scoreNum);
        space.setIsCon("0");
        space.setBlance(0f);
        space.setScoreRank(0);
        return space;
    }


//	/**
//	 * 组装  0,0,0
//	 * @param num
//	 * @return
//	 */
//	private String makeZero(int num){
//		String rs="";
//		for(int i=0;i<num;i++){
//			rs=rs+",0";
//		}
//		if(StringUtils.isNotBlank(rs)){
//			rs=rs.substring(1);
//		}
//		return rs;
//	}

    private List<EmStatSpace> makeZeroEmStatSpace(String rangeId, String statObjectId, List<EmSpaceItem> itemList) {
        List<EmStatSpace> returnList = new ArrayList<EmStatSpace>();
        for (EmSpaceItem item : itemList) {
            returnList.add(makeEmStatSpace(rangeId, item.getId(), statObjectId, "0"));
        }
        return returnList;
    }

    @Override
    public void deleteByStatObjectId(String... statObjectId) {
        if (statObjectId != null && statObjectId.length > 0) {
            emStatSpaceService.deleteByStatObjectId(statObjectId);
            emStatDao.deleteByStatObjectIdIn(statObjectId);
            emStatRangeService.deleteByStatObjectId(statObjectId);
            emStatLineService.deleteByStatObjectId(statObjectId);
            emStatObjectService.updateIsStat("0", statObjectId);
        }

    }

    @Override
    public List<EmStat> saveAllEntitys(EmStat... emStat) {
        return emStatDao.saveAll(checkSave(emStat));
    }

    @Override
    public List<EmStat> findBySearchDto(EmScoreSearchDto dto) {

        Specification<EmStat> specification = findStudentStatSpecification(dto);
        List<EmStat> emStatList = emStatDao.findAll(specification);
        if (CollectionUtils.isEmpty(emStatList)) {
            return new ArrayList<EmStat>();
        }
        return emStatList;
    }

    /**
     * 除校校联考外
     *
     * @param searchType 1本单位设定的考试,2直属教育局设定的考试
     * @param unitId
     * @param searchDto
     * @return
     */
    private Specification<EmStat> findStudentStatSpecification(
            final EmScoreSearchDto searchDto) {
        Specification<EmStat> specification = null;
        specification = new Specification<EmStat>() {
            @Override
            public Predicate toPredicate(Root<EmStat> root,
                                         CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = Lists.newArrayList();

                ps.add(cb
                        .equal(root.get("statObjectId").as(String.class), searchDto.getObjId()));
                ps.add(cb
                        .equal(root.get("examId").as(String.class), searchDto.getExamId()));
                if (StringUtils.isNotBlank(searchDto.getSubjectId())) {
                    ps.add(cb.equal(root.get("subjectId").as(String.class),
                            searchDto.getSubjectId()));
                }
                if (StringUtils.isNotBlank(searchDto.getClassId())) {
                    ps.add(cb.equal(root.get("classId").as(String.class),
                            searchDto.getClassId()));
                }

                List<Order> orderList = new ArrayList<Order>();
                orderList.add(cb.desc(root.get("schoolId").as(
                        String.class)));
                orderList.add(cb
                        .desc(root.get("studentId").as(String.class)));
                orderList.add(cb
                        .desc(root.get("subjectId").as(String.class)));
                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return cq.getRestriction();
            }
        };

        return specification;
    }

    @Override
    public EmStat findByExamIdAndSubjectIdAndStudentId(String statObjectId, String examId,
                                                       String subjectId, String studentId) {
        return emStatDao.findByExamIdAndSubjectIdAndStudentId(statObjectId, examId, subjectId, studentId);
    }

    @Override
    public List<EmStat> findBySchoolRank(String statObjectId, String examId, String schoolId,
                                         String subjectId, int rank1, int rank2, Pagination page) {
        if (page == null) {
            return emStatDao.findBySchoolRank(statObjectId, examId, schoolId, subjectId, rank1, rank2);
        } else {
            page.setMaxRowCount(emStatDao.countBySchoolRank(statObjectId, examId, schoolId, subjectId, rank1, rank2));
            return emStatDao.findBySchoolRank(statObjectId, examId, schoolId, subjectId, rank1, rank2, Pagination.toPageable(page));
        }

    }

    @Override
    public List<EmStat> findByClassRank(String statObjectId, String examId,
                                        String classId, String subjectId, int rank1, int rank2) {
        return emStatDao.findByClassRank(statObjectId, examId, classId, subjectId, rank1, rank2);
    }

    @Override
    public List<EmStat> findByStudentIds(String statObjectId, String examId, String subjectId,
                                         String[] studentIds) {
        //防止in过长报错
        if (studentIds == null || studentIds.length <= 0) {
            return new ArrayList<EmStat>();
        }
        if (studentIds.length <= 1000) {
            List<EmStat> l = new ArrayList<EmStat>();
            if (StringUtils.isNotBlank(subjectId)) {
                l = emStatDao.findByStudentIds(statObjectId, examId, subjectId, studentIds);

            } else {
                l = emStatDao.findByStudentIds(statObjectId, examId, studentIds);

            }
            return l;
        } else {
            List<EmStat> returnList = new ArrayList<EmStat>();
            int length = studentIds.length;
            int cyc = length / 1000 + (length % 1000 == 0 ? 0 : 1);
            for (int i = 0; i < cyc; i++) {
                int max = (i + 1) * 1000;
                if (max > length)
                    max = length;
                List<EmStat> l = new ArrayList<EmStat>();
                String[] stuId = ArrayUtils.subarray(studentIds, i * 1000, max);
                if (StringUtils.isNotBlank(subjectId)) {
                    l = emStatDao.findByStudentIds(statObjectId, examId, subjectId, stuId);
                } else {
                    l = emStatDao.findByStudentIds(statObjectId, examId, stuId);

                }
                returnList.addAll(l);
            }
            return returnList;
        }
    }

    public List<EmStat> findByClassId(String statObjectId, String examId, String classId) {
        return emStatDao.findByClassId(statObjectId, examId, classId);
    }

    @Override
    public List<EmStat> findByClassIdAndSubId(String statObjectId, String examId, String classId, String subjectId, String subType) {
        return emStatDao.findByClassIdAndSubId(statObjectId, examId, classId, subjectId, subType);
    }

    @Override
    public List<EmStat> findByClassIdsAndSubId(String statObjectId, String examId, String[] classIds, String subjectId, String subType) {
        return emStatDao.findByClassIdsAndSubId(statObjectId, examId, subjectId, subType, classIds);
    }

    @Override
    public List<EmStat> findByStudentExamIdIn(String statObjectId, String[] examIds, String subjectId, String studentId) {
        return emStatDao.findByStudentExamIdIn(statObjectId, examIds, subjectId, studentId);
    }

    @Override
    public List<EmStat> findByObjectIdsExamIdIn(String[] statObjectIds, String[] examIds, String subjectId, String studentId) {
        if (StringUtils.isBlank(subjectId)) {
            return emStatDao.findByObjectIdsExamIdIn(statObjectIds, examIds, studentId);
        }
        return emStatDao.findByObjectIdsExamIdIn(statObjectIds, examIds, subjectId, studentId);
    }

    @Override
    public List<EmStat> findByStudentId(String studentId) {
        return emStatDao.findByStudentId(studentId);
    }

    @Override
    public List<EmStat> findByExamIdAndObjectSubType(String examId,
                                                     String objectId) {
        return emStatDao.findByExamIdAndObjectSubType(examId, objectId);
    }

    @Override
    public List<EmStat> findByExamIdAndObjectSubIds(String examId, String objectId, String[] subjectIds) {
        return emStatDao.findByExamIdAndObjectSubIds(examId, objectId, subjectIds);
    }

    @Override
    public List<EmStat> findByExamIdAndObjectsAndSubjectId(String examId,
                                                           String subjectId, String objectId) {
        return emStatDao.findByExamIdAndObjectsAndSubjectId(examId, subjectId, objectId);
    }

    @Override
    public List<EmStat> findByAllExamIdAndObjIdAndSubjectId(String examId,
                                                            String subjectId, String objectId) {
        return emStatDao.findByAllExamIdAndObjIdAndSubjectId(examId, subjectId, objectId);
    }

    @Override
    public List<EmStat> findByExamIdAndStudentId(String statObjectId, String examId, String studentId) {
        return emStatDao.findByExamIdAndStudentId(statObjectId, examId, studentId);
    }

    @Override
    public List<EmStat> findByExamIdAndObjectsAndSubjectIdAndClassId(
            String examId, String subjectId, String objectId, String classId) {
        List<EmStat> list = emStatDao.findByExamIdAndObjectsAndSubjectIdAndClassId(examId, subjectId, objectId, classId);
        for (EmStat emStat : list) {
            if (emStat.getScoreT() != null) {
                BigDecimal b1 = new BigDecimal(emStat.getScoreT());
                emStat.setScoreT(b1.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue());
            }
        }
        return emStatDao.findByExamIdAndObjectsAndSubjectIdAndClassId(examId, subjectId, objectId, classId);
    }

    @Override
    public List<EmStat> findByExamIdAndObjectsAndSubjectIdTotal(String examId,
                                                                String subjectId, String objectId, String subType) {
        return emStatDao.findByExamIdAndObjectsAndSubjectIdTotal(examId, subjectId, objectId, subType);
    }

    @Override
    public List<EmStat> findByExamIdAndObjects(String examId, String objectId) {
        return emStatDao.findByExamIdAndObjects(examId, objectId);
    }

    @Override
    public void saveAbiEmStatList(Map<String, EmAbilitySet> abiMap, String examId, String statObjectId, List<EmStat> statList) {
        if (statList == null || CollectionUtils.isEmpty(statList)) {
            statList = this.findByExamIdAndObjectSubType(examId, statObjectId);//取出该对象考试下的所有学生数据
        }
        Map<String, List<EmStat>> statMap = statList.stream().collect(Collectors.groupingBy(k -> k.getStudentId()));
        List<EmStat> totalList = new ArrayList<>();
        boolean noAbiMap = abiMap == null;
        //计算综合能力分
        Map<String, Float> abiScoreMap = new HashMap<>();//学生对应的综合能力分map
        Float abilityScore = 0.0f;
        for (Entry<String, List<EmStat>> entry : statMap.entrySet()) {
            abilityScore = 0.0f;
//			System.out.println("studentId="+entry.getKey());
            for (EmStat inStat : entry.getValue()) {
                if (ExammanageConstants.ZERO32.equals(inStat.getSubjectId())) {//取出总分的数据
                    totalList.add(inStat);
                }
                if ("1".equals(inStat.getSubType())) {//1 赋分的标准分T过滤 留下3的赋分考试分的标准分T
                    continue;
                }
                if (noAbiMap) {
                    if (!ExammanageConstants.CON_SUM_ID.equals(inStat.getSubjectId()) &&
                            !ExammanageConstants.CON_YSY_ID.equals(inStat.getSubjectId()) && !ExammanageConstants.ZERO32.equals(inStat.getSubjectId())) {
                        abilityScore += inStat.getScoreT();
//						System.out.println("subId:"+inStat.getSubjectId()+"========"+inStat.getScoreT()+"*************"+abilityScore);
                    }
                } else if (abiMap.containsKey(inStat.getSubjectId())) {//排除000 999  888的subjectId
                    abilityScore += inStat.getScoreT() * (abiMap.get(inStat.getSubjectId()).getWeights());
                }
            }
            abiScoreMap.put(entry.getKey(), abilityScore);
        }
        for (EmStat emStat : totalList) {
            abilityScore = abiScoreMap.get(emStat.getStudentId());
            emStat.setAbilityScore(abilityScore == null ? 0f : abilityScore);
        }
        Collections.sort(totalList, (o1, o2) -> {
            return o2.getAbilityScore().compareTo(o1.getAbilityScore());
        });
        abilityScore = 0.0f;
        int j = 1;
        for (int i = 0; i < totalList.size(); i++) {
            //TODO
            Float aFloat = totalList.get(i).getAbilityScore();
            if (Float.isNaN(aFloat)) {
                aFloat = 0f;
            }
            BigDecimal b = new BigDecimal(aFloat);
            Float nowAbiScore = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
            if (i == 0) {
                abilityScore = nowAbiScore;
            }
            EmStat stat = totalList.get(i);
            if (abilityScore > nowAbiScore) {
                stat.setAbilityRank(i + 1);
                abilityScore = nowAbiScore;
                j = i + 1;
            } else {
                stat.setAbilityRank(j);
            }
        }
        this.saveAll(totalList.toArray(new EmStat[]{}));
    }
}
