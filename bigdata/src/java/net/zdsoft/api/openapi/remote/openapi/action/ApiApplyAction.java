package net.zdsoft.api.openapi.remote.openapi.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


import net.zdsoft.api.base.dto.ApplyApiDto;
import net.zdsoft.api.base.dto.EntityDto;
import net.zdsoft.api.base.entity.eis.ApiApply;
import net.zdsoft.api.base.entity.eis.ApiDeveloper;
import net.zdsoft.api.base.entity.eis.ApiEntity;
import net.zdsoft.api.base.entity.eis.ApiEntityTicket;
import net.zdsoft.api.base.entity.eis.ApiInterface;
import net.zdsoft.api.base.entity.eis.OpenApiApp;
import net.zdsoft.api.base.enums.ApplyStatusEnum;
import net.zdsoft.api.base.enums.YesNoEnum;
import net.zdsoft.api.base.service.ApiApplyService;
import net.zdsoft.api.base.service.ApiInterfaceService;
import net.zdsoft.api.base.service.OpenApiAppService;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.UuidUtils;


import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.hadoop.hive.ql.parse.HiveParser.ifExists_return;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


/**
 * 申请接口类
 * @author yangsj
 *
 */
@Controller
@RequestMapping("/bigdata/api/apply")
public class ApiApplyAction extends ApiBaseAction{

	  @Autowired
	  private ApiInterfaceService apiInterfaceService;
	  @Autowired
	  private OpenApiAppService openApiAppService;
	  /**
	  * 申请接口
	  * @param dataType
	  * @return
	  */
	 @RequestMapping("/list")
	 public String applyInterface(String dataType, ModelMap map) {
	 	ApiDeveloper developer = getDeveloper();
	 	List<ApiInterface> openApiInterfaces = openApiInterfaceService.findByDataType(Integer.valueOf(dataType));
	 	if (null != developer) {
	    	  int[] status = {ApplyStatusEnum.PASS_VERIFY.getValue(),ApplyStatusEnum.IN_VERIFY.getValue()};
	  		  List<ApiApply> apiApplies = openApiApplyService.findByTicketKeyAndStatusIn(developer.getTicketKey(),status);
	  		  if(CollectionUtils.isNotEmpty(apiApplies)){
	  			 Map<String, ApiApply> interidMap = EntityUtils.getMap(apiApplies, ApiApply::getInterfaceId);
	  			 openApiInterfaces = EntityUtils.filter2(openApiInterfaces, t->{
	 				return interidMap.get(t.getId()) == null;
			     });
	  		  }
	  		  Map<String, String> typeNameMap = getAllTypeNameMap();
	  		  openApiInterfaces.forEach(c->{
	  			  c.setResultTypeName(typeNameMap.get(c.getResultType()));
	  		  });
	 	}
	 	Map<String, List<ApiInterface>> resultTypeMap = EntityUtils.getListMap(openApiInterfaces, 
				  ApiInterface::getResultType, Function.identity());
	 	//得到开发者注册的接口申请应用
	 	List<OpenApiApp> apiApps = openApiAppService.getAppsByDeveloperId(developer.getId()).stream()
                .filter(e->e.getAppTypes().contains(OpenApiApp.APP_APPLYAPI_NORMAL))
                .collect(Collectors.toList());
	 	if(apiApps.size() == 1 )
	 		map.put("isMandatory", Boolean.TRUE);
	 	map.put("apiApps", apiApps);
	 	map.put("applyInterfaces", resultTypeMap);
	 	map.put("dataType", dataType);
	 	return "/api/openapi/interfaceApi/apply/applyList.ftl";
	 }
 
	 @ResponseBody
	 @RequestMapping("/showEntityList")
	 public String showEntityList(ModelMap map,String type) {
			List<ApiEntity> openApiEntities = openApiEntityService.findByType(type);
			JSONArray jsonArray = new JSONArray();
			for(ApiEntity entity:openApiEntities){
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("id",entity.getId());
				jsonObject.put("type", entity.getType());
				jsonObject.put("entityName", entity.getEntityName());
				jsonObject.put("displayName", entity.getDisplayName());
				jsonObject.put("mandatory", entity.getMandatory() == YesNoEnum.YES.getValue() ? YesNoEnum.YES.getNameValue() 
						: YesNoEnum.NO.getNameValue());
				jsonArray.add(jsonObject);
			}
			return jsonArray.toJSONString();
	 }
	 
	   /**
	   * 接口申请
	   * @param entityIds
	   * @return
	   */
	  @ResponseBody
	  @RequestMapping("/save")
	  public Response saveApplyInterface(String passInterfaceDtos) {
		try {
			ApiDeveloper developer = getDeveloper();
			passInterfaceDtos = StringEscapeUtils.unescapeHtml(passInterfaceDtos);
			List<ApplyApiDto> allInterfaceDtos = JSON.parseArray(passInterfaceDtos, ApplyApiDto.class);
			Set<String> interfaceIds = EntityUtils.getSet(allInterfaceDtos, ApplyApiDto::getInterfaceId);
			List<ApiInterface> allInterfaces = apiInterfaceService.findListByIdIn(interfaceIds.toArray(new String[interfaceIds.size()]));
			
			//得到未通过的apply
			List<ApiApply> apiApplies = openApiApplyService.findByTicketKeyAndStatus(developer.getTicketKey(), ApplyStatusEnum.UNPASS_VERIFY.getValue());
			Map<String, ApiApply> interfaceidMap = EntityUtils.getMap(apiApplies, ApiApply::getInterfaceId);
			
			Map<String, String> typeMap = EntityUtils.getMap(allInterfaces, ApiInterface::getId, ApiInterface::getResultType);
			List<ApiApply> saveApiApplies = new ArrayList<ApiApply>();
			List<ApiEntityTicket> saveEntityTickets = new ArrayList<ApiEntityTicket>();
			
			allInterfaceDtos.forEach(c->{
				String interfaceId = c.getInterfaceId();
				if(MapUtils.isNotEmpty(interfaceidMap) && interfaceidMap.get(interfaceId) != null){
					openApiApplyService.deleteApply(interfaceidMap.get(interfaceId).getId());
				}
				ApiApply apply = new ApiApply();
				apply.setId(UuidUtils.generateUuid());
				apply.setTicketKey(developer.getTicketKey());
				apply.setCreationTime(new Date());
				apply.setModifyTime(new Date());
				apply.setStatus(ApplyStatusEnum.IN_VERIFY.getValue());
		        apply.setType(typeMap.get(interfaceId));
				apply.setMaxNumDay(ApiApply.DEFAULT_MAX_NUM_EVERYDAY);
				apply.setLimitEveryTime(ApiApply.DEFAULT_LIMIT_EVERY_TIME);
				apply.setInterfaceId(interfaceId);
				apply.setIsLimit(ApiApply.IS_LIMIT_TRUE);
				apply.setAppIds(c.getAppIds());
				saveApiApplies.add(apply);
				List<EntityDto> entityDtos = c.getEntityDtos();
				entityDtos.forEach(t->{
					ApiEntityTicket et = new ApiEntityTicket();
					et.setId(UuidUtils.generateUuid());
			        et.setType(typeMap.get(interfaceId));
					et.setInterfaceId(interfaceId);
					et.setTicketKey(developer.getTicketKey());
					et.setEntityId(t.getEntityId());
					saveEntityTickets.add(et);
				});
			});
			if(CollectionUtils.isNotEmpty(saveApiApplies)){
			  	openApiApplyService.saveAllAndEntityTicket(saveApiApplies,saveEntityTickets);
			}
		} catch (Exception e) {
			e.printStackTrace();
//			return Response.error().message(e.getMessage()).build();
		}
    	return Response.ok().build();
	}
}
