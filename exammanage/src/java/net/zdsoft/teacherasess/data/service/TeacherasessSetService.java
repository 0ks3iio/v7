package net.zdsoft.teacherasess.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.teacherasess.data.entity.TeacherAsessRank;
import net.zdsoft.teacherasess.data.entity.TeacherAsessSet;

public interface TeacherasessSetService extends BaseService<TeacherAsessSet, String>{

	public List<TeacherAsessSet> findByUnitIdAndAsessIdAndSubjectId(String unitId,String asessId,String subjectId);
	
	public void saveAllEntity(String unitId,String asessId,String subjectId,List<TeacherAsessSet> teacherAsessSets,List<TeacherAsessRank> teacherAsessRanks);

	public List<TeacherAsessSet> findByUnitIdAndAsessId(String unitId, String teacherAsessId);
}
