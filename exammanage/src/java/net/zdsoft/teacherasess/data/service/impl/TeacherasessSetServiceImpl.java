package net.zdsoft.teacherasess.data.service.impl;

import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.teacherasess.data.dao.TeacherasessSetDao;
import net.zdsoft.teacherasess.data.entity.TeacherAsessRank;
import net.zdsoft.teacherasess.data.entity.TeacherAsessSet;
import net.zdsoft.teacherasess.data.service.TeacherasessRankService;
import net.zdsoft.teacherasess.data.service.TeacherasessSetService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("teacherasessSetService")
public class TeacherasessSetServiceImpl extends BaseServiceImpl<TeacherAsessSet, String> implements TeacherasessSetService{

	@Autowired
	private TeacherasessSetDao teacherasessSetDao;
	@Autowired
	private TeacherasessRankService teacherasessRankService;
	
	@Override
	public List<TeacherAsessSet> findByUnitIdAndAsessIdAndSubjectId(
			String unitId, String asessId, String subjectId) {
		return teacherasessSetDao.findByUnitIdAndAsessIdAndSubjectId(unitId, asessId, subjectId);
	}
	@Override
	public List<TeacherAsessSet> findByUnitIdAndAsessId(String unitId, String teacherAsessId) {
		return teacherasessSetDao.findByUnitIdAndAssessId(unitId, teacherAsessId);
	}
	@Override
	public void saveAllEntity(String unitId, String asessId, String subjectId,
			List<TeacherAsessSet> teacherAsessSets,
			List<TeacherAsessRank> teacherAsessRanks) {
		List<TeacherAsessRank> teaAsessRanks=teacherasessRankService.findByUnitIdAndAsessIdAndSubjectId(unitId, asessId, subjectId);
		teacherasessRankService.deleteAll(teaAsessRanks.toArray(new TeacherAsessRank[] {}));
		List<TeacherAsessSet> teaAsessSets=this.findByUnitIdAndAsessIdAndSubjectId(unitId, asessId, subjectId);
		this.deleteAll(teaAsessSets.toArray(new TeacherAsessSet[]{}));
		
		this.saveAll(teacherAsessSets.toArray(new TeacherAsessSet[]{}));
		teacherasessRankService.saveAll(teacherAsessRanks.toArray(new TeacherAsessRank[]{}));
	}

	@Override
	protected BaseJpaRepositoryDao<TeacherAsessSet, String> getJpaDao() {
		return teacherasessSetDao;
	}

	@Override
	protected Class<TeacherAsessSet> getEntityClass() {
		return TeacherAsessSet.class;
	}

}
