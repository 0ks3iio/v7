package net.zdsoft.studevelop.data.dao;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.entity.StuDevelopQualityReportSet;

public interface StuDevelopQualityReportSetDao extends BaseJpaRepositoryDao<StuDevelopQualityReportSet, String>{
    @Query("From StuDevelopQualityReportSet where unitId = ?1 and acadyear = ?2 and semester = ?3 and section = ?4")
	public StuDevelopQualityReportSet findByAll(String unitId, String acadyear, String semester, int section);
	
}
