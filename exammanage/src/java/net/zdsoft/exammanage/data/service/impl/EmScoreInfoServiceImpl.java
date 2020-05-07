package net.zdsoft.exammanage.data.service.impl;

import com.google.common.collect.Lists;
import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.dao.EmScoreInfoDao;
import net.zdsoft.exammanage.data.dto.EmScoreInfoDto;
import net.zdsoft.exammanage.data.dto.ScoreLimitSearchDto;
import net.zdsoft.exammanage.data.entity.EmConversion;
import net.zdsoft.exammanage.data.entity.EmLimit;
import net.zdsoft.exammanage.data.entity.EmScoreInfo;
import net.zdsoft.exammanage.data.entity.EmSubjectInfo;
import net.zdsoft.exammanage.data.service.*;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.*;

@Service("emScoreInfoService")
public class EmScoreInfoServiceImpl extends BaseServiceImpl<EmScoreInfo, String> implements EmScoreInfoService {

    @Autowired
    private EmScoreInfoDao emScoreInfoDao;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private TeachClassRemoteService teachClassService;
    @Autowired
    private EmExamInfoService emExamInfoService;
    @Autowired
    private EmSubjectInfoService emSubjectInfoService;
    @Autowired
    private EmConversionService emConversionService;
    @Autowired
    private CourseRemoteService courseRemoteService;
    @Autowired
    private EmLimitService emLimitService;
    @Autowired
    private EmNotLimitService emNotLimitService;
    @Autowired
    private EmPlaceStudentService emPlaceStudentService;
    @Autowired
    private EmExamNumService emExamNumService;

    @Override
    protected BaseJpaRepositoryDao<EmScoreInfo, String> getJpaDao() {
        return emScoreInfoDao;
    }

    @Override
    protected Class<EmScoreInfo> getEntityClass() {
        return EmScoreInfo.class;
    }

    @Override
    public Map<String, EmScoreInfo> findByStudent(String examId,
                                                  String subjectId, String[] studentIds) {
        if (studentIds == null || studentIds.length <= 0) {
            return new HashMap<>();
        }
        //防止in过长报错
        List<EmScoreInfo> list = new ArrayList<EmScoreInfo>();
        if (studentIds.length <= 1000) {
            list = emScoreInfoDao.findByStudent(examId, subjectId, studentIds);
        } else {
            int length = studentIds.length;
            int cyc = length / 1000 + (length % 1000 == 0 ? 0 : 1);
            for (int i = 0; i < cyc; i++) {
                int max = (i + 1) * 1000;
                if (max > length)
                    max = length;
                String[] stuId = ArrayUtils.subarray(studentIds, i * 1000, max);
                List<EmScoreInfo> list1 = emScoreInfoDao.findByStudent(examId, subjectId, stuId);
                list.addAll(list1);
            }
        }

        Map<String, EmScoreInfo> stuScoreMap = new HashMap<String, EmScoreInfo>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (EmScoreInfo e : list) {
                stuScoreMap.put(e.getStudentId(), e);
            }
        }
        return stuScoreMap;
    }

    @Override
    public Map<String, EmScoreInfo> findByStudent(String examId,
                                                  String subjectId, String subType, String[] studentIds) {
        if (studentIds == null || studentIds.length <= 0) {
            return new HashMap<>();
        }
        //防止in过长报错
        List<EmScoreInfo> list = new ArrayList<EmScoreInfo>();
        if (studentIds.length <= 1000) {
            list = emScoreInfoDao.findByStudent(examId, subjectId, subType, studentIds);
        } else {
            int length = studentIds.length;
            int cyc = length / 1000 + (length % 1000 == 0 ? 0 : 1);
            for (int i = 0; i < cyc; i++) {
                int max = (i + 1) * 1000;
                if (max > length)
                    max = length;
                String[] stuId = ArrayUtils.subarray(studentIds, i * 1000, max);
                List<EmScoreInfo> list1 = emScoreInfoDao.findByStudent(examId, subjectId, subType, stuId);
                list.addAll(list1);
            }
        }

        Map<String, EmScoreInfo> stuScoreMap = new HashMap<String, EmScoreInfo>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (EmScoreInfo e : list) {
                stuScoreMap.put(e.getStudentId(), e);
            }
        }
        return stuScoreMap;
    }

    @Override
    public void saveAll(List<EmScoreInfo> saveList, Set<String> stuId, String subjectId,
                        String examId) {
        if (stuId != null && stuId.size() > 0) {
            String[] studentIds = stuId.toArray(new String[]{});
            if (studentIds.length <= 1000) {
                emScoreInfoDao.deleteByStudent(examId, subjectId, studentIds);

            } else {
                int length = studentIds.length;
                int cyc = length / 1000 + (length % 1000 == 0 ? 0 : 1);
                for (int i = 0; i < cyc; i++) {
                    int max = (i + 1) * 1000;
                    if (max > length)
                        max = length;
                    String[] stuIds = ArrayUtils.subarray(studentIds, i * 1000, max);
                    emScoreInfoDao.deleteByStudent(examId, subjectId, stuIds);
                }
            }

        }
        if (CollectionUtils.isNotEmpty(saveList)) {
            saveAllEntitys(saveList.toArray(new EmScoreInfo[]{}));
        }

    }

    @Override
    public void deleteBy(String examId, String subjectId) {
        emScoreInfoDao.deleteBy(examId, subjectId);
    }

    @Override
    public List<EmScoreInfo> saveAllEntitys(EmScoreInfo... emScoreInfo) {
        return emScoreInfoDao.saveAll(checkSave(emScoreInfo));
    }

    @Override
    public List<EmScoreInfo> findByExamIdAndUnitId(String examId, String unitId) {
        if (unitId != null) {
            return emScoreInfoDao.findByExamIdAndUnitId(examId, unitId);
        }
        return emScoreInfoDao.findByExamId(examId);
    }

    @Override
    public List<String> findExamIds(String[] examIds) {
        if (examIds != null && examIds.length > 0) {
            return emScoreInfoDao.findExamIds(examIds);
        }
        return new ArrayList<>();
    }

    @Override
    public void conversionCount(String unitId, String examId) {
        List<EmSubjectInfo> subInfos = subjectList(examId, unitId);
        if (CollectionUtils.isEmpty(subInfos)) {
            return;
        }
        List<EmConversion> cons = emConversionService.findByUnitId(unitId);
        if (CollectionUtils.isEmpty(cons)) {
            return;
        }
        for (EmSubjectInfo subInfo : subInfos) {
            List<EmScoreInfo> insertlist = new ArrayList<>();
            String subjectId = subInfo.getSubjectId();
            if (StringUtils.equals(subInfo.getInputType(), "G")) {
                //录入类型为等第的情况下不计算赋分
                continue;
            }
            List<EmScoreInfo> list = findByContion(unitId, null, null, examId, subjectId, "1", null);
            if (CollectionUtils.isEmpty(list)) {
                continue;
            }

            //排序
            //排名对应的学生
            Map<String, Set<String>> rankMap = new HashMap<>();
            Map<String, EmScoreInfo> map = new HashMap<>();
            int maxRank = 1;
            float oldScore = 0f;
            for (int i = 0; i < list.size(); i++) {
                map.put(list.get(i).getId(), list.get(i));
                if (i == 0) {
                    list.get(i).setRank(maxRank + "");
                    oldScore = NumberUtils.toFloat(list.get(i).getScore());
                    rankMap.put(maxRank + "", new HashSet<>());
                } else {
                    if (oldScore > NumberUtils.toFloat(list.get(i).getScore())) {
                        oldScore = NumberUtils.toFloat(list.get(i).getScore());
                        maxRank = i + 1;
                        rankMap.put(maxRank + "", new HashSet<>());
                    }
                    list.get(i).setRank(maxRank + "");
                }
                rankMap.get(maxRank + "").add(list.get(i).getId());
            }
            int countMax = list.size();//12
            //TODO 计算每一个赋分等级段内的人数
            List<EmScoreInfo> conInfos;
            int arrangeRank = 0;//已经被安排了的排名
            int blance = 0;//已经被分走的人数百分比
            int oldStuNumTo = 0;//已经被前面的等级分走的人数
            //计算赋分 (a-z)/(z-b) = (c-x)/(x-d)
            for (int i = 0; i < cons.size(); i++) {
                conInfos = new ArrayList<>();
                EmConversion con = cons.get(i);
                int conBlace = con.getBalance() + blance;
                int stuNumTo = Math.round((float) (countMax * conBlace) / 100);
                blance = con.getBalance() + blance;
                if (stuNumTo <= oldStuNumTo) {
                    continue;
                }
                String nowRank;
                for (; arrangeRank < maxRank; ) {
                    nowRank = (arrangeRank + 1) + "";
                    if (rankMap.containsKey(nowRank)) {
                        for (String ee : rankMap.get(nowRank)) {
                            EmScoreInfo e = map.get(ee);
                            conInfos.add(e);
                        }
                        oldStuNumTo += rankMap.get(nowRank).size();
                    }
                    arrangeRank++;
                    if (oldStuNumTo >= stuNumTo) {
                        break;
                    }
                }
                if (CollectionUtils.isNotEmpty(conInfos)) {
                    float a = NumberUtils.toFloat(conInfos.get(0).getScore());
                    float b = NumberUtils.toFloat(conInfos.get(conInfos.size() - 1).getScore());
                    float d = (float) con.getStartScore();
                    float c = (float) con.getEndScore();
                    for (EmScoreInfo em : conInfos) {
                        if (a == b) {
                            em.setConScore(con.getEndScore() + "");
                        } else {
                            float z = NumberUtils.toFloat(em.getScore());
                            int x = Math.round((z * c - b * c + a * d - d * z) / (a - b));
                            em.setConScore(x + "");
                        }
                        em.setScoreRank(con.getScoreRank() + "");
                        insertlist.add(em);
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(insertlist)) {
                this.saveAll(insertlist.toArray(new EmScoreInfo[]{}));
            }
        }
    }

    private List<EmSubjectInfo> subjectList(String examId, String unitId) {
        List<EmSubjectInfo> infoList = emSubjectInfoService.findByExamId(examId);
        List<EmSubjectInfo> elist = new ArrayList<>();
        List<Course> courseList73 = SUtils.dt(courseRemoteService.findByCodes73(unitId), new TR<List<Course>>() {
        });
        Set<String> id73Set = EntityUtils.getSet(courseList73, Course::getId);
        for (EmSubjectInfo sub : infoList) {
            if (StringUtils.isNotBlank(sub.getGkSubType()) && !StringUtils.equals(sub.getGkSubType(), "2") && id73Set.contains(sub.getSubjectId())) {
                elist.add(sub);
            }
        }
        return elist;
    }

    private List<EmScoreInfo> findByContion(String unitId, String acadyear, String semester,
                                            String examId, String subjectId, String gkSubType, Pagination page) {
        Specification<EmScoreInfo> specification = new Specification<EmScoreInfo>() {
            @Override
            public Predicate toPredicate(Root<EmScoreInfo> root, CriteriaQuery<?> cq,
                                         CriteriaBuilder cb) {
                List<Predicate> ps = Lists.newArrayList();
                ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
                ps.add(cb.equal(root.get("examId").as(String.class), examId));
                if (StringUtils.isNotBlank(acadyear)) {
                    ps.add(cb.equal(root.get("acadyear").as(String.class), acadyear));
                }
                if (StringUtils.isNotBlank(semester)) {
                    ps.add(cb.equal(root.get("semester").as(String.class), semester));
                }
                if (StringUtils.isNotBlank(subjectId)) {
                    ps.add(cb.equal(root.get("subjectId").as(String.class), subjectId));
                }
                if (StringUtils.isNotBlank(gkSubType)) {
                    ps.add(cb.equal(root.get("gkSubType").as(String.class), gkSubType));
                }
                ps.add(cb.notEqual(root.get("scoreStatus").as(String.class), "1"));
                ps.add(cb.gt(root.get("score").as(Float.class), 0));
                List<Order> orderList = new ArrayList<>();
                orderList.add(cb.desc(root.get("score").as(Float.class)));
                orderList.add(cb.desc(root.get("id").as(String.class)));
                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return cq.getRestriction();
            }
        };
        List<EmScoreInfo> list = new ArrayList<>();

        if (page != null) {
            list = findAll(specification, page);
        } else {
            list = findAll(specification);
        }
        return list;
    }

    @Override
    public List<EmScoreInfo> findOptionalCourseScore(String unitId, String[] teachClassIds) {
        return emScoreInfoDao.findByUnitIdAndExamIdAndTeachClassIdIn(unitId, BaseConstants.ZERO_GUID, teachClassIds);
    }

    @Override
    public List<EmScoreInfo> findOptionalCourseScore(String unitId, String subjectId, String teachClassId) {
        return emScoreInfoDao.findByUnitIdAndExamIdAndSubjectIdAndTeachClassId(unitId, BaseConstants.ZERO_GUID, subjectId, teachClassId);
    }

    @Override
    public void deleteByExamIdIn(String[] examIds) {
        emScoreInfoDao.deleteByExamIdIn(examIds);
    }

    @Override
    public List<EmScoreInfo> findByUnitIdAndExamIdAndSubjectId(String unitId, String examId, String subjectId) {
        return emScoreInfoDao.findByUnitIdAndExamIdAndSubjectId(unitId, examId, subjectId);
    }

    @Override
    public List<EmScoreInfoDto> findByXkConListDto(String unitId, String acadyear, String semester,
                                                   String examId, String subjectId, Pagination page) {
        if (StringUtils.isBlank(subjectId)) {
            return new ArrayList<>();
        }
        List<EmScoreInfo> list = findByContion(unitId, acadyear, semester, examId, subjectId, "1", page);
        List<EmScoreInfoDto> dtoList = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            return dtoList;
        }
        Set<String> stuIds = EntityUtils.getSet(list, EmScoreInfo::getStudentId);
        List<Student> stuList = SUtils.dt(studentRemoteService.findListByIds(stuIds.toArray(new String[0])), new TR<List<Student>>() {
        });
        Map<String, Student> stuMap = EntityUtils.getMap(stuList, Student::getId);
        Set<String> classIds = EntityUtils.getSet(stuList, Student::getClassId);
        Map<String, Clazz> clsMap = EntityUtils.getMap(SUtils.dt(classRemoteService.findByIdsSort(classIds.toArray(new String[0])), new TR<List<Clazz>>() {
        }), Clazz::getId);
        Set<String> teachClassIds = EntityUtils.getSet(list, EmScoreInfo::getTeachClassId);
        Map<String, TeachClass> teachClsMap = EntityUtils.getMap(SUtils.dt(teachClassService.findListByIds(teachClassIds.toArray(new String[0])), new TR<List<TeachClass>>() {
        }), TeachClass::getId);
        Map<String, String> numMap = new HashMap<>();
        numMap = emExamNumService.findByExamIdAndStudentIdIn(examId, stuIds.toArray(new String[0]));
//		List<EmPlaceStudent> nums = emPlaceStudentService.findByExamIdStuIds(examId, stuIds.toArray(new String[0]));
//		if(CollectionUtils.isNotEmpty(nums)) {
//			numMap = EntityUtils.getMap(nums, EmPlaceStudent::getStudentId, EmPlaceStudent::getExamNumber);
//		}
        EmScoreInfoDto dto;
        for (EmScoreInfo score : list) {
            dto = new EmScoreInfoDto();
            dto.setScoreInfo(score);
            if (stuMap.containsKey(score.getStudentId())) {
                dto.setStuName(stuMap.get(score.getStudentId()).getStudentName());
                if (clsMap.containsKey(stuMap.get(score.getStudentId()).getClassId())) {
                    dto.setClassName(clsMap.get(stuMap.get(score.getStudentId()).getClassId()).getClassNameDynamic());
                }
                dto.setStuCode(stuMap.get(score.getStudentId()).getStudentCode());
            }
            if (StringUtils.isNotBlank(score.getTeachClassId()) && teachClsMap.containsKey(score.getTeachClassId())) {
                dto.setTeachClassName(teachClsMap.get(score.getTeachClassId()).getName());
                ;
            }
            if (numMap.containsKey(score.getStudentId())) {
                dto.setExamNum(numMap.get(score.getStudentId()));
            }
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public List<EmScoreInfo> findByCondition(String unitId, String acadyear, String semester, String studentId, String examId) {
        Specification<EmScoreInfo> specification = new Specification<EmScoreInfo>() {
            @Override
            public Predicate toPredicate(Root<EmScoreInfo> root,
                                         CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();
                ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
                ps.add(cb.equal(root.get("acadyear").as(String.class), acadyear));
                ps.add(cb.equal(root.get("semester").as(String.class), semester));
                ps.add(cb.equal(root.get("studentId").as(String.class), studentId));
                if (StringUtils.isNotBlank(examId)) {
                    ps.add(cb.equal(root.get("examId").as(String.class), examId));
                }
                cq.where(ps.toArray(new Predicate[ps.size()]));
                return cq.getRestriction();
            }
        };
        List<EmScoreInfo> scoreInfoList = findAll(specification);
        return scoreInfoList;
    }

    @Override
    public int getEditRole(String examId, String classIdSearch,
                           String courseId, String unitId, String acadyear, String semester, String teacherId) {
        boolean noLimit = false;  //可以录分
        boolean normal = false;
        //判断录分总管理员权限
        List<String> teacherIdList = emNotLimitService.findTeacherIdByUnitId(unitId);
        if (teacherIdList.contains(teacherId)) {
            noLimit = true;
        }
        //判断普通录分人员权限
        ScoreLimitSearchDto searchDto = new ScoreLimitSearchDto();
        if (examId != null) {
            searchDto.setExamId(examId);
        } else {
            //选修课，从教学班获取学年学期
            searchDto.setExamId(Constant.GUID_ZERO);
        }

        searchDto.setAcadyear(acadyear);
        searchDto.setSemester(semester);
        searchDto.setUnitId(unitId);
        searchDto.setSubjectId(courseId);
        searchDto.setClassIds(new String[]{classIdSearch});
        searchDto.setTeacherId(teacherId);

        List<EmLimit> limitList = emLimitService.findBySearchDto(searchDto);
        if (CollectionUtils.isNotEmpty(limitList)) {
            normal = true;
        }

        if (noLimit && normal) {
            //同时具有两个权限
            return 2;
        } else if (noLimit) {
            //录分总管理员
            return 0;
        } else if (normal) {
            //普通录分人员
            return 1;
        } else {
            //没有录入权限
            return -1;
        }
    }
}