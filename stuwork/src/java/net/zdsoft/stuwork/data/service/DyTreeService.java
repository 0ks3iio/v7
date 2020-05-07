package net.zdsoft.stuwork.data.service;

import java.util.List;

import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Unit;

import com.alibaba.fastjson.JSONArray;

public interface DyTreeService {

	public void findUnitZTreeJson(String pId, boolean isOpen, JSONArray jsonArr,List<Unit> list);
	public void findGradeZTreeJson(String pId, boolean isOpen, JSONArray jsonArr,List<Grade> list, boolean isParent);
	public void findClassZTreeJson(String pId, boolean isOpen, JSONArray jsonArr,List<Clazz> list, boolean isParent);
	public void findStudentZTreeJson(String pId, boolean isOpen, JSONArray jsonArr,List<Student> list);

	
	public JSONArray gradeClassForSchoolInsetZTree(String unitId,String userId);
	
	public JSONArray gradeClassStudentForSchoolInsetZTree(String unitId,String userId);





}
