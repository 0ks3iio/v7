package net.zdsoft.bigdata.property.action;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import net.zdsoft.bigdata.base.entity.DwRank;
import net.zdsoft.bigdata.base.entity.PropertyTopic;
import net.zdsoft.bigdata.base.service.BgPropertyTopicService;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.extend.data.entity.FormSet;
import net.zdsoft.bigdata.extend.data.service.FormSetService;
import net.zdsoft.bigdata.metadata.entity.Metadata;
import net.zdsoft.bigdata.metadata.entity.MetadataTableColumn;
import net.zdsoft.bigdata.metadata.service.MetadataService;
import net.zdsoft.bigdata.metadata.service.MetadataTableColumnService;
import net.zdsoft.bigdata.property.constant.BgPropertyConstant;
import net.zdsoft.bigdata.property.service.PropertyGeneralService;
import net.zdsoft.bigdata.property.vo.MetaRelationVo;
import net.zdsoft.bigdata.property.vo.MetadataVo;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.system.remote.service.SysOptionRemoteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

@Controller
@RequestMapping(value = { "/bigdata/property/general" })
public class GeneralController  extends BigdataBaseAction {
	@Autowired
	private PropertyGeneralService propertyGeneralService;
	@Autowired
	private MetadataService metadataService;
	@Autowired
	private MetadataTableColumnService metadataTableColumnService;
	@Autowired
	private BgPropertyTopicService bgPropertyTopicService;
	@Autowired
	private SysOptionRemoteService sysOptionRemoteService;
	@Autowired
	private FormSetService formSetService;
	
	@RequestMapping("/index")
	public String index(ModelMap map) {
		//查询层级 信息（名称）
//		List<DwRank> levels = propertyGeneralService.findLevels();
//		
//		Map<String, List<PropertyTopic>> topicMap =  propertyGeneralService.findPropertyTopicMap();
//		for(DwRank level:levels){
//			if(!topicMap.containsKey(level.getId())){
//				topicMap.put(level.getId(), Lists.newArrayList());
//			}
//		}
		
		List<Metadata> metadataList = metadataService.findPropertyMetadata();
        Map<String, List<Metadata>> metadataMap = metadataList.stream().collect(Collectors.groupingBy(Metadata::getPropertyTopicId));
        List<PropertyTopic> topics  = Lists.newArrayList();
        if(metadataMap.size()>0){
        	topics = bgPropertyTopicService.findListByIdIn(metadataMap.keySet().toArray(new String[0]));
        }
		map.put("topics", topics);
		map.put("metaMap", metadataMap);
		return "/bigdata/property/general/index.ftl";
	}
	
	@RequestMapping("/topic/detail")
	public String topic(String levelId,String topicId,ModelMap map) {
		//层级查询可显示的主题列表（名称、图标）
		PropertyTopic topic = propertyGeneralService.findTopic(topicId);
		DwRank level = propertyGeneralService.findLevel(levelId);
		String name = "";
		String remark = "";
		if(level!=null)name = level.getCode()+level.getName()+"-";
		if(topic!=null){
			name+=topic.getName();
			remark = topic.getRemark();
		}
		map.put("name", name);
		map.put("remark", remark);
		map.put("levelId", levelId);
		map.put("topicId", topicId);
		return "/bigdata/property/general/topicInfo.ftl";
	}
	
	@RequestMapping("/recent/daily/acount")
	@ResponseBody
	public Response findStatRecentyDays(String key){
		List<String[]> datas = propertyGeneralService.findStatRecentyDays(key, BgPropertyConstant.QUERY_DAILY_ACOUNT_DAYS);
		return Response.ok().data(datas).build();
	}
	
	@RequestMapping("/stat/info")
	@ResponseBody
	public Response findStatInfo(String key,String metaId,String topicId){
		//根据层级和主题，获取主题下详细信息（主题说明，数据总量，元数据数，数据提供方数量+图标，数据使用方数量+图标,最新统计的时间（yyyy-mm-dd））
		Json json = propertyGeneralService.findStatInfo(key,BgPropertyConstant.QUERY_STAT_TABLE_TIMES);
		if(json == null)json = new Json();
		if(StringUtils.isNotBlank(topicId)){
			PropertyTopic topic = propertyGeneralService.findTopic(topicId);
			if(topic!=null)json.put("topic_name", topic.getName());
			if(topic!=null)json.put("topic_remark", topic.getRemark()==null?"暂无备注!":topic.getRemark());
		}
		if(StringUtils.isNotBlank(metaId)){
			Metadata meta = propertyGeneralService.findMetadata(metaId);
			if(meta!=null)json.put("meta_name", meta.getName());
			if(meta!=null)json.put("meta_remark", meta.getRemark()==null?"暂无备注!":meta.getRemark());
		}
		return Response.ok().data(json).build();
	}
	
	@RequestMapping("/topic/metadatas")
	public String topicMetadatas(String levelId,String topicId,ModelMap map){
		//根据层级和主题，获取主题下元数据信息（名称，图标，数据量，接口数量，数据质量，最新统计的时间（yyyy-mm-dd））
		List<MetadataVo> metas = propertyGeneralService.findMetadatas(levelId, topicId);
		map.put("metadatas", metas);
		return "/bigdata/property/general/metadataList.ftl";
	}
	
	@RequestMapping("/metadata/info")
	public String metadata(String metaId,ModelMap map){
		//根据层级和主题，获取主题下元数据信息（名称，图标，数据量，接口数量，数据质量，最新统计的时间（yyyy-mm-dd））
		Metadata meta = propertyGeneralService.findMetadata(metaId);
		map.put("meta", meta);
		return "/bigdata/property/general/metadata.ftl";
	}
	
	@RequestMapping("/source/target/url")
	@ResponseBody
	public Response findSourceUrl(String levelId,String topicId,String metaId,int type){
		List<Set<String>> datas = Lists.newLinkedList();
		Set<String> surls = propertyGeneralService.findSourceUrl(levelId, topicId, metaId, type);
		Set<String> turls = propertyGeneralService.findTargetUrl(levelId, topicId, metaId, type);
		datas.add(surls);
		datas.add(turls);
		return Response.ok().data(datas).build();
	}
	
	@RequestMapping("/source/target/relation")
	@ResponseBody
	public Response findRelation(String metaId){
		MetaRelationVo  vo = propertyGeneralService.findRelationByMeta(metaId);
		return Response.ok().data(vo).build();
	}
	
	@RequestMapping("/meta/list/head")
	public String metaListHead(String metaId,ModelMap map){
		List<MetadataTableColumn> cols =  propertyGeneralService.findShowColumn(metaId);
		String mainkey = "";
		boolean skip = true;
		for(MetadataTableColumn mc:cols){
			if(mc.getIsPrimaryKey()==1){
				mainkey = mc.getColumnName();
				if(mc.getStatType()!=null&&mc.getStatType().contains("showColumn")){
					skip = false;
				}
			}
		}
		if(skip)cols = cols.stream().filter(line -> line.getIsPrimaryKey()!=1).collect(Collectors.toList());
		map.put("cols", cols);
		map.put("metaId", metaId);
		map.put("mainkey", mainkey);
		FormSet form = formSetService.findByMdId(metaId);
		if(form!=null&&StringUtils.isNotBlank(mainkey)){
			map.put("showDetail", true);
		}else{
			map.put("showDetail", false);
		}
		return "/bigdata/property/general/metaListHead.ftl";
	}
	
	@RequestMapping("/meta/list/detail")
	@ResponseBody
	public Response metaListDetail(String metaId,int pageIndex){
		List<Json> metaDetails = propertyGeneralService.findShowColumnListDatas(metaId,pageIndex);
		return Response.ok().data(metaDetails).build();
	}
	
	@RequestMapping("/meta/{metaId}/{id}")
	public String metaOne(@PathVariable String metaId,@PathVariable String id,ModelMap map){
		String html = propertyGeneralService.findMetaOneDatas(metaId,id);
		map.put("html", html);
		return "/bigdata/property/general/metaDetail.ftl";
	}
	
}
