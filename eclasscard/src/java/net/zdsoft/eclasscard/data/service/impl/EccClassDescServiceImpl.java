package net.zdsoft.eclasscard.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.eclasscard.data.dao.EccClassDescDao;
import net.zdsoft.eclasscard.data.entity.EccClassDesc;
import net.zdsoft.eclasscard.data.service.EccClassDescService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service("eccClassDescService")
public class EccClassDescServiceImpl extends BaseServiceImpl<EccClassDesc, String> implements
		EccClassDescService {

	@Autowired
	private EccClassDescDao eccClassDescDao;
	
	@Override
	protected BaseJpaRepositoryDao<EccClassDesc, String> getJpaDao() {
		return eccClassDescDao;
	}

	@Override
	protected Class<EccClassDesc> getEntityClass() {
		return EccClassDesc.class;
	}


}
