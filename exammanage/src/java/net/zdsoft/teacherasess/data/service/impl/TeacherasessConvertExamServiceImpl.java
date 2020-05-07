package net.zdsoft.teacherasess.data.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.teacherasess.data.dao.TeacherasessConvertExamDao;
import net.zdsoft.teacherasess.data.entity.TeacherasessConvertExam;
import net.zdsoft.teacherasess.data.service.TeacherasessConvertExamService;

@Service("teacherasessConvertExamService")
public class TeacherasessConvertExamServiceImpl extends BaseServiceImpl<TeacherasessConvertExam, String> implements TeacherasessConvertExamService{
	@Autowired
	private TeacherasessConvertExamDao teacherasessConvertExamDao;
	
	@Override
	public List<TeacherasessConvertExam> findListByConvertIdInWithMaster(String[] convertIds) {
		return teacherasessConvertExamDao.findListByConvertIdInWithMaster(convertIds);
	}
	
	@Override
	protected BaseJpaRepositoryDao<TeacherasessConvertExam, String> getJpaDao() {
		return teacherasessConvertExamDao;
	}
	
	@Override
	protected Class<TeacherasessConvertExam> getEntityClass() {
		return TeacherasessConvertExam.class;
	}
	
}
