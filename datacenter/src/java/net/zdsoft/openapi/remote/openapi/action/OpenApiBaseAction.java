package net.zdsoft.openapi.remote.openapi.action;

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

import net.zdsoft.base.constant.BaseDataCenterConstant;
import net.zdsoft.base.dto.InnerOpenDto;
import net.zdsoft.base.dto.OpenApiParamDto;
import net.zdsoft.base.entity.eis.Developer;
import net.zdsoft.base.entity.eis.OpenApiApply;
import net.zdsoft.base.entity.eis.OpenApiApplyPower;
import net.zdsoft.base.entity.eis.OpenApiEntity;
import net.zdsoft.base.entity.eis.OpenApiInterface;
import net.zdsoft.base.entity.eis.OpenApiInterfaceCount;
import net.zdsoft.base.entity.eis.OpenApiInterfaceType;
import net.zdsoft.base.entity.eis.OpenApiParameter;
import net.zdsoft.base.enums.ApplyStatusEnum;
import net.zdsoft.base.enums.OpenapiParamEnum;
import net.zdsoft.base.service.BaseCommonService;
import net.zdsoft.base.service.DeveloperService;
import net.zdsoft.base.service.OpenApiApplyPowerService;
import net.zdsoft.base.service.OpenApiApplyService;
import net.zdsoft.base.service.OpenApiEntityService;
import net.zdsoft.base.service.OpenApiInterfaceCountService;
import net.zdsoft.base.service.OpenApiInterfaceService;
import net.zdsoft.base.service.OpenApiInterfaceTypeService;
import net.zdsoft.base.service.OpenApiParameterService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.JsonUtils;
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.SecurityUtils;
import net.zdsoft.openapi.pushjob.constant.BaseOpenapiConstant;
import net.zdsoft.openapi.remote.openapi.constant.OpenApiConstants;
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
    protected Logger log = Logger.getLogger(OpenApiBaseAction.class);

    @Autowired
    BaseCommonService baseCommonService;
    @Autowired
    OpenApiParameterService openApiParameterService;
    @Autowired
    OpenApiEntityService openApiEntityService;
    @Autowired
    OpenApiInterfaceService openApiInterfaceService;
    @Autowired
    DeveloperService developerService;
    @Autowired
    OpenApiInterfaceTypeService openApiInterfaceTypeService;
    @Autowired
    OpenApiApplyService openApiApplyService;
    @Autowired
    OpenApiInterfaceCountService openApiInterfaceCountService;
    @Autowired
    OpenApiApplyPowerService openApiApplyPowerService;
    
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
     * @param interfaceId
     * @param methodName
     * @param tableName
     * @param resultType
     * @param paramMap
     * @param openTicketKey
     * @param isOld
     *            是否2.0
     * @return
     */
    public JSONObject queryData(String interfaceId, String methodName, final String tableName, String resultType,
            Map<String, OpenApiParamDto> paramDtoMap, HttpServletRequest request, String nextUri, Developer developer,
            boolean isOld) {
        final Map<String, String> paramMap = new HashMap<String, String>();
        for (String key : paramDtoMap.keySet()) {
            paramMap.put(key, paramDtoMap.get(key).getValue());
        }
        JSONObject json = new JSONObject();
        try {
            long time = System.currentTimeMillis();
            final Map<String, String> columnMap = validateColumns(developer, resultType, interfaceId);
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
     * 根据接口id来得到参数
     * 
     * @param interfaceId
     * @return
     */
    @SuppressWarnings("unchecked")
    protected Map<String, OpenApiParamDto> dealParam(String interfaceId, HttpServletRequest request) {
        Map<String, List<OpenApiParameter>> params = openApiParameterService.findParameters(interfaceId);
        Map<String, String> map = new HashMap<String, String>();
        List<OpenApiParameter> ps = params.get(interfaceId);
        if (CollectionUtils.isEmpty(ps)) {
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
    protected Map<String, String> validateColumns(Developer ticketKey, String type, String interfaceId) {
        Map<String, String> map = new HashMap<String, String>();
        String key = OPEN_API_KEY + ".VALIDATE.COLUMN." + ticketKey + "." + type;
        String json = RedisUtils.get(key);
        if (StringUtils.isBlank(json)) {
            Map<String, List<OpenApiEntity>> entityMap = openApiEntityService.findEntities(ticketKey,interfaceId, type);
            List<OpenApiEntity> openEntities = entityMap.get(type);
            if (openEntities == null) {
                return new HashMap<String, String>();
            }
            int index = 0;
            for (OpenApiEntity entity : openEntities) {
                map.put(entity.getEntityColumnName().toUpperCase(), entity.getEntityName() + "," + (index++) + ","
                        + (StringUtils.isBlank(entity.getMcodeId()) ? null : entity.getMcodeId()));
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
    public String qd(String type, final HttpServletRequest request, String nextUri) {
        String uri = "/openapi/v3.0/" + type;
        OpenApiInterface oi = openApiInterfaceService.findByUri(uri);
        if (oi != null) {
            Map<String, OpenApiParamDto> paramDtoMap;
            Developer developer = getDeveloperByRequest(request);
            //把授权的单位放在参数列表中
            paramDtoMap = dealParam(oi.getId(), request);
            doDeveloperPower(oi.getId(), paramDtoMap,developer.getTicketKey(),type);
            JSONObject json = queryData(oi.getId(), "queryData", oi.getTableName(), oi.getResultType(), paramDtoMap, request,
                    nextUri, developer, true);
            int result = JsonUtils.getInt(json, "result");
            if (result == 0 || result == 1) {
                if (isSecurity()) {
                	json.put("data",
                            SecurityUtils.encryptAESAndBase64(json.getString("data"), developer.getTicketKey()));
                    return returnOpenJsonObjSuccess(json, "ok").toString();
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
     * 返回的结果是否加密
     */
    private boolean isSecurity(){
    	return Evn.getBoolean(BaseDataCenterConstant.IS_SECURITY);
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
//            doDeveloperPower(type, paramDtoMap, developer, null);
            
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
            String fpkName = null;
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
//                    doDeveloperPower(type, paramDtoMap, developer, null);
                    
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
	 * 进行开发者权限单位的查找
	 * @param type
	 * @param paramDtoMap
	 * @param developer
	 * @param oapDto
	 */
	private void doDeveloperPower(String interfaceId, Map<String, OpenApiParamDto> paramDtoMap, String ticketKey,String type) {
		List<OpenApiApplyPower> allPowers = openApiApplyPowerService.findByTicketKeyAndInterfaceIdOrType(ticketKey,interfaceId,type);
		if(CollectionUtils.isNotEmpty(allPowers)){
			allPowers.forEach(c->{
				OpenApiParamDto oapDto = new OpenApiParamDto();
				oapDto.setParamName("in_" + c.getPowerColumnName());
				oapDto.setValue(c.getPowerValue());
				paramDtoMap.put(oapDto.getParamName(), oapDto);
			});
		}
	}
	
    protected List<OpenApiApply> getTypes(List<String> types) {
        return getTypes(types,null);
    }
	
    protected List<OpenApiApply> getTypes(List<String> types,Integer dataType) {
        boolean isNull = CollectionUtils.isEmpty(types);
        List<OpenApiApply> list = new ArrayList<>();
        List<String[]> resultTypeList = getInterfaceTypes(dataType);
        for(Object[] type : resultTypeList) {
        	 OpenApiApply apply = new OpenApiApply();
             apply.setType(String.valueOf(type[0]));
             apply.setTypeNameValue(String.valueOf(type[1]));
             apply.setStatus(isNull ? ApplyStatusEnum.ALL.getValue()
                     : types.contains(String.valueOf(type[0])) ? ApplyStatusEnum.PASS_VERIFY.getValue() : ApplyStatusEnum.ALL
                             .getValue());
             apply.setIsDeleted(BaseDataCenterConstant.BOOLEAN_FALSE_VAL);
             list.add(apply);
        }
        return list;
    }
    
    protected List<String[]> getInterfaceTypes (Integer dataType){
    	List<String[]> resultTypes = new ArrayList<>();
    	Integer[] types = {OpenApiInterfaceType.INTERFACE_TYPE,OpenApiInterfaceType.PUBLIC_TYPE};
		List<OpenApiInterfaceType>  interfaceTypes = openApiInterfaceTypeService.findByClassifyIn(types);
        Map<String, String> resultTypeMap = EntityUtils.getMap(interfaceTypes, OpenApiInterfaceType::getType, OpenApiInterfaceType::getTypeName);
        List<String> typeList = openApiInterfaceService.findDistinctTypeByDataType(dataType);
    	for (String type : typeList) {
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
//		List<DeveloperPower> developerPowers = developerPowerService.findByDeveloperId(developer.getId());
//		if(CollectionUtils.isNotEmpty(developerPowers)) {
//			Set<String> unitIdList = EntityUtils.getSet(developerPowers, DeveloperPower::getUnitId);
//			return unitIdList.contains(unitId);
//		}
		return false;
	}
	
    protected Set<String> getPowerUnitSet(HttpServletRequest request) {
		//判断是否有权限查找课表信息
        Developer developer = getDeveloperByRequest(request);
//		List<DeveloperPower> developerPowers = developerPowerService.findByDeveloperId(developer.getId());
//		if(CollectionUtils.isNotEmpty(developerPowers)) {
//			return EntityUtils.getSet(developerPowers, DeveloperPower::getUnitId);
//		}
		return null;
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
