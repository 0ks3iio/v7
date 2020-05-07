package net.zdsoft.eclasscard.data.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.eclasscard.data.dao.EccPermissionDao;
import net.zdsoft.eclasscard.data.entity.EccPermission;
import net.zdsoft.eclasscard.data.service.EccPermissionService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
@Service("eccPermissionService")
public class EccPermissionServiceImpl extends BaseServiceImpl<EccPermission, String> implements
		EccPermissionService {

	@Autowired
	private EccPermissionDao eccPermissionDao;
	@Autowired
	private UserRemoteService userRemoteService;
	
	@Override
	protected BaseJpaRepositoryDao<EccPermission, String> getJpaDao() {
		return eccPermissionDao;
	}

	@Override
	protected Class<EccPermission> getEntityClass() {
		return EccPermission.class;
	}

	@Override
	public void savePermission(String eccName, String eccNames, String[] userIds, Boolean isAll,String unitId) {
		List<EccPermission> permissions = Lists.newArrayList();
		if (isAll) {
			if (userIds!=null) {
				String[] eccNamesl = eccNames.split(",");
				Map<String,List<EccPermission>> permissionMap = findbyEccInfoMap(eccNamesl,unitId);
				List<String> userList = Lists.newArrayList();
				for (String oneEccName : eccNamesl) {
					userList = EntityUtils.getList(permissionMap.get(oneEccName), "userId");
					for (String userId : userIds) {
						if (userList.contains(userId)) {
							continue;
						}
						EccPermission permission = new EccPermission();
						permission.setId(UuidUtils.generateUuid());
						permission.setEccName(oneEccName);
						permission.setUserId(userId);
						permission.setUnitId(unitId);
						permissions.add(permission);
					}
				}
			}
		} else {
			eccPermissionDao.deleteByEccName(eccName,unitId);
			if(userIds!=null){
				for(String userId:userIds){
					EccPermission permission = new EccPermission();
					permission.setId(UuidUtils.generateUuid());
					permission.setEccName(eccName);
					permission.setUserId(userId);
					permission.setUnitId(unitId);
					permissions.add(permission);
				}
			}
		}
		if(permissions.size()>0){
			saveAll(permissions.toArray(new EccPermission[permissions.size()]));
		}
		
	}

	@Override
	public Map<String, List<EccPermission>> findbyEccInfoMap(String[] infoNames,String unitId) {
		Map<String, List<EccPermission>> pMap = Maps.newHashMap();
		if(infoNames.length<1){
			return pMap;
		}
		List<EccPermission> permissions = findListByIn("eccName", infoNames);
		fillUserName(permissions);
		for(EccPermission permission:permissions){
			List<EccPermission> eps = pMap.get(permission.getEccName());
			if(eps==null){
				eps = Lists.newArrayList();
			}
			eps.add(permission);
			pMap.put(permission.getEccName(), eps);
		}
		return pMap;
	}

	private void fillUserName(List<EccPermission> permissions){
		if(CollectionUtils.isEmpty(permissions)){
			return;
		}
		Set<String> userIds = Sets.newHashSet();
		for(EccPermission permission:permissions){
			userIds.add(permission.getUserId());
		}
		List<User> users = SUtils.dt(userRemoteService.findListByIds(userIds.toArray(new String[userIds.size()])),new TR<List<User>>() {});
		Map<String,String> userMap = EntityUtils.getMap(users, "id", "realName");
		for(EccPermission permission:permissions){
			if(userMap.containsKey(permission.getUserId())){
				permission.setUserName(userMap.get(permission.getUserId()));
			}
		}
	}

	@Override
	public void deleteByEccName(String eccName,String unitId) {
		eccPermissionDao.deleteByEccName(eccName,unitId);
	}

}
