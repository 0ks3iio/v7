package net.zdsoft.newgkelective.remote.service;

import java.util.List;

import net.zdsoft.basedata.remote.service.BaseRemoteService;
import net.zdsoft.newgkelective.data.entity.NewGkChoice;

public interface NewGkChoiceRemoteService extends BaseRemoteService<NewGkChoice, String> {

	/**
	 * 根据年级id查询选课
	 * 
	 * @param gradeId
	 * @return
	 */
	List<NewGkChoice> findListByGradeId(String gradeId);
}
