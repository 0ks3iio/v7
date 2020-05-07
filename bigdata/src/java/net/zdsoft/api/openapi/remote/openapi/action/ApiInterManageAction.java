package net.zdsoft.api.openapi.remote.openapi.action;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import net.zdsoft.api.base.dto.InterfaceDto;
import net.zdsoft.api.base.entity.eis.ApiApply;
import net.zdsoft.api.base.entity.eis.ApiDeveloper;
import net.zdsoft.api.base.entity.eis.ApiEntity;
import net.zdsoft.api.base.entity.eis.ApiEntityTicket;
import net.zdsoft.api.base.entity.eis.ApiInterface;
import net.zdsoft.api.base.entity.eis.ApiParameter;
import net.zdsoft.api.base.entity.eis.OpenApiApp;
import net.zdsoft.api.base.enums.ApplyStatusEnum;
import net.zdsoft.api.base.service.ApiApplyService;
import net.zdsoft.api.base.service.ApiDeveloperService;
import net.zdsoft.api.base.service.ApiEntityService;
import net.zdsoft.api.base.service.ApiEntityTicketService;
import net.zdsoft.api.base.service.ApiInterfaceTypeService;
import net.zdsoft.api.base.service.ApiParameterService;
import net.zdsoft.api.base.service.OpenApiAppService;
import net.zdsoft.framework.utils.EntityUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.dubbo.common.utils.CollectionUtils;


@Controller
@RequestMapping("/bigdata/api/interface")
public class ApiInterManageAction extends ApiBaseAction {
	
	   @Autowired
	   private ApiApplyService apiApplyService;
	   @Autowired
	   private ApiInterfaceTypeService apiInterfaceTypeService;
	   @Autowired
	   private ApiParameterService apiParameterService;
	   @Autowired
	   private ApiEntityTicketService apiEntityTicketService;
	   @Autowired
	   private ApiEntityService apiEntityService;
	   @Resource
	   private ApiDeveloperService apiDeveloperService;
	   @Autowired
	   private OpenApiAppService openApiAppService;
	   
	   @RequestMapping("/index")
	   public String interfaceManageIndex(ModelMap map) {
	        return "/api/openapi/interfaceApi/index.ftl";
	   }
    
	   /**
	   * 查询接口
	   * @param type
	   * @param dataType
	   * @return
	   */
	  @RequestMapping("/{dataType}/list")
	  public String queryTypeList(@PathVariable String dataType,ModelMap map) {
		ApiDeveloper developer = getDeveloper();
		//判断开发者是否注册了接口申请 应用
		List<OpenApiApp> apiApps = openApiAppService.getAppsByDeveloperId(developer.getId()).stream()
                .filter(e->e.getAppTypes().contains(OpenApiApp.APP_APPLYAPI_NORMAL))
                .collect(Collectors.toList());
		if(CollectionUtils.isEmpty(apiApps) )
	 		map.put("isApply", Boolean.FALSE);
		
		Map<String, String> typeNameMap = getAllTypeNameMap();
		List<ApiInterface> interfaces = openApiInterfaceService.findByDataType(Integer.valueOf(dataType));
		Map<String, ApiInterface> idMap = EntityUtils.getMap(interfaces, ApiInterface::getId);
		Map<String, String> allTypeName = new HashMap<>();
		interfaces.forEach(c->{
			allTypeName.put(c.getResultType(), typeNameMap.get(c.getResultType()));
		});
		if(developer != null) {
			String ticketKey = developer.getTicketKey();
			int[] status = {ApplyStatusEnum.PASS_VERIFY.getValue(),ApplyStatusEnum.IN_VERIFY.getValue(), ApplyStatusEnum.UNPASS_VERIFY.getValue()};
			List<ApiApply> applyList = apiApplyService.findByTicketKeyAndStatusIn(ticketKey, status);
			List<ApiApply> endList = new ArrayList<ApiApply>();
			applyList.forEach(c->{
				if(idMap.get(c.getInterfaceId()) != null){
					c.setTypeNameValue(typeNameMap.get(c.getType()));
					endList.add(c);
				}
			});
			Map<Integer, List<ApiApply>> typeMap = EntityUtils.getListMap(endList, ApiApply::getStatus, Function.identity());
			for (Integer key : typeMap.keySet()) {
				String name = "passType";
				if(ApplyStatusEnum.IN_VERIFY.getValue() == key){
					name = "verify";
				}else if (ApplyStatusEnum.UNPASS_VERIFY.getValue() == key) {
					name = "unpassType";
				}
				map.put(name, EntityUtils.getMap(typeMap.get(key), ApiApply::getType, ApiApply::getTypeNameValue));
		    }
		}
		map.put("allType", allTypeName);
		map.put("dataType", dataType);
		return "/api/openapi/interfaceApi/typeList.ftl";
	  }

	  
	  @RequestMapping("/apiList")
	  public String queryInterfaceList(String type,String dataType,int status, ModelMap map) {
		List<ApiInterface> interfaces = null;
		List<InterfaceDto> interfaceDtos = new ArrayList<>();
		ApiDeveloper developer = getDeveloper();
		Map<String, List<ApiEntity>> entityMap = new HashMap<>();
		if(status != ApplyStatusEnum.ALL.getValue() && developer != null) {
			String ticketKey = developer.getTicketKey();
			List<ApiApply> allApiApplies = apiApplyService.findByTicketKeyAndStatusAndType(ticketKey,status,type);
			if(CollectionUtils.isNotEmpty(allApiApplies)){
				Set<String> applyInterSet = EntityUtils.getSet(allApiApplies, ApiApply::getInterfaceId);
				interfaces = openApiInterfaceService.findListByIds(applyInterSet.toArray(new String[applyInterSet.size()]));
				interfaces = EntityUtils.filter2(interfaces, t->{
					return t.getDataType() == Integer.valueOf(dataType);
				});
				List<ApiEntityTicket> entityTickets = apiEntityTicketService.fingByTicketKeyAndInterfaceIdIn(ticketKey,applyInterSet.toArray(new String[applyInterSet.size()]));
				if(CollectionUtils.isNotEmpty(entityTickets)){
					Set<String> entityIds = EntityUtils.getSet(entityTickets, ApiEntityTicket::getEntityId);
					List<ApiEntity> apiEntities = apiEntityService.findListByIdIn(entityIds.toArray(new String[entityIds.size()]));
					Map<String, ApiEntity>  entityIdMap = EntityUtils.getMap(apiEntities, ApiEntity::getId);
					Map<String, List<String>> interfaceIdMap = EntityUtils.getListMap(entityTickets, ApiEntityTicket::getInterfaceId, ApiEntityTicket::getEntityId);
					for (Map.Entry<String, List<String>> entry : interfaceIdMap.entrySet()) {
						List<ApiEntity> entities = new ArrayList<ApiEntity>();
						List<String> entityIdList = entry.getValue();
						entityIdList.forEach(c->{
							entities.add(entityIdMap.get(c));
						});
						entityMap.put(entry.getKey(), entities);
					}
				}
			}
		}else{
			interfaces = openApiInterfaceService.findByResultTypeAndDataType(type, Integer.valueOf(dataType));
			Set<String> metadataIds = EntityUtils.getSet(interfaces, ApiInterface::getMetadataId);
			List<ApiEntity> apiEntities = apiEntityService.findByMetadataIdIn(metadataIds.toArray(new String[metadataIds.size()]));
			entityMap = EntityUtils.getListMap(apiEntities, ApiEntity::getMetadataId, Function.identity());
		}
		if(CollectionUtils.isNotEmpty(interfaces)){
			Set<String> interfaceIds = EntityUtils.getSet(interfaces, ApiInterface::getId);
			List<ApiParameter> parameters = apiParameterService.findByInterfaceIdIn(interfaceIds.toArray(new String[interfaceIds.size()]));
			Map<String, List<ApiParameter>> interMap = EntityUtils.getListMap(parameters, ApiParameter::getInterfaceId, Function.identity());
			for (ApiInterface c : interfaces) {
				InterfaceDto dto = new InterfaceDto();
				c.setMethodType(c.getMethodType().toUpperCase());
				dto.setApiInterface(c);
				dto.setParameters(interMap.get(c.getId()));
				if(ApplyStatusEnum.ALL.getValue() == status){
					dto.setEntitys(entityMap.get(c.getMetadataId()));
				}else{
					dto.setEntitys(entityMap.get(c.getId()));
					dto.setTicketKey(developer.getTicketKey());
				}
				boolean isDoTest = (String.valueOf(ApiInterface.PUSH_DATE_TYPE).equals(dataType) ||  String.valueOf(ApiInterface.UPDATE_DATE_TYPE).equals(dataType));
				dto.setShow((ApplyStatusEnum.PASS_VERIFY.getValue() == status && !isDoTest)  ?Boolean.TRUE:Boolean.FALSE);
				interfaceDtos.add(dto);
			}
		}
		map.put("interfaceDtos", interfaceDtos);
		return "/api/openapi/interfaceApi/apiList.ftl";
	  }
}
