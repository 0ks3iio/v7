package net.zdsoft.gkelective.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.gkelective.data.dao.GkStuConversionDao;
import net.zdsoft.gkelective.data.entity.GkStuConversion;
import net.zdsoft.gkelective.data.service.GkStuConversionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("gkStuConversionService")
public class GkStuConversionServiceImpl extends BaseServiceImpl<GkStuConversion, String> implements GkStuConversionService{

	@Autowired
	private GkStuConversionDao gkStuConversionDao;
	
	@Override
	protected BaseJpaRepositoryDao<GkStuConversion, String> getJpaDao() {
		return gkStuConversionDao;
	}

	@Override
	protected Class<GkStuConversion> getEntityClass() {
		return GkStuConversion.class;
	}

	@Override
	public void saveAllEntitys(GkStuConversion... array) {
		gkStuConversionDao.saveAll(checkSave(array));
	}

}
