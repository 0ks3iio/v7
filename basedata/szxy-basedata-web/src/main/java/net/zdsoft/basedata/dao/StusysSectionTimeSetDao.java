package net.zdsoft.basedata.dao;

import java.util.List;

import net.zdsoft.basedata.entity.StusysSectionTimeSet;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Query;

public interface StusysSectionTimeSetDao extends BaseJpaRepositoryDao<StusysSectionTimeSet, String> {
	
	@Query("From StusysSectionTimeSet where acadyear =?1 and semester = ?2 and unitId = ?3 and isDeleted=0 order by sectionNumber ")
    public List<StusysSectionTimeSet> findByAcadyearAndSemesterAndUnitId(String acadyear, Integer semester, String unitId);

	@Query("From StusysSectionTimeSet where isDeleted=0  and unitId in ?1 ")
	public List<StusysSectionTimeSet> findByUnitIdIn(String[] unitIds);
}
