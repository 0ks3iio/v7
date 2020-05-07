package net.zdsoft.base.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import javax.annotation.Resource;

import net.zdsoft.base.dao.OpenApiApplyDao;
import net.zdsoft.base.dao.OpenApiEntityJpaDao;
import net.zdsoft.base.dao.OpenApiEntityTicketDao;
import net.zdsoft.base.dto.EntityDto;
import net.zdsoft.base.dto.InterfaceDto;
import net.zdsoft.base.entity.eis.OpenApiApply;
import net.zdsoft.base.entity.eis.OpenApiEntityTicket;
import net.zdsoft.base.enums.ApplyStatusEnum;
import net.zdsoft.base.service.OpenApiApplyService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.framework.utils.Validators;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("openApiApplyService")
public class OpenApiApplyServiceImpl extends BaseServiceImpl<OpenApiApply, String> implements OpenApiApplyService {
    @Autowired
    private OpenApiApplyDao openApiApplyDao;
    @Resource
    private OpenApiEntityTicketDao entityTicketDao;
    @Resource
    private OpenApiEntityJpaDao openApiEntityJpaDao;

    @Override
	protected BaseJpaRepositoryDao<OpenApiApply, String> getJpaDao() {
		return openApiApplyDao;
	}

	@Override
	protected Class<OpenApiApply> getEntityClass() {
		return OpenApiApply.class;
	}
    
    @Override
    public void save(OpenApiApply openApiApply) {
        openApiApplyDao.save(openApiApply);
    }

    @Override
    public List<String> getTypes(int status, String ticketKey) {
        return openApiApplyDao.fingByStatusAndTicketKey(status, ticketKey);
    }

    @Override
    public void saveAll(List<OpenApiApply> openApiApplys) {
        openApiApplyDao.saveAll(openApiApplys);
    }

    @Override
    public List<OpenApiApply> getApplys(String ticketKey) {
        return openApiApplyDao.findByTicketKey(ticketKey);
    }

    @Override
    public void updateBatchStatus(int status, Date modifyTime, String[] ids) {
        openApiApplyDao.batchUpdateStatus(status, modifyTime, ids);
    }

    @Override
    public List<OpenApiApply> getApplys(String[] ticketKeys) {
        return openApiApplyDao.findByTicketKeyIn(ticketKeys);
    }

    @Override
    public void removeInterface(String[] type, String ticketKey, String developerId) {
        if (Validators.isEmpty(type) || Validators.isEmpty(ticketKey) || Validators.isEmpty(developerId)) {
            return;
        }
        if(type != null) {
        	openApiApplyDao.deleteApply(type, developerId);
//        	entityTicketDao.deleteEntityTicket(type, ticketKey);
        }
    }

    @Override
    public void updateApplyInterface(String[] types, String ticketKey, int status) {
    	openApiApplyDao.updateStatus(status, new Date(), ticketKey, types);
    }

    @Override
    public void updatePassApply(String developerId, String ticketKey, Map<String, List<String>> interfaces) {
        String[] types = interfaces.keySet().toArray(new String[0]);
        List<OpenApiEntityTicket> entityTickets = entityTicketDao.findByTicketKeyAndTypeIn(ticketKey, types);
        Set<String> unPassEntityList = EntityUtils.getSet(entityTickets, OpenApiEntityTicket::getId);
        if(CollectionUtils.isNotEmpty(entityTickets)){
        	Map<String, List<OpenApiEntityTicket>> typeMap = EntityUtils.getListMap(entityTickets, OpenApiEntityTicket::getType, Function.identity());
        	List<String> PassEntityList = new ArrayList<>();
        	for (String type : types) {
        		List<String> list = interfaces.get(type);
        		List<OpenApiEntityTicket> verifyEn = typeMap.get(type);
//        		Map<String, String> columnMap = EntityUtils.getMap(verifyEn, OpenapiEntityTicket::getEntityColumnName, OpenapiEntityTicket::getId);
        		if (CollectionUtils.isNotEmpty(list)) {
        			for (String c : list) {
//        				PassEntityList.add(columnMap.get(c));
        			}
        		}
        	}
        	unPassEntityList.removeAll(PassEntityList);
        }
        if(CollectionUtils.isNotEmpty(unPassEntityList)){
        	entityTicketDao.deleteByIdIn(unPassEntityList.toArray(new String[unPassEntityList.size()]));
        }
    }

	@Override
	public List<String> findByTicketKeyAndStatusIn(String ticketKey,
			int[] status) {
		return openApiApplyDao.findByTicketKeyAndStatusIn(ticketKey, status);
	}

	@Override
	public List<OpenApiApply> findByTypeAndStatusIn(String type, int[] status) {
		return openApiApplyDao.findByTypeAndStatusIn(type, status);
	}

	@Override
	public List<OpenApiApply> findByTicketKeyAndTypeIn(String ticketKey,
			String[] types) {
		return openApiApplyDao.findByTicketKeyAndTypeIn(ticketKey,types);
	}

	@Override
	public List<OpenApiApply> findByTicketKeyAndTypeOrInterfaceId(String ticketKey, String type, String interfaceId) {
		return openApiApplyDao.findByTicketKeyAndTypeOrInterfaceId(ticketKey,type,interfaceId);
	}

	@Override
	public List<OpenApiApply> findByTicketKeyAndStatus(String ticketKey,int status) {
		return openApiApplyDao.findByTicketKeyAndStatus(ticketKey,status);
	}

	@Override
	public void updateApplyInterface(String ticketKey,
			List<InterfaceDto> allInterfaceDtos) {
		Map<String, InterfaceDto> typeMap = EntityUtils.getMap(allInterfaceDtos, InterfaceDto::getType);
		Set<String> types = EntityUtils.getSet(allInterfaceDtos, InterfaceDto::getType);
		String[] types1 = types.toArray(new String[types.size()]);
		List<OpenApiApply> updaApiApplies = openApiApplyDao.findByTicketKeyAndTypeIn(ticketKey, types1);
		for (OpenApiApply openApiApply : updaApiApplies) {
			openApiApply.setStatus(ApplyStatusEnum.PASS_VERIFY.getValue());
			openApiApply.setModifyTime(new Date());
			InterfaceDto dto = typeMap.get(openApiApply.getType());
			openApiApply.setMaxNumDay(dto.getMaxNumDay());
			openApiApply.setLimitEveryTime(dto.getLimitEveryTime());
		}
		openApiApplyDao.saveAll(updaApiApplies);
		//先清空之前的，再保存新的
		entityTicketDao.deleteByTypeInAndTicketKey(types1,ticketKey);
		List<OpenApiEntityTicket> saveApiEntityTickets = new ArrayList<>();
		for (InterfaceDto dto : allInterfaceDtos) {
			List<EntityDto> entitys = dto.getEntitys();
			for (EntityDto entityDto : entitys) {
				OpenApiEntityTicket entityTicket = new OpenApiEntityTicket();
				entityTicket.setEntityId(entityDto.getEntityId());
				entityTicket.setTicketKey(ticketKey);
				entityTicket.setId(UuidUtils.generateUuid());
				entityTicket.setInterfaceId(dto.getId());
				entityTicket.setType(dto.getType());
				saveApiEntityTickets.add(entityTicket);
			}
		}
		entityTicketDao.saveAll(saveApiEntityTickets);
	}

	@Override
	public void deleteByTicketAndTypeIn(String ticketKey, String[] type) {
		openApiApplyDao.deleteByTicketAndTypeIn(ticketKey,type);
		entityTicketDao.deleteByTypeInAndTicketKey(type,ticketKey);
	}
}
