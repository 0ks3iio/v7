package net.zdsoft.remote.openapi.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import javax.annotation.Resource;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.framework.utils.Validators;
import net.zdsoft.remote.openapi.dao.EntityTicketDao;
import net.zdsoft.remote.openapi.dao.OpenApiApplyDao;
import net.zdsoft.remote.openapi.dao.OpenApiEntityJpaDao;
import net.zdsoft.remote.openapi.entity.EntityTicket;
import net.zdsoft.remote.openapi.entity.OpenApiApply;
import net.zdsoft.remote.openapi.entity.OpenApiEntity;
import net.zdsoft.remote.openapi.enums.ApplyStatusEnum;
import net.zdsoft.remote.openapi.enums.YesNoEnum;
import net.zdsoft.remote.openapi.service.OpenApiApplyService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("openApiApplyService")
public class OpenApiApplyServiceImpl extends BaseServiceImpl<OpenApiApply, String> implements OpenApiApplyService {
    @Autowired
    private OpenApiApplyDao openApiApplyDao;
    @Resource
    private EntityTicketDao entityTicketDao;
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
    public List<String> getTypes(int status, String developerId) {
        return openApiApplyDao.findInterfaceTypes(status, developerId);
    }

    @Override
    public void saveAll(List<OpenApiApply> openApiApplys) {
        openApiApplyDao.saveAll(openApiApplys);
    }

    @Override
    public List<OpenApiApply> getApplys(String developerId) {
        return openApiApplyDao.findByDeveloperId(developerId);
    }

    @Override
    public void updateBatchStatus(int status, Date modifyTime, String[] ids) {
        openApiApplyDao.batchUpdateStatus(status, modifyTime, ids);
    }

//    @Override
//    public Map<String, List<String>> getTypes(int status, String[] developerIds) {
//        List<OpenApiApply> applys = openApiApplyDao.findByStatusAndDeveloperIdIn(status, developerIds);
//        Map<String, List<String>> map = new HashMap<>();
//        for (OpenApiApply ay : applys) {
//            String developerId = ay.getDeveloperId();
//            List<String> list = map.get(developerId);
//            if (CollectionUtils.isEmpty(list)) {
//                list = new ArrayList<String>();
//            }
//            list.add(InterfaceTypeEnum.getNameValue(ay.getType()));
//            map.put(developerId, list);
//        }
//        return map;
//    }

    @Override
    public List<OpenApiApply> getApplys(String[] developerIds) {
        return openApiApplyDao.findByDeveloperIdIn(developerIds);
    }

    @Override
    public void removeInterface(String[] type, String ticketKey, String developerId) {
        if (Validators.isEmpty(type) || Validators.isEmpty(ticketKey) || Validators.isEmpty(developerId)) {
            return;
        }
        if(type != null) {
        	openApiApplyDao.deleteApply(type, developerId);
        	entityTicketDao.deleteEntityTicket(type, ticketKey);
        }
    }

    @Override
    public void updateApplyInterface(String[] types, String developerId, String ticketKey, int status) {
        if (ApplyStatusEnum.PASS_VERIFY.getValue() == status) {
            List<EntityTicket> ticketKeys = getTicketKeys(types, ticketKey);
            if (CollectionUtils.isNotEmpty(ticketKeys)) {
                entityTicketDao.saveAll(ticketKeys);
            }
        }
        openApiApplyDao.updateStatus(status, new Date(), developerId, types);
    }

    @Override
    public void updatePassApply(String developerId, String ticketKey, Map<String, List<String>> interfaces) {
        String[] types = interfaces.keySet().toArray(new String[0]);
        List<EntityTicket> entityTickets = entityTicketDao.findByTicketKeyAndTypeIn(ticketKey, types);
        Set<String> unPassEntityList = EntityUtils.getSet(entityTickets, EntityTicket::getId);
        if(CollectionUtils.isNotEmpty(entityTickets)){
        	Map<String, List<EntityTicket>> typeMap = EntityUtils.getListMap(entityTickets, EntityTicket::getType, Function.identity());
        	List<String> PassEntityList = new ArrayList<>();
        	for (String type : types) {
        		List<String> list = interfaces.get(type);
        		List<EntityTicket> verifyEn = typeMap.get(type);
        		Map<String, String> columnMap = EntityUtils.getMap(verifyEn, EntityTicket::getEntityColumnName, EntityTicket::getId);
        		if (CollectionUtils.isNotEmpty(list)) {
        			for (String c : list) {
        				PassEntityList.add(columnMap.get(c));
        			}
        		}
        	}
        	unPassEntityList.removeAll(PassEntityList);
        }
        if(CollectionUtils.isNotEmpty(unPassEntityList)){
        	entityTicketDao.deleteByIdIn(unPassEntityList.toArray(new String[unPassEntityList.size()]));
        }
    }

    /**
     * @author chicb
     * @param types
     * @param ticketKey
     */
    private List<EntityTicket> getTicketKeys(String[] types, String ticketKey) {
        List<OpenApiEntity> entitys = openApiEntityJpaDao.findByIsUsingAndIsSensitiveAndTypeIn(
                YesNoEnum.YES.getValue(), YesNoEnum.NO.getValue(), types);
        List<EntityTicket> tickets = new ArrayList<EntityTicket>();
        if (CollectionUtils.isNotEmpty(entitys)) {
            for (OpenApiEntity e : entitys) {
                EntityTicket et = new EntityTicket();
                et.setDisplayOrder(e.getDisplayOrder());
                et.setEntityColumnName(e.getEntityColumnName());
                et.setIsSensitive(e.getIsSensitive());
                et.setTicketKey(ticketKey);
                et.setType(e.getType());
                et.setId(UuidUtils.generateUuid());
                tickets.add(et);
            }
        }
        return tickets;
    }

	@Override
	public List<String> findByDeveloperIdAndStatusIn(String developerId,
			int[] status) {
		return openApiApplyDao.findByDeveloperIdAndStatusIn(developerId, status);
	}

	@Override
	public List<OpenApiApply> findByTypeAndStatusIn(String type, int[] status) {
		return openApiApplyDao.findByTypeAndStatusIn(type, status);
	}

	@Override
	public List<OpenApiApply> findByDeveloperIdAndTypeIn(String developerId,
			String[] types) {
		return openApiApplyDao.findByDeveloperIdAndTypeIn(developerId,types);
	}
}
