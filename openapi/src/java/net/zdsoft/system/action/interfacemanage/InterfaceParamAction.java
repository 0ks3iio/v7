package net.zdsoft.system.action.interfacemanage;

import java.util.ArrayList;
import java.util.List;

import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.remote.openapi.entity.OpenApiInterface;
import net.zdsoft.remote.openapi.entity.OpenApiParameter;
import net.zdsoft.remote.openapi.remote.service.OpenApiInterfaceRemoteService;
import net.zdsoft.remote.openapi.remote.service.OpenApiParameterRemoteService;
import net.zdsoft.system.dto.interfaceManage.OpenParamDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;


@Controller
@RequestMapping(value = "/system/interface/param")
public class InterfaceParamAction extends InterfaceBaseAction{

	@Autowired
	private OpenApiParameterRemoteService openApiParameterRemoteService;
	@Autowired
	private OpenApiInterfaceRemoteService openApiInterfaceRemoteService;
	
	
	@RequestMapping("/showIndex")
    @ControllerInfo("进入页面")
	public String showIndex(ModelMap map,String interfaceId){
		map.put("interfaceId", interfaceId);
		
    	return "/openapi/system/interfaceManage/param/paramIndex.ftl";
	}
	
	/**
     * 查找参数
     * @param uri interfaceId
     * @return
     */
    @RequestMapping("/findParam")
    @ControllerInfo("查找参数")
    public String findParam(ModelMap map,String interfaceId) {
    	OpenApiInterface openApiInterface = SUtils.dc(openApiInterfaceRemoteService.findOneById(interfaceId), OpenApiInterface.class);
		List<OpenApiParameter> openApiParameters = SUtils.dt(openApiParameterRemoteService.findByUri(openApiInterface.getUri()), OpenApiParameter.class);
		List<OpenParamDto> openParamDtos = new ArrayList<>();
		for (OpenApiParameter openApiParameter : openApiParameters) {
			OpenParamDto dto = new OpenParamDto();
			dto.setId(openApiParameter.getId());
			dto.setInterfaceName(openApiInterface.getTypeName());
			dto.setUri(openApiInterface.getUri());
			dto.setParamName(openApiParameter.getParamName());
			dto.setParamColumnName(openApiParameter.getParamColumnName());
			dto.setDescription(openApiParameter.getDescription());
			dto.setMandatory(openApiParameter.getMandatory());
			dto.setMcodeId(openApiParameter.getMcodeId());
			dto.setType(openApiInterface.getType());
			openParamDtos.add(dto);
		}
		map.put("openParamDtos", openParamDtos);
		return "/openapi/system/interfaceManage/param/paramList.ftl";
    }
	
    /**
     * 修改参数
     * @param paramId
     * @return
     */
    @RequestMapping("/editParam")
    @ControllerInfo("查找参数")
    public String editParam(String paramId, String interfaceId,ModelMap map) {
    	OpenApiInterface openApiInterface = SUtils.dc(openApiInterfaceRemoteService.findOneById(interfaceId), OpenApiInterface.class);
    	if(StringUtils.isNotBlank(paramId)){
    		OpenApiParameter openApiParameter = SUtils.dc(openApiParameterRemoteService.findOneById(paramId), OpenApiParameter.class);
    		map.put("param", openApiParameter);
    		map.put("uri", openApiParameter.getUri());
    	}
    	if(StringUtils.isNotBlank(interfaceId)){
    		map.put("uri", openApiInterface.getUri());
    	}
		return "/openapi/system/interfaceManage/param/paramEdit.ftl";
    }
	/**
     * 删除参数 必填的不可删除
     * @param paramId
     * @return
     */
    @ResponseBody
    @RequestMapping("/delParam")
    @ControllerInfo("超管删除参数")
    public String delParam(String paramId) {
    	try {
    		OpenApiParameter openApiParameter = SUtils.dc(openApiParameterRemoteService.findOneById(paramId), OpenApiParameter.class);
    		if(openApiParameter.getMandatory() == 0){
    			openApiParameterRemoteService.deleteById(paramId);
    		}else{
    			return error("参数是必填，不能删除！");
    		}
		} catch (Exception e) {
			return error("删除参数失败！"+e.getMessage());
		}
        return success("删除参数成功");
    }
    
    /**
     * 保存参数
     * @param body
     * @return
     */
    @ResponseBody
    @RequestMapping("/saveParam")
    @ControllerInfo("超管保存参数")
    public String saveParam(@RequestBody String body) {
    	try {
    		JSONObject jsonObject = SUtils.dc(body, JSONObject.class);
    		openApiParameterRemoteService.save(getApiParameter(jsonObject));
		} catch (Exception e) {
			return error("保存参数失败！"+e.getMessage());
		}
        return success("保存参数成功");
    }
    
    private OpenApiParameter getApiParameter(JSONObject jsonObject){
    	OpenApiParameter openApiParameter =new OpenApiParameter();
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
    	openApiParameter.setUri(jsonObject.getString("uri"));
    	return  openApiParameter;
    }
    
    
}
