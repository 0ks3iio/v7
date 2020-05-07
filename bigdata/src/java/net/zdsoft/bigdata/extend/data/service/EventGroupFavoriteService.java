package net.zdsoft.bigdata.extend.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.extend.data.entity.EventGroupFavorite;

/**
 * 事件预览组和收藏关系接口
 * 
 * @author jiangf
 *
 */
public interface EventGroupFavoriteService extends
		BaseService<EventGroupFavorite, String> {

	/**
	 * 根据组id获取收藏的图表list
	 * 
	 * @param groupId
	 * @return
	 */
	public List<EventGroupFavorite> findFavoriteListByGroupId(String groupId);

	/**
	 * 根据收藏id获取组list
	 * 
	 * @param favoriteId
	 * @return
	 */
	public List<EventGroupFavorite> findGroupListByFavoriteId(String favoriteId);

	/**
	 * 保存收藏夹数据到组
	 * 
	 * @param favoriteId
	 * @param favoriteList
	 */
	public void saveFavorite2Group(List<EventGroupFavorite> favoriteList);

	/**
	 * 根据收藏id删除数据
	 * 
	 * @param favoriteId
	 * @return
	 */
	public void deleteByFavoriteId(String favoriteId);

	/**
	 * 根据组id删除数据
	 * 
	 * @param groupId
	 * @return
	 */
	public void deleteByGroupId(String groupId);

	/**
	 * 根据groupid和favoriteId删除组下的组件
	 * 
	 * @param groupId
	 * @param favoriteId
	 */
	public void deleteByGroupAndFarvoriteId(String groupId, String favoriteId);
	
	/**
	 * 获取最大排序号
	 * @param groupId
	 * @return
	 */
	 public Integer getMaxOrderIdByGroupId(String groupId);
}
