package net.zdsoft.api.openapi.remote.openapi.action.base;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.api.base.constant.BaseDataCenterConstant;
import net.zdsoft.api.base.dto.ResultBean;
import net.zdsoft.api.base.service.ApiBaseEntityService;
import net.zdsoft.framework.annotation.ControllerInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = { "/api/remote/openapi/sync", "/api/openapi/sync" })
public class ApiSaveV30Action {
	public <T> ResultBean<T> success() {
		return new ResultBean<T>().setMsg("成功");
	}
	
	@Autowired
	ApiBaseEntityService baseEntityService;
	
	@ResponseBody
    @RequestMapping(value = "/v3.0/{type}",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ControllerInfo(value = "{type}开发者调用接口：/v3.0/{type}",parameter = "{ticketKey}")
	public String saveOrUpdateData(@RequestBody String jsonStr,@PathVariable String type,  HttpServletRequest request) {
		String ticketKey = request.getParameter(BaseDataCenterConstant.TICKET_KEY);
		return baseEntityService.up(ticketKey, jsonStr, type,0);
	}	
	@ResponseBody
	@RequestMapping(value = "/save/v3.0/{type}",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ControllerInfo(value = "{type}开发者调用接口：/v3.0/{type}",parameter = "{ticketKey}")
	public String saveData(@RequestBody String jsonStr,@PathVariable String type,  HttpServletRequest request) {
		String ticketKey = request.getParameter(BaseDataCenterConstant.TICKET_KEY);
		return baseEntityService.up(ticketKey, jsonStr, type,1);
	}	
	
	@ResponseBody
	@RequestMapping(value = "/update/v3.0/{type}",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ControllerInfo(value = "{type}开发者调用接口：/v3.0/{type}",parameter = "{ticketKey}")
	public String updateData(@RequestBody String jsonStr,@PathVariable String type,  HttpServletRequest request) {
		String ticketKey = request.getParameter(BaseDataCenterConstant.TICKET_KEY);
		return baseEntityService.up(ticketKey, jsonStr, type,2);
	}	
}
