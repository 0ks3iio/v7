package net.zdsoft.bigdata.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.dao.DataModelFavoriteParamDao;
import net.zdsoft.bigdata.data.entity.DataModelFavoriteParam;
import net.zdsoft.bigdata.data.service.DataModelFavoriteParamService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DataModelFavoriteParamServiceImpl extends BaseServiceImpl<DataModelFavoriteParam, String> implements DataModelFavoriteParamService {

	@Resource
	private DataModelFavoriteParamDao dataModelFavoriteParamDao;

	@Override
	protected BaseJpaRepositoryDao<DataModelFavoriteParam, String> getJpaDao() {
		return dataModelFavoriteParamDao;
	}

	@Override
	protected Class<DataModelFavoriteParam> getEntityClass() {
		return DataModelFavoriteParam.class;
	}

	@Override
	public void deleteByFavoriteId(String favoriteId) {
		dataModelFavoriteParamDao.deleteByFavoriteId(favoriteId);
	}
}
