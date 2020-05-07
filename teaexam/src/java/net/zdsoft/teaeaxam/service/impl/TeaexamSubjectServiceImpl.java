package net.zdsoft.teaeaxam.service.impl;

import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.teaeaxam.dao.TeaexamSubjectDao;
import net.zdsoft.teaeaxam.entity.TeaexamSubject;
import net.zdsoft.teaeaxam.service.TeaexamSubjectService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("teaexamSubjectService")
public class TeaexamSubjectServiceImpl extends BaseServiceImpl<TeaexamSubject,String> implements TeaexamSubjectService{
    @Autowired
	private TeaexamSubjectDao teaexamSubjectDao;
	
	@Override
	protected BaseJpaRepositoryDao<TeaexamSubject, String> getJpaDao() {
		return teaexamSubjectDao;
	}

	@Override
	protected Class<TeaexamSubject> getEntityClass() {
		return TeaexamSubject.class;
	}

	@Override
	public List<TeaexamSubject> findByExamIds(String[] examIds) {
		return teaexamSubjectDao.findByExamIds(examIds);
	}

}
