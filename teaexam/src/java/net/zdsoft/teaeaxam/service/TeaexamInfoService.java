package net.zdsoft.teaeaxam.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.teaeaxam.entity.TeaexamInfo;
import net.zdsoft.teaeaxam.entity.TeaexamSubject;

public interface TeaexamInfoService extends BaseService<TeaexamInfo, String>{

	public List<TeaexamInfo> findByInfoYearType(String unitId, int year, int type);
	
	/**
	 * 已发布的包含该学校的考试
	 * @param schoolId 包含该学校的考试
	 * @return
	 */
	public List<TeaexamInfo> findByInfoYearSchoolId(int year, String schoolId);
	
	/**
	 * 已发布的包含该学校的考试
	 * @param schoolId 包含该学校的考试
	 * @param type=-1，则查询所有
	 * @return
	 */
	public List<TeaexamInfo> findByInfoYearTypeSchoolId(int year, int type, String schoolId);
	
	public List<TeaexamInfo> findByUnitId(String unitId);
		
	public void saveTeaexamInfo(TeaexamInfo teaexamInfo, List<TeaexamSubject> subjectList);
	
	public void deleteTeaexamInfo(String examId);
	
	public List<TeaexamInfo> findByRegisterTime(String registerTime);
	
	public List<TeaexamInfo> findByRegisterEnd(int year, int type, String registerTime);
	
	public List<TeaexamInfo> findByRegisterTimeAndUnitId(String unitId, int type, String registerTime);
	
	public List<TeaexamInfo> findByEndTime(int year, int type,
			String examEndTime, String unitId);
}
