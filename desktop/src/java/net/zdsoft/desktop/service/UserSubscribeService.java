package net.zdsoft.desktop.service;

import java.util.List;

import com.alibaba.fastjson.JSONArray;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.desktop.entity.UserSubscribe;

/**
 * @author yangsj  2017-6-12下午6:14:39
 */
public interface UserSubscribeService extends BaseService<UserSubscribe, String>{

	/**
	 * @param userId
	 * @return
	 */
	List<UserSubscribe> findByUserId(String userId);


	/**
	 * @param idArray
	 * @param serverService
	 * @param userId
	 */
	String addUserSubscribe(JSONArray idArray, 
			String userId);


	/**
	 * @param serverId
	 * @return
	 */
	UserSubscribe findByUserServerId(Integer serverId,String userId);


	/**
	 * @param serverId
	 * @param userId
	 * @return
	 */
	String addUserSubscribe(String serverId, String userId);
  


	/**
	 * @param id
	 * @return
	 */
	List<UserSubscribe> findByServerId(Integer id);
	
	
	
}
