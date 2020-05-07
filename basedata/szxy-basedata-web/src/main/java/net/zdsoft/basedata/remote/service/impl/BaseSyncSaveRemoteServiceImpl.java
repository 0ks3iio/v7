package net.zdsoft.basedata.remote.service.impl;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.entity.ClassTeaching;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Family;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.SchoolSemester;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.StusysSectionTimeSet;
import net.zdsoft.basedata.entity.SubSchool;
import net.zdsoft.basedata.entity.TeachArea;
import net.zdsoft.basedata.entity.TeachBuilding;
import net.zdsoft.basedata.entity.TeachPlace;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.TeacherDuty;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.BaseSyncSaveRemoteService;
import net.zdsoft.basedata.service.BaseSyncSaveService;
import net.zdsoft.system.entity.user.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("baseSyncSaveRemoteService")
public class BaseSyncSaveRemoteServiceImpl implements BaseSyncSaveRemoteService {
    
	@Autowired
	private  BaseSyncSaveService baseSyncSaveService;

	@Override
	public void saveTeacher(Teacher[] teachers) {
		baseSyncSaveService.saveTeacher(teachers);
	}

	@Override
	public void saveUser(User[] users) {
		baseSyncSaveService.saveUser(users);
	}

	@Override
	public void saveStudent(Student[] students) {
		baseSyncSaveService.saveStudent(students);
	}

	@Override
	public void saveSchool(School[] schools) {
		// TODO Auto-generated method stub
		baseSyncSaveService.saveSchool(schools);
	}

	@Override
	public void saveSchool(School[] schools, Map<String, String> returnMsg,
			Boolean isSaveUnit) {
		// TODO Auto-generated method stub
		baseSyncSaveService.saveSchool(schools, returnMsg, isSaveUnit);
	}

	@Override
	public void saveUnit(Unit[] units) {
		// TODO Auto-generated method stub
		baseSyncSaveService.saveUnit(units);
	}

	@Override
	public void saveUnit(Unit[] units, Map<String, String> returnMsg,Boolean isSaveAdmin) {
		// TODO Auto-generated method stub
		baseSyncSaveService.saveUnit(units, returnMsg,isSaveAdmin);
	}

	@Override
	public void saveSchoolAndUnit(School[] schools, Unit[] units) {
		// TODO Auto-generated method stub
		baseSyncSaveService.saveSchoolAndUnit(schools, units);
	}

	@Override
	public void saveDept(Dept[] depts) {
		// TODO Auto-generated method stub
		baseSyncSaveService.saveDept(depts);
	}

	@Override
	public void saveDept(Dept[] depts, Map<String, String> returnMsg) {
		// TODO Auto-generated method stub
		baseSyncSaveService.saveDept(depts, returnMsg);
	}

	@Override
	public void saveTeacherDuty(TeacherDuty[] teacherDutys) {
		// TODO Auto-generated method stub
		baseSyncSaveService.saveTeacherDuty(teacherDutys);
	}

	@Override
	public void saveClass(Clazz[] clazzs) {
		// TODO Auto-generated method stub
		baseSyncSaveService.saveClass(clazzs);
	}

	@Override
	public void saveClass(Clazz[] clazzs, Map<String, String> returnMsg) {
		// TODO Auto-generated method stub
		baseSyncSaveService.saveClass(clazzs, returnMsg);
	}

	@Override
	public void saveGrade(Grade[] grades) {
		// TODO Auto-generated method stub
		baseSyncSaveService.saveGrade(grades);
	}

	@Override
	public void saveGrade(Grade[] grades, Map<String, String> returnMsg) {
		// TODO Auto-generated method stub
		baseSyncSaveService.saveGrade(grades, returnMsg);
	}

	@Override
	public void saveClassAndGrade(Clazz[] clazzs, Grade[] grades) {
		// TODO Auto-generated method stub
		baseSyncSaveService.saveClassAndGrade(clazzs, grades);
	}

	@Override
	public void saveTeacher(Teacher[] teachers, Map<String, String> returnMsg) {
		// TODO Auto-generated method stub
		baseSyncSaveService.saveTeacher(teachers, returnMsg);
	}

	@Override
	public void saveUser(User[] users, Map<String, String> returnMsg) {
		// TODO Auto-generated method stub
		baseSyncSaveService.saveUser(users, returnMsg);
	}

	@Override
	public void saveFamily(Family[] familys) {
		// TODO Auto-generated method stub
		baseSyncSaveService.saveFamily(familys);
	}

	@Override
	public void saveFamily(Family[] array, Map<String, String> returnMsg) {
		// TODO Auto-generated method stub
		baseSyncSaveService.saveFamily(array, returnMsg);
	}

	@Override
	public void saveStudent(Student[] students, Map<String, String> returnMsg) {
		// TODO Auto-generated method stub
		baseSyncSaveService.saveStudent(students, returnMsg);
	}

	@Override
	public void saveRole(Role[] roles) {
		// TODO Auto-generated method stub
		baseSyncSaveService.saveRole(roles);
	}

	@Override
	public void saveTeachArea(TeachArea[] teachAreas) {
		// TODO Auto-generated method stub
		baseSyncSaveService.saveTeachArea(teachAreas);
		
	}

	@Override
	public void saveTeachBuilding(TeachBuilding[] teachBuildings) {
		// TODO Auto-generated method stub
		baseSyncSaveService.saveTeachBuilding(teachBuildings);
	}

	@Override
	public void saveTeachPlace(TeachPlace[] teachPlaces) {
		// TODO Auto-generated method stub
		baseSyncSaveService.saveTeachPlace(teachPlaces);
	}

	@Override
	public void saveSubSchool(SubSchool[] subSchools) {
		// TODO Auto-generated method stub
		baseSyncSaveService.saveSubSchool(subSchools);
	}

	@Override
	public void saveSubSchool(SubSchool[] subSchools,
			Map<String, String> returnMsg) {
		// TODO Auto-generated method stub
		baseSyncSaveService.saveSubSchool(subSchools, returnMsg);
	}

	@Override
	public void saveSemester(Semester[] semesters) {
		// TODO Auto-generated method stub
		baseSyncSaveService.saveSemester(semesters);
	}

	@Override
	public void saveSemester(Semester[] semesters, Map<String, String> returnMsg) {
		// TODO Auto-generated method stub
		baseSyncSaveService.saveSemester(semesters, returnMsg);
	}

	@Override
	public void saveSchoolSemester(SchoolSemester[] schoolSemesters) {
		// TODO Auto-generated method stub
		baseSyncSaveService.saveSchoolSemester(schoolSemesters);
	}

	@Override
	public void saveCourse(Course[] courses) {
		// TODO Auto-generated method stub
		baseSyncSaveService.saveCourse(courses);
	}

	@Override
	public void saveCourse(Course[] courses, Map<String, String> returnMsg) {
		// TODO Auto-generated method stub
		baseSyncSaveService.saveCourse(courses, returnMsg);
	}

	@Override
	public void saveClassTeaching(ClassTeaching[] classTeachings) {
		// TODO Auto-generated method stub
		baseSyncSaveService.saveClassTeaching(classTeachings);
	}

	@Override
	public void saveClassTeaching(ClassTeaching[] classTeachings,
			Map<String, String> returnMsg) {
		// TODO Auto-generated method stub
		baseSyncSaveService.saveClassTeaching(classTeachings, returnMsg);
	}

	@Override
	public void saveUserToPassport(List<User> urs) {
		// TODO Auto-generated method stub
		baseSyncSaveService.saveUserToPassport(urs);
	}

	@Override
	public void saveStusysSectionTimeSet(StusysSectionTimeSet[] times) {
		// TODO Auto-generated method stub
		baseSyncSaveService.saveStusysSectionTimeSet(times);
	}

	@Override
	public void saveTeacherAndUser(Teacher[] teachers, User[] users) {
		// TODO Auto-generated method stub
		baseSyncSaveService.saveTeacherAndUser(teachers, users);
	}

	@Override
	public void saveTeacherAndUser(Teacher[] teachers, User[] users,
			Map<String, String> returnMsg) {
		// TODO Auto-generated method stub
		baseSyncSaveService.saveTeacherAndUser(teachers, users,returnMsg);
	}

	@Override
	public void saveStudentAndUser(Student[] students, User[] users) {
		// TODO Auto-generated method stub
		baseSyncSaveService.saveStudentAndUser(students, users);
	}

	@Override
	public void saveStudentAndUser(Student[] students, User[] users,
			Map<String, String> returnMsg) {
		// TODO Auto-generated method stub
		baseSyncSaveService.saveStudentAndUser(students, users, returnMsg);
	}

	@Override
	public void saveFamilyAndUser(Family[] familys, User[] users) {
		// TODO Auto-generated method stub
		baseSyncSaveService.saveFamilyAndUser(familys, users);
	}

	@Override
	public void saveUnitAndServerExtension(Map<String, String> serverUMap) {
		// TODO Auto-generated method stub
		baseSyncSaveService.saveUnitAndServerExtension(serverUMap);
	}
}
