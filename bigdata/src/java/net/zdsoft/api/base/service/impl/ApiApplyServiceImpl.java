package net.zdsoft.api.base.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import net.zdsoft.api.base.dao.ApiApplyDao;
import net.zdsoft.api.base.dao.ApiEntityJpaDao;
import net.zdsoft.api.base.dao.ApiEntityTicketDao;
import net.zdsoft.api.base.dto.AuditApply;
import net.zdsoft.api.base.dto.InterfaceDto;
import net.zdsoft.api.base.entity.eis.ApiApply;
import net.zdsoft.api.base.entity.eis.ApiEntityTicket;
import net.zdsoft.api.base.entity.eis.ApiInterface;
import net.zdsoft.api.base.enums.ApplyStatusEnum;
import net.zdsoft.api.base.service.ApiApplyService;
import net.zdsoft.api.base.service.ApiEntityTicketService;
import net.zdsoft.api.base.service.ApiInterfaceService;
import net.zdsoft.api.openapi.remote.openapi.dto.ApiInterfaceApplyPass;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.metadata.entity.MetadataRelation;
import net.zdsoft.bigdata.metadata.enums.MetadataRelationTypeEnum;
import net.zdsoft.bigdata.metadata.service.MetadataRelationService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("apiApplyService")
public class ApiApplyServiceImpl extends BaseServiceImpl<ApiApply, String> implements ApiApplyService {
    @Autowired
    private ApiApplyDao apiApplyDao;
    @Autowired
    private ApiEntityTicketDao entityTicketDao;
    @Resource
	private ApiEntityTicketService apiEntityTicketService;
    @Autowired
    private ApiEntityJpaDao openApiEntityJpaDao;
    @Resource
	private ApiEntityJpaDao apiEntityJpaDao;
    @Autowired
    private ApiInterfaceService apiInterfaceService;
    @Autowired
	private MetadataRelationService metadataRelationService;

    @Override
	protected BaseJpaRepositoryDao<ApiApply, String> getJpaDao() {
		return apiApplyDao;
	}

	@Override
	protected Class<ApiApply> getEntityClass() {
		return ApiApply.class;
	}

	@Transactional(rollbackFor = Throwable.class)
	@Override
	public void deleteByInterfaceId(String interfaceId) {
		apiApplyDao.deleteByInterfaceId(interfaceId);
		String[] fieldIds = apiEntityTicketService.findListBy("interfaceId", interfaceId).stream()
				.map(ApiEntityTicket::getEntityId).toArray(String[]::new);
		entityTicketDao.deleteByInterfaceId(interfaceId);
	}

	@Transactional(rollbackFor = Throwable.class)
	@Override
	public void passApply(ApiInterfaceApplyPass applyPass) {
		//更改 apply 状态
		ApiApply apiApply = findOne(applyPass.getId());
		if (apiApply == null) {
			throw new RuntimeException("接口申请信息不存在");
		}
		apiApply.setMaxNumDay(applyPass.getMaxInvokeNumber());
		apiApply.setLimitEveryTime(applyPass.getMaxCount());
		apiApply.setStatus(ApplyStatusEnum.PASS_VERIFY.getValue());
		apiApply.setModifyTime(new Date());
		apiApply.setDataSetRuleIds(applyPass.getDataSetRuleId());
		save(apiApply);

		//保存绑定关系
		String interfaceId = apiApply.getInterfaceId();
		ApiInterface apiInterface = apiInterfaceService.findOne(interfaceId);
		String sourceType;
		String targetSource;
		if(ApiInterface.BASE_DATE_TYPE == apiInterface.getDataType() || ApiInterface.BUSINESS_DATE_TYPE == apiInterface.getDataType()){
			sourceType = MetadataRelationTypeEnum.API.getCode();
			targetSource =  MetadataRelationTypeEnum.APP.getCode();
		}else{
			sourceType = MetadataRelationTypeEnum.APP.getCode();
			targetSource = MetadataRelationTypeEnum.API.getCode();
		}
		String appIds = apiApply.getAppIds();
		String[] app = appIds.split(",");
		for (int i = 0; i < app.length; i++) {
			String appId = app[i];
			MetadataRelation daMetadataRelation = new MetadataRelation();
			if(MetadataRelationTypeEnum.APP.getCode().equals(sourceType)){
				daMetadataRelation.setSourceId(appId);
				daMetadataRelation.setTargetId(interfaceId);
			}else{
				daMetadataRelation.setSourceId(interfaceId);
				daMetadataRelation.setTargetId(appId);
			}
			daMetadataRelation.setSourceType(sourceType);
			daMetadataRelation.setTargetType(targetSource);
			try {
				metadataRelationService.saveMetadataRelation(daMetadataRelation);
			} catch (BigDataBusinessException e1) {
				e1.printStackTrace();
			}
		}
		//删除 字段
		List<ApiEntityTicket> fields = apiEntityTicketService.findListBy("interfaceId", applyPass.getInterfaceId());
		Set<String> passFieldIds = new HashSet<>(applyPass.getFieldIds());
		List<ApiEntityTicket> remove = fields.stream().filter(e->{
			return !passFieldIds.contains(e.getEntityId());
		}).collect(Collectors.toList());
		entityTicketDao.deleteAll(remove);
	}

	@Override
    public void save(ApiApply openApiApply) {
    	apiApplyDao.save(openApiApply);
    }

    @Override
    public void saveAll(List<ApiApply> openApiApplys) {
    	apiApplyDao.saveAll(openApiApplys);
    }

    @Override
    public void updateBatchStatus(int status, Date modifyTime, String[] ids) {
    	apiApplyDao.batchUpdateStatus(status, modifyTime, ids);
    }

	@Override
	public List<ApiApply> findByTicketKeyAndStatusIn(String ticketKey,
			int[] status) {
		return apiApplyDao.findByTicketKeyAndStatusIn(ticketKey, status);
	}

	@Override
	public List<ApiApply> findByTypeAndStatusIn(String type, int[] status) {
		return apiApplyDao.findByTypeAndStatusIn(type, status);
	}

	@Override
	public List<ApiApply> findByTicketKeyAndTypeIn(String ticketKey,
			String[] types) {
		return apiApplyDao.findByTicketKeyAndTypeIn(ticketKey,types);
	}

	@Override
	public List<ApiApply> findByTicketKeyAndTypeOrInterfaceId(String ticketKey, String type, String interfaceId) {
		return apiApplyDao.findByTicketKeyAndTypeOrInterfaceId(ticketKey,type,interfaceId);
	}

	@Override
	public List<ApiApply> findByTicketKeyAndStatus(String ticketKey,int status) {
		return apiApplyDao.findByTicketKeyAndStatus(ticketKey,status);
	}

	@Override
	public void updateApplyInterface(String ticketKey,
			List<InterfaceDto> allInterfaceDtos) {
//		Map<String, InterfaceDto> typeMap = EntityUtils.getMap(allInterfaceDtos, InterfaceDto::getType);
//		Set<String> types = EntityUtils.getSet(allInterfaceDtos, InterfaceDto::getType);
//		String[] types1 = types.toArray(new String[types.size()]);
//		List<ApiApply> updaApiApplies = apiApplyDao.findByTicketKeyAndTypeIn(ticketKey, types1);
//		for (ApiApply openApiApply : updaApiApplies) {
//			openApiApply.setStatus(ApplyStatusEnum.PASS_VERIFY.getValue());
//			openApiApply.setModifyTime(new Date());
//			InterfaceDto dto = typeMap.get(openApiApply.getType());
//			openApiApply.setMaxNumDay(dto.getMaxNumDay());
//			openApiApply.setLimitEveryTime(dto.getLimitEveryTime());
//		}
//		apiApplyDao.saveAll(updaApiApplies);
//		//先清空之前的，再保存新的
//		entityTicketDao.deleteByTypeInAndTicketKey(types1,ticketKey);
//		List<ApiEntityTicket> saveApiEntityTickets = new ArrayList<>();
//		for (InterfaceDto dto : allInterfaceDtos) {
//			List<EntityDto> entitys = dto.getEntitys();
//			for (EntityDto entityDto : entitys) {
//				ApiEntityTicket entityTicket = new ApiEntityTicket();
//				entityTicket.setEntityId(entityDto.getEntityId());
//				entityTicket.setTicketKey(ticketKey);
//				entityTicket.setId(UuidUtils.generateUuid());
//				entityTicket.setInterfaceId(dto.getId());
//				entityTicket.setType(dto.getType());
//				saveApiEntityTickets.add(entityTicket);
//			}
//		}
//		entityTicketDao.saveAll(saveApiEntityTickets);
	}

	@Override
	public void deleteByTicketAndTypeIn(String ticketKey, String[] type) {
		apiApplyDao.deleteByTicketAndTypeIn(ticketKey,type);
		entityTicketDao.deleteByTypeInAndTicketKey(type,ticketKey);
	}

	@Transactional(rollbackFor = Throwable.class)
	@Override
	public void deleteApply(String applyId) {
    	ApiApply apiApply = findOne(applyId);
    	if (apiApply == null) {
    		return;
		}
		entityTicketDao.deleteByInterfaceIdAndTicketKey(apiApply.getInterfaceId(), apiApply.getTicketKey());
		delete(applyId);
	}

	@Override
	public List<AuditApply> getAuditApplies(String[] ticketKeys) {
    	if (ArrayUtils.isEmpty(ticketKeys)) {
    		return Collections.emptyList();
		}
		return apiApplyDao.getAuditApplies(ticketKeys);
	}

	@Override
	public void saveAllAndEntityTicket(List<ApiApply> saveApiApplies,
			List<ApiEntityTicket> saveEntityTickets) {
		apiApplyDao.saveAll(saveApiApplies);
		entityTicketDao.saveAll(saveEntityTickets);
	}

	@Override
	public List<ApiApply> findByTicketKeyAndStatusInAndInterfaceIdIn(
			String ticketKey, int[] status, String[] interfaceIds) {
		return apiApplyDao.findByTicketKeyAndStatusInAndInterfaceIdIn(ticketKey, status, interfaceIds);
	}

	@Override
	public List<ApiApply> findByTicketKeyAndStatusAndType(String ticketKey,
			int status, String type) {
		return apiApplyDao.findByTicketKeyAndStatusAndType(ticketKey,status,type);
	}

	@Override
	public List<ApiApply> findByInterfaceIdAndStatusIn(String interfaceId,
			int[] status) {
		return apiApplyDao.findByInterfaceIdAndStatusIn(interfaceId,status);
	}

	@Override
	public ApiApply findByTicketKeyAndInterfaceId(String ticketKey,String interfaceId) {
		return apiApplyDao.findByTicketKeyAndInterfaceId(ticketKey,interfaceId);
	}
}
