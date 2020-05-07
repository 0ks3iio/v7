package net.zdsoft.base.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.base.dao.OpenApiEntityJpaDao;
import net.zdsoft.base.dao.OpenApiEntityTicketDao;
import net.zdsoft.base.entity.eis.Developer;
import net.zdsoft.base.entity.eis.OpenApiEntity;
import net.zdsoft.base.entity.eis.OpenApiEntityTicket;
import net.zdsoft.base.enums.ApplyStatusEnum;
import net.zdsoft.base.enums.YesNoEnum;
import net.zdsoft.base.service.OpenApiEntityService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.StringUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("openApiEntityService")
public class OpenApiEntityServiceImpl extends BaseServiceImpl<OpenApiEntity, String> implements OpenApiEntityService {

    @Autowired
    private OpenApiEntityJpaDao openApiEntityJpaDao;
    @Autowired
    private OpenApiEntityTicketDao openApiEntityTicketDao;

    public Map<String, List<OpenApiEntity>> getEntities(final Developer developer, final String... types) {

        return null;
    }

    @Override
    public Map<String, List<OpenApiEntity>> findEntities(final Developer developer,final String interfaceId, final String... types) {
        Map<String, List<OpenApiEntity>> map = new HashMap<String, List<OpenApiEntity>>();
        List<OpenApiEntityTicket> ticketTypes;
        if(StringUtils.isNotBlank(interfaceId)){
        	ticketTypes = openApiEntityTicketDao.findByTicketKeyAndInterfaceIdOrTypeIn(
                    developer.getTicketKey(), interfaceId, types);
        }else{
        	ticketTypes = openApiEntityTicketDao.findByTicketKeyAndTypeIn(
                    developer.getTicketKey(), types);
        }
        if(CollectionUtils.isEmpty(ticketTypes)){
        	return map;
        }
        Set<String> entitySet = EntityUtils.getSet(ticketTypes, OpenApiEntityTicket::getEntityId);
        List<OpenApiEntity> lists = openApiEntityJpaDao.findByIds(OpenApiEntity.TRUE_IS_USING, entitySet.toArray(new String[entitySet.size()]));
        for (OpenApiEntity ope : lists) {
            String u = ope.getType();
            List<OpenApiEntity> list = map.get(u);
            if (list == null) {
                list = new ArrayList<OpenApiEntity>();
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
    public OpenApiEntity findByTypeAndEntityName(String type, String entityName) {
        return openApiEntityJpaDao.findByTypeAndEntityName(type, entityName);
    }

    @Override
    public OpenApiEntity updateEntity(OpenApiEntity entity) {
        return openApiEntityJpaDao.save(entity);
    }

    @Override
    public int updateEntityMcode(String mcodeId, String id) {
        int ret = openApiEntityJpaDao.updateEntityMcodeId(mcodeId, id);
        return ret;
    }

    @Override
    protected BaseJpaRepositoryDao<OpenApiEntity, String> getJpaDao() {
        return openApiEntityJpaDao;
    }

    @Override
    protected Class<OpenApiEntity> getEntityClass() {
        return OpenApiEntity.class;
    }

    @Override
    public List<OpenApiEntity> findByType(String type) {
        return openApiEntityJpaDao.findByType(type);
    }

    @Override
    public List<OpenApiEntity> getEntityParams(int applyStatus, String type, String resultType, String ticketKey) {
        // 如何接口不是已订阅接口，则默认可以看到所有非敏感字段参数
        if (applyStatus != ApplyStatusEnum.PASS_VERIFY.getValue()) {
            return openApiEntityJpaDao.findByTypeAndIsUsingAndIsSensitiveOrderByDisplayOrder(resultType,
                    YesNoEnum.YES.getValue(), YesNoEnum.NO.getValue());
        }
        // 查询允许获得的参数
        List<OpenApiEntityTicket> entityTickets = openApiEntityTicketDao.findByTicketKeyAndTypeIn(ticketKey,type);
        if(CollectionUtils.isNotEmpty(entityTickets)){
        	Set<String> entityIds = EntityUtils.getSet(entityTickets, OpenApiEntityTicket::getEntityId);
        	return openApiEntityJpaDao.findByIds(YesNoEnum.YES.getValue(), entityIds.toArray(new String[entityIds.size()]));
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public Map<String, Map<String, String>> getSensitiveFile() {
        List<OpenApiEntity> all = openApiEntityJpaDao.findByIsUsingAndIsSensitive(YesNoEnum.YES.getValue(),
                YesNoEnum.YES.getValue());
        Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
        for (OpenApiEntity entity : all) {
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
    public List<OpenApiEntity> getEntitys(String type, int isSensitive) {
        return openApiEntityJpaDao.findByTypeAndIsUsingAndIsSensitiveOrderByDisplayOrder(type,
                YesNoEnum.YES.getValue(), isSensitive);
    }

    @Override
    public List<OpenApiEntity> getEntities(int isSensitive, String[] types) {
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
	public List<OpenApiEntity> findByTypeIn(String[] types) {
		return openApiEntityJpaDao.findByTypeIn(types);
	}

	@Override
	public List<OpenApiEntity> findByIsUsingAndEntityColumnNameIn(int isUsing,
			String... columnName) {
		return openApiEntityJpaDao.findByIsUsingAndEntityColumnNameIn(isUsing,columnName);
	}

	@Override
	public List<OpenApiEntity> findByTypeAll(String type) {
		return openApiEntityJpaDao.findByTypeAll(type);
	}

	@Override
	public List<OpenApiEntity> findByIsUsingAndEntityColumnNameInAndTypeIn(
			int isUsing, String[] columnName, String[] types) {
		return openApiEntityJpaDao.findByIsUsingAndEntityColumnNameInAndTypeIn(isUsing,columnName,types);
	}

	@Override
	public void updateType(String newType, String oldType) {
		openApiEntityJpaDao.updateType(newType,oldType);
	}
}
