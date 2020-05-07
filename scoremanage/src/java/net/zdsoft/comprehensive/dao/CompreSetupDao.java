package net.zdsoft.comprehensive.dao;

import java.util.List;

import net.zdsoft.comprehensive.entity.CompreSetup;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CompreSetupDao extends BaseJpaRepositoryDao<CompreSetup, String>{
    @Query("From CompreSetup where unitId = ?1 and examId = ?2 and subjectId = ?3 and compreInfoId = ?4")
	public CompreSetup findByAll(String unitId, String examId, String subjectId, String infoId);
	@Modifying
	@Query("delete from CompreSetup where compreInfoId = ?1 ")
	void deleteByInfo(String id);

	@Modifying
	@Query("delete from CompreSetup where compreInfoId = ?1 and subjectId = ?2")
	void deleteByInfoIdAndSubId(String compreInfoId, String subjectId);
	
	@Query("From CompreSetup where unitId = ?1 and subjectId = ?2 and compreInfoId = ?3")
	public List<CompreSetup> findByUnitIdAndSubIdAndInfoId(String unitId,String subjectId, String comInfoId);
	
//分割线，以上为原有方法，方便后续删除
	
	@Query("From CompreSetup where unitId = ?1 and compreInfoId = ?2 order by type")
	public List<CompreSetup> findByUnitIdAndCompreInfoId(String unitId, String infoId);
	
	@Query("From CompreSetup where unitId = ?1 and compreInfoId = ?2 and type = ?3")
	public List<CompreSetup> findByUnitIdAndCompreInfoIdAndType(String unitId, String infoId, String type);
	
	@Modifying
	@Query("delete from CompreSetup where unitId = ?1 and compreInfoId = ?2 and type = ?3")
	public void deleteByInfoIdAndType(String unitId, String infoId, String type);

}
