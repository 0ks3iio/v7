/*
 * Project: v7
 * Author : shenke
 * @(#) CustomRoleServiceImpl.java Created on 2016-10-13
 * @Copyright (c) 2016 ZDSoft Inc. All rights reserved
 */
package net.zdsoft.basedata.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.dao.CustomRoleDao;
import net.zdsoft.basedata.entity.CustomRole;
import net.zdsoft.basedata.entity.CustomRoleUser;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.service.CustomRoleService;
import net.zdsoft.basedata.service.CustomRoleUserService;
import net.zdsoft.basedata.service.UserService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.UuidUtils;

/**
 * @description:
 * @author: shenke
 * @version: 1.0
 * @date: 2016-10-13下午7:03:02
 */
@Service("customRoleService")
public class CustomRoleServiceImpl extends BaseServiceImpl<CustomRole, String> implements CustomRoleService {

    @Autowired
    private CustomRoleDao customRoleDao;
    @Autowired
    private CustomRoleUserService customRoleUserService;
    @Autowired
    private UserService userService;

    @Override
    protected BaseJpaRepositoryDao<CustomRole, String> getJpaDao() {
        return customRoleDao;
    }

    @Override
    protected Class<CustomRole> getEntityClass() {
        return CustomRole.class;
    }


    @Override
    public void saveAllEntitys(CustomRole... customRole) {
        customRoleDao.saveAll(checkSave(customRole));
    }

    @Override
    public List<CustomRole> findBySubsystem(final String subsystem) {
        return customRoleDao.findAll(new Specification<CustomRole>() {

            @Override
            public Predicate toPredicate(Root<CustomRole> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return query.where(cb.equal(root.<String> get("subsystems"), subsystem + ",")).getRestriction();
            }
        });
    }

    
    public List<CustomRole> findByUnitIdAndSubsystem(final String unitId, final String subsystem){
//    	List<CustomRole> list=RedisUtils.getObject("CUSTOMROLE"+unitId, RedisUtils.TIME_ONE_WEEK, new TypeReference<List<CustomRole>>(){}, new RedisInterface<List<CustomRole>>(){
//			@Override
//			public List<CustomRole> queryData() {
//				return customRoleDao.findByUnitIdAndSubsystem(unitId,subsystem+ ",");
//			}
//        });
//    	if(CollectionUtils.isEmpty(list)) {
//    		list=customRoleDao.findByUnitIdAndSubsystem(unitId,subsystem+ ",");
//    		RedisUtils.setObject("CUSTOMROLE"+unitId, list);
//    	}
    	List<CustomRole> list=customRoleDao.findByUnitIdAndSubsystem(unitId,subsystem+ ",");
    	return list;
    }
    
    //强制走主库
	@Override
	@Transactional
	public List<CustomRole> findByUnitIdAndSubsystem(String unitId, String subsystem, boolean isMakeUser) {
		//注意点：有新的权限角色插入数据库 需要清空缓存 目前取消放入缓存
		//CustomRole  subsystems一般保存subsystem+","
        //加分布式锁以防止可能发生的重复初始化现象
        RedisUtils.hasLocked("customRoleInitLock_" + unitId + "_" + subsystem);
		List<CustomRole> list=findByUnitIdAndSubsystem(unitId,subsystem);

		Set<String> roleCodes=new HashSet<>();
		if(CollectionUtils.isNotEmpty(list)) {
			roleCodes=EntityUtils.getSet(list, e->e.getRoleCode());
			if(isMakeUser) {
				//填入用户
				Set<String> idSet = EntityUtils.getSet(list, e->e.getId());
				List<CustomRoleUser> roleUserList = customRoleUserService.findListByIn("roleId", idSet.toArray(new String[] {}));
				if(CollectionUtils.isNotEmpty(roleUserList)) {
					Set<String> userIdset = EntityUtils.getSet(roleUserList,  e->e.getUserId());
					Map<String, List<String>> userByRoleMap = EntityUtils.getListMap(roleUserList, "roleId", "userId");
					List<User> userList = userService.findListByIdIn(userIdset.toArray(new String[] {}));
					if(CollectionUtils.isNotEmpty(userList)) {
						Map<String, String> usermap = userList.parallelStream().collect(Collectors.toMap(User::getId, user -> user.getRealName()));
						for(CustomRole role:list) {
							StringBuffer userIds=new StringBuffer("");
							StringBuffer userNames=new StringBuffer("");
							if(userByRoleMap.containsKey(role.getId())) {
								for(String userId:userByRoleMap.get(role.getId())) {
									if(usermap.containsKey(userId)) {
										userIds.append(","+userId);
										userNames.append(","+usermap.get(userId));
									}
								}
							}
							
							if(StringUtils.isNotBlank(userIds.toString())){
								role.setUserIds(userIds.toString().substring(1));
								role.setUserNames(userNames.toString().substring(1));
							}
						}
					}
					
					
				}
			}
		}else {
			list=new ArrayList<>();
		}
		//增加其他新增的code
		List<CustomRole> tempList=findByUnitIdAndSubsystem(BaseConstants.ZERO_GUID,subsystem);

		//初始化
		if(CollectionUtils.isNotEmpty(tempList)) {
			List<CustomRole> insertList=new ArrayList<>();
			CustomRole roleTemp;
			for (CustomRole customRole : tempList) {
				if(roleCodes.contains(customRole.getRoleCode())) {
					continue;
				}
				roleTemp=new CustomRole();
                roleTemp.setId(UuidUtils.generateUuid());
                roleTemp.setUnitId(unitId);
                roleTemp.setRoleName(customRole.getRoleName());
                roleTemp.setRoleCode(customRole.getRoleCode());
                roleTemp.setSubsystems(customRole.getSubsystems());
                roleTemp.setOrderId(customRole.getOrderId());
                roleTemp.setType(customRole.getType());
                roleTemp.setRemark(customRole.getRemark());
                insertList.add(roleTemp);
            }
			if(CollectionUtils.isNotEmpty(insertList)) {
				customRoleDao.saveAll(insertList);
				list.addAll(insertList);
				//去除缓存
//				RedisUtils.del("CUSTOMROLE"+unitId);
			}
		}
		RedisUtils.unLock("customRoleInitLock_" + unitId + "_" + subsystem);
			 
		return list;
	}

	@Override
	public boolean checkUserRole(String unitId, String subsystem, String roleCode, String userId) {
		if(StringUtils.isBlank(userId)) {
			return false;
		}
		List<CustomRole> list=customRoleDao.findByUnitIdAndSubsystemAndRoleCode(unitId,subsystem+ ",", roleCode);
		if(CollectionUtils.isNotEmpty(list)) {
			Set<String> idSet = EntityUtils.getSet(list,  e->e.getId());
			List<CustomRoleUser> roleUserList = customRoleUserService.findListByIn("roleId", idSet.toArray(new String[] {}));
			if(CollectionUtils.isNotEmpty(roleUserList)) {
				Set<String> userIdset = EntityUtils.getSet(roleUserList,  e->e.getUserId());
				if(userIdset.contains(userId)) {
					return true;
				}
			}
			
		}
		return false;
	}

	@Override
	public List<String> findUserIdListByUserRole(String unitId, String subsystem, String roleCode) {
		
		List<String> list=RedisUtils.getObject("CUSTOMROLE"+unitId+subsystem+roleCode, RedisUtils.TIME_ONE_HOUR, new TypeReference<List<String>>(){}, new RedisInterface<List<String>>(){
			@Override
			public List<String> queryData() {
				List<String> returnList=new ArrayList<>();
				List<CustomRole> roleList=customRoleDao.findByUnitIdAndSubsystemAndRoleCode(unitId,subsystem+ ",", roleCode);
				if(CollectionUtils.isNotEmpty(roleList)) {
					Set<String> idSet = EntityUtils.getSet(roleList,  e->e.getId());
					List<CustomRoleUser> roleUserList = customRoleUserService.findListByIn("roleId", idSet.toArray(new String[] {}));
					if(CollectionUtils.isNotEmpty(roleUserList)) {
						Set<String> userIdset = EntityUtils.getSet(roleUserList,  e->e.getUserId());
						returnList.addAll(userIdset);
					}
				}
				return returnList;
			}
        });
    	if(CollectionUtils.isEmpty(list)) {
    		list=new ArrayList<>();
			List<CustomRole> roleList=customRoleDao.findByUnitIdAndSubsystemAndRoleCode(unitId,subsystem+ ",", roleCode);
			if(CollectionUtils.isNotEmpty(roleList)) {
				Set<String> idSet = EntityUtils.getSet(roleList,  e->e.getId());
				List<CustomRoleUser> roleUserList = customRoleUserService.findListByIn("roleId", idSet.toArray(new String[] {}));
				if(CollectionUtils.isNotEmpty(roleUserList)) {
					Set<String> userIdset = EntityUtils.getSet(roleUserList,  e->e.getUserId());
					list.addAll(userIdset);
				}
			}
    		RedisUtils.setObject("CUSTOMROLE"+unitId+subsystem+roleCode, list);
    	}
    	return list;

	}

	@Override
	public void deleteById(String id) {
		customRoleDao.deleteById(id);
	}

}
