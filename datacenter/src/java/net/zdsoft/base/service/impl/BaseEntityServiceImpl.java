package net.zdsoft.base.service.impl;

import static net.zdsoft.base.utils.CheckUtil.check;
import static net.zdsoft.base.utils.CheckUtil.checkMaxLen;
import static net.zdsoft.base.utils.CheckUtil.notEmpty;
import static net.zdsoft.base.utils.CheckUtil.notNull;
import static net.zdsoft.base.utils.CheckUtil.permissionCheck;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.base.constant.BaseDataCenterConstant;
import net.zdsoft.base.constant.TipsConstant;
import net.zdsoft.base.entity.base.BaseRelation;
import net.zdsoft.base.entity.eis.Developer;
import net.zdsoft.base.entity.eis.OpenApiEntity;
import net.zdsoft.base.entity.eis.OpenApiInterface;
import net.zdsoft.base.service.BaseEntityService;
import net.zdsoft.base.service.BaseRelationService;
import net.zdsoft.base.service.DeveloperService;
import net.zdsoft.base.service.OpenApiEntityService;
import net.zdsoft.base.service.OpenApiInterfaceService;
import net.zdsoft.base.service.OpenApiParameterService;
import net.zdsoft.basedata.service.BaseJdbcService;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.openapi.savedata.constant.BaseSaveConstant;
import net.zdsoft.openapi.savedata.utils.JsonParserUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * 处理模型接口的业务
 * 
 * @author chenlw
 *
 */
@Service("baseEntityService")
public class BaseEntityServiceImpl implements BaseEntityService {
	@Autowired
	OpenApiParameterService openApiParameterService;
	@Autowired
	OpenApiEntityService openApiEntityService;
	@Autowired
	OpenApiInterfaceService openApiInterfaceService;
	@Autowired
	DeveloperService developerService;
	@Autowired
	BaseJdbcService baseJdbcService;
	@Autowired
	BaseRelationService baseRelationService;

	@Override
	public void dealModelSave(String ticketKey, String jsonStr, String type,int dealType) {
		notEmpty(ticketKey, TipsConstant.PARAM_IS_NULL, BaseDataCenterConstant.TICKET_KEY);
		notEmpty(jsonStr, TipsConstant.PARAM_IS_NULL, "数据");
		notEmpty(type,TipsConstant.PARAM_IS_NULL, "模型");
		Developer developer = developerService.findByTicketKey(ticketKey);
		permissionCheck(developer);
		String lockKey = BaseDataCenterConstant.DATA_IN_REDIS_LOCK_PREFIX+developer.getApKey()+ticketKey+type;
		check(RedisUtils.hasLocked(lockKey), TipsConstant.INTERFACE_RUNING_NOW);//同一模型保存时串行，避免并行造成存取关联关系异常
		try{
			boolean isSave = (dealType ==1||dealType==0);
			boolean isUpdate = (dealType ==2||dealType==0);
			JSONObject jsonObject = JSON.parseObject(jsonStr);
			jsonStr = jsonObject.getString("data");
			jsonStr = JsonParserUtils.getSaveData(jsonStr, ticketKey);
			notEmpty(jsonStr, TipsConstant.PARAM_IS_NULL, "解压缩的数据");
			JSONArray jsonArray = JSONArray.parseArray(jsonStr);
			notEmpty(jsonStr, TipsConstant.PARAM_IS_NULL, "解析的数据");
			StringBuilder insertSql = new StringBuilder();
			StringBuilder updateSql = new StringBuilder();
			// 1 ---根据类型得到 表名 、
			String uri = BaseDataCenterConstant.URI_PREFIX_SAVE_UPDATE + type;
			if(dealType==1){
				uri = BaseDataCenterConstant.URI_PREFIX_SAVE + type;
			}else if(dealType==2){
				uri = BaseDataCenterConstant.URI_PREFIX_UPDATE + type;
			}
			OpenApiInterface oi = openApiInterfaceService.findByUri(uri);
			permissionCheck(oi);
			String tableName = oi.getTableName();
			// 2 ---根据表名 得到 实体类的各个字段
			Map<String, List<OpenApiEntity>> entityMap = openApiEntityService
					.findEntities(developer,oi.getId(), oi.getType());
			List<OpenApiEntity> openEntities = entityMap.get(type);
			notEmpty(openEntities, TipsConstant.NOT_HAVE_ENTITY, "");
			Map<String, OpenApiEntity> entityNameMap = Maps.newLinkedHashMap();
			String[] param = dealEntity(openEntities, entityNameMap, tableName,
					insertSql, updateSql);
			List<String> typeIntegers = Lists.newLinkedList();
			List<Object[]> insertObjList = new ArrayList<>();
			List<Object[]> updateObjList = new ArrayList<>();
			Set<String> deldcIds = Sets.newHashSet();
			Integer unitClass = 0;
			boolean isMix = false;
			if(StringUtils.isNotBlank(param[1])){
				JSONArray jsonArray0 = new JSONArray(); 
				JSONArray jsonArray1 = new JSONArray(); 
				splitJsonArr(jsonArray,jsonArray0,jsonArray1,param[1]);
				if(CollectionUtils.isEmpty(jsonArray0)){
					jsonArray = jsonArray1;
					unitClass = 1;
				}else if(CollectionUtils.isEmpty(jsonArray1)){
					jsonArray = jsonArray0;
				}else{
					isMix = true;
					Map<String,Map<String,String>> relationMaps0 = dealRelation(jsonArray, param[0], ticketKey, developer.getApKey(), entityNameMap,unitClass,dealType);
					Map<String,String> saveRelaMap = dealValues(jsonArray0, param[0], entityNameMap, typeIntegers,relationMaps0,insertObjList,updateObjList,deldcIds, ticketKey, developer.getApKey());
					unitClass = 1;
					Map<String,Map<String,String>> relationMaps1 = dealRelation(jsonArray, param[0], ticketKey, developer.getApKey(), entityNameMap,unitClass,dealType);
					saveRelaMap.putAll(dealValues(jsonArray1, param[0], entityNameMap, typeIntegers,relationMaps1,insertObjList,updateObjList,deldcIds, ticketKey, developer.getApKey()));
					if (isSave){
						saveRelation(saveRelaMap,ticketKey,developer.getApKey(),type,deldcIds);
					}
					if (isUpdate){
						delRelation(deldcIds,tableName,param[0]);
					}
				}
			}
			
			// 3.----得到对应的数值
			if(!isMix){
				Map<String,Map<String,String>> relationMaps = dealRelation(jsonArray, param[0], ticketKey, developer.getApKey(), entityNameMap,unitClass,dealType);
				Map<String,String> saveRelaMap = dealValues(jsonArray, param[0], entityNameMap, typeIntegers,relationMaps,insertObjList,updateObjList,deldcIds, ticketKey, developer.getApKey());
				if (isSave){
					saveRelation(saveRelaMap,ticketKey,developer.getApKey(),type,deldcIds);
				}
				if (isUpdate){
					delRelation(deldcIds,tableName,param[0]);
				}
			}
			int[] argTypes = getTypes(typeIntegers);
	
			// 4保存数据
			if (isSave && CollectionUtils.isNotEmpty(insertObjList)){
				baseJdbcService.updateList(insertSql.toString(), insertObjList, argTypes);
			}
			if (isUpdate && CollectionUtils.isNotEmpty(updateObjList)){
				baseJdbcService.updateList(updateSql.toString(), updateObjList, argTypes);
			}
		}finally{
			RedisUtils.unLock(lockKey);
		}

	}
	
	private void splitJsonArr(JSONArray jsonArray, JSONArray jsonArray0,
			JSONArray jsonArray1, String key) {
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject js = jsonArray.getJSONObject(i);
			Integer unitClass = js.getInteger(key);
			notNull(unitClass, TipsConstant.PARAM_IS_NULL, key);
			check(unitClass==0||unitClass==1, TipsConstant.PARAM_USE_VALUE, key,"0或1");
			if(unitClass == 1){
				jsonArray1.add(js);
			}else{
				jsonArray0.add(js);
			}
		}
		
	}

	/**
	 * 删除关联关系
	 * @param delBuIds
	 * @param ticketKey
	 * @param apKey
	 * @param model
	 */
	private void delRelation(Set<String> dcIds,String tableName,String mainKeyName) {
		if(CollectionUtils.isNotEmpty(dcIds)){
			baseRelationService.deleteByDcIds(dcIds.toArray(new String[dcIds.size()]));
			if(CollectionUtils.isNotEmpty(dcIds)){
				String sql = "update "+ tableName+" set is_deleted = 1 where "+mainKeyName+" = ?";
				int[] argTypes = {Types.CHAR};
				List<Object[]> updateObjList = Lists.newArrayList();
				for(String dcId:dcIds){
					Object[] objs = {dcId};
					updateObjList.add(objs);
				}
				baseJdbcService.updateList(sql, updateObjList, argTypes);
			}
		}
	}

	/**
	 * 存储关联关系
	 * @param saveRelaMap
	 * @param ticketKey
	 * @param sourceAp
	 * @param model
	 */
	private void saveRelation(Map<String, String> saveRelaMap,String ticketKey,String sourceAp,String model,Set<String> delBuIds) {
		List<BaseRelation> saveRes = Lists.newArrayList(); 
		for (Map.Entry<String, String> entry : saveRelaMap.entrySet()){
			BaseRelation re = new BaseRelation();
			re.setBusinessId(entry.getValue());
			re.setCreationTime(new Date());
			re.setDcId(entry.getKey());
			re.setId(UuidUtils.generateUuid());
			if(delBuIds.contains(entry.getKey())){
				re.setIsDeleted(1);
			}else{
				re.setIsDeleted(0);
			}
			re.setModel(model);
			re.setModifyTime(new Date());
			re.setSourceAp(sourceAp);
			re.setTicketKey(ticketKey);
			saveRes.add(re);
		}
		if(CollectionUtils.isNotEmpty(saveRes)){
			baseRelationService.savelists(saveRes);
		}	
		
	}
	/**
	 * 关联字段获取
	 * @param jsonArray
	 * @param mainKeyName
	 * @param ticketKey
	 * @param sourceAp
	 * @param model
	 * @return
	 */
	private Map<String,Map<String,String>> dealRelation(JSONArray jsonArray, String mainKey,String ticketKey,String sourceAp,Map<String, OpenApiEntity> entityNameMap,Integer unitClass,int dealType){
		Map<String,Map<String,String>> reMap = Maps.newHashMap();
		Map<String,String> map = Maps.newHashMap();
		for (Map.Entry<String, OpenApiEntity> entry : entityNameMap
				.entrySet()) {
			String key = entry.getKey();
			OpenApiEntity entity = entry.getValue();
			String col = entity.getRelationColumn();
			if(StringUtils.isNotBlank(col)){
				String[] cols = col.split("\\.");
				if(cols.length==2 && unitClass==1){
					map.put(key, cols[1]);
				}else{
					map.put(key, cols[0]);
				}
			}
			
		}
		
		Map<String,Set<String>> busMap = Maps.newHashMap();
		for (String key : map.keySet()) {
			Set<String> businessIds = Sets.newHashSet();
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject js = jsonArray.getJSONObject(i);
				String businessId = js.getString(key);
				if(key.equals(mainKey)){
					notEmpty(businessId, TipsConstant.PARAM_IS_NULL, key);
					check(!businessIds.contains(businessId), TipsConstant.REPEAT_MAIN_KEY, businessId);//主键重复
				}
				businessIds.add(businessId);
			}
			busMap.put(key, businessIds);
		}
		relationQuerry(reMap,map,busMap,ticketKey,sourceAp,unitClass,dealType,mainKey);
		
		return reMap;
	}
	
	private void relationQuerry(Map<String, Map<String, String>> reMap,Map<String, String> map,
			Map<String, Set<String>> busMap,String ticketKey,String sourceAp,int unitClass,int dealType,String mainKey) {
		for (Map.Entry<String, Set<String>> entry : busMap.entrySet()) {
			String key = entry.getKey();
			String model = map.get(key);
			Set<String> businessIds = entry.getValue();
			List<BaseRelation> relations = baseRelationService.findListByRelationParm(businessIds.toArray(new String[businessIds.size()]), ticketKey, sourceAp, model,unitClass);
			Set<String> reBusinessIds = EntityUtils.getSet(relations, BaseRelation::getBusinessId);
			if(key.equals(mainKey)){
				if(dealType==1&&CollectionUtils.isNotEmpty(relations)){
					check(false, TipsConstant.REPEAT_MAIN_KEY, StringUtils.join(reBusinessIds.toArray(), "、"));//主键重复
				}
				if(dealType==2){
					businessIds.removeAll(reBusinessIds);
					check(CollectionUtils.isEmpty(businessIds), TipsConstant.PARAM_NOT_MATCH, key,StringUtils.join(businessIds.toArray(), "、"));//主键重复
				}
			}
			Map<String,String> relationMap = EntityUtils.getMap(relations, BaseRelation::getBusinessId,BaseRelation::getDcId);
			reMap.put(key, relationMap);
		}
		
	}

	/**
	 * 处理调用方传来的数据
	 * @param jsonArray
	 * @param mainKeyName
	 * @param entityNameMap
	 * @param typeIntegers
	 * @param relationMap
	 * @param insertObjList
	 * @param updateObjList
	 * @return
	 */
	private Map<String,String> dealValues(JSONArray jsonArray, String mainKeyName,
			Map<String, OpenApiEntity> entityNameMap, List<String> typeIntegers,
			Map<String,Map<String,String>> relationMap,List<Object[]> insertObjList,
			List<Object[]> updateObjList,Set<String> deldcIds,String ticketKey,String apKey) {
		
		Map<String,String> saveRelaMap = Maps.newHashMap();
		Map<String,String> mainKeyMap = relationMap.get(mainKeyName);
		int index = 0;
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject js = jsonArray.getJSONObject(i);
			List<Object> valueList = new LinkedList<>();
			valueList.add(ticketKey);
			valueList.add(apKey);
			String businessId = js.getString(mainKeyName);	
			String dcId ="";
			if(mainKeyMap.containsKey(businessId)){
				js.put(mainKeyName, mainKeyMap.get(businessId));
			}else{
				dcId =  UuidUtils.generateUuid();
				js.put(mainKeyName, dcId);
				saveRelaMap.put(dcId, businessId);
			}
			if(index==0){
				typeIntegers.clear();
				typeIntegers.add("CHAR");
				typeIntegers.add("CHAR");
			}
			boolean isDelete = false;//删除不做数据校验，另外处理
			for (Map.Entry<String, OpenApiEntity> entry : entityNameMap
					.entrySet()) {
				String key = entry.getKey();
				OpenApiEntity entity = entry.getValue();
				String columnType = entity.getEntityType();
				boolean  mandatory = entity.getMandatory()==1;
				int maxLen = entity.getEntityLength();
				String col = entity.getRelationColumn();
				boolean isDel = entity.getColumnProp() !=null && entity.getColumnProp() == 2;
				if(isDel){
					Integer del =  js.getInteger(key);
					if(del==null){
						del = 0;
						js.put(key, del);
					}
					if(del == 1){
						deldcIds.add(dcId);
						isDelete = true;
						saveRelaMap.remove(dcId);
						break ;
					}
				}
				if(!key.equals(mainKeyName) && StringUtils.isNotBlank(col)){
					if(relationMap.containsKey(key)){
						Map<String,String> keyMap = relationMap.get(key);
						String colId = js.getString(key);
						if(mandatory){
							notEmpty(colId,TipsConstant.PARAM_IS_NULL, key);
							boolean isParent = entity.getColumnProp() !=null && entity.getColumnProp() == 4;
							if(isParent){
								notEmpty(colId,TipsConstant.PARAM_IS_NULL, key);
								if("00000000000000000000000000000000".equals(colId)){
									keyMap.put(colId, "00000000000000000000000000000000");
								}
							}
							notEmpty(keyMap.get(colId),TipsConstant.PARAM_NOT_MATCH, key,colId);
						}
						js.put(key, keyMap.get(colId));
					}else{
						js.put(key, "");
						if(mandatory)notEmpty("",TipsConstant.PARAM_IS_NULL, key);
					}
				}
				if (index == 0) {
					typeIntegers.add(columnType);
				}
				dealValuesDetail(js,key,columnType,mandatory,maxLen,valueList);
			}
			if(isDelete)continue;
			index++;
			if(mainKeyMap.containsKey(businessId)){
				updateObjList.add(valueList.toArray(new Object[valueList.size()]));
			}else{
				insertObjList.add(valueList.toArray(new Object[valueList.size()]));
			}
		}
		return saveRelaMap;
	}

	/**
	 * 逐条数据处理
	 * @param js
	 * @param key
	 * @param columnType
	 * @param mandatory
	 * @param maxLen
	 * @param valueList
	 */
	private void dealValuesDetail(JSONObject js,String key, String columnType,
			boolean mandatory, int maxLen, List<Object> valueList) {
		// 校验数据类型，数据长度，数据不为空
		if (columnType.equalsIgnoreCase("TIMESTAMP")
				|| columnType.equalsIgnoreCase("DATE")) {
			Date d = js.getDate(key);
			if(mandatory) notNull(d, TipsConstant.PARAM_IS_NULL, key);
			valueList.add(d);
		} else if (columnType.equalsIgnoreCase("TINYINT")
				|| columnType.equalsIgnoreCase("SMALLINT")
				|| columnType.equalsIgnoreCase("INT")
				|| columnType.equalsIgnoreCase("INTEGER")) {
			Integer num = js.getInteger(key);
			if(mandatory) notNull(num, TipsConstant.PARAM_IS_NULL, key);
			if(maxLen>0 && num != null)checkMaxLen(String.valueOf(num), TipsConstant.MAX_LENGTH_ERROR, maxLen, key,maxLen);
			valueList.add(num);
		} else if (columnType.equalsIgnoreCase("FLOAT")) {
			Double num = js.getDouble(key);
			if(mandatory) notNull(num, TipsConstant.PARAM_IS_NULL, key);
			if(maxLen>0 && num != null)checkMaxLen(String.valueOf(num), TipsConstant.MAX_LENGTH_ERROR, maxLen, key,maxLen);
			valueList.add(num);
		} else {
			String str = js.getString(key);
			if(mandatory) notEmpty(str, TipsConstant.PARAM_IS_NULL, key);
			if(maxLen>0)checkMaxLen(str, TipsConstant.MAX_LENGTH_ERROR, maxLen, key,maxLen);
			valueList.add(str);
		}
		
	}

	private int[] getTypes(List<String> typeIntegers) {
		List<Integer> typeList = Lists.newLinkedList();
		typeIntegers.forEach(c -> {
			switch (c) {
			case "VARCHAR":
			case "varchar":
				typeList.add(Types.VARCHAR);
				break;
			case "CHAR":
			case "char":
				typeList.add(Types.CHAR);
				break;
			case "DATE":
			case "date":
				typeList.add(Types.DATE);
				break;
			case "TIMESTAMP":
			case "timestamp":
				typeList.add(Types.TIMESTAMP);
				break;
			case "INTEGER":
			case "INT":
			case "integer":
			case "int":
				typeList.add(Types.INTEGER);
				break;
			case "SMALLINT":
			case "smallint":
				typeList.add(Types.SMALLINT);
				break;
			case "TINYINT":
			case "tinyint":
				typeList.add(Types.TINYINT);
				break;
			case "FLOAT":
			case "float":
				typeList.add(Types.FLOAT);
				break;
			default:
				break;
			}
		});
		int[] argTypes = new int[typeList.size()];
		for (int i = 0; i < argTypes.length; i++) {
			argTypes[i] = typeList.get(i);
		}
		return argTypes;
	}

	/**
	 * 根据字段处理语法
	 * @param openEntities
	 * @param entityNameMap
	 * @param tableName
	 * @param insertSql
	 * @param updateSql
	 * @return
	 */
	private String[] dealEntity(List<OpenApiEntity> openEntities,
			Map<String, OpenApiEntity> entityNameMap, String tableName,
			StringBuilder insertSql, StringBuilder updateSql) {
		StringBuilder paramBuffer = new StringBuilder();
		String[] param = new String[2];
		insertSql.append("insert into ");
		insertSql.append(tableName);
		updateSql.append("update ");
		updateSql.append(tableName);
		int size = openEntities.size();
		int index = 0;
		insertSql.append(" (ticket_key,source_ap,");
		updateSql.append(" set ticket_key = ?,source_ap = ?,");
		paramBuffer.append(",?,?");
		OpenApiEntity mianCon = null;
		for (OpenApiEntity entity : openEntities) {
			String entityName = entity.getEntityName();
			String colName = entity.getEntityColumnName();
			if (entity.getColumnProp() == 1) {
				param[0] = entityName;
				mianCon = entity;
				continue;
			}
			if (entity.getColumnProp() == 3) {
				param[1] = entityName;
			}
			if(entity.getIsDefault()==1){
				paramBuffer.append(",IFNULL(?,DEFAULT("+colName+"))");
			}else{
				paramBuffer.append(",?");
			}
			
			index++;
			insertSql.append(entity.getEntityColumnName());
			insertSql.append(",");
			
			updateSql.append(entity.getEntityColumnName());
			if(entity.getIsDefault()==1){
				updateSql.append("= IFNULL(?,DEFAULT("+colName+"))");
			}else{
				updateSql.append("= ?");
			}
			if (size-1 != index) {
				updateSql.append(",");
			}
			
			entityNameMap.put(entityName, entity);
		}
		// 主键字段放到最后
		if (mianCon != null)
			entityNameMap.put(param[0], mianCon);
		insertSql.append(param[0]);
		paramBuffer.append(",?");
		insertSql.append(")");
		insertSql.append(" values ");
		insertSql.append("(");
		insertSql.append(paramBuffer.toString().replaceFirst(",", ""));
		insertSql.append(")");
		updateSql.append(" where ");
		updateSql.append(param[0]);
		updateSql.append("= ?");
		return param;
	}

	/**
	 * 进行截取数据
	 */
	private JSONArray getLimitArray(JSONArray jsonArray) {
		if (jsonArray.size() > BaseSaveConstant.DEFAULT_MAX_PAGE_SIZE) {
			jsonArray = (JSONArray) jsonArray.subList(0,
					BaseSaveConstant.DEFAULT_MAX_PAGE_SIZE);
		}
		return jsonArray;
	}

}
