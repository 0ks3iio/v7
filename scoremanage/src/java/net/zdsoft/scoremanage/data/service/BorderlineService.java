package net.zdsoft.scoremanage.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.scoremanage.data.entity.Borderline;
import net.zdsoft.scoremanage.data.entity.ExamInfo;

public interface BorderlineService extends BaseService<Borderline, String>{

	int saveCopy(ExamInfo sourceExamInfo, ExamInfo oldExamInfo);

	List<Borderline> findBorderlineListByExamId(String examId,String gradeCode);

	List<Borderline> findBorderlineList(String examId, String gradeCode, String... courseIds);
	
	List<Borderline> findBorderlineList(String examId, String gradeCode,String statType, String courseId);

	/**
	 * 找总分数线\段设置
	 * @param examId
	 * @param gradeCode TODO
	 * @param statType
	 * @return
	 */
	List<Borderline> findSubjectId32(String examId, String gradeCode, String... statType);

	List<Borderline> saveAllEntitys(Borderline... borderline);

	void deleteAllByIds(String... id);

}
