package net.zdsoft.familydear.dao;

import java.util.List;

import net.zdsoft.familydear.entity.FamDearArrange;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Query;

/**
 * @ProjectName: eis
 * @Package: net.zdsoft.familydear.dao
 * @ClassName: FamDearArrangeDao
 * @Description: java类作用描述
 * @Author: Sweet
 * @CreateDate: 2019/5/7 9:07
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/5/7 9:07
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface FamDearArrangeDao extends BaseJpaRepositoryDao<FamDearArrange, String> {
	
	@Query("FROM FamDearArrange WHERE activityId in (?1)")
	public List<FamDearArrange> getFamilyDearArrangeList(String[] activitys);
	
	@Query("FROM FamDearArrange WHERE rural like ?1")
	public List<FamDearArrange> getFamilyDearArrangeListByContryName(String contryName);
}
