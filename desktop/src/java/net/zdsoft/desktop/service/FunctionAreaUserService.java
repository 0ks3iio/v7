package net.zdsoft.desktop.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.desktop.entity.FunctionAreaUser;

public interface FunctionAreaUserService extends BaseService<FunctionAreaUser, String>{

	String CACHE_KEY = "function.area.user.";

	void save(FunctionAreaUser fau);

	/**
	 * 根据用户信息查找
	 * @param userId
	 * @param unitClass
	 * @return
	 */
	List<FunctionAreaUser> findByUserInfo(String userId, Integer unitClass);

	List<FunctionAreaUser> findByUserInfo(String userId, Integer unitClass, Integer layoutType);
	/**
	 * 软删，更新state not is_deleted</br>
	 * 更新的同时也会更新其他功能区的排序号，该功能区排序号移动到最后
	 * @param Id
	 */
	void updateState2SoftDeleteById(String Id,String userId,Integer unitClass);

	List<FunctionAreaUser> findDisplayOrderMoreThan(Integer displayOrder);


	/**
	 * @param userId
	 * @param functionId
	 * @return
	 */
	FunctionAreaUser findOwnerByFunctionAreaIds(String userId, List<String> functionId);

	/**
	 * @return
	 */
	int getMaxDisplayOrder(String... userId);

	/**
	 * 更新displayOrder
	 * @param order
	 * @param id
	 */
	void updateDisplayOrderAndState(int order, String id);

	/**
	 * 更新functionId
	 * @param functionId
	 * @param id
	 */
	void updateFunctionAreaId(String functionId, String id);

	/**
	 * @param userId
	 * @param id
	 * @return
	 */
	FunctionAreaUser findOwnerByFunctionAreaId(String userId, String id);




}
