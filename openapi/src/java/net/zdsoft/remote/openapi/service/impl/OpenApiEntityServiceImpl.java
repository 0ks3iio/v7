package net.zdsoft.remote.openapi.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.remote.openapi.dao.EntityTicketDao;
import net.zdsoft.remote.openapi.dao.OpenApiEntityJpaDao;
import net.zdsoft.remote.openapi.dao.OpenApiEntityTicketJpaDao;
import net.zdsoft.remote.openapi.entity.Developer;
import net.zdsoft.remote.openapi.entity.OpenApiEntity;
import net.zdsoft.remote.openapi.entity.OpenApiEntityTicket;
import net.zdsoft.remote.openapi.enums.ApplyStatusEnum;
import net.zdsoft.remote.openapi.enums.YesNoEnum;
import net.zdsoft.remote.openapi.service.OpenApiEntityService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service("openApiEntityService")
public class OpenApiEntityServiceImpl extends BaseServiceImpl<OpenApiEntity, String> implements OpenApiEntityService {

    @Autowired
    private OpenApiEntityJpaDao openApiEntityJpaDao;
    @Autowired
    private OpenApiEntityTicketJpaDao openApiEntityTicketJapDao;
    @Resource
    private EntityTicketDao entityTicketDao;

    public Map<String, List<OpenApiEntity>> getEntities(final Developer developer, final String... types) {

        return null;
    }

    @Override
    public Map<String, List<OpenApiEntity>> findEntities(final Developer developer, final String... types) {
        Map<String, List<OpenApiEntity>> map = new HashMap<String, List<OpenApiEntity>>();
        // username为空说明是以前的数据
        List<OpenApiEntityTicket> ticketTypes = openApiEntityTicketJapDao.findByTicketKeyAndTypeIn(
                developer.getTicketKey(), types);
        if(CollectionUtils.isEmpty(ticketTypes)){
        	return map;
        }
        List<OpenApiEntity> lists = openApiEntityJpaDao.findAll(new Specification<OpenApiEntity>() {
            @Override
            public Predicate toPredicate(Root<OpenApiEntity> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Predicate in = root.get("type").as(String.class).in(types);
                Predicate using = cb.equal(root.get("isUsing").as(Integer.class), 1);
                return cq.where(in, using).getRestriction();
            }
        });
        // 如果没有单独为ticketKey配置显示列的话，显示默认的
        // if (CollectionUtils.isEmpty(ticketTypes)) {
        // ticketTypes = openApiEntityTicketJapDao.findByTicketKeyAndTypeIn(
        // "WinuponTestTicketKey", types);
        // }

        Set<String> ticketTypeSet = new HashSet<String>();
        for (OpenApiEntityTicket ticketType : ticketTypes) {
            ticketTypeSet.add(ticketType.getType() + "_" + ticketType.getEntityColumnName());
        }
        
        for (OpenApiEntity ope : lists) {
        	if(CollectionUtils.isNotEmpty(ticketTypeSet)){
        		if (!ticketTypeSet.contains(ope.getType() + "_" + ope.getEntityColumnName())) {
        			continue;
        		}
        	}
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
    public List<OpenApiEntity> getEntityParams(int applyStatus, String resultType, String ticketKey) {
    	//如果接口是推送的接口 push_ 前缀的，直接取对应类型中的所有值
//        if(StringUtils.startsWithIgnoreCase(resultType, "push_")){
//        	resultType = resultType.replaceFirst("push_", "");
//        	return openApiEntityJpaDao.findByTypeAndIsUsingOrderByDisplayOrder(resultType,YesNoEnum.YES.getValue());
//        }
        // 如何接口不是已订阅接口，则默认可以看到所有非敏感字段参数
        if (applyStatus != ApplyStatusEnum.PASS_VERIFY.getValue()) {
            return openApiEntityJpaDao.findByTypeAndIsUsingAndIsSensitiveOrderByDisplayOrder(resultType,
                    YesNoEnum.YES.getValue(), YesNoEnum.NO.getValue());
        }
        // 查询允许获得的参数
        List<String> columnNuames = entityTicketDao.findColumnNames(resultType, ticketKey);
        if (!CollectionUtils.isEmpty(columnNuames)) {
            return openApiEntityJpaDao.findByTypeAndIsUsingAndEntityColumnNameInOrderByDisplayOrder(resultType,
                    YesNoEnum.YES.getValue(), columnNuames.toArray(new String[columnNuames.size()]));
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
