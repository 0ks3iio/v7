package net.zdsoft.basedata.remote.service.impl;

import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.StudentService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("studentRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class StudentRemoteServiceImpl extends
		BaseRemoteServiceImpl<Student, String> implements StudentRemoteService {

	@Autowired
	private StudentService studentService;

	@Override
	protected BaseService<Student, String> getBaseService() {
		return studentService;
	}
	
	@Override
	public String findListByIds(String[] ids){
	    return SUtils.s(studentService.findListByIds(ids));
	}

	@Override
	public String findByClassIds(String... classIds) {
		return SUtils.s(studentService.findByClassIds(classIds));
	}

	@Override
	public String findByTeachClassIds(String... classIds) {
		return SUtils.s(studentService.findByTeachClass(classIds));
	}

	@Override
	public String findByClassIds(String[] classIds, String page) {
		Pagination pagination = SUtils.dc(page, Pagination.class);
		List<Student> findByClassIdIn = studentService.findByClassIdIn(null,
				classIds, pagination);
		return SUtils.s(findByClassIdIn, (long) pagination.getMaxRowCount());
	}

	@Override
	public String findByTeachClassIds(String[] classIds, String page) {
		Pagination pagination = SUtils.dc(page, Pagination.class);
		List<Student> findByTeachClass = studentService.findByTeachClass(
				classIds, pagination);
		return SUtils.s(findByTeachClass, (long) pagination.getMaxRowCount());
	}

	@Override
	public String findBySchIdStudentCode(String schoolId, String studentCode) {
		return SUtils.s(studentService.findBySchIdStudentCode(schoolId,
				studentCode));
	}

	public String findBySchIdStudentCodes(String schoolId, String[] studentCodes) {
		return SUtils.s(studentService.findBySchIdStudentCodes(schoolId,
				studentCodes));
	}
	
	public String findByIdentityCards(String... identityCards) {
		return SUtils.s(studentService.findByIdentityCards(identityCards));
	}

	public String findBySchoolIdIn(String stuName, String[] schoolIds) {
		return SUtils.s(studentService.findBySchoolIdIn(stuName, schoolIds));
	}

	@Override
	public String saveAllEntitys(String entitys) {
		Student[] dt = SUtils.dt(entitys, new TR<Student[]>() {
		});
		return SUtils.s(studentService.saveAllEntitys(dt));
	}

	@Override
	public String findByIdsClaIdLikeStuCodeNames(String unitId, String[] ids,
			String[] classIds, String searchStudent, String page) {
		Pagination pagination = SUtils.dc(page, Pagination.class);
		List<Student> findByIdsClaIdLikeStuCodeNames = studentService
				.findByIdsClaIdLikeStuCodeNames(
						unitId,
						ids,
						classIds,
						StringUtils.isNotBlank(searchStudent) ? SUtils.dc(
								searchStudent, Student.class) : new Student(),
						pagination);
		if (StringUtils.isBlank(page)) {
			return SUtils.s(findByIdsClaIdLikeStuCodeNames);
		}
		return SUtils.s(findByIdsClaIdLikeStuCodeNames,
				pagination != null ? (long) pagination.getMaxRowCount() : 0);
	}

	@Override
	public String findByNotIdsClaIdLikeStuCodeNames(String unitId,
			String[] ids, String[] classIds, String searchStudent, String page) {
		Pagination pagination = SUtils.dc(page, Pagination.class);
		List<Student> findByNotIdsClaIdLikeStuCodeNames = studentService
				.findByNotIdsClaIdLikeStuCodeNames(
						unitId,
						ids,
						classIds,
						StringUtils.isNotBlank(searchStudent) ? SUtils.dc(
								searchStudent, Student.class) : new Student(),
						pagination);
		if (StringUtils.isBlank(page)) {
			return SUtils.s(findByNotIdsClaIdLikeStuCodeNames);
		}
		return SUtils.s(findByNotIdsClaIdLikeStuCodeNames,
				pagination != null ? (long) pagination.getMaxRowCount() : 0);
	}

	@Override
	public String findByUnitLikeCode(String unitId, String valCode, String Val) {
		return SUtils.s(studentService.findByUnitLikeCode(unitId, valCode, Val
				+ "%"));
	}

	@Override
	public String findByGreadIdStuCode(String greadId, String studentCode) {
		return SUtils.s(studentService.findByGreadIdStuCode(greadId,
				studentCode));
	}

	@Override
	public String findByGradeIds(String... gradeIds) {
		return SUtils.s(studentService.findByGradeIds(gradeIds));
	}

	@Override
	public String findMapByClassIdIn(String[] classIds) {
		return SUtils.s(studentService.findMapByClassIdIn(classIds));
	}

	@Override
	public String countMapByClassIds(String... classIds) {
		return SUtils.s(studentService.countMapByClassIds(classIds));
	}

	@Override
	public String findByCardNumber(String unitId, String cardNumber) {
		return SUtils.s(studentService.findByCardNumber(unitId, cardNumber));
	}

	@Override
	public String findByStudentNameAndIdentityCardWithNoUser(
			String studentName, String identityCard) {
		return SUtils.s(studentService
				.findByStudentNameAndIdentityCardWithNoUser(studentName,
						identityCard));
	}

	@Override
	public String findByStudentNameAndIdentityCard(String studentName,
			String identityCard) {
		return SUtils.s(studentService.findByStudentNameAndIdentityCard(
				studentName, identityCard));
	}

	public int[] updateCardNumber(List<String[]> studentList) {
		return studentService.updateCardNumber(studentList);
	}

	@Override
	public String findByGradeId(String gradeId) {
		return SUtils.s(studentService.findByGradeId(gradeId));
	}

	@Override
	public String findAllStudentByGradeId(String gradeId) {
		return SUtils.s(studentService.findAllStudentByGradeId(gradeId));
	}

	@Override
	public String findByClaIdsLikeStuCodeNames(String unitId, String gradeId,
			String[] classIds, String searchStudent) {
		return SUtils.s(studentService.findByClaIdsLikeStuCodeNames(unitId,gradeId,classIds,StringUtils.isNotBlank(searchStudent) ? SUtils.dc(
				searchStudent, Student.class) : new Student()));
	}

	@Override
	public String findBySchoolId(String schoolId) {
		return SUtils.s(studentService.findBySchoolId(schoolId));
	}


	@Override
	public long CountStudByGradeId(String gradeId) {
		return studentService.CountStudByGradeId(gradeId);
	}

	@Override
	public String findPartStudByGradeId(String unitId, String gradeId, String[] classIds, String[] studentIds) {
		return SUtils.s(studentService.findPartStudByGradeId(unitId, gradeId, classIds, studentIds));
	}

	@Override
	public void updateClaIds(List<Student> studentList) {
		studentService.updateClaIds(studentList);
	}

	@Override
	public String findPartStudentById(String[] studentIds) {
		return SUtils.s(studentService.findPartStudentById(studentIds));
	}

	@Override
	public String findMapByAttr(String[] ids, String attrName){
		return SUtils.s(studentService.findMapByAttr(ids, attrName));
	}

	@Override
	public String findBySchoolIdIn(String[] schoolIds, String page) {
		if(StringUtils.isBlank(page)) {
			return findBySchoolIdIn(null, schoolIds);
		}
		Pagination pagination = SUtils.dc(page, Pagination.class);
		return  SUtils.s(studentService.findBySchoolIdIn(null,schoolIds, pagination));
	}

	@Override
	public String findBySchoolIdStudentNameStudentCode(String unitId, String studentName, String studentCode) {
		return  SUtils.s(studentService.findBySchoolIdStudentNameStudentCode(unitId,studentName, studentCode));
	}

	@Override
	public String findByStudentCode(String studentCode) {
		return SUtils.s(studentService.findByStudentCode(studentCode));
	}

	@Override
	public String countListBySchoolIds(String[] schoolIds) {
		return SUtils.s(studentService.countListBySchoolIds(schoolIds));
	}

	@Override
	public String countMapByGradeIds(String[] gradeIds) {
		return SUtils.s(studentService.countMapByGradeIds(gradeIds));
	}

	@Override
	public String findListBlendClassIds(String[] ids) {
		return SUtils.s(studentService.findListBlendClassIds(ids));
	}
}
