package net.zdsoft.remote.openapi.action.base;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.remote.openapi.action.OpenApiBaseAction;
import net.zdsoft.remote.openapi.service.OpenApiInterfaceCountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = { "/remote/openapi", "/openapi" })
public class OpenApiV20Action extends OpenApiBaseAction {
    
	@Autowired
	OpenApiInterfaceCountService openApiInterfaceCountService;
    // ------------------------------------
    // 基础数据接口

    @ResponseBody
    @RequestMapping(value = "/v2.0/{type}")
    @ControllerInfo(value = "{type}开发者调用接口：/openapi/v2.0/{type}",parameter = "{ticketKey}")
    public String queryData(@PathVariable String type, HttpServletRequest request) {
        String uri = "/openapi/v2.0/" + type;
        return qd(type, request, uri, null);
    }

    @ResponseBody
    @RequestMapping(value = "/v2.0/{type}/{id}")
    @ControllerInfo(value = "{type}开发者调用接口：/openapi/v2.0/{type}/id",parameter = "{ticketKey}")
    public String queryData(@PathVariable String type, @PathVariable String id, HttpServletRequest request) {
        String uri = "/openapi/v2.0/" + type + "/{id}";
        return qd(type, id, request, uri, null);
    }

    @ResponseBody
    @RequestMapping(value = "/v2.0/{type}/{id}/{subType}")
    @ControllerInfo(value = "{type}开发者调用接口：/openapi/v2.0/{type}/id/{subType}",parameter = "{ticketKey}")
    public String queryData(@PathVariable String type, @PathVariable String id, @PathVariable String subType,
            HttpServletRequest request) {
        String uri = "/openapi/v2.0/" + type + "/{id}/" + subType;
        return qd(type, id, subType, request, uri, null);
    }
}
