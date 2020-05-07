package net.zdsoft.basedata.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.basedata.entity.SubSchool;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface SubSchoolDao extends BaseJpaRepositoryDao<SubSchool, String> {

	@Query("From SubSchool where isDeleted = 0 and schoolId in ?1")
	List<SubSchool> findbySchoolIdIn(String... unitId);

}
