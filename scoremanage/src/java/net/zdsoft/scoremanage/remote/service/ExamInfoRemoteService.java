package net.zdsoft.scoremanage.remote.service;

import net.zdsoft.basedata.remote.service.BaseRemoteService;
import net.zdsoft.scoremanage.data.entity.ExamInfo;

public interface ExamInfoRemoteService extends BaseRemoteService<ExamInfo,String>{
	
	/**
	 * 根据条件获取本校参与过的考试
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param gradeId
	 * @return List<ExamInfo>
	 */
	public String findExamInfoListAll(String unitId, String acadyear, String semester,String gradeId);
	
}
