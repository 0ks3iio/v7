package net.zdsoft.eclasscard.data.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.eclasscard.data.dao.EccAttenceNoticeUserDao;
import net.zdsoft.eclasscard.data.entity.EccAttenceNoticeUser;
import net.zdsoft.eclasscard.data.service.EccAttenceNoticeUserService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
@Service("eccAttenceNoticeUserService")
public class EccAttenceNoticeUserServiceImpl extends BaseServiceImpl<EccAttenceNoticeUser, String> implements EccAttenceNoticeUserService {
	@Autowired
	private EccAttenceNoticeUserDao eccAttenceNoticeUserDao;
	@Autowired
	private UserRemoteService userRemoteService;
	@Override
	protected BaseJpaRepositoryDao<EccAttenceNoticeUser, String> getJpaDao() {
		return eccAttenceNoticeUserDao;
	}

	@Override
	protected Class<EccAttenceNoticeUser> getEntityClass() {
		return EccAttenceNoticeUser.class;
	}
	
	@Override
	public List<EccAttenceNoticeUser> findUserWhithName(String type){
		List<EccAttenceNoticeUser>  noticeUsers = findListBy("type", type);
		Set<String> userIds = Sets.newHashSet();
		for(EccAttenceNoticeUser noticeUser : noticeUsers){
			userIds.add(noticeUser.getUserId());
		}
		List<User> users = SUtils.dt(userRemoteService.findListByIds(userIds.toArray(new String[0])),new TR<List<User>>());
		Map<String,User> userMap = Maps.newHashMap();
		for(User user:users){
			userMap.put(user.getId(), user);
		}
		for(EccAttenceNoticeUser noticeUser : noticeUsers){
			if(userMap.containsKey(noticeUser.getUserId())){
				User user = userMap.get(noticeUser.getUserId());
				if(user!=null){
					noticeUser.setUserName(user.getRealName());
				}
			}
		}
		return noticeUsers;
	}

	@Override
	public void updateNoticeUser(String type,String[] userIds,String unitId) {
		List<EccAttenceNoticeUser> noticeUsers = findListBy("unitId", unitId);
		List<EccAttenceNoticeUser> deletenoticeUsers = Lists.newArrayList();
		for(EccAttenceNoticeUser noticeUser:noticeUsers){
			if(type.equals(noticeUser.getType())){
				deletenoticeUsers.add(noticeUser);
			}
		}
		if(deletenoticeUsers.size()>0){
			deleteAll(deletenoticeUsers.toArray(new EccAttenceNoticeUser[deletenoticeUsers.size()]));
		}
		noticeUsers = new ArrayList<EccAttenceNoticeUser>();
		for(String userId:userIds){
			EccAttenceNoticeUser noticeUser = new EccAttenceNoticeUser();
			noticeUser.setId(UuidUtils.generateUuid());
			noticeUser.setType(type);
			noticeUser.setUserId(userId);
			noticeUser.setUnitId(unitId);
			noticeUsers.add(noticeUser);
		}
		saveAll(noticeUsers.toArray(new EccAttenceNoticeUser[noticeUsers.size()]));
	}

	@Override
	public List<EccAttenceNoticeUser> findByUnitIdAndType(String unitId,
			String type) {
		return eccAttenceNoticeUserDao.findByUnitIdAndType(unitId,type);
	}

}
