package net.zdsoft.gkelective.remote.service;

import net.zdsoft.basedata.remote.service.BaseRemoteService;
import net.zdsoft.gkelective.data.entity.GkResult;

public interface GkResultRemoteService extends BaseRemoteService<GkResult,String>{
	
	/**
	 * 根据年级id获取选考科目列表
	 * @param gradeId
	 * @return
	 */
	public String getGkResultByGradeId(String gradeId);
	
}
