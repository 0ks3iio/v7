package net.zdsoft.basedata.service;

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
import net.zdsoft.system.entity.user.Role;

/**
 * @author yangsj  2018年4月23日下午5:22:18
 * 
 * 基础数据进行保存
 */
public interface BaseSyncSaveService {
	
	void saveSchool(School[] schools);
	
	void saveSchool(School[] schools, Map<String, String> returnMsg,Boolean isSaveUnit);
	
	void saveUnit(Unit[] units);
	
	void saveUnit(Unit[] units,Map<String,String> returnMsg,Boolean isSaveAdmin);
	
	void saveSchoolAndUnit(School[] schools,Unit[] units);
	
    void saveDept(Dept[] depts);
    
	void saveDept(Dept[] depts, Map<String, String> returnMsg);
    
    void saveTeacherDuty(TeacherDuty[] teacherDutys);
    
    void saveClass(Clazz[] clazzs);
    
	void saveClass(Clazz[] clazzs, Map<String, String> returnMsg);
    
    void saveGrade(Grade[] grades);
    
    void saveGrade(Grade[] grades, Map<String, String> returnMsg);
    
    void saveClassAndGrade(Clazz[] clazzs,Grade[] grades);
    
    void saveTeacher(Teacher[] teachers);
    
	void saveTeacher(Teacher[] teachers, Map<String, String> returnMsg);
    
    void saveUser(User[] users);
    
    void saveUser(User[] users, Map<String, String> returnMsg);
    
    void saveFamily(Family[] familys);
    
	void saveFamily(Family[] array, Map<String, String> returnMsg);
    
    void saveStudent(Student[] students);
    
    void saveStudent(Student[] students, Map<String, String> returnMsg);
    
    void saveRole(Role[] roles);

	void saveTeachArea(TeachArea[] teachAreas);

	void saveTeachBuilding(TeachBuilding[] teachBuildings);

	void saveTeachPlace(TeachPlace[] teachPlaces);

	void saveSubSchool(SubSchool[] subSchools);
	
	void saveSubSchool(SubSchool[] subSchools, Map<String, String> returnMsg);
	
	void saveSemester(Semester[] semesters);

	void saveSemester(Semester[] semesters, Map<String, String> returnMsg);

	void saveSchoolSemester(SchoolSemester[] schoolSemesters);
	
	void saveCourse(Course[] courses);

	void saveCourse(Course[] courses,Map<String, String> returnMsg);
	
	void saveClassTeaching(ClassTeaching[] classTeachings);
	
	void saveClassTeaching(ClassTeaching[] classTeachings,Map<String, String> returnMsg);

	void saveUserToPassport(List<User> urs);

	void saveStusysSectionTimeSet(StusysSectionTimeSet[] times);

	void saveTeacherAndUser(Teacher[] teachers, User[] users);
	
	void saveTeacherAndUser(Teacher[] teachers, User[] users, Map<String, String> returnMsg);

	void saveStudentAndUser(Student[] students, User[] users);
	
	void saveStudentAndUser(Student[] students, User[] users, Map<String, String> returnMsg);

	void saveFamilyAndUser(Family[] families, User[] users);

	void saveFamilyAndUser(Family[] families, User[] users,
			Map<String, String> returnMsg);

	void saveUnitAndServerExtension(Map<String, String> serverUMap);

}
