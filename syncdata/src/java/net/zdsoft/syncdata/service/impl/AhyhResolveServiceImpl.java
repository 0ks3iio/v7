package net.zdsoft.syncdata.service.impl;

import java.util.Date;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.BaseSyncSaveRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.syncdata.service.AhyhResolveService;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

@Service("ahyhResolveService")
public class AhyhResolveServiceImpl implements AhyhResolveService {

	@Autowired
	private BaseSyncSaveRemoteService baseSyncSaveService;

	@Autowired
	private UnitRemoteService unitRemoteService;

	@Override
	public void saveData(String... jsonStrings) {
		for (String s : jsonStrings) {
			JSONObject json = JSONObject.parseObject(s);
			String code = json.getString("code");
			if (StringUtils.equals("user", code)) {
				dealUser(json);
			} else if (StringUtils.equals("school", code)) {
				dealSchool(json);
			} else if (StringUtils.equals("user_organization", code)) {
				dealUserOrg(json);
			} else if (StringUtils.equals("class", code)) {
				dealClazz(json);
			}
		}
	}

	private void dealClazz(JSONObject json) {
		AhyhObject ahyh = anayClass(json);
		if (ahyh.getClazz() != null) {
			baseSyncSaveService.saveClass(new Clazz[] { ahyh.getClazz() });
		}
	}

	private AhyhObject anayClass(JSONObject json) {
		JSONObject dataJson = json.getJSONObject("data");
		String id = json.getString("id");
		String schoolId = dataJson.getString("schoolId");
		String className = dataJson.getString("className");
		String year = dataJson.getString("year");
		Clazz clazz = new Clazz();
		clazz.setId(StringUtils.leftPad(id, 32, "0"));
		clazz.setAcadyear(year + "-" + (NumberUtils.toInt(year) + 1));
		clazz.setIsDeleted(StringUtils.equals("delete", json.getString("action")) ? 1 : 0);
		clazz.setClassCode(json.getString(id).substring(22));
		clazz.setClassName(className);
		clazz.setSchoolId(StringUtils.leftPad(schoolId, 32, "0"));
		clazz.setCreationTime(new Date());
		clazz.setModifyTime(new Date());
		clazz.setEventSource(0);
		AhyhObject ahyh = new AhyhObject();
		ahyh.setClazz(clazz);
		return ahyh;
	}

	private void dealUserOrg(JSONObject json) {
		AhyhObject person = anayUserOrg(json);
		if (person.getUser() != null) {
			baseSyncSaveService.saveUser(new User[] { person.getUser() });
		}
		if (person.getTeacher() != null) {
			baseSyncSaveService.saveTeacher(new Teacher[] { person.getTeacher() });
		}
		if (person.getStudent() != null) {
			baseSyncSaveService.saveStudent(new Student[] { person.getStudent() });
		}
	}

	private void dealSchool(JSONObject json) {
		AhyhObject ahyhObject = anaySchool(json);
		baseSyncSaveService.saveSchool(new School[] { ahyhObject.getSchool() });
		Unit unit = ahyhObject.getUnit();
		Unit unitO = Unit.dc(unitRemoteService.findOneById(unit.getId()));
		User admin = new User();
		if (unitO == null) {
			admin.setPassword("12345678");
			admin.setOwnerId(unit.getId());
			admin.setOwnerType(User.OWNER_TYPE_TEACHER);
			admin.setUnitId(unit.getId());
			admin.setCreationTime(new Date());
			admin.setSex(1);
			admin.setId(unit.getId());
			admin.setSequence((int) NumberUtils.toLong(StringUtils.substring(unit.getId(), 22)));
			admin.setModifyTime(new Date());
			admin.setRegionCode(unit.getRegionCode());
			admin.setUserType(User.USER_TYPE_UNIT_ADMIN);
			admin.setIsDeleted(0);
			// 因为没有部门信息，所以默认的部门id与单位id一致，采用相同的内容
			admin.setDeptId(unit.getId());
			admin.setRealName(unit.getUnitName() + "管理员");

			Teacher adminTeacher = new Teacher();
			adminTeacher.setTeacherName(admin.getRealName());
			adminTeacher.setId(admin.getId());
			adminTeacher.setTeacherCode("000000");
			adminTeacher.setIsDeleted(0);
			adminTeacher.setUnitId(unit.getId());
			adminTeacher.setSex(1);
			adminTeacher.setEventSource(0);
			adminTeacher.setModifyTime(admin.getModifyTime());
			adminTeacher.setCreationTime(admin.getCreationTime());
			adminTeacher.setDeptId(unit.getId());
			baseSyncSaveService.saveTeacher(new Teacher[] { adminTeacher });
		}
		if (unitO != null && StringUtils.isBlank(unit.getUnionCode())) {
			unit.setUnionCode(unitO.getUnionCode());
		}
		if (StringUtils.isBlank(unit.getUnionCode()))
			unit.setUnionCode(unitRemoteService.findNextUnionCode(unit.getRegionCode(), unit.getUnitClass()));
		baseSyncSaveService.saveUnit(new Unit[] { ahyhObject.getUnit() });

		admin.setUsername("admin" + unit.getUnionCode());
		baseSyncSaveService.saveUser(new User[] { admin });

		if (ahyhObject.getDept() != null)
			baseSyncSaveService.saveDept(new Dept[] { ahyhObject.getDept() });
	}

	private void dealUser(JSONObject json) {
		User user = anayUser(json);
		baseSyncSaveService.saveUser(new User[] { user });
	}

	private class AhyhObject {
		private User user;
		private Student student;
		private Teacher teacher;
		private Unit unit;
		private School school;
		private Dept dept;
		private Clazz clazz;

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public Student getStudent() {
			return student;
		}

		public void setStudent(Student student) {
			this.student = student;
		}

		public Teacher getTeacher() {
			return teacher;
		}

		public void setTeacher(Teacher teacher) {
			this.teacher = teacher;
		}

		public Unit getUnit() {
			return unit;
		}

		public void setUnit(Unit unit) {
			this.unit = unit;
		}

		public School getSchool() {
			return school;
		}

		public void setSchool(School school) {
			this.school = school;
		}

		public Dept getDept() {
			return dept;
		}

		public void setDept(Dept dept) {
			this.dept = dept;
		}

		public Clazz getClazz() {
			return clazz;
		}

		public void setClazz(Clazz clazz) {
			this.clazz = clazz;
		}
	}

	private AhyhObject anayUserOrg(JSONObject json) {
		AhyhObject person = new AhyhObject();
		User user = new User();
		String schoolId = json.getString("school_id");
		String classId = json.getString("class_id");
		String eduorgId = json.getString("eduorg_id");
		String roleId = json.getString("role_id");
		if (StringUtils.isBlank(schoolId) && StringUtils.isBlank(eduorgId) && StringUtils.isBlank(roleId)) {
			return person;
		}
		person.setUser(user);
		String userId = StringUtils.leftPad(json.getString("user_id"), 32, "0");
		if (StringUtils.isNotBlank(eduorgId)) {
			user.setUnitId(Evn.getString("sync_ahyh_top_unit_id"));
			user.setId(userId);
		} else if (StringUtils.isNotBlank(schoolId)) {
			schoolId = StringUtils.leftPad(schoolId, 32, "0");
			classId = StringUtils.leftPad(classId, 32, "0");
			user.setUnitId(schoolId);
			person.setUser(user);
			if (StringUtils.equals("2300000001000000002", roleId)) {
				user.setOwnerType(User.OWNER_TYPE_STUDENT);
				Student student = new Student();
				student.setId(user.getId());
				student.setSchoolId(schoolId);
				student.setClassId(classId);
				student.setEnrollYear("0000-0000");
				student.setNowState("40");
				student.setStudentName(user.getRealName());
				student.setModifyTime(user.getModifyTime());
				student.setCreationTime(user.getCreationTime());
				student.setIsDeleted(StringUtils.equals("delete", json.getString("action")) ? 1 : 0);
				student.setSex(user.getSex());
				person.setStudent(student);

			} else if (StringUtils.equals("2300000001000000003", roleId)) {
				user.setOwnerType(User.OWNER_TYPE_TEACHER);
				Teacher teacher = new Teacher();
				teacher.setIsDeleted(StringUtils.equals("delete", json.getString("action")) ? 1 : 0);
				teacher.setTeacherName(user.getRealName());
				teacher.setDeptId(user.getDeptId());
				teacher.setId(user.getId());
				teacher.setTeacherCode("000000");
				teacher.setEventSource(0);
				teacher.setSex(user.getSex());
				teacher.setModifyTime(user.getModifyTime());
				teacher.setCreationTime(user.getCreationTime());
				person.setTeacher(teacher);
			}
		}

		return person;

	}

	private AhyhObject anaySchool(JSONObject json) {
		AhyhObject ahyhObject = new AhyhObject();
		School school = new School();
		JSONObject dataJson = json.getJSONObject("data");
		school.setId(StringUtils.leftPad(json.getString("id"), 32, "0"));
		school.setSchoolName(dataJson.getString("schoolName"));
		school.setSchoolCode(dataJson.getString("schoolCode"));
		school.setAddress(dataJson.getString("address"));
		// school.setMobilePhone(dataJson.getString("mobile"));
		school.setRegionCode("340102");
		school.setCreationTime(new Date());
		school.setModifyTime(new Date());
		school.setIsDeleted(StringUtils.equals("delete", json.getString("action")) ? 1 : 0);

		Unit unit = new Unit();
		unit.setId(school.getId());
		unit.setUnitName(school.getSchoolName());
		unit.setIsDeleted(school.getIsDeleted());
		unit.setRegionCode(school.getRegionCode());
		unit.setUnitClass(Unit.UNIT_CLASS_SCHOOL);
		unit.setParentId(Evn.getString("sync_ahyh_top_unit_id"));
		unit.setCreationTime(school.getCreationTime());
		unit.setModifyTime(school.getModifyTime());

		Dept dept = new Dept();
		dept.setId("D" + StringUtils.substring(school.getId(), 1));
		dept.setDeptName("默认部门");
		dept.setCreationTime(school.getCreationTime());
		dept.setModifyTime(school.getModifyTime());
		dept.setDeptType(1);
		dept.setIsDeleted(school.getIsDeleted());
		dept.setUnitId(unit.getId());
		dept.setDeptState(Dept.DeptStateEnum.VALIDATE.getState());

		ahyhObject.setSchool(school);
		ahyhObject.setUnit(unit);
		ahyhObject.setDept(dept);
		return ahyhObject;
	}

	private User anayUser(JSONObject json) {
		User user = new User();
		JSONObject dataJson = json.getJSONObject("data");
		user.setId(StringUtils.leftPad(json.getString("id"), 32, "0"));
		user.setSequence((int) NumberUtils.toLong(StringUtils.substring(user.getId(), 22)));
		user.setIsDeleted(StringUtils.equals("delete", json.getString("action")) ? 1 : 0);
		user.setSex(NumberUtils.toInt(dataJson.getString("gender")));
		user.setOwnerId(user.getId());
		user.setAddress(dataJson.getString("homeAddress"));
		// user.setIdentityCard(dataJson.getString("idCardNo"));
		user.setUsername(dataJson.getString("loginName"));
		// user.setMobilePhone(dataJson.getString("mobile"));
		user.setRealName(dataJson.getString("userName"));
		user.setRegionCode("000000");
		user.setPassword(PWD.encodeIfNot("12345678"));
		user.setOwnerType(0);
		user.setCreationTime(new Date());
		user.setModifyTime(new Date());
		user.setUnitId(Constant.GUID_ZERO);
		// 因为没有部门信息，所以默认的部门id与单位id一致，采用相同的内容
		user.setDeptId(user.getUnitId());
		return user;
	}
}
