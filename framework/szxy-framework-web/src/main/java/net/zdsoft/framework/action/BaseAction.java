package net.zdsoft.framework.action;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import net.zdsoft.framework.config.ControllerException;
import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.Specifications;
import net.zdsoft.framework.utils.PageUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.UrlUtils;

public class BaseAction {

	protected static final Logger log = Logger.getLogger(BaseAction.class);

	public LoginInfo getLoginInfo(HttpSession httpSession) {
//		String sessionId = httpSession.getId();
//		Session session = Session.get(sessionId);
//		if (session == null) { 
//			return null;
//		} else {
//			return session.getAttribute("loginInfo", LoginInfo.class);
//		}
		return (LoginInfo)httpSession.getAttribute("loginInfo");
	}

	public <K, V> String getMcodeWithJqGrid(Map<K, V> map) {
		String s = "";
		for (K k : map.keySet()) {
			String t = StringUtils.trimToEmpty(k.toString()) + ":" + StringUtils.trimToEmpty(map.get(k).toString());
			if (StringUtils.isBlank(s)) {
				s = t;
			} else {
				s = s + ";" + t;
			}
		}
		return s;
	}

	public LoginInfo getLoginInfo() {
		return getLoginInfo(getRequest().getSession());
	}

	@ModelAttribute
	public void modelAttribute(ModelMap map, String rtnBackUrl) {
		if (StringUtils.isNotBlank(rtnBackUrl))
			map.put("_rtnBackUrl", rtnBackUrl);
	}

	public List<String> getPermission(HttpSession httpSession) {
		return RedisUtils.hgetObject("user.permissions", getLoginInfo(httpSession).getUserId(),
				new TypeReference<List<String>>() {
				});
	}

	public boolean isPermission(HttpSession httpSession, String permitUrl) {
		List<String> permissions = RedisUtils.hgetObject("user.permissions", getLoginInfo(httpSession).getUserId(),
				new TypeReference<List<String>>() {
				});
		return CollectionUtils.isEmpty(permissions) || permissions.contains(permitUrl);
	}

	public boolean isPermission(String userId, String permitUrl) {
		List<String> permissions = RedisUtils.hgetObject("user.permissions", userId, new TypeReference<List<String>>() {
		});
		return CollectionUtils.isEmpty(permissions) || permissions.contains(permitUrl);
	}

	public List<String> getPermission(String userId) {
		return RedisUtils.hgetObject("user.permissions", userId, new TypeReference<List<String>>() {
		});
	}

	@ResponseBody
	@ExceptionHandler(ControllerException.class)
	public String runtimeExceptionHandler(ControllerException runtimeException, WebRequest request) {
		log.error("controller error ,code {} , msg {}", runtimeException);
		return Json.toJSONString(new ResultDto().setSuccess(false).setCode(runtimeException.getCode())
				.setMsg(runtimeException.getMessage()));
	}

	@ResponseBody
	@ExceptionHandler(Exception.class)
	public String runtimeExceptionHandler2(Exception runtimeException, WebRequest request) {
		log.error("controller error ,code {} , msg {}", runtimeException);
		return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg(runtimeException.getMessage()));
	}

	public String returnError(String code, String msg) {
		return Json.toJSONString(new ResultDto().setSuccess(false).setCode(code).setMsg(msg));
	}

	public String returnError(String code, String msg, String detailError) {
		ResultDto rd = new ResultDto();
		rd.setCode(code);
		if (StringUtils.isNotBlank(detailError)) {
			rd.setMsg(msg + "<div class='hide'>" + detailError
					+ "</div><br>点击查看<a href='javascript:void();' onclick='alert(1);)'>详细错误</a>");
		}
		return Json.toJSONString(new ResultDto().setSuccess(false).setCode(code).setMsg(msg));
	}

	public String error(String msg) {
		return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg(msg));
	}

	public String errorFtl(ModelMap map, String msg) {
		map.put("_errorMsg", msg);
		return "/fw/homepage/error.ftl";
	}
	/**
	 * 跟errorFtl类似的提示
	 * @param map
	 * @param msg
	 * @return
	 */
	public String promptFlt(ModelMap map, String msg){
		map.put("_promptMsg", msg);
		return "/fw/homepage/prompt.ftl";
	}

	public void addErrorFtlOperation(ModelMap map, String operationName, String operationUrl) {
		@SuppressWarnings("unchecked")
		List<String> operas = (List<String>) map.get("_errorOperations");
		if (operas == null) {
			operas = new ArrayList<String>();
			map.put("_errorOperations", operas);
		}
		JSONObject json = new JSONObject();
		json.put("name", operationName);
		json.put("url", operationUrl);
		operas.add(json.toJSONString());
		map.put("_errorOperations", operas);
	}

	/**
	 * 
	 * @param map
	 * @param operationName
	 * @param operationUrl
	 * @param divJquery
	 *            写法.divClss或者#divId
	 */
	public void addErrorFtlOperation(ModelMap map, String operationName, String operationUrl, String divJquery) {
		@SuppressWarnings("unchecked")
		List<String> operas = (List<String>) map.get("_errorOperations");
		if (operas == null) {
			operas = new ArrayList<String>();
			map.put("_errorOperations", operas);
		}
		JSONObject json = new JSONObject();
		json.put("name", operationName);
		json.put("url", operationUrl);
		json.put("divJquery", divJquery);
		operas.add(json.toJSONString());
		map.put("_errorOperations", operas);
	}

	public String returnJqGridData(Pagination page, JSONArray array) {
		JSONObject on = new JSONObject();
		if (page.getMaxPageIndex() < page.getPageIndex()) {
			page.setPageIndex(1);
		}
		on.put("rows", array);
		on.put("records", page.getMaxRowCount());
		on.put("page", page.getPageIndex());
		on.put("total", page.getMaxPageIndex());
		return on.toString();
	}

	public <T> String returnJqGridData(Pagination page, List<T> ts) {
		JSONObject on = new JSONObject();
		if (page.getMaxPageIndex() < page.getPageIndex()) {
			page.setPageIndex(1);
		}
		JSONArray array = new JSONArray();
		for (T t : ts) {
			array.add(JSON.toJSON(t));
		}
		on.put("rows", array);
		on.put("records", page.getMaxRowCount());
		on.put("page", page.getPageIndex());
		on.put("total", page.getMaxPageIndex());
		return on.toString();
	}

	public String returnError() {
		return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg("操作失败！"));
	}

	public String returnSuccess(String code, String msg) {
		return Json.toJSONString(new ResultDto().setSuccess(true).setCode(code).setMsg(msg));
	}

	public String success(String msg) {
		return Json.toJSONString(new ResultDto().setSuccess(true).setCode("00").setMsg(msg));
	}

	public String returnSuccess() {
		return Json.toJSONString(new ResultDto().setSuccess(true).setCode("00").setMsg("操作成功！"));
	}
	
	public String successByValue(String businessValue) {
		return Json.toJSONString(new ResultDto().setSuccess(true).setCode("00").setMsg("操作成功！").setBusinessValue(businessValue));
	}

	public String jobSuccess(String jobId, String msg) {
		JSONObject on = new JSONObject();
		on.put("success", true);
		on.put("msg", msg);
		on.put("jobId", jobId);
		return on.toJSONString();
	}

	public HttpSession getSession() {
		return getRequest().getSession();
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

	protected Pagination createPaginationJqGrid(HttpServletRequest request) {
		Map<String, String> paramMap = syncParameters(request);
		int index = NumberUtils.toInt(paramMap.get("page"));
		if (index <= 0) {
			index = 1;
		}
		int row = NumberUtils.toInt(paramMap.get("rows"));
		if (row <= 0) {
			row = 20;
		}
		Pagination page = new Pagination(index, row, false);
		page.setParams(paramMap);
		page.setUri(request.getRequestURI());
		return page;
	}

	protected void sendPagination(HttpServletRequest request, ModelMap map, Pagination page) {
		map.put("pageContent", page);
		String htmlOfPaginationLoad = "";
		Map<String, String[]> mop = request.getParameterMap();
		Map<String, String> mapOfParameter = new HashMap<String, String>();
		for (String key : mop.keySet()) {
			mapOfParameter.put(key, mop.get(key)[0]);
		}
		Map<String, String> mapOfParameterOpe = new HashMap<String, String>();
		mapOfParameterOpe.putAll(mapOfParameter);
		if (mapOfParameterOpe.containsKey("_pageIndex")) {
			mapOfParameterOpe.remove("_pageIndex");
		}
		if (mapOfParameterOpe.containsKey("_pageSize")) {
			mapOfParameterOpe.remove("_pageSize");
		}
		String[] keys = mapOfParameterOpe.keySet().toArray(new String[0]);
		Object[] values = new Object[0];
		if (keys != null) {
			values = new Object[keys.length];
			for (int i = 0; i < keys.length; i++) {
				values[i] = mapOfParameterOpe.get(keys[i]);
			}
		}
		String actionName = request.getRequestURI();
		String url = UrlUtils.addQueryString(actionName, keys, values);
		htmlOfPaginationLoad = PageUtils.paginationLoad(url, page);
		map.put("htmlOfPaginationLoad", htmlOfPaginationLoad);
	}

	protected Pagination createPagination() {
		return createPagination(getRequest());
	}

	protected <T> Specifications<T> getSpecification() {
		Specifications<T> sp = new Specifications<T>();
		return sp;
	}

	// /**
	// * 根据jqgrid过滤器生成SPEL语言脚本
	// *
	// * @param request
	// * @return
	// */
	// protected String createJqGridFilter(HttpServletRequest request) {
	// String json = request.getParameter("filters");
	// List<String> ops = new ArrayList<String>();
	// String groupOp = null;
	// if (json != null) {
	// JsonNode node = JsonUtils.toObjectNode(json);
	// if (node != null) {
	// groupOp = node.get("groupOp").asText();
	// JsonNode rules = node.findValues("rules").get(0);
	// for (JsonNode jnode : rules) {
	// String op = jnode.get("op").asText();
	// String dn = "data." + jnode.get("field").asText();
	// String field;
	// if (ArrayUtils.contains(new String[] { "nn", "nu" }, op)) {
	// field = "(#" + dn + ")";
	// }
	// else {
	// field = "(#" + dn + " == null ? \"\" : #" + dn +
	// " + \"\").toLowerCase()";
	// }
	// String data = jnode.get("data").asText().toLowerCase();
	// if (StringUtils.equals(op, "cn"))
	// ops.add("(" + field + ".contains(\"" + data + "\"))");
	// else if (StringUtils.equals(op, "bw"))
	// ops.add("(" + field + ".startsWith(\"" + data + "\"))");
	// else if (StringUtils.equals(op, "bn"))
	// ops.add("(!" + field + ".startsWith(\"" + data + "\"))");
	// else if (StringUtils.equals(op, "in"))
	// ops.add("(" + field + ".contains(\"" + data + "\"))");
	// else if (StringUtils.equals(op, "ni"))
	// ops.add("(" + field + ".contains(\"" + data + "\"))");
	// else if (StringUtils.equals(op, "ew"))
	// ops.add("(" + field + ".endsWith(\"" + data + "\"))");
	// else if (StringUtils.equals(op, "en"))
	// ops.add("(!" + field + ".endsWith(\"" + data + "\"))");
	// else if (StringUtils.equals(op, "nc"))
	// ops.add("(!" + field + ".contains(\"" + data + "\"))");
	// else if (StringUtils.equals(op, "nu"))
	// ops.add("(" + field + " == null)");
	// else if (StringUtils.equals(op, "nn"))
	// ops.add("(" + field + " != null)");
	// else
	// ops.add("(" + field + " " + op + " \"" + data + "\")");
	//
	// }
	// }
	// }
	// String ops_ = StringUtils.join(ops.toArray(new String[0]), " " +
	// groupOp
	// + " ");
	// return ops_;
	// }

	/**
	 * 根据jqgrid的标题点击排序进行排序
	 * 
	 * @param units
	 * @param filterMap
	 * @return
	 */
	protected <T> List<T> applyJqGridSort(List<T> units, Map<String, String> filterMap) {
		final String sidx = String.valueOf(filterMap.get("sidx"));
		if (StringUtils.isNotBlank(sidx)) {
			final StandardEvaluationContext context = new StandardEvaluationContext();
			final ExpressionParser parser = new SpelExpressionParser();
			final String sord = String.valueOf(filterMap.get("sord"));

			Collections.sort(units, new Comparator<T>() {
				@Override
				public int compare(T o1, T o2) {
					try {
						Field field = o1.getClass().getDeclaredField(sidx);
						String c = field.getType().getName();
						if (ArrayUtils.contains(new String[] { "java.lang.Integer", "java.lang.Long", "int", "long" },
								c)) {
							context.setVariable("o1", o1);
							context.setVariable("o2", o2);
							Integer i1 = parser.parseExpression("#o1." + sidx).getValue(context, Integer.class);
							Integer i2 = parser.parseExpression("#o2." + sidx).getValue(context, Integer.class);
							int k = (i1 == null ? 0 : i1) - (i2 == null ? 0 : i2);
							return StringUtils.equals(sord, "asc") ? k : -k;
						} else {
							context.setVariable("o1", o1);
							context.setVariable("o2", o2);
							int k = parser.parseExpression("#o1." + sidx + ".compareTo(#o2." + sidx + ")")
									.getValue(context, int.class);
							return StringUtils.equals(sord, "asc") ? k : -k;
						}
					} catch (Exception e) {
						return 0;
					}

				}
			});
		}
		return units;
	}

	protected HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

	protected HttpServletResponse getResponse() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
	}

	// @ModelAttribute
	// public void setRequestAndResponse(HttpServletRequest
	// request,HttpServletResponse response){
	// this.request = request;
	// this.response = response;
	// }
}
