package net.zdsoft.eclasscard.data.service;

import java.util.Date;
import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.eclasscard.data.entity.EccInOutAttance;

public interface EccInOutAttanceService extends BaseService<EccInOutAttance, String>{

	EccInOutAttance findByPeriodIdAndStudentIdToDay(String unitId, String periodId, String studentId,Date date);
	
	/**
	 * 班级已考勤学生
	 * @param unitId
	 * @param classId
	 * @param studentId
	 * @return
	 */
	List<EccInOutAttance> findByPeriodIdAndClassId(String unitId,String periodId, String classId);

	void addInOutAttenceQueue(String unitId);

	void InOutTaskRun(String bizId,String schoolId, boolean isEnd);

}
