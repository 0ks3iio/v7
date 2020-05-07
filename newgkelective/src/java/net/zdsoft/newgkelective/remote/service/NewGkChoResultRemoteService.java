package net.zdsoft.newgkelective.remote.service;

import net.zdsoft.basedata.remote.service.BaseRemoteService;
import net.zdsoft.newgkelective.data.entity.NewGkChoResult;

public interface NewGkChoResultRemoteService extends BaseRemoteService<NewGkChoResult,String>{

	/**
	 * 查询某个年级选择某个科目的学生
	 * @param gradeId
	 * @param isXuankao 是：查询某个年级选择某个科目的学生 否：查询某个年级没有选择某个科目的学生
	 * @param subjectIds
	 * @return Map<String,Set<String>> 备注：返回null代表没有确定的选课方案
	 */
	public String findStuIdListBySubjectId(String gradeId,boolean isXuankao,String[] subjectIds);
}
