package net.zdsoft.bigdata.extend.data.biz;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.zdsoft.basedata.service.BaseJdbcService;
import net.zdsoft.bigdata.extend.data.entity.TagRuleRelation;
import net.zdsoft.bigdata.extend.data.entity.TagRuleSymbol;
import net.zdsoft.bigdata.extend.data.entity.UserProfile;
import net.zdsoft.bigdata.extend.data.entity.UserTag;
import net.zdsoft.bigdata.extend.data.entity.WarningProject;
import net.zdsoft.bigdata.extend.data.entity.WarningResult;
import net.zdsoft.bigdata.extend.data.enums.WarnResultStatusEnum;
import net.zdsoft.bigdata.extend.data.enums.WarnResultTypeEnum;
import net.zdsoft.bigdata.extend.data.service.TagRuleRelationService;
import net.zdsoft.bigdata.extend.data.service.TagRuleSymbolService;
import net.zdsoft.bigdata.extend.data.service.UserProfileService;
import net.zdsoft.bigdata.extend.data.service.UserTagService;
import net.zdsoft.bigdata.extend.data.service.WarningProjectService;
import net.zdsoft.bigdata.extend.data.service.WarningResultService;
import net.zdsoft.bigdata.frame.data.hbase.HbaseClientService;
import net.zdsoft.bigdata.frame.data.mysql.MysqlClientService;
import net.zdsoft.bigdata.frame.data.phoenix.PhoenixClientService;
import net.zdsoft.bigdata.metadata.entity.Metadata;
import net.zdsoft.bigdata.metadata.entity.MetadataTableColumn;
import net.zdsoft.bigdata.metadata.service.MetadataService;
import net.zdsoft.bigdata.metadata.service.MetadataTableColumnService;
import net.zdsoft.bigdata.taskScheduler.service.EtlJobService;
import net.zdsoft.framework.delayQueue.DelayItem;
import net.zdsoft.framework.delayQueue.DelayQueueService;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.Objects;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

@Service("bgCalService")
public class BgCalServiceImpl implements BgCalService {
	public static final String DWD_APP_STUDENT_TAG_FULLY = "DWD_APP_STUDENT_TAG_FULLY";//内置数据表学生标签
	public static final String DWD_APP_TEACHER_TAG_FULLY = "DWD_APP_TEACHER_TAG_FULLY";//内置数据表教师标签
	public static final String BG_TAG_CALCULATE_REDIS_KEY = "BG_TAG_CALCULATE_REDIS_KEY";//存放数据库获取的用户ids
	private Logger log = Logger.getLogger(BgCalServiceImpl.class);

	@Autowired
	private WarningResultService warningResultService;

	@Autowired
	private MysqlClientService mysqlClientService;

	@Autowired
	private UserProfileService userProfileService;

	@Autowired
	private UserTagService userTagService;

	@Autowired
	private TagRuleRelationService tagRuleRelationService;

	@Autowired
	private TagRuleSymbolService tagRuleSymbolService;

	@Autowired
	private WarningProjectService warningProjectService;

	@Autowired
	private BaseJdbcService baseJdbcService;

	@Autowired
	private DelayQueueService delayQueueService;
	@Autowired
	private MetadataService metadataService;
	@Autowired
    private MetadataTableColumnService metadataTableColumnService;
	@Autowired
	private HbaseClientService hbaseClientService;
	@Autowired
	private PhoenixClientService phoenixClientService;
	@Autowired
	private EtlJobService etlJobService;

	@Override
	public boolean dealUserTagCal(String profileCode) {
		long start = System.currentTimeMillis();
		List<UserProfile> userProfiles = userProfileService.findListBy("code",
				profileCode);
		UserProfile up = userProfiles.get(0);
		if(up==null||(BgCalStorage.statusMap.containsKey(profileCode)&&BgCalStorage.statusMap.get(profileCode)))return false;
		BgCalStorage.statusMap.put(profileCode, true);
		BgCalStorage bcs = new BgCalStorage();
		try{
			// 获取所有的标签
			List<UserTag> userTagList = userTagService
					.getUserTagByProfileCode(up.getCode());
			
			// 获取所有的符号map
			bcs.tagSymbolMap = tagRuleSymbolService
					.getTagRuleSymbolMap();
			
			// 获取所有的规则map
			bcs.ruleRelationMap = tagRuleRelationService
					.getTagRuleRelationMapByProfileCode(up.getCode());
			//所有的规则列表
			List<TagRuleRelation> tagRuleReList = Lists.newArrayList();
			bcs.ruleRelationMap.values().forEach(e -> tagRuleReList.addAll(e));
			//规则中元数据的列
			Set<String> mdColIds = EntityUtils.getSet(tagRuleReList, TagRuleRelation::getMdColumnId);
			List<MetadataTableColumn> metaCols = metadataTableColumnService.findListByIdIn(mdColIds.toArray(new String[0]));
			bcs.metaColMap = EntityUtils.getMap(metaCols, MetadataTableColumn::getId);
			//所有资产元数据
			List<Metadata> metas = metadataService.findTableNameOrColName(up.getMainTableName(), up.getForeignKey());
			bcs.metaMap  = EntityUtils.getMap(metas, Metadata::getId);
			bcs.tagMap = EntityUtils.getMap(userTagList, UserTag::getId);
			if(!bcs.chekMap()){
				BgCalStorage.statusMap.put(profileCode, false);
				return false;
			}
			final CountDownLatch cdl = new CountDownLatch(BgCalStorage.THREAD_MUNBER);
			int pnumber = 10;
			for(int i=0;i<pnumber;i++){
				BgCalProducer producer = new BgCalProducer(up,cdl,bcs);
				bcs.submit(producer);
			}
			for(int i=0;i<BgCalStorage.THREAD_MUNBER-pnumber;i++){
				BgCalConsumer consumer = new BgCalConsumer(up,cdl,bcs);
				bcs.submit(consumer);
			}
			cdl.await();
			bcs.clearMap();
			long end = System.currentTimeMillis();
			bcs.count.set(0);
			System.out.println("总耗时："+(end-start)+"条数:"+bcs.count.get());
			BgCalStorage.statusMap.put(profileCode, false);
		}catch(Exception e){
			BgCalStorage.statusMap.put(profileCode, false);
		}
		return true;
		
	}
	
	private List<Json> fillJson(String family,Map<String,String> colValMap){
		 List<Json> datas = Lists.newArrayList();
		 for (Map.Entry<String,String> entry : colValMap.entrySet()) {
		    String key = entry.getKey();
		    String value = entry.getValue();
		    Json j = new Json();
		    j.put("family", family);
		    j.put("column", key==null?"":key.toUpperCase());
		    j.put("value", value==null?"":value);
		    datas.add(j);
		 }
         return datas;
	}
	

	@Override
	public boolean dealWarningCal(String projectId) {
		// 根据id获取预警项目
		WarningProject project = warningProjectService.findOne(projectId);

		// 还未到开始时间
		if (DateUtils.compareIgnoreSecond(project.getStartTime(), new Date()) > 0) {
			return false;
		}
		// 不是到永久 並且 过了结束时间
		if (project.getIsAllTime() == 1
				&& DateUtils.compareIgnoreSecond(new Date(),
						project.getEndTime()) > 0) {
			return false;
		}

		//启动ETL调度任务
		if (StringUtils.isNotBlank(project.getJobId())) {
			boolean etlJob = etlJobService.dealEtlQuantzJob(project.getJobId());
			if (!etlJob) {
				log.error("预警项目--" + project.getProjectName() + "运行ETL调度任务失败");
				return false;
			}
		}

		// 获取所有的符号map
		Map<String, TagRuleSymbol> tagSymbolMap = tagRuleSymbolService
				.getTagRuleSymbolMap();

		// 获取所有的规则map
		Map<String, List<TagRuleRelation>> ruleRelationMap = tagRuleRelationService
				.getTagRuleRelationMapByProfileCode("warning");
		//所有的规则列表
		List<TagRuleRelation> tagRuleReList = Lists.newArrayList();
		ruleRelationMap.values().forEach(e -> tagRuleReList.addAll(e));
		//规则中元数据的列
		Set<String> mdColIds = EntityUtils.getSet(tagRuleReList, TagRuleRelation::getMdColumnId);
		List<MetadataTableColumn> metaCols = metadataTableColumnService.findListByIdIn(mdColIds.toArray(new String[0]));
		Map<String,MetadataTableColumn> metaColMap = EntityUtils.getMap(metaCols, MetadataTableColumn::getId);
		//所有资产元数据
		List<Metadata> metas = metadataService.findAllTables();
		Map<String,Metadata> metaMap  = EntityUtils.getMap(metas, Metadata::getId);
		// 获取所有的规则
		List<TagRuleRelation> tagRelationList = tagRuleRelationService
				.getTagRuleRelationByProjectId(projectId);

		String batchId = UuidUtils.generateUuid();

		// 组织SQL
		
		String selectSql = " SELECT @BUSINESS_KEYS FROM @SOURCE_TABLE WHERE @CONDITION ";
		Map<Short, List<TagRuleRelation>> ruleGroupMap = new HashMap<Short, List<TagRuleRelation>>();
		Set<Short> groupIdSet = new HashSet<Short>();

		for (TagRuleRelation tagRelation : tagRelationList) {
			List<TagRuleRelation> tempGroupList = ruleGroupMap.get(tagRelation
					.getGroupId());
			if (CollectionUtils.isEmpty(tempGroupList))
				tempGroupList = new ArrayList<TagRuleRelation>();
			tempGroupList.add(tagRelation);
			ruleGroupMap.put(tagRelation.getGroupId(), tempGroupList);
			groupIdSet.add(tagRelation.getGroupId());
		}

		String condition = " ( ";
		int outterCount = 0;
		
		// 判断规则的表名是否一致 不一致的话 就不能更新
		String sourceTableName = StringUtils.EMPTY;
		String businessKeys = "UNIT_ID,TIPS,RESULT,REMARK,CALLBACK_JSON,BUSINESS_KEY";
		Map<String,Metadata> mdKeyMap = Maps.newHashMap();//目前支持选一个元数据
		Metadata metaData = null;
		for (Short groupId : groupIdSet) {
			List<TagRuleRelation> ruleRelationList = ruleGroupMap.get(groupId);
			String subCondtion = " ( ";
			int innerCount = 0;
			for (TagRuleRelation ruleRelation : ruleRelationList) {
				Metadata meta = metaMap.get(ruleRelation.getMdId());
				
				if (meta == null||StringUtils.isBlank(meta.getTableName())) {
					log.warn("元数据不存在，跳过标签--" + project.getProjectName());
					continue;
				}
				if(mdKeyMap.size()>0 && !mdKeyMap.containsKey(meta.getId())){
					continue;
				}
				metaData = meta;
				mdKeyMap.put(meta.getId(), meta);
				MetadataTableColumn metaCol = metaColMap.get(ruleRelation.getMdColumnId());
				if (metaCol == null||StringUtils.isBlank(metaCol.getColumnName())) {
					log.warn("元数据列不存在，跳过标签--" + project.getProjectName());
					continue;
				}
				TagRuleSymbol symbol = tagSymbolMap.get(ruleRelation
						.getRuleSymbolId());
				if (symbol == null||StringUtils.isBlank(symbol.getSymbolCode())) {
					log.warn("符号不存在，跳过标签--" + project.getProjectName());
					continue;
				}
				sourceTableName = meta.getTableName();

				String tableColumn = metaCol.getColumnName();
				String assembledResult = getAssembledSqlResult(null,
						tableColumn, symbol.getSymbolCode(),
						ruleRelation.getResult());
				if (innerCount == 0)
					subCondtion += assembledResult;
				else
					subCondtion += " OR " + assembledResult;

				innerCount++;
			}

			subCondtion += " ) ";
			if (outterCount == 0)
				condition += subCondtion;
			else
				condition += " AND " + subCondtion;
			outterCount++;
		}
		condition += " ) ";
		if(metaData==null){
			return false;
		}
		selectSql = selectSql.replace("@BUSINESS_KEYS", businessKeys);
		selectSql = selectSql.replace("@SOURCE_TABLE", sourceTableName);
		selectSql = selectSql.replace("@CONDITION", condition);

		List<Json> keysResult = null;
		if (Objects.equals("hbase",metaData.getDbType())) {
			keysResult = phoenixClientService.getDataListFromPhoenix(null, selectSql, null);
		} else if (Objects.equals("mysql",metaData.getDbType())) {
			keysResult = mysqlClientService.getDataListFromMysql(null, null, selectSql, null, null);
		}

		if (Objects.equals(project.getWarnResultType(), WarnResultTypeEnum.TRACE.getCode())) {
			// 获取项目中所有预警中的预警结果
			List<WarningResult> warningList = warningResultService.findByUnitIdAndProjectIdAndStatus(project.getUnitId(),project.getId(), WarnResultStatusEnum.WARNING.getCode());
			List<String> oldWarningKey = warningList.stream().map(warn -> warn.getBusinessKey()).collect(Collectors.toList());
			List<String> nowWarningKey = keysResult.stream().map(json -> json.getString("business_key")).collect(Collectors.toList());
			Set<String> warnings = warningList.stream().filter(warn -> nowWarningKey.contains(warn.getBusinessKey())).map(warn -> warn.getId()).collect(Collectors.toSet());
			Set<String> historyWarn = warningList.stream().filter(warn -> !nowWarningKey.contains(warn.getBusinessKey())).map(warn -> warn.getId()).collect(Collectors.toSet());
			keysResult = keysResult.stream().filter(json -> !oldWarningKey.contains(json.getString("business_key"))).collect(Collectors.toList());

			String warningSql = "update bg_warning_result set warn_date = CURRENT_TIMESTAMP where id in (@WARNINGIDS)";
			String historyWarnSql = "update bg_warning_result set status = 1 where id in (@HISTORYWARNIDS)";
			if (CollectionUtils.isNotEmpty(warnings)) {
				String warningIds = "";
				for (String str : warnings) {
					warningIds += ",'" + str + "'";
				}
				warningSql = warningSql.replace("@WARNINGIDS", warningIds.substring(1));
				try {
					warningResultService.execSql(warningSql, null);
				} catch (Exception e) {
					log.error("设置预警中结果--" + project.getProjectName() + "失败" +e.getMessage());
				}
			}
			if (CollectionUtils.isNotEmpty(historyWarn)) {
				String historyWarnIds = "";
				for (String str : historyWarn) {
					historyWarnIds += ",'" + str + "'";
				}
				historyWarnSql = historyWarnSql.replace("@HISTORYWARNIDS", historyWarnIds.substring(1));
				try {
					warningResultService.execSql(historyWarnSql, null);
				} catch (Exception e) {
					log.error("设置历史预警结果--" + project.getProjectName() + "失败" +e.getMessage());
				}
			}
		}

		for(Json json : keysResult){
			String sql = " INSERT INTO bg_warning_result(id,project_id,batch_id,@BUSINESS_KEYS,warn_date,is_read,warn_result_type,status) VALUES ("
					+ "sys_guid(),'@PROJECT_ID','@BATCH_ID',@BUSINESS_VALUE,CURRENT_TIMESTAMP,@IS_READ,@WARN_RESULT_TYPE,@STATUS )";

			sql = sql.replace("@PROJECT_ID", project.getId());
			sql = sql.replace("@BATCH_ID", batchId);
			sql = sql.replace("@IS_READ", "1");
			sql = sql.replace("@WARN_RESULT_TYPE", String.valueOf(WarnResultStatusEnum.WARNING.getCode()));
			sql = sql.replace("@STATUS", "0");
			StringBuilder b = new StringBuilder();
			b.append("'");
			b.append(json.getString("unit_id"));
			b.append("',");
			b.append("'");
			b.append(json.getString("tips"));
			b.append("',");
			b.append("'");
			b.append(json.getString("result"));
			b.append("',");
			b.append("'");
			b.append(json.getString("remark"));
			b.append("',");
			b.append("'");
			b.append(json.getString("callback_json"));
			b.append("',");
			b.append("'");
			b.append(json.getString("business_key"));
			b.append("'");

			sql = sql.replace("@BUSINESS_VALUE", b.toString());
			sql = sql.replace("@BUSINESS_KEYS", businessKeys);
			// 执行SQL
			try {
				warningResultService.execSql(sql, null);
			} catch (Exception e) {
				log.error("新增预警--" + project.getProjectName() + "失败"
						+ e.getMessage());
			}
		}

		// 更新项目表中的预警次数和时间
		warningProjectService.updateWarningProject(projectId, new Date());

		// 按照单位和项目统计
		String deleteStatSql = "delete from BG_WARNING_RESULT_STAT where project_id ='@PROJECT_ID' ";
		deleteStatSql = deleteStatSql.replace("@PROJECT_ID", projectId);
		String statSql = "insert into BG_WARNING_RESULT_STAT(id,unit_id,PROJECT_ID,stat_result) "
				+ "select sys_guid(), unit_id,project_id,count(1) from BG_WARNING_RESULT where project_id ='@PROJECT_ID' group by unit_id,project_id";
		statSql = statSql.replace("@PROJECT_ID", projectId);

		baseJdbcService.execSql(deleteStatSql, null);
		baseJdbcService.execSql(statSql, null);

		// 推送结果 根据batchId获取预警结果list 组织成json数据发送; 5分钟后发送
		if (StringUtils.isNotBlank(project.getCallbackApis())) {
			try {
				BgCalResultPush push = new BgCalResultPush(
						project.getCallbackApis(), batchId);// 创建一个任务对象
				Date sendDate = DateUtils.addMinute(new Date(), 5);
				DelayItem<?> k = new DelayItem<BgCalResultPush>(
						"bg_cal_result_push", DateUtils.date2String(sendDate,
								"yyyy-MM-dd HH:mm:ss"), push);
				List<DelayItem<?>> itemList = new ArrayList<DelayItem<?>>();
				itemList.add(k);
				delayQueueService.addRecentlyTimeoutItems(itemList);
			} catch (Exception e) {
				log.error("新增预警--" + project.getProjectName() + "推送失败"
						+ e.getMessage());
			}
		}
		return true;
	}

	private String getAssembledSqlResult(String tableAlias, String columnName,
			String symbolCode, String result) {

		if (StringUtils.isNotBlank(tableAlias)) {
			columnName = tableAlias + "." + columnName;
		}
		String assembledResult = StringUtils.EMPTY;
		switch (symbolCode) {
		case "equal":
			if (NumberUtils.isNumber(result))
				assembledResult = columnName + " = " + result;
			else
				assembledResult = columnName + " = '" + result + "' ";
			break;
		case "notEqualTo":
			if (NumberUtils.isNumber(result))
				assembledResult = columnName + " <> " + result;
			else
				assembledResult = columnName + " <> '" + result + "' ";
			break;
		case "gt":
			if (NumberUtils.isNumber(result))
				assembledResult = columnName + " > " + result;
			else
				assembledResult = columnName + " > '" + result + "' ";
			break;
		case "gtet":
			if (NumberUtils.isNumber(result))
				assembledResult = columnName + " >= " + result;
			else
				assembledResult = columnName + " >= '" + result + "' ";
			break;
		case "lt":
			if (NumberUtils.isNumber(result))
				assembledResult = columnName + " < " + result;
			else
				assembledResult = columnName + " < '" + result + "' ";
			break;
		case "ltet":
			if (NumberUtils.isNumber(result))
				assembledResult = columnName + " <= " + result;
			else
				assembledResult = columnName + " <= '" + result + "' ";
			break;
		case "contain":
			if (result.split(";").length > 1) {
				String[] resultArray = result.split(";");
				assembledResult += " ( ";
				for (int i = 0; i < resultArray.length; i++) {
					if (i == 0)
						assembledResult += columnName + " like '%"
								+ resultArray[i] + "%' ";
					else
						assembledResult += " OR " + columnName + " like '%"
								+ resultArray[i] + "%' ";
				}
				assembledResult += " ) ";
			} else {
				assembledResult = columnName + " like '%" + result + "%' ";
			}
			break;
		case "notContain":
			assembledResult = columnName + " not like '%" + result + "%' ";
			if (result.split(";").length > 1) {
				String[] resultArray = result.split(";");
				assembledResult += " ( ";
				for (int i = 0; i < resultArray.length; i++) {
					if (i == 0)
						assembledResult += columnName + " not like '%"
								+ resultArray[i] + "%' ";
					else
						assembledResult += " AND " + columnName
								+ " not like '%" + resultArray[i] + "%' ";
				}
				assembledResult += " ) ";
			} else {
				assembledResult = columnName + " not like '%" + result + "%' ";
			}
			break;
		case "startWith":
			assembledResult = columnName + " like '" + result + "%' ";
			break;
		case "notStartWith":
			assembledResult = columnName + " not like '" + result + "%' ";
			break;
		case "endWith":
			assembledResult = columnName + " like '%" + result + "' ";
			break;
		case "notEndWith":
			assembledResult = columnName + " not like '%" + result + "' ";
			break;
		case "isNull":
			assembledResult = columnName + " is null ";
			break;
		case "isNotNull":
			assembledResult = columnName + " is not null ";
			break;
		}
		return assembledResult;
	}

	@Override
	public boolean dealUserTagCalDetail(UserProfile up,Map<String,Json> jsonMap,BgCalStorage bcs) {
		long start = System.currentTimeMillis();
		Map<String,Set<String>> tagPfMap = Maps.newHashMap();//标签id--角色的主键集合
		Map<String,UserTag> parentTagMap = Maps.newHashMap();//子标签id--父标签
		for (Map.Entry<String,UserTag> tagEn : bcs.tagMap.entrySet()) {
			UserTag userTag = tagEn.getValue();
			if(userTag==null || "00000000000000000000000000000000".equals(userTag.getParentId())){
				continue;
			}
			
			boolean isUpdate = false;
			// 获取更新周期，判断是否需要更新
			String updateCycle = userTag.getUpdateCycle();
			if (StringUtils.isBlank(updateCycle)) {
				log.warn("没有对应的标签规则，跳过标签--" + userTag.getTagName());
				continue;
			}
			switch (updateCycle) {
			case "daily":
				isUpdate = true;
				break;
			case "weekly":
				if (DateUtils.getDayOfWeek(new Date()) == 1) {
					isUpdate = true;
				}
				break;
			case "monthly":
				if (DateUtils.getDayOfMonth(new Date()) == 1) {
					isUpdate = true;
				}
				break;
			case "quarterly":
				if (DateUtils.date2String(new Date(), "MM-dd")
						.endsWith("01-01")
						|| DateUtils.date2String(new Date(), "MM-dd").endsWith(
								"04-01")
								|| DateUtils.date2String(new Date(), "MM-dd").endsWith(
										"07-01")
										|| DateUtils.date2String(new Date(), "MM-dd").endsWith(
												"10-01")) {
					isUpdate = true;
				}
				break;
			case "semiannual":
				if (DateUtils.date2String(new Date(), "MM-dd")
						.endsWith("01-01")
						|| DateUtils.date2String(new Date(), "MM-dd").endsWith(
								"07-01")) {
					isUpdate = true;
				}
				break;
			case "yearly":
				if (DateUtils.getDayOfYear(new Date()) == 1) {
					isUpdate = true;
				}
				break;
			}
			if (!isUpdate) {
				continue;
			}
			Set<String> primaryKeys = Sets.newHashSet();
			for(String key:jsonMap.keySet()){
				primaryKeys.add(key);
			}
			parentTagMap.put(userTag.getId(), bcs.tagMap.get(userTag.getParentId()));
			Set<Short> groupIdSet = new HashSet<>();
			List<TagRuleRelation> tagRelationList = bcs.ruleRelationMap.get(userTag
					.getId());
			if(CollectionUtils.isEmpty(tagRelationList)){
				continue;
			}
			Map<Short, List<TagRuleRelation>> ruleGroupMap = Maps.newConcurrentMap();
			for (TagRuleRelation tagRelation : tagRelationList) {
				List<TagRuleRelation> tempGroupList = ruleGroupMap
						.get(tagRelation.getGroupId());
				if (CollectionUtils.isEmpty(tempGroupList))
					tempGroupList = Lists.newArrayList();
				tempGroupList.add(tagRelation);
				ruleGroupMap.put(tagRelation.getGroupId(), tempGroupList);
				groupIdSet.add(tagRelation.getGroupId());
			}
			int index = 0;
			Set<String> tempKeys = Sets.newHashSet();
			for (Short groupId : groupIdSet) {
				List<TagRuleRelation> ruleRelationList = ruleGroupMap
						.get(groupId);
				if(index>0){
					primaryKeys.clear();
					primaryKeys.addAll(tempKeys);
				}
				int indexIn = 0;
				for (TagRuleRelation ruleRelation : ruleRelationList) {
					int size = primaryKeys.size();
					if(size==0)break;
					if(indexIn==0)tempKeys = Sets.newHashSet();
					Metadata meta = bcs.metaMap.get(ruleRelation.getMdId());
					if (meta == null||StringUtils.isBlank(meta.getTableName())) {
						log.warn("元数据不存在，跳过标签--" + userTag.getTagName());
						continue;
					}
					MetadataTableColumn metaCol = bcs.metaColMap.get(ruleRelation.getMdColumnId());
					if (metaCol == null||StringUtils.isBlank(metaCol.getColumnName())) {
						log.warn("元数据列不存在，跳过标签--" + userTag.getTagName());
						continue;
					}
					TagRuleSymbol symbol = bcs.tagSymbolMap.get(ruleRelation
							.getRuleSymbolId());
					if (symbol == null||StringUtils.isBlank(symbol.getSymbolCode())) {
						log.warn("符号不存在，跳过标签--" + userTag.getTagName());
						continue;
					}
					String condition = getAssembledSqlResult(null,
							metaCol.getColumnName(), symbol.getSymbolCode(),
							ruleRelation.getResult());
					String keyName = "";
					if(meta.getTableName().equalsIgnoreCase(up.getMainTableName())){
						keyName = up.getPrimaryKey();
					}else{
						keyName = up.getForeignKey();
					}
					StringBuilder sql = new StringBuilder("select ");
					sql.append(keyName);
					sql.append(" from ");
					sql.append(meta.getTableName());
					sql.append(" where ");
					sql.append(condition);
					sql.append(" and ");
					sql.append(up.getPrimaryKey());
					sql.append(" in (");
					
					for(String key:primaryKeys){
						size--;
						sql.append("'");
						sql.append(key);
						if(size==0){
							sql.append("'");
						}else{
							sql.append("',");
						}
					}
					sql.append(")");
					List<Json> keysResult = Lists.newArrayList();
					if ("hbase".equals(meta.getDbType())) {
						keysResult = phoenixClientService.getDataListFromPhoenix(null, sql.toString(), null);
					}else if ("mysql".equals(meta.getDbType())) {
						keysResult =  mysqlClientService.getDataListFromMysql(null, null, sql.toString(), null, null);
					}
					Set<String> keys = Sets.newHashSet();
					for(Json j:keysResult){
						keys.add(j.getString(keyName));
					}
					index++;
					indexIn++;
					tempKeys.addAll(keys);
					primaryKeys.removeAll(keys);
				}
			}
			tagPfMap.put(userTag.getId(), tempKeys);
		}
		Map<String, List<Json>> datas = Maps.newConcurrentMap();
		
		for (Map.Entry<String,Json> json : jsonMap.entrySet()) {
			Json j = json.getValue();
			String key = json.getKey();
			Map<String,String> colValMap = Maps.newHashMap();
			for (Map.Entry<String,Set<String>> entry : tagPfMap.entrySet()) {
				String tagId = entry.getKey();
				Set<String> ids = entry.getValue();
				if(ids.contains(key)){
					colValMap.put(parentTagMap.get(tagId).getTargetColumn(), bcs.tagMap.get(tagId).getTagName());
				}
			}
			Map<String,String> infoColMap = Maps.newHashMap();
			
			for(String col:up.getBasicColumns().split(",")){
				infoColMap.put(col, j.getString(col));
			}
			List<Json> result = fillJson("INFO", infoColMap);
			if(colValMap.size()>0){
				 result.addAll(fillJson("TAG", colValMap));
			}
			datas.put(key, result);
		}
		String tableName = "";
		if ("student".equals(up.getCode())) {
			tableName = DWD_APP_STUDENT_TAG_FULLY;
		}else if ("teacher".equals(up.getCode())) {
			tableName = DWD_APP_TEACHER_TAG_FULLY;
		}else{
			return false;
		}
		try {
			long mid = System.currentTimeMillis();
			if(!datas.isEmpty())
			hbaseClientService.putDatas(tableName, datas);
			long end = System.currentTimeMillis();
			log.info("耗时1："+(end-start)+"耗时2："+(end-mid)+"总量deal："+bcs.count.get());
		} catch (IOException e1) {
			log.error("保存计算标签结果是失败--"+tableName + e1.getMessage());
			return false;
		}
		return false;
	}

}
