package net.zdsoft.teacherasess.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.teacherasess.data.entity.TeacherasessConvert;

public interface TeacherasessConvertDao extends BaseJpaRepositoryDao<TeacherasessConvert, String>{
	
	@Query("From TeacherasessConvert where unitId = ?1 and acadyear = ?2 and isDeleted = 0 order by creationTime desc")
	public List<TeacherasessConvert> findByUnitIdAndAcadyearWithMaster(String unitId, String acadyear);
	
	@Query("From TeacherasessConvert where unitId = ?1 and acadyear = ?2 and gradeId=?3 and isDeleted = 0 and status=2 order by creationTime desc")
	public List<TeacherasessConvert> findListByAcadyearAndGradeId(String unitId,String acadyear,String gradeId);

}
