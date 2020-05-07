package net.zdsoft.scoremanage.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.scoremanage.data.entity.Filtration;

public interface FiltrationDao extends BaseJpaRepositoryDao<Filtration, String>{

	public List<Filtration> findByExamIdAndSchoolIdAndType(String examId,
			String unitId, String type);

	public void deleteByExamIdAndTypeAndStudentIdIn(String examId, String type,String[] studentIds);

	@Query("From Filtration where examId = ?1 and schoolId in (?2)")
	public List<Filtration> findListBySchoolIds(String examId, String... schoolIds);

	public List<Filtration> findBySchoolIdAndStudentIdAndType(String unitId, String studentId, String type);

}
