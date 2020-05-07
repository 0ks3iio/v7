package net.zdsoft.openapi.remote.openapi.action.base;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.base.constant.BaseDataCenterConstant;
import net.zdsoft.base.dto.ResultBean;
import net.zdsoft.base.service.BaseEntityService;
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
@RequestMapping(value = { "/remote/openapi/sync", "/openapi/sync" })
public class OpenApiSaveV30Action {
	public <T> ResultBean<T> success() {
		return new ResultBean<T>().setMsg("成功");
	}
	
	@Autowired
	BaseEntityService baseEntityService;
	@ResponseBody
    @RequestMapping(value = "/v3.0/{type}",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ControllerInfo(value = "{type}开发者调用接口：/v3.0/{type}",parameter = "{ticketKey}")
	public ResultBean<String> saveOrUpdateData(@RequestBody String jsonStr,@PathVariable String type,  HttpServletRequest request) {
		String ticketKey = request.getParameter(BaseDataCenterConstant.TICKET_KEY);
		baseEntityService.dealModelSave(ticketKey, jsonStr, type,0);
		return success();
	}	
	@ResponseBody
	@RequestMapping(value = "/save/v3.0/{type}",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ControllerInfo(value = "{type}开发者调用接口：/v3.0/{type}",parameter = "{ticketKey}")
	public ResultBean<String> saveData(@RequestBody String jsonStr,@PathVariable String type,  HttpServletRequest request) {
		String ticketKey = request.getParameter(BaseDataCenterConstant.TICKET_KEY);
		baseEntityService.dealModelSave(ticketKey, jsonStr, type,1);
		return success();
	}	
	
	@ResponseBody
	@RequestMapping(value = "/update/v3.0/{type}",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ControllerInfo(value = "{type}开发者调用接口：/v3.0/{type}",parameter = "{ticketKey}")
	public ResultBean<String> updateData(@RequestBody String jsonStr,@PathVariable String type,  HttpServletRequest request) {
		String ticketKey = request.getParameter(BaseDataCenterConstant.TICKET_KEY);
		baseEntityService.dealModelSave(ticketKey, jsonStr, type,2);
		return success();
	}	
}
