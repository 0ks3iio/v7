package net.zdsoft.teacherasess.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.teacherasess.data.dto.RankDto;
import net.zdsoft.teacherasess.data.entity.TeacherAsessRank;

public interface TeacherasessRankService extends BaseService<TeacherAsessRank, String>{

	public List<TeacherAsessRank> findByUnitIdAndAsessIdAndSubjectId(String unitId,String asessId,String subjectId);

	public List<TeacherAsessRank> findByUnitIdAndAssessId(String unitId, String teacherAsessId);
	
	public List<RankDto> findRankDtoByUnitIdAndAsessIdAndSubjectId(String unitId,String asessId,String subjectId);
	
	public List<RankDto> findCheckRankDtoByUnitIdAndAsessIdAndSubjectId(String unitId,String asessId,String subjectId);
}
