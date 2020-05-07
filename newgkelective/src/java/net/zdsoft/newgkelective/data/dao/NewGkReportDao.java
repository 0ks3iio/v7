package net.zdsoft.newgkelective.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkReport;

public interface NewGkReportDao extends BaseJpaRepositoryDao<NewGkReport, String>{

	@Query("From NewGkReport r where r.unitId in (?1) and openAcadyear =?2 ")
	List<NewGkReport> findListBy(String[] unitIds, String openAcadyear);

	@Query("From NewGkReport where unitId=?1 and gradeId=?2 and isChosen=1")
    NewGkReport findOneChoseByGradeId(String unitId, String gradeId);
	@Query("select distinct openAcadyear From NewGkReport ")
	List<String> findAllOpenAcadyear();

	@Query("From NewGkReport where unitId=?1 and gradeId=?2")
    NewGkReport findOneByGradeId(String unitId, String gradeId);
}
