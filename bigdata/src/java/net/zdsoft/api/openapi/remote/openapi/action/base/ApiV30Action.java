package net.zdsoft.api.openapi.remote.openapi.action.base;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.api.base.service.ApiInterfaceCountService;
import net.zdsoft.api.openapi.remote.openapi.action.ApiBaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = { "/api/remote/openapi", "/api/openapi" })
public class ApiV30Action extends ApiBaseAction {
    
	@Autowired
	ApiInterfaceCountService openApiInterfaceCountService;
    // ------------------------------------
    // 基础数据接口

    @ResponseBody
    @RequestMapping(value = "/v3.0/{type}")
    @ControllerInfo(value = "{type}开发者调用接口：/openapi/v3.0/{type}",parameter = "{ticketKey}")
    public String queryData(@PathVariable String type, HttpServletRequest request) {
        String uri = "/openapi/v3.0/" + type;
        return qd(type, request, uri);
    }

    @ResponseBody
    @RequestMapping(value = "/v3.0/{type}/{id}")
    @ControllerInfo(value = "{type}开发者调用接口：/openapi/v3.0/{type}/id",parameter = "{ticketKey}")
    public String queryData(@PathVariable String type, @PathVariable String id, HttpServletRequest request) {
        String uri = "/openapi/v3.0/" + type + "/{id}";
        return qd(type, id, request, uri, null);
    }

    @ResponseBody
    @RequestMapping(value = "/v3.0/{type}/{id}/{subType}")
    @ControllerInfo(value = "{type}开发者调用接口：/openapi/v3.0/{type}/id/{subType}",parameter = "{ticketKey}")
    public String queryData(@PathVariable String type, @PathVariable String id, @PathVariable String subType,
            HttpServletRequest request) {
        String uri = "/openapi/v3.0/" + type + "/{id}/" + subType;
        return qd(type, id, subType, request, uri, null);
    }
}
