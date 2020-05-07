package net.zdsoft.career.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.career.data.entity.CarPlanResult;
import net.zdsoft.framework.entity.Pagination;

public interface CarPlanResultService extends BaseService<CarPlanResult,String>{

	public List<CarPlanResult> findByStudentIds(String[] studentIds);

	public void saveTestResult(CarPlanResult testResult,String unitId,String studentId);

	public List<CarPlanResult> findAllByTimeAndName(String unitId,String classId,
			String studentName, Pagination page);

	public void deleteTest(String studentIds,String unitId);
}
