package net.zdsoft.newstusys.service.impl;

import net.zdsoft.basedata.entity.*;
import net.zdsoft.basedata.remote.service.*;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.newstusys.constants.BaseStudentConstants;
import net.zdsoft.newstusys.dao.StudentAbnormalFlowJdbcDao;
import net.zdsoft.newstusys.entity.BaseStudent;
import net.zdsoft.newstusys.entity.StudentAbnormalFlow;
import net.zdsoft.newstusys.service.StudentAbnormalFlowService;
import net.zdsoft.system.remote.service.SystemIniRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 * @author weixh
 * 2019年2月28日	
 */
@Service("studentAbnormalFlowService")
public class StudentAbnormalFlowServiceImpl implements StudentAbnormalFlowService {
	@Autowired
	private StudentRemoteService studentRemoteService;
	@Autowired
	private FamilyRemoteService familyRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private StudentAbnormalFlowJdbcDao studentAbnormalFlowJdbcDao;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Autowired
    private SystemIniRemoteService systemIniRemoteService;
	@Autowired
    private ClassRemoteService classRemoteService;
	
	@Override
	public void saveLeaveStu(StudentAbnormalFlow flow) {
		Student stu = Student.dc(studentRemoteService.findOneById(flow.getStuid()));
		if(stu == null || stu.getIsDeleted() == 1) {
			throw new RuntimeException("学生不存在或者已被删除！");
		}
		if(stu.getIsLeaveSchool() != 0) {
			throw new RuntimeException("学生已离校，无法继续操作！");
		}
		if(!StringUtils.equals(stu.getSchoolId(), flow.getSchid())) {
			throw new RuntimeException("非本校学生，无法继续操作！");
		}
		Date now = new Date();
		stu.setIsLeaveSchool(1);
		stu.setModifyTime(now);
		stu.setNowState(flow.getFlowtype());
		studentRemoteService.save(stu);
		// flow
		flow.leaveToFlow(stu);
		Semester sem = Semester.dc(semesterRemoteService.getCurrentSemester(1, flow.getSchid()));
		if(sem != null) {
			flow.setAcadyear(sem.getAcadyear());
			flow.setSemester(sem.getSemester()+"");
		}
		studentAbnormalFlowJdbcDao.save(flow);
		List<String> ownerIds = new ArrayList<>();
		ownerIds.add(stu.getId());
		List<Family> fams = SUtils.dt(familyRemoteService.findByStudentId(stu.getId()), new TR<List<Family>>() {});
		for(Family fam : fams) {
			fam.setIsLeaveSchool(1);
			fam.setModifyTime(now);
			ownerIds.add(fam.getId());
		}
		familyRemoteService.saveAll(fams.toArray(new Family[0]));
		List<User> users = User.dt(userRemoteService.findByOwnerIds(ownerIds.toArray(new String[0])));
		for(User us : users) {
			us.setModifyTime(now);
			us.setUserState(User.USER_MARK_LOCK);
		}
		userRemoteService.saveAll(users.toArray(new User[0]));
	}
	
	@Override
	public void saveInStu(StudentAbnormalFlow flow) {
		// TODO
		Student stu = Student.dc(studentRemoteService.findOneById(flow.getStuid()));
		if(stu == null || stu.getIsDeleted() == 1) {
			throw new RuntimeException("学生不存在或者已被删除！");
		}
		if(stu.getIsLeaveSchool() == 0) {
			throw new RuntimeException("学生已在校，无法继续操作！");
		}
		BaseStudent bs = new BaseStudent();
		EntityUtils.copyProperties(stu, bs);
		if (BaseStudentConstants.SYSDEPLOY_SCHOOL_ZJZJ.equals(systemIniRemoteService.findValue(BaseStudentConstants.SYSTEM_DEPLOY_SCHOOL)) 
        		&& StringUtils.isNotEmpty(flow.getStuStuCode())) {
			List<Student> studentList = Student
					.dt(studentRemoteService.findListBy(
							new String[] { "schoolId", "isDeleted", "isLeaveSchool", "studentCode" },
							new String[] { flow.getSchid(), "0", "0", flow.getStuStuCode() }))
					.stream().filter(s -> !s.getId().equals(flow.getStuid())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(studentList)) {
            	throw new RuntimeException("操作失败，该学号/学籍铺号在本校已存在！");
            }
        }
		flow.inToFlow(bs);
		flow.setToschoolid(flow.getSchid());
		flow.setFlowto(flow.getCurrentclassid());
		stu.setIsLeaveSchool(0);
		stu.setSchoolId(flow.getSchid());
		stu.setClassId(flow.getCurrentclassid());
		stu.setOldStudentCode(stu.getStudentCode());
		stu.setStudentCode(flow.getStuStuCode());
		stu.setClassInnerCode(flow.getStuInnerCode());
		stu.setNowState(BaseStudentConstants.YD_DJ);
		if (stu.getToSchoolDate() == null) {
			Clazz cls = Clazz.dc(classRemoteService.findOneById(stu.getClassId()));
			stu.setToSchoolDate(DateUtils.string2Date(cls.getAcadyear().substring(0, 4) + "-09-01"));
			stu.setEnrollYear(cls.getAcadyear());
		}
		Date now = new Date();
		stu.setModifyTime(now);
		studentRemoteService.save(stu);
		if (StringUtils.isEmpty(flow.getAcadyear())) {
			Semester sem = Semester.dc(semesterRemoteService.getCurrentSemester(1, flow.getSchid()));
			if (sem != null) {
				flow.setAcadyear(sem.getAcadyear());
				flow.setSemester(sem.getSemester() + "");
			} 
		}
		studentAbnormalFlowJdbcDao.save(flow);
		List<String> ownerIds = new ArrayList<>();
		ownerIds.add(stu.getId());
		List<Family> fams = SUtils.dt(familyRemoteService.findByStudentId(stu.getId()), new TR<List<Family>>() {});
		for(Family fam : fams) {
			fam.setIsLeaveSchool(0);
			fam.setSchoolId(stu.getSchoolId());
			fam.setModifyTime(now);
			ownerIds.add(fam.getId());
		}
		familyRemoteService.saveAll(fams.toArray(new Family[0]));
		List<User> users = User.dt(userRemoteService.findByOwnerIds(ownerIds.toArray(new String[0])));
		for(User us : users) {
			us.setUnitId(stu.getSchoolId());
			us.setModifyTime(now);
			us.setUserState(User.USER_MARK_NORMAL);
		}
		userRemoteService.saveAll(users.toArray(new User[0]));
	}

	public StudentAbnormalFlow findAbnormalFlowByFlowByStuid(String stuid, String[] flowtypes) {
		List<StudentAbnormalFlow> flows =  studentAbnormalFlowJdbcDao.findAbnormalFlowByFlowByStuid(stuid, flowtypes);
		if(CollectionUtils.isNotEmpty(flows)){
			return flows.get(0);
		}
		return null;
	}

	public List<StudentAbnormalFlow> findFlowByStuidsType(String schoolId, String[] stuids, String[] flowtypes){
		return studentAbnormalFlowJdbcDao.findFlowByStuidsType(schoolId, stuids, flowtypes);
	}

	@Override
	public List<StudentAbnormalFlow> findAbnormalFlowByFlowTypes(
			String[] unitIds, String[] flowTypes, Pagination page) {	
		return studentAbnormalFlowJdbcDao.findAbnormalFlowByFlowTypes(unitIds, flowTypes, page);
	}

	@Override
	public List<StudentAbnormalFlow> findAbnormalFlowStudent(String schoolId,
			String acadyear, String semester, String[] flowTypes) {
		return studentAbnormalFlowJdbcDao.findAbnormalFlowStudent(schoolId, acadyear, semester, flowTypes);
	}

	public List<StudentAbnormalFlow> findAbnormalFlowStudentSec(String schoolId, String acadyear, String semester, String section, String[] flowTypes){
		if(StringUtils.isEmpty(section)){
			return findAbnormalFlowStudent(schoolId, acadyear, semester, flowTypes);
		}
		return studentAbnormalFlowJdbcDao.findAbnormalFlowStudentSection(schoolId, acadyear, semester, section, flowTypes);
	}
}
