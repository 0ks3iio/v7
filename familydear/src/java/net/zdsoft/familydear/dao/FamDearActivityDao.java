package net.zdsoft.familydear.dao;

import java.util.List;

import net.zdsoft.familydear.entity.FamDearActivity;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Query;

/**
 * @ProjectName: eis
 * @Package: net.zdsoft.familydear.dao
 * @ClassName: FamDearActivityDao
 * @Description: java类作用描述
 * @Author: Sweet
 * @CreateDate: 2019/5/7 9:09
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/5/7 9:09
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface FamDearActivityDao extends BaseJpaRepositoryDao<FamDearActivity, String> {
	
	@Query("FROM FamDearActivity WHERE state=1 and planId in (?1)")
	public List<FamDearActivity> findByPlanIds(String[] planIds);
	
	@Query("FROM FamDearActivity WHERE state=1 and title like '%'||?1||'%' and planId in (?2)")
	public List<FamDearActivity> findByNameAndPlanIds(String activityName,String[] planIds);

//	@Query("FROM FamDearActivity WHERE  planId in (?1)")
//	public List<FamDearActivity> findListByPlanId(String[] planIds);
}
