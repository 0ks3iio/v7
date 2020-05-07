package net.zdsoft.api.examine.inte.action;

import com.alibaba.fastjson.JSONObject;
import net.zdsoft.api.base.dto.OpenInterfaceDto;
import net.zdsoft.api.base.entity.eis.ApiInterface;
import net.zdsoft.api.base.enums.InterfaceDateTypeEnum;
import net.zdsoft.api.base.service.ApiInterfaceService;
import net.zdsoft.api.base.service.ApiInterfaceTypeService;
import net.zdsoft.api.base.service.ApiParameterService;
import net.zdsoft.bigdata.data.dto.LogDto;
import net.zdsoft.bigdata.data.service.BigLogService;
import net.zdsoft.bigdata.data.service.DatabaseService;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.metadata.entity.Metadata;
import net.zdsoft.bigdata.metadata.entity.MetadataRelation;
import net.zdsoft.bigdata.metadata.enums.MetadataRelationTypeEnum;
import net.zdsoft.bigdata.metadata.service.MetadataRelationService;
import net.zdsoft.bigdata.metadata.service.MetadataService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping(value = "/bigdata/api/inteManager")
public class InterManageAction extends InterBaseAction{

	@Autowired
	private ApiInterfaceService apiInterfaceService;
	@Autowired
    private ApiParameterService apiParameterService;
	@Autowired
	private ApiInterfaceTypeService apiInterfaceTypeService;
	@Autowired
    private DatabaseService databaseService;
	@Autowired
	private MetadataService metadataService;
	@Autowired
	private MetadataRelationService metadataRelationService;
	@Resource
	private BigLogService bigLogService;
	
	@RequestMapping("/index")
    public String interfaceManageIndex(ModelMap map) {
        return "/api/examine/interfaceManage/inteTab.ftl";
    }
	
	@RequestMapping("/showIndex")
    public String showIndexManageIndex(ModelMap map) {
        return "/api/examine/interfaceManage/interface/intefaceIndex.ftl";
    }
	/**
     * 接口管理
     */
    @RequestMapping("/showInterfaces")
    public String showInterfaces(HttpServletRequest request,String typeName, Integer isUsing, Integer dataType, ModelMap map) {
        List<ApiInterface> interfaces = apiInterfaceService.getAllInterfaces(typeName, isUsing, dataType, null);
        ArrayList<OpenInterfaceDto> openInterfaceDtos = new ArrayList<>();
    	if(CollectionUtils.isNotEmpty(interfaces)){
    		 for (ApiInterface openApiInterface : interfaces) {
    			 OpenInterfaceDto openInterfaceDto = new OpenInterfaceDto();
    			 openInterfaceDto.setId(openApiInterface.getId());
    			 openInterfaceDto.setDescription(openApiInterface.getDescription());
    			 String dataTypeName = InterfaceDateTypeEnum.get(openApiInterface.getDataType()).getDescription();
    			 openInterfaceDto.setDataTypeName(dataTypeName);
    			 openInterfaceDto.setMethodType(openApiInterface.getMethodType());
    			 openInterfaceDto.setUrlString(openApiInterface.getUri());
    			 openInterfaceDto.setState(openApiInterface.getIsUsing() == 1 ? "启用" : "停用");
    			 openInterfaceDto.setIsUsing(openApiInterface.getIsUsing());
    			 openInterfaceDto.setInterfaceName(openApiInterface.getTypeName());
    			 openInterfaceDto.setType(openApiInterface.getType());
    			 openInterfaceDto.setResultType(openApiInterface.getResultType());
    			 openInterfaceDtos.add(openInterfaceDto);
    		 }
    	}
    	map.put("openInterfaceDtos", openInterfaceDtos);
        return "/api/examine/interfaceManage/interface/intefaceList.ftl";
    }

    /**
     * 删除接口
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/delInterface")
    @ControllerInfo("删除接口")
    public Response delInterface(String interfaceId) {
    	try {
    		if(isNotExistOpenApiApply(interfaceId)){
				ApiInterface apiInterface = apiInterfaceService.findOne(interfaceId);
				apiInterfaceService.delete(interfaceId);
				//删除关联关系表
        		metadataRelationService.deleteBySourceIdAndTargetId(apiInterface.getMetadataId(),apiInterface.getId());
				//业务日志埋点  删除
				LogDto logDto=new LogDto();
				logDto.setBizCode("delete-Interface");
				logDto.setDescription("接口 "+apiInterface.getUri());
				logDto.setBizName("数据接口管理");
				logDto.setOldData(apiInterface);
				bigLogService.deleteLog(logDto);

    		}else{
    			return Response.error().message("接口正在使用，不能删除！").build();
    		}
    		//删除关联数据
    		apiParameterService.deleteByInterfaceId(interfaceId);
		} catch (Exception e) {
			return Response.error().message(e.getMessage()).build();
		}
    	 return Response.ok().build();
    }
   
    /**
     * 接口启用和不启用
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/isUsingInterface")
    @ControllerInfo("启用和停用接口")
    public Response isUsingInterface(String interfaceId, int isUsing) {
    	try {
    		//先判断停用的接口是否正在审核或使用
    		if(isNotExistOpenApiApply(interfaceId) || isUsing == ApiInterface.TRUE_IS_USING){
    			apiInterfaceService.updateInterfaceById(isUsing,interfaceId);
    		}else{
    			return Response.error().message("接口正在使用，不能停用！").build();
    		}
		} catch (Exception e) {
			return Response.error().message(e.getMessage()).build();
		}
    	return Response.ok().build();
    }
    
    /**
     * 修改接口
     * @param interfaceId
     * @return
     */
    @RequestMapping("/editInterface")
    @ControllerInfo("修改接口")
    public String editInterface(String interfaceId,ModelMap map) {
    	if(StringUtils.isNotBlank(interfaceId)){
    		ApiInterface openApiInterface = apiInterfaceService.findOne(interfaceId);
    		map.put("interface", openApiInterface);
    	}
    	map.put("interTypeList", getInterfaceTypes());
    	map.put("resultTypeList", getResultTypes());
		return "/api/examine/interfaceManage/interface/intefaceEdit.ftl";
    }
    
    /**
     * 保存参数
     * @param body
     * @return
     */
    @ResponseBody
    @RequestMapping("/saveInterface")
    @ControllerInfo("超管保存接口")
    public Response saveInterface(@RequestBody String body) {
    	try {
    		JSONObject jsonObject = SUtils.dc(body, JSONObject.class);
    		ApiInterface openApiInterface;
    		String interfaceId = jsonObject.getString("interfaceId");
			ApiInterface apiInterfaceCopy = new ApiInterface();
			boolean flag=true;
    		if(StringUtils.isBlank(interfaceId)){
        		openApiInterface =new ApiInterface();

        	}else{
        		openApiInterface = apiInterfaceService.findOne(interfaceId);
        		EntityUtils.copyProperties(openApiInterface,apiInterfaceCopy);
				flag=false;
        	}
        	if(checkUri(jsonObject.getString("uri"),openApiInterface.getUri())){
        		ApiInterface apiInterface = getApiInterface(openApiInterface,jsonObject);
        		apiInterfaceService.save(apiInterface);
        		//保存绑定关系
        		MetadataRelation daMetadataRelation = new MetadataRelation();
        		daMetadataRelation.setSourceId(apiInterface.getMetadataId());
        		daMetadataRelation.setTargetId(apiInterface.getId());
        		daMetadataRelation.setSourceType(MetadataRelationTypeEnum.TABLE.getCode());
        		daMetadataRelation.setTargetType(MetadataRelationTypeEnum.API.getCode());
        		metadataRelationService.saveMetadataRelation(daMetadataRelation);
        		if (flag){
					//业务日志埋点  新增
					LogDto logDto=new LogDto();
					logDto.setBizCode("insert-interface");
					logDto.setDescription("接口 "+apiInterface.getUri());
					logDto.setNewData(apiInterface);
					logDto.setBizName("数据接口管理");
					bigLogService.insertLog(logDto);
				}else {
					//业务日志埋点  修改
					LogDto logDto=new LogDto();
					logDto.setBizCode("update-interface");
					logDto.setDescription("接口 "+apiInterfaceCopy.getUri());
					logDto.setOldData(apiInterfaceCopy);
					logDto.setNewData(apiInterface);
					logDto.setBizName("数据接口管理");
					bigLogService.updateLog(logDto);
				}
        	}else{
        		return Response.error().message("url地址已经存在！").build();
        	}
		} catch (Exception e) {
			return Response.error().message(e.getMessage()).build();
		}
        return  Response.ok().build();
    }
    
	//    ---------------------------------------------------------私有的方法区 -----------------------------------------------------
    private ApiInterface getApiInterface(ApiInterface openApiInterface,JSONObject jsonObject){
//    	ApiDeveloper developer = getDeveloper();
//    	openApiInterface.setTicketKey(developer.getTicketKey());
    	String interfaceId = jsonObject.getString("interfaceId");
    	if(StringUtils.isBlank(interfaceId)){
    		interfaceId = UuidUtils.generateUuid();
    		openApiInterface.setCreationTime(new Date());
    	}
    	openApiInterface.setModifyTime(new Date());
    	openApiInterface.setId(interfaceId);
    	openApiInterface.setDataType(Integer.valueOf(jsonObject.getString("dataType")));
    	String isUsing = jsonObject.getString("isUsing");
    	if(StringUtils.isBlank(isUsing)){
    		isUsing = "1";
    	}
    	openApiInterface.setIsUsing(Integer.valueOf(isUsing));
    	openApiInterface.setMethodType(jsonObject.getString("methodType"));
    	openApiInterface.setResultType(jsonObject.getString("resultType"));
    	String[] paramString  = jsonObject.getString("type").split(",");
    	openApiInterface.setType(paramString[0]);
    	openApiInterface.setTypeName(paramString[1]);
    	openApiInterface.setUri(jsonObject.getString("uri"));
    	openApiInterface.setDescription(jsonObject.getString("description"));
    	openApiInterface.setFpkColumnName(jsonObject.getString("fpkColumnName"));
    	openApiInterface.setMetadataId(jsonObject.getString("metadataId"));
    	Metadata table = metadataService.findOne(jsonObject.getString("metadataId"));
    	openApiInterface.setTableName(table.getTableName());
    	return  openApiInterface;
    }
    
    /**
     * 验证地址是否唯一
     * @param string
     */
    private boolean checkUri(String url,String oldUrl) {
    	if(StringUtils.isNotBlank(oldUrl) && url.equals(oldUrl)){
    		return Boolean.TRUE;
    	}
    	return (apiInterfaceService.findByUri(url) == null);
	}
}
