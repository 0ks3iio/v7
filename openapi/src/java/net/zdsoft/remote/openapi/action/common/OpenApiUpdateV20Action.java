package net.zdsoft.remote.openapi.action.common;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.basedata.constant.BaseSaveConstant;
import net.zdsoft.basedata.service.BaseJdbcService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.JdbcUtils;
import net.zdsoft.remote.openapi.entity.Developer;
import net.zdsoft.remote.openapi.entity.OpenApiEntity;
import net.zdsoft.remote.openapi.entity.OpenApiInterface;
import net.zdsoft.remote.openapi.entity.OpenApiParameter;
import net.zdsoft.remote.openapi.service.BaseCommonJdbcService;
import net.zdsoft.remote.openapi.service.DeveloperService;
import net.zdsoft.remote.openapi.service.OpenApiEntityService;
import net.zdsoft.remote.openapi.service.OpenApiInterfaceService;
import net.zdsoft.remote.openapi.service.OpenApiParameterService;
import net.zdsoft.savedata.action.BaseSyncAction;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping(value = { "/remote/openapi/update", "/openapi/update" })
public class OpenApiUpdateV20Action extends BaseSyncAction{
    
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
    @Lazy
    @Autowired
    BaseCommonJdbcService baseCommonJdbcService;
    
	@ResponseBody
    @RequestMapping(value = "/v2.0/{type}",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ControllerInfo(value = "{type}开发者调用接口：/v2.0/{type}",parameter = "{ticketKey}")
	public String updateDate(@RequestBody String jsonStr,@PathVariable String type,  HttpServletRequest request) {
		String ticketKey = request.getParameter("ticketKey");
		String code = "-1";
		String msg = "保存失败";
		String  sourceType = "1"; //获取数据源的类型
		int count;
        if (StringUtils.isNotBlank(ticketKey)) {
        	Developer developer = developerService.findByTicketKey(ticketKey);
        	if(developer != null){
        		JSONObject jsonObject = JSON.parseObject(jsonStr);
        		count = jsonObject.getInteger("count");
        		jsonStr = jsonObject.getString("data");
        		jsonStr =  getSaveData(jsonStr, ticketKey);  
        		if(StringUtils.isNotBlank(jsonStr)){
        			//封装sql 语句
        			List<Object[]> objList = new ArrayList<Object[]>();
        			List<String> typeIntegers = new ArrayList<String>();
        			StringBuffer sqlBuffer = new StringBuffer();
        			StringBuffer paramBuffer = new StringBuffer();
        			List<String> entityCommontList = new ArrayList<String>();
        			//1 ---根据类型得到 表名 
        			String uri = "/openapi/update/v2.0/"+ type;
        			OpenApiInterface oi = openApiInterfaceService.findByUri(uri);
        			if (oi != null) {
        				sqlBuffer.append("UPDATE ");
        				sqlBuffer.append(oi.getTableName());
        				sourceType = oi.getSourceType();
        			}
        			//得到必传的参数
        			List<OpenApiParameter> parameters = openApiParameterService.findByUri(uri);
        			parameters = EntityUtils.filter2(parameters, t->{
	        		    return t.getMandatory() == 1;
	        	    });
        			//2 ---根据表名 得到 实体类的各个字段 
        			Map<String, List<OpenApiEntity>> entityMap = openApiEntityService.findEntities(developer, type);
        			List<OpenApiEntity> openEntities = entityMap.get(type);
        			Map<String, String> entityNamMap = EntityUtils.getMap(openEntities, OpenApiEntity::getEntityName, OpenApiEntity::getEntityType);
        			Map<String,String>  columnTypeMap  = new HashMap<>(); 
        			if (CollectionUtils.isNotEmpty(openEntities)) {
        				sqlBuffer.append(" set ");
        				for (int i = 0; i < openEntities.size(); i++) {
        					sqlBuffer.append(openEntities.get(i).getEntityColumnName());
        					sqlBuffer.append("= ?");
        					if((openEntities.size()-1) != i){
        						sqlBuffer.append(",");
        					}
        					String entityName = openEntities.get(i).getEntityName();
        					String entityType = openEntities.get(i).getEntityType();
        					entityCommontList.add(entityName);
        					entityType = getEntityType(entityType);
        					typeIntegers.add(entityType);
        					columnTypeMap.put(entityName, entityType);
        				}
        				sqlBuffer.append(" where ");
        				if(CollectionUtils.isNotEmpty(parameters)){
        					for (int i = 0; i < parameters.size(); i++) {
        						OpenApiParameter c = parameters.get(i);
        						String paraName = c.getParamName();
        						String columnName = c.getParamColumnName();
        						if(StringUtils.startsWith(columnName, "_")){
        							columnName = columnName.replaceFirst("_", "");
        						}
        						sqlBuffer.append(columnName);
        						sqlBuffer.append("= ?");
        						if((parameters.size()-1) != i){
            						sqlBuffer.append(",");
            					}
        						String entityType = entityNamMap.get(paraName);
        						entityType = getEntityType(entityType);
            					entityCommontList.add(paraName);
            					typeIntegers.add(entityType);
            					columnTypeMap.put(paraName, entityType);
							}
        				}
        			}
        			sqlBuffer.append(paramBuffer.toString().replaceFirst(",", ""));
        			//----------得到对应的数值
        			JSONArray jsonArray =  JSONArray.parseArray(jsonStr);
        			if(CollectionUtils.isNotEmpty(jsonArray)) {
        				jsonArray = getLimitArray(jsonArray);
        				for (int i = 0; i < jsonArray.size(); i++) {
        					JSONObject js = jsonArray.getJSONObject(i);
        					if (CollectionUtils.isNotEmpty(entityCommontList)) {
        						List<Object> valueList = new ArrayList<Object>();
        						entityCommontList.forEach(c->{
        							String columnType = columnTypeMap.get(c);
        							if(columnType.equalsIgnoreCase("TIMESTAMP") || columnType.equalsIgnoreCase("DATE")){
        								valueList.add(js.getDate(c));
        							}else if(columnType.equalsIgnoreCase("VARCHAR") || columnType.equalsIgnoreCase("CHAR") || columnType.equalsIgnoreCase("VARCHAR2") ){
        								valueList.add(js.getString(c));
        							}else if(columnType.equalsIgnoreCase("INTEGER") || columnType.equalsIgnoreCase("SMALLINT") ){
        								valueList.add(js.getInteger(c));
        							}else if (columnType.equalsIgnoreCase("NUMBER")){
        								valueList.add(js.getDouble(c));
        							}else {
        								valueList.add(js.getString(c));
        							}
        						});
        						objList.add(valueList.toArray(new Object[valueList.size()]));
        					}
        				}
        			} 
        			//---------得到对应的类型
        			int[] argTypes = null;
        			List<Integer> typeList = new ArrayList<>();
        			if(CollectionUtils.isNotEmpty(typeIntegers)){
        				typeIntegers.forEach(c->{
        					switch (c) {
        					case "VARCHAR":
        						typeList.add(Types.VARCHAR);
        						break;
        					case "VARCHAR2":
        						typeList.add(Types.VARCHAR);
        						break;
        					case "CHAR":
        						typeList.add(Types.CHAR);
        						break;
        					case "DATE":
        						typeList.add(Types.DATE);
        						break;
        					case "TIMESTAMP":
        						typeList.add(Types.TIMESTAMP);
        						break;
        					case "INTEGER":
        						typeList.add(Types.INTEGER);
        						break;
        					case "SMALLINT":
        						typeList.add(Types.SMALLINT);
        						break;
        					case "NUMBER":
        						typeList.add(Types.SMALLINT);
        						break;
        					default:
        						break;
        					}
        				});
        			}
        			if(CollectionUtils.isNotEmpty(typeList)){
        				argTypes = new int[typeList.size()];
        				for(int i = 0;i< argTypes.length;i++){
        					argTypes[i]= typeList.get(i);
        				}
        			}
        			//3 ---直接用 该对象进行解析，还是 封装 sql 语句，进行保存
        			String sql = sqlBuffer.toString();
        			int[] result = null;
					try {
						if("2".equals(sourceType)){
							System.out.println(JdbcUtils.getSQL(sql, objList.get(0)));
							result = baseCommonJdbcService.updateList(sql,objList,argTypes);
						}else{
							result = baseJdbcService.updateList(sql,objList,argTypes);
						}
					} catch (Exception e) {
						log.error(msg, e);
						return returnMsg(code, msg);
					}
					if(count == result.length){
						code = "1";
						msg = "保存成功";
					}
        		 }
        	}
        }
        return returnMsg(code, msg);
    }

	// -----------------------------------私有方法区 -----------------------------
	/**
	 * 得到去掉“（” 号之后的字符
	 * @param entityType
	 * @return
	 */
	private String getEntityType(String entityType) {
		if(entityType.contains("(")){
			entityType = entityType.substring(0, StringUtils.indexOf(entityType, "("));
			entityType = entityType.toUpperCase();
		}
		return entityType;
	}
	/**
	 * 进行截取数据
	 */
	private JSONArray getLimitArray(JSONArray jsonArray) {
		if(jsonArray.size() > BaseSaveConstant.DEFAULT_MAX_PAGE_SIZE) {
			jsonArray = (JSONArray) jsonArray.subList(0, BaseSaveConstant.DEFAULT_MAX_PAGE_SIZE);
		}
		return jsonArray;
	}
	
}
