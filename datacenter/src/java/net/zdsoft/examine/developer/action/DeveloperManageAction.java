package net.zdsoft.examine.developer.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import net.zdsoft.base.dto.DeveloperDto;
import net.zdsoft.base.entity.eis.Developer;
import net.zdsoft.base.entity.eis.OpenApiApply;
import net.zdsoft.base.entity.eis.OpenApiInterfaceType;
import net.zdsoft.base.enums.ApplyStatusEnum;
import net.zdsoft.base.enums.YesNoEnum;
import net.zdsoft.base.service.DeveloperService;
import net.zdsoft.base.service.OpenApiApplyService;
import net.zdsoft.base.service.OpenApiInterfaceCountService;
import net.zdsoft.base.service.OpenApiInterfaceTypeService;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.openapi.remote.openapi.action.OpenApiBaseAction;
import net.zdsoft.openapi.remote.openapi.constant.OpenApiConstants;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;

@Controller
@RequestMapping(value = "/datacenter/examine/developer")
public class DeveloperManageAction extends OpenApiBaseAction {
    @Autowired
    @Lazy
    private DeveloperService developerService;
    @Autowired
	private OpenApiInterfaceCountService openApiInterfaceCountService;
    @Autowired
    private OpenApiApplyService openApiApplyService;
    @Autowired
    private OpenApiInterfaceTypeService openApiInterfaceTypeService;
    
    @RequestMapping("/index")
    public String developerManageIndex(ModelMap map) {
        String url = "/datacenter/examine/developer/manage";
        map.put("url", url);
        return "/examine/developer/common/home.ftl";
    }
    
    /**
     * 开发者管理
     * 
     * @author chicb
     * @param map
     * @return
     */
    @RequestMapping("/manage")
    public String shwoManage(ModelMap map) {
    	Developer developer = getDeveloper();
        List<DeveloperDto> developerDtos =  getDevelperInfos(developer);
        map.put("developerDtos", developerDtos);
        map.put("isPower", Developer.IS_COMMON_TYPE != developer.getUserType());
        return "/examine/developer/developerManage.ftl";
    }
    
    
    /**
     * 开发者详细信息
     * 
     * @author chicb
     * @param map
     * @param ticketKey
     * @param developerId
     * @return
     */
    @RequestMapping("/info")
    public String manageInfo(ModelMap map, String developerId) {
        DeveloperDto developer = getDeveloperDto(developerId);
        map.put("developerDto", developer);
        return "/examine/developer/developerInfo.ftl";
    }
    
    
    /**
     * 重置密码
     * 
     * @author chicb
     * @return
     */
    @ResponseBody
    @RequestMapping("/defaultPw")
    public String defaultPw(String developerId) {
        if (YesNoEnum.YES.getValue() == developerService.updatePwd(OpenApiConstants.DEFAULT_PASSWORD, developerId)) {
            return success("success");
        }
        return error("error");
    }

    @ResponseBody
    @RequestMapping("/modifyIps")
    public String modifyDeveloperIps(String developerId, String ips) {
        if (1 == developerService.updateIps(developerId, ips)) {
            return returnSuccess();
        }
        return error("服务开小差，请刷新后重试...");
    }
    
    @ResponseBody
    @RequestMapping("/modifyUnitName")
    public String modifyDeveloperUnitName(String developerId, String unitName) {
        if (1 == developerService.updateUnitName(developerId, unitName)) {
            return returnSuccess();
        }
        return error("服务开小差，请刷新后重试...");
    }
    
 //--------------------私有方法区 ---------------
	private List<DeveloperDto> getDevelperInfos(Developer developer) {
		int userType = developer.getUserType();
		List<Developer> developers = Lists.newArrayList();
		if(Developer.IS_SUPER_TYPE == userType) {
			developers = developerService.getAllOdereByCreationTime();
		}else if (Developer.IS_AP_ADMID_TYPE == userType) {
			developers = developerService.findByApkeyAndCreationTimeDesc(developer.getApKey());
		}else{
			developers.add(developer);
		}
        if (CollectionUtils.isNotEmpty(developers)) {
            Set<String> list = EntityUtils.getSet(developers, Developer::getTicketKey);
            List<OpenApiApply> applys = openApiApplyService.getApplys(list.toArray(new String[list.size()]));
            Map<String,List<OpenApiApply>> ticketKeyMap = EntityUtils.getListMap(applys, OpenApiApply::getTicketKey, Function.identity());
            Map<String, String> typeNameMap = getAllTypeNameMap();
            Map<String, List<String>> map = new HashMap<>();
            Set<String> check = new HashSet<>();
            for (Map.Entry<String,List<OpenApiApply>> entry : ticketKeyMap.entrySet()) {
            	String ticketKey = entry.getKey();
            	List<String> typeNames = new ArrayList<>();
            	List<OpenApiApply> openApiApplies = entry.getValue();
            	openApiApplies.forEach(e->{
            		if (ApplyStatusEnum.PASS_VERIFY.getValue() == e.getStatus()) {
            			typeNames.add(typeNameMap.get(e.getType()));
            		}else if (ApplyStatusEnum.IN_VERIFY.getValue() == e.getStatus()) {
            			check.add(ticketKey);
            		}
            	});
            	map.put(ticketKey, typeNames);
			}
            return getDeveloperDtos(developers, map, check);
        }
        return Collections.emptyList();          
	}
	
	/**
	 * 得到所有的接口类型名称 根据 type-->key typeName -->value
	 * @return
	 */
	private Map<String, String> getAllTypeNameMap() {
		Integer[] types = {OpenApiInterfaceType.RESULT_TYPE,OpenApiInterfaceType.PUBLIC_TYPE};
		List<OpenApiInterfaceType>  interfaceTypes = openApiInterfaceTypeService.findByClassifyIn(types);
		return EntityUtils.getMap(interfaceTypes, OpenApiInterfaceType::getType, OpenApiInterfaceType::getTypeName);
	}
	
	 private List<DeveloperDto> getDeveloperDtos(List<Developer> developers, Map<String, List<String>> interfaces,
	            Set<String> check) {
	        boolean ismap = MapUtils.isNotEmpty(interfaces);
	        boolean isset = CollectionUtils.isNotEmpty(check);
	        List<DeveloperDto> dtos = new ArrayList<DeveloperDto>();
	        for (Developer d : developers) {
	            DeveloperDto dto = convertDeveloperDto(d);
	            dto.setInterfaceNames(ismap ? interfaces.get(d.getId()) : null);
	            if (isset) {
	                dto.setIsCheck(check.contains(d.getTicketKey()) ? YesNoEnum.YES.getValue() : YesNoEnum.NO.getValue());
	            }
	            dto.setPower(Developer.IS_COMMON_TYPE == d.getUserType() ? Boolean.FALSE : Boolean.TRUE);
	            dtos.add(dto);
	        }
	        return dtos;
	    }

	    /**
	     * @author chicb
	     * @param d
	     * @return
	     */
	    private DeveloperDto convertDeveloperDto(Developer d) {
	        DeveloperDto dto = new DeveloperDto();
	        dto.setAddress(d.getAddress());
	        dto.setCreationTime(d.getCreationTime());
	        dto.setTicketKey(d.getTicketKey());
	        dto.setEmail(d.getEmail());
	        dto.setId(d.getId());
	        dto.setMobilePhone(d.getMobilePhone());
	        dto.setRealName(d.getRealName());
	        dto.setUsername(d.getUsername());
	        dto.setIps(d.getIps());
	        dto.setApKey(d.getApKey());
	        dto.setUnitName(d.getUnitName());
	        return dto;
	    }
	    
	    private DeveloperDto getDeveloperDto(String id) {
	        Developer one = developerService.findOne(id);
	        return convertDeveloperDto(one);
	    }
}
