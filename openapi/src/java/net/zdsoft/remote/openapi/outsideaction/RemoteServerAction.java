package net.zdsoft.remote.openapi.outsideaction;

import com.alibaba.fastjson.JSONObject;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.utils.EncryptAES;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.remote.openapi.action.OpenApiBaseAction;
import net.zdsoft.remote.openapi.action.business.UserSubscribeAction;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.entity.server.Server;
import net.zdsoft.system.remote.service.ModelRemoteService;
import net.zdsoft.system.remote.service.ServerRemoteService;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping(value = { "/remote/openapi/outside" })
public class RemoteServerAction extends OpenApiBaseAction{

protected static final Logger logger = LoggerFactory.getLogger(UserSubscribeAction.class);
	
	@Autowired
	private ServerRemoteService serverRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	private ModelRemoteService modelRemoteService;
	@Autowired
	private SysOptionRemoteService sysOptionRemoteService;
	
	/**
     *获取当前用户有权限的所有应用（ 第三方应用）
     *
     * @param remoteParam
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getServer")
    public String findRankingList(String userId, String pwd) {
        try {
        	JSONObject jsonObject = new JSONObject();
        	List<ServerDto> serverDtos = new ArrayList<>();
        	List<Server> serverList = new ArrayList<>();
        	String username = null;
        	if(StringUtils.isNotBlank(userId)){
        		username = doExcuteDate(userId);
        		User user = SUtils.dc(userRemoteService.findByUsername(username), User.class);
        		if(user != null){
        			username = user.getUsername();
					String unitId = user.getUnitId();
					Unit unit = SUtils.dc(unitRemoteService.findOneById(unitId), Unit.class);
					//得到有权限的第三方应用
					serverList = SUtils.dt(serverRemoteService.findByOwnerTypeAndUnitIdAndUnitClass(
							user.getOwnerType(), unitId, unit.getUnitClass()), Server.class);
					if(CollectionUtils.isNotEmpty(serverList)){
						serverList= serverList.stream().filter(t -> t.getServerClass().equals(2))
						.collect(Collectors.toList());
					}
//    					List<Server> innerServerLists = getInnerServers(user);
//    					if(CollectionUtils.isNotEmpty(innerServerLists)){
//    						serverList.addAll(innerServerLists);
//    					}
        		}
        	}
        	if(CollectionUtils.isNotEmpty(serverList)){
        		for (Server server : serverList) {
        			ServerDto serverDto = new ServerDto();
        			String iconURLString = UrlUtils.ignoreLastRightSlash(getFileURL())
        					+ UrlUtils.SEPARATOR_SIGN + UrlUtils.ignoreFirstLeftSlash(server.getIconUrl());
        			serverDto.setImageUrl(iconURLString);
        			boolean isSecondUrl = sysOptionRemoteService.isSecondUrl(getRequest().getServerName());
        			serverDto.setUrlIndex(getIndexUrl(server, isSecondUrl));
        			serverDto.setServerName(server.getName());
        			serverDtos.add(serverDto);
				}
        		
        	}
        	jsonObject.put("username", username);
        	jsonObject.put("data", serverDtos);
        	jsonObject.put("dataCount", CollectionUtils.isEmpty(serverDtos)? 0: serverDtos.size());
      	    return returnOpenJsonObjSuccess(jsonObject, "ok");
        }
        catch (Exception ex) {
        	return returnOpenJsonError("调用远程接口失败");
        }
    }


	private String getIndexUrl(Server server, boolean isSecondUrl) {
		String serverUrl = isSecondUrl ? server.getSecondUrl() : server.getUrl();
		String indexUrl = server.getIndexUrl();
		if (StringUtils.startsWith(indexUrl, "http")) {
		    return indexUrl;
		}
		if (StringUtils.isNotBlank(indexUrl) && !StringUtils.equals("/", indexUrl)) {
		    return serverUrl + indexUrl;
		}
		return serverUrl;
	}
    
    
    /**
     * 根据用户信息来获取所有有权限的子系统
     * @return
     */
//    private List<Server> getInnerServers (User user){
//    	List<Model>  models = SUtils.dt(modelRemoteService.findByUserId(user.getId()), Model.class);
//        Set<Integer> subSystemIds = EntityUtils.getSet(models, Model::getSubSystem);
//    	return serverRemoteService.findListObjectByIn("subId", subSystemIds.toArray(new Integer[0]));
//    }
    
    
    class ServerDto {
    	private String imageUrl;
    	private String urlIndexSecond;
    	private String urlIndex;
    	private String serverName;
		public String getImageUrl() {
			return imageUrl;
		}
		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}
		public String getUrlIndexSecond() {
			return urlIndexSecond;
		}
		public void setUrlIndexSecond(String urlIndexSecond) {
			this.urlIndexSecond = urlIndexSecond;
		}
		public String getUrlIndex() {
			return urlIndex;
		}
		public void setUrlIndex(String urlIndex) {
			this.urlIndex = urlIndex;
		}
		public String getServerName() {
			return serverName;
		}
		public void setServerName(String serverName) {
			this.serverName = serverName;
		}
    }
    
    private String getFileURL(){
        return sysOptionRemoteService.findValue(Constant.FILE_URL);
    }
    
    /**
	 * 进行用户id的解析
	 * @param userId
	 * @return
	 */
	private String doExcuteDate(String userId) throws Exception {
		return new String(EncryptAES.base64Decode(userId));
	}
}
