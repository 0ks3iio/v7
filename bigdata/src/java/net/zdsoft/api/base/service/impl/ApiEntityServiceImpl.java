package net.zdsoft.api.base.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.api.base.dao.ApiEntityJpaDao;
import net.zdsoft.api.base.dao.ApiEntityTicketDao;
import net.zdsoft.api.base.entity.eis.ApiDeveloper;
import net.zdsoft.api.base.entity.eis.ApiEntity;
import net.zdsoft.api.base.entity.eis.ApiEntityTicket;
import net.zdsoft.api.base.enums.ApplyStatusEnum;
import net.zdsoft.api.base.enums.YesNoEnum;
import net.zdsoft.api.base.service.ApiEntityService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.StringUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("apiEntityService")
public class ApiEntityServiceImpl extends BaseServiceImpl<ApiEntity, String> implements ApiEntityService {

    @Autowired
    private ApiEntityJpaDao openApiEntityJpaDao;
    @Autowired
    private ApiEntityTicketDao openApiEntityTicketDao;

    public Map<String, List<ApiEntity>> getEntities(final ApiDeveloper developer, final String... types) {

        return null;
    }

    @Override
    public Map<String, List<ApiEntity>> findEntities(final ApiDeveloper developer,final String interfaceId, final String... types) {
        Map<String, List<ApiEntity>> map = new HashMap<String, List<ApiEntity>>();
        List<ApiEntityTicket> ticketTypes;
        if(StringUtils.isNotBlank(interfaceId)){
        	ticketTypes = openApiEntityTicketDao.findByTicketKeyAndInterfaceId(developer.getTicketKey(), interfaceId);
        }else{
        	ticketTypes = openApiEntityTicketDao.findByTicketKeyAndTypeIn(
                    developer.getTicketKey(), types);
        }
        if(CollectionUtils.isEmpty(ticketTypes)){
        	return map;
        }
        Set<String> entitySet = EntityUtils.getSet(ticketTypes, ApiEntityTicket::getEntityId);
        List<ApiEntity> lists = openApiEntityJpaDao.findByIds(ApiEntity.TRUE_IS_USING, entitySet.toArray(new String[entitySet.size()]));
        for (ApiEntity ope : lists) {
            String u = ope.getType();
            List<ApiEntity> list = map.get(u);
            if (list == null) {
                list = new ArrayList<ApiEntity>();
                map.put(u, list);
            }
            list.add(ope);
        }
        return map;
    }

    @Override
    public int updateEntityHide(String entityName, String type) {
        return openApiEntityJpaDao.updateEntityHide(entityName, type);
    }

    @Override
    public ApiEntity findByTypeAndEntityName(String type, String entityName) {
        return openApiEntityJpaDao.findByTypeAndEntityName(type, entityName);
    }

    @Override
    public ApiEntity updateEntity(ApiEntity entity) {
        return openApiEntityJpaDao.save(entity);
    }

    @Override
    public int updateEntityMcode(String mcodeId, String id) {
        int ret = openApiEntityJpaDao.updateEntityMcodeId(mcodeId, id);
        return ret;
    }

    @Override
    protected BaseJpaRepositoryDao<ApiEntity, String> getJpaDao() {
        return openApiEntityJpaDao;
    }

    @Override
    protected Class<ApiEntity> getEntityClass() {
        return ApiEntity.class;
    }

    @Override
    public List<ApiEntity> findByType(String type) {
        return openApiEntityJpaDao.findByType(type);
    }

    @Override
    public List<ApiEntity> getEntityParams(int applyStatus, String type, String resultType, String ticketKey) {
        // 如何接口不是已订阅接口，则默认可以看到所有非敏感字段参数
        if (applyStatus != ApplyStatusEnum.PASS_VERIFY.getValue()) {
            return openApiEntityJpaDao.findByTypeAndIsUsingAndIsSensitiveOrderByDisplayOrder(resultType,
                    YesNoEnum.YES.getValue(), YesNoEnum.NO.getValue());
        }
        // 查询允许获得的参数
        List<ApiEntityTicket> entityTickets = openApiEntityTicketDao.findByTicketKeyAndTypeIn(ticketKey,type);
        if(CollectionUtils.isNotEmpty(entityTickets)){
        	Set<String> entityIds = EntityUtils.getSet(entityTickets, ApiEntityTicket::getEntityId);
        	return openApiEntityJpaDao.findByIds(YesNoEnum.YES.getValue(), entityIds.toArray(new String[entityIds.size()]));
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public Map<String, Map<String, String>> getSensitiveFile() {
        List<ApiEntity> all = openApiEntityJpaDao.findByIsUsingAndIsSensitive(YesNoEnum.YES.getValue(),
                YesNoEnum.YES.getValue());
        Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
        for (ApiEntity entity : all) {
            String type = entity.getType();
            Map<String, String> IdAndColunmNames = map.get(type);
            if (MapUtils.isEmpty(IdAndColunmNames)) {
                IdAndColunmNames = new HashMap<String, String>();
            }
            IdAndColunmNames.put(entity.getId(), entity.getEntityColumnName());
            map.put(type, IdAndColunmNames);
        }
        return map;
    }

    @Override
    public List<ApiEntity> getEntitys(String type, int isSensitive) {
        return openApiEntityJpaDao.findByTypeAndIsUsingAndIsSensitiveOrderByDisplayOrder(type,
                YesNoEnum.YES.getValue(), isSensitive);
    }

    @Override
    public List<ApiEntity> getEntities(int isSensitive, String[] types) {
        return openApiEntityJpaDao.findByIsUsingAndIsSensitiveAndTypeIn(YesNoEnum.YES.getValue(),
                YesNoEnum.YES.getValue(), types);
    }

	@Override
	public void updateEntityById(int isUsing, String entityId) {
		openApiEntityJpaDao.updateEntityById(isUsing, entityId);
	}

	@Override
	public void deleteByType(String type) {
		openApiEntityJpaDao.deleteByType(type);
	}

	@Override
	public List<ApiEntity> findByTypeIn(String[] types) {
		return openApiEntityJpaDao.findByTypeIn(types);
	}

	@Override
	public List<ApiEntity> findByIsUsingAndEntityColumnNameIn(int isUsing,
			String... columnName) {
		return openApiEntityJpaDao.findByIsUsingAndEntityColumnNameIn(isUsing,columnName);
	}

	@Override
	public List<ApiEntity> findByTypeAll(String type) {
		return openApiEntityJpaDao.findByTypeAll(type);
	}

	@Override
	public List<ApiEntity> findByIsUsingAndEntityColumnNameInAndTypeIn(
			int isUsing, String[] columnName, String[] types) {
		return openApiEntityJpaDao.findByIsUsingAndEntityColumnNameInAndTypeIn(isUsing,columnName,types);
	}

	@Override
	public void updateType(String newType, String oldType) {
		openApiEntityJpaDao.updateType(newType,oldType);
	}

	@Override
	public List<ApiEntity> findByMetadataIdIn(String... metadataIds) {
		return openApiEntityJpaDao.findByMetadataIdIn(metadataIds);
	}

	@Override
	public boolean findByMetadataIdAndColumnName(String metadataId,String columnName) {
		return (openApiEntityJpaDao.findByMetadataIdAndColumnName(metadataId,columnName) != null);
	}
}
