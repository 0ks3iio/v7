package net.zdsoft.desktop.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.desktop.constant.DeskTopConstant;
import net.zdsoft.desktop.constant.WenXunConstant;
import net.zdsoft.desktop.dao.FunctionAreaUserDao;
import net.zdsoft.desktop.entity.FunctionArea;
import net.zdsoft.desktop.entity.FunctionAreaUser;
import net.zdsoft.desktop.service.FunctionAreaService;
import net.zdsoft.desktop.service.FunctionAreaUserService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.remote.service.SysOptionRemoteService;

@Service
public class FunctionAreaUserServiceImpl extends BaseServiceImpl<FunctionAreaUser, String>
		implements FunctionAreaUserService {
	
	@Autowired
	private FunctionAreaUserDao functionAreaUserDao;
	@Autowired
	private FunctionAreaService functionAreaService;
	@Autowired
	@Lazy
	private SysOptionRemoteService sysOptionRemoteService;
	@Autowired
	@Lazy
	private UserRemoteService userRemoteService;

	@Override
	protected BaseJpaRepositoryDao<FunctionAreaUser, String> getJpaDao() {
		return functionAreaUserDao;
	}

	@Override
	protected Class<FunctionAreaUser> getEntityClass() {
		return FunctionAreaUser.class;
	}

	@Override
	public void save(FunctionAreaUser fau) {
		functionAreaUserDao.save(fau);
	}

	//默认功能区，顶级默认 单位默认
	public List<FunctionAreaUser> findDefaultAreaUsers(String unitId) {

		return functionAreaUserDao.findByCustomerIds(unitId, Constant.GUID_ZERO);
	}

	@Override
	public List<FunctionAreaUser> findByUserInfo(String userId, Integer unitClass) {
		User user = userRemoteService.findOneObjectById(userId);
		if ( user == null ) {
			return Collections.emptyList();
		}
		initDefaultForCurrentUser(user, unitClass);
		return findByCurrentUser(user, unitClass, false);
	}

	private boolean belongToCurrentUser(FunctionArea area, User user, Integer unitClass) {
		boolean accord = area != null;
		accord = accord && (area.getUnitClass() == null || area.getUnitClass().equals(unitClass));
		accord = accord && !FunctionArea.STATE_ILLEGAL.equals(area.getState());
		accord = accord && (StringUtils.isBlank(area.getUserType()) || net.zdsoft.framework.utils.StringUtils.containAny(area.getUserType(), user.getOwnerType()));
		return accord;
	}

	/**
	 * 查找当前用户的功能区
	 * @param user
	 * @param unitClass
	 * @return
	 */
	private List<FunctionAreaUser> findByCurrentUser(final User user, final Integer unitClass, final boolean delete) {
		List<FunctionAreaUser> currentAreaUsers = functionAreaUserDao.findByUserIdAndNotZeroId(user.getId());
		Set<String> currentAreaIdSet = EntityUtils.getSet(currentAreaUsers, FunctionAreaUser::getFunctionAreaId);
		final Map<String, FunctionArea> areaMap = functionAreaService.findMapByIdIn(currentAreaIdSet.toArray(new String[currentAreaIdSet.size()]));
		return EntityUtils.filter2(currentAreaUsers, functionAreaUser -> {
			FunctionArea area = areaMap.get(functionAreaUser.getFunctionAreaId());
			if ( delete ) {
				return belongToCurrentUser(area, user, unitClass);
			} else {
				return belongToCurrentUser(area, user, unitClass) && !functionAreaUser.getState().equals(FunctionAreaUser.STATE_ILLEGAL);
			}
		});
	}

	//为每个用户初始化默认功能区
	private void initDefaultForCurrentUser(User user, Integer unitClass) {
		//超管，顶级管理员不初始化
		if (Integer.valueOf(User.OWNER_TYPE_SUPER).equals(user.getOwnerType())
				|| Integer.valueOf(User.USER_TYPE_TOP_ADMIN).equals(user.getUserType()) ) {
			return ;
		}

		//ZERO 当前单位默认功能区
		List<FunctionAreaUser> defaultAreaUsers = findDefaultAreaUsers(user.getUnitId());
		boolean orderNull = false;
		for (FunctionAreaUser defaultAreaUser : defaultAreaUsers) {
			if ( defaultAreaUser.getDisplayOrder() == null ) {
				orderNull = true;
				break;
			}
		}
		if ( orderNull ) {
			Collections.sort(defaultAreaUsers, (o1, o2) -> {
				int order1 = o1.getDisplayOrder() == null ? 0 : o1.getDisplayOrder();
				int order2 = o2.getDisplayOrder() == null ? 0 : o2.getDisplayOrder();
				return order1 - order2;
			});
			
			for (FunctionAreaUser areaUser : defaultAreaUsers) {
				areaUser.setDisplayOrder(defaultAreaUsers.indexOf(areaUser) + 1);
			}
			functionAreaUserDao.saveAll(defaultAreaUsers);
		}

		List<FunctionAreaUser> currentAreaUsers = findByCurrentUser(user, unitClass, true);

		Set<String> currentAreaIdSet = EntityUtils.getSet(currentAreaUsers, FunctionAreaUser::getFunctionAreaId);


		Set<String> defaultAreaIdSet = EntityUtils.getSet(defaultAreaUsers, FunctionAreaUser::getFunctionAreaId);
		Map<String, FunctionArea> areaMap = functionAreaService.findMapByIdIn(defaultAreaIdSet.toArray(new String[defaultAreaIdSet.size()]));
		List<FunctionAreaUser> needAddList = Lists.newArrayList();
		for (FunctionAreaUser areaUser : defaultAreaUsers) {
			if ( currentAreaIdSet.contains(areaUser.getFunctionAreaId()) ) {
				continue;  //已经有了
			}
			
			FunctionArea area = areaMap.get(areaUser.getFunctionAreaId());
			//判断是否是文轩账号登录 --右侧桌面只显示一个 我的课表
			String userName = user.getUsername();
	        if(StringUtils.startsWithIgnoreCase(userName, WenXunConstant.WENXUN_BEFORE_USERNAME_VALUE)) {
	           if(DeskTopConstant.MY_STUDENT_SCHEDULE.equals(area.getType())) {
	        	   needAddList.add(areaUser);
	           }
	        }else {
	        	//状态、单位类型、用户类型 不匹配
	        	if ( belongToCurrentUser(area, user, unitClass) ) {
	        		needAddList.add(areaUser);
	        	}
	        }
		}
		boolean areaUserSetOpen = BooleanUtils.toBoolean(sysOptionRemoteService.findValue(DeskTopConstant.FUNCTION_AREA_USER_SET_OPEN));
		if ( areaUserSetOpen ) {
			for (FunctionAreaUser areaUser : needAddList) {
				areaUser.setCustomerId(user.getId());
				areaUser.setId(UuidUtils.generateUuid());
			}
			if (!needAddList.isEmpty()) {
				needAddList.addAll(currentAreaUsers);
			}
			//对needList 排序
			Collections.sort(needAddList, new Comparator<FunctionAreaUser>() {
				@Override
				public int compare(FunctionAreaUser o1, FunctionAreaUser o2) {

					return (o1.getDisplayOrder() != null ? o1.getDisplayOrder():0) - (o2.getDisplayOrder() !=null ? o2.getDisplayOrder() : 0);
				}
			});
			//needAddList.sort(Comparator.comparingInt(FunctionAreaUser::getDisplayOrder));
			for (FunctionAreaUser areaUser : needAddList) {
				areaUser.setDisplayOrder(needAddList.indexOf(areaUser)+1);
			}
			saveAll(needAddList.toArray(new FunctionAreaUser[0]));
		}
	}

	@Override
	public List<FunctionAreaUser> findByUserInfo(String userId, Integer unitClass, Integer layoutType) {
		List<FunctionAreaUser> all = findByUserInfo(userId, unitClass);
		if ( layoutType == null ) {
			return all;
		}
		List<FunctionAreaUser> functionAreaUsers = Lists.newArrayList();
		for (FunctionAreaUser areaUser : all) {
			if ( areaUser.getLayoutType().equals(layoutType) ) {
				functionAreaUsers.add(areaUser);
			}
		}
		return functionAreaUsers;
	}

	@Override
	public void updateState2SoftDeleteById(String id,String userId,Integer unitClass) {
		FunctionAreaUser functionAreaUser = findOne(id);
		User user =SUtils.dc(userRemoteService.findOneById(userId), User.class);
		List<FunctionAreaUser> functionAreaUsers = findByCurrentUser(user,unitClass, false);
		for (FunctionAreaUser fau : functionAreaUsers) {
			if ( fau.getDisplayOrder() > functionAreaUser.getDisplayOrder() ) {
				fau.setDisplayOrder(fau.getDisplayOrder() - 1);
			}
			if ( fau.getId().equals(id) ) {
				functionAreaUser.setState(-1);
			}
		}
		saveAll(EntityUtils.toArray(functionAreaUsers,FunctionAreaUser.class));
	}

	@Override
	public List<FunctionAreaUser> findDisplayOrderMoreThan(Integer displayOrder) {
		return functionAreaUserDao.findDisplayOrderMoreThan(displayOrder);
	}


	@Override
	public FunctionAreaUser findOwnerByFunctionAreaIds(String userId,
	                                                   List<String> functionId) {
		return functionAreaUserDao.findOwnerByFunctionAreaIds(userId,functionId);
	}

	@Override
	public int getMaxDisplayOrder(String ... userId) {
		Integer a = functionAreaUserDao.getMaxDisplayOrder(userId);
		if(a == null){
			return 0;
		}
		return a;
	}

	@Override
	public void updateDisplayOrderAndState(int order, String id) {
		functionAreaUserDao.updateDisplayOrderAndState(order,id);
	}

	@Override
	public void updateFunctionAreaId(String functionId, String id) {
		functionAreaUserDao.updateFunctionAreaId(functionId,id);
	}

	@Override
	public FunctionAreaUser findOwnerByFunctionAreaId(String userId,
			String funId) {
		return functionAreaUserDao.findOwnerByFunctionAreaId(userId,funId);
	}

}
