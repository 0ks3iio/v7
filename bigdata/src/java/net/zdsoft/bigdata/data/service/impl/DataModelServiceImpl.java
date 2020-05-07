package net.zdsoft.bigdata.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.OrderType;
import net.zdsoft.bigdata.data.dao.DataModelDao;
import net.zdsoft.bigdata.data.entity.DataModel;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.data.service.DataModelParamService;
import net.zdsoft.bigdata.data.service.DataModelService;
import net.zdsoft.bigdata.data.service.SubscribeService;
import net.zdsoft.bigdata.metadata.service.MetadataRelationService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class DataModelServiceImpl extends BaseServiceImpl<DataModel, String> implements DataModelService {

	@Resource
	private DataModelDao dataModelDao;
	@Resource
	private SubscribeService subscribeService;
	@Resource
	private DataModelParamService dataModelParamService;
	@Resource
	private MetadataRelationService metadataRelationService;

	@Override
	protected BaseJpaRepositoryDao<DataModel, String> getJpaDao() {
		return dataModelDao;
	}

	@Override
	protected Class<DataModel> getEntityClass() {
		return DataModel.class;
	}

	@Override
	public List<DataModel> findAll(String unitId) {
		return dataModelDao.findAllByUnitIdOrderByOrderId(unitId);
	}

	@Override
	public void saveDataModel(DataModel dataModel, String[] orderUnits, String[] orderUsers) {
		dataModelDao.update(dataModel, new String[]{"orderType"});
		if (OrderType.UNIT_ORDER.getOrderType()> dataModel.getOrderType()) {
			subscribeService.deleteByBusinessId(new String[]{dataModel.getId()});
		}
		subscribeService.addAuthorization(new String[]{dataModel.getId()}, orderUnits, orderUsers, dataModel.getUnitId(), dataModel.getOrderType());
	}

	@Override
	public void saveDataModel(DataModel dataModel) throws BigDataBusinessException {
		if (dataModel.getType().equals("kylin")) {
		} else {
			dataModel.setProject(null);
		}

		if (dataModel.getOrderId() == null) {
			dataModel.setOrderId(0);
		}

		if (StringUtils.isBlank(dataModel.getId())) {

			// 验证名称和code是否存在
			long numForName = this.countBy("name", dataModel.getName());
			if (numForName > 0) {
				throw new BigDataBusinessException("该名称已存在!");
			}

			// 验证名称和code是否存在
			long numForCode = this.countBy("code", dataModel.getCode());
			if (numForCode > 0) {
				throw new BigDataBusinessException("该code已存在!");
			}

			dataModel.setId(UuidUtils.generateUuid());
			dataModel.setOrderType(1);
			dataModel.setCreationTime(new Date());
			dataModelDao.save(dataModel);
			return;
		}

		// 验证名称和code是否存在
		List<DataModel> listForName = this.findListBy("name", dataModel.getName());
		if (listForName.size() > 0) {
			if (!listForName.get(0).getId().equals(dataModel.getId())) {
				throw new BigDataBusinessException("该名称已存在!");
			}
		}

		// 验证名称和code是否存在
		List<DataModel> listForCode = this.findListBy("code", dataModel.getCode());
		if (listForCode.size() > 0) {
			if (!listForCode.get(0).getId().equals(dataModel.getId())) {
				throw new BigDataBusinessException("该code已存在!");
			}
		}
		dataModelDao.update(dataModel, new String[]{"name", "code", "type", "project", "remark", "orderId", "datasetType", "dateDimSwitch", "dateDimTable", "dateColumn", "userDatasetSwitch", "dbId", "source"});
	}

	@Override
	public List<DataModel> getCurrentUserDataModel(String userId, String unitId) {
		return dataModelDao.getCurrentUserDataModel(userId, unitId);
	}

	@Override
	public void deleteDataModel(String id, String code) {
		// 删除模型
		dataModelDao.deleteById(id);
		// 删除指标和维度
		dataModelParamService.deleteByCode(code);
		// 删除订阅信息
		subscribeService.deleteByBusinessId(new String[]{id});
		// 删除关系
		metadataRelationService.deleteByTargetId(id);
	}

	@Override
	public long count(Date start, Date end) {
		if (start == null && end == null) {
			return dataModelDao.count((Specification<DataModel>) (root, criteriaQuery, criteriaBuilder)
					-> criteriaQuery.getRestriction());
		} else {
			return dataModelDao.count((Specification<DataModel>) (root, criteriaQuery, criteriaBuilder)
					-> {
				Predicate time = criteriaBuilder.between(root.get("creationTime").as(Timestamp.class), start, end);
				return criteriaQuery.where(time).getRestriction();
			});
		}
	}

	@Override
	public void deleteDataModelByIds(String[] modelIds) {
		List<DataModel> dataModels = this.findListByIdIn(modelIds);
		dataModels.forEach(e->deleteDataModel(e.getId(), e.getCode()));
	}
}
