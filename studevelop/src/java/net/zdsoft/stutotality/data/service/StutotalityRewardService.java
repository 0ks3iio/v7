package net.zdsoft.stutotality.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stutotality.data.entity.StutotalityReward;

import java.util.List;

public interface StutotalityRewardService extends BaseService<StutotalityReward,String>{
	List<StutotalityReward> findByUnitId(String unitId);

	List<StutotalityReward> findByUnitIdAndAcadyearAndSemester(String unitId, String acadyear,String semester );

	List<StutotalityReward> findByUnitIdAndAcadyearAndSemesterWithMaster(String unitId, String acadyear,String semester );

	List<StutotalityReward> findByAcadyearAndSemesterAndUnitIdAndGradeId(String acadyear, String semester, String unitId, String gradeId);

	void deleteByUnitIdAndAcadyearAndSemester(String unitId, String acadyear,String semester );


}
