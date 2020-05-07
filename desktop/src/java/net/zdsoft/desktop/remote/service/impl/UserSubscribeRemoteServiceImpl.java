package net.zdsoft.desktop.remote.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import net.zdsoft.basedata.remote.service.OperationLogDataSyncRemoteService;
import net.zdsoft.basedata.remote.service.OperationLogRemoteService;
import net.zdsoft.basedata.remote.service.impl.BaseRemoteServiceImpl;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.desktop.action.RecommendAppAction;
import net.zdsoft.desktop.entity.UserSubscribe;
import net.zdsoft.desktop.remote.service.UserSubscribeRemoteService;
import net.zdsoft.desktop.service.UserSubscribeService;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.system.entity.server.Server;
import net.zdsoft.system.remote.service.ServerRemoteService;
import net.zdsoft.system.remote.service.SysOptionRemoteService;

/**
 * @author yangsj  2017-6-16上午10:43:14
 */
@Service("UserSubscribeRemoteService")
public class UserSubscribeRemoteServiceImpl extends BaseRemoteServiceImpl<UserSubscribe, String> implements
		UserSubscribeRemoteService {
	 @Autowired
	 private UserSubscribeService userSubscribeService;
	 @Autowired
	 private ServerRemoteService serverRemoteService;
	 @Autowired
	 private OperationLogRemoteService operationLogRemoteService;
	 @Autowired
	 private SysOptionRemoteService sysOptionRemoteService;
     @Autowired
     private OperationLogDataSyncRemoteService operationLogDataSyncRemoteService;

	@Override
	protected BaseService<UserSubscribe, String> getBaseService() {
		return userSubscribeService;
	}

	//获得排行榜的应用列表
	 @Override
	public String findRankingList(Integer  pageNum) {
		 //计时器
		 doTimer();
		 //先取缓存，缓存时间为一天
		 JSONObject jsonObject2 =RedisUtils.getObject("rankingList.history",new TR<JSONObject>() {});
		 if(jsonObject2 != null){
			 return jsonObject2.toJSONString();
		 }else{
			 try {
					 if(pageNum == null)
						pageNum = 8;
					 List<Server> serverList = SUtils.dt(serverRemoteService.findAll(),Server.class);
					 List<Integer> serverIds1 = EntityUtils.getList(serverList, "id");
					 List<UserSubscribe> userSubscribes = userSubscribeService.findAll();
					 List<Integer> serverIds2 = EntityUtils.getList(userSubscribes, "serverId");
					 //取两个集合的交集
					 List<Integer> showServerIds = (List<Integer>) CollectionUtils.intersection(serverIds1, serverIds2);
				     List<Server> showServerList = SUtils.dt(serverRemoteService.findListByIds(showServerIds.toArray(new Integer[showServerIds.size()])),Server.class);
					 Map<Integer, List<Server>> listServerMap = EntityUtils.getListMap(showServerList, "id", null);
				     
					 List<Server> serverList2 = new ArrayList<Server>();
					 for (Server server : showServerList) {
							int count = listServerMap.get(server.getId()).size();
							server.setSubscribeCount(count);
							serverList2.add(server);
					 }
					 
					 //根据订阅数量排序
					 Collections.sort(serverList2, new Comparator<Server>() {
					     @Override
					     public int compare(Server o1, Server o2) {
					         int o1Count = o1.getSubscribeCount();
							 int o2Count = o2.getSubscribeCount();
							 return o1Count <=  o2Count ?1:-1;
					     }
					 });
					 //取前几个数据
					 if(serverList2.size()>pageNum){
						   List<Server> list1 = new ArrayList<Server>();
						   list1 = serverList2.subList(pageNum, serverList2.size());
						   list1.clear();
						   serverList2.removeAll(list1);		   
					   }
					 JSONObject jsonObject1 = new JSONObject();
					 JSONArray jsonArray = new JSONArray();
					 String fileUrl = sysOptionRemoteService.getFileUrl(null);
					 
//					 Map<String, List<OperationLog>> operationMap = getServerIdMap(serverList2);
					 
					 for (Server server : serverList2) {
						 JSONObject jsonObject = new JSONObject();
						 jsonObject.put("id", server.getId());
						 jsonObject.put("name", server.getName());
						 jsonObject.put("url", server.getUrlIndex(false));
						 jsonObject.put("introductionPage", server.getIntroductionPage());
						 jsonObject.put("icon", UrlUtils.ignoreLastRightSlash(fileUrl) + "/" + UrlUtils.ignoreFirstLeftSlash(server.getIconUrl()));
						 jsonObject.put("showType", new RecommendAppAction().getShowTypeName(server));
						 jsonObject.put("description", server.getDescription());
						 jsonObject.put("popularity", operationLogDataSyncRemoteService.findAppPopularity(String.valueOf(server.getId())));
						 jsonArray.add(jsonObject);
					}
					 //缓存有效时间为一天
					 jsonObject1.put("rankingList", jsonArray);
					 RedisUtils.setObject(null,"rankingList.history", jsonObject1,86400);
					 return jsonObject1.toJSONString();
					 
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 return "";
		 }
	}

	/**
	 * @param serverList2
	 * @return
	 */
//	protected Map<String, List<OperationLog>> getServerIdMap(List<Server> serverList2) {
//		Set<Integer> serverIds = EntityUtils.getSet(serverList2, "id");
//		 List<String> stringIds = new ArrayList<String>();
//		 for (Integer serverId : serverIds) {
//			 stringIds.add(String.valueOf(serverId));
//		 }
//		 List<OperationLog> operationLogs = operationLogDataSyncService.findAppPopularity(stringIds.toArray(new String[stringIds.size()]));
//		 Map<String, List<OperationLog>> operationMap = EntityUtils.getListMap(operationLogs, "parameter", null);
//		return operationMap;
//	}

	//获得每个应用的人气
	 @Override
	 public String findAppPopularity() {
		//计时器
		 doTimer();
		//先取缓存，缓存时间为一天
		JSONObject jsonObject2 =RedisUtils.getObject("popularityList.history",new TR<JSONObject>() {});
		 if(jsonObject2 != null){
			 return jsonObject2.toJSONString();
		 }else{
			 try {
			    List<Server> serverList = SUtils.dt(serverRemoteService.findAll(),Server.class);
			    JSONObject jsonObject1 = new JSONObject();
				JSONArray jsonArray = new JSONArray();
				 for (Server server : serverList) {
					 JSONObject jsonObject = new JSONObject();
					 jsonObject.put("id", server.getId());
					 jsonObject.put("popularity", operationLogDataSyncRemoteService.findAppPopularity(String.valueOf(server.getId())));
					 jsonArray.add(jsonObject);
				}
				 jsonObject1.put("popularityList", jsonArray);
				 //缓存有效时间为一天
				 RedisUtils.setObject(null,"popularityList.history", jsonObject1,86400);
				 return jsonObject1.toJSONString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 return "";
		 }
	 }
	 
	 /**
		 * 计时器  86400000
		 */
	private void doTimer() {
		 Calendar c = Calendar.getInstance();
	     int hour = c.get(Calendar.HOUR_OF_DAY);
	     c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY) + (23-hour));
	     Date date = c.getTime();
		 new Timer().schedule(new TimerTask() {
	            @Override
	            public void run() {
	            	RedisUtils.del("rankingList.history");  
	            	RedisUtils.del("popularityList.history");
	            }
	        }, date, 86400000); 
	}
	
	 public static void main(String[] args) {
	      // creating timer task, timer
		 Calendar c = Calendar.getInstance();
	     int hour = c.get(Calendar.HOUR_OF_DAY);
	     c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY) + (23-hour));
	     Date date = c.getTime();
	     SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	     System.out.println("一个小时前的时间：" +df.format(c.getTime()));
	     System.out.println("当前的时间：" + df.format(new Date()));
	     
		 new Timer().schedule(new TimerTask() {
	            @Override
	            public void run() {
	            	System.out.println("working on");    
	            }
	        },  date,1000); 
	   }
}
