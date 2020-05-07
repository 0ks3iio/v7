package net.zdsoft.bigdata.property.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import net.zdsoft.api.base.service.OpenApiAppService;
import net.zdsoft.bigdata.base.entity.DwRank;
import net.zdsoft.bigdata.base.entity.PropertyTopic;
import net.zdsoft.bigdata.base.service.BgDwRankService;
import net.zdsoft.bigdata.base.service.BgPropertyTopicService;
import net.zdsoft.bigdata.datax.enums.DataxTypeEnum;
import net.zdsoft.bigdata.extend.data.biz.BgCalStorage;
import net.zdsoft.bigdata.extend.data.service.FormSetService;
import net.zdsoft.bigdata.frame.data.hbase.HbaseClientService;
import net.zdsoft.bigdata.frame.data.mysql.MysqlClientService;
import net.zdsoft.bigdata.frame.data.phoenix.PhoenixClientService;
import net.zdsoft.bigdata.metadata.entity.Metadata;
import net.zdsoft.bigdata.metadata.entity.MetadataRelation;
import net.zdsoft.bigdata.metadata.entity.MetadataTableColumn;
import net.zdsoft.bigdata.metadata.enums.MetadataRelationTypeEnum;
import net.zdsoft.bigdata.metadata.service.MetadataRelationService;
import net.zdsoft.bigdata.metadata.service.MetadataService;
import net.zdsoft.bigdata.metadata.service.MetadataTableColumnService;
import net.zdsoft.bigdata.property.constant.BgPropertyConstant;
import net.zdsoft.bigdata.property.service.PropertyGeneralService;
import net.zdsoft.bigdata.property.vo.MetaRelationVo;
import net.zdsoft.bigdata.property.vo.MetadataVo;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.StringUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
@Service("propertyGeneralService")
public class PropertyGeneralServiceImpl implements PropertyGeneralService {
	
	@Resource
	private PhoenixClientService phoenixClientService;
	@Resource
	private HbaseClientService hbaseClientService;
	@Resource
	private MysqlClientService mysqlClientService;
	@Resource
	private BgDwRankService bgDwRankService;
	@Resource
	private BgPropertyTopicService bgPropertyTopicService;
	@Resource
	private MetadataService metadataService;
	@Resource
	private MetadataRelationService metadataRelationService;
	@Resource
	private OpenApiAppService openApiAppService;
	@Resource
	private MetadataTableColumnService metadataTableColumnService;
	@Resource
	private FormSetService formSetService;

	@Override
	public List<DwRank> findLevels() {
		return bgDwRankService.findAll();
	}

	@Override
	public PropertyTopic findTopic(String topicId) {
		return bgPropertyTopicService.findOne(topicId);
	}

	@Override
	public Json findStatInfo(String key,int times) {
		String today = DateUtils.date2String(DateUtils.addDay(new Date(), times-BgPropertyConstant.QUERY_STAT_TABLE_TIMES), "yyyyMMdd");
		String sql = "select * from DWC_STAT_METADATA_DAILY where ID= '"+key+today+"'";
//		Json j = hbaseClientService.getOneRowAndMultiColumn(BgPropertyConstant.STAT_METADATA_DAILY_TABLE, key+today, null);
		List<Json> resultList = phoenixClientService.getDataListFromPhoenix(null, sql, new ArrayList<>());
		times--;
		if(CollectionUtils.isNotEmpty(resultList)){
			return resultList.get(0);
		}else{
			if(times<=0){
				return new Json();
			}else{
				return findStatInfo(key,times);
			}
		}
	}
	
	public Map<String, List<PropertyTopic>> findPropertyTopicMap() {
        Map<String, List<PropertyTopic>> topicMap = Maps.newHashMap();
		List<Metadata> metadataList = metadataService.findPropertyMetadata();
        Map<String, List<Metadata>> metadataMap = metadataList.stream().collect(Collectors.groupingBy(Metadata::getDwRankId));
        metadataMap.forEach((k, v) -> {
            topicMap.put(k, bgPropertyTopicService.findListByIdIn(v.stream().map(Metadata::getPropertyTopicId).toArray(String[]::new)));
        });
		return topicMap;
	}


	@Override
	public DwRank findLevel(String levelId) {
		return bgDwRankService.findOne(levelId);
	}

	@Override
	public List<MetadataVo> findMetadatas(String levelId, String topicId) {
		List<MetadataVo> metaVos = Lists.newArrayList();
		Map<String,Json> valMap = Maps.newHashMap();
		List<Metadata> metas = metadataService.findByDwRankAndPropertyTopic(levelId, topicId);
		Set<String> keys = EntityUtils.getSet(metas, Metadata::getId);
		List<Json> resultList = 
				findStatByKeys(keys.toArray(new String[keys.size()]), BgPropertyConstant.QUERY_STAT_TABLE_TIMES);
		for(Json j:resultList){
			String id = j.getString("id");
			if(StringUtils.isNotBlank(id)&&id.length()>8)
			valMap.put(id.substring(0,id.length()-8), j);
		}
		for(Metadata meta:metas){
			MetadataVo vo = new MetadataVo();
			vo.setId(meta.getId());
			vo.setName(meta.getName());
			if(valMap.containsKey(meta.getId())){
				Json j = valMap.get(meta.getId());
				if(j==null)continue;
				vo.setDataCount(j.getString("total_amount"));
				vo.setApiCount(j.getString("api_amount"));
				vo.setDataQuality(j.getString("data_quality"));
				vo.setStatDate(j.getString("stat_time"));
			}
			metaVos.add(vo);
		}
		return metaVos;
	}

	@Override
	public Metadata findMetadata(String metaId) {
		return metadataService.findOne(metaId);
	}

	@Override
	public List<String[]> findStatRecentyDays(String key, int length) {
		List<String[]> datas = Lists.newLinkedList(); 
		Map<String,String> dateMap = Maps.newLinkedHashMap();
		Map<String,String> valMap = Maps.newHashMap();
		for(int i=length;i>0;i--){
			Date date = DateUtils.addDay(new Date(), -i);
			String day = DateUtils.date2String(date, "yyyyMMdd");
			String value = DateUtils.date2String(date, "MM-dd");
			dateMap.put(key+day, value);
		}
		String startDay = DateUtils.date2String(DateUtils.addDay(new Date(), -length), "yyyyMMdd");
		String today = DateUtils.date2String(new Date(), "yyyyMMdd");
		String sql = "select * from DWC_STAT_METADATA_DAILY where ID <= '"+key+today+"' and ID >'"+key+startDay+"'";
		List<Json> resultList = phoenixClientService.getDataListFromPhoenix(null, sql, new ArrayList<>());
		for(Json j:resultList){
			valMap.put(j.getString("id"), j.getString("daily_amount"));
		}
		List<String> acounts = Lists.newLinkedList();
		for(String key1:dateMap.keySet()){
			if(valMap.containsKey(key1)){
				acounts.add(valMap.get(key1));
			}else{
				acounts.add("0");
			}
		}
		datas.add(dateMap.values().toArray(new String[dateMap.values().size()]));
		datas.add(acounts.toArray(new String[acounts.size()]));
		return datas;
	}
	
	private List<Json> findStatByKeys(String[] keys,int times) {
		if(keys==null||keys.length == 0)return new ArrayList<>();
		String today = DateUtils.date2String(DateUtils.addDay(new Date(), times-BgPropertyConstant.QUERY_STAT_TABLE_TIMES), "yyyyMMdd");
		StringBuilder sql = new StringBuilder("select * from DWC_STAT_METADATA_DAILY where ID in ("); 
		int index = keys.length;
		for(String key:keys){
			index--;
			sql.append("'");
			sql.append(key);
			sql.append(today);
			if(index==0){
				sql.append("'");
			}else{
				sql.append("',");
			}
		}
		sql.append(")");
		List<Json> resultList = phoenixClientService.getDataListFromPhoenix(null, sql.toString(), new ArrayList<>());
		times--;
		if(CollectionUtils.isNotEmpty(resultList)){
			return resultList;
		}else{
			if(times<=0){
				return new ArrayList<>();
			}else{
				return findStatByKeys(keys,times);
			}
		}
	}

	@Override
	public Set<String> findSourceUrl(String levelId, String topicId,
			String metaId, int type) {
		Set<String> metaIds = Sets.newHashSet();
		Set<String> urls = Sets.newHashSet();
		if(type==1){
			List<MetadataRelation> mr = metadataRelationService.getTargetMetadataRelation(metaId);
			metaIds.addAll(EntityUtils.getSet(mr, MetadataRelation::getTargetId));
		}else{
			List<MetadataRelation> mr = metadataRelationService.getTargetMetadataRelation(levelId, topicId);
			List<Metadata> metas = metadataService.findByDwRankAndPropertyTopic(levelId, topicId);
			metaIds.addAll(EntityUtils.getSet(metas, Metadata::getId));
			metaIds.addAll(EntityUtils.getSet(mr, MetadataRelation::getTargetId));
		}
		if(CollectionUtils.isNotEmpty(metaIds)){
			List<MetadataRelation> relations = metadataRelationService.findByTargetIdsAndSourceType(MetadataRelationTypeEnum.APP.getCode(), metaIds.toArray(new String[metaIds.size()]));
			Set<String> sourceIds = EntityUtils.getSet(relations, MetadataRelation::getSourceId);
			int index = 1;
			for(String id:sourceIds){
				if(index>5)break;
				String url = openApiAppService.getAppHttpIconUrl(id);
				if(StringUtils.isNotBlank(url)){
					urls.add(url);
					index++;
				}
			}
		}
		return urls;
	}

	@Override
	public Set<String> findTargetUrl(String levelId, String topicId,
			String metaId, int type) {
		Set<String> metaIds = Sets.newHashSet();
		Set<String> urls = Sets.newHashSet();
		if(type==1){
			List<MetadataRelation> mr = metadataRelationService.getSourceMetadataRelation(metaId);
			metaIds.add(metaId);
			metaIds.addAll(EntityUtils.getSet(mr, MetadataRelation::getSourceId));
		}else{
			List<MetadataRelation> mr = metadataRelationService.getSourceMetadataRelation(levelId, topicId);
			List<Metadata> metas = metadataService.findByDwRankAndPropertyTopic(levelId, topicId);
			metaIds.addAll(EntityUtils.getSet(metas, Metadata::getId));
			metaIds.addAll(EntityUtils.getSet(mr, MetadataRelation::getSourceId));
		}
		if(CollectionUtils.isNotEmpty(metaIds)){
			List<MetadataRelation> relations = metadataRelationService.findBySourceIdsAndTargetType(MetadataRelationTypeEnum.APP.getCode(), metaIds.toArray(new String[metaIds.size()]));
			Set<String> targetIds = EntityUtils.getSet(relations, MetadataRelation::getTargetId);
			int index = 1;
			for(String id:targetIds){
				if(index>5)break;
				String url = openApiAppService.getAppHttpIconUrl(id);
				if(StringUtils.isNotBlank(url)){
					urls.add(url);
					index++;
				}
			}
		}
		return urls;
	}

	@Override
	public MetaRelationVo findRelationByMeta(String metaId) {
		List<MetadataRelation> relations =  metadataRelationService.getMetadataRelation(metaId);
		MetaRelationVo vo = new MetaRelationVo();
		if(CollectionUtils.isEmpty(relations)){
			Metadata meta = findMetadata(metaId);
			if(meta!=null){
				MetaRelationVo.MetaRelationName name = vo.new MetaRelationName();
				name.setCategory(0);
				name.setLabel(meta.getName());
				name.setName(meta.getId());
				vo.getData().add(name);
			}
		}
		Map<String,MetadataRelation> reMap = EntityUtils.getMap(relations, MetadataRelation::getSourceId);
		Map<String,MetadataRelation> reLMap = Maps.newHashMap(); 
		for(String key :reMap.keySet()){
			MetaRelationVo.MetaRelationName name = vo.new MetaRelationName();
			MetadataRelation mre = reMap.get(key);
			if(reLMap.containsKey(key)){
				continue;
			}
			reLMap.put(key, mre);
			MetadataRelationTypeEnum metadataRelationTypeEnum = MetadataRelationTypeEnum.valueOfCode(mre.getSourceType());
	        switch (metadataRelationTypeEnum) {
	            case ETL_JOB:
	            	name.setCategory(2);
	                break;
	            case APP:
	            	name.setCategory(4);
	                break;
	            case API:
	            	name.setCategory(3);
	            	break;
	            case EVENT:
	            	name.setCategory(6);
	                break;
	            case MODEL:
	            	name.setCategory(5);
	                break;
	            case TABLE:
	            	name.setCategory(0);
	                break;
	            case DATAX_JOB:
	            	name.setCategory(1);
	                break;
	            default:
	            	name.setCategory(0);
	                break;
	        }
			name.setLabel(mre.getSourceName());
			name.setName(key);
			vo.getData().add(name);
		}
		reMap = EntityUtils.getMap(relations, MetadataRelation::getTargetId);
		for(String key :reMap.keySet()){
			MetaRelationVo.MetaRelationName name = vo.new MetaRelationName();
			MetadataRelation mre = reMap.get(key);
			if(reLMap.containsKey(key)){
				continue;
			}
			reLMap.put(key, mre);
			MetadataRelationTypeEnum metadataRelationTypeEnum = MetadataRelationTypeEnum.valueOfCode(mre.getTargetType());
	        switch (metadataRelationTypeEnum) {
		        case ETL_JOB:
	            	name.setCategory(2);
	                break;
	            case APP:
	            	name.setCategory(4);
	                break;
	            case API:
	            	name.setCategory(3);
	            	break;
	            case EVENT:
	            	name.setCategory(6);
	                break;
	            case MODEL:
	            	name.setCategory(5);
	                break;
	            case TABLE:
	            	name.setCategory(0);
	                break;
	            case DATAX_JOB:
	            	name.setCategory(1);
	                break;
	            default:
	            	name.setCategory(0);
	                break;
	        }
			name.setLabel(mre.getTargetName());
			name.setName(key);
			vo.getData().add(name);
		}
		for(MetadataRelation re:relations){
			MetaRelationVo.MetaRelationTo to = vo.new MetaRelationTo();
			to.setSource(re.getSourceId());
			to.setTarget(re.getTargetId());
			vo.getLinks().add(to);
		}
		return vo;
	}

	@Override
	public List<MetadataTableColumn> findShowColumn(String metaId) {
		List<MetadataTableColumn> cols =  metadataTableColumnService.findByMetadataId(metaId);
		return cols.stream().filter(line -> (line.getIsPrimaryKey()!=null&&line.getIsPrimaryKey()==1)||(line.getStatType()!=null&&line.getStatType().contains("showColumn"))).collect(Collectors.toList());
	}

	@Override
	public List<Json> findShowColumnListDatas(String metaId,int pageIndex) {
		List<Json> lists =Lists.newArrayList();
		List<MetadataTableColumn> cols = findShowColumn(metaId);
		Metadata meta = findMetadata(metaId);
		if(meta ==null ||pageIndex<1||CollectionUtils.isEmpty(cols)){
			return lists;
		}
		if ("hbase".equals(meta.getDbType())&&meta.getIsPhoenix() == 1) {
			StringBuilder parm = new StringBuilder();
			int size = cols.size();
        	int index = 1;
			for(MetadataTableColumn cl:cols){
				parm.append(cl.getColumnName().split(":")[1]);
				if(size!=index){
        			parm.append(",");	
        		}
        		index++;
			}
			StringBuilder infoSql = new StringBuilder();
			infoSql.append("SELECT ");
			infoSql.append(parm.toString());
			infoSql.append(" FROM ");
			infoSql.append(meta.getTableName());
			infoSql.append(" LIMIT 50 OFFSET");
			infoSql.append(50*(pageIndex-1));
			lists = phoenixClientService.getDataListFromPhoenix(null, infoSql.toString(), null);
        } else if ("mysql".equals(meta.getDbType())){
        	StringBuilder parm = new StringBuilder();
        	int size = cols.size();
        	int index = 1;
        	for(MetadataTableColumn cl:cols){
        		parm.append(cl.getColumnName());
        		if(size!=index){
        			parm.append(",");	
        		}
        		index++;
        	}
        	StringBuilder infoSql = new StringBuilder();
        	infoSql.append("SELECT ");
        	infoSql.append(parm.toString());
        	infoSql.append(" FROM ");
        	infoSql.append(meta.getTableName());
        	infoSql.append(" LIMIT ");
			infoSql.append(50*(pageIndex-1));
			infoSql.append(",50");
			lists = mysqlClientService.getDataListFromMysql(null, null, infoSql.toString(), null, null);
        }
		
		return lists;
	}

	@Override
	public String findMetaOneDatas(String metaId, String id) {
		List<Json> lists =Lists.newArrayList();
		String mainKey = "id";
		List<MetadataTableColumn> cols =  metadataTableColumnService.findByMetadataId(metaId);
		for(MetadataTableColumn c:cols){
			if(c.getIsPrimaryKey()==1){mainKey = c.getColumnName();}
		}
		Metadata meta = findMetadata(metaId);
		if(meta ==null){
			return "";
		}
		String html = formSetService.findHtmlByMdId(metaId);
		if(StringUtils.isBlank(html)){
			return "";
		}
		StringBuilder infoSql = new StringBuilder("select * from ");
		infoSql.append(meta.getTableName());
		infoSql.append(" where ");
		infoSql.append(mainKey);
		infoSql.append(" = '");
		infoSql.append(id);
		infoSql.append("'");
		if ("hbase".equals(meta.getDbType())&&meta.getIsPhoenix() == 1) {
			lists = phoenixClientService.getDataListFromPhoenix(null, infoSql.toString(), null);
        } else if ("mysql".equals(meta.getDbType())){
        	lists = mysqlClientService.getDataListFromMysql(null, null, infoSql.toString(), null, null);
		}
		Map<String,String> valuesMap = Maps.newHashMap();
		for(MetadataTableColumn c:cols){
			String colName = c.getColumnName();
			String columnType = c.getColumnType();
			if(StringUtils.isBlank(colName)||StringUtils.isBlank(columnType))continue;
			String colVal = "";
			for(Json j:lists){
				if (columnType.equalsIgnoreCase("TIMESTAMP")
						|| columnType.equalsIgnoreCase("DATE")) {
					colVal=j.getDate(colName)==null?"":DateUtils.date2StringBySecond(j.getDate(colName));
				} else if (columnType.equalsIgnoreCase("TINYINT")
						|| columnType.equalsIgnoreCase("SMALLINT")
						|| columnType.equalsIgnoreCase("INT")
						|| columnType.equalsIgnoreCase("INTEGER")) {
					colVal=j.getInteger(colName)==null?"":String.valueOf(j.getInteger(colName));
				} else if (columnType.equalsIgnoreCase("FLOAT")) {
					colVal=j.getFloat(colName)==null?"":String.valueOf(j.getFloat(colName));
				} else if (columnType.equalsIgnoreCase("DOUBLE")) {
					colVal=j.getDouble(colName)==null?"":String.valueOf(j.getDouble(colName));
				}else{
					colVal=j.getString(colName)==null?"":j.getString(colName);
				}
			}
			valuesMap.put(colName, colVal);
		}
		StrSubstitutor sub = new StrSubstitutor(valuesMap, "{#", "}");
		html = sub.replace(html);
		return html;
	}

}
