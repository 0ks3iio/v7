package net.zdsoft.basedata.remote.service;

import com.alibaba.fastjson.JSONArray;

public interface TreeRemoteService {
	
	/**
	 * 某单位年级-班级树数据
	 * 
	 * @param unitIds
	 * @return JsonArray
	 */
	public String gradeClassForSchoolInsetZTree(String... unitIds);
	
	/**
	 * 某单位年级树数据
	 * 
	 * @param unitIds
	 * @return JsonArray
	 */
	public String gradeForSchoolInsetZTree(String... unitIds);
	
	/**
	 * 某单位部门树数据
	 * 
	 * @param unitIds
	 * @return JsonArray
	 */
	public String deptForUnitInsetZTree(String... unitIds);

	JSONArray deptTeacherForUnitInsetTree(String unitId);
}
