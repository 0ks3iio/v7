package net.zdsoft.bigdata.data.dao;

import net.zdsoft.bigdata.data.entity.Api;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author ke_shen@126.com
 * @since 2018/4/8 下午5:39
 */
public interface ApiDao extends BaseJpaRepositoryDao<Api, String> {
	
	@Query("From Api where unitId = ?1 order by modifyTime desc")
	public List<Api> findApisByUnitId(String unitId);

	List<Api> findApisByUnitIdAndNameLikeAndRemarkLikeOrderByModifyTimeDesc(String unitId, String name, String remark);
}
