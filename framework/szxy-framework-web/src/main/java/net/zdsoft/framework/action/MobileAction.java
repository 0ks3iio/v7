package net.zdsoft.framework.action;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.Pagination;

public class MobileAction {
		
	protected static final Logger log = Logger.getLogger(MobileAction.class);
	
	public String returnSuccess(String code, String msg) {
		return Json.toJSONString(new ResultDto().setSuccess(true).setCode(code).setMsg(msg));
	}
	
	protected HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

	protected HttpServletResponse getResponse() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
	}
	
	public String success() {
		return success("操作成功！");
	}
	
	public String error() {
		return error("操作失败！");
	}

	public String success(String msg) {
		return Json.toJSONString(new ResultDto().setSuccess(true).setCode("00").setMsg(msg));
	}
	public String error(String msg) {
		return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg(msg));
	}
	
	public String errorFtl(ModelMap map, String msg) {
		map.put("_errorMsg", msg);
		return "/fw/homepage/mobileError.ftl";
	}
	
	protected Pagination createPagination(HttpServletRequest request) {
		Map<String, String> paramMap = syncParameters(request);
		int index = NumberUtils.toInt(paramMap.get("_pageIndex"));
		if (index <= 0) {
			index = 1;
		}
		int row = NumberUtils.toInt(paramMap.get("_pageSize"));
		if (row <= 0) {
			row = 50;
		}
		Pagination page = new Pagination(index, row, false);
		page.setParams(paramMap);
		page.setUri(request.getRequestURI());
		return page;
	}
	
	@SuppressWarnings("unchecked")
	protected Map<String, String> syncParameters(HttpServletRequest request) {
		Map<String, String> paramMap = new HashMap<String, String>();
		Enumeration<String> keys = request.getParameterNames();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			String s = request.getParameter(key);
			paramMap.put(key, s);
		}
		return paramMap;
	}
}
