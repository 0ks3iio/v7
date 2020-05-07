package net.zdsoft.bigdata.system.dao;

import java.util.List;

import net.zdsoft.bigdata.system.entity.BgHelp;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Query;

public interface BgHelpDao extends BaseJpaRepositoryDao<BgHelp, String> {

	@Query("From BgHelp order by orderId ")
	public List<BgHelp> findAllHelpList();
	
	@Query("From BgHelp where moduleId =?1 order by orderId ")
	public List<BgHelp> findHelpListByModuleId(String moduleId);

	@Query("From BgHelp where core =?1 order by orderId ")
	public List<BgHelp> findHelpListByCore(Integer core);
	
    @Query("select max(orderId) from BgHelp")
    public Integer getMaxOrderId();
}
