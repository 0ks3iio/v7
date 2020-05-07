package net.zdsoft.newstusys.remote.service;

import com.alibaba.fastjson.JSONArray;

public interface StudentAbnormalFlowRemoteService {

	public String findAbnormalFlowStudent(String schoolId, String acadyear, String semester,String[] flowTypes);

	/**
	 * 返回在该学期复学，并满足条件（必须有休学记录，且新班级和原班级不同级）的学生
	 * @param schoolId
	 * @param acadyear
	 * @param semester
	 * @param section 可为null
	 * @return jsonobject的内容(stuId学生id，oldAcadyear休学学年，oldSemester休学学期，oldClsId原班级id，nowClsId复学后班级id，years新班级和原班级间隔年数)
	 */
	public JSONArray findRecoverStuBySemester(String schoolId, String acadyear, String semester, Integer section);
}
