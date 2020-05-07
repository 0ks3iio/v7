package net.zdsoft.eclasscard.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.eclasscard.data.entity.EccTeaclzAttence;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface EccTeaclzAttenceDao extends BaseJpaRepositoryDao<EccTeaclzAttence, String>{
	@Query("From EccTeaclzAttence Where classAttId = ?1")
	public EccTeaclzAttence findByAttId(String classAttId);
	
	@Query("From EccTeaclzAttence Where classAttId in (?1)")
	public List<EccTeaclzAttence> findListByAttIds(String[] attIds);

	@Query("From EccTeaclzAttence Where (status = 1 or status = 2) and teacherId = ?1 and to_char(clockDate,'yyyy-MM-dd') >= ?2 and to_char(clockDate,'yyyy-MM-dd') <= ?3")
	public List<EccTeaclzAttence> findByTeacherIdSum(String teacherId,
			String bDate, String eDate);
	@Query("From EccTeaclzAttence Where (status = 1 or status = 2) and to_char(clockDate,'yyyy-MM-dd') >= ?1 and to_char(clockDate,'yyyy-MM-dd') <= ?2 and teacherId in (?3)")
	public List<EccTeaclzAttence> findByBtTimeInTeaIds(
			String bDate, String eDate,String[] teacherIds);

}
