package net.zdsoft.teacherasess.data.service.impl;

import java.util.ArrayList;
import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.teacherasess.data.dao.TeacherasessConvertGroupDao;
import net.zdsoft.teacherasess.data.entity.TeacherasessConvertGroup;
import net.zdsoft.teacherasess.data.service.TeacherasessConvertGroupService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("teacherasessConvertGroupService")
public class TeacherasessConvertGroupServiceImpl extends BaseServiceImpl<TeacherasessConvertGroup, String> implements TeacherasessConvertGroupService{

	@Autowired
	private TeacherasessConvertGroupDao teacherasessConvertGroupDao;
	
	@Override
	public List<TeacherasessConvertGroup> findListByConvertIdInWithMaster(String[] convertIds){
		List<TeacherasessConvertGroup> groups=teacherasessConvertGroupDao.findListByConvertIdInWithMaster(convertIds);
		if(CollectionUtils.isNotEmpty(groups)){
			return groups;
		}
		return new ArrayList<>();
	}
	
	@Override
	protected BaseJpaRepositoryDao<TeacherasessConvertGroup, String> getJpaDao() {
		return teacherasessConvertGroupDao;
	}

	@Override
	protected Class<TeacherasessConvertGroup> getEntityClass() {
		return TeacherasessConvertGroup.class;
	}


}
