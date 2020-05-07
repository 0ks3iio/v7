package net.zdsoft.comprehensive.dao;

import java.util.List;

import net.zdsoft.comprehensive.entity.CompreInfo;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Query;

public interface CompreInfoDao extends BaseJpaRepositoryDao<CompreInfo, String>{

	public List<CompreInfo> findByUnitIdAndAcadyearAndSemester(String unitId,String searchAcadyear, String searchSemester);

	

//分割线，以上为原有方法，方便后续删除	
	@Query("from CompreInfo where gradeId = ?1 and acadyear=?2 and semester= ?3 ")
	public CompreInfo findByGradeId(String gradeId, String acadyear, String semester);
	
	@Query("from CompreInfo where unitId = ?1 and gradeId in (?2) order by gradeCode")
	public List<CompreInfo> findByUnitIdAndGradeIdIn(String unitId, String[] gradeIds);

    @Query("from CompreInfo where unitId = ?1 and gradeId = ?2 and gradeCode = ?3")
    public CompreInfo findOneByGradeIdAndGradeCode(String unitId, String gradeId, String gradeCode);

}
