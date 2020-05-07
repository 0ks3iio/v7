package net.zdsoft.exammanage.data.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.basedata.remote.service.SchoolRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.dao.EmAimsStudentDao;
import net.zdsoft.exammanage.data.dto.EmAimsStudentDto;
import net.zdsoft.exammanage.data.entity.EmAimsInfo;
import net.zdsoft.exammanage.data.entity.EmAimsStudent;
import net.zdsoft.exammanage.data.entity.EmEnrollStudent;
import net.zdsoft.exammanage.data.entity.EmExamInfo;
import net.zdsoft.exammanage.data.entity.EmPlaceStudent;
import net.zdsoft.exammanage.data.service.EmAimsInfoService;
import net.zdsoft.exammanage.data.service.EmAimsStudentService;
import net.zdsoft.exammanage.data.service.EmEnrollStudentService;
import net.zdsoft.exammanage.data.service.EmExamInfoService;
import net.zdsoft.exammanage.data.service.EmPlaceStudentService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

@Service("emAimsStudentService")
public class EmAimsStudentServiceImpl extends BaseServiceImpl<EmAimsStudent, String> implements EmAimsStudentService {
    @Autowired
    private EmAimsStudentDao emAimsStudentDao;
    @Autowired
    private EmAimsInfoService emAimsInfoService;
    @Autowired
    private SchoolRemoteService schoolRemoteService;
    @Autowired
    private EmEnrollStudentService emEnrollStudentService;
    @Autowired
    private EmExamInfoService emExamInfoService;
    @Autowired
    private StudentRemoteService studentRemoteService;
    @Autowired
    private ClassRemoteService classRemoteService;
    @Autowired
    private EmPlaceStudentService emPlaceStudentService;

    @Override
    public void saveAimsStu(EmAimsStudent aimsStu) {
        checkSave(aimsStu);
        save(aimsStu);
    }

    @Override
    public EmAimsStudentDto findByAimsIdAndStuId(String aimsId, String stuId) {
        EmAimsStudentDto dto = new EmAimsStudentDto();
        Student stu = SUtils.dc(studentRemoteService.findOneById(stuId), Student.class);
        School sch = SUtils.dc(schoolRemoteService.findOneById(stu.getSchoolId()), School.class);
        List<EmAimsStudent> aimsStudents = emAimsStudentDao.findListByStuIdAndAimsIds(stuId, new String[]{aimsId});
        dto.setAimsId(aimsId);
        Clazz clazz=SUtils.dc(classRemoteService.findOneById(stu.getClassId()),Clazz.class);
        if(stu!=null){
        	stu.setClassName(clazz.getClassNameDynamic());
        }
        dto.setStudent(stu);
        dto.setSchoolName(sch.getSchoolName());
        if (CollectionUtils.isNotEmpty(aimsStudents)) {
            EmAimsStudent aimsStudent = aimsStudents.get(0);
            dto.setId(aimsStudent.getId());
            dto.setAimsSchoolId(aimsStudent.getAimsSchoolId());
        }
        return dto;
    }

    @Override
    public List<EmAimsStudentDto> findListByStuId(String schoolId, String stuId) {
        List<EmAimsStudentDto> dtolist = new ArrayList<>();
        List<EmEnrollStudent> enrollStudentList = emEnrollStudentService.findByStuIdAndState(stuId, "1");
        Set<String> examIds = EntityUtils.getSet(enrollStudentList, EmEnrollStudent::getExamId);
        List<EmExamInfo> examInfos = emExamInfoService.findListByIds(examIds.toArray(new String[0]));
        Map<String, EmExamInfo> examMap = EntityUtils.getMap(examInfos, EmExamInfo::getId);
        List<EmAimsInfo> aimsInfos = emAimsInfoService.findOpenByExamIds(examIds.toArray(new String[0]));
        Set<String> aimsIds = EntityUtils.getSet(aimsInfos, EmAimsInfo::getId);
        List<EmAimsStudent> aimsStudents = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(aimsIds)) {
            aimsStudents = emAimsStudentDao.findListByStuIdAndAimsIds(stuId, aimsIds.toArray(new String[0]));
        }
        Set<String> schIds = EntityUtils.getSet(aimsStudents, EmAimsStudent::getAimsSchoolId);
        Map<String, School> schMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(schIds)) {
            schMap = EntityUtils.getMap(SUtils.dt(schoolRemoteService.findListByIds(schIds.toArray(new String[0])), new TR<List<School>>() {
            }), School::getId);
        }
        Map<String, String> aimsIdMap = EntityUtils.getMap(aimsStudents, EmAimsStudent::getAimsId, EmAimsStudent::getAimsSchoolId);
        EmAimsStudentDto dto;
        for (EmAimsInfo emAimsInfo : aimsInfos) {
            dto = new EmAimsStudentDto();
            EmExamInfo examInfo = examMap.get(emAimsInfo.getExamId());
            dto.setExamCode(examInfo.getExamCode());
            dto.setExamName(examInfo.getExamName());
            dto.setExamType(examInfo.getExamType());
            dto.setAimsId(emAimsInfo.getId());
            dto.setStartDate(emAimsInfo.getStartTime());
            dto.setEndDate(emAimsInfo.getEndTime());
            dto.setIsEdit(isEdit(emAimsInfo.getStartTime(), emAimsInfo.getEndTime()) ? "1" : "0");
            if (aimsIdMap.containsKey(emAimsInfo.getId())) {
                dto.setState("1");
                String aimsSchId = aimsIdMap.get(emAimsInfo.getId());
                dto.setAimsSchoolId(aimsSchId);
                dto.setAimsSchoolName(schMap.get(aimsSchId).getSchoolName());
            } else {
                dto.setState("0");
            }
            if(isEdit(emAimsInfo.getStartTime(), emAimsInfo.getEndTime())){
            	dtolist.add(dto);
            }
        }
        return dtolist;
    }

    private boolean isEdit(Date startDate, Date endDate) {
        boolean flag = false;
        String nowStr = DateUtils.date2String(new Date(), "yyyy-MM-dd");
        Date nowDate = DateUtils.string2Date(nowStr);
        if (startDate.getTime() <= nowDate.getTime()
                && endDate.getTime() >= nowDate.getTime()) {
            flag = true;
        }
        return flag;
    }

    @Override
    public List<EmAimsStudent> findListBySchoolId(String examId,String aimsId, String schoolId, String field, String keyWord,
                                                  Pagination page) {
        List<EmAimsStudent> list = new ArrayList<>();
        //Specification<EmAimsStudent> specification = new Specification<EmAimsStudent>() {
        //    @Override
        //    public Predicate toPredicate(Root<EmAimsStudent> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        //        List<Predicate> ps = new ArrayList<Predicate>();
        //        ps.add(cb.equal(root.get("aimsId").as(String.class), aimsId));
        //        ps.add(cb.equal(root.get("schoolId").as(String.class), schoolId));
        //        if (StringUtils.isNotBlank(field) && StringUtils.isNotBlank(keyWord)) {
        //            if (StringUtils.equals(field, "1")) {
        //                ps.add(cb.equal(root.get("examCode").as(String.class), keyWord));
        //            } else if (StringUtils.equals(field, "2")) {
        //                ps.add(cb.equal(root.get("studentCode").as(String.class), keyWord));
        //            }else if (StringUtils.equals(field, "4")) {
        //                ps.add(cb.equal(root.get("studentName").as(String.class), keyWord));
        //            } else {
        //                ps.add(cb.equal(root.get("identityCard").as(String.class), keyWord));
        //            }
        //
        //        }
        //        List<Order> orderList = new ArrayList<Order>();
        //        cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
        //        return cq.getRestriction();
        //    }
        //};
        //if (page != null) {
        //    Pageable pageable = Pagination.toPageable(page);
        //    Page<EmAimsStudent> findAll = emAimsStudentDao.findAll(specification, pageable);
        //    page.setMaxRowCount((int) findAll.getTotalElements());
        //    list = findAll.getContent();
        //} else {
        //    list = emAimsStudentDao.findAll(specification);
        //}
        List<EmEnrollStudent> enrollStudentList = Lists.newArrayList();
        enrollStudentList = emEnrollStudentService.findByIdsAndstate(examId, schoolId,"", "1", page);
        if (CollectionUtils.isNotEmpty(enrollStudentList)) {
        	List<EmAimsStudent> scoreList = emAimsStudentDao.findListByAimsIdAndSchoolId(aimsId, schoolId);
        	Map<String, EmAimsStudent> map = EntityUtils.getMap(scoreList, EmAimsStudent::getStudentId);
            Set<String> schoolIdSet = EntityUtils.getSet(enrollStudentList, "schoolId");
            Set<String> schoolIdSetOld = EntityUtils.getSet(scoreList, "aimsSchoolId");
            schoolIdSet.addAll(schoolIdSetOld);
            Map<String, String> schoolNameMap = getSchoolNameMap(schoolIdSet);
            Set<String> studentIds = EntityUtils.getSet(enrollStudentList, "studentId");
            List<Student> students = SUtils.dt(studentRemoteService.findListByIds(studentIds.toArray(new String[0])), new TR<List<Student>>() {
            });
            Map<String, Student> studentNameMap = EntityUtils.getMap(students, "id");
            Set<String> classIds = EntityUtils.getSet(students, "classId");
            List<Clazz> clazzes = SUtils.dt(classRemoteService.findClassListByIds(classIds.toArray(new String[0])), new TR<List<Clazz>>() {
            });
            Map<String, Clazz> clazzesNameMap = EntityUtils.getMap(clazzes, "id");
            
            List<EmPlaceStudent> emStuList = emPlaceStudentService.findByExamIdStuIds(examId, studentIds.toArray(new String[0]));
            Map<String, String> examNumMap = EntityUtils.getMap(emStuList, EmPlaceStudent::getStudentId, EmPlaceStudent::getExamNumber);

            Iterator<EmEnrollStudent> it = enrollStudentList.iterator();
            while (it.hasNext()) {
                EmEnrollStudent emEnrollStudent = it.next();
                EmAimsStudent emAimsStudent = new EmAimsStudent();
                emAimsStudent.setSchoolName(schoolNameMap.get(emEnrollStudent.getSchoolId()));
                Student student = studentNameMap.get(emEnrollStudent.getStudentId());
                if (student != null) {
                    if (clazzesNameMap.get(student.getClassId()) == null) {
                        it.remove();
                        continue;
                    }
                    emAimsStudent.setIdentityCard(student.getIdentityCard());
                    if (MapUtils.isNotEmpty(map) && map.containsKey(student.getId())) {
                    	EmAimsStudent old = map.get(student.getId());
                    	if (schoolNameMap.containsKey(old.getAimsSchoolId())) {
                    		emAimsStudent.setAimsSchoolId(schoolNameMap.get(old.getAimsSchoolId()));
                        }
                    }
                    if(MapUtils.isNotEmpty(examNumMap)&&examNumMap.containsKey(student.getId())){
                    	emAimsStudent.setExamCode(examNumMap.get(student.getId()));
                    }
                    emAimsStudent.setSex(student.getSex() + "");
                    emAimsStudent.setStudentName(student.getStudentName());
                    emAimsStudent.setStudentCode(student.getUnitiveCode());

                    if (StringUtils.isNotBlank(field) && StringUtils.isNotBlank(keyWord) && StringUtils.isNotBlank(keyWord.trim())) {
                        String str = "";
                        if (StringUtils.equals("1", field)) {
                            str = emAimsStudent.getExamCode();
                        } else if (StringUtils.equals("2", field)) {
                            str = emAimsStudent.getStudentCode();
                        }else if(StringUtils.equals("4", field)){
                            str = emAimsStudent.getStudentName();
                        } else {
                            str = emAimsStudent.getIdentityCard();
                        }
                        if (StringUtils.equals(keyWord.trim(), str)) {
                            list.add(emAimsStudent);
                        }
                    } else {
                        list.add(emAimsStudent);
                    }
                }
            }

        }
        
//        if (CollectionUtils.isEmpty(list)) {
//            return list;
//        }
//        Set<String> schIds = new HashSet<>();
//        Set<String> stuIds = new HashSet<>();
//        for (EmAimsStudent aimsStu : list) {
//        	stuIds.add(aimsStu.getStudentId());
//            schIds.add(aimsStu.getAimsSchoolId());
//            schIds.add(aimsStu.getSchoolId());
//        }
//        Map<String, School> schMap = EntityUtils.getMap(SUtils.dt(schoolRemoteService.findListByIds(schIds.toArray(new String[0])), new TR<List<School>>() {
//        }), School::getId);
//        List<Student> students = SUtils.dt(studentRemoteService.findListByIds(stuIds.toArray(new String[0])), new TR<List<Student>>() {
//        });
//        Map<String, Student> studentNameMap = EntityUtils.getMap(students, "id");
//        for (EmAimsStudent aimsStu : list) {
//            if (schMap.containsKey(aimsStu.getSchoolId())) {
//                aimsStu.setSchoolName(schMap.get(aimsStu.getSchoolId()).getSchoolName());
//            }
//            if (schMap.containsKey(aimsStu.getAimsSchoolId())) {
//                aimsStu.setAimsSchoolId(schMap.get(aimsStu.getAimsSchoolId()).getSchoolName());
//            }
//            Student student = studentNameMap.get(aimsStu.getStudentId());
//            if (student != null) {
//            	aimsStu.setSex(student.getSex() + "");
//            }
//        }
        return list;
    }
    
    private Map<String, String> getSchoolNameMap(Set<String> schoolIds) {
        List<School> schools = SUtils.dt(schoolRemoteService.findListByIds(schoolIds.toArray(new String[0])), new TR<List<School>>() {
        });
        Map<String, String> schoolNameMap = EntityUtils.getMap(schools, School::getId, School::getSchoolName);
        return schoolNameMap;
    }

    @Override
    protected BaseJpaRepositoryDao<EmAimsStudent, String> getJpaDao() {
        return emAimsStudentDao;
    }

    @Override
    protected Class<EmAimsStudent> getEntityClass() {
        return EmAimsStudent.class;
    }
}
