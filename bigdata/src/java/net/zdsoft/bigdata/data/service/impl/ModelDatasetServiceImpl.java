package net.zdsoft.bigdata.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.dao.ModelDatasetDao;
import net.zdsoft.bigdata.data.entity.ModelDataset;
import net.zdsoft.bigdata.data.entity.ModelDatasetUser;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.data.service.ModelDatasetService;
import net.zdsoft.bigdata.data.service.ModelDatasetUserService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ModelDatasetServiceImpl extends BaseServiceImpl<ModelDataset, String> implements ModelDatasetService {

	@Resource
	private ModelDatasetDao modelDatasetDao;
	@Resource
	private ModelDatasetUserService modelDatasetUserService;

	@Override
	protected BaseJpaRepositoryDao<ModelDataset, String> getJpaDao() {
		return modelDatasetDao;
	}

	@Override
	protected Class<ModelDataset> getEntityClass() {
		return ModelDataset.class;
	}

    @Override
    public List<ModelDataset> getByModelId(String modelId, String unitId) {
        return modelDatasetDao.findAllByModelIdAndUnitIdOrderByOrderId(modelId, unitId);
    }

	@Override
	public void saveModelDataset(ModelDataset modelDataset) throws BigDataBusinessException {
		if (modelDataset.getOrderId() == null) {
			modelDataset.setOrderId(0);
		}

		if (StringUtils.isBlank(modelDataset.getId())) {

			// 验证名称和code是否存在
			List<ModelDataset> listForName = this.findListBy(new String[]{"dsName", "modelId"}, new String[]{modelDataset.getDsName(), modelDataset.getModelId()});
			if (listForName.size() > 0) {
				throw new BigDataBusinessException("该名称已存在!");
			}

			modelDataset.setId(UuidUtils.generateUuid());
			modelDatasetDao.save(modelDataset);
			return;
		}

		// 验证名称和code是否存在
		List<ModelDataset> listForName = this.findListBy(new String[]{"dsName", "modelId"}, new String[]{modelDataset.getDsName(), modelDataset.getModelId()});
		if (listForName.size() > 0) {
			if (!listForName.get(0).getId().equals(modelDataset.getId())) {
				throw new BigDataBusinessException("该名称已存在!");
			}
		}

		modelDatasetDao.update(modelDataset, new String[]{"dsName", "orderId", "dsConditionSql"});
	}

	@Override
	public void authorization(String modelId, String modelDatasetId, String[] orderUserIdArray, String unitId) {
		//删除以前授权记录
		modelDatasetUserService.deleteByModelDatasetId(modelDatasetId, unitId);
		//保存授权
		List<ModelDatasetUser> mdus = new ArrayList<>();
		if (orderUserIdArray == null) {
			return;
		}
		for (String userId : orderUserIdArray) {
			ModelDatasetUser md = new ModelDatasetUser();
			md.setModelId(modelId);
			md.setUserId(userId);
			md.setUnitId(unitId);
			md.setDsId(modelDatasetId);
			md.setId(UuidUtils.generateUuid());
			mdus.add(md);
		}
		modelDatasetUserService.saveAll(mdus.toArray(new ModelDatasetUser[mdus.size()]));
	}

    @Override
    public void deleteDataModel(String id) {
        // 删除用户绑定关系
	    modelDatasetUserService.deleteByModelDatasetId(id, null);
	    // 删除数据集
        modelDatasetDao.deleteById(id);
    }

	@Override
	public List<ModelDataset> findAllByIdIn(String[] ids) {
		return modelDatasetDao.findAllByIdInOrderByOrderId(ids);
	}
}
