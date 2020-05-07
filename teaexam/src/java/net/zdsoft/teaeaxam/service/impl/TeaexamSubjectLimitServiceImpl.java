package net.zdsoft.teaeaxam.service.impl;

import java.util.Date;
import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.teaeaxam.constant.TeaexamConstant;
import net.zdsoft.teaeaxam.dao.TeaexamSubjectLimitDao;
import net.zdsoft.teaeaxam.entity.TeaexamSubjectLimit;
import net.zdsoft.teaeaxam.service.TeaexamSubjectLimitService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service("teaexamSubjectLimitService")
public class TeaexamSubjectLimitServiceImpl extends BaseServiceImpl<TeaexamSubjectLimit,String> implements TeaexamSubjectLimitService{
    @Autowired
	private TeaexamSubjectLimitDao teaexamSubjectLimitDao;
	
	@Override
	protected BaseJpaRepositoryDao<TeaexamSubjectLimit, String> getJpaDao() {
		return teaexamSubjectLimitDao;
	}

	@Override
	protected Class<TeaexamSubjectLimit> getEntityClass() {
		return TeaexamSubjectLimit.class;
	}

	@Override
	public void saveLimit(String examId, String subjectId, String teacherIds) {
		if(StringUtils.isBlank(subjectId)){
			subjectId = TeaexamConstant.ALL_LIMIT;
			examId = TeaexamConstant.ALL_LIMIT;
		}
		teaexamSubjectLimitDao.deleteBySubjectId(subjectId);
		TeaexamSubjectLimit limit = new TeaexamSubjectLimit();
		if(StringUtils.isNotBlank(teacherIds)){
			limit.setExamId(examId);
			limit.setSubjectInfoId(subjectId);
			limit.setTeacherIds(teacherIds);
			limit.setId(UuidUtils.generateUuid());
			limit.setCreationTime(new Date());
			limit.setModifyTime(new Date());
			teaexamSubjectLimitDao.save(limit);
		}		
	}

	@Override
	public void deleteBySubjectId(String subjectId) {
		teaexamSubjectLimitDao.deleteBySubjectId(subjectId);
	}

	@Override
	public TeaexamSubjectLimit findByExamIdAndSubId(String examId,
			String subjectId) {
		return teaexamSubjectLimitDao.findByExamIdAndSubId(examId, subjectId);
	}

	@Override
	public List<TeaexamSubjectLimit> limitList(String[] examIds) {
		return teaexamSubjectLimitDao.limitList(examIds);
	}

	@Override
	public List<TeaexamSubjectLimit> findBySubjectIds(String[] subIds) {
		return teaexamSubjectLimitDao.findBySubjectIds(subIds);
	}

}
