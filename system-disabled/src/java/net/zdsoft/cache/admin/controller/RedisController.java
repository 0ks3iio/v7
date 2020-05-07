package net.zdsoft.cache.admin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.zdsoft.cache.admin.CacheAdminConstant;
import net.zdsoft.cache.admin.InitCacheAdmin;
import net.zdsoft.cache.admin.RedisApplication;
import net.zdsoft.cache.admin.core.CKey;
import net.zdsoft.cache.admin.core.CValue;
import net.zdsoft.cache.admin.core.CacheTreeNode;
import net.zdsoft.cache.admin.core.QueryType;
import net.zdsoft.cache.admin.service.RedisService;
import net.zdsoft.cache.admin.service.ViewService;
import net.zdsoft.cache.admin.utils.TomcatUtils;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Pagination;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.DataType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Set;

/**
 * @author shenke
 * @since 2017.07.11
 */
@Controller
@RequestMapping("/redis")
public class RedisController extends BaseAction {

    @Autowired private ViewService viewService;
    @Autowired private RedisService redisService;
    @Autowired private Environment environment;
    @Autowired private InitCacheAdmin initCacheAdmin;

    @RequestMapping(value = "index", method = RequestMethod.GET)
    public Object index(){
        String host;
        String port;
        Pagination pagination = createPagination();
        ModelAndView mv = createMV("/system/cacheAdmin/index.ftl").addObject("pagination",pagination);
        if ( RedisApplication.redisServerCache.isEmpty() ) {
            return mv;
        }
        host = environment.getProperty(CacheAdminConstant.PROPERTIES_REDIS_HOST);
        port = environment.getProperty(CacheAdminConstant.PROPERTIES_REDIS_PORT);
        if ( RedisApplication.keyCache.isEmpty() ) {
            Evn.getBean("initAdminContext");
        }
        List<CKey> cKeys = RedisApplication.keyCache.get(host+"_"+port+"_0");
        pagination.setPageIndex(1);
        pagination.setMaxRowCount(0);
        pagination.setPageSize(10);
        pagination.initialize();
        return mv.addObject("cKeys",null);
    }

    @ResponseBody
    @RequestMapping(value = "serverTree", method = RequestMethod.GET)
    public Object redisTree(@RequestParam(value = "refresh", required = false, defaultValue = "false") boolean refresh) {
        JSONObject data = new JSONObject();
        try {
            Set<CacheTreeNode> tree = viewService.getLeftTree(refresh);
            data.put("tree",JSON.toJSONString(tree));
            data.put("success",Boolean.TRUE);
        } catch (Exception e){
            data.put("success",Boolean.FALSE);
        }
        return data.toJSONString();
    }

    @RequestMapping(value = "keyList", method = RequestMethod.GET)
    public Object keyList(@RequestParam(name = "dbIndex", required = false,defaultValue = "0") int dbIndex,
                          @RequestParam(name = "queryType", required = false) String queryType,
                          @RequestParam(name = "queryKey", required = false) String queryKey,
                          @RequestParam(name = "dataType", required = false) String dataType) {
        Pagination pagination = createPagination();
        pagination.setPageSize(10);
        dataType = StringUtils.lowerCase(dataType);
        List<CKey> cKeys = viewService.getCKeyByDataTypeAndKey(StringUtils.isBlank(dataType)?null:DataType.fromCode(dataType),
                StringUtils.isBlank(queryType)?null:QueryType.fromType(queryType) ,queryKey, dbIndex, pagination);
        return createMV("/system/cacheAdmin/keyList.ftl").addObject("cKeys",cKeys).addObject("pagination",pagination);
    }

    @ResponseBody
    @RequestMapping(value = "refresh/keyCache", method = RequestMethod.GET)
    public Object refresh() {
        initCacheAdmin.initKeyCacheFromServerCache();
        viewService.getLeftTree(Boolean.TRUE);
        return success("刷新成功");
    }

    @ResponseBody
    @RequestMapping(value = "refresh/server", method = RequestMethod.GET)
    public Object refreshServer(){
        initCacheAdmin.initRedisKeys();

        return null;
    }

    @RequestMapping(value = "view" , method = RequestMethod.GET)
    public Object viewValue(@RequestParam(name = "dbIndex", required = false) int dbIndex ,
                            @RequestParam(name = "dataType") String dataType ,
                            @RequestParam(name = "key") String key ){

        CKey cKey = new CKey();
        getRequest().getCharacterEncoding();
        // this can move to interceptor or filter for GET
        String uriEncoding = TomcatUtils.getURIEncoding(getRequest().getServerPort());

        if ( StringUtils.isNotBlank(uriEncoding) && "utf-8".equalsIgnoreCase(uriEncoding) ) {
            try {
                key = new String(key.getBytes(uriEncoding),"utf-8");
            } catch (Exception e){
                log.error("转码失败");
            }
        }
        cKey.setKey(key);
        cKey.setDataType(DataType.fromCode(dataType.toLowerCase()));

        Object object = redisService.getCValueByCKey(cKey,dbIndex);
        return createMV("/system/cacheAdmin/viewValue.ftl").addObject("data",object);
    }

    @ResponseBody
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Object deleteKV(@RequestParam("key") String key){
        redisService.delKV(key);
        return success("删除成功");
    }

    @RequestMapping(value = "add/page", method = RequestMethod.GET)
    public Object addPage() {
        return createMV("/system/cacheAdmin/add.ftl");
    }

    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public  Object addKV(@RequestBody CValue cValue,
                         @RequestParam("key") String key,
                         @RequestParam("dataType") String dataType) {
        return success("add success");
    }

    @RequestMapping(value = "add/server/page", method = RequestMethod.GET)
    public Object addServerPage() {

        return createMV("/system/cacheAdmin/addServer.ftl");
    }

    @ResponseBody
    @RequestMapping(value = "add/server", method = RequestMethod.GET)
    public Object addServer(@RequestParam("host") String host,
                            @RequestParam("port") int port,
                            @RequestParam(value = "password", required = false) String password, RedirectAttributes redirectAttributes) {

        boolean refresh = false;
        try {
            refresh = redisService.addServer(host,port,password);
        } catch (Exception e){
            return error(e.getMessage());
            //initCacheAdmin.initRedisKeys();
        }
        redirectAttributes.addAttribute("refresh",refresh);
        return success("添加成功");
    }

    private ModelAndView createMV(String viewName){
        return new ModelAndView(viewName);
    }
}
