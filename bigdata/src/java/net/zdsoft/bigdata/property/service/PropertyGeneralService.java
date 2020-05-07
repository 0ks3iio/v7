package net.zdsoft.bigdata.property.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.bigdata.base.entity.DwRank;
import net.zdsoft.bigdata.base.entity.PropertyTopic;
import net.zdsoft.bigdata.metadata.entity.Metadata;
import net.zdsoft.bigdata.metadata.entity.MetadataTableColumn;
import net.zdsoft.bigdata.property.vo.MetaRelationVo;
import net.zdsoft.bigdata.property.vo.MetadataVo;
import net.zdsoft.framework.entity.Json;

public interface PropertyGeneralService {
	
	/**
	 * 查询层级 信息（名称）
	 * @return
	 */
	public List<DwRank> findLevels();
	
	public DwRank findLevel(String levelId);
	
	/**
	 * 根据层级查询可显示的主题列表（名称、图标）
	 * @param levelId
	 * @return
	 */
	public PropertyTopic findTopic(String topicId);

	/**
	 * 主题map
	 * @return key 层级id, value 主题集合
	 */
	Map<String, List<PropertyTopic>> findPropertyTopicMap();

	/**
	 * 根据层级和主题，获取主题下元数据信息（名称，图标，数据量，接口数量，数据质量，最新统计的时间（yyyy-mm-dd））
	 * @param levelId
	 * @param topicId
	 * @return
	 */
	public List<MetadataVo> findMetadatas(String levelId,String topicId);
	
	/**
	 * 根据元数据id，获取详细信息（名称，说明，数据总量，接口数，数据提供方数量+图标，数据使用方数量+图标,最新统计的时间（yyyy-mm-dd））
	 * @param metaId
	 * @return
	 */
	public Json findStatInfo(String key,int times);
	
	/**
	 * 获取最近length天的日增长量
	 * @param key
	 * @param length
	 * @return
	 */
	public List<String[]> findStatRecentyDays(String key,int length);
	
	public Metadata findMetadata(String metaId);
	/**
	 * 
	 * @param levelId
	 * @param topicId
	 * @param metaId
	 * @param type  1主题找  2.元数据找
	 * @return
	 */
	public Set<String> findSourceUrl(String levelId,String topicId,String metaId,int type);
	
	public Set<String> findTargetUrl(String levelId,String topicId,String metaId,int type);

	public MetaRelationVo findRelationByMeta(String metaId);
	
	public List<MetadataTableColumn> findShowColumn(String metaId);

	public List<Json> findShowColumnListDatas(String metaId,int pageIndex);

	public String findMetaOneDatas(String metaId, String id);
}
