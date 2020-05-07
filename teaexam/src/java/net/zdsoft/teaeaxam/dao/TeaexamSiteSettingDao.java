package net.zdsoft.teaeaxam.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.teaeaxam.entity.TeaexamSiteSetting;

/**
 * 
 * @author weixh
 * 2018年10月26日	
 */
public interface TeaexamSiteSettingDao extends BaseJpaRepositoryDao<TeaexamSiteSetting, String>{
	@Query("FROM TeaexamSiteSetting WHERE examId=?1 ORDER BY creationTime")
	public List<TeaexamSiteSetting> findSettingByExamId(String examId);
	
	@Query("FROM TeaexamSiteSetting WHERE subjectInfoId=?1 ORDER BY creationTime")
	public List<TeaexamSiteSetting> findSettingBySubInfoId(String subInfoId);

}
