package net.zdsoft.newgkelective.data.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkReportDivide;

public interface NewGkReportDivideDao extends BaseJpaRepositoryDao<NewGkReportDivide, String>{

	
	List<NewGkReportDivide> findByReportIdInAndSubjectId(String[] reportIds, String subjectId);

	List<NewGkReportDivide> findByReportIdIn(String[] reportIds);

	void deleteByReportId(String reportId);

}
