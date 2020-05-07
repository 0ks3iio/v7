package net.zdsoft.teaeaxam.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.teaeaxam.entity.ExamCenterSchool;

@Repository("examCenterSchoolDao")
public interface ExamCenterSchoolDao extends BaseJpaRepositoryDao<ExamCenterSchool, String> {

	@Query("FROM ExamCenterSchool WHERE type IN (?1) ORDER BY CODE")
	public List<ExamCenterSchool> findSchoolByTypes(String[] types);
}
