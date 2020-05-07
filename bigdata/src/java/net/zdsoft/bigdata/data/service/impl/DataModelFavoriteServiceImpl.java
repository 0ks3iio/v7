package net.zdsoft.bigdata.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.ChartBusinessType;
import net.zdsoft.bigdata.data.dao.DataModelFavoriteDao;
import net.zdsoft.bigdata.data.entity.DataModelFavorite;
import net.zdsoft.bigdata.data.entity.DataModelFavoriteParam;
import net.zdsoft.bigdata.data.entity.Tag;
import net.zdsoft.bigdata.data.entity.TagRelation;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.data.service.*;
import net.zdsoft.bigdata.metadata.entity.FolderDetail;
import net.zdsoft.bigdata.metadata.service.FolderDetailService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DataModelFavoriteServiceImpl extends BaseServiceImpl<DataModelFavorite, String> implements DataModelFavoriteService {

	@Resource
	private DataModelFavoriteDao dataModelFavoriteDao;
	@Resource
	private TagService tagService;
	@Resource
	private TagRelationService tagRelationService;
	@Resource
	private SubscribeService subscribeService;
	@Resource
	private FolderDetailService folderDetailService;
	@Resource
	private DataModelFavoriteParamService dataModelFavoriteParamService;

	@Override
	protected BaseJpaRepositoryDao<DataModelFavorite, String> getJpaDao() {
		return dataModelFavoriteDao;
	}

	@Override
	protected Class<DataModelFavorite> getEntityClass() {
		return DataModelFavorite.class;
	}

	@Override
	public List<DataModelFavorite> findAll(String unitId) {
		return dataModelFavoriteDao.findAllByUnitIdOrderByOrderId(unitId);
	}

	@Override
	public List<DataModelFavorite> getUserReport(String unitId, String userId, String name, String []tagIds, Pagination page, boolean isEdit) {
		if (StringUtils.isBlank(name)) {
			name = "";
		}

		name = "%" + name + "%";
		if (isEdit) {
			if (ArrayUtils.isNotEmpty(tagIds)) {
				Page<DataModelFavorite> result = dataModelFavoriteDao.getCurrentUserEditReportCharts(unitId, tagIds, name, page.toPageable());
				page.setMaxRowCount((int) result.getTotalElements());
				return result.getContent();
			}
			if ("%%".equals(name)) {
				Page<DataModelFavorite> result = dataModelFavoriteDao.getCurrentUserEditReportCharts(unitId, page.toPageable());
				page.setMaxRowCount((int) result.getTotalElements());
				return result.getContent();
			}
			Page<DataModelFavorite> result = dataModelFavoriteDao.getCurrentUserEditReportCharts(unitId, name, page.toPageable());
			page.setMaxRowCount((int) result.getTotalElements());
			return result.getContent();
		}

		if (ArrayUtils.isNotEmpty(tagIds)) {
			Page<DataModelFavorite> result = dataModelFavoriteDao.getCurrentUserQueryReportChart(userId, unitId, tagIds, name, page.toPageable());
			page.setMaxRowCount((int) result.getTotalElements());
			return result.getContent();
		}
		Page<DataModelFavorite> result = dataModelFavoriteDao.getCurrentUserQueryReportChart(userId, unitId, name, page.toPageable());
		page.setMaxRowCount((int) result.getTotalElements());
		return result.getContent();
	}

	@Override
	public List<DataModelFavorite> getUserReport(String userId, String name, String[] tagIds) {
		if (StringUtils.isBlank(name)) {
			name = "";
		}

		name = "%" + name + "%";
		if (ArrayUtils.isNotEmpty(tagIds)) {
			return dataModelFavoriteDao.findAllByUserId(userId, tagIds, name);
		}

		return dataModelFavoriteDao.findAllByUserId(userId, name);
	}

	@Override
	public void saveDataModelFavorite(DataModelFavorite dataModelFavorite) throws BigDataBusinessException {

		List<DataModelFavorite> listForName = this.findListBy(
				new String[] { "favoriteName", "unitId" },
				new String[] { dataModelFavorite.getFavoriteName(), dataModelFavorite.getUnitId() });
		if (StringUtils.isBlank(dataModelFavorite.getId())) {
			if (listForName.size() > 0) {
				throw new BigDataBusinessException("该名称已存在!");
			}
			dataModelFavorite.setId(UuidUtils.generateUuid());
			this.save(dataModelFavorite);
		} else {
			dataModelFavoriteDao.update(dataModelFavorite, new String[]{"isShowChart", "chartType"
					, "windowSize", "favoriteName", "orderId", "remark", "queryRow", "queryColumn", "queryIndex", "queryChartSql", "querySql"});
		}

		//更新tag关系
		this.saveTagRelation(dataModelFavorite);
		this.saveFolderDetail(dataModelFavorite);
		this.saveParam(dataModelFavorite);
	}

	private void saveFolderDetail(DataModelFavorite dataModelFavorite) {
		// 删除之前文件夹关系
		folderDetailService.deleteByBusinessId(dataModelFavorite.getId());
		// 保存文件夹关系
		FolderDetail folderDetail = new FolderDetail();
		folderDetail.setBusinessId(dataModelFavorite.getId());
		folderDetail.setBusinessName(dataModelFavorite.getFavoriteName());
		folderDetail.setBusinessType(ChartBusinessType.MODEL_REPORT.getBusinessType().toString());
		folderDetail.setCreationTime(new Date());
		folderDetail.setOperatorId(dataModelFavorite.getUserId());
		folderDetail.setOrderId(dataModelFavorite.getOrderId());
		folderDetail.setFolderId(dataModelFavorite.getFolderId());
		folderDetail.setOrderType(dataModelFavorite.getOrderType());
		folderDetail.setUnitId(dataModelFavorite.getUnitId());
		folderDetail.setId(UuidUtils.generateUuid());
		folderDetailService.save(folderDetail);
	}

	private void saveParam(DataModelFavorite dataModelFavorite) {
		//删除之前保存的
		dataModelFavoriteParamService.deleteByFavoriteId(dataModelFavorite.getId());
		//判断有没有保存条件
		if (dataModelFavorite.getDataModelFavoriteParams().size() > 0) {
			dataModelFavorite.getDataModelFavoriteParams().forEach(e->{
				e.setId(UuidUtils.generateUuid());
				e.setFavoriteId(dataModelFavorite.getId());
				dataModelFavoriteParamService.save(e);
			});
		}
	}

	@Override
	public void deleteDataModelFavorite(String favoriteId) {
		dataModelFavoriteDao.deleteById(favoriteId);
		// 删除文件夹关联信息
		folderDetailService.deleteByBusinessId(favoriteId);
		// 删除订阅
		subscribeService.deleteByBusinessId(new String[]{favoriteId});
		// 删除标签关系
		tagRelationService.deleteByBusinessId(favoriteId);
	}

	@Override
	public void deleteDataModelFavoriteByIds(String[] ids) {
		List<DataModelFavorite> favorites = this.findListByIdIn(ids);
		favorites.forEach(e-> deleteDataModelFavorite(e.getId()));
	}

	@Override
	public void updateOrderType(DataModelFavorite dataModelFavorite) {
		this.save(dataModelFavorite);
		List<FolderDetail> folderDetails = folderDetailService.findListBy("businessId", dataModelFavorite.getId());
		if (folderDetails.size() > 0) {
			FolderDetail folderDetail = folderDetails.get(0);
			folderDetail.setOrderType(dataModelFavorite.getOrderType());
			folderDetailService.save(folderDetail);
		}
	}

	@Override
	public void updateDataModelFavorite(DataModelFavorite dataModelFavorite) {

		// 保存标签关系
		this.saveTagRelation(dataModelFavorite);
		// 保存文件夹关系
		this.saveFolderDetail(dataModelFavorite);
		//判断有没有保存条件
		if (dataModelFavorite.getDataModelFavoriteParams().size() > 0) {
			DataModelFavoriteParam favoriteParam = dataModelFavorite.getDataModelFavoriteParams().get(0);
			List<DataModelFavoriteParam> params = dataModelFavoriteParamService.findListBy(new String[]{"favoriteId", "paramType"}, new String[]{dataModelFavorite.getId(), "condition"});
			if (params.size() > 0) {
				DataModelFavoriteParam condition = params.get(0);
				dataModelFavoriteParamService.update(favoriteParam, condition.getId(), new String[]{"paramValue"});
			} else {
				favoriteParam.setId(UuidUtils.generateUuid());
				favoriteParam.setFavoriteId(dataModelFavorite.getId());
				dataModelFavoriteParamService.save(favoriteParam);
			}
		} else {
			dataModelFavoriteParamService.deleteByFavoriteId(dataModelFavorite.getId());
		}
	}

	private void saveTagRelation(DataModelFavorite dataModelFavorite) {
		//更新tag关系
		tagRelationService.deleteByBusinessId(dataModelFavorite.getId());
		String[] tags = dataModelFavorite.getTags();
		if (tags != null) {
			Map<String, Tag> tagMap = tagService.findListByIdIn(tags).stream()
					.collect(Collectors.toMap(Tag::getId, t -> t));
			TagRelation[] tagRelations = Arrays.stream(tags).map(tagId -> {
				Tag tag = tagMap.get(tagId);
				if (tag != null) {
					TagRelation tagRelation = new TagRelation();
					tagRelation.setTagId(tagId);
					tagRelation.setBusinessId(dataModelFavorite.getId());
					tagRelation.setCreationTime(new Date());
					tagRelation.setId(UuidUtils.generateUuid());
					tagRelation.setTagName(tag.getTagName());
					return tagRelation;
				}
				return null;
			}).filter(Objects::nonNull).toArray(TagRelation[]::new);
			tagRelationService.saveAll(tagRelations);
		}
	}
}
