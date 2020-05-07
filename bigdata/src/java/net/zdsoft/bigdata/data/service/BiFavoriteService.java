
package net.zdsoft.bigdata.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.data.entity.BiFavorite;
import net.zdsoft.framework.entity.Pagination;


public interface BiFavoriteService extends BaseService<BiFavorite, String> {
	
	/**
	 * 根据用户获取收藏list
	 * @param userId
	 * @param pagination
	 * @return
	 */
	public List<BiFavorite> findBiFavoriteListByUserId(String userId,Pagination pagination);
	
	/**
	 * 根据用户和业务id获取收藏记录
	 * @param userId
	 * @param businessId
	 * @return
	 */
	public List<BiFavorite> findBiFavoriteListByUserIdAndBusinessId(String userId,String businessId);
	
	/**
	 * 根据用户和业务id删除收藏记录
	 * @param userId
	 * @param businessId
	 */
	public void deleteBiFavoriteByUserIdAndBusinessId(String userId,String businessId);
	
	/**
	 * 保存收藏
	 * @param favorite
	 */
	public void addBiFavorite(BiFavorite favorite);
	
	
	/**
	 * 保存收藏
	 * @param businessId
	 * @param businessType
	 * @param businessName
	 * @param userId
	 */
	public void addBiFavorite(String businessId,String businessType,String businessName,String userId);

	/**
	 * 获取所有的分享个数
	 * @param userId
	 * @return
	 */
	public Integer findAllByUserId(String userId);
}
