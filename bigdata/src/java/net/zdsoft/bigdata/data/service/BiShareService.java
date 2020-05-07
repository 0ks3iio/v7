package net.zdsoft.bigdata.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.data.entity.BiShare;
import net.zdsoft.framework.entity.Pagination;

import java.util.List;

public interface BiShareService extends BaseService<BiShare, String> {

	/**
	 * 根据分享用户获取分享list
	 * 
	 * @param shareUserId
	 * @param pagination
	 * @return
	 */
	public List<BiShare> findBiFavoriteListByShareUserId(String shareUserId,
			Pagination pagination);

	/**
	 * 根据被分享用户获取分享list
	 * 
	 * @param beSharedUserId
	 * @param pagination
	 * @return
	 */
	public List<BiShare> findBiFavoriteListByBeShareUserId(
			String beSharedUserId, Pagination pagination);

	/**
	 * 删除分析
	 * 
	 * @param userId
	 * @param businessId
	 */
	public void deleteBiShareByShardUserIdAndBusinessId(String userId,
			String businessId);

	/**
	 * 保存分享记录
	 * 
	 * @param businessId
	 * @param businessType
	 * @param businessName
	 * @param userId
	 * @param beSharedUserIdArray
	 */

	public void addBiShares(String businessId, String businessType,
			String businessName, String userId, String[] beSharedUserIdArray);

	/**
	 * 保存分享
	 * 
	 * @param bishareList
	 */
	public void addBiShares(List<BiShare> bishareList);

	/**
	 * 获取我分享的所有个数
	 * @param userId
	 * @return
	 */
	public Integer findMyShareByUserId(String userId);

	/**
	 * 获取分享给我的所有个数
	 * @param userId
	 * @return
	 */
	public Integer findBeShareByUserId(String userId);
}
