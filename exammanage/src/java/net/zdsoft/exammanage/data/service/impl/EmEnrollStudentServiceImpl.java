package net.zdsoft.exammanage.data.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.constant.ExammanageConstants;
import net.zdsoft.exammanage.data.dao.EmEnrollStudentDao;
import net.zdsoft.exammanage.data.entity.EmClassInfo;
import net.zdsoft.exammanage.data.entity.EmEnrollStuCount;
import net.zdsoft.exammanage.data.entity.EmEnrollStudent;
import net.zdsoft.exammanage.data.entity.EmFiltration;
import net.zdsoft.exammanage.data.service.*;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.*;

@Service("emEnrollStudentService")
public class EmEnrollStudentServiceImpl extends BaseServiceImpl<EmEnrollStudent, String> implements EmEnrollStudentService {

    @Autowired
    private EmEnrollStudentDao emEnrollStudentDao;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private EmExamInfoService emExamInfoService;
    @Autowired
    private EmClassInfoService emClassInfoService;
    @Autowired
    private EmFiltrationService emFiltrationService;
    @Autowired
    private EmEnrollStuCountService emEnrollStuCountService;
    @Autowired
    private ClassRemoteService classRemoteService;

    @Override
    protected BaseJpaRepositoryDao<EmEnrollStudent, String> getJpaDao() {
        return emEnrollStudentDao;
    }

    @Override
    protected Class<EmEnrollStudent> getEntityClass() {
        return EmEnrollStudent.class;
    }

    @Override
    public List<EmEnrollStudent> findByExamIdAndSchoolId(String examId,
                                                         String schoolId) {
        if (StringUtils.isNotBlank(schoolId)) {
            return emEnrollStudentDao.findByExamIdAndSchoolId(examId, schoolId);
        } else {
            return emEnrollStudentDao.findByExamId(examId);
        }
    }

    @Override
    public List<EmEnrollStudent> findByExamIdAndSchoolIdInClassIds(String examId, String schoolId, String[] classIds) {
        if (classIds == null || classIds.length == 0) {
            return findByExamIdAndSchoolId(examId, schoolId);
        } else {
            return emEnrollStudentDao.findByExamIdAndSchoolIdAndClassIdIn(examId, schoolId, classIds);
        }
    }

    @Override
    public List<EmEnrollStudent> findByExamIdAndSchoolIdAndStudentIdIn(String examId, String schoolId, String[] studentIds) {
        if (studentIds == null || studentIds.length == 0) {
            return findByExamIdAndSchoolId(examId, schoolId);
        } else {
            List<EmEnrollStudent> stuList = emEnrollStudentDao.findByExamIdAndSchoolId(examId, schoolId);
            List<EmEnrollStudent> lastList = new ArrayList<>();
            Set<String> stuIds = new HashSet<>();
            stuIds.addAll(Arrays.asList(studentIds));
            if (CollectionUtils.isNotEmpty(stuList)) {
                stuList.forEach(emtudent -> {
                    if (stuIds.contains(emtudent.getStudentId())) {
                        lastList.add(emtudent);
                    }
                });
            }
            return lastList;
        }
    }

    @Override
    public void deleteByExamIdAndUnitId(String examId, String unitId) {
        emEnrollStudentDao.deleteByExamIdAndUnitId(examId, unitId);
    }

    @Override
    public Map<String, Integer> getStudentAllCount(String[] examIds) {
        return emEnrollStudentDao.getStudentAllCount(examIds);
    }

    @Override
    public Map<String, Integer> getStudentPassNum(String[] examIds) {
        return emEnrollStudentDao.getStudentPassNum(examIds);
    }

    @Override
    public Map<String, Integer> getStudentAuditNum(String[] examIds) {
        return emEnrollStudentDao.getStudentAuditNum(examIds);
    }

    @Override
    public List<EmEnrollStudent> findByExamId(String examId) {
        return emEnrollStudentDao.findByExamId(examId);
    }

    @Override
    public List<EmEnrollStudent> findByExamIdAndHasPass(String examId, String hasPass) {
        return emEnrollStudentDao.findByExamIdAndHasPass(examId, hasPass);
    }

    @Override
    public void saveByIds(String examId, String state, String[] studentIds) {
        if (studentIds != null && studentIds.length > 0) {
            //报名学生
            List<EmEnrollStudent> emEnrollStudents = Lists.newArrayList();
            List<String> stuIdsList = Arrays.asList(studentIds);
            List<String> stuIdList = null;
            int num = 500;
            int len = studentIds.length;
            int size = len % num;
            if (size == 0) {
                size = len / num;
            } else {
                size = (len / num) + 1;
            }
            if (studentIds.length < 1000) {
                emEnrollStudents = emEnrollStudentDao.findByStudentIdIn(studentIds);
            } else {
                for (int i = 0; i < size; i++) {
                    int fromIndex = i * num;
                    int toIndex = fromIndex + num;
                    if (toIndex > len) {
                        toIndex = len;
                    }
                    stuIdList = stuIdsList.subList(fromIndex, toIndex);
                    emEnrollStudents.addAll(emEnrollStudentDao.findByStudentIdIn(stuIdList.toArray(new String[stuIdList.size()])));
                }
            }
            List<EmEnrollStuCount> counts = null;
            Set<String> schoolIds = EntityUtils.getSet(emEnrollStudents, "schoolId");
            Map<String, EmEnrollStuCount> countMap = Maps.newHashMap();
            //修改统计信息
            if ("allpass".equals(state)) {
                counts = emEnrollStuCountService.findByExamId(examId, null);
                for (EmEnrollStuCount count : counts) {
                    count.setPassNum(count.getSumCount());
                    count.setNotPassNum(0);
                }
            } else {
                counts = emEnrollStuCountService.findByExamIdAndSchoolIdIn(examId, schoolIds.toArray(new String[0]));
                for (EmEnrollStuCount count : counts) {
                    countMap.put(count.getExamId() + "_" + count.getSchoolId(), count);
                }
            }
            EmEnrollStuCount stuCount = null;
            Set<String> clazzIdSet = Sets.newHashSet();
            Map<String, String> schoolClazzMap = Maps.newHashMap();
            Map<String, List<String>> studentsMap = Maps.newHashMap();
            List<String> studentIdsList;
            for (EmEnrollStudent enrollStudent : emEnrollStudents) {
                if (studentsMap.containsKey(enrollStudent.getClassId())) {
                    studentIdsList = studentsMap.get(enrollStudent.getClassId());
                } else {
                    studentIdsList = Lists.newArrayList();
                }
                studentIdsList.add(enrollStudent.getStudentId());
                studentsMap.put(enrollStudent.getClassId(), studentIdsList);
                clazzIdSet.add(enrollStudent.getClassId());
                schoolClazzMap.put(enrollStudent.getClassId(), enrollStudent.getSchoolId());
                stuCount = countMap.get(enrollStudent.getExamId() + "_" + enrollStudent.getSchoolId());
                if (stuCount == null) {
                    continue;
                }
                if ("fail".equals(state)) {
                    if (ExammanageConstants.ENROLL_STU_PASS_1.equals(enrollStudent.getHasPass())) {
                        stuCount.setPassNum(stuCount.getPassNum() - 1);
                    }
                    if (!ExammanageConstants.ENROLL_STU_PASS_2.equals(enrollStudent.getHasPass())) {
                        stuCount.setNotPassNum(stuCount.getNotPassNum() + 1);
                    }
                } else if ("pass".equals(state)) {
                    if (ExammanageConstants.ENROLL_STU_PASS_2.equals(enrollStudent.getHasPass())) {
                        stuCount.setNotPassNum(stuCount.getNotPassNum() - 1);
                    }
                    if (!ExammanageConstants.ENROLL_STU_PASS_1.equals(enrollStudent.getHasPass())) {
                        stuCount.setPassNum(stuCount.getPassNum() + 1);
                    }
                }
                countMap.put(enrollStudent.getExamId() + "_" + enrollStudent.getSchoolId(), stuCount);
            }

            if (!"allpass".equals(state)) {
                counts = Lists.newArrayList();
                for (Map.Entry<String, EmEnrollStuCount> entry : countMap.entrySet()) {
                    counts.add(entry.getValue());
                }
            }
            emEnrollStuCountService.saveAll(counts.toArray(new EmEnrollStuCount[counts.size()]));

            //考试班级
            List<EmClassInfo> classInfoList = emClassInfoService.findByExamId(examId);
            Set<String> emClassSet = EntityUtils.getSet(classInfoList, "classId");
            List<EmFiltration> fList = new ArrayList<EmFiltration>();
            EmFiltration emFiltration = null;
            if ("fail".equals(state)) {
                //修改审核状态
                emEnrollStudentDao.updateforHaspass(ExammanageConstants.ENROLL_STU_PASS_2, examId, studentIds);
                //添加不排考学生
                clazzIdSet.retainAll(emClassSet);
                for (String str : clazzIdSet) {
                    List<String> examStuIds = studentsMap.get(str);
                    for (String stuId : examStuIds) {
                        emFiltration = new EmFiltration();
                        emFiltration.setId(UuidUtils.generateUuid());
                        emFiltration.setExamId(examId);
                        emFiltration.setSchoolId(schoolClazzMap.get(str));
                        emFiltration.setStudentId(stuId);
                        emFiltration.setType(ExammanageConstants.FILTER_TYPE1);
                        fList.add(emFiltration);
                    }
                }
                if (CollectionUtils.isNotEmpty(fList)) {
                    emFiltrationService.saveAll(fList.toArray(new EmFiltration[fList.size()]));
                }
            } else {
                //修改审核状态
                if (studentIds.length < 1000) {
                    emEnrollStudentDao.updateforHaspass(ExammanageConstants.ENROLL_STU_PASS_1, examId, studentIds);
                } else {
                    for (int i = 0; i < size; i++) {
                        int fromIndex = i * num;
                        int toIndex = fromIndex + num;
                        if (toIndex > len) {
                            toIndex = len;
                        }
                        stuIdList = stuIdsList.subList(fromIndex, toIndex);
                        emEnrollStudentDao.updateforHaspass(ExammanageConstants.ENROLL_STU_PASS_1, examId, stuIdList.toArray(new String[stuIdList.size()]));
                    }
                }
                //通过学生ids
                List<String> passStudentIds = Lists.newArrayList();
                //添加考试班级
                clazzIdSet.removeAll(emClassSet);
                List<EmClassInfo> clazzInfoList = Lists.newArrayList();
                EmClassInfo clazzInfo = null;
                for (String clazzId : clazzIdSet) {
                    clazzInfo = new EmClassInfo();
                    clazzInfo.setId(UuidUtils.generateUuid());
                    clazzInfo.setExamId(examId);
                    clazzInfo.setClassId(clazzId);
                    clazzInfo.setSchoolId(schoolClazzMap.get(clazzId));
                    clazzInfo.setClassType("1");//行政班
                    clazzInfoList.add(clazzInfo);
                    passStudentIds.addAll(studentsMap.get(clazzId));
                }
                emClassInfoService.saveAll(clazzInfoList.toArray(new EmClassInfo[clazzInfoList.size()]));
                //删除不排考学生信息
                List<String> delStuIds = Lists.newArrayList();
                for (String emclazzId : emClassSet) {
                    List<String> examStuIds = studentsMap.get(emclazzId);
                    if (CollectionUtils.isNotEmpty(examStuIds)) {
                        delStuIds.addAll(examStuIds);
                    }
                }
                if (CollectionUtils.isNotEmpty(delStuIds)) {
                    if (delStuIds.size() < 1000) {
                        emFiltrationService.deleteByExamIdAndStudentIdIn(examId, ExammanageConstants.FILTER_TYPE1, delStuIds.toArray(new String[delStuIds.size()]));
                    } else {
                        List<String> delStuIdList = null;
                        int dellen = delStuIds.size();
                        int delsize = dellen % num;
                        if (delsize == 0) {
                            delsize = dellen / num;
                        } else {
                            delsize = (dellen / num) + 1;
                        }
                        for (int i = 0; i < delsize; i++) {
                            int fromIndex = i * num;
                            int toIndex = fromIndex + num;
                            if (toIndex > dellen) {
                                toIndex = dellen;
                            }
                            delStuIdList = delStuIds.subList(fromIndex, toIndex);
                            emFiltrationService.deleteByExamIdAndStudentIdIn(examId, ExammanageConstants.FILTER_TYPE1, delStuIdList.toArray(new String[delStuIdList.size()]));
                        }
                    }
                }
                //添加的考试班级其他学生设置为不排考学生
                List<Student> otherStudents = SUtils.dt(studentRemoteService.findByClassIds(clazzIdSet.toArray(new String[0])), new TR<List<Student>>() {
                });
                Iterator<Student> it = otherStudents.iterator();
                while (it.hasNext()) {
                    Student student = (Student) it.next();
                    if (passStudentIds.contains(student.getId())) {
                        it.remove();
                    }
                }
                if (CollectionUtils.isNotEmpty(otherStudents)) {
                    for (Student stu : otherStudents) {
                        emFiltration = new EmFiltration();
                        emFiltration.setId(UuidUtils.generateUuid());
                        emFiltration.setExamId(examId);
                        emFiltration.setSchoolId(stu.getSchoolId());
                        emFiltration.setStudentId(stu.getId());
                        emFiltration.setType(ExammanageConstants.FILTER_TYPE1);
                        fList.add(emFiltration);
                    }
                    emFiltrationService.saveAll(fList.toArray(new EmFiltration[fList.size()]));
                }
            }
        }
    }

    @Override
    public List<Clazz> findClsByExamIdAndSchId(String acadyear, String examId, String schoolId) {
        List<Clazz> clslist = new ArrayList<>();
        Set<String> clsIds = emEnrollStudentDao.findClsIdByExamIdAndSchoolId(examId, schoolId);
        if (CollectionUtils.isEmpty(clsIds)) {
            return clslist;
        }
        clslist = SUtils.dt(classRemoteService.findClassListByIds(clsIds.toArray(new String[0])), new TR<List<Clazz>>() {
        });
        return clslist;
    }

    @Override
    public List<EmEnrollStudent> findByStuIdAndState(String stuId, String state) {
        return emEnrollStudentDao.findByStuIdAndState(stuId, state);
    }

    @Override
    public List<EmEnrollStudent> findByIdsAndstate(String examId, String schoolId, String classId, String state, Pagination page) {
        Specification<EmEnrollStudent> specification = new Specification<EmEnrollStudent>() {
            @Override
            public Predicate toPredicate(Root<EmEnrollStudent> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();
                ps.add(cb.equal(root.get("examId").as(String.class), examId));
                if (StringUtils.isNotBlank(classId)) {
                    ps.add(cb.equal(root.get("classId").as(String.class), classId));
                }
                if (!"all".equals(schoolId)) {
                    ps.add(cb.equal(root.get("schoolId").as(String.class), schoolId));
                }

                if (!"all".equals(state)) {
                    ps.add(cb.equal(root.get("hasPass").as(String.class), state));
                }
                List<Order> orderList = new ArrayList<Order>();
                orderList.add(cb.asc(root.get("schoolId").as(String.class)));
                orderList.add(cb.asc(root.get("classId").as(String.class)));
                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return cq.getRestriction();
            }
        };
        if (page != null) {
            Pageable pageable = Pagination.toPageable(page);
            Page<EmEnrollStudent> findAll = emEnrollStudentDao.findAll(specification, pageable);
            page.setMaxRowCount((int) findAll.getTotalElements());
            List<EmEnrollStudent> emEnrollStudents = findAll.getContent();
            return emEnrollStudents;
        } else {
            return emEnrollStudentDao.findAll(specification);
        }
    }

    @Override
    public List<EmEnrollStudent> findByIdsAndGood(String examId,
                                                  String schoolId, String hasGood, Pagination page) {
        Specification<EmEnrollStudent> specification = new Specification<EmEnrollStudent>() {
            @Override
            public Predicate toPredicate(Root<EmEnrollStudent> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();
                ps.add(cb.equal(root.get("examId").as(String.class), examId));
                ps.add(cb.equal(root.get("hasPass").as(String.class), "1"));

                if (!"all".equals(schoolId)) {
                    ps.add(cb.equal(root.get("schoolId").as(String.class), schoolId));
                }

                if (!"all".equals(hasGood)) {
                    ps.add(cb.equal(root.get("hasGood").as(String.class), hasGood));
                }
                List<Order> orderList = new ArrayList<Order>();
                orderList.add(cb.asc(root.get("schoolId").as(String.class)));
                orderList.add(cb.asc(root.get("classId").as(String.class)));
                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return cq.getRestriction();
            }
        };
        if (page != null) {
            Pageable pageable = Pagination.toPageable(page);
            Page<EmEnrollStudent> findAll = emEnrollStudentDao.findAll(specification, pageable);
            page.setMaxRowCount((int) findAll.getTotalElements());
            List<EmEnrollStudent> emEnrollStudents = findAll.getContent();
            return emEnrollStudents;
        } else {
            return emEnrollStudentDao.findAll(specification);
        }
    }

    @Override
    public List<EmEnrollStudent> findByStudentIdIn(String[] studentIds) {
        return emEnrollStudentDao.findByStudentIdIn(studentIds);
    }

    @Override
    public Map<String, List<EmEnrollStudent>> findMapByExamIds(String[] examIds) {
        List<EmEnrollStudent> emEnrollStudentList = emEnrollStudentDao.findByExamIdIn(examIds);
        Map<String, List<EmEnrollStudent>> map = new HashMap<String, List<EmEnrollStudent>>();
        if (CollectionUtils.isNotEmpty(emEnrollStudentList)) {
            for (EmEnrollStudent emEnrollStudent : emEnrollStudentList) {
                if (!map.containsKey(emEnrollStudent.getExamId())) {
                    map.put(emEnrollStudent.getExamId(), new ArrayList<EmEnrollStudent>());
                }
                map.get(emEnrollStudent.getExamId()).add(emEnrollStudent);
            }
        }
        return map;
    }

    @Override
    public Map<String, Integer> findByExamIdAndHasPassAndHasGood(String examId,
                                                                 String hasPass, String hasGood) {
        return emEnrollStudentDao.findByExamIdAndHasPassAndHasGood(examId, hasPass, hasGood);
    }

    @Override
    public List<EmEnrollStudent> findMapByExamIdsAndStudentId(String[] examIds, String studentId) {
        return emEnrollStudentDao.findByStudentIdAndExamIdIn(studentId, examIds);
    }
}
