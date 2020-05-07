package net.zdsoft.bigdata.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.dao.ModelDatasetUserDao;
import net.zdsoft.bigdata.data.entity.ModelDatasetUser;
import net.zdsoft.bigdata.data.service.ModelDatasetUserService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ModelDatasetUserServiceImpl extends BaseServiceImpl<ModelDatasetUser, String> implements ModelDatasetUserService {

	@Resource
	private ModelDatasetUserDao modelDatasetUserDao;

	@Override
	protected BaseJpaRepositoryDao<ModelDatasetUser, String> getJpaDao() {
		return modelDatasetUserDao;
	}

	@Override
	protected Class<ModelDatasetUser> getEntityClass() {
		return ModelDatasetUser.class;
	}

	@Override
	public void deleteByModelDatasetId(String modelDatasetId, String unitId) {
		if (StringUtils.isBlank(unitId)) {
			modelDatasetUserDao.deleteByDsId(modelDatasetId);
			return;
		}
		modelDatasetUserDao.deleteByDsIdAndUnitId(modelDatasetId, unitId);
	}
}
