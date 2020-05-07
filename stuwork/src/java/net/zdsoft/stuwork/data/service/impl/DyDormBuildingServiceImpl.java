package net.zdsoft.stuwork.data.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.stuwork.data.dao.DyDormBuildingDao;
import net.zdsoft.stuwork.data.entity.DyDormBuilding;
import net.zdsoft.stuwork.data.entity.DyDormCheckRole;
import net.zdsoft.stuwork.data.entity.DyDormRoom;
import net.zdsoft.stuwork.data.service.DyDormBedService;
import net.zdsoft.stuwork.data.service.DyDormBuildingService;
import net.zdsoft.stuwork.data.service.DyDormCheckRoleService;
import net.zdsoft.stuwork.data.service.DyDormRoomService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dyDormBuildingService")
public class DyDormBuildingServiceImpl extends BaseServiceImpl<DyDormBuilding, String> implements DyDormBuildingService{
	@Autowired
	private DyDormBuildingDao dyDormBuildingDao;
	@Autowired
	private DyDormCheckRoleService dyDormCheckRoleService;
	@Autowired
	private DyDormRoomService dyDormRoomService;
	@Autowired
	private DyDormBedService dyDormBedService;
	@Autowired
	private UserRemoteService userRemoteService;
	
	@Override
	public boolean getIsHaveStu(String unitId,String id){
		List<DyDormRoom> roomList=dyDormRoomService.findByName(unitId, null, id,null);
		boolean flag=false;
		Set<String> roomIds=new HashSet<String>();
		
		if(CollectionUtils.isNotEmpty(roomList)){
			for(DyDormRoom room:roomList){
				roomIds.add(room.getId());
			}
			if(CollectionUtils.isNotEmpty(dyDormBedService.getDormBedsByRoomIds(unitId, roomIds.toArray(new String[0]),null,null))){
				flag=true;
			}
		}
		return flag;
	}
	@Override
	public List<DyDormBuilding> findByUnitId(String unitId){
		return dyDormBuildingDao.findByUnitId(unitId);
	}
	@Override
	public List<DyDormBuilding> getBuildsByName(String unitId,String name){
		return dyDormBuildingDao.findByName(unitId, name);
	}
	@Override
	public DyDormBuilding getDormBuildingById(String id) {
		return dyDormBuildingDao.findById(id).orElse(null);
	}
	@Override
	public void deletedById(String id) {
		dyDormBuildingDao.deleteById(id);
	}

	@Override
	public List<DyDormBuilding> getDormBuildingsByUnitId(String unitId,String acadyear,String semester) {
		List<DyDormBuilding> buildingList=dyDormBuildingDao.findByUnitId(unitId);
		//List<DyDormCheckRole> checkRoleList=dyDormCheckRoleService.getCheckRolesBy(unitId, acadyear, semester);
		List<DyDormCheckRole> checkRoleList=dyDormCheckRoleService.findListBy(
				new String[]{"schoolId","acadyear","semester"}, new String[]{unitId, acadyear, semester});
		//key-buildingId   
		Map<String,List<DyDormCheckRole>> checkRoleMap=new HashMap<String,List<DyDormCheckRole>>();
		//得到所有的userIds
		Set<String> userIds=new HashSet<String>();
		if(CollectionUtils.isNotEmpty(checkRoleList)){
			for(DyDormCheckRole checkRole:checkRoleList){
				String buildingId=checkRole.getBuildingId();
				List<DyDormCheckRole> roleList=checkRoleMap.get(buildingId);
				if(CollectionUtils.isEmpty(roleList)){
					roleList=new ArrayList<DyDormCheckRole>();
				}
				roleList.add(checkRole);//同一个buildingId对应的数据 放在一个list中
				checkRoleMap.put(buildingId, roleList);
				if(StringUtils.isNotBlank(checkRole.getUserId())){
					userIds.add(checkRole.getUserId());
				}
			}
		}
		//给buildingList 添加用户user的name
		if(CollectionUtils.isNotEmpty(buildingList)){
			Map<String,User> userMap=null;
			if(CollectionUtils.isNotEmpty(userIds)){
				List<User> userList=SUtils.dt(userRemoteService.findListByIds(userIds.toArray(new String[0])),new TR<List<User>>(){});
				userMap=EntityUtils.getMap(userList, "id");
			}else{
				userMap=new HashMap<String,User>();
			}
			for(DyDormBuilding building:buildingList){
				String userIdsStr=null;
				String userNamesStr=null;
				List<DyDormCheckRole> roleList=checkRoleMap.get(building.getId());
				if(CollectionUtils.isNotEmpty(roleList)){
					for(DyDormCheckRole checkRole:roleList){
						User user=userMap.get(checkRole.getUserId());
						if(user==null){
							continue;
						}
						if(StringUtils.isBlank(userNamesStr)){
							userNamesStr=user.getRealName();
						}else{
							userNamesStr+=", "+user.getRealName();
						}
						
						if(StringUtils.isBlank(userIdsStr)){
							userIdsStr=checkRole.getUserId();
						}else{
							userIdsStr+=","+checkRole.getUserId();
						}
					}
					building.setUserIds(userIdsStr);
					building.setUserNames(userNamesStr);
					//building.setCheckRoleList(roleList);
				}
			}
		}
		return buildingList;
	}

	@Override
	protected BaseJpaRepositoryDao<DyDormBuilding, String> getJpaDao() {
		return dyDormBuildingDao;
	}

	@Override
	protected Class<DyDormBuilding> getEntityClass() {
		return DyDormBuilding.class;
	}

}
