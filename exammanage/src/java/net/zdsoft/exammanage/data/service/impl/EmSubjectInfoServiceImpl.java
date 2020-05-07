package net.zdsoft.exammanage.data.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.eclasscard.remote.service.EclasscardRemoteService;
import net.zdsoft.exammanage.data.dao.EmSubjectInfoDao;
import net.zdsoft.exammanage.data.dto.ExamInfoDto;
import net.zdsoft.exammanage.data.dto.ScoreInfoDto;
import net.zdsoft.exammanage.data.dto.SubjectInfoDto;
import net.zdsoft.exammanage.data.entity.*;
import net.zdsoft.exammanage.data.service.*;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("emSubjectInfoService")
public class EmSubjectInfoServiceImpl extends BaseServiceImpl<EmSubjectInfo, String> implements EmSubjectInfoService {
    @Autowired
    private EmSubjectInfoDao emSubjectInfoDao;
    @Autowired
    private EmClassInfoService emClassInfoService;
    @Autowired
    private CourseRemoteService courseRemoteService;
    @Autowired
    private StudentSelectSubjectRemoteService studentSelectSubjectRemoteService;
    @Autowired
    private GradeRemoteService gradeRemoteService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private EmStudentGroupService emStudentGroupService;
    @Autowired
    private EmSubGroupService emSubGroupService;
    @Autowired
    private EmEnrollStudentService emEnrollStudentService;
    @Autowired
    private EmExamNumService emExamNumService;
    @Autowired
    private EmJoinexamschInfoService emJoinexamschInfoService;
    @Autowired
    private EmScoreInfoService emScoreInfoService;
    @Autowired
    private EmClassLockService emClassLockService;
    @Autowired
    private EmStatObjectService emStatObjectService;
    @Autowired
    private EmStatService emStatService;
    @Autowired
    private ClassTeachingRemoteService classTeachingRemoteService;
    @Autowired
    private EmExamInfoService emExamInfoService;
    @Autowired
    private TeachClassRemoteService teachClassService;

    @Override
    protected BaseJpaRepositoryDao<EmSubjectInfo, String> getJpaDao() {
        return emSubjectInfoDao;
    }

    @Override
    protected Class<EmSubjectInfo> getEntityClass() {
        return EmSubjectInfo.class;
    }

    @Override
    public List<EmSubjectInfo> findByExamId(String examId) {
        List<EmSubjectInfo> list = emSubjectInfoDao.findByExamId(examId);
        makeSubjectName(list);
        return list;
    }

    @Override
    public List<EmSubjectInfo> findByUnitIdAndExamId(String unitId, String... examIds) {
        List<EmSubjectInfo> list = emSubjectInfoDao.findByUnitIdAndExamId(unitId, examIds);
        makeSubjectName(list);
        return list;
    }

    /**
     * 组装科目名称
     *
     * @param list
     */
    private void makeSubjectName(List<EmSubjectInfo> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        Set<String> subjectIds = EntityUtils.getSet(list, "subjectId");

        List<Course> courseList = SUtils.dt(courseRemoteService.findBySubjectIdIn(subjectIds.toArray(new String[]{})), new TR<List<Course>>() {
        });
        if (CollectionUtils.isEmpty(courseList)) {
            return;
        }
        Map<String, String> courseNamesMap = EntityUtils.getMap(courseList, "id", "subjectName");
        for (EmSubjectInfo emSubjectInfo : list) {
            if (courseNamesMap.containsKey(emSubjectInfo.getSubjectId())) {
                emSubjectInfo.setCourseName(courseNamesMap.get(emSubjectInfo.getSubjectId()));
            }
        }
    }

    @Override
    public List<EmSubjectInfo> saveAllEntitys(EmSubjectInfo... emSubjectInfo) {
        return emSubjectInfoDao.saveAll(checkSave(emSubjectInfo));
    }

    @Override
    public void deleteByIdIn(String... id) {
//		eclasscardRemoteService.syncDeleteExamTimeTo(id);
        emSubjectInfoDao.deleteByIdIn(id);
    }

    @Override
    public void saveSubjectClass(EmExamInfo examInfo, String schoolId,
                                 List<EmSubjectInfo> emSubjectInfos, String[] classIds) {
        //当传入的教育局id 删除的数据是空的 没有影响
        String examId = examInfo.getId();
        emClassInfoService.deleteByExamIdAndSchoolId(examId, schoolId);
        if (classIds != null && classIds.length > 0) {
            EmClassInfo classInfo = new EmClassInfo();
            List<EmClassInfo> list = new ArrayList<EmClassInfo>();
            for (String cId : classIds) {
                classInfo = new EmClassInfo();
                classInfo.setClassId(cId);
                classInfo.setSchoolId(schoolId);
                classInfo.setExamId(examId);
                classInfo.setClassType("1");//行政班
                classInfo.setId(UuidUtils.generateUuid());
                list.add(classInfo);
            }
            if (CollectionUtils.isNotEmpty(list)) {
                emClassInfoService.saveAllEntitys(list.toArray(new EmClassInfo[]{}));
            }
        }
        List<EmJoinexamschInfo> schInfos = emJoinexamschInfoService.findByExamId(examId);
        String unitIds = "";
        if (CollectionUtils.isNotEmpty(schInfos)) {
            for (EmJoinexamschInfo e : schInfos) {
                if (unitIds.length() > 0) {
                    unitIds = unitIds + "," + e.getSchoolId();
                } else {
                    unitIds = e.getSchoolId();
                }
            }
        } else {
            unitIds = schoolId;
        }


        //删除原来的
        emSubjectInfoDao.deleteByExamId(examId);
        if ("1".equals(examInfo.getIsgkExamType())) {//高考模式下的数据保存
            //通过schoolId  examId删除对应的高考模式的数据
            emSubGroupService.deleteByExamIdAndUnitId(examId, schoolId);
            emEnrollStudentService.deleteByExamIdAndUnitId(examId, schoolId);
            emStudentGroupService.deleteByExamIdAndSchoolId(examId, schoolId);
            List<EmSubGroup> groups = saveOfGkType(examInfo, emSubjectInfos, schoolId);
            //TODO
            if (CollectionUtils.isNotEmpty(groups)) {
                JSONArray array = new JSONArray();
                for (EmSubjectInfo emSubjectInfo : emSubjectInfos) {
                    for (EmSubGroup group : groups) {
                        if (group.getSubjectId().indexOf(emSubjectInfo.getSubjectId()) > -1) {
                            JSONObject json = new JSONObject();
                            String startTime ="";
                            String endTime = "";
                            if ("2".equals(group.getSubType())) {
                                startTime=DateUtils.date2String(emSubjectInfo.getGkStartDate(), "yyyy-MM-dd HH:mm");
                                endTime = DateUtils.date2String(emSubjectInfo.getGkEndDate(), "yyyy-MM-dd HH:mm");
                            } else {
                                startTime = DateUtils.date2String(emSubjectInfo.getStartDate(), "yyyy-MM-dd HH:mm");
                                endTime = DateUtils.date2String(emSubjectInfo.getEndDate(), "yyyy-MM-dd HH:mm");
                            }
                            json.put("beginTime", startTime);
                            json.put("endTime", endTime);
                            json.put("examId", examId);
                            json.put("subjectId", emSubjectInfo.getId());
                            json.put("subType", group.getSubType());
                            json.put("unitId", unitIds);
                            array.add(json);
                        }
                    }
                }
                EclasscardRemoteService eclasscardRemoteService = Evn.getBean("eclasscardRemoteService");
                if (eclasscardRemoteService != null)
                    eclasscardRemoteService.syncExamTimeToEClassCard(array);
            }
        } else {
            if (CollectionUtils.isNotEmpty(emSubjectInfos)) {
                this.saveAllEntitys(emSubjectInfos.toArray(new EmSubjectInfo[]{}));
                JSONArray array = new JSONArray();
                for (EmSubjectInfo emSubjectInfo : emSubjectInfos) {
                    JSONObject json = new JSONObject();
                    String startTime = DateUtils.date2String(emSubjectInfo.getStartDate(), "yyyy-MM-dd HH:mm");
                    String endTime = DateUtils.date2String(emSubjectInfo.getEndDate(), "yyyy-MM-dd HH:mm");
                    json.put("beginTime", startTime);
                    json.put("endTime", endTime);
                    json.put("examId", examId);
                    json.put("subjectId", emSubjectInfo.getId());
                    json.put("subType", "0");
                    json.put("unitId", unitIds);
                    array.add(json);
                }
                EclasscardRemoteService eclasscardRemoteService = Evn.getBean("eclasscardRemoteService");
                if (eclasscardRemoteService != null)
                    eclasscardRemoteService.syncExamTimeToEClassCard(array);
            }
        }
    }

    /**
     * 高考模式
     *
     * @param examInfo
     * @param emSubjectInfos
     * @param schoolId
     */
    public List<EmSubGroup> saveOfGkType(EmExamInfo examInfo, List<EmSubjectInfo> emSubjectInfos, String schoolId) {
        String examId = examInfo.getId();
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findByUnitIdAndGradeCode(schoolId, examInfo.getGradeCodes()), new TR<List<Grade>>() {
        });
        String[] subjectIds = emSubjectInfos.stream().map(EmSubjectInfo::getSubjectId).collect(Collectors.toSet()).toArray(new String[0]);
        //获取要保存的课程信息
        EmEnrollStudent enrollStudent = null;
        List<EmSubGroup> insertGroupList = new ArrayList<EmSubGroup>();
        List<EmEnrollStudent> insEnrStudentList = new ArrayList<EmEnrollStudent>();
        boolean haveYsy = false;
        Set<String> ysySubjectIds = new HashSet<String>();
        Set<String> xxTypeSubIds = new HashSet<String>();//除语数英等外的subject+学考或者选考类型
        //保存exammanage_sub_group表
        haveYsy = saveEmSubGroup(insertGroupList, emSubjectInfos, subjectIds, examId, schoolId, ysySubjectIds, xxTypeSubIds);

        this.saveAllEntitys(emSubjectInfos.toArray(new EmSubjectInfo[]{}));

        if (CollectionUtils.isNotEmpty(gradeList)) {
            Map<String, Set<String>> stuIdMap = new HashMap<>();
            List<Student> studentList = getStuSth(examInfo.getAcadyear(), examInfo.getSemester(), stuIdMap, subjectIds, gradeList.get(0).getId(), haveYsy, ysySubjectIds, xxTypeSubIds);
            Map<String, String> stuClassIdMap = studentList.stream().collect(Collectors.toMap(Student::getId, Student::getClassId));
            //exammanage_apply_student表保存
            for (Student student : studentList) {
                enrollStudent = new EmEnrollStudent();
                enrollStudent.setId(UuidUtils.generateUuid());
                enrollStudent.setUnitId(schoolId);
                enrollStudent.setSchoolId(schoolId);
                enrollStudent.setExamId(examId);
                enrollStudent.setStudentId(student.getId());
                enrollStudent.setClassId(student.getClassId());
                enrollStudent.setHasPass("1");
                insEnrStudentList.add(enrollStudent);
            }
            emEnrollStudentService.saveAll(insEnrStudentList.toArray(new EmEnrollStudent[]{}));
            //exammanage_student_group表保存
            saveStuGrp(insertGroupList, stuIdMap, stuClassIdMap, examId, schoolId);
            //
        }
        return insertGroupList;
    }

    public void deleteExamNum(List<Student> studentList, String examId, String schoolId) {
        List<EmExamNum> exNumList = emExamNumService.findunitIdAndExamId(schoolId, examId);
        if (CollectionUtils.isNotEmpty(exNumList)) {
            List<EmExamNum> deleteList = new ArrayList<EmExamNum>();
            if (CollectionUtils.isNotEmpty(studentList)) {
                Set<String> stuIds = studentList.stream().map(Student::getId).collect(Collectors.toSet());
                for (EmExamNum examNum : exNumList) {
                    if (!stuIds.contains(examNum.getStudentId())) {
                        deleteList.add(examNum);
                    }
                }
            } else {
                deleteList.addAll(exNumList);
            }
            emExamNumService.deleteAll(deleteList.toArray(new EmExamNum[]{}));
        }

    }

    /**
     * 保存exammanage_sub_group表 并获取相应的数据
     *
     * @param insertGroupList
     * @param emSubjectInfos
     * @param subjectIds
     * @param examId
     * @param schoolId
     * @param ysySubjectIds
     * @param xxTypeSubIds
     * @return
     */
    public boolean saveEmSubGroup(List<EmSubGroup> insertGroupList, List<EmSubjectInfo> emSubjectInfos, String[] subjectIds, String examId,
                                  String schoolId, Set<String> ysySubjectIds, Set<String> xxTypeSubIds) {
//		List<String> ysyList=Arrays.asList(BaseConstants.SUBJECT_TYPES_YSY);
        List<Course> courseList73 = SUtils.dt(courseRemoteService.findByCodes73(schoolId), new TR<List<Course>>() {
        });
        Map<String, Course> course73Map = courseList73.stream().collect(Collectors.toMap(Course::getId, Function.identity()));
        EmSubGroup group = null;
        boolean haveYsy = false;
        Map<String, EmSubGroup> groupMap = new HashMap<String, EmSubGroup>();
        //获取要保存的课程信息
        List<Course> courseList = SUtils.dt(courseRemoteService.findListByIds(subjectIds), new TR<List<Course>>() {
        });
        Map<String, Course> courseMap = courseList.stream().collect(Collectors.toMap(Course::getId, Function.identity()));
        //exammanage_sub_group表保存
        for (EmSubjectInfo emSubjectInfo : emSubjectInfos) {
            group = new EmSubGroup();
            String gkSubType = emSubjectInfo.getGkSubType();
            String subjectId = emSubjectInfo.getSubjectId();
            Course course = courseMap.get(subjectId);
            if (course != null) {
                group.setId(UuidUtils.generateUuid());
                group.setUnitId(schoolId);
                group.setExamId(examId);
                Course course73 = course73Map.get(subjectId);
                if (course73 == null) {//非7选3科目
                    if (groupMap.containsKey("ysyGroup")) {
                        group = groupMap.get("ysyGroup");
                        if (group.getSubjectId().indexOf(subjectId) == -1) {//确保不重复的语数英等非7选3科目
                            group.setSubjectId(group.getSubjectId() + "," + subjectId);
                        }
                    } else {
                        group.setSubType("0");
                        group.setSubjectId(subjectId);
                        group.setGroupName("非7选3科目");
                    }
                    groupMap.put("ysyGroup", group);
                    emSubjectInfo.setGkSubType("0");
                    emSubjectInfo.setGkEndDate(null);
                    emSubjectInfo.setGkStartDate(null);
                    ysySubjectIds.add(subjectId);
                    haveYsy = true;
                } else {
                    group.setSubType(gkSubType);
                    group.setSubjectId(subjectId);
                    if ("1".equals(gkSubType)) {//判断选考学考
                        group.setGroupName(course.getShortName() + "选考");
                        emSubjectInfo.setGkEndDate(null);
                        emSubjectInfo.setGkStartDate(null);
                        xxTypeSubIds.add(subjectId + "_1");
                    } else if ("2".equals(gkSubType)) {
                        group.setGroupName(course.getShortName() + "学考");
                        emSubjectInfo.setEndDate(null);
                        xxTypeSubIds.add(subjectId + "_2");
                    } else {
                        xxTypeSubIds.add(subjectId + "_1");
                        xxTypeSubIds.add(subjectId + "_2");
                        EmSubGroup group1 = new EmSubGroup();
                        group.setGroupName(course.getShortName() + "选考");
                        group.setSubType("1");
                        group1.setId(UuidUtils.generateUuid());
                        group1.setUnitId(schoolId);
                        group1.setExamId(examId);
                        group1.setSubType("2");
                        group1.setSubjectId(subjectId);
                        group1.setGroupName(course.getShortName() + "学考");
                        insertGroupList.add(group1);
                    }
                }
            }
            insertGroupList.add(group);
        }
        emSubGroupService.saveAll(insertGroupList.toArray(new EmSubGroup[]{}));
        return haveYsy;
    }

    /**
     * 获取学生数据 以及 subjectid对应的选考学考学生数map
     *
     * @param stuIdMap
     * @param subjectIds
     * @param gradeId
     * @param haveYsy
     * @param ysySubjectIds
     * @param xxTypeSubIds
     * @return
     */
    public List<Student> getStuSth(String acadyear, String semester, Map<String, Set<String>> stuIdMap, String[] subjectIds,
                                   String gradeId, boolean haveYsy, Set<String> ysySubjectIds, Set<String> xxTypeSubIds) {
        List<Student> studentList = new ArrayList<Student>();
        //获取subjectId对应的studentIds的map
        Map<String, Set<String>> xuankaoStuIdMap = new HashMap<>();
        Map<String, Set<String>> xuekaoStuIdMap = new HashMap<>();
        if (subjectIds != null && subjectIds.length > 0) {
            xuankaoStuIdMap = SUtils.dt(studentSelectSubjectRemoteService.getStuSelectByGradeId(acadyear, semester, gradeId, true, subjectIds), new TR<Map<String, Set<String>>>() {
            });
            xuekaoStuIdMap = SUtils.dt(studentSelectSubjectRemoteService.getStuSelectByGradeId(acadyear, semester, gradeId, false, subjectIds), new TR<Map<String, Set<String>>>() {
            });
        }
        Set<String> studentIds = new HashSet<>();
        if (xuankaoStuIdMap != null) {
            for (Entry<String, Set<String>> entry : xuankaoStuIdMap.entrySet()) {
                //选考学生ids
                Set<String> xuanStuIds = entry.getValue();
                if (xuanStuIds == null)
                    xuanStuIds = new HashSet<String>();
                if (xxTypeSubIds.contains(entry.getKey() + "_1")) {
                    studentIds.addAll(xuanStuIds);
                }
                stuIdMap.put(entry.getKey() + "_1", xuanStuIds);
            }
        }
        if (xuekaoStuIdMap != null) {
            for (Entry<String, Set<String>> entry : xuekaoStuIdMap.entrySet()) {
                //选考学生ids
                Set<String> xueStuIds = entry.getValue();
                if (xueStuIds == null)
                    xueStuIds = new HashSet<String>();
                if (xxTypeSubIds.contains(entry.getKey() + "_2")) {
                    studentIds.addAll(xueStuIds);
                }
                stuIdMap.put(entry.getKey() + "_2", xueStuIds);
            }
        }
        if (haveYsy) {
            studentList.addAll(SUtils.dt(studentRemoteService.findByGradeId(gradeId), new TR<List<Student>>() {
            }));
            Set<String> ysyStuIds = studentList.stream().map(Student::getId).collect(Collectors.toSet());
            for (String subjectId : subjectIds) {
                if (ysySubjectIds.contains(subjectId)) {
                    stuIdMap.put(subjectId, ysyStuIds);
                }
            }
        } else {
            studentList = SUtils.dt(studentRemoteService.findListByIds(studentIds.toArray(new String[0])), new TR<List<Student>>() {
            });
        }
        return studentList;
    }

    /**
     * 保存exammanage_student_group表数据
     *
     * @param insertGroupList
     * @param stuIdMap
     * @param stuClassIdMap
     * @param examId
     * @param schoolId
     */
    public void saveStuGrp(List<EmSubGroup> insertGroupList, Map<String, Set<String>> stuIdMap, Map<String, String> stuClassIdMap,
                           String examId, String schoolId) {
        EmStudentGroup studentGroup = null;
        List<EmStudentGroup> insStuGroupList = new ArrayList<EmStudentGroup>();
        Set<String> inStuIds = null;
        for (EmSubGroup emSubGroup : insertGroupList) {
            inStuIds = new HashSet<String>();
            String subjectIdstr = emSubGroup.getSubjectId();
            if (subjectIdstr.indexOf(",") == -1) {//不包含，说明是单个subjectid
                //选考或学考的学生ids
                if ("1".equals(emSubGroup.getSubType())) {
                    inStuIds = stuIdMap.get(subjectIdstr + "_1");
                } else if ("2".equals(emSubGroup.getSubType())) {
                    inStuIds = stuIdMap.get(subjectIdstr + "_2");
                } else {
                    inStuIds = stuIdMap.get(subjectIdstr);
                }
            } else {
                String[] subjectIdArr = subjectIdstr.split(",");
                for (String subjectIdA : subjectIdArr) {
                    if (CollectionUtils.isNotEmpty(stuIdMap.get(subjectIdA))) {
                        inStuIds.addAll(stuIdMap.get(subjectIdA));//理论上只执行一次，可取到所有学生
                    }
                }
            }
            //exammanage_student_group表保存
            if (CollectionUtils.isNotEmpty(inStuIds)) {
                for (String inStuId : inStuIds) {
                    studentGroup = new EmStudentGroup();
                    studentGroup.setId(UuidUtils.generateUuid());
                    studentGroup.setSchoolId(schoolId);
                    studentGroup.setExamId(examId);
                    studentGroup.setGroupId(emSubGroup.getId());
                    studentGroup.setStudentId(inStuId);
                    studentGroup.setClassId(stuClassIdMap.get(inStuId));
                    studentGroup.setClassType("1");//TODO
                    insStuGroupList.add(studentGroup);
                }
            }
        }
        emStudentGroupService.saveAll(insStuGroupList.toArray(new EmStudentGroup[]{}));
    }

    @Override
    public List<EmSubjectInfo> findByExamIdAndSubjectId(String examId,
                                                        String subjectId) {
        List<EmSubjectInfo> subInfoList = null;
        if (StringUtils.isNotBlank(subjectId) && !subjectId.equals("all")) {
            subInfoList = emSubjectInfoDao.findByExamIdAndSubjectId(examId, subjectId);
        } else {
            subInfoList = emSubjectInfoDao.findByExamId(examId);
        }
        makeSubjectName(subInfoList);
        return subInfoList;
    }

    @Override
    public List<String> findSubIdByExamIds(Set<String> examIds) {
        return emSubjectInfoDao.findSubIdByExamIds(examIds.toArray(new String[0]));
    }

    @Override
    public List<EmSubjectInfo> findByExamIds(Set<String> examIds) {
        return emSubjectInfoDao.findByExamIds(examIds.toArray(new String[0]));
    }

    @Override
    public void updateIsLock(String examId, String isLock) {
        emSubjectInfoDao.updateIsLock(examId, isLock);
    }

    @Override
    public Map<String, Set<String>> findTeacher(String examId, String subjectId, String unitId, Set<String> classIds, Set<String> teachClassIds) {
        // TODO Auto-generated method stub
        EmExamInfo examInfo = emExamInfoService.findOne(examId);
        Map<String, Set<String>> map = new HashMap<>();
        if (examInfo != null && CollectionUtils.isNotEmpty(classIds)) {
            List<ClassTeaching> classTeachingList = SUtils.dt(classTeachingRemoteService.findByClassIdsSubjectIds(unitId, examInfo.getAcadyear(), examInfo.getSemester(),
                    classIds.toArray(new String[]{}), new String[]{subjectId}, true), new TR<List<ClassTeaching>>() {
            });
            if (CollectionUtils.isNotEmpty(classTeachingList)) {
                for (ClassTeaching c : classTeachingList) {
                    Set<String> teacherSet = new HashSet<>();
                    if (CollectionUtils.isNotEmpty(c.getTeacherIds())) {
                        teacherSet.addAll(c.getTeacherIds());
                    }
                    if (StringUtils.isNotBlank(c.getTeacherId())) {
                        teacherSet.add(c.getTeacherId());
                    }
                    if (CollectionUtils.isEmpty(teacherSet)) {
                        continue;
                    }
                    //取得教师
                    if (!map.containsKey(c.getClassId())) {
                        map.put(c.getClassId(), new HashSet<String>());
                    }
                    map.get(c.getClassId()).addAll(teacherSet);
                }
            }

        }
        if (CollectionUtils.isNotEmpty(teachClassIds)) {
            List<TeachClass> jxbList = SUtils.dt(teachClassService.findListByIds(teachClassIds.toArray(new String[]{})), new TR<List<TeachClass>>() {
            });
            if (CollectionUtils.isNotEmpty(jxbList)) {
                for (TeachClass t : jxbList) {
                    if (!map.containsKey(t.getId())) {
                        map.put(t.getId(), new HashSet<String>());
                    }
                    //取得教师
                    Set<String> tSet = new HashSet<>();
                    if (StringUtils.isNotBlank(t.getTeacherId()) && (!BaseConstants.ZERO_GUID.equals(t.getTeacherId()))) {
                        tSet.add(t.getTeacherId());
                    }
                    if (StringUtils.isNotBlank(t.getAssistantTeachers())) {
                        if (t.getAssistantTeachers().indexOf(",") >= 0) {
                            String[] arr = t.getAssistantTeachers().split(",");
                            tSet.addAll(Arrays.asList(arr));
                        } else {
                            tSet.add(t.getAssistantTeachers());
                        }
                    }
                    if (CollectionUtils.isNotEmpty(tSet)) {
                        map.get(t.getId()).addAll(tSet);
                    }
                }
            }
        }
        return map;
    }

    @Override
    public void deleteByExamIdIn(String[] examIds) {
        emSubjectInfoDao.deleteByExamIdIn(examIds);
    }

    /**
     * 杭外同步功能
     */
    @Override
    public void synch(ExamInfoDto examDto, List<ScoreInfoDto> scoreDtoList, List<SubjectInfoDto> subjectDtoList) {
        String gradeCodeStr = examDto.getRanges();
        if (StringUtils.isNotBlank(gradeCodeStr)) {
            //考试信息list
            List<EmExamInfo> examInfoList = new ArrayList<>();
            //考试班级
            List<EmClassInfo> classInfolist = new ArrayList<>();
            //考试科目
            List<EmSubjectInfo> subjectInfoList = new ArrayList<>();
            //考试成绩
            List<EmScoreInfo> scoreInfoList = new ArrayList<>();
            //成绩锁定
            List<EmClassLock> lockList = new ArrayList<>();
            String[] gradeCodeArr = gradeCodeStr.split(",");
            String acadyear = examDto.getAcadyear();
            String unitId = examDto.getUnitId();
            String examId = null;
            for (String gradeCode : gradeCodeArr) {
                //考试信息list
                EmExamInfo examInfo = examDto.getExamInfoByDto();
                examInfo.setId(UuidUtils.generateUuid());
                examInfo.setGradeCodes(gradeCode);
                examInfo.setOriginExamId(examDto.getId());
                examInfo.setIsgkExamType("0");
                examInfoList.add(examInfo);
                examId = examInfo.getId();

                //至多几个年级 暂时循环调用     考试班级
                List<Clazz> classList = showFindClass(unitId, acadyear, gradeCode);
                Set<String> classIds = classList.stream().map(Clazz::getId).collect(Collectors.toSet());
                EmClassInfo classInfo = new EmClassInfo();
                for (Clazz clazz : classList) {
                    classInfo = new EmClassInfo();
                    classInfo.setClassId(clazz.getId());
                    classInfo.setSchoolId(unitId);
                    classInfo.setExamId(examId);
                    classInfo.setClassType("1");//行政班
                    classInfo.setId(UuidUtils.generateUuid());
                    classInfolist.add(classInfo);
                }
                //考试科目
                if (CollectionUtils.isNotEmpty(subjectDtoList)) {
                    for (SubjectInfoDto subjectDto : subjectDtoList) {
                        if (gradeCode.equals(StringUtils.trim(subjectDto.getRangeType()))) {
                            EmSubjectInfo subjectInfo = subjectDto.getSubjectInfoByDto();
                            subjectInfo.setId(UuidUtils.generateUuid());
                            subjectInfo.setExamId(examId);
                            subjectInfo.setIsLock("0");//默认否
                            subjectInfo.setGkSubType("0");//默认
                            subjectInfoList.add(subjectInfo);
                            for (Clazz clazz : classList) {
                                EmClassLock classLock = new EmClassLock();
                                classLock.setId(UuidUtils.generateUuid());
                                classLock.setClassId(clazz.getId());
                                classLock.setClassType("1");//杭外默认行政班
                                classLock.setCreationTime(new Date());
                                classLock.setExamId(examId);
                                classLock.setLockState("1");
                                classLock.setSchoolId(unitId);
                                classLock.setSubjectId(subjectInfo.getSubjectId());
                                lockList.add(classLock);
                            }
                        }
                    }
                }
                Map<String, String> testMap = new HashMap<>();
                if (CollectionUtils.isNotEmpty(scoreDtoList)) {
                    for (ScoreInfoDto scoreDto : scoreDtoList) {
                        if (classIds.contains(scoreDto.getClassId())) {
                            if (!testMap.containsKey(scoreDto.getStudentId() + scoreDto.getSubjectId())) {
                                testMap.put(scoreDto.getStudentId() + scoreDto.getSubjectId(), "one");//去除重复学生科目成绩
                                EmScoreInfo scoreInfo = scoreDto.getScoreInfoByDto();
                                scoreInfo.setId(UuidUtils.generateUuid());
                                scoreInfo.setExamId(examId);
                                scoreInfo.setGkSubType("0");
                                scoreInfoList.add(scoreInfo);
                            }
                        }
                    }
                }
                String[] examIds = null;
                List<EmExamInfo> oldExamList = emExamInfoService.findByOriginExamId(examDto.getId());
                EmStatObject obj = null;
                //通过来源考试id删除exammanage对应的考试以及科目成绩等
                if (CollectionUtils.isNotEmpty(oldExamList)) {
                    examIds = oldExamList.stream().map(EmExamInfo::getId).collect(Collectors.toSet()).toArray(new String[]{});
                    emExamInfoService.deleteAllIsDeleted(examIds);
                    emClassInfoService.deleteByExamIds(examIds);
                    deleteByExamIdIn(examIds);
                    emScoreInfoService.deleteByExamIdIn(examIds);
                    emClassLockService.deleteByExamIdIn(examIds);
                    for (EmExamInfo oldExam : oldExamList) {//删除统计结果
                        obj = emStatObjectService.findByUnitIdExamId(unitId, oldExam.getId());
                        if (obj != null)
                            emStatService.deleteByStatObjectId(obj.getId());
                    }
                }
				/*if(obj!=null){//随机取一个统计参数 作为默认同步后的参数
					List<EmStatParm>  parmList=emStatParmService.findByStatObjectIdAndExamId(obj.getId(), examId);
					List<EmStatParm> newParmList=new ArrayList<>();
					if(CollectionUtils.isNotEmpty(parmList)){
						for(EmExamInfo exam:examInfoList){
							for(EmStatParm parm:parmList){
								parm.setId(UuidUtils.generateUuid());
								parm.setExamId(exam.getId());
								parm.setIsStat("0");
								newParmList.add(parm);
							}
						}
						emStatParmService.saveAll(newParmList.toArray(new EmStatParm[]{}));
						
					}
				}*/
                //删除后 保存新的
                emExamInfoService.saveAll(examInfoList.toArray(new EmExamInfo[]{}));
                emClassInfoService.saveAll(classInfolist.toArray(new EmClassInfo[]{}));
                this.saveAll(subjectInfoList.toArray(new EmSubjectInfo[]{}));
                emScoreInfoService.saveAll(scoreInfoList.toArray(new EmScoreInfo[]{}));
                emClassLockService.saveAll(lockList.toArray(new EmClassLock[]{}));
            }
        }
    }

    public List<Clazz> showFindClass(String schoolId, String acadyear, String gradeCodes) {
        int section = NumberUtils.toInt(gradeCodes.substring(0, 1));
        int afterGradeCode = NumberUtils.toInt(gradeCodes.substring(1, 2));
        int beforeSelectAcadyear = NumberUtils.toInt(StringUtils.substringBefore(acadyear, "-"));
        String openAcadyear = (beforeSelectAcadyear - afterGradeCode + 1) + "-" + (beforeSelectAcadyear - afterGradeCode + 2);
        List<Grade> gradeList = SUtils.dt(gradeRemoteService.findBySchidSectionAcadyear(schoolId, openAcadyear, new Integer[]{section}), new TR<List<Grade>>() {
        });
        //如果出现多个高一这种情况---这个问题在考务编排上--保存gradeId?
        List<Clazz> classList = new ArrayList<Clazz>();
        if (CollectionUtils.isNotEmpty(gradeList)) {
            //Set<String> ids = EntityUtils.getSet(gradeList, "id");
            Grade grade = gradeList.get(0);
            classList = SUtils.dt(classRemoteService.findBySchoolIdGradeId(grade.getSchoolId(), grade.getId()), new TR<List<Clazz>>() {
            });
        }
        return classList;
    }
}
