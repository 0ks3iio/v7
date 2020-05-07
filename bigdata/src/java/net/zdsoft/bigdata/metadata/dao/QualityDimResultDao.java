package net.zdsoft.bigdata.metadata.dao;

import java.util.List;

import net.zdsoft.bigdata.metadata.entity.QualityDimResult;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface QualityDimResultDao extends BaseJpaRepositoryDao<QualityDimResult, String>{

	@Query("From QualityDimResult where type = ?1 order by statTime")
	public List<QualityDimResult> findQualityDimResultsByType(Integer type);
	
	@Query("From QualityDimResult where type = ?1 order by statTime desc")
	public List<QualityDimResult> findQualityDimResultsByTypeDesc(Integer type);
	
	@Query("From QualityDimResult where type = ?1 and status =?2 order by statTime")
	public List<QualityDimResult> findQualityDimResultsByTypeAndStatus(Integer type,Integer status);
	
	@Query("From QualityDimResult where type = ?1 and dimName =?2 order by statTime")
	public List<QualityDimResult> findQualityDimResultsByTypeAndDimName(Integer type,String dimName);
	
	@Query(value = "update bg_md_quality_dim_result set status =2", nativeQuery = true)
	@Modifying
	public void updateHistoryDataStatus();
	
	}
