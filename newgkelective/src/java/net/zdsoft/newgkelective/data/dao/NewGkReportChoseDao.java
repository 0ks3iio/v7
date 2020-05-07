package net.zdsoft.newgkelective.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkReportChose;

public interface NewGkReportChoseDao extends BaseJpaRepositoryDao<NewGkReportChose, String>{

	@Query("From NewGkReportChose where reportId in (?1) and dataType =?2 and dataKeys =?3")
	List<NewGkReportChose> findByReportIdAndType(String[] reportIds, String dataType, String dataKeys);

	List<NewGkReportChose> findByReportIdInAndDataType(String[] reportIds, String dataType);

	void deleteByReportId(String reportId);
}
