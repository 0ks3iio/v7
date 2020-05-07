package net.zdsoft.desktop.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.desktop.dao.UserSubscribeDao;
import net.zdsoft.desktop.entity.UserSubscribe;
import net.zdsoft.desktop.service.UserSubscribeService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.Objects;
import net.zdsoft.framework.utils.UuidUtils;

/**
 * @author yangsj  2017-6-12下午6:15:52
 */
@Service("userSubscribeService")
public class UserSubscribeServiceImpl extends BaseServiceImpl<UserSubscribe, String> implements UserSubscribeService {
    
	@Autowired private UserSubscribeDao userSubscribeDao;
	
	
	@Override
	protected BaseJpaRepositoryDao<UserSubscribe, String> getJpaDao() {
		// TODO Auto-generated method stub
		return userSubscribeDao;
	}

	@Override
	protected Class<UserSubscribe> getEntityClass() {
		// TODO Auto-generated method stub
		return UserSubscribe.class;
	}

	@Override
	public List<UserSubscribe> findByUserId(String userId) {
		// TODO Auto-generated method stub
		return userSubscribeDao.findByUserId(userId);
	}

    
	private UserSubscribe addSubscribe(Integer serverId,String userId){
			UserSubscribe userSubscribe = new UserSubscribe();
			userSubscribe.setServerId(serverId);
			userSubscribe.setStatus(UserSubscribe.STATUS_TURNON);
			userSubscribe.setUserId(userId);
			userSubscribe.setId(UuidUtils.generateUuid());
			return userSubscribe;
	}

	@Override
	public String addUserSubscribe(JSONArray idArray,
			 String userId) {
		String msg = "success";
		try {
			// TODO Auto-generated method stub
			List<UserSubscribe> listUser  = new ArrayList<UserSubscribe>();
			for (int i = 0; i < idArray.size(); i++) {
				Integer serverId = Integer.valueOf((String) idArray.get(i)) ;
				if(Objects.isNull(userSubscribeDao.findByUserServerId(serverId,userId))){
					listUser.add(addSubscribe(serverId,userId));
				}
			}
			saveAll(EntityUtils.toArray(listUser, UserSubscribe.class));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			msg = "error";
		}
		return msg;
		
	}

	@Override
	public UserSubscribe findByUserServerId(Integer serverId,String userId) {
		// TODO Auto-generated method stub

		return userSubscribeDao.findByUserServerId(serverId,userId);
	}

	@Override
	public String addUserSubscribe(String serverId, String userId) {
		String msg = "success";
		try {
			if(Objects.isNull(userSubscribeDao.findByUserServerId(Integer.valueOf(serverId),userId))){
				userSubscribeDao.save(addSubscribe(Integer.valueOf(serverId),userId));
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			msg = "error";
		}
		return msg;
	}


	@Override
	public List<UserSubscribe> findByServerId(Integer id) {
		// TODO Auto-generated method stub
		return userSubscribeDao.findByServerId(id);
	}
	
	
	
}
