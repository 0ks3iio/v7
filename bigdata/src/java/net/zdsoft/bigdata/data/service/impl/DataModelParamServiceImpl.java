package net.zdsoft.bigdata.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.dao.DataModelParamDao;
import net.zdsoft.bigdata.data.entity.DataModel;
import net.zdsoft.bigdata.data.entity.DataModelParam;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.data.service.DataModelParamService;
import net.zdsoft.bigdata.data.service.DataModelService;
import net.zdsoft.bigdata.metadata.entity.MetadataRelation;
import net.zdsoft.bigdata.metadata.enums.MetadataRelationTypeEnum;
import net.zdsoft.bigdata.metadata.service.MetadataRelationService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DataModelParamServiceImpl extends BaseServiceImpl<DataModelParam, String> implements DataModelParamService {

	@Resource
	private DataModelParamDao dataModelParamDao;
	@Resource
	private DataModelService dataModelService;
	@Resource
	private MetadataRelationService metadataRelationService;

	@Override
	protected BaseJpaRepositoryDao<DataModelParam, String> getJpaDao() {
		return dataModelParamDao;
	}

	@Override
	protected Class<DataModelParam> getEntityClass() {
		return DataModelParam.class;
	}

	@Override
	public List<DataModelParam> findByCodeAndType(String code, String type) {
		Assert.isTrue(StringUtils.isNotBlank(code), "code不能为空");
		Assert.isTrue(StringUtils.isNotBlank(type), "类型不能为空");

		return dataModelParamDao.findAllByCodeAndTypeOrderByOrderId(code, type);
	}

	@Override
	public List<DataModelParam> findTopByCodeAndType(String code, String type) {
		Assert.isTrue(StringUtils.isNotBlank(code), "code不能为空");
		Assert.isTrue(StringUtils.isNotBlank(type), "类型不能为空");

		return dataModelParamDao.findAllByCodeAndTypeAndParentIdOrderByOrderId(code, type, "00000000000000000000000000000000");
	}

	@Override
	public void deleteByCode(String code) {
		dataModelParamDao.deleteByCode(code);
	}

	@Override
	public void saveDataModelParam(DataModelParam dataModelParam) throws BigDataBusinessException {
		if (dataModelParam.getType().equals("dimension")) {
			dataModelParam.setMeasures(null);
		}

		if (dataModelParam.getOrderId() == null) {
			dataModelParam.setOrderId(0);
		}

		if (dataModelParam.getIsFilter() == null) {
			dataModelParam.setIsFilter(0);
		}

		if (dataModelParam.getOrderField() == null) {
			dataModelParam.setOrderField(null);
		}

		if (StringUtils.isBlank(dataModelParam.getId())) {
			// 验证名称和code是否存在
			List<DataModelParam> listForName = this.findListBy(new String[]{"name", "code", "type"}, new String[]{dataModelParam.getName(), dataModelParam.getCode(), dataModelParam.getType()});
			if (listForName.size() > 0) {
				throw new BigDataBusinessException("该名称已存在!");
			}
			dataModelParam.setId(UuidUtils.generateUuid());
			dataModelParamDao.save(dataModelParam);
			return;
		}

		// 验证名称和code是否存在
		List<DataModelParam> listForName = this.findListBy(new String[]{"name", "code", "type"}, new String[]{dataModelParam.getName(), dataModelParam.getCode(), dataModelParam.getType()});
		if (listForName.size() > 0) {
			if (!listForName.get(0).getId().equals(dataModelParam.getId())) {
				throw new BigDataBusinessException("该名称已存在!");
			}
		}

		List<DataModel> dataModels = dataModelService.findListBy("code", dataModelParam.getCode());
		if (dataModels.size() > 0) {
			DataModel dataModel = dataModels.get(0);
			if (dataModel.getSource() !=null && dataModel.getSource() == 0) {
				MetadataRelation metadataRelation = new MetadataRelation();
				metadataRelation.setTargetId(dataModel.getId());
				metadataRelation.setTargetType(MetadataRelationTypeEnum.MODEL.getCode());
				metadataRelation.setSourceId(dataModelParam.getUseTable());
				metadataRelation.setSourceType(MetadataRelationTypeEnum.TABLE.getCode());
				metadataRelationService.saveMetadataRelation(metadataRelation);
			}
		}

		dataModelParamDao.update(dataModelParam, new String[]{"name", "useTable", "useField", "orderField", "orderJson","dimForeignId", "factForeignId", "measures", "orderId", "isFilter","parentId"});
	}

	@Override
	public void updateDataModelParam(String id, Integer isFilter) {
		DataModelParam modelParam = this.findOne(id);
		modelParam.setIsFilter(isFilter);
		dataModelParamDao.update(modelParam, new String[]{"isFilter"});
	}

	@Override
	public List<DataModelParam> findNonDirectDimensionByIdAndUseTable(String useTable, String id) {
		return dataModelParamDao.findNonDirectDimensionByIdAndUseTable(useTable,id);
	}

	@Override
	public List<DataModelParam> findChildDimensionById(String id) {
		return dataModelParamDao.findByParentId(id);
	}
}
