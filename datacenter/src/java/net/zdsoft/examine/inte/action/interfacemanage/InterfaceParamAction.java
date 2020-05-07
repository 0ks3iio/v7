package net.zdsoft.examine.inte.action.interfacemanage;

import java.util.ArrayList;
import java.util.List;

import net.zdsoft.base.dto.OpenParamDto;
import net.zdsoft.base.entity.eis.OpenApiInterface;
import net.zdsoft.base.entity.eis.OpenApiParameter;
import net.zdsoft.base.service.OpenApiInterfaceService;
import net.zdsoft.base.service.OpenApiParameterService;
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
@RequestMapping(value = "/datacenter/examine/interface/param")
public class InterfaceParamAction extends InterfaceBaseAction{

	@Autowired
	private OpenApiParameterService openApiParameterService;
	@Autowired
	private OpenApiInterfaceService openApiInterfaceService;
	
	
	@RequestMapping("/showIndex")
    @ControllerInfo("进入页面")
	public String showIndex(ModelMap map,String interfaceId){
		map.put("interfaceId", interfaceId);
		
    	return "/examine/interfaceManage/param/paramIndex.ftl";
	}
	
	/**
     * 查找参数
     * @param uri interfaceId
     * @return
     */
    @RequestMapping("/findParam")
    @ControllerInfo("查找参数")
    public String findParam(ModelMap map,String interfaceId) {
    	OpenApiInterface openApiInterface = openApiInterfaceService.findOne(interfaceId);
		List<OpenApiParameter> openApiParameters = openApiParameterService.findByInterfaceId(interfaceId);
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
		return "/examine/interfaceManage/param/paramList.ftl";
    }
	
    /**
     * 修改参数
     * @param paramId
     * @return
     */
    @RequestMapping("/editParam")
    @ControllerInfo("查找参数")
    public String editParam(String paramId, String interfaceId,ModelMap map) {
    	if(StringUtils.isNotBlank(paramId)){
    		OpenApiParameter openApiParameter = openApiParameterService.findOne(paramId);
    		map.put("param", openApiParameter);
    		interfaceId = openApiParameter.getInterfaceId();
    	}
    	OpenApiInterface openApiInterface = openApiInterfaceService.findOne(interfaceId);
    	if(StringUtils.isNotBlank(interfaceId)){
    		map.put("uri", openApiInterface.getUri());
    	}
    	map.put("interfaceId",interfaceId);
		return "/examine/interfaceManage/param/paramEdit.ftl";
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
    		OpenApiParameter openApiParameter = openApiParameterService.findOne(paramId);
    		if(openApiParameter.getMandatory() == 0){
    			openApiParameterService.delete(paramId);
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
    		openApiParameterService.save(getApiParameter(jsonObject));
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
    	openApiParameter.setInterfaceId(jsonObject.getString("interfaceId"));
    	return  openApiParameter;
    }
    
    
}
