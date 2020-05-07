package net.zdsoft.basedata.service;

import java.util.List;

import com.alibaba.fastjson.JSONArray;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;

public interface TreeService {

    public void findUnitZTreeJson(String pId, boolean isOpen, JSONArray jsonArr, List<Unit> list);

    public void findGradeZTreeJson(String pId, boolean isOpen, JSONArray jsonArr, List<Grade> list, boolean isParent);

    public void findClassZTreeJson(String pId, boolean isOpen, JSONArray jsonArr, List<Clazz> list, boolean isParent);

    public void findDeptZTreeJson(String pId, boolean isOpen, JSONArray jsonArr, List<Dept> list, boolean isParent);

    public void findStudentZTreeJson(String pId, boolean isOpen, JSONArray jsonArr, List<Student> list);

    /**
     * 某单位年级
     * 
     * @param unitIds
     * @return
     */
    public JSONArray gradeForSchoolInsetZTree(String... unitIds);

    /**
     * 直属单位年级
     * 
     * @param unitIds
     * @return
     */
    public JSONArray gradeForUnitInsetZTree(String... unitIds);

    /**
     * 某单位年级-班级
     * 
     * @param unitIds
     * @return
     */
    public JSONArray gradeClassForSchoolInsetZTree(String... unitIds);

    /**
     * 某单位年级-班级-学生
     * 
     * @param unitIds
     * @return
     */
    public JSONArray gradeClassStudentForSchoolInsetZTree(String... unitIds);

    /**
     * 直属单位年级-班级
     * 
     * @param unitIds
     * @return
     */
    public JSONArray gradeClassForUnitInsetZTree(String... unitIds);

    /**
     * 直属单位
     * 
     * @param isSchool
     *            是否只显示学校
     * @param unitIds
     * @return
     */
    public JSONArray unitForDirectInsetZTree(boolean isSchool, String... unitIds);
    /**
     * 某单位部门
     * 
     * @param unitIds
     * @return
     */
    public JSONArray deptForUnitInsetZTree(String... unitIds);

    /**
     * 某单位部门 - 教师
     * 
     * @param unitIds
     * @return
     */
    public JSONArray deptTeacherForUnitInsetZTree(String... unitIds);

    /**
     * 直属单位部门
     * 
     * @param unitIds
     * @return
     */
    public JSONArray deptForDirectUnitInsetZTree(String... unitIds);

    /**
     * @param pId
     * @param isOpen
     * @param jsonArr
     * @param list
     */
    void findTeacherZTreeJson(String pId, boolean isOpen, JSONArray jsonArr, List<Teacher> list);
    /**
     * 某单位年级-班级
     * @param unitIds
     * @param sections 学段
     * @param gradeCodes 年级代码
     * @return
     */
	public JSONArray gradeClassForSchoolInsetZTree(
			String sections, String gradeCodes,String... unitIds);
	/**
	 * 场地
	 * @param unitId
	 * @return
	 */
	public JSONArray placeForBuildingTree(String unitId);

	/**
	 * 本单位-用户类型-用户
	 * @param array
	 * @return
	 */
	public JSONArray ownerTypeUserForUnitInsetTree(String... unitIds);

	/**
	 * 角色类型-角色
	 * @param unitId
	 * @param ownerType
	 * @param unitClass
	 * @return
	 */
	public JSONArray typeServerRoleForUserInsetTree(String unitId,Integer ownerType,Integer unitClass);

	/**
     * 教研组教师
     * 
     * @param unitId
     * @param teacherId
     * @return
     */
	public JSONArray teacherForGroupTree(String unitId, String teacherId);

    /**
     * 某单位年级-班级-学生
     * 
     * @param classIds
     * @return
     */
	public JSONArray gradeClassStudentForClazzInsetZTree(String[] classIds);

}
