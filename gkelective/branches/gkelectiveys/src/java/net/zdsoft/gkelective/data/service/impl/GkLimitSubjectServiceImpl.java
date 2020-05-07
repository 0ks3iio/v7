package net.zdsoft.gkelective.data.service.impl;

import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.gkelective.data.dao.GkLimitSubjectDao;
import net.zdsoft.gkelective.data.entity.GkLimitSubject;
import net.zdsoft.gkelective.data.service.GkLimitSubjectService;
import net.zdsoft.gkelective.data.service.GkResultService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service("gkLimitSubjectService")
public class GkLimitSubjectServiceImpl extends BaseServiceImpl<GkLimitSubject, String> implements GkLimitSubjectService{
	@Autowired
	private GkLimitSubjectDao gkLimitSubjectDao;
	@Autowired
	private GkResultService gkResultService;
	@Override
	public List<GkLimitSubject> findBySubjectArrangeId(String subjectArrangeId) {
		return gkLimitSubjectDao.findBySubjectArrangeId(subjectArrangeId);
	}

	@Override
	public void deleteBySubjectArrangeId(String arrangeSubjectId) {
		gkLimitSubjectDao.deleteBySubjectArrangeId(arrangeSubjectId);
	}

	@Override
	protected BaseJpaRepositoryDao<GkLimitSubject, String> getJpaDao() {
		return gkLimitSubjectDao;
	}

	@Override
	protected Class<GkLimitSubject> getEntityClass() {
		return GkLimitSubject.class;
	}

	@Override
	public void saveLimitSubject(GkLimitSubject ent) {
		if(StringUtils.isNotBlank(ent.getSubjectVal()) && ent.getSubjectVal().indexOf(",")>0)
			gkResultService.deleteBySubjectArrangeIdAndSubjectId(ent.getSubjectArrangeId(), ent.getSubjectVal().split(","));
		
		this.checkSave(ent);
		
		this.save(ent);
	}

	
}
