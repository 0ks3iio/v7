package net.zdsoft.bigdata.system.dao;

import java.util.Date;
import java.util.List;

import net.zdsoft.bigdata.system.entity.BgModule;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BgModuleDao extends BaseJpaRepositoryDao<BgModule, String> {

	@Query("From BgModule order by orderId ")
	public List<BgModule> findAllModuleList();

	@Query("From BgModule where parentId=?1 order by orderId ")
	public List<BgModule> findModuleListByParentId(String parentId);

	@Modifying
	@Query("update BgModule set mark=?2,modifyTime = ?3 Where id = ?1")
	public void updateMarkById(String id, Integer mark, Date modifyTime);
}
