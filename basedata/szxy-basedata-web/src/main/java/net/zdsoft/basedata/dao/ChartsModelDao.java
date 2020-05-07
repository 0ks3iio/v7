package net.zdsoft.basedata.dao;

import java.util.List;

import net.zdsoft.basedata.entity.ChartsModel;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface ChartsModelDao extends BaseJpaRepositoryDao<ChartsModel, String>{

	List<ChartsModel> findByIsUsingAndModelIdAndTypeAndTagType(int isUsing1, Integer modelId, String modelType,
			String tagType);

	List<ChartsModel> findByIsUsingAndModelIdAndType(int isUsing1, Integer modelId, String modelType);

	List<ChartsModel> findByIsUsing(int isUsing);

}
