package net.zdsoft.comprehensive.dao;

import java.util.List;

import net.zdsoft.comprehensive.entity.CompreParameterInfo;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CompreParamInfoDao extends BaseJpaRepositoryDao<CompreParameterInfo, String>{

	

	@Modifying
	@Query("delete from CompreParameterInfo where mcPrefix = ?1  and mcSuffix= ?2 and unitId = ?3 and infoKey in (?4)")
	void delete(Integer mcPrefix, Integer mcSuffix, String unitId, String[] infoKeys);
	
	//分割线，以上为原有方法，方便后续删除
	//合并findByUnitIdAndInfoKeyIn与getCompreParamInfoList
	//List<CompreParameterInfo> findByUnitIdAndInfoKeyIn(String unitId,String[] infoKey);
	
	@Query("From CompreParameterInfo where unitId = ?1 and infoKey in (?2) order by infoKey,gradeScore,mcPrefix")
	List<CompreParameterInfo> getCompreParamInfoList(String unitId,
			String[] infoKey);
	
	@Modifying
	@Query("delete from CompreParameterInfo where unitId = ?1 and infoKey in (?2)")
	void deleteAll(String unitId,String[] infoKey);
	
	
	
}
