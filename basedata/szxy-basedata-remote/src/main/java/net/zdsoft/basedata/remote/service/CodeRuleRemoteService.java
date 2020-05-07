package net.zdsoft.basedata.remote.service;

import net.zdsoft.basedata.entity.CodeRule;

/**
 * @author yangsj  2017-1-24下午2:38:27
 */
public interface CodeRuleRemoteService extends BaseRemoteService<CodeRule,String> {

	/**
	 * @param section
	 * @param type
	 * @return
	 */
	String findBySectionCodeType(String section, int type);

	/**
	 * @param type
	 * @return
	 */
	String findByCodeType(int type);

	/**
	 * @param schoolId
	 * @param codeType
	 * @return
	 */
	String findByUnitIdCodeType(String schoolId, int codeType);

	 
}
