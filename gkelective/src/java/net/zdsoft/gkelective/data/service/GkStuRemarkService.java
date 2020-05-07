package net.zdsoft.gkelective.data.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.gkelective.data.dto.ChosenSubjectSearchDto;
import net.zdsoft.gkelective.data.dto.GkStuScoreDto;
import net.zdsoft.gkelective.data.entity.GkStuRemark;

public interface GkStuRemarkService extends BaseService<GkStuRemark, String>{

	public void deleteByStudentIdsAndArrangeId(String subjectArrangeId, String type, String[] studentIds);

	public void saveAll(List<GkStuRemark> stuRemarkList);

	public List<GkStuRemark> findByStudentIds(String subjectArrangeId, String type, String[] studentIds);

	public List<GkStuScoreDto> findStuScoreDtoList(String arrangeId, ChosenSubjectSearchDto searchDto, Pagination page);

	public void saveStuScore(String unitId,String arrangeId, String searchExamId, String[] subjectIds);

	public List<GkStuRemark> findStuScoreList(String arrangeId, String[] subjectId, String[] studentId);
	/**
	 * key=stuid+subjectid value =score
	 * @param subjectArrangeId
	 * @param type
	 * @return
	 */
	public Map<String,Double> findByArrangeIdType(String subjectArrangeId, String type);

	public List<GkStuRemark> findByStudentIds(String subjectArrangeId, String[] types, String[] studentIds);
}
