package net.zdsoft.desktop.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.zdsoft.basedata.entity.User;
import net.zdsoft.desktop.entity.UserSubscribe;
import net.zdsoft.desktop.remote.service.UserSubscribeRemoteService;
import net.zdsoft.desktop.service.UserSubscribeService;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.Objects;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.SortUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.system.entity.server.Server;
import net.zdsoft.system.remote.service.ServerRemoteService;

/**
 * 推荐应用设置
 * @author shenke
 * @since 2017.05.15
 */
@Controller
@RequestMapping("/desktop/recommendApp")
public class RecommendAppAction extends DeskTopBaseAction {


    @Autowired private ServerRemoteService serverRemoteService;
    @Autowired private UserSubscribeService userSubscribeService;
    
    private static final String MODEL_ICON_DEFAULT = "/default/images/default_b.png";
    
	@ResponseBody
	@RequestMapping("/openApp")
	@ControllerInfo(value = "打开应用：{serverId}",parameter = "{serverId}")
	public String openApp(Integer serverId, HttpServletResponse response){
		try {
			Server server = SUtils.dc(serverRemoteService.findOneById(serverId), Server.class);
			String url = server.getIndexUrl();
			return success("打开应用");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return error("应用不存在");
	}
    @RequestMapping("/set") public String execute(ModelMap map) {
        try {
            List<Server> showServerList = findShowServers();
            
            List<JSONObject> serverDtos = Lists.newArrayList();
            if (showServerList != null) {
                SortUtils.ASC(showServerList,"orderId");
                for (Server server : showServerList) {
                    JSONObject app = new JSONObject();
                    String name = server.getName();
                    if (name.length() > 8) {
	                      String title = name.substring(0, 8) ;
	                      name = title;
	                 }
                    app.put("name", name);
                    app.put("nameAll", server.getName());
                    //app.put("url", server.getUrlIndex());
                    app.put("icon", UrlUtils.ignoreLastRightSlash(getFileURL()) + "/" + UrlUtils.ignoreFirstLeftSlash(server.getIconUrl()));
                    app.put("id",server.getId());
                    app.put("orderId",server.getOrderId());
                    serverDtos.add(app);
                }
            }
            map.put("serverDtos",serverDtos);
            map.put("isApp", true);
        } catch (Exception e){
            log.error(e);
        }
        return "/desktop/homepage/ap/set/operation-scrollBig-set.ftl";
    }
    
    @RequestMapping("/showApp") public String showApp(ModelMap map) {
        try {
            LoginInfo info = getLoginInfo();
            List<Server> serverList = SUtils.dt(serverRemoteService.findByOwnerTypeAndUnitIdAndUnitClass(info.getOwnerType(), info.getUnitId(), info.getUnitClass()), Server.class);
            List<JSONObject> serverDtos = Lists.newArrayList();
            if (serverList != null) {
                SortUtils.ASC(serverList,"orderId");
                for (Server server : serverList) {
                    JSONObject app = new JSONObject();
                    app.put("name", server.getName());
                    //app.put("url", server.getUrlIndex());
                    String description = server.getDescription();
                    if (StringUtils.isNotBlank(description) && description.length() > 25) {
	                      String title = description.substring(0, 25) + "...";
	                      description = title;
	                  }
                    app.put("description", description);
                    app.put("descriptionAll", server.getDescription());
                    app.put("showType", getShowTypeName(server));
                    app.put("icon", UrlUtils.ignoreLastRightSlash(getFileURL()) + "/" + UrlUtils.ignoreFirstLeftSlash(server.getIconUrl()));
                    app.put("id",server.getId());
                    app.put("orderId",server.getOrderId());
//                    app.put("status", "1");
                    app.put("status", String.valueOf(isAddApp(server.getId(),info.getUserId())));
                    serverDtos.add(app);
                }
            }
            String filePath = sysOptionRemoteService.findValue(net.zdsoft.system.constant.Constant.FILE_PATH);
            map.put("serverDtos",serverDtos);
            map.put("isApp", "true");
        } catch (Exception e){
            log.error(e);
        }
        return "/desktop/homepage/ap/set/operation-app-set.ftl";
    }

	/**
	 * @param server
	 */
	public String getShowTypeName(Server server) {
		String typeString  = server.getUsertype();
		String[] tStrings = typeString.split(",");
		StringBuilder showType = new StringBuilder();
		for (int i = 0; i < tStrings.length; i++) {
			if(Objects.equals(tStrings[i], String.valueOf(User.OWNER_TYPE_FAMILY))){
				showType.append("家长/");
			}else if(Objects.equals(tStrings[i], String.valueOf(User.OWNER_TYPE_STUDENT))){
				showType.append("学生/");
			}else{
				showType.append("教师/");
			}
		}
		return StringUtils.removeEnd(showType.toString(), "/");
	}
    
    @ResponseBody
    @RequestMapping("/order/save") public String save(String jsonString) {

        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            JSONArray idArray = jsonObject.getJSONArray("id");
            JSONArray orderIdArray = jsonObject.getJSONArray("orderId");
            Map<Integer,Integer> idOrderIdMap = Maps.newHashMap();
            for (Object o : idArray) {
                idOrderIdMap.put(NumberUtils.toInt(o.toString()), NumberUtils.toInt(orderIdArray.get(idArray.indexOf(o)).toString()));
            }
            serverRemoteService.update(idOrderIdMap,"id","orderId");
            RedisUtils.del("my.thrid.app." + getLoginInfo().getUserId()); //update cache
        } catch (Exception e){
            e.printStackTrace();
        }

        return success("保存成功");
    }
    
    //保存应用中心的添加
    @ResponseBody
    @RequestMapping("/order/add") public String add(String jsonString) {

        try {
            serverRemoteService.update(saveAppMap(jsonString, "id", "orderId"),"id","orderId");
        } catch (Exception e){
            e.printStackTrace();
        }finally{
            try {
          JSONObject jsonObject = JSONObject.parseObject(jsonString);
          String serverId = jsonObject.getString("serverId");
          if(serverId.contains("[") ){
        	   //个人订阅不存在，就添加
        	   JSONArray serverIdArray = jsonObject.getJSONArray("serverId");
               String msg = userSubscribeService.addUserSubscribe(serverIdArray,getLoginInfo().getUserId());
               if(Objects.equals("error", msg)){
               	return error("保存失败");
               }
          }else{
        	 //个人订阅不存在，就添加
               String msg = userSubscribeService.addUserSubscribe(serverId,getLoginInfo().getUserId());
               if(Objects.equals("error", msg)){
               	return error("保存失败");
               }
          }
          userSubscribeService.update(saveAppMap(jsonString, "serverId", "status"),"serverId","status");
          } catch (Exception e2) {}
        }
        RedisUtils.del("my.thrid.app." + getSession().getId() + getLoginInfo().getUserId()); //update cache
        return success("保存成功");
    }
    
    //判断是否已经添加
    private int isAddApp(Integer serverId,String userId){
	    UserSubscribe userSubscribe = userSubscribeService.findByUserServerId(serverId,userId);
    	if(userSubscribe != null && userSubscribe.getStatus() == UserSubscribe.STATUS_TURNON){
    		return UserSubscribe.STATUS_TURNON;
    	}else{
    		return UserSubscribe.STATUS_TURNOFF;
    	}
    }
    
    
    //得到我的应用的集合
    protected List<Server> findShowServers(){
    	LoginInfo info = getLoginInfo();
        List<Server> serverList = SUtils.dt(serverRemoteService.findByOwnerTypeAndUnitIdAndUnitClass(info.getOwnerType(), info.getUnitId(), info.getUnitClass()), Server.class);
        List<Integer> serverIds1 = EntityUtils.getList(serverList, Server::getId);
        List<UserSubscribe> userSubscribes = userSubscribeService.findByUserId(info.getUserId());
        List<Integer> serverIds2 = EntityUtils.getList(userSubscribes, UserSubscribe::getServerId);
        //取两个集合的交集
        List<Integer> showServerIds = (List<Integer>) CollectionUtils.intersection(serverIds1, serverIds2);
        List<Server> showServerList = SUtils.dt(serverRemoteService.findListByIds(showServerIds.toArray(new Integer[showServerIds.size()])), Server.class);
    	
        return showServerList;
    	
    	
    }
    
    //返回map集合
    private Map<Integer,Integer> saveAppMap (String jsonString,String condition1,String condition2 ){
      Map<Integer,Integer> idOrderIdMap = Maps.newHashMap();
      JSONArray idArray = null;
      JSONArray orderIdArray = null;
      JSONObject jsonObject = JSONObject.parseObject(jsonString);
      String outCome1 = jsonObject.getString(condition1);
      String outCome2 = jsonObject.getString(condition2);
      if(outCome1.contains("[") && outCome2.contains("[")){
    	   idArray = jsonObject.getJSONArray(condition1);
    	   orderIdArray = jsonObject.getJSONArray(condition2);
      
	      for (Object o : idArray) {
	          idOrderIdMap.put(NumberUtils.toInt(o.toString()), NumberUtils.toInt(orderIdArray.get(idArray.indexOf(o)).toString()));
	      }
      }	else{
    	  idOrderIdMap.put(NumberUtils.toInt(outCome1),NumberUtils.toInt(outCome2) );
      }
    	
      return idOrderIdMap;
    	
    	
    }
    
    
}
