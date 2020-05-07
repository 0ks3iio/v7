package net.zdsoft.bigdata.metadata.service;

import net.zdsoft.api.base.entity.eis.ApiInterface;
import net.zdsoft.api.base.entity.eis.OpenApiApp;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.metadata.entity.MetadataRelation;
import net.zdsoft.bigdata.metadata.entity.MetadataRelationEx;

import java.util.List;

/**
 * Created by wangdongdong on 2019/1/7 11:41.
 */
public interface MetadataRelationService extends BaseService<MetadataRelation, String> {


    /**
     * 根据源id查询
     * @param sourceId
     * @return
     */
    List<MetadataRelation> findBySourceId(String sourceId);

    /**
     * 根据目标id查询
     * @param targetId
     * @return
     */
    List<MetadataRelation> findByTargetId(String targetId);

    /**
     * 根据源id和源类型查询查询
     * @param sourceId
     * @param sourceType net.zdsoft.bigdata.metadata.enums.MetadataRelationTypeEnum
     * @return
     */
    List<MetadataRelation> findBySourceIdAndSourceType(String sourceId, String sourceType);

    /**
     * 根据目标id和目标类型查询
     * @param targetId
     * @param targetType net.zdsoft.bigdata.metadata.enums.MetadataRelationTypeEnum
     * @return
     */
    List<MetadataRelation> findByTargetIdAndTargetType(String targetId, String targetType);

    /**
     * 保存血缘关系（若关系已存在，则不处理）
     * @param metadataRelation
     * @throws BigDataBusinessException
     */
    void saveMetadataRelation(MetadataRelation metadataRelation) throws BigDataBusinessException;

    /**
     * 血缘关系图
     * @param metadataId
     * @param mdType
     * @return
     */
    MetadataRelationEx getMetadataRelationGather(String metadataId, String mdType);

    /**
     * 根据目标数据源id删除
     * @param targetId
     */
    void deleteByTargetId(String targetId);

    /**
     * 根据源id删除
     * @param sourceId
     */
    void deleteBySourceId(String sourceId);

    /**
     * 根据源和目标删除关系
     *
     * @param sourceId
     * @param targetId
     */
    void deleteBySourceIdAndTargetId(String sourceId, String targetId);

    /**
     * 
     * @param targetType
     * @param sourceIds
     * @return
     */
    List<MetadataRelation> findBySourceIdsAndTargetType(String targetType,String... sourceIds );
    
    /**
     * 
     * @param sourceType
     * @return
     * @param targetIds
     */
    List<MetadataRelation> findByTargetIdsAndSourceType(String sourceType,String... targetIds);

    /**
     * 获取某个元数据的应用来源和去向应用列表
     * @return
     */
    List<OpenApiApp> getAllAppByMetadataId(String metadataId);

    /**
     * 获取某个层次加主题的整个关系列表
     * @return
     */
    List<MetadataRelation> getMetadataRelation(String dwRankId, String propertyTopicId);

    /**
     * 获取某个元数据的整个关系列表
     * @return
     */
    List<MetadataRelation> getMetadataRelationByMetadataId(String metadataId);

    /**
     * 获取某个元数据的应用来源和去向应用列表
     * @return
     */
    List<OpenApiApp> getAllAppBydwRankIdAndPropertyTopicId(String dwRankId, String propertyTopicId);

    /**
     * 获取某个主题下的job和datax job关系
     * @return
     */
    List<MetadataRelation> getJobMetadataRelation();

    /**
     * 获取所有的应用来源和去向列表
     * @return
     */
    List<OpenApiApp> getAllApp();

    /**
     * 统计某个元数据应用来源和去向应用数量
     * @param metadataId
     * @return
     */
    Integer countAppByMetadataId(String metadataId);

    /**
     * 统计某个层次+主题应用来源和应用数量
     * @param dwRankId
     * @param propertyTopicId
     * @return
     */
    Integer countAppBydwRankIdAndPropertyTopicId(String dwRankId, String propertyTopicId);

    /**
     * 统计总的应用来源和应用数量
     * @return
     */
    Integer countAllApp();

    /**
     * 获取某个元数据的接口列表
     *
     * @param metadataId
     * @return
     */
    List<ApiInterface> getAllApiByMetadataId(String metadataId);

    /**
     * 获取某个层次+主题的接口列表
     *
     * @param dwRankId
     * @param propertyTopicId
     * @return
     */
    List<ApiInterface> getAllApiBydwRankIdAndPropertyTopicId(String dwRankId, String propertyTopicId);

    /**
     * 获取所有接口列表
     * @return
     */
    List<ApiInterface> getAllApi();

    /**
     * 统计某个元数据的接口数量
     * @param metadataId
     * @return
     */
    Integer countApiByMetadataId(String metadataId);

    /**
     * 统计某个层次+主题接口数量
     * @param dwRankId
     * @param propertyTopicId
     * @return
     */
    Integer countApiBydwRankIdAndPropertyTopicId(String dwRankId, String propertyTopicId);

    /**
     * 统计某个主题接口数量
     * @param propertyTopicId
     * @return
     */
    Integer countApiByPropertyTopicId(String propertyTopicId);

    /**
     * 统计总的接口数量
     * @return
     */
    Integer countAllApi();
    
    /**
    *
    * @return
    */
   List<MetadataRelation> getMetadataRelation(String metaId);


    /*====================================来源应用相关查询======================================================*/

    /**
     * 获取某个元数据的应用来源
     * @return
     */
    List<OpenApiApp> getSourceAppByMetadataId(String metadataId);

    /**
     * 统计某个元数据的应用来源数量
     * @param metadataId
     * @return
     */
    Integer countSourceAppByMetadataId(String metadataId);

    /**
     * 根据层次和主题获取应用来源列表
     * @return
     */
    List<OpenApiApp> getSourceAppBydwRankIdAndPropertyTopicId(String dwRankId, String propertyTopicId);

    /**
     * 统计 层次和主题下应用来源数量
     * @return
     */
    Integer countSourceAppBydwRankIdAndPropertyTopicId(String dwRankId, String propertyTopicId);

    /**
     * 根据主题获取应用来源列表
     * @return
     */
    List<OpenApiApp> getSourceAppByPropertyTopicId(String propertyTopicId);

    /**
     * 统计主题下应用来源数量
     * @return
     */
    Integer countSourceAppByPropertyTopicId(String propertyTopicId);

    /**
     * 获取所有的应用来源
     * @return
     */
    List<OpenApiApp> getAllSourceApp();

    /**
     * 统计所有应用来源数量
     * @return
     */
    Integer countAllSourceApp();




    /*====================================去向应用相关查询======================================================*/

    /**
     * 获取某个元数据的去向应用
     * @return
     */
    List<OpenApiApp> getTargetAppByMetadataId(String metadataId);

    /**
     * 统计某个元数据的应用去向数量
     * @param metadataId
     * @return
     */
    Integer countTargetAppByMetadataId(String metadataId);

    /**
     * 根据层次和主题获取应用去向列表
     * @return
     */
    List<OpenApiApp> getTargetAppBydwRankIdAndPropertyTopicId(String dwRankId, String propertyTopicId);

    /**
     * 统计层次和主题下应用去向数量
     * @return
     */
    Integer countTargetAppBydwRankIdAndPropertyTopicId(String dwRankId, String propertyTopicId);

    /**
     * 根据主题获取应用去向列表
     * @return
     */
    List<OpenApiApp> getTargetAppByPropertyTopicId(String propertyTopicId);

    /**
     * 统计主题下应用去向数量
     * @return
     */
    Integer countTargetAppByPropertyTopicId(String propertyTopicId);

    /**
     * 获取所有的应用去向
     * @return
     */
    List<OpenApiApp> getAllTargetApp();

    /**
     * 统计所有的应用去向数量
     * @return
     */
    Integer countAllTargetApp();

	List<MetadataRelation> getTargetMetadataRelation(String dwRankId,
			String propertyTopicId);

	List<MetadataRelation> getSourceMetadataRelation(String dwRankId,
			String propertyTopicId);

	List<MetadataRelation> getTargetMetadataRelation(String metaId);

	List<MetadataRelation> getSourceMetadataRelation(String metaId);

}
