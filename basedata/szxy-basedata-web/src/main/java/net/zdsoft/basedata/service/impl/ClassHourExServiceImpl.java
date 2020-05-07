package net.zdsoft.basedata.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dao.ClassHourExDao;
import net.zdsoft.basedata.entity.ClassHourEx;
import net.zdsoft.basedata.service.ClassHourExService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

@Service("classHourExService")
public class ClassHourExServiceImpl extends BaseServiceImpl<ClassHourEx, String> implements ClassHourExService{

	@Autowired
	private ClassHourExDao classHourExDao;
	
	@Override
	public List<ClassHourEx> findByClassHourIdIn(String[] classHourIds) {
		if(classHourIds == null || classHourIds.length==0){
			return new ArrayList<>();
		}
		return classHourExDao.findByClassHourIdIn(classHourIds);
	}

	@Override
	protected BaseJpaRepositoryDao<ClassHourEx, String> getJpaDao() {
		return classHourExDao;
	}

	@Override
	protected Class<ClassHourEx> getEntityClass() {
		return ClassHourEx.class;
	}

	@Override
	public void deleteUpdateById(String id) {
		classHourExDao.updateById(id);
	}

	@Override
	public void deleteUpdateByClassHourIdIn(String[] classHourIds) {
		classHourExDao.deleteUpdateByClassHourIdIn(classHourIds);
	}

}
