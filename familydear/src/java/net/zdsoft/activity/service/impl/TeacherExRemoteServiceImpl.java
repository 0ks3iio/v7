package net.zdsoft.activity.service.impl;

import net.zdsoft.activity.dao.TeacherExDao;
import net.zdsoft.activity.entity.TeacherEx;
import net.zdsoft.activity.service.TeacherExRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("teacherExRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class TeacherExRemoteServiceImpl extends BaseServiceImpl<TeacherEx,String> implements TeacherExRemoteService{

	@Autowired
	private TeacherExDao teacherExDao;
	
	@Override
	protected BaseJpaRepositoryDao<TeacherEx, String> getJpaDao() {
		return teacherExDao;
	}

	@Override
	protected Class<TeacherEx> getEntityClass() {
		return TeacherEx.class;
	}
}
