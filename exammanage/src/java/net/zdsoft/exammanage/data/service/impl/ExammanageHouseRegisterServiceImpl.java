package net.zdsoft.exammanage.data.service.impl;


import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.exammanage.data.dao.ExammanageHouseRegisterDao;
import net.zdsoft.exammanage.data.dto.EmHouseDto;
import net.zdsoft.exammanage.data.entity.ExammanageHouseRegister;
import net.zdsoft.exammanage.data.service.ExammanageHouseRegisterService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.EntityUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;

import java.util.*;

@Service("exammanageHouseRegisterService")
public class ExammanageHouseRegisterServiceImpl extends BaseServiceImpl<ExammanageHouseRegister, String> implements ExammanageHouseRegisterService {

    @Autowired
    private ExammanageHouseRegisterDao exammanageHouseRegisterDao;
    @Autowired
    private UnitRemoteService unitRemoteService;
    @Autowired
    private StudentRemoteService studentRemoteService;

    @Override
    public List<ExammanageHouseRegister> getExammanageHouseRegisterByAcadyearAndSemesterAndExamId(
            String acadyear, String semester, String examId, Pagination page) {
        Specification<ExammanageHouseRegister> specification = new Specification<ExammanageHouseRegister>() {
            @Override
            public Predicate toPredicate(Root<ExammanageHouseRegister> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();
                ps.add(cb.equal(root.get("examId").as(String.class), examId));
                ps.add(cb.equal(root.get("acadyear").as(String.class), acadyear));
                ps.add(cb.equal(root.get("semester").as(String.class), semester));

                List<Order> orderList = new ArrayList<Order>();
                orderList.add(cb.asc(root.get("oldSchoolId").as(String.class)));
                orderList.add(cb.asc(root.get("studentId").as(String.class)));
                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return cq.getRestriction();
            }
        };
        if (page != null) {
            Pageable pageable = Pagination.toPageable(page);
            Page<ExammanageHouseRegister> findAll = exammanageHouseRegisterDao.findAll(specification, pageable);
            page.setMaxRowCount((int) findAll.getTotalElements());
            List<ExammanageHouseRegister> exammanageHouseRegisters = findAll.getContent();
            setThing(exammanageHouseRegisters);
            return exammanageHouseRegisters;
        } else {
            return exammanageHouseRegisterDao.findAll(specification);
        }
    }

    private void setThing(List<ExammanageHouseRegister> exammanageHouseRegisters) {
        if (CollectionUtils.isNotEmpty(exammanageHouseRegisters)) {
            Set<String> stuSet = new HashSet<>();
            Set<String> unitSet = new HashSet<>();
            for (ExammanageHouseRegister exammanageHouseRegister : exammanageHouseRegisters) {
                stuSet.add(exammanageHouseRegister.getStudentId());
                unitSet.add(exammanageHouseRegister.getOldSchoolId());
                unitSet.add(exammanageHouseRegister.getOldUnitId());
                unitSet.add(exammanageHouseRegister.getNewUnitId());
            }
            List<Student> students = studentRemoteService.findListObjectByIds(stuSet.toArray(new String[0]));
            Map<String, Student> stuMap = EntityUtils.getMap(students, e -> e.getId());
            List<Unit> units = unitRemoteService.findListObjectByIds(unitSet.toArray(new String[0]));
            Map<String, Unit> unitMap = EntityUtils.getMap(units, e -> e.getId());
            for (ExammanageHouseRegister exammanageHouseRegister : exammanageHouseRegisters) {
                if (stuMap.containsKey(exammanageHouseRegister.getStudentId())) {
                    Student student = stuMap.get(exammanageHouseRegister.getStudentId());
                    exammanageHouseRegister.setStuName(student.getStudentName());
                    exammanageHouseRegister.setCard(student.getIdentityCard());
                }
                if (unitMap.containsKey(exammanageHouseRegister.getOldSchoolId())) {
                    Unit unit = unitMap.get(exammanageHouseRegister.getOldSchoolId());
                    exammanageHouseRegister.setOldSchoolName(unit.getUnitName());
                }
                if (unitMap.containsKey(exammanageHouseRegister.getOldUnitId())) {
                    Unit unit = unitMap.get(exammanageHouseRegister.getOldUnitId());
                    exammanageHouseRegister.setOldUnitName(unit.getUnitName());
                }
                if (unitMap.containsKey(exammanageHouseRegister.getNewUnitId())) {
                    Unit unit = unitMap.get(exammanageHouseRegister.getNewUnitId());
                    exammanageHouseRegister.setNewUnitName(unit.getUnitName());
                }
            }
        }
    }

    @Override
    public List<EmHouseDto> getExammanageHouseRegistersByAcadyearAndSemesterAndExamId(
            String acadyear, String semester, String examId) {
        List<EmHouseDto> emHouseDtos = new ArrayList<>();
        List<ExammanageHouseRegister> exammanageHouseRegisters = exammanageHouseRegisterDao.getExammanageHouseRegisterByAcadyearAndSemesterAndExamId(acadyear, semester, examId);
        if (CollectionUtils.isNotEmpty(exammanageHouseRegisters)) {
            Set<String> unitSetAll = new HashSet<>();
            unitSetAll = EntityUtils.getSet(exammanageHouseRegisters, e -> e.getNewUnitId());
            Set<String> uSet = EntityUtils.getSet(exammanageHouseRegisters, e -> e.getOldUnitId());
            if (CollectionUtils.isNotEmpty(uSet)) {
                unitSetAll.addAll(uSet);
            }
            Map<String, List<ExammanageHouseRegister>> oldUnitMap = new HashMap<>();
            Map<String, List<ExammanageHouseRegister>> newUnitMap = new HashMap<>();
            for (ExammanageHouseRegister exammanageHouseRegister : exammanageHouseRegisters) {
                if (!oldUnitMap.containsKey(exammanageHouseRegister.getOldUnitId())) {
                    List<ExammanageHouseRegister> units = new ArrayList<>();
                    units.add(exammanageHouseRegister);
                    oldUnitMap.put(exammanageHouseRegister.getOldUnitId(), units);
                } else {
                    oldUnitMap.get(exammanageHouseRegister.getOldUnitId()).add(exammanageHouseRegister);
                }
                if (!newUnitMap.containsKey(exammanageHouseRegister.getNewUnitId())) {
                    List<ExammanageHouseRegister> units = new ArrayList<>();
                    units.add(exammanageHouseRegister);
                    newUnitMap.put(exammanageHouseRegister.getNewUnitId(), units);
                } else {
                    newUnitMap.get(exammanageHouseRegister.getNewUnitId()).add(exammanageHouseRegister);
                }
            }
            List<Unit> units = unitRemoteService.findListObjectByIds(unitSetAll.toArray(new String[0]));
            for (Unit unit : units) {
                EmHouseDto emHouseDto = new EmHouseDto();
                emHouseDto.setUnitName(unit.getUnitName());
                if (oldUnitMap.containsKey(unit.getId())) {
                    emHouseDto.setTurnOut(oldUnitMap.get(unit.getId()).size());
                }
                if (newUnitMap.containsKey(unit.getId())) {
                    emHouseDto.setTurnIn(newUnitMap.get(unit.getId()).size());
                }
                emHouseDtos.add(emHouseDto);
            }
        }
        return emHouseDtos;
    }

    @Override
	public List<ExammanageHouseRegister> getExammanageHouseRegisterByExamIdAndStudentId(
			String examId, String studentId) {
		return exammanageHouseRegisterDao.getExammanageHouseRegistersByExamIdAndStudentId(examId, studentId);
	}

	@Override
    protected BaseJpaRepositoryDao<ExammanageHouseRegister, String> getJpaDao() {
        return exammanageHouseRegisterDao;
    }

    @Override
    protected Class<ExammanageHouseRegister> getEntityClass() {
        return ExammanageHouseRegister.class;
    }


}
