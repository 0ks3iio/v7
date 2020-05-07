package net.zdsoft.newgkelective.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkReportBase;

public interface NewGkReportBaseDao extends BaseJpaRepositoryDao<NewGkReportBase, String>{

	List<NewGkReportBase> findByReportIdIn(String[] reportIds);

	@Query("From NewGkReportBase where reportId in (?1) and dataKeyType in (?2)")
	List<NewGkReportBase> findByReportIdAndType(String[] reportIds, String[] types);

	void deleteByReportId(String reportId);

}
