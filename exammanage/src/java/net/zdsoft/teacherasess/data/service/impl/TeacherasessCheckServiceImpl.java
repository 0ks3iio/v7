package net.zdsoft.teacherasess.data.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.teacherasess.data.dao.TeacherasessCheckDao;
import net.zdsoft.teacherasess.data.entity.TeacherAsessCheck;
import net.zdsoft.teacherasess.data.service.TeacherasessCheckService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("teacherasessCheckService")
public class TeacherasessCheckServiceImpl extends BaseServiceImpl<TeacherAsessCheck, String> implements TeacherasessCheckService{

	@Autowired
	private TeacherasessCheckDao teacherasessCheckDao;
	
	@Override
	public void deleteByAssessId(String teacherAsessId) {
		teacherasessCheckDao.deleteByAssessId(teacherAsessId);
	}
	
	@Override
	public Map<String,TeacherAsessCheck> getTeacherAsessLineByUnitIdAndAssessIdAndSubjectId(
			String unitId, String assessId, String subjectId) {
		Map<String,TeacherAsessCheck> teacherAsessCheckMap=new HashMap<>();
		List<TeacherAsessCheck> teacherAsessChecks=teacherasessCheckDao.getTeacherAsessLineByUnitIdAndAssessIdAndSubjectId(unitId, assessId, subjectId);
		teacherAsessCheckMap=EntityUtils.getMap(teacherAsessChecks, e->e.getClassId()+"_"+e.getAsessRankId());
		return teacherAsessCheckMap;
	}

	@Override
	protected BaseJpaRepositoryDao<TeacherAsessCheck, String> getJpaDao() {
		return teacherasessCheckDao;
	}

	@Override
	protected Class<TeacherAsessCheck> getEntityClass() {
		return TeacherAsessCheck.class;
	}

}
