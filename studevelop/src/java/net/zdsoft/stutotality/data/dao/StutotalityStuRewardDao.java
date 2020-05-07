package net.zdsoft.stutotality.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stutotality.data.entity.StutotalityStuReward;

import java.util.List;

/**
 * stutotality_stu_reward 
 * @author 
 * 
 */
public interface StutotalityStuRewardDao extends BaseJpaRepositoryDao<StutotalityStuReward, String>{
	List<StutotalityStuReward> getByAcadyearAndSemesterAndUnitIdAndStudentId(String year,String semester,String unitId,String studentId);
}
