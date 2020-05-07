package net.zdsoft.stutotality.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stutotality.data.entity.StutotalityReward;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * stutotality_reward 
 * @author 
 * 
 */
public interface StutotalityRewardDao extends BaseJpaRepositoryDao<StutotalityReward, String>{

	public List<StutotalityReward> findByUnitId(String unitId);

	List<StutotalityReward> findByAcadyearAndSemesterAndUnitIdAndGradeId(String acadyear,String semester,String unitId,String gradeId);

	public void deleteByUnitIdAndAcadyearAndSemester(String unitId, String acadyear,String semester );

	@Query("from StutotalityReward where unitId =?1 and acadyear =?2 and semester=?3 order by starNumber desc")
	public List<StutotalityReward> findByUnitIdAndAcadyearAndSemester(String unitId, String acadyear,String semester );
}
