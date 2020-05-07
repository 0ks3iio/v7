package net.zdsoft.exammanage.data.service.impl;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.CourseRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.constant.ExammanageConstants;
import net.zdsoft.exammanage.data.dao.EmPlaceStudentDao;
import net.zdsoft.exammanage.data.dto.EmStudentDto;
import net.zdsoft.exammanage.data.dto.EmTicketDto;
import net.zdsoft.exammanage.data.entity.*;
import net.zdsoft.exammanage.data.service.*;
import net.zdsoft.exammanage.data.utils.ExamUtils;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("emPlaceStudentService")
public class EmPlaceStudentServiceImpl extends BaseServiceImpl<EmPlaceStudent, String> implements EmPlaceStudentService {

    @Autowired
    private EmPlaceStudentDao emPlaceStudentDao;
    @Autowired
    private EmPlaceService emPlaceService;
    @Autowired
    private EmExamNumService emExamNumService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private EmOptionService emOptionService;
    @Autowired
    private EmExamRegionService emExamRegionService;
    @Autowired
    private SchoolRemoteService schoolRemoteService;
    @Autowired
    private EmEnrollStudentService emEnrollStudentService;
    @Autowired
    private EmFiltrationService emFiltrationService;
    @Autowired
    private EmClassInfoService emClassInfoService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private EmSubGroupService emSubGroupService;
    @Autowired
    private EmPlaceGroupService emPlaceGroupService;
    @Autowired
    private EmStudentGroupService emStudentGroupService;
    @Autowired
    private EmSubjectInfoService emSubjectInfoService;
    @Autowired
    private CourseRemoteService courseRemoteService;

    @Override
    protected BaseJpaRepositoryDao<EmPlaceStudent, String> getJpaDao() {
        return emPlaceStudentDao;
    }

    @Override
    protected Class<EmPlaceStudent> getEntityClass() {
        return EmPlaceStudent.class;
    }

    @Override
    public List<EmPlaceStudent> saveAllEntitys(EmPlaceStudent... emPlaceStudent) {
        return emPlaceStudentDao.saveAll(checkSave(emPlaceStudent));
    }

    @Override
    public List<EmPlaceStudent> findByExamId(String examId) {
        return emPlaceStudentDao.findByExamIds(new String[]{examId});
    }

    @Override
    public Set<String> getClsSchMap(String examId) {
        return emPlaceStudentDao.getClsSchMap(examId);
    }

    @Override
    public Set<String> getExamIds(String[] examIds) {
        return emPlaceStudentDao.getExamIds(examIds);
    }

    @Override
    public Map<String, Integer> getAllCountMap(String[] examIds) {
        return emPlaceStudentDao.getAllCountMap(examIds);
    }

    @Override
    public Map<String, Integer> getCountMapByPlaceIds(String examId, String groupId, Set<String> emPlaceIds) {
        return emPlaceStudentDao.getCountMapByPlaceIds(examId, groupId, emPlaceIds.toArray(new String[0]));
    }

    @Override
    public Set<String> getPlaceIdSet(String examId) {
        return emPlaceStudentDao.getPlaceIdSet(examId);
    }

    @Override
    public List<EmPlaceStudent> findByNewExamPlaceIdAndGroupId(String groupId, String... examPlaceId) {
        List<EmPlaceStudent> list = new ArrayList<>();
        if (StringUtils.isNotBlank(groupId)) {
            list = emPlaceStudentDao.findByGroupAndExamPlaceId(new String[]{groupId}, examPlaceId);
        } else {
            list = emPlaceStudentDao.findByExamPlaceId(examPlaceId);
        }
        return list;
    }

    public List<EmPlaceStudent> findByGroupAndExamPlaceId(String groupId, String... examPlaceId) {
        if (examPlaceId != null && examPlaceId.length > 0) {
            List<EmPlaceStudent> emPlaceStudentList = emPlaceStudentDao.findByGroupAndExamPlaceId(new String[]{groupId}, examPlaceId);
            if (CollectionUtils.isNotEmpty(emPlaceStudentList)) {
                //设置学生信息 如姓名
                Set<String> studentIds = emPlaceStudentList.stream().map(EmPlaceStudent::getStudentId).collect(Collectors.toSet());
                List<Student> studentList = SUtils.dt(studentRemoteService.findListByIds(studentIds.toArray(new String[0])), new TR<List<Student>>() {
                });
                Set<String> schIds = studentList.stream().map(Student::getSchoolId).collect(Collectors.toSet());
                Map<String, School> schMap = SUtils.dt(schoolRemoteService.findListByIds(schIds.toArray(new String[0])), new TR<List<School>>() {
                }).stream().collect(Collectors.toMap(School::getId, Function.identity()));
                Map<String, Student> studentMap = studentList.stream().collect(Collectors.toMap(Student::getId, Function.identity()));
                for (EmPlaceStudent emPlaceStudent : emPlaceStudentList) {
                    Student student = studentMap.get(emPlaceStudent.getStudentId());
                    if (student != null) {
                        emPlaceStudent.setStudentName(student.getStudentName());
                        emPlaceStudent.setStudentFilePath(ExamUtils.showPicUrl(student.getDirId(), student.getFilePath(), student.getSex()));
                        emPlaceStudent.setSchoolId(student.getSchoolId());
                        if (schMap.containsKey(student.getSchoolId())) {
                            emPlaceStudent.setSchName(schMap.get(student.getSchoolId()).getSchoolName());
                        }

                    }
                }
            }
            return emPlaceStudentList;
        }
        return new ArrayList<EmPlaceStudent>();
    }

    @Override
    public List<EmPlaceStudent> findByExamPlaceId(String examPlaceId) {
        List<EmPlaceStudent> emPlaceStudentList = emPlaceStudentDao.findByExamPlaceId(new String[]{examPlaceId});
        return emPlaceStudentList;
    }

    @Override
    public List<EmPlaceStudent> findByExamPlaceIds(String[] examPlaceIds) {
        List<EmPlaceStudent> list = emPlaceStudentDao.findByExamPlaceIds(examPlaceIds);
        return list;
    }

    @Override
    public Map<String, String> findByExamIdAndStuIds(String examId, String[] stuIds) {
        List<EmPlaceStudent> list = emPlaceStudentDao.findByExamIdAndStuIds(examId, stuIds);
        return EntityUtils.getMap(list, EmPlaceStudent::getStudentId, EmPlaceStudent::getExamNumber);
    }

    @Override
    public List<EmPlaceStudent> findByExamPlaceIdAndGroupId(String groupId, String... examPlaceId) {
        if (examPlaceId != null && examPlaceId.length > 0) {
            List<EmPlaceStudent> emPlaceStudentList = new ArrayList<>();
            if (StringUtils.isNotBlank(groupId)) {
                emPlaceStudentList = emPlaceStudentDao.findByGroupAndExamPlaceId(new String[]{groupId}, examPlaceId);
            } else {
                emPlaceStudentList = emPlaceStudentDao.findByExamPlaceId(examPlaceId);
            }
            if (CollectionUtils.isNotEmpty(emPlaceStudentList)) {
                //设置学生信息 如姓名
                Set<String> studnetIds = EntityUtils.getSet(emPlaceStudentList, EmPlaceStudent::getStudentId);
                List<Student> studentList = SUtils.dt(studentRemoteService.findListByIds(studnetIds.toArray(new String[0])), new TR<List<Student>>() {
                });
                Set<String> schIds = EntityUtils.getSet(studentList, Student::getSchoolId);
                Map<String, School> schMap = EntityUtils.getMap(SUtils.dt(schoolRemoteService.findListByIds(schIds.toArray(new String[0])), new TR<List<School>>() {
                }), School::getId);
                Map<String, Student> studentMap = EntityUtils.getMap(studentList, Student::getId);
                for (EmPlaceStudent emPlaceStudent : emPlaceStudentList) {
                    Student student = studentMap.get(emPlaceStudent.getStudentId());
                    if (student != null) {
                        emPlaceStudent.setStudentName(student.getStudentName());
                        emPlaceStudent.setStudentFilePath(ExamUtils.showPicUrl(student.getDirId(), student.getFilePath(), student.getSex()));
                        emPlaceStudent.setSchoolId(student.getSchoolId());
                        if (schMap.containsKey(student.getSchoolId())) {
                            emPlaceStudent.setSchName(schMap.get(student.getSchoolId()).getSchoolName());
                        }

                    }
                }
            }
            return emPlaceStudentList;
        }
        return new ArrayList<EmPlaceStudent>();
    }

    @Override
    public List<EmPlaceStudent> findByExamIdAndSchoolIdAndGroupIdIn(String examId, String schoolId, String[] groupIds) {
        List<EmPlace> emPlaceList = emPlaceService.findByExamIdAndSchoolIdWithMaster(examId, schoolId, false);
        if (CollectionUtils.isNotEmpty(emPlaceList)) {
            Set<String> ids = emPlaceList.stream().map(EmPlace::getId).collect(Collectors.toSet());
            List<EmPlaceStudent> list = new ArrayList<>();
            if (groupIds == null || groupIds.length == 0) {
                list = findByExamPlaceIdAndGroupId(null, ids.toArray(new String[]{}));
            } else {
                list = emPlaceStudentDao.findByGroupAndExamPlaceId(groupIds, ids.toArray(new String[0]));
            }
            if (CollectionUtils.isNotEmpty(list)) {
                return list;
            }
        }
        return new ArrayList<EmPlaceStudent>();
    }

    @Override
    public List<EmPlaceStudent> findByExamIdAndSchoolIdAndGroupId(String examId,
                                                                  String schoolId, String groupId) {
        List<EmPlace> emPlaceList = emPlaceService.findByExamIdAndSchoolIdWithMaster(examId, schoolId, false);
        if (CollectionUtils.isNotEmpty(emPlaceList)) {
            Set<String> ids = emPlaceList.stream().map(EmPlace::getId).collect(Collectors.toSet());
            List<EmPlaceStudent> list = new ArrayList<>();
            if (StringUtils.isBlank(groupId)) {
                list = findByExamPlaceIdAndGroupId(null, ids.toArray(new String[]{}));
            } else {
                list = findByGroupAndExamPlaceId(groupId, ids.toArray(new String[]{}));
            }
            if (CollectionUtils.isNotEmpty(list)) {
                return list;
            }
        }
        return new ArrayList<EmPlaceStudent>();
    }

    @Override
    public boolean hasStudent(String examId, String unitId) {
        List<EmPlace> emPlaceList = emPlaceService.findByExamIdAndSchoolIdWithMaster(examId, unitId, false);
        if (CollectionUtils.isNotEmpty(emPlaceList)) {
            Set<String> ids = emPlaceList.stream().map(EmPlace::getId).collect(Collectors.toSet());
            int count = emPlaceStudentDao.getCountByPlaceIds(ids.toArray(new String[0]));
            if (count > 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<EmPlaceStudent> findByExamIdAndGroupId(String examId, String emSubGroupId) {
        return emPlaceStudentDao.findByExamIdAndGroupId(examId, emSubGroupId);
    }

    @Transactional
    public void insertList(Set<String> emPlaceIds,
                           List<EmPlaceStudent> emStudentList, List<EmExamNum> examNumList) {
        if (CollectionUtils.isNotEmpty(examNumList)) {
            emExamNumService.addOrDel(examNumList, null, null);
        }
        if (CollectionUtils.isNotEmpty(emPlaceIds)) {
            emPlaceStudentDao.deleteByExamPlaceIdIn(emPlaceIds.toArray(new String[0]));
        }
        if (CollectionUtils.isNotEmpty(emStudentList)) {
            saveAllEntitys(emStudentList.toArray(new EmPlaceStudent[]{}));
        }
    }

    @Override
    public void deleteByExamId(String examId) {
        emPlaceStudentDao.deleteByExamId(examId);
    }

    @Override
    public void deleteByExamId(String examId, String schoolId) {
        List<EmPlace> list = emPlaceService.findByExamIdAndSchoolIdWithMaster(examId, schoolId, false);
        if (CollectionUtils.isNotEmpty(list)) {
            Set<String> ids = EntityUtils.getSet(list, "id");
            deleteByEmPlaceIds(ids.toArray(new String[]{}));
        }
    }

    @Override
    public void deleteByEmPlaceIds(String... emPlaceIds) {
        emPlaceStudentDao.deleteByExamPlaceIdIn(emPlaceIds);
    }

    @Override
    public Map<String, List<EmPlaceStudent>> findMapByExamIds(String[] examIds) {
        List<EmPlaceStudent> placeStudentList = emPlaceStudentDao.findByExamIds(examIds);
        Map<String, List<EmPlaceStudent>> map = new HashMap<String, List<EmPlaceStudent>>();
        if (CollectionUtils.isNotEmpty(placeStudentList)) {
            for (EmPlaceStudent emPlaceStudent : placeStudentList) {
                if (!map.containsKey(emPlaceStudent.getExamId())) {
                    map.put(emPlaceStudent.getExamId(), new ArrayList<EmPlaceStudent>());
                }
                map.get(emPlaceStudent.getExamId()).add(emPlaceStudent);
            }
        }
        return map;
    }

    @Override
    public List<EmPlaceStudent> findByExamIdStuIds(String examId,
                                                   String[] stuids) {
        if (stuids == null || stuids.length <= 0) {
            return new ArrayList<EmPlaceStudent>();
        }
        Set<String> set = new HashSet<>();
        for (String m : stuids) {
            if (m == null)
                continue;
            set.add(m);
        }
        if (CollectionUtils.isEmpty(set)) {
            return new ArrayList<>();
        }
        Specification<EmPlaceStudent> s = new Specification<EmPlaceStudent>() {
            @Override
            public Predicate toPredicate(Root<EmPlaceStudent> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<>();
                queryIn("studentId", set.toArray(), root, ps, null);
                Predicate inPre = cb.or(ps.toArray(new Predicate[0]));
                return cb.and(inPre, cb.equal(root.<String>get("examId"), examId));
            }

        };
        return getJpaDao().findAll(s);

//		return emPlaceStudentDao.findByExamIdStuIds(examId, stuids);
    }

    @Override
    public List<EmTicketDto> findByExamIdStuIds(String examId, List<Student> stus) {
        List<EmTicketDto> dtos = new ArrayList<>();
        Map<String, Student> stuMap = EntityUtils.getMap(stus, "id");
        List<EmPlace> places = emPlaceService.findByExamId(examId);
        Map<String, EmPlace> placeMap = EntityUtils.getMap(places, "id");
        Map<String, EmOption> optionMap = EntityUtils.getMap(emOptionService.findByExamIdWithMaster(examId), "id");
        Map<String, EmRegion> regionMap = EntityUtils.getMap(emExamRegionService.findByExamId(examId), "id");
        List<EmPlaceStudent> emStus = emPlaceStudentDao.findByExamIdStuIds(examId, stuMap.keySet().toArray(new String[0]));
        EmTicketDto dto;
        for (EmPlaceStudent emStu : emStus) {
            dto = new EmTicketDto();
            EmPlace place = placeMap.get(emStu.getExamPlaceId());
            EmOption option = optionMap.get(place.getOptionId());
            EmRegion region = regionMap.get(option.getExamRegionId());
            Student stu = stuMap.get(emStu.getStudentId());
            dto.setSeatNum(emStu.getSeatNum());
            dto.setExamNumber(emStu.getExamNumber());
            dto.setExamPlace(place.getExamPlaceCode());
            dto.setExamOption(option.getId());
            dto.setExamOptionName(option.getOptionName());
            dto.setExamRegion(region.getId());
            dto.setExamRegionName(region.getRegionName());
            dto.setExamPlaceAdd(option.getOptionAdd());
            dto.setName(stu.getStudentName());
            dto.setUnitiveCode(stu.getUnitiveCode());
            dto.setStudentFilePath(ExamUtils.showPicUrl(stu.getDirId(), stu.getFilePath(), stu.getSex()));
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public List<EmPlaceStudent> findByExamIdAndPlaceId(String examId,
                                                       String placeId) {
        return emPlaceStudentDao.findByExamIdAndPlaceId(examId, placeId);
    }

    @Override
    public List<EmPlaceStudent> findByExamIdAndPlaceIdAndGroupId(String examId, String placeId, String groupId) {
        return emPlaceStudentDao.findByGroupAndExamPlaceId(new String[]{groupId}, new String[]{placeId});
    }

    /**
     * @param examId
     * @param unitId
     * @param flag   是否排除不排考
     * @return
     */
    private List<Student> findGkStudentByExamId(String examId, String unitId, String searchClassId, boolean flag, Map<String, String> classMap) {
        List<Student> studentList = new ArrayList<>();
        Set<String> classIds = new HashSet<>();
        if (StringUtils.isNotBlank(searchClassId)) {
            classIds.add(searchClassId);
        } else {
            List<EmClassInfo> classInfoList = emClassInfoService.findByExamIdAndSchoolId(examId, unitId);
            if (CollectionUtils.isNotEmpty(classInfoList)) {
                //暂不考虑教学班
                classIds = EntityUtils.getSet(classInfoList, "classId");
            }
        }
        if (CollectionUtils.isNotEmpty(classIds)) {
            List<EmEnrollStudent> emStus = emEnrollStudentService.findByExamIdAndSchoolIdInClassIds(examId, unitId, classIds.toArray(new String[0]));
            Set<String> stuIds = EntityUtils.getSet(emStus, "studentId");
            studentList = SUtils.dt(studentRemoteService.findListByIds(stuIds.toArray(new String[]{})), new TR<List<Student>>() {
            });
            if (classMap != null) {
                List<Clazz> classList = SUtils.dt(classRemoteService.findClassListByIds(classIds.toArray(new String[]{})), new TR<List<Clazz>>() {
                });
                if (CollectionUtils.isNotEmpty(classList)) {
                    for (Clazz z : classList) {
                        classMap.put(z.getId(), z.getClassNameDynamic());
                    }
                }
            }
        }
        if (flag) {
            Map<String, String> filterMap = emFiltrationService.findByExamIdAndSchoolIdAndType(examId, unitId, ExammanageConstants.FILTER_TYPE1);
            List<Student> newstudentList = new ArrayList<Student>();
            for (Student s : studentList) {
                if (!filterMap.containsKey(s.getId())) {
                    newstudentList.add(s);
                }
            }
            return newstudentList;
        }
        return studentList;
    }

    /**
     * @param examId
     * @param unitId
     * @param searchClassId
     * @param flag              是否排除不排考
     * @param classMap//用于之后的命名 为null 不进行返回
     * @return
     */
    private List<Student> findStudentByExamId(String examId, String unitId, String searchClassId, boolean flag, Map<String, String> classMap) {
        List<Student> studentList = new ArrayList<>();
        Set<String> classIds = new HashSet<>();
        if (StringUtils.isNotBlank(searchClassId)) {
            classIds.add(searchClassId);
        } else {
            List<EmClassInfo> classInfoList = emClassInfoService.findByExamIdAndSchoolId(examId, unitId);
            if (CollectionUtils.isNotEmpty(classInfoList)) {
                //暂不考虑教学班
                classIds = EntityUtils.getSet(classInfoList, "classId");
            }
        }
        if (CollectionUtils.isNotEmpty(classIds)) {
            studentList = SUtils.dt(studentRemoteService.findByClassIds(classIds.toArray(new String[]{})), new TR<List<Student>>() {
            });
            if (classMap != null) {
                List<Clazz> classList = SUtils.dt(classRemoteService.findClassListByIds(classIds.toArray(new String[]{})), new TR<List<Clazz>>() {
                });
                if (CollectionUtils.isNotEmpty(classList)) {
                    for (Clazz z : classList) {
                        classMap.put(z.getId(), z.getClassNameDynamic());
                    }
                }
            }

        }
        if (flag) {
            Map<String, String> filterMap = emFiltrationService.findByExamIdAndSchoolIdAndType(examId, unitId, ExammanageConstants.FILTER_TYPE1);
            List<Student> newstudentList = new ArrayList<Student>();
            for (Student s : studentList) {
                if (!filterMap.containsKey(s.getId())) {
                    newstudentList.add(s);
                }
            }
            return newstudentList;
        }
        return studentList;
    }

    private int makeStuNum(Set<String> stuNum, int index, String prex) {
        while (true) {
            String rr = StringUtils.leftPad(index + "", 4, '0');
            if (index > 9999) {
                rr = index + "";
            }
            rr = prex + rr;
            if (!stuNum.contains(rr)) {
                break;
            }
            index++;
        }
        return index;
    }

    private void autoByRandomOne(List<EmStudentDto> dtoList, List<EmPlace> emPlaceList, List<EmPlaceStudent> emStudentList) {
        Collections.shuffle(dtoList);
        EmPlaceStudent emStudent;
        int index = 0;
        for (EmPlace em : emPlaceList) {
            int seat = em.getCount();
            for (int i = 1; i < seat + 1; i++) {
                if (dtoList.size() <= index) {
                    break;
                }
                EmStudentDto dto = dtoList.get(index);
                emStudent = new EmPlaceStudent();
                emStudent.setId(UuidUtils.generateUuid());
                emStudent.setExamNumber(dto.getExamNumber());
                emStudent.setStudentId(dto.getStudentId());
                emStudent.setExamPlaceId(em.getId());
                emStudent.setExamId(em.getExamId());
                String ii = StringUtils.leftPad(i + "", 2, '0');
                emStudent.setSeatNum(ii);
                emStudentList.add(emStudent);
                index++;
            }
        }
    }

    //按照考号大小排序
    private void autoByExamNumOne(List<EmStudentDto> dtoList, List<EmPlace> emPlaceList, List<EmPlaceStudent> emStudentList) {
        //按考号从大到小
        Collections.sort(dtoList, new Comparator<EmStudentDto>() {
            @Override
            public int compare(EmStudentDto o1, EmStudentDto o2) {
                if (o1.getExamNumber() == null) {
                    return -1;
                }
                if (o2.getExamNumber() == null) {
                    return -1;
                }
                return o1.getExamNumber().compareTo(o2.getExamNumber());
            }
        });
        EmPlaceStudent emStudent = new EmPlaceStudent();
        int iii = 0;
        int allCount = dtoList.size();
        //自动排座位号
        for (EmPlace em : emPlaceList) {
            Integer count = em.getCount();
            if (count == null || count <= 0) {
                continue;
            }
            int begin = iii;
            int end = iii + count;

            if (allCount <= begin) {
                break;
            }
            if (allCount <= end) {
                end = allCount;
            }
            List<EmStudentDto> ssList = dtoList.subList(begin, end);
            int zz = 0;
            for (EmStudentDto s : ssList) {
                zz++;
                String zzz = StringUtils.leftPad(zz + "", 2, '0');
                if (zz > 99) {
                    zzz = String.valueOf(zz);
                }
                emStudent = new EmPlaceStudent();
                emStudent.setId(UuidUtils.generateUuid());
                emStudent.setExamNumber(s.getExamNumber());
                emStudent.setStudentId(s.getStudentId());
                emStudent.setExamPlaceId(em.getId());
                emStudent.setExamId(em.getExamId());
                emStudent.setSeatNum(zzz);
                emStudentList.add(emStudent);
            }
            iii = end;
        }
    }

    private void createExamNum(String examId, String schoolId, List<EmStudentDto> dtoList, List<Student> studentList, List<EmExamNum> insertExamNumList) {
        Map<String, String> filterMap = emFiltrationService.findByExamIdAndSchoolIdAndType(examId, schoolId, ExammanageConstants.FILTER_TYPE1);
        Map<String, String> stuNumMap = emExamNumService.findBySchoolIdAndExamId(schoolId, examId);
        Set<String> stuNumSet = new HashSet<String>();//已安排的考号
        for (String key : stuNumMap.keySet()) {
            if (stuNumMap.get(key) != null) {
                stuNumSet.add(stuNumMap.get(key));
            }
        }
        //安排的学生整体
        //如果没有考号的 默认前缀：20170823+4位（0001）----暂时
        //学生整体
        EmExamNum numItem = null;
        EmStudentDto dto = null;
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String eeee = sdf.format(dt);
        int ii = 1;
        for (Student ss : studentList) {
            if (filterMap.containsKey(ss.getId())) {
                continue;
            }
            dto = new EmStudentDto();
            dto.setStudentId(ss.getId());
            if (stuNumMap.containsKey(ss.getId())) {
                dto.setExamNumber(stuNumMap.get(ss.getId()));
            } else {
                ii = makeStuNum(stuNumSet, ii, eeee);
                String rr = StringUtils.leftPad(ii + "", 4, '0');
                if (ii > 99999) {
                    rr = ii + "";
                }

                rr = eeee + rr;
                dto.setExamNumber(rr);
                //新增考号
                numItem = new EmExamNum();
                numItem.setExamId(examId);
                numItem.setExamNumber(rr);
                numItem.setId(UuidUtils.generateUuid());
                numItem.setSchoolId(schoolId);
                numItem.setStudentId(ss.getId());
                insertExamNumList.add(numItem);

                ii++;
            }
            dtoList.add(dto);
        }
    }
    /**
     * 将选考学考科目分开
     * @param subList
     * @return
     */
    public List<EmSubjectInfo> getAllList(List<EmSubjectInfo> subList){
        List<EmSubjectInfo> allList=new ArrayList<>();
        if(CollectionUtils.isNotEmpty(subList)) {
            subList.forEach(sub -> {
                if ("0".equals(sub.getGkSubType()) && sub.getGkStartDate() != null) {//0的情况下且有这个时间，说明是7选三科目 所有学生范围
                    EmSubjectInfo inSub = new EmSubjectInfo();
                    inSub.setSubjectId(sub.getSubjectId());
                    inSub.setGkSubType("2");//学考
                    inSub.setStartDate(sub.getGkStartDate());
                    inSub.setEndDate(sub.getGkEndDate());
                    allList.add(inSub);
                    sub.setGkSubType("1");//设为选考类型即可
                    allList.add(sub);
                } else if ("2".equals(sub.getGkSubType())) {
                    sub.setStartDate(sub.getGkStartDate());
                    sub.setEndDate(sub.getGkEndDate());
                    allList.add(sub);
                } else {
                    allList.add(sub);
                }
            });
        }
        return allList;
    }
    /**
     * 得到交叉的科目是否有学生重复
     * @param subList
     * @param course73Map
     * @return
     */
    public String checkDate(List<EmSubjectInfo> subList,Map<String, Course> course73Map,
                            Map<String,EmSubGroup> subIdOfGroupMap,Map<String, Set<String>> stuGroupMap){
        if(CollectionUtils.isNotEmpty(subList)){
            List<EmSubjectInfo> allList=getAllList(subList);
            int length=allList.size();
            for(int i=0;i<length;i++){
                for(int j=i+1;j<length;j++){
                    if(allList.get(i).getEndDate().compareTo(allList.get(j).getStartDate())<=0
                            || allList.get(i).getStartDate().compareTo(allList.get(j).getEndDate())>=0){
                        //是不交叉的
                    }else{
                        EmSubGroup group1=subIdOfGroupMap.get(allList.get(i).getSubjectId()+"_"+allList.get(i).getGkSubType());
                        EmSubGroup group2=subIdOfGroupMap.get(allList.get(j).getSubjectId()+"_"+allList.get(j).getGkSubType());
                        if(group1!=null && group2!=null){
                            if(stuGroupMap.containsKey(group1.getId()) && stuGroupMap.containsKey(group2.getId())){
                                Set<String> set1=new HashSet<>();
                                set1.addAll(stuGroupMap.get(group1.getId()));
                                set1.retainAll(stuGroupMap.get(group2.getId()));
                                if(CollectionUtils.isNotEmpty(set1)){
                                    return group1.getGroupName()+"与"+group2.getGroupName()+"时间交叉，且存在同一学生!";
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    @Override
    public String autoByExamNum(String schoolId, String examId, String isgkExamType, String chooseType) {
        List<Student> studentList = new ArrayList<>();
        List<EmSubjectInfo> subList=emSubjectInfoService.findByExamId(examId);
        Map<String, Course> course73Map =EntityUtils.getMap(SUtils.dt(courseRemoteService.findByCodes73(schoolId), new TR<List<Course>>() {}),Course::getId);

        int arrangeCount = 0;
        List<EmPlace> emPlaceList = emPlaceService.findByExamIdAndSchoolIdWithMaster(examId, schoolId, false);
        Set<String> delEmPlaceIds = new HashSet<String>();
        if (CollectionUtils.isNotEmpty(emPlaceList)) {
            for (EmPlace em : emPlaceList) {
                arrangeCount = arrangeCount + em.getCount();
                delEmPlaceIds.add(em.getId());
            }
        }
        //获得参加这次考试的该校学生
        if (StringUtils.equals(isgkExamType, "1")) {
            studentList = findGkStudentByExamId(examId, schoolId, null, false, null);
        } else {
            studentList = findStudentByExamId(examId, schoolId, null, false, null);
        }
        List<EmStudentDto> dtoList = new ArrayList<>();
        List<EmExamNum> insertExamNumList = new ArrayList<>();
        createExamNum(examId, schoolId, dtoList, studentList, insertExamNumList);
        List<EmPlaceStudent> emStudentList = new ArrayList<>();
        if (StringUtils.equals(isgkExamType, "1")) {
            Map<String, EmStudentDto> dtoMap = EntityUtils.getMap(dtoList, "studentId");
            Map<String, EmPlace> placeMap = EntityUtils.getMap(emPlaceList, "id");
            List<EmSubGroup> groups = emSubGroupService.findListByExamId(examId);
            Set<String> groupIds = EntityUtils.getSet(groups, "id");
            Map<String, Set<String>> stuGroupMap = emStudentGroupService.findGroupMap(schoolId, examId, groupIds.toArray(new String[0]));
            Map<String, Set<String>> placeGroupMap = emPlaceGroupService.findGroupMap(schoolId, groupIds.toArray(new String[0]));
            //获取科目id+科目类型 对应的科目组
            Map<String,EmSubGroup> subIdOfGroupMap=new HashMap<>();
            groups.forEach(g->{
                if("0".equals(g.getSubType())){//非7选3
                    for(String inId:g.getSubjectId().split(",")){
                        subIdOfGroupMap.put(inId+"_"+g.getSubType(),g);
                    }
                }else{
                    subIdOfGroupMap.put(g.getSubjectId()+"_"+g.getSubType(),g);
                }
            });
            //得到交叉的科目是否有学生重复
            String str=checkDate(subList,course73Map,subIdOfGroupMap,stuGroupMap);
            if(StringUtils.isNotBlank(str)){
                return str;
            }
            for (EmSubGroup emSubGroup : groups) {
                if (!placeGroupMap.containsKey(emSubGroup.getId()) || !stuGroupMap.containsKey(emSubGroup.getId())) {
                    continue;
                }
                Set<String> stus = stuGroupMap.get(emSubGroup.getId());
                List<EmStudentDto> dtos = new ArrayList<>();
                for (String stuId : stus) {
                    if (dtoMap.containsKey(stuId)) {
                        EmStudentDto dto = dtoMap.get(stuId);
                        dtos.add(dto);
                    }
                }
                Set<String> emPlaceIds = placeGroupMap.get(emSubGroup.getId());
                List<EmPlace> places = new ArrayList<>();
                for (String placeId : emPlaceIds) {
                    EmPlace place = placeMap.get(placeId);
                    if (place == null) {
                        continue;
                    }
                    places.add(place);
                }
                Collections.sort(places, new Comparator<EmPlace>() {
                    @Override
                    public int compare(EmPlace o1, EmPlace o2) {
                        if (StringUtils.isNotBlank(o1.getExamPlaceCode()) && StringUtils.isNotBlank(o2.getExamPlaceCode())) {
                            return o1.getExamPlaceCode().compareTo(o2.getExamPlaceCode());
                        }
                        return 0;
                    }
                });
                List<EmPlaceStudent> pmStuGroup = new ArrayList<>();
                if (StringUtils.equals(chooseType, "1")) {
                    autoByExamNumOne(dtos, places, pmStuGroup);
                } else {
                    autoByRandomOne(dtos, places, pmStuGroup);
                }
                for (EmPlaceStudent e : pmStuGroup) {
                    e.setGroupId(emSubGroup.getId());
                }
                emStudentList.addAll(pmStuGroup);
            }
        } else {
            //非高考
            int allCount = dtoList.size();
            if (allCount > arrangeCount) {
                //无需安排
//				return error("学生总人数："+allCount+",考场总容纳："+arrangeCount+",考场不足，请先维护考场！");
            }
            if (StringUtils.equals(chooseType, "1")) {
                autoByExamNumOne(dtoList, emPlaceList, emStudentList);
            } else {
                autoByRandomOne(dtoList, emPlaceList, emStudentList);
            }
//			autoByExamNumOne(dtoList, emPlaceList, emStudentList);
        }
        insertList(delEmPlaceIds, emStudentList, insertExamNumList);
        return null;
    }

}
