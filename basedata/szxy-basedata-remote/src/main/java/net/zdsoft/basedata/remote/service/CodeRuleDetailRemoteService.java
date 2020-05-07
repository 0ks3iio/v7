package net.zdsoft.basedata.remote.service;

import net.zdsoft.basedata.entity.CodeRuleDetail;

/**
 * @author yangsj  2017-1-24下午2:45:44
 */
public interface CodeRuleDetailRemoteService extends BaseRemoteService<CodeRuleDetail,String> {

	/**
	 * @param ruleId
	 * @return
	 */
	String findByRuleId(String ruleId);

}
