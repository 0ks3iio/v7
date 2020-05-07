package net.zdsoft.eclasscard.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.eclasscard.data.entity.EccInOutAttance;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface EccInOutAttanceDao extends BaseJpaRepositoryDao<EccInOutAttance, String>{
	
	@Query("From EccInOutAttance Where unitId = ?1 and periodId = ?2 and studentId =?3 and to_char(clockDate,'yyyy-MM-dd')= ?4")
	EccInOutAttance findByUnitIdAndPeriodIdAndStudentIdToDay(String unitId,String periodId,String studentId,String today);
	
	@Query("From EccInOutAttance Where unitId = ?1 and periodId = ?2 and classId =?3 and to_char(clockDate,'yyyy-MM-dd')= ?4")
	List<EccInOutAttance> findByPeriodIdAndClassIdToday(String unitId, String periodId, String classId,String today);
}
