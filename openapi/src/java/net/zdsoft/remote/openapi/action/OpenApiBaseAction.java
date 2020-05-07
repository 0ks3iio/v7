package net.zdsoft.remote.openapi.action;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.TeachClass;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.TeachClassRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.JsonUtils;
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.SecurityUtils;
import net.zdsoft.pushjob.constant.BaseOpenapiConstant;
import net.zdsoft.remote.openapi.constant.OpenApiConstants;
import net.zdsoft.remote.openapi.dto.InnerOpenDto;
import net.zdsoft.remote.openapi.entity.Developer;
import net.zdsoft.remote.openapi.entity.DeveloperPower;
import net.zdsoft.remote.openapi.entity.OpenApiApply;
import net.zdsoft.remote.openapi.entity.OpenApiEntity;
import net.zdsoft.remote.openapi.entity.OpenApiInterface;
import net.zdsoft.remote.openapi.entity.OpenApiInterfaceCount;
import net.zdsoft.remote.openapi.entity.OpenApiInterfaceType;
import net.zdsoft.remote.openapi.entity.OpenApiParamDto;
import net.zdsoft.remote.openapi.entity.OpenApiParameter;
import net.zdsoft.remote.openapi.enums.ApplyStatusEnum;
import net.zdsoft.remote.openapi.enums.InterfaceTypeEnum;
import net.zdsoft.remote.openapi.enums.OpenapiParamEnum;
import net.zdsoft.remote.openapi.service.BaseCommonService;
import net.zdsoft.remote.openapi.service.DeveloperPowerService;
import net.zdsoft.remote.openapi.service.DeveloperService;
import net.zdsoft.remote.openapi.service.OpenApiApplyService;
import net.zdsoft.remote.openapi.service.OpenApiEntityService;
import net.zdsoft.remote.openapi.service.OpenApiInterfaceCountService;
import net.zdsoft.remote.openapi.service.OpenApiInterfaceService;
import net.zdsoft.remote.openapi.service.OpenApiInterfaceTypeService;
import net.zdsoft.remote.openapi.service.OpenApiParameterService;
import oracle.sql.TIMESTAMP;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class OpenApiBaseAction extends BaseAction {

    // 缓存key开头
    public static final String OPEN_API_KEY = "openapi.";

    public static final String OPEN_AP_ID = "openApId";
    public static final String OPEN_PARAM = "openParam";
    public static final String MAX_NUM_DAY_ERROR_MSG = "今天调用的次数已经超过限制！";
    public static final String APPLY_ERROR_MSG = "该类型的接口没有申请！";
    protected Logger log = Logger.getLogger(OpenApiBaseAction.class);

    @Autowired
    BaseCommonService baseCommonService;
    @Autowired
	SemesterRemoteService semesterRemoteServicer;
    @Autowired
    OpenApiParameterService openApiParameterService;
    @Autowired
    OpenApiEntityService openApiEntityService;
    @Autowired
    OpenApiInterfaceService openApiInterfaceService;
    @Autowired
    DeveloperService developerService;
    @Autowired
    DeveloperPowerService developerPowerService;
    @Autowired
    UnitRemoteService unitRemoteService;
    @Autowired
    OpenApiInterfaceTypeService openApiInterfaceTypeService;
    @Autowired
    OpenApiApplyService openApiApplyService;
    @Autowired
    OpenApiInterfaceCountService openApiInterfaceCountService;
    @Autowired
    TeachClassRemoteService teachClassRemoteService;
    @Autowired
    public StudentRemoteService studentRemoteService;
    
    /**
     * 该类型 是否已经申请了
     * 
     */
    protected boolean isApply(String type, HttpServletRequest request) {
    	Developer developer = getDeveloperByRequest(request);
    	List<OpenApiApply> openApiApplies = openApiApplyService.findByDeveloperIdAndTypeIn(developer.getId(), type);
    	return CollectionUtils.isNotEmpty(openApiApplies);
    }
    /**
     * 是否超过每天限制调用次数
	 * @param type
	 * @param request
	 * @param uri
	 */
    protected boolean isOverMaxNumDay(String type, HttpServletRequest request, String uri) {
    	Developer developer = getDeveloperByRequest(request);
    	List<OpenApiApply> openApiApplies = openApiApplyService.findByDeveloperIdAndTypeIn(developer.getId(), type);
    	if(CollectionUtils.isNotEmpty(openApiApplies)){
    		int maxNum = openApiApplies.get(0).getMaxNumDay();
    		Integer limitEveryTime = openApiApplies.get(0).getLimitEveryTime();
    		if(limitEveryTime == null) 
    			limitEveryTime = 1000;
    		request.setAttribute(OpenapiParamEnum.MAX_LIMIT.getName(), limitEveryTime);
    		return isOverMaxNumDay(type, developer.getTicketKey(), maxNum);
    	}
    	return Boolean.FALSE;
    }
    
    protected Developer getDeveloperByRequest(HttpServletRequest request) {
		return developerService.findByTicketKey(getTicketKey(request));
	}

    protected void doSaveInterfaceCount(String interfaceType, HttpServletRequest request, String uri) {
    	openApiInterfaceCountService.save(OpenApiInterfaceCount.saveDoInterface(interfaceType, uri, request,OpenApiConstants.OPENAPI_V20_VERSION));
    }
    
    protected void setDeveloper(Developer developer) {
        HttpSession session = getRequest().getSession();
        session.setAttribute(OpenApiConstants.DEVELOPER_SESSION, developer);
        session.setMaxInactiveInterval(60 * 60);
    }

    protected Developer getDeveloper() {
        return (Developer) getRequest().getSession().getAttribute(OpenApiConstants.DEVELOPER_SESSION);
    }

    @Override
    public String error(String msg) {
        return Json.toJSONString(new ResultDto().setSuccess(false).setCode("-1").setMsg(msg));
    }

    @Override
    public String errorFtl(ModelMap map, String msg) {
        map.put("_errorMsg", msg);
        return "/fw/homepage/error.ftl";
    }

    @Override
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
     * openapi获取基础数据
     * 
     * @param uri
     * @param methodName
     * @param tableName
     * @param resultType
     * @param paramMap
     * @param openTicketKey
     * @param isOld
     *            是否2.0
     * @return
     */
    public JSONObject queryData(String uri, String methodName, final String tableName, String resultType,
            Map<String, OpenApiParamDto> paramDtoMap, HttpServletRequest request, String nextUri, Developer developer,
            boolean isOld) {
        final Map<String, String> paramMap = new HashMap<String, String>();
        for (String key : paramDtoMap.keySet()) {
            paramMap.put(key, paramDtoMap.get(key).getValue());
        }
        JSONObject json = new JSONObject();
        try {
            long time = System.currentTimeMillis();
//            String s = SUtils.s(paramMap) + tableName;
            final Map<String, String> columnMap = validateColumns(developer, resultType);
//            List<Map<String, Object>> entities = RedisUtils.getObject(
//                    OPEN_API_KEY + "queryBaseData." + EntityUtils.getCode(s), RedisUtils.TIME_FIVE_MINUTES,
//                    new TR<List<Map<String, Object>>>() {
//                    }, new RedisInterface<List<Map<String, Object>>>() {
//                        @Override
//                        public List<Map<String, Object>> queryData() {
//                            return baseCommonService.getDataMapByParamMap(tableName, paramMap, columnMap);
//                        }
//                    });
            List<Map<String, Object>> entities = baseCommonService.getDataMapByParamMap(tableName, paramMap, columnMap);
            dealDatas(json, paramMap, entities, developer, columnMap);
            JSONObject pagination = json.getJSONObject("pagination");
            if (pagination != null && !StringUtils.equals(pagination.getString("maxPage"), "1")) {

                String[] systemParams = { "_containAdmin", "_limit", "_dataModifyTime", "_realtime", "_showNull",
                        "_isDeleted" };
                List<String> params = new ArrayList<String>();
                for (String key : paramMap.keySet()) {
                    if (ArrayUtils.contains(systemParams, key) && paramMap.get(key) != null) {
                        params.add(StringUtils.substringAfter(key, "_") + "=" + paramMap.get(key));
                    }
                    else if (StringUtils.startsWith(key, "_")) {
                        continue;
                    }
                    else {
                        params.add(paramDtoMap.get(key).getParamName() + "=" + paramMap.get(key));
                    }
                }
                JSONArray jarray = json.getJSONArray("data");
                long maxId = 0;
                for (int i = 0; i < jarray.size(); i++) {
                    JSONObject obj = jarray.getJSONObject(i);
                    long intId = obj.getLongValue("sequenceIntId");
                    if (intId > maxId) {
                        maxId = intId;
                    }
                }

                params.add("dataSortMaxId=" + maxId);
                if (!params.contains("ticketKey")) {
                    params.add("ticketKey=" + developer.getTicketKey());
                }
                String nextDataUrl = "";
                if (isOld) {
                    nextDataUrl = request.getScheme().trim() + "://" + request.getServerName().trim() + ":"
                            + request.getServerPort() + request.getContextPath() + "/remote" + nextUri + "?"
                            + StringUtils.join(params, "&");
                }
                else {
                    nextDataUrl = request.getScheme().trim() + "://" + request.getServerName().trim() + ":"
                            + request.getServerPort() + request.getContextPath() + "/remote" + nextUri + "?"
                            + OPEN_AP_ID + "=" + developer.getId();
                    JSONObject paramJsonObj = new JSONObject();
                    for (String param : params) {
                        if (param.indexOf("ticketKey") == -1) {
                            String[] split = param.split("=");
                            paramJsonObj.put(split[0], split[1]);
                        }
                    }
                    nextDataUrl += "&"
                            + OPEN_PARAM
                            + "="
                            + SecurityUtils.encryptAESAndBase64URLSafe(paramJsonObj.toString(),
                                    developer.getTicketKey());
                }
                json.put("nextDataUri", nextDataUrl);
                if (Evn.isDevModel()) {
                    System.out.println(json);
                }
            }
            if (Evn.isDevModel()) {
                log.info("---------获取数据耗时：" + ((System.currentTimeMillis() - time)) + "ms");
                // System.out.println("---------获取数据耗时：" + ((System.currentTimeMillis() - time)) + "ms");
            }
        }

        catch (Exception e) {
            log.error(e.getMessage(), e);
            String errorMsg = e.getMessage();
            if (StringUtils.isNotBlank(errorMsg)) {
                try {
                    returnOpenJsonObj(json, -1, URLEncoder.encode(errorMsg, "utf-8"));
                }
                catch (UnsupportedEncodingException e1) {
                    returnOpenJsonObj(json, -1, "获取数据错误！");
                }
            }
            return json;
        }
        return json;
    }

    /**
     * 根据url处理参数
     * 
     * @param uri
     * @return
     */
    @SuppressWarnings("unchecked")
    protected Map<String, OpenApiParamDto> dealParam(String uri, HttpServletRequest request) {
        Map<String, List<OpenApiParameter>> params = openApiParameterService.findParameters(uri);
        Map<String, String> map = new HashMap<String, String>();
        List<OpenApiParameter> ps = params.get(uri);
        if (ps == null) {
            return new HashMap<String, OpenApiParamDto>();
        }
        for (OpenApiParameter param : ps) {
            map.put(param.getParamName(), param.getParamColumnName());
        }
        Set<String> keys = request.getParameterMap().keySet();
        Map<String, String> parMap = new HashMap<String, String>();
        for (String key : keys) {
            parMap.put(key, request.getParameter(key));
        }
        return dealParam(map, parMap,request);
    }

    protected Map<String, OpenApiParamDto> dealParam(String uri, JSONObject json) {
        Map<String, List<OpenApiParameter>> params = openApiParameterService.findParameters(uri);
        Map<String, String> map = new HashMap<String, String>();
        List<OpenApiParameter> ps = params.get(uri);
        if (ps == null) {
            return new HashMap<String, OpenApiParamDto>();
        }
        for (OpenApiParameter param : ps) {
            map.put(param.getParamName(), param.getParamColumnName());
        }
        Map<String, String> parMap = new HashMap<String, String>();
        if (json != null) {
            parMap = SUtils.dt(json.toJSONString(), new TR<Map<String, String>>() {
            });
        }
        return dealParam(map, parMap, null);
    }

    /**
     * 处理数据获取参数
     * 
     * @param map
     * @return
     */
    protected Map<String, OpenApiParamDto> dealParam(Map<String, String> map, Map<String, String> request, HttpServletRequest servletRequest) {
        Map<String, OpenApiParamDto> paramMap = new HashMap<String, OpenApiParamDto>();

        Set<String> keys = request.keySet();
        OpenApiParamDto oapDto = null;
        for (String key : keys) {
            oapDto = new OpenApiParamDto();
            oapDto.setParamName(key);
            oapDto.setValue(request.get(key));
            if (map != null && map.get(key) != null) {
                String v = request.get(key);
                if (StringUtils.isNotBlank(v)) {
                    paramMap.put(map.get(key), oapDto);
                }
            }
            else {
                if (StringUtils.startsWith(key, "TAB_COL_")) {
                    paramMap.put(StringUtils.substringAfter(key, "TAB_COL_"), oapDto);
                }
                else {
                    paramMap.put("_" + key, oapDto);
                }
            }
        }

        String dataModifyTime = request.get(OpenapiParamEnum.DATA_MODIFY_TIME.getName());
        String dataSortMaxId = request.get(OpenapiParamEnum.DATA_SORT_MAX_ID.getName());
        String realtime = request.get(OpenapiParamEnum.REALTIME.getName());
        String limit = request.get(OpenapiParamEnum.LIMIT.getName());
        String containAdmin = request.get(OpenapiParamEnum.CONTAIN_ADMIN.getName());
        String page = request.get(OpenapiParamEnum.PAGE.getName());
        String showNull = request.get(OpenapiParamEnum.SHOW_NULL.getName());
        String isDeleted = request.get(OpenapiParamEnum.IS_DELETED.getName());
        Integer maxLimit = (Integer) servletRequest.getAttribute(OpenapiParamEnum.MAX_LIMIT.getName());
        if (maxLimit != null && (StringUtils.isBlank(limit) || NumberUtils.toInt(limit) > maxLimit)) {
            limit = String.valueOf(maxLimit);
        }
        if (StringUtils.isBlank(page)) {
            page = "1";
        }
        if (NumberUtils.toInt(limit) > 1000) {
            limit = "1000";
        }

        oapDto = new OpenApiParamDto();
        oapDto.setParamName(OpenapiParamEnum.IS_DELETED.getName());
        oapDto.setValue(isDeleted);
        paramMap.put("_isDeleted", oapDto);

        oapDto = new OpenApiParamDto();
        oapDto.setParamName(OpenapiParamEnum.LIMIT.getName());
        oapDto.setValue(limit);
        paramMap.put("_limit", oapDto);

        oapDto = new OpenApiParamDto();
        oapDto.setParamName(OpenapiParamEnum.PAGE.getName());
        oapDto.setValue(page);
        paramMap.put("_page", oapDto);

        oapDto = new OpenApiParamDto();
        oapDto.setParamName(OpenapiParamEnum.CONTAIN_ADMIN.getName());
        oapDto.setValue(containAdmin);
        paramMap.put("_containAdmin", oapDto);

        oapDto = new OpenApiParamDto();
        oapDto.setParamName(OpenapiParamEnum.DATA_MODIFY_TIME.getName());
        oapDto.setValue(dataModifyTime);
        paramMap.put("_dataModifyTime", oapDto);

        oapDto = new OpenApiParamDto();
        oapDto.setParamName(OpenapiParamEnum.DATA_SORT_MAX_ID.getName());
        oapDto.setValue(dataSortMaxId);
        paramMap.put("_dataSortMaxId", oapDto);

        oapDto = new OpenApiParamDto();
        oapDto.setParamName(OpenapiParamEnum.REALTIME.getName());
        oapDto.setValue(realtime);
        paramMap.put("_realtime", oapDto);

        oapDto = new OpenApiParamDto();
        oapDto.setParamName(OpenapiParamEnum.SHOW_NULL.getName());
        oapDto.setValue(showNull);
        paramMap.put("_showNull", oapDto);

        return paramMap;
    }

    @SuppressWarnings("unchecked")
    protected Map<String, String> validateColumns(Developer ticketKey, String type) {
        Map<String, String> map = new HashMap<String, String>();
        String key = OPEN_API_KEY + ".VALIDATE.COLUMN." + ticketKey + "." + type;
        String json = RedisUtils.get(key);
        if (StringUtils.isBlank(json)) {
            Map<String, List<OpenApiEntity>> entityMap = openApiEntityService.findEntities(ticketKey, type);
            List<OpenApiEntity> openEntities = entityMap.get(type);
            if (openEntities == null) {
                return new HashMap<String, String>();
            }
            int index = 0;
            for (OpenApiEntity entity : openEntities) {
                map.put(entity.getEntityColumnName().toUpperCase(), entity.getEntityName() + "," + (index++) + ","
                        + entity.getMcodeId());
            }
            RedisUtils.set(key, JSON.toJSONString(map), getCacheTime());
        }
        else {
            map = JSON.parseObject(json, Map.class);
        }
        return map;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected void dealDatas(JSONObject jsonNode, Map<String, String> paramMap, List entities, Developer developer,
            Map<String, String> columnMap) {
        // long time = System.currentTimeMillis();
        JSONArray data = new JSONArray();
        int count = 0;
        for (int i = 0; i < entities.size() - 1; i++) {
            Object entity = entities.get(i);
            if (entity instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) entity;
                Map<String, String> map2 = new HashMap<String, String>();
                JSONObject jsonEntity = new JSONObject();
                Set<String> set = map.keySet();
                for (String key : set) {
                    String attributeNames = columnMap.get(key);
                    if (attributeNames == null) {
                        continue;
                    }
                    String[] attrs = attributeNames.split(",");
                    map2.put(attrs[1], attrs[0] + "," + key + "," + attrs[2]);
                }
                for (int index = 0; index < columnMap.size(); index++) {
                    String key2 = map2.get(index + "");
                    if (StringUtils.isBlank(key2)) {
                        continue;
                    }
                    String[] keys = key2.split(",");
                    Object o = map.get(keys[1]);
                    if (o != null) {
                        if (o instanceof Date) {
                            String pattern = keys[2];
                            if (StringUtils.isBlank(pattern) || StringUtils.equals("null", pattern)) {
//                                pattern = "yyyy-MM-dd";
                                pattern = BaseOpenapiConstant.BASE_UPDATE_SIMPLE_DATE_FORMAT;
                            }
                            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                            o = sdf.format(o);
                        }
                        else if (o instanceof TIMESTAMP) {
                            String pattern = keys[2];
                            if (StringUtils.isBlank(pattern) || StringUtils.equals("null", pattern)) {
//                                pattern = "yyyy-MM-dd";
                                pattern = BaseOpenapiConstant.BASE_UPDATE_SIMPLE_DATE_FORMAT;
                            }
                            try {
//                                Date date = TIMESTAMP.toDate(((TIMESTAMP) o).getBytes());
                            	Timestamp timestamp = TIMESTAMP.toTimestamp(((TIMESTAMP) o).getBytes());
                                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                                o = sdf.format(timestamp);
                            }
                            catch (Exception e) {
                            }
                        }
                        else{
                        	String pattern = keys[2];
                        	if(StringUtils.equalsIgnoreCase("md5", pattern)){
                        		o = DigestUtils.md5Hex(o.toString());
                        	}
                        	else if (StringUtils.equalsIgnoreCase("sha", pattern)){
                        		o = DigestUtils.shaHex(o.toString());
                        	}
                        	else if (StringUtils.equalsIgnoreCase("md5sha", pattern)){
                        		o = DigestUtils.md5Hex(DigestUtils.shaHex(o.toString()));
                        	}
                        	else if(StringUtils.equalsIgnoreCase("pwdmd5", pattern)){
                        		o = DigestUtils.md5Hex(PWD.decode(o.toString()));
                        	}
                        	else if (StringUtils.equalsIgnoreCase("pwdsha", pattern)){
                        		o = DigestUtils.shaHex(PWD.decode(o.toString()));
                        	}
                        	else if (StringUtils.equalsIgnoreCase("pwdmd5sha", pattern)){
                        		o = DigestUtils.md5Hex(DigestUtils.shaHex(PWD.decode(o.toString())));
                        	}
                        }
                    }
                    if (StringUtils.equals(paramMap.get("_showNull"), "0")) {
                        if (o == null) {
                            continue;
                        }
                    }
                    jsonEntity.put(keys[0], o == null ? "" : o.toString());
                }
                data.add(jsonEntity);
            }
            else {
                String jsonStr = JSON.toJSONString(entity);
                data.add(jsonStr);
            }
            count++;
        }
        jsonNode.put("data", data);
        jsonNode.put("dataCount", count);
        JSONObject jsonPage = new JSONObject();
        if (entities.size() > 1) {
            Map<String, Object> page = (Map<String, Object>) entities.get(entities.size() - 1);
            // jsonPage.put("currentPage", String.valueOf(page.get("_page")));
            jsonPage.put("maxPage", String.valueOf(page.get("_maxPage")));
            jsonPage.put("totalDataCount", String.valueOf(page.get("_maxRow")));
            jsonNode.put("pagination", jsonPage);
        }
    }

    /**
     * 
     * @param json
     * @param result
     *            -1失败，1成功
     * @param msg
     * @return
     */
    protected JSONObject returnOpenJsonObj(JSONObject json, int result, String msg) {
        JSONObject jsonObj = null;
        if (json != null) {
            jsonObj = json;
        }
        else {
            jsonObj = new JSONObject();
        }
        jsonObj.put("result", result);
        jsonObj.put("message", msg);
        return jsonObj;
    }

    protected String returnOpenJsonError(String msg) {
        return returnOpenJsonObj(null, -1, msg).toString();
    }

    protected String returnOpenJsonError(int code, String msg) {
        return returnOpenJsonObj(null, code, msg).toString();
    }

    protected String returnOpenJsonObjSuccess(JSONObject json, String msg) {
        return returnOpenJsonObj(json, 1, msg).toString();
    }

    /**
     * 20和21都有调用
     * 
     * @param type
     * @param request
     * @param nextUri
     * @param innerOpen
     * @return
     */
    public String qd(String type, final HttpServletRequest request, String nextUri, InnerOpenDto innerOpen) {
        String uri = "/openapi/v2.0/" + type;
        OpenApiInterface oi = openApiInterfaceService.findByUri(uri);
        if (oi != null) {
            Map<String, OpenApiParamDto> paramDtoMap;
            Developer developer = null;
            if (innerOpen != null) {
                developer = innerOpen.getDeveloper();
                paramDtoMap = dealParam(uri, innerOpen.getParamJsonObj());
            }
            else {
                developer = getDeveloperByRequest(request);
                paramDtoMap = dealParam(uri, request);
            }
            OpenApiParamDto oapDto = new OpenApiParamDto();
            oapDto.setParamName("sourceType");
			oapDto.setValue(oi.getSourceType());
            paramDtoMap.put("sourceType", oapDto);
            if("2".equals(oi.getSourceType())){
            	nextUri = "/openapi/v2.0/sync/" + type;
            }
            //把授权的单位放在参数列表中
            doDeveloperPower(type, paramDtoMap, developer, oi.getUnitColumnName());
            
            JSONObject json = queryData(uri, "queryData", oi.getTableName(), oi.getResultType(), paramDtoMap, request,
                    nextUri, developer, (innerOpen != null ? false : true));
            int result = JsonUtils.getInt(json, "result");
            if (result == 0 || result == 1) {
                if (innerOpen != null) {
                    JSONObject mainjson = new JSONObject();
                    json.remove("result");
                    mainjson.put("maindata",
                            SecurityUtils.encryptAESAndBase64(json.toString(), developer.getTicketKey()));
                    return returnOpenJsonObjSuccess(mainjson, "ok").toString();
                }
                else {
                    return returnOpenJsonObjSuccess(json, "ok").toString();
                }
            }
            else {
                return returnOpenJsonError(json.getString("message")).toString();
            }
        }
        return returnOpenJsonError("未知");
    }

	/**
     * 20和21都有调用
     * 
     * @param type
     * @param id
     * @param request
     * @param nextUri
     * @param innerOpen
     * @return
     */
    public String qd(String type, String id, HttpServletRequest request, String nextUri, InnerOpenDto innerOpen) {
        String uri = "/openapi/v2.0/" + type + "/{id}";
        OpenApiInterface oi = openApiInterfaceService.findByUri(uri);
        if (oi != null) {
            Map<String, OpenApiParamDto> paramDtoMap;
            Developer developer = null;
            if (innerOpen != null) {
                developer = innerOpen.getDeveloper();
                paramDtoMap = dealParam(uri, innerOpen.getParamJsonObj());
            }
            else {
                developer = getDeveloperByRequest(request);
                paramDtoMap = dealParam(uri, request);
            }
            OpenApiParamDto oapDto = new OpenApiParamDto();
            oapDto.setParamName("id");
            oapDto.setValue(id);
            paramDtoMap.put("id", oapDto);
            //把授权的单位放在参数列表中
            doDeveloperPower(type, paramDtoMap, developer,oi.getUnitColumnName());
            
            JSONObject json = queryData(uri, "queryDataWithId", oi.getTableName(), oi.getResultType(), paramDtoMap,
                    request, nextUri, developer, (innerOpen != null ? false : true));
            int result = JsonUtils.getInt(json, "result");
            if (result == 0 || result == 1) {
                if (innerOpen != null) {
                    JSONObject mainjson = new JSONObject();
                    json.remove("result");
                    mainjson.put("maindata",
                            SecurityUtils.encryptAESAndBase64(json.toString(), developer.getTicketKey()));
                    return returnOpenJsonObjSuccess(mainjson, "ok").toString();
                }
                else {
                    return returnOpenJsonObjSuccess(json, "ok").toString();
                }
            }
            else {
                return returnOpenJsonError(json.getString("message")).toString();
            }
        }
        return returnOpenJsonError("未知");
    }

    /**
     * 20和21都有调用
     * 
     * @param type
     * @param id
     * @param subType
     * @param request
     * @param nextUri
     * @param innerOpen
     * @return
     */
    public String qd(String type, String id, String subType, HttpServletRequest request, String nextUri,
            InnerOpenDto innerOpen) {
        String uri = "/openapi/v2.0/" + type + "/{id}/" + subType;
        OpenApiInterface oi = openApiInterfaceService.findByUri(uri);
        if (oi != null) {
            String fpkName = oi.getFpkColumnName();
            if (StringUtils.isNotBlank(fpkName)) {
                if (StringUtils.contains(fpkName, ".")) {
                    String[] fs = fpkName.split("\\.");
                    String uri2 = "/openapi/v2.0/" + type + "/{id}";
                    Map<String, OpenApiParamDto> paramDtoMap;
                    Developer developer = null;
                    if (innerOpen != null) {
                        developer = innerOpen.getDeveloper();
                        paramDtoMap = dealParam(uri2, innerOpen.getParamJsonObj());
                    }
                    else {
                        developer = getDeveloperByRequest(request);
                        paramDtoMap = dealParam(uri2, request);
                    }
                    OpenApiInterface oi2 = openApiInterfaceService.findByUri(uri2);
                    OpenApiParamDto oapDto = new OpenApiParamDto();
                    oapDto.setParamName("id");
                    oapDto.setValue(id);
                    paramDtoMap.put("id", oapDto);
                    //把授权的单位放在参数列表中
                    doDeveloperPower(type, paramDtoMap, developer, oi.getUnitColumnName());
                    
                    JSONObject json = queryData(uri2, "queryDataWithId", oi2.getTableName(), oi2.getResultType(),
                            paramDtoMap, request, nextUri, developer, (innerOpen != null ? false : true));
                    JSONArray array = json.getJSONArray("data");
                    String fid = null;
                    if (array.size() > 0) {
                        fid = array.getJSONObject(0).getString(fs[1]);
                    }
                    if (innerOpen != null) {
                        paramDtoMap = dealParam(uri, innerOpen.getParamJsonObj());
                    }
                    else {
                        paramDtoMap = dealParam(uri, request);
                    }
                    oapDto = new OpenApiParamDto();
                    oapDto.setParamName("id");
                    oapDto.setValue(fid);
                    paramDtoMap.put("id", oapDto);
                    json = queryData(uri, "queryData" + subType, oi.getTableName(), oi.getResultType(), paramDtoMap,
                            request, nextUri, developer, (innerOpen != null ? false : true));
                    int result = JsonUtils.getInt(json, "result");
                    if (result == 0 || result == 1) {
                        if (innerOpen != null) {
                            JSONObject mainjson = new JSONObject();
                            json.remove("result");
                            mainjson.put("maindata",
                                    SecurityUtils.encryptAESAndBase64(json.toString(), developer.getTicketKey()));
                            return returnOpenJsonObjSuccess(mainjson, "ok").toString();
                        }
                        else {
                            return returnOpenJsonObjSuccess(json, "ok").toString();
                        }
                    }
                    else {
                        return returnOpenJsonError(json.getString("message")).toString();
                    }
                }
                else {
                    Map<String, OpenApiParamDto> paramDtoMap;
                    Developer developer = null;
                    if (innerOpen != null) {
                        developer = innerOpen.getDeveloper();
                        paramDtoMap = dealParam(uri, innerOpen.getParamJsonObj());
                    }
                    else {
                        String ticketKey = request.getHeader("ticketKey");
                        if (StringUtils.isBlank(ticketKey)) {
                            ticketKey = request.getParameter("ticketKey");
                        }
                        developer = developerService.findByTicketKey(ticketKey);
                        paramDtoMap = dealParam(uri, request);
                    }
                    OpenApiParamDto oapDto = new OpenApiParamDto();
                    oapDto.setParamName(fpkName);
                    oapDto.setValue(id);
                    paramDtoMap.put(fpkName, oapDto);
                    
                    doDeveloperPower(subType, paramDtoMap, developer, oi.getUnitColumnName());
                    
                    uri = StringUtils.replace(uri, "{id}", id);
                    JSONObject json = queryData(uri, "queryData" + subType, oi.getTableName(), oi.getResultType(),
                            paramDtoMap, request, nextUri, developer, (innerOpen != null ? false : true));
                    int result = JsonUtils.getInt(json, "result");
                    if (result == 0 || result == 1) {
                        if (innerOpen != null) {
                            JSONObject mainjson = new JSONObject();
                            json.remove("result");
                            mainjson.put("maindata",
                                    SecurityUtils.encryptAESAndBase64(json.toString(), developer.getTicketKey()));
                            return returnOpenJsonObjSuccess(mainjson, "ok").toString();
                        }
                        else {
                            return returnOpenJsonObjSuccess(json, "ok").toString();
                        }
                    }
                    else {
                        return returnOpenJsonError(json.getString("message")).toString();
                    }
                }
            }
            return returnOpenJsonError("未找到");
        }
        return returnOpenJsonError("未知");
    }

    protected int getCacheTime() {
        return 60;
    }
    
    
    /**
	 * @param type
	 * @param paramDtoMap
	 * @param developer
	 */
	private void doDeveloperPower(String type, Map<String, OpenApiParamDto> paramDtoMap, Developer developer) {
		doDeveloperPower(type,paramDtoMap,developer,null);
	}
    
    
    /**
	 * 进行开发者权限单位的查找
	 * @param type
	 * @param paramDtoMap
	 * @param developer
	 * @param oapDto
	 */
	private void doDeveloperPower(String type, Map<String, OpenApiParamDto> paramDtoMap, Developer developer,String fpkName) {
		//判断是否走授权
		if(StringUtils.isNotBlank(fpkName)){
			List<DeveloperPower> developerPowers = developerPowerService.findByDeveloperId(developer.getId());
			if(CollectionUtils.isNotEmpty(developerPowers)) {
				OpenApiParamDto oapDto = new OpenApiParamDto();
				Set<String> unitIdList = EntityUtils.getSet(developerPowers, DeveloperPower::getUnitId);
				StringBuilder valueList = new StringBuilder();
				//是否是 TEACHCLASSSTUDENT
				if(InterfaceTypeEnum.TEACHCLASSSTUDENT.getName().equals(type)){
					List<TeachClass> allTeachClasses = SUtils.dt(teachClassRemoteService.findbyUnitIdIn(unitIdList.toArray(new String[unitIdList.size()])), 
							TeachClass.class);
					if(CollectionUtils.isNotEmpty(allTeachClasses)){
						allTeachClasses.forEach(c->{
							valueList.append(",");
							valueList.append(c.getId());
						});
					}
				}else{
					unitIdList.forEach(c->{
						valueList.append(",");
						valueList.append(c);
					});
					//是否是学科类型 0--否 1--是
					if(InterfaceTypeEnum.SUBJECT.getName().equals(type)){
						OpenApiParamDto dto = paramDtoMap.get(OpenApiConstants.IS_GET_TOP);
						if(!(dto != null && dto.getValue().equals(OpenApiConstants.BOOLEAN_FALSE_VAL))){
							Unit unit = unitRemoteService.findTopUnitObject(null);
							valueList.append(","+unit.getId());
						}
					}
				}
				oapDto.setParamName(fpkName);
				oapDto.setValue(valueList.toString().replaceFirst(",", ""));
				paramDtoMap.put(oapDto.getParamName(), oapDto);
			}
		}
	}
	
    protected List<OpenApiApply> getTypes(List<String> types) {
        return getTypes(types,null);
    }
	
    protected List<OpenApiApply> getTypes(List<String> types,Integer dataType) {
        boolean isNull = CollectionUtils.isEmpty(types);
        List<OpenApiApply> list = new ArrayList<>();
        List<String[]> resultTypeList = getResultTypes(dataType);
        for(Object[] type : resultTypeList) {
        	 OpenApiApply apply = new OpenApiApply();
             apply.setType(String.valueOf(type[0]));
             apply.setTypeNameValue(String.valueOf(type[1]));
             apply.setStatus(isNull ? ApplyStatusEnum.ALL.getValue()
                     : types.contains(String.valueOf(type[0])) ? ApplyStatusEnum.PASS_VERIFY.getValue() : ApplyStatusEnum.ALL
                             .getValue());
             list.add(apply);
        }
        return list;
    }
    
    protected List<String[]> getResultTypes (Integer dataType){
    	List<String[]> resultTypes = new ArrayList<>();
    	Integer[] types = {OpenApiInterfaceType.RESULT_TYPE,OpenApiInterfaceType.PUBLIC_TYPE};
		List<OpenApiInterfaceType>  interfaceTypes = openApiInterfaceTypeService.findByClassifyIn(types);
        Map<String, String> resultTypeMap = EntityUtils.getMap(interfaceTypes, OpenApiInterfaceType::getType, OpenApiInterfaceType::getTypeName);
        List<String> resultTypeList = openApiInterfaceService.findDistinctResultTypeByDataType(dataType);
    	for (String type : resultTypeList) {
    		String[] types1 = {type,resultTypeMap.get(type)};
    		resultTypes.add(types1);
		}
        return resultTypes;
    }
    
    
    
    /**
	 * 验证单位权限
	 * @param request
	 * @param unitId
	 * @return
	 */
    protected boolean doPowerProving(HttpServletRequest request, String unitId) {
		//判断是否有权限查找课表信息
        Developer developer = getDeveloperByRequest(request);
		List<DeveloperPower> developerPowers = developerPowerService.findByDeveloperId(developer.getId());
		if(CollectionUtils.isNotEmpty(developerPowers)) {
			Set<String> unitIdList = EntityUtils.getSet(developerPowers, DeveloperPower::getUnitId);
			return unitIdList.contains(unitId);
		}
		return false;
	}
	
    protected Set<String> getPowerUnitSet(HttpServletRequest request) {
		//判断是否有权限查找课表信息
        Developer developer = getDeveloperByRequest(request);
		List<DeveloperPower> developerPowers = developerPowerService.findByDeveloperId(developer.getId());
		if(CollectionUtils.isNotEmpty(developerPowers)) {
			return EntityUtils.getSet(developerPowers, DeveloperPower::getUnitId);
		}
		return null;
	}
    
    protected Semester getCurrentSemester() {
  		String remoteSemesterKey4Hours = "ngke.remote.semester.4hour";
  		Semester semester = Semester
  				.dc(RedisUtils.get(remoteSemesterKey4Hours, RedisUtils.TIME_ONE_HOUR * 4, new RedisInterface<String>() {
  					@Override
  					public String queryData() {
  						return semesterRemoteServicer.getCurrentSemester(SemesterRemoteService.RET_PRE);
  					}
  				}));
  		return semester;
  	}
    
    
    //TODO--------------------------------------------------私有方法区 ------------------
    private String getTicketKey(HttpServletRequest request) {
    	String ticketKey = request.getHeader("ticketKey");
        if (StringUtils.isBlank(ticketKey)) {
            ticketKey = request.getParameter("ticketKey");
        }
		return ticketKey;
	}
    
    private Date getDateBySimple(Date date,String pattern) {
		return DateUtils.string2Date(DateUtils.date2String(date,pattern),pattern);
	}
    
    private boolean isOverMaxNumDay(String type, String ticketKey , int maxNum){
    	Date startTime = getDateBySimple(new Date(),"yyyy-MM-dd");// 定义起始日期
        Date endTime = DateUtils.addDay(new Date(), 1);// 定义结束日期
        endTime = getDateBySimple (endTime,"yyyy-MM-dd");
	    List<OpenApiInterfaceCount> operationLogs = openApiInterfaceCountService.findDoInterfaceNum(ticketKey,type,startTime,endTime);
	    int hasCount = 0;
	    if(CollectionUtils.isNotEmpty(operationLogs)){
	    	hasCount = operationLogs.size();
	    }
	    return hasCount >= maxNum;
    }
}
