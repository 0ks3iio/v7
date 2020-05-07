package net.zdsoft.familydear.dao;

import java.util.List;

import net.zdsoft.familydear.entity.FamDearPlan;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Query;

/**
 * @ProjectName: eis
 * @Package: net.zdsoft.familydear.dao
 * @ClassName: FamDearPlanDao
 * @Description: java类作用描述
 * @Author: Sweet
 * @CreateDate: 2019/5/6 11:20
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/5/6 11:20
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface FamDearPlanDao extends BaseJpaRepositoryDao<FamDearPlan, String> {
	
	@Query("From FamDearPlan where state=1 and unitId=?1 and year=?2 ")
	public List<FamDearPlan> getFamilyDearPlanList(String unitId , String year);
}
