package net.zdsoft.basedata.dingding.service;

public interface DingDingUserService {
	
	public void getUsers();
	
	/**
	 * 添加钉钉用户
	 * 
	 * @param content
	 */
	public void addUser(String unitId,String accessToken,String userId,String userName,String content);

	/**
	 * 修改钉钉用户
	 * 
	 * @param content
	 */
	public void updateUser(String unitId,String accessToken,String userName,String content);

	/**
	 * 删除钉钉用户
	 * 
	 * @param userId
	 */
	public void deleteUser(String unitId,String accessToken,String userName,String userId);
}
