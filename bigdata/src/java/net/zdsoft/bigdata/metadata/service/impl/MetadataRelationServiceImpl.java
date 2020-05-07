package net.zdsoft.bigdata.metadata.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.zdsoft.api.base.entity.eis.ApiInterface;
import net.zdsoft.api.base.entity.eis.OpenApiApp;
import net.zdsoft.api.base.service.ApiInterfaceService;
import net.zdsoft.api.base.service.OpenApiAppService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.entity.DataModel;
import net.zdsoft.bigdata.taskScheduler.entity.EtlJob;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.data.service.DataModelService;
import net.zdsoft.bigdata.taskScheduler.service.EtlJobService;
import net.zdsoft.bigdata.datax.entity.DataxJob;
import net.zdsoft.bigdata.datax.service.DataxJobService;
import net.zdsoft.bigdata.extend.data.entity.Event;
import net.zdsoft.bigdata.extend.data.service.EventService;
import net.zdsoft.bigdata.metadata.dao.MetadataRelationDao;
import net.zdsoft.bigdata.metadata.entity.Metadata;
import net.zdsoft.bigdata.metadata.entity.MetadataRelation;
import net.zdsoft.bigdata.metadata.entity.MetadataRelationEx;
import net.zdsoft.bigdata.metadata.enums.MetadataRelationTypeEnum;
import net.zdsoft.bigdata.metadata.service.MetadataRelationService;
import net.zdsoft.bigdata.metadata.service.MetadataService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by wangdongdong on 2019/1/7 14:16.
 */
@Service
public class MetadataRelationServiceImpl extends BaseServiceImpl<MetadataRelation, String> implements MetadataRelationService {


    @Resource
    private MetadataRelationDao metadataRelationDao;
    @Resource
    private MetadataService metadataService;
    @Resource
    private EtlJobService etlJobService;
    @Resource
    private DataModelService dataModelService;
    @Resource
    private EventService eventService;
    @Resource
    private OpenApiAppService openApiAppService;
    @Resource
    private ApiInterfaceService apiInterfaceService;
    @Resource
    private DataxJobService dataxJobService;

    @Override
    protected BaseJpaRepositoryDao<MetadataRelation, String> getJpaDao() {
        return metadataRelationDao;
    }

    @Override
    protected Class<MetadataRelation> getEntityClass() {
        return MetadataRelation.class;
    }

    @Override
    public List<MetadataRelation> findBySourceId(String sourceId) {
        List<MetadataRelation> metadataRelations = metadataRelationDao.findBySourceId(sourceId);
        padMetadataRelationName(metadataRelations);
        return metadataRelations;
    }

    @Override
    public List<MetadataRelation> findByTargetId(String targetId) {
        List<MetadataRelation> metadataRelations = metadataRelationDao.findByTargetId(targetId);
        padMetadataRelationName(metadataRelations);
        return metadataRelations;
    }

    private void padMetadataRelationName(List<MetadataRelation> metadataRelations) {
        metadataRelations.forEach(e -> {
            e.setTargetName(getName(e.getTargetType(), e.getTargetId()));
            e.setSourceName(getName(e.getSourceType(), e.getSourceId()));
        });
    }

    @Override
    public List<MetadataRelation> findBySourceIdAndSourceType(String sourceId, String sourceType) {
        return metadataRelationDao.findBySourceIdAndSourceType(sourceId, sourceType);
    }

    @Override
    public List<MetadataRelation> findByTargetIdAndTargetType(String targetId, String targetType) {
        return metadataRelationDao.findByTargetIdAndTargetType(targetId, targetType);
    }

    @Override
    public void saveMetadataRelation(MetadataRelation metadataRelation) throws BigDataBusinessException {
        List<MetadataRelation> listForName = this.findListBy(new String[]{"sourceId", "targetId"}, new String[]{metadataRelation.getSourceId(), metadataRelation.getTargetId()});
        if (StringUtils.isBlank(metadataRelation.getId())) {
            if (listForName.size() > 0) {
                return;
            }
            metadataRelation.setId(UuidUtils.generateUuid());
            metadataRelationDao.save(metadataRelation);
            return;
        }

        // 验证名称和code是否存在
        if (listForName.size() > 0) {
            if (!listForName.get(0).getId().equals(metadataRelation.getId())) {
                return;
            }
        }
        metadataRelationDao.update(metadataRelation, new String[]{"targetId", "targetType"});
    }

    @Override
    public MetadataRelationEx getMetadataRelationGather(String metadataId, String mdType) {
        return getMetadataRelationEx(metadataId, mdType, 0);
    }

    @Override
    public void deleteByTargetId(String targetId) {
        metadataRelationDao.deleteByTargetId(targetId);
    }

    @Override
    public void deleteBySourceId(String sourceId) {
        metadataRelationDao.deleteBySourceId(sourceId);
    }

    @Override
    public void deleteBySourceIdAndTargetId(String sourceId, String targetId) {
        metadataRelationDao.deleteBySourceIdAndTargetId(sourceId, targetId);
    }

    @Override
    public List<MetadataRelation> getMetadataRelation(String dwRankId, String propertyTopicId) {
        List<Metadata> metadata = null;
        if (StringUtils.isNotBlank(dwRankId)) {
            metadata = metadataService.findByDwRankAndPropertyTopic(dwRankId, propertyTopicId);
        } else {
            metadata = metadataService.findByPropertyTopicId(propertyTopicId);
        }
        Set<String> ids = EntityUtils.getSet(metadata, Metadata::getId);
        List<MetadataRelation> returnList = Lists.newArrayList();
        getMetadataRelation(returnList,ids,new HashSet<String>());
        Set<String> RelationIds = Sets.newHashSet();
        Iterator it = returnList.iterator();
        while(it.hasNext()) {
            MetadataRelation relation = (MetadataRelation)it.next();
            if (RelationIds.contains((relation.getId()))) {
                it.remove();
            } else {
                RelationIds.add(relation.getId());
            }
        }
        if (CollectionUtils.isEmpty(returnList) && CollectionUtils.isNotEmpty(metadata)) {
            MetadataRelation relation = null;
            for (Metadata data : metadata) {
                relation = new MetadataRelation();
                relation.setSourceId(data.getId());
                relation.setSourceType(data.getMdType());
                relation.setSourceName(data.getName());
                returnList.add(relation);
            }
        }
    	return returnList;
    }

    @Override
	public List<MetadataRelation> getMetadataRelation(String metaId) {
    	Set<String> ids = Sets.newHashSet();
    	ids.add(metaId);
    	List<MetadataRelation> returnList = Lists.newArrayList();
    	getMetadataRelation(returnList,ids,new HashSet<String>());
    	return returnList;
	}
    
    @Override
  	public List<MetadataRelation> getSourceMetadataRelation(String metaId) {
      	Set<String> ids = Sets.newHashSet();
      	ids.add(metaId);
      	List<MetadataRelation> returnList = Lists.newArrayList();
      	getSourceMetadataRelation(returnList,ids,new HashSet<String>());
      	return returnList;
  	}
    
    @Override
    public List<MetadataRelation> getTargetMetadataRelation(String metaId) {
    	Set<String> ids = Sets.newHashSet();
    	ids.add(metaId);
    	List<MetadataRelation> returnList = Lists.newArrayList();
    	getTargetMetadataRelation(returnList,ids,new HashSet<String>());
    	return returnList;
    }
    
    @Override
    public List<MetadataRelation> getSourceMetadataRelation(String dwRankId, String propertyTopicId) {
    	List<Metadata> metadata = null;
    	if (StringUtils.isNotBlank(dwRankId)) {
    		metadata = metadataService.findByDwRankAndPropertyTopic(dwRankId, propertyTopicId);
    	} else {
    		metadata = metadataService.findByPropertyTopicId(propertyTopicId);
    	}
    	Set<String> ids = EntityUtils.getSet(metadata, Metadata::getId);
    	List<MetadataRelation> returnList = Lists.newArrayList();
    	getSourceMetadataRelation(returnList,ids,new HashSet<String>());
    	return returnList;
    }
    
    @Override
    public List<MetadataRelation> getTargetMetadataRelation(String dwRankId, String propertyTopicId) {
    	List<Metadata> metadata = null;
    	if (StringUtils.isNotBlank(dwRankId)) {
    		metadata = metadataService.findByDwRankAndPropertyTopic(dwRankId, propertyTopicId);
    	} else {
    		metadata = metadataService.findByPropertyTopicId(propertyTopicId);
    	}
    	Set<String> ids = EntityUtils.getSet(metadata, Metadata::getId);
    	List<MetadataRelation> returnList = Lists.newArrayList();
    	getTargetMetadataRelation(returnList,ids,new HashSet<String>());
    	return returnList;
    }

    private void getSourceMetadataRelation(List<MetadataRelation> returnList,Set<String> ids,Set<String> allIds){
    	allIds.addAll(ids);
    	List<MetadataRelation> metadataRelations = metadataRelationDao.findAllBySourceIdIn(new ArrayList<String>(ids));
    	Set<String> reIds = EntityUtils.getSet(metadataRelations, MetadataRelation::getTargetId);
    	reIds.removeAll(allIds);
    	returnList.addAll(metadataRelations);
    	if(CollectionUtils.isNotEmpty(reIds)){
    		getMetadataRelation(returnList,reIds,allIds);
    	}
    }
    private void getTargetMetadataRelation(List<MetadataRelation> returnList,Set<String> ids,Set<String> allIds){
    	allIds.addAll(ids);
    	List<MetadataRelation> metadataRelations = metadataRelationDao.findAllByTargetIdIn(new ArrayList<String>(ids));
    	Set<String> reIds = EntityUtils.getSet(metadataRelations, MetadataRelation::getSourceId);
    	reIds.removeAll(allIds);
    	returnList.addAll(metadataRelations);
    	if(CollectionUtils.isNotEmpty(reIds)){
    		getMetadataRelation(returnList,reIds,allIds);
    	}
    }
    private void getMetadataRelation(List<MetadataRelation> returnList,Set<String> ids,Set<String> allIds){
    	allIds.addAll(ids);
    	List<MetadataRelation> metadataRelations = metadataRelationDao.findAllBySourceIdIn(new ArrayList<String>(ids));
    	metadataRelations.addAll(metadataRelationDao.findAllByTargetIdIn(new ArrayList<String>(ids)));
    	Set<String> reIds = EntityUtils.getSet(metadataRelations, MetadataRelation::getSourceId);
    	reIds.addAll(EntityUtils.getSet(metadataRelations, MetadataRelation::getTargetId));
    	reIds.removeAll(allIds);
    	returnList.addAll(metadataRelations);
    	if(CollectionUtils.isNotEmpty(reIds)){
    		getMetadataRelation(returnList,reIds,allIds);
    	}else{
    		returnList.forEach(e->{
    			e.setSourceName(getName(e.getSourceType(), e.getSourceId()));
    			e.setTargetName(getName(e.getTargetType(), e.getTargetId()));
    		});
    	}
    }

    @Override
    public List<MetadataRelation> getMetadataRelationByMetadataId(String metadataId) {
        List<MetadataRelation> metadataRelations = Lists.newArrayList();
        metadataRelations.addAll(metadataRelationDao.findBySourceId(metadataId));
        metadataRelations.addAll(metadataRelationDao.findByTargetId(metadataId));
        this.padMetadataRelation(metadataRelations);
        return metadataRelations;
    }

    @Override
    public List<OpenApiApp> getAllAppBydwRankIdAndPropertyTopicId(String dwRankId, String propertyTopicId) {
        return getOpenApiApps(getIdsByDwRankIdAndPropertyTopicId(dwRankId, propertyTopicId, MetadataRelationTypeEnum.APP.getCode()));
    }

    @Override
    public List<MetadataRelation> getJobMetadataRelation() {
        List<MetadataRelation> metadataRelations = Lists.newArrayList();
        metadataRelations.addAll(metadataRelationDao.findAllBySourceType(MetadataRelationTypeEnum.DATAX_JOB.getCode()));
        metadataRelations.addAll(metadataRelationDao.findAllBySourceType(MetadataRelationTypeEnum.ETL_JOB.getCode()));
        metadataRelations.addAll(metadataRelationDao.findAllByTargetType(MetadataRelationTypeEnum.DATAX_JOB.getCode()));
        metadataRelations.addAll(metadataRelationDao.findAllByTargetType(MetadataRelationTypeEnum.ETL_JOB.getCode()));

        return metadataRelations;
    }

    @Override
    public List<OpenApiApp> getAllApp() {
        return getOpenApiApps(getAllIds(MetadataRelationTypeEnum.APP.getCode()));
    }

    @Override
    public Integer countAppByMetadataId(String metadataId) {
        return getAllMetdataIdsByType(metadataId, MetadataRelationTypeEnum.APP.getCode()).size();
    }

    @Override
    public Integer countAppBydwRankIdAndPropertyTopicId(String dwRankId, String propertyTopicId) {
        return getIdsByDwRankIdAndPropertyTopicId(dwRankId, propertyTopicId, MetadataRelationTypeEnum.APP.getCode()).size();
    }

    @Override
    public Integer countAllApp() {
        return getAllIds(MetadataRelationTypeEnum.APP.getCode()).size();
    }

    @Override
    public List<ApiInterface> getAllApiByMetadataId(String metadataId) {
        Set<String> apiIds = getAllMetdataIdsByType(metadataId, MetadataRelationTypeEnum.API.getCode());
        return getApiInterfaces(apiIds);
    }

    @Override
    public List<ApiInterface> getAllApiBydwRankIdAndPropertyTopicId(String dwRankId, String propertyTopicId) {
        return getApiInterfaces(getIdsByDwRankIdAndPropertyTopicId(dwRankId, propertyTopicId, MetadataRelationTypeEnum.API.getCode()));
    }

    @Override
    public List<ApiInterface> getAllApi() {
        return getApiInterfaces(getAllIds(MetadataRelationTypeEnum.API.getCode()));
    }

    @Override
    public Integer countApiByMetadataId(String metadataId) {
        return getAllMetdataIdsByType(metadataId, MetadataRelationTypeEnum.API.getCode()).size();
    }

    @Override
    public Integer countApiBydwRankIdAndPropertyTopicId(String dwRankId, String propertyTopicId) {
        return getIdsByDwRankIdAndPropertyTopicId(dwRankId, propertyTopicId, MetadataRelationTypeEnum.API.getCode()).size();
    }

    @Override
    public Integer countApiByPropertyTopicId(String propertyTopicId) {
        return getIdsByPropertyTopicId(propertyTopicId, MetadataRelationTypeEnum.API.getCode()).size();
    }

    @Override
    public Integer countAllApi() {
        return getAllIds(MetadataRelationTypeEnum.API.getCode()).size();
    }

    @Override
    public List<OpenApiApp> getSourceAppByMetadataId(String metadataId) {
        Set<String> appIds = getSourceIdsBySourceType(metadataId, MetadataRelationTypeEnum.APP.getCode());
        return getOpenApiApps(appIds);
    }

    @Override
    public Integer countSourceAppByMetadataId(String metadataId) {
    	  List<MetadataRelation> mr = getTargetMetadataRelation(metadataId);
          Set<String> tIds = EntityUtils.getSet(mr, MetadataRelation::getTargetId);
          tIds.add(metadataId);
          Set<String> ids = Sets.newHashSet();
          tIds.forEach(e -> ids.addAll(getSourceIdsBySourceType(e, MetadataRelationTypeEnum.APP.getCode())));
        return ids.size();
    }

    @Override
    public List<OpenApiApp> getSourceAppBydwRankIdAndPropertyTopicId(String dwRankId, String propertyTopicId) {
        return getOpenApiApps(getSourceIdsByDwRankIdAndPropertyTopicId(dwRankId, propertyTopicId, MetadataRelationTypeEnum.APP.getCode()));
    }

    @Override
    public Integer countSourceAppBydwRankIdAndPropertyTopicId(String dwRankId, String propertyTopicId) {
        return getSourceIdsByDwRankIdAndPropertyTopicId(dwRankId, propertyTopicId, MetadataRelationTypeEnum.APP.getCode()).size();
    }

    @Override
    public List<OpenApiApp> getSourceAppByPropertyTopicId(String propertyTopicId) {
        return getOpenApiApps(getSourceIdsByPropertyTopicId(propertyTopicId, MetadataRelationTypeEnum.APP.getCode()));
    }

    @Override
    public Integer countSourceAppByPropertyTopicId(String propertyTopicId) {
        return getSourceIdsByPropertyTopicId(propertyTopicId, MetadataRelationTypeEnum.APP.getCode()).size();
    }

    @Override
    public List<OpenApiApp> getAllSourceApp() {
        return getOpenApiApps(getAllSourceIds(MetadataRelationTypeEnum.APP.getCode()));
    }

    @Override
    public Integer countAllSourceApp() {
        return getAllSourceIds(MetadataRelationTypeEnum.APP.getCode()).size();
    }

    /**
     * 根据某个元数据的源类型获取所有id
     * @param metadataId
     * @param sourceType
     * @return
     */
    private Set<String> getSourceIdsBySourceType(String metadataId, String sourceType) {
        List<MetadataRelation> metadataRelations = metadataRelationDao.findByTargetIdAndSourceType(metadataId, sourceType);
        return metadataRelations.stream().map(MetadataRelation::getSourceId).collect(Collectors.toSet());
    }

    /**
     * 根据某个元数据的目标类型获取所有id
     * @param metadataId
     * @param sourceType
     * @return
     */
    private Set<String> getTargetIdsBySourceType(String metadataId, String sourceType) {
        List<MetadataRelation> metadataRelations = metadataRelationDao.findBySourceIdAndTargetType(metadataId, sourceType);
        return metadataRelations.stream().map(MetadataRelation::getTargetId).collect(Collectors.toSet());
    }

    private Set<String> getIdsByDwRankIdAndPropertyTopicId(String dwRankId, String propertyTopicId, String type) {
        List<Metadata> metadatas = metadataService.findByDwRankAndPropertyTopic(dwRankId, propertyTopicId);
        Set<String> ids = Sets.newHashSet();
        metadatas.forEach(e -> ids.addAll(getAllMetdataIdsByType(e.getId(), type)));
        return ids;
    }

    private Set<String> getIdsByPropertyTopicId(String propertyTopicId, String type) {
        List<Metadata> metadatas = metadataService.findByPropertyTopicId(propertyTopicId);
        Set<String> ids = Sets.newHashSet();
        metadatas.forEach(e -> ids.addAll(getAllMetdataIdsByType(e.getId(), type)));
        return ids;
    }

    private Set<String> getSourceIdsByDwRankIdAndPropertyTopicId(String dwRankId, String propertyTopicId, String type) {
        List<Metadata> metadatas = metadataService.findByDwRankAndPropertyTopic(dwRankId, propertyTopicId);
        List<MetadataRelation> mr = getTargetMetadataRelation(dwRankId, propertyTopicId);
        Set<String> targetIds = EntityUtils.getSet(metadatas, Metadata::getId);
        targetIds.addAll(EntityUtils.getSet(mr, MetadataRelation::getTargetId));
        Set<String> ids = Sets.newHashSet();
        targetIds.forEach(e -> ids.addAll(getSourceIdsBySourceType(e, type)));
        return ids;
    }

    private Set<String> getTargetIdsByDwRankIdAndPropertyTopicId(String dwRankId, String propertyTopicId, String type) {
        List<Metadata> metadatas = metadataService.findByDwRankAndPropertyTopic(dwRankId, propertyTopicId);
        List<MetadataRelation> mr = getSourceMetadataRelation(dwRankId, propertyTopicId);
        Set<String> sourceIds = EntityUtils.getSet(metadatas, Metadata::getId);
        sourceIds.addAll(EntityUtils.getSet(mr, MetadataRelation::getSourceId));
        Set<String> ids = Sets.newHashSet();
        sourceIds.forEach(e -> ids.addAll(getTargetIdsBySourceType(e, type)));
        return ids;
    }

    private Set<String> getSourceIdsByPropertyTopicId(String propertyTopicId, String type) {
        List<Metadata> metadatas = metadataService.findByPropertyTopicId(propertyTopicId);
        Set<String> ids = Sets.newHashSet();
        metadatas.forEach(e -> ids.addAll(getSourceIdsBySourceType(e.getId(), type)));
        return ids;
    }

    private Set<String> getTargetIdsByPropertyTopicId(String propertyTopicId, String type) {
        List<Metadata> metadatas = metadataService.findByPropertyTopicId(propertyTopicId);
        Set<String> ids = Sets.newHashSet();
        metadatas.forEach(e -> ids.addAll(getTargetIdsBySourceType(e.getId(), type)));
        return ids;
    }


    /**
     * 根据获取该元数据的来源和去向id
     * @param metadataId
     * @param type
     * @return
     */
    private Set<String> getAllMetdataIdsByType(String metadataId, String type) {
        Set<String> appIds = Sets.newHashSet();
        List<MetadataRelation> sourceRelations = metadataRelationDao.findByTargetIdAndSourceType(metadataId, type);
        List<MetadataRelation> targetRelations = metadataRelationDao.findBySourceIdAndTargetType(metadataId, type);
        appIds.addAll(EntityUtils.getSet(sourceRelations, MetadataRelation::getSourceId));
        appIds.addAll(EntityUtils.getSet(targetRelations, MetadataRelation::getTargetId));
        return appIds;
    }

    /**
     * 根据类型 获取所有id
     * @param type
     * @return
     */
    private Set<String> getAllIds(String type) {
        Set<String> appIds = Sets.newHashSet();
        appIds.addAll(getAllSourceIds(type));
        appIds.addAll(getAllTargetIds(type));
        return appIds;
    }

    /**
     * 根据sourceId 类型 获取所有id
     * @return
     */
    private Set<String> getAllSourceIds(String type) {
        List<MetadataRelation> sourceRelations = metadataRelationDao.findAllBySourceType(type);
        return EntityUtils.getSet(sourceRelations, MetadataRelation::getSourceId);
    }

    /**
     * 根据类型 获取所有id
     * @param type
     * @return
     */
    private Set<String> getAllTargetIds(String type) {
        List<MetadataRelation> targetRelations = metadataRelationDao.findAllByTargetType(type);
        return EntityUtils.getSet(targetRelations, MetadataRelation::getTargetId);
    }

    @Override
    public List<OpenApiApp> getTargetAppByMetadataId(String metadataId) {
        Set<String> appIds = getTargetIdsBySourceType(metadataId, MetadataRelationTypeEnum.APP.getCode());
        return getOpenApiApps(appIds);
    }

    @Override
    public Integer countTargetAppByMetadataId(String metadataId) {
    	List<MetadataRelation> mr = getSourceMetadataRelation(metadataId);
    	Set<String> sIds = EntityUtils.getSet(mr, MetadataRelation::getSourceId);
    	sIds.add(metadataId);
        Set<String> ids = Sets.newHashSet();
        sIds.forEach(e -> ids.addAll(getTargetIdsBySourceType(e, MetadataRelationTypeEnum.APP.getCode())));
        return ids.size();
    }

    @Override
    public List<OpenApiApp> getTargetAppBydwRankIdAndPropertyTopicId(String dwRankId, String propertyTopicId) {
        return getOpenApiApps(getTargetIdsByDwRankIdAndPropertyTopicId(dwRankId, propertyTopicId, MetadataRelationTypeEnum.APP.getCode()));
    }

    @Override
    public Integer countTargetAppBydwRankIdAndPropertyTopicId(String dwRankId, String propertyTopicId) {
        return getTargetIdsByDwRankIdAndPropertyTopicId(dwRankId, propertyTopicId, MetadataRelationTypeEnum.APP.getCode()).size();
    }

    @Override
    public List<OpenApiApp> getTargetAppByPropertyTopicId(String propertyTopicId) {
        return getOpenApiApps(getTargetIdsByPropertyTopicId(propertyTopicId, MetadataRelationTypeEnum.APP.getCode()));
    }

    @Override
    public Integer countTargetAppByPropertyTopicId(String propertyTopicId) {
        return getTargetIdsByPropertyTopicId(propertyTopicId, MetadataRelationTypeEnum.APP.getCode()).size();
    }

    @Override
    public List<OpenApiApp> getAllTargetApp() {
        return getOpenApiApps(getAllTargetIds(MetadataRelationTypeEnum.APP.getCode()));
    }

    @Override
    public Integer countAllTargetApp() {
        return getAllTargetIds(MetadataRelationTypeEnum.APP.getCode()).size();
    }

    private List<OpenApiApp> getOpenApiApps(Set<String> appIds) {
        List<OpenApiApp> apps = Lists.newArrayList();
        appIds.forEach(e -> {
            OpenApiApp app = openApiAppService.getApp(e);
            if (app != null) {
                apps.add(app);
            }
        });
        return apps;
    }

    private List<ApiInterface> getApiInterfaces(Set<String> apiIds) {
        return apiInterfaceService.findListByIdIn(apiIds.toArray(new String[apiIds.size()]));
    }

    @Override
    public List<OpenApiApp> getAllAppByMetadataId(String metadataId) {
        Set<String> appIds = getAllMetdataIdsByType(metadataId, MetadataRelationTypeEnum.APP.getCode());
        return getOpenApiApps(appIds);
    }

    private void padMetadataRelation(List<MetadataRelation> metadataRelations) {
        metadataRelations.forEach(e -> {
            e.setSourceName(getName(e.getSourceType(), e.getSourceId()));
            e.setTargetName(getName(e.getTargetType(), e.getTargetId()));
        });
    }

    private String getName(String type, String id) {
        MetadataRelationTypeEnum metadataRelationTypeEnum = MetadataRelationTypeEnum.valueOfCode(type);
        switch (metadataRelationTypeEnum) {
            case TABLE:
                Metadata table = metadataService.findOne(id);
                return Optional.ofNullable(table).map(Metadata::getName).orElse("该数据源已被删除");
            case MODEL:
                DataModel dataModel = dataModelService.findOne(id);
                return Optional.ofNullable(dataModel).map(DataModel::getName).orElse("该数据源已被删除");
            case EVENT:
                Event event = eventService.findOne(id);
                return Optional.ofNullable(event).map(Event::getEventName).orElse("该数据源已被删除");
            case APP:
                OpenApiApp app = openApiAppService.getApp(id);
                return Optional.ofNullable(app).map(OpenApiApp::getName).orElse("该数据源已被删除");
            case DATAX_JOB:
                DataxJob dataxJob = dataxJobService.findOne(id);
                return Optional.ofNullable(dataxJob).map(DataxJob::getName).orElse("该数据源已被删除");
            case ETL_JOB:
                EtlJob etlJob = etlJobService.findOne(id);
                return Optional.ofNullable(etlJob).map(EtlJob::getName).orElse("该数据源已被删除");
            case API:
                ApiInterface api = apiInterfaceService.findOne(id);
                return Optional.ofNullable(api).map(ApiInterface::getTypeName).orElse("该数据源已被删除");
        }
        return StringUtils.EMPTY;
    }

    private MetadataRelationEx getMetadataRelationEx(String metadataId, String mdType, int level) {
        List<MetadataRelation> relations = metadataRelationDao.findBySourceId(metadataId);
        MetadataRelationEx metadataRelationEx = new MetadataRelationEx();
        MetadataRelationTypeEnum metadataRelationTypeEnum = MetadataRelationTypeEnum.valueOfCode(mdType);
        switch (metadataRelationTypeEnum) {
            case ETL_JOB:
                EtlJob job = etlJobService.findOne(metadataId);
                if (job == null) {
                    return null;
                }
                metadataRelationEx.setName(job.getName());
                metadataRelationEx.setValue(job.getJobType());
                break;
            case APP:
                OpenApiApp app = openApiAppService.getApp(metadataId);
                if (app == null) {
                    return null;
                }
                metadataRelationEx.setName(app.getName());
                metadataRelationEx.setValue(app.getDescription());
                break;
            case EVENT:
                Event event = eventService.findOne(metadataId);
                if (event == null) {
                    return null;
                }
                metadataRelationEx.setName(event.getEventName());
                metadataRelationEx.setValue(event.getEventCode());
                break;
            case MODEL:
                DataModel model = dataModelService.findOne(metadataId);
                if (model == null) {
                    return null;
                }
                metadataRelationEx.setName(model.getName());
                metadataRelationEx.setValue(model.getType());
                break;
            case TABLE:
                Metadata metadata = metadataService.findOne(metadataId);
                if (metadata == null) {
                    return null;
                }
                metadataRelationEx.setName(metadata.getName());
                metadataRelationEx.setValue(metadata.getTableName());
                break;
            case DATAX_JOB:
                DataxJob dataxJob = dataxJobService.findOne(metadataId);
                if (dataxJob == null) {
                    return null;
                }
                metadataRelationEx.setName(dataxJob.getName());
                metadataRelationEx.setValue(dataxJob.getScheduleParam());
                break;
            default:
                break;
        }
        List<MetadataRelationEx> children = Lists.newArrayList();
        if (level++ > 9) {
            return null;
        }
        for (MetadataRelation e : relations) {
            MetadataRelationEx ex = getMetadataRelationEx(e.getTargetId(), e.getTargetType(), level);
            if (ex == null) {
                continue;
            }
            children.add(ex);
        }
        metadataRelationEx.setChildren(children);
        return metadataRelationEx;
    }

	@Override
	public List<MetadataRelation> findBySourceIdsAndTargetType(
			String targetType, String... sourceIds) {

		Specification<MetadataRelation> specification = new Specification<MetadataRelation>() {
			 @Override
			 public Predicate toPredicate(Root<MetadataRelation> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
				 List<Predicate> ps = new ArrayList<Predicate>();
				 ps.add(cb.equal(root.get("targetType").as(String.class), targetType));
              	 queryIn("sourceId", sourceIds, root, ps, cb);
				 cq.where(ps.toArray(new Predicate[0]));
				 return cq.getRestriction();
	         }
	     };
	     return metadataRelationDao.findAll(specification);
	}

	@Override
	public List<MetadataRelation> findByTargetIdsAndSourceType(
			String sourceType, String... targetIds) {
		Specification<MetadataRelation> specification = new Specification<MetadataRelation>() {
			 @Override
			 public Predicate toPredicate(Root<MetadataRelation> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
				 List<Predicate> ps = new ArrayList<Predicate>();
				 ps.add(cb.equal(root.get("sourceType").as(String.class), sourceType));
             	 queryIn("targetId", targetIds, root, ps, cb);
				 cq.where(ps.toArray(new Predicate[0]));
				 return cq.getRestriction();
	         }
	     };
	     return metadataRelationDao.findAll(specification);
	}

}
