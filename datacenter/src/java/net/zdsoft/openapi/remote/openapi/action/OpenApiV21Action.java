//package net.zdsoft.remote.openapi.action;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.alibaba.fastjson.TypeReference;
//
//import net.zdsoft.basedata.entity.User;
//import net.zdsoft.basedata.remote.service.UserRemoteService;
//import net.zdsoft.framework.annotation.ControllerInfo;
//import net.zdsoft.framework.config.Evn;
//import net.zdsoft.framework.entity.TR;
//import net.zdsoft.framework.utils.RedisInterface;
//import net.zdsoft.framework.utils.RedisUtils;
//import net.zdsoft.framework.utils.SUtils;
//import net.zdsoft.framework.utils.SecurityUtils;
//import net.zdsoft.framework.utils.ServletUtils;
//import net.zdsoft.remote.openapi.dto.InnerOpenDto;
//import net.zdsoft.remote.openapi.entity.Developer;
//import net.zdsoft.system.entity.server.Model;
//import net.zdsoft.system.entity.server.Server;
//import net.zdsoft.system.entity.server.SubSystem;
//import net.zdsoft.system.remote.service.ModelRemoteService;
//import net.zdsoft.system.remote.service.ServerRemoteService;
//import net.zdsoft.system.remote.service.SubSystemRemoteService;
//import net.zdsoft.system.remote.service.SysOptionRemoteService;
//
//@RequestMapping(value = { "/remote/openapi", "/openapi" })
//public class OpenApiV21Action extends OpenApiBaseAction {
//
//    @Autowired
//    SubSystemRemoteService subSystemRemoteService;
//    @Autowired
//    ModelRemoteService modelRemoteService;
//    @Autowired
//    ServerRemoteService serverRemoteService;
//    @Autowired
//    SysOptionRemoteService sysOptionRemoteService;
//
//    // ------------------------------------
//    // 基础数据接口
//
//    @ResponseBody
//    @RequestMapping(value = "/v2.1/basedata/queryData/{type}")
//    @ControllerInfo(value = "开发者调用接口：/v2.1/basedata/queryData/{type}",parameter = "{ticketKey}")
//    public String queryDataV21(@PathVariable String type, HttpServletRequest request) {
//        InnerOpenDto innerOpen = checkParam(request, "/openapi/v2.0/" + type);
//        if (!innerOpen.isPass()) {
//            return returnOpenJsonError(innerOpen.getResultErrorCode(), innerOpen.getErrorMsg());
//        }
//        String nextUri = "/openapi/v2.1/basedata/queryData/" + type;
//        return qd(type, request, nextUri, innerOpen);
//    }
//
//    @ResponseBody
//    @RequestMapping(value = "/v2.1/basedata/queryData/{type}/{id}")
//    @ControllerInfo(value = "开发者调用接口：/v2.1/basedata/queryData/{type}/id",parameter = "{ticketKey}")
//    public String queryDataV21(@PathVariable String type, @PathVariable String id, HttpServletRequest request) {
//        InnerOpenDto innerOpen = checkParam(request, "/openapi/v2.0/" + type + "/{id}");
//        if (!innerOpen.isPass()) {
//            return returnOpenJsonError(innerOpen.getResultErrorCode(), innerOpen.getErrorMsg());
//        }
//        String nextUri = "/openapi/v2.1/basedadta/queryData/" + type + "/{id}";
//        return qd(type, id, request, nextUri, innerOpen);
//    }
//
//    @ResponseBody
//    @RequestMapping(value = "/v2.1/basedata/queryData/{type}/{id}/{subType}")
//    @ControllerInfo(value = "开发者调用接口：/v2.1/basedata/queryData/{type}/id/{subType}",parameter = "{ticketKey}")
//    public String queryDataV21(@PathVariable String type, @PathVariable String id, @PathVariable String subType,
//            HttpServletRequest request) {
//        InnerOpenDto innerOpen = checkParam(request, "/openapi/v2.0/" + type + "/{id}/" + subType);
//        if (!innerOpen.isPass()) {
//            return returnOpenJsonError(innerOpen.getResultErrorCode(), innerOpen.getErrorMsg());
//        }
//        String uri = "/v2.1/basedata/queryData/" + type + "/{id}/" + subType;
//        return qd(type, id, subType, request, uri, innerOpen);
//    }
//
//    // ------------------------------------
//    // 其他业务接口
//
//    /**
//     * 获取用户拥有的子系统
//     * 
//     * @param type
//     * @param value
//     * @param request
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping(value = "/v2.1/system/controlForUser/{type}/{value}")
//    public String queryControlForUser(@PathVariable String type, @PathVariable String value, HttpServletRequest request) {
//        InnerOpenDto innerOpen = checkParam(request, "/openapi/v2.1/system/controlForUser/{type}/{value}");
//        if (!innerOpen.isPass()) {
//            return returnOpenJsonError(innerOpen.getResultErrorCode(), innerOpen.getErrorMsg());
//        }
//        // 匹配type
//        SubsystemForUserEnum subUserEnum = SubsystemForUserEnum.getByName(type, value);
//        if (subUserEnum == null) {
//            return returnOpenJsonError("不匹配");
//        }
//        // 找用户
//        final User user = subUserEnum.getUser();
//        if (user == null) {
//            return returnOpenJsonError("未找到");
//        }
//        // 暂时用缓存时效
//        JSONObject finJson = RedisUtils.getObject(
//                OPEN_API_KEY + ".system.controlForUser." + getHashKey(request, type + value),
//                RedisUtils.TIME_HALF_MINUTE, new TypeReference<JSONObject>() {
//                }, new RedisInterface<JSONObject>() {
//                    @Override
//                    public JSONObject queryData() {
//                        JSONObject returnJson = new JSONObject();
//                        List<SubSystem> subSystemList = SUtils.dt(subSystemRemoteService.findAll(),
//                                new TR<List<SubSystem>>() {
//                                });
//                        Map<Integer, SubSystem> subSystemMap = new HashMap<Integer, SubSystem>();
//                        List<SubSystem> finSubList = new ArrayList<SubSystem>();// list便于后面排序
//                        Set<SubSystem> finSubSet = new HashSet<SubSystem>();
//                        for (SubSystem sub : subSystemList) {
//                            subSystemMap.put(sub.getId(), sub);
//                            if (SubSystem.SOURCE_THIRD_PART_NORMAL.equals(sub.getSource())) {
//                                // 非本地应用
//                                finSubSet.add(sub);
//                            }
//                        }
//                        List<Model> modelList = SUtils.dt(modelRemoteService.findByUserId(user.getId()),
//                                new TypeReference<List<Model>>() {
//                                });
//                        SubSystem subSystem = null;
//                        for (Model model : modelList) {
//                            Integer subSystemId = model.getSubSystem();
//                            subSystem = subSystemMap.get(subSystemId);
//                            if (subSystem == null) {
//                                continue;
//                            }
//                            finSubSet.add(subSystem);
//                        }
//                        for (SubSystem item : finSubSet) {
//                            finSubList.add(item);
//                        }
//                        // 排序
//                        Collections.sort(finSubList, new Comparator<SubSystem>() {
//                            @Override
//                            public int compare(SubSystem o1, SubSystem o2) {
//                                int order1 = o1.getDisplayOrder() == null ? 0 : o1.getDisplayOrder();
//                                int order2 = o2.getDisplayOrder() == null ? 0 : o2.getDisplayOrder();
//                                return order1 - order2;
//                            }
//                        });
//
//                        JSONArray linJsonArr = new JSONArray();
//                        JSONObject linJson = null;
//                        List<Server> serverList = SUtils.dt(serverRemoteService.findAll(), new TR<List<Server>>() {
//                        });
//                        Map<String, Server> serverMap = new HashMap<String, Server>();
//                        for (Server server : serverList) {
//                            serverMap.put(server.getCode(), server);
//                        }
//                        String fileUrl = sysOptionRemoteService.findValue("FILE.URL");
//                        StringBuffer linUrl = null;
//                        for (SubSystem item : finSubList) {
//                            linJson = new JSONObject();
//                            linJson.put("code", item.getCode());
//                            linJson.put("name", item.getName());
//                            Server server = serverMap.get(item.getCode());
//                            linUrl = new StringBuffer();
//                            if (server == null) {
//                                linUrl.append(item.getUrl());
//                            }
//                            else {
//                                linUrl.append(server.getUrl() + server.getIndexUrl());
//                                if (linUrl.indexOf("appId") != -1) {
//                                    linUrl.append("&remote=true");
//                                }
//                            }
//                            linJson.put("url", linUrl.toString());
//                            linJson.put("imageUrl", fileUrl + "/store/subsystempic/" + item.getImage());
//                            linJsonArr.add(linJson);
//                        }
//                        returnJson.put("data", linJsonArr);
//                        return returnJson;
//                    }
//
//                });
//        JSONObject mainjson = new JSONObject();
//        mainjson.put("maindata",
//                SecurityUtils.encryptAESAndBase64(finJson.toString(), innerOpen.getDeveloper().getTicketKey()));
//        return returnOpenJsonObjSuccess(mainjson, "ok");
//    }
//
//    private enum SubsystemForUserEnum {
//        ID("id") {
//            @Override
//            public User getUser() {
//                UserRemoteService userService = (UserRemoteService) Evn.getBean("userRemoteService");
//                return SUtils.dt(userService.findOneById(getValue()), new TypeReference<User>() {
//                });
//            }
//        },
//        USERNAME("username") {
//            @Override
//            public User getUser() {
//                UserRemoteService userService = (UserRemoteService) Evn.getBean("userRemoteService");
//                return SUtils.dt(userService.findByUsername(getValue()), new TypeReference<User>() {
//                });
//            }
//        };
//
//        private final String type;
//        private String value;
//
//        SubsystemForUserEnum(String type) {
//            this.type = type;
//        }
//
//        public String getType() {
//            return type;
//        }
//
//        public void setValue(String value) {
//            this.value = value;
//        }
//
//        public String getValue() {
//            return value;
//        }
//
//        public abstract User getUser();
//
//        public static SubsystemForUserEnum getByName(String type, String value) {
//            if (StringUtils.isEmpty(type)) {
//                return null;
//            }
//            for (SubsystemForUserEnum item : SubsystemForUserEnum.values()) {
//                if (item.getType().equals(type)) {
//                    item.setValue(value);
//                    return item;
//                }
//            }
//            return null;
//        }
//    }
//
//    private String getHashKey(HttpServletRequest request, String other) {
////        Map<String, String> parameterMap = request.getParameterMap();
//        Map<String, String[]> mop = request.getParameterMap();
//		Map<String, String> mapOfParameter = new HashMap<String, String>();
//		for(String key : mop.keySet()){
//			mapOfParameter.put(key, mop.get(key)[0]);
//		}
//        return SUtils.s(mapOfParameter) + (StringUtils.isNotBlank(other) ? other : "");
//    }
//
//    /**
//     * v2.1+校验接口、参数和ip合法性 顺序ip、接口、参数
//     * 
//     * @param request
//     * @return
//     */
//    private InnerOpenDto checkParam(HttpServletRequest request, String uri) {
//        InnerOpenDto innerOpen = new InnerOpenDto();
//        String apId = request.getParameter(OPEN_AP_ID);
//        String param = request.getParameter(OPEN_PARAM);
//        int resultErrorCode = -1;
//        String errorMsg = "";
//        Developer developer = developerService.findOne(apId);
//        if (developer == null) {
//            errorMsg = "不匹配";
//        }
//        else {
//            if (checkIps(request, developer)) {
//                boolean isOwn = false;
//                if (StringUtils.isNotBlank(developer.getUsername())) {
//                    // 校验接口权限
//                    // 先注释掉，逻辑重写
//                    // List<OpenApiInterface> findInterfacesTkList =
//                    // openApiInterfaceService.findInterfacesTkList(ticketKey.getId());
//                    // for (OpenApiInterface item : findInterfacesTkList) {
//                    // if(uri.equals(item.getUri())){
//                    // isOwn = true;
//                    // break;
//                    // }
//                    // }
//                }
//                else {
//                    isOwn = true;
//                }
//                if (!isOwn) {
//                    errorMsg = "接口无访问权限";
//                }
//                else {
//                    innerOpen.setDeveloper(developer);
//                    if (StringUtils.isNotBlank(param)) {
//                        byte[] bs = null;
//                        try {
//                            bs = SecurityUtils.decryptAESAndBase64(param.getBytes(), developer.getTicketKey());
//                            innerOpen.setParamJsonObj(JSONObject.parseObject(new String(bs)));
//                            return innerOpen;
//                        }
//                        catch (Exception e) {
//                            e.printStackTrace();
//                            errorMsg = "无法识别";
//                        }
//                    }
//                    else {
//                        return innerOpen;
//                    }
//                }
//            }
//            else {
//                errorMsg = "ip无访问权限";
//            }
//        }
//        innerOpen.setPass(false);
//        innerOpen.setResultErrorCode(resultErrorCode);
//        innerOpen.setErrorMsg(errorMsg);
//        return innerOpen;
//    }
//
//    /**
//     * 校验ip白名单
//     * 
//     * @param request
//     * @param ticketKey
//     * @return
//     */
//    private boolean checkIps(HttpServletRequest request, Developer developer) {
//        String ips = developer.getIps();
//        if (StringUtils.isNotBlank(ips) && ips.indexOf(ServletUtils.getRemoteAddr(request)) == -1) {
//            // 白名单之外
//            return false;
//        }
//        return true;
//    }
//
//}
