package net.zdsoft.basedata.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.basedata.entity.TeachGroup;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface TeachGroupDao extends BaseJpaRepositoryDao<TeachGroup, String>{

	@Query("select t from TeachGroup t where t.schoolId = ?1  and t.isDeleted = 0 order by t.orderId ")
	List<TeachGroup> findBySchoolId(String unitId);

	@Modifying
	@Query("update TeachGroup set isDeleted = 1 where id = ?1")
	void deleteById(String teachGroupId);
	
	@Modifying
	@Query("update TeachGroup set isDeleted = 1 where id in (?1)")
	void deleteByIdIn(String[] teachGroupIds);
	
	@Query("select t from TeachGroup t where t.schoolId = ?1 and t.subjectId in (?2) and  t.isDeleted = 0 order by t.orderId ")
	List<TeachGroup> findBySchoolIdAndSubjectIdIn(String unitId, String[] subids);

	@Query("select max(orderId) from TeachGroup where schoolId = ?1 ")
	Integer findMaxOrder(String unitId);

	void deleteBySubjectIdIn(String... subjectIds);
}
