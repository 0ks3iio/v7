package net.zdsoft.api.examine.inte.action;

import java.util.ArrayList;
import java.util.List;

import net.zdsoft.api.base.dto.OpenParamDto;
import net.zdsoft.api.base.entity.eis.ApiInterface;
import net.zdsoft.api.base.entity.eis.ApiParameter;
import net.zdsoft.api.base.service.ApiInterfaceService;
import net.zdsoft.api.base.service.ApiParameterService;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;


@Controller
@RequestMapping(value = "/bigdata/api/interParam")
public class InterParamAction extends InterBaseAction{

	@Autowired
	private ApiParameterService openApiParameterService;
	@Autowired
	private ApiInterfaceService openApiInterfaceService;
	
	
	@RequestMapping("/showIndex")
    @ControllerInfo("进入页面")
	public String showIndex(ModelMap map,String interfaceId){
		map.put("interfaceId", interfaceId);
    	return "/api/examine/interfaceManage/param/paramIndex.ftl";
	}
	
	/**
     * 查找参数
     * @param uri interfaceId
     * @return
     */
    @RequestMapping("/findParam")
    @ControllerInfo("查找参数")
    public String findParam(ModelMap map,String interfaceId) {
    	ApiInterface openApiInterface = openApiInterfaceService.findOne(interfaceId);
		List<ApiParameter> openApiParameters = openApiParameterService.findByInterfaceId(interfaceId);
		List<OpenParamDto> openParamDtos = new ArrayList<>();
		for (ApiParameter openApiParameter : openApiParameters) {
			OpenParamDto dto = new OpenParamDto();
			dto.setId(openApiParameter.getId());
			dto.setInterfaceName(openApiInterface.getTypeName());
			dto.setUri(openApiInterface.getUri());
			dto.setParamName(openApiParameter.getParamName());
			dto.setParamColumnName(openApiParameter.getParamColumnName());
			String description = openApiParameter.getDescription();
//			if(description.length() > 15){
//				description = StringUtils.substring(description, 0, 15) + "...";
//			}
			dto.setDescription(description);
			dto.setMandatory(openApiParameter.getMandatory());
			dto.setMcodeId(openApiParameter.getMcodeId());
			dto.setType(openApiInterface.getType());
			openParamDtos.add(dto);
		}
		map.put("openParamDtos", openParamDtos);
		return "/api/examine/interfaceManage/param/paramList.ftl";
    }
	
    /**
     * 修改参数
     * @param paramId
     * @return
     */
    @RequestMapping("/editParam")
    @ControllerInfo("修改参数")
    public String editParam(String paramId, String interfaceId,ModelMap map) {
    	if(StringUtils.isNotBlank(paramId) && !"undefined".equalsIgnoreCase(paramId)){
    		ApiParameter openApiParameter = openApiParameterService.findOne(paramId);
    		map.put("param", openApiParameter);
    		interfaceId = openApiParameter.getInterfaceId();
    	}
    	ApiInterface openApiInterface = openApiInterfaceService.findOne(interfaceId);
    	if(StringUtils.isNotBlank(interfaceId)){
    		map.put("uri", openApiInterface.getUri());
    	}
    	map.put("interfaceId",interfaceId);
		return "/api/examine/interfaceManage/param/paramEdit.ftl";
    }
	/**
     * 删除参数 必填的不可删除
     * @param paramId
     * @return
     */
    @ResponseBody
    @RequestMapping("/delParam")
    @ControllerInfo("超管删除参数")
    public Response delParam(String paramId) {
    	try {
    		ApiParameter openApiParameter = openApiParameterService.findOne(paramId);
    		if(openApiParameter.getMandatory() == 0){
    			openApiParameterService.delete(paramId);
    		}else{
    			return Response.error().message("参数是必填，不能删除！").build();
    		}
		} catch (Exception e) {
			return Response.error().message(e.getMessage()).build();
		}
    	 return Response.ok().build();
    }
    
    /**
     * 保存参数
     * @param body
     * @return
     */
    @ResponseBody
    @RequestMapping("/saveParam")
    @ControllerInfo("超管保存参数")
    public Response saveParam(@RequestBody String body) {
    	try {
    		JSONObject jsonObject = SUtils.dc(body, JSONObject.class);
    		openApiParameterService.save(getApiParameter(jsonObject));
		} catch (Exception e) {
			return Response.error().message(e.getMessage()).build();
		}
    	 return Response.ok().build();
    }
    
    private ApiParameter getApiParameter(JSONObject jsonObject){
    	ApiParameter openApiParameter =new ApiParameter();
    	String paramId = jsonObject.getString("paramId");
    	if(StringUtils.isBlank(paramId)){
    		paramId = UuidUtils.generateUuid();
    	}
    	openApiParameter.setId(paramId);
    	openApiParameter.setParamName(jsonObject.getString("paramName"));
    	openApiParameter.setParamColumnName(jsonObject.getString("paramColumnName"));
    	openApiParameter.setDescription(jsonObject.getString("description"));
    	String mandatory = jsonObject.getString("mandatory");
    	if(StringUtils.isBlank(mandatory)){
    		mandatory = "0";
    	}
    	openApiParameter.setMandatory(Integer.valueOf(mandatory));
    	openApiParameter.setMcodeId(jsonObject.getString("mcodeId"));
    	openApiParameter.setInterfaceId(jsonObject.getString("interfaceId"));
    	return  openApiParameter;
    }
    
    
}
