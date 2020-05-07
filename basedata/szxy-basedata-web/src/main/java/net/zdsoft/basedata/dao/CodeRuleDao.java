package net.zdsoft.basedata.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.basedata.entity.CodeRule;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

/**
 * @author yangsj  2017-1-24下午2:51:04
 */
public interface CodeRuleDao extends BaseJpaRepositoryDao<CodeRule, String> {

	/**
	 * @param section
	 * @param type
	 * @return
	 */
	@Query("From CodeRule where isDeleted = 0 and section = ?1 and codeType=?2 and isSystemInit=0 ")
	CodeRule findBySectionCodeType(String section, int type);

	/**
	 * @param type
	 * @return
	 */
	@Query("From CodeRule where isDeleted = 0 and  codeType=?1 and isSystemInit=0 ")
	List<CodeRule> findByCodeType(int type);

	/**
	 * @param schoolId
	 * @param codeType
	 * @return
	 */
	@Query("From CodeRule where isDeleted = 0 and  unitId=?1 and codeType=?2 ")
	CodeRule findByUnitIdCodeType(String schoolId, int codeType);

	
	
}
