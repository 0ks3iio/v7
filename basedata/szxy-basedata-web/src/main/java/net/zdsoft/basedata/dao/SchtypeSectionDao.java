package net.zdsoft.basedata.dao;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.basedata.entity.SchtypeSection;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface SchtypeSectionDao extends BaseJpaRepositoryDao<SchtypeSection, String>{

	public static final String SQL_AFTER=" and isDeleted = 0";
	
	@Query("From SchtypeSection Where schoolType = ?1"+SQL_AFTER)
	public SchtypeSection findBySchoolType(String schoolType);
	
}
