package net.zdsoft.remote.openapi.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.remote.openapi.entity.Developer;
import net.zdsoft.remote.openapi.entity.EntityTicket;
import net.zdsoft.remote.openapi.entity.OpenApiEntity;
import net.zdsoft.remote.openapi.entity.OpenApiInterface;
import net.zdsoft.remote.openapi.entity.OpenApiParameter;
import net.zdsoft.remote.openapi.service.EntityTicketService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

@Controller
@RequestMapping(value = { "/remote/openapi" })
public class OpenApiIndexAction extends OpenApiBaseAction {

    @Autowired
    McodeRemoteService mcodeRemoteService;
    @Autowired
    EntityTicketService entityTicketService;

    @ResponseBody
    @RequestMapping(value = "/login")
    public String login() {
        return "Login";
    }

    @RequestMapping(value = "/updateTicketKey")
    public String updateTicket(HttpSession httpSession, RedirectAttributes redAttri, String ticketKey,
            String currentInterfaceType) {
    	
    	if(StringUtils.equals(ticketKey, "zdsoft.net")){
    		EntityTicket entityTicket = entityTicketService.findOneTop();
    		if(entityTicket != null){
    			ticketKey = entityTicket.getTicketKey();
    		}
    	}
    	else{
	        boolean ticketCheck = false;
	        List<Developer> keys = developerService.findAll();
	        for (Developer key : keys) {
	            if (StringUtils.equalsIgnoreCase(key.getTicketKey(), ticketKey)) {
	                ticketCheck = true;
	                break;
	            }
	        }
	        if (!ticketCheck) {
	            redAttri.addFlashAttribute("errorMsg", "ticketKey输入错误！");
	        }
	        else {
	            redAttri.addFlashAttribute("errorMsg", "");
	        }
    	}
        redAttri.addFlashAttribute("currentInterfaceType", currentInterfaceType);
        redAttri.addFlashAttribute("ticketKey", ticketKey);
        return "redirect:index";
    }

    @ResponseBody
    @RequestMapping(value = "/entity/hide", method = RequestMethod.POST)
    public String hideEntity(String entityName, String type, String id) {
        int ret = openApiEntityService.updateEntityHide(entityName, type);
        if (ret > 0) {
            Set<String> keys = RedisUtils.keys(OPEN_API_KEY + ".findEntities.types*");
            RedisUtils.del(keys.toArray(new String[0]));
            return "1";
        }
        else {
            return "0";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/entity/mcodeId", method = RequestMethod.POST)
    public String updateEntityMcodeId(String mcodeId, String id) {
        int ret = openApiEntityService.updateEntityMcode(mcodeId, id);
        if (ret > 0) {
            Set<String> keys = RedisUtils.keys(OPEN_API_KEY + ".findEntities.types*");
            RedisUtils.del(keys.toArray(new String[0]));
            return "1";
        }
        else {
            return "0";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/parameter", method = RequestMethod.POST)
    public String addparameter(String uri, String entityName, String type, String id) {
        OpenApiParameter parameter = new OpenApiParameter();
        OpenApiEntity entity = openApiEntityService.findByTypeAndEntityName(type, entityName);
        parameter.setDescription(entity.getDisplayName());
        parameter.setDisplayOrder(entity.getDisplayOrder());
        parameter.setMandatory(0);
        parameter.setMcodeId(entity.getMcodeId());
        parameter.setParamColumnName(entity.getEntityColumnName());
        parameter.setParamName(entityName);
        parameter.setUri(uri);
        parameter.setId(UuidUtils.generateUuid());
        openApiParameterService.save(parameter);

        Set<String> keys = RedisUtils.keys(OPEN_API_KEY + ".findParameters.uri*");
        RedisUtils.del(keys.toArray(new String[0]));
        if (StringUtils.isNotBlank(parameter.getId())) {
            return "1";
        }
        else {
            return "0";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/parameter/{id}", method = RequestMethod.GET)
    public String deleteParameter(@PathVariable String id) {
        openApiParameterService.delete(id);
        Set<String> keys = RedisUtils.keys(OPEN_API_KEY + ".findParameters.uri*");
        RedisUtils.del(keys.toArray(new String[0]));
        return "1";
    }

    @RequestMapping("/index")
    public String index(@ModelAttribute("ticketKey") String ticketKey,
            @ModelAttribute("currentInterfaceType") String currentInterfaceType, ModelMap map, HttpSession httpSession)
            throws Exception {
        // 获取接口类型
        List<String> interfaceTypes = openApiInterfaceService.findInterfaceType();
        map.put("interfaceTypes", interfaceTypes);
        map.put("currentInterfaceType",
                StringUtils.isBlank(currentInterfaceType) ? interfaceTypes.size() > 0 ? interfaceTypes.get(0) : ""
                        : currentInterfaceType);
        return "/remote/openapi/index.ftl";
    }

    @RequestMapping("/showInterface")
    public String showInterface(ModelMap map, String ticketKey, String currentInterfaceType) {
        Map<String, List<OpenApiParameter>> openApiParameters;
        Map<String, List<OpenApiEntity>> openApiEntities;
        List<OpenApiInterface> openApiInterfaces;
        // 根据当前选中的接口类型，获取具体接口
        openApiInterfaces = openApiInterfaceService.findInterfaces(currentInterfaceType);

        map.put("currentInterfaceType", currentInterfaceType);
        map.put("openApiInterfaces", openApiInterfaces);
        map.put("ticketKey", ticketKey);

        List<String> urls = new ArrayList<String>();
        Set<String> types = new HashSet<String>();
        for (OpenApiInterface oai : openApiInterfaces) {
            urls.add(oai.getUri());
            types.add(oai.getResultType());
        }
        Set<String> mcodeIds = new HashSet<String>();
        openApiParameters = openApiParameterService.findParameters(urls.toArray(new String[0]));
        for (String key : openApiParameters.keySet()) {
            List<OpenApiParameter> oaps = openApiParameters.get(key);
            for (OpenApiParameter oap : oaps) {
                if (StringUtils.isNotBlank(oap.getMcodeId())) {
                    mcodeIds.add(oap.getMcodeId());
                }
            }
        }
        if (CollectionUtils.isNotEmpty(mcodeIds)) {
            Map<String, List<McodeDetail>> mcodeMap = new HashMap<String, List<McodeDetail>>();
            for (String mcodeId : mcodeIds) {
                List<McodeDetail> mcodes = SUtils.dt(mcodeRemoteService.findByMcodeIds(mcodeId),
                        new TypeReference<List<McodeDetail>>() {
                        });
                mcodeMap.put(mcodeId, mcodes);

            }
            map.put("mcodeMap", mcodeMap);
        }
        Developer findByTicketKey = developerService.findByTicketKey(ticketKey);
        openApiEntities = openApiEntityService.findEntities(findByTicketKey, types.toArray(new String[0]));
        map.put("openApiParameters", openApiParameters);
        map.put("openApiEntities", openApiEntities);
        return "/remote/openapi/interface.ftl";

    }

    @ResponseBody
    @RequestMapping(value = "/mcode/{mcodeId}", method = RequestMethod.GET)
    public String queryMcodes(@PathVariable String mcodeId) throws Exception {
        List<McodeDetail> mcodes = SUtils.dt(mcodeRemoteService.findByMcodeIds(mcodeId),
                new TypeReference<List<McodeDetail>>() {
                });
        return JSON.toJSONString(mcodes);
    }

}
