package net.zdsoft.bigdata.extend.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.extend.data.entity.EventFavorite;
import net.zdsoft.bigdata.extend.data.entity.EventFavoriteParam;

import java.util.List;

/**
 * Created by wangdongdong on 2018/9/25 13:41.
 */
public interface EventFavoriteService extends BaseService<EventFavorite, String> {

    /**
     * 保存收藏夹
     * @param eventFavorite
     * @param eventFavoriteParams
     * @param eventGroupIds
     */
    void saveEventFavorite(EventFavorite eventFavorite, List<EventFavoriteParam> eventFavoriteParams, List<String> eventGroupIds) throws BigDataBusinessException;
    
    
    /**
     * 更新窗口尺寸大小
     * @param id
     * @param windowSize
     * @throws BigDataBusinessException
     */
	public void updateFavoriteWindowSize(String id, int windowSize)
			throws BigDataBusinessException ;

    /**
     * 根据用户和ids获取
     * @param userId
     * @param favoriteIds
     * @return
     */
    List<EventFavorite> findByUserIdAndFavoriteIds(String userId, List<String> favoriteIds);

    /**
     * 根据用户获取
     * @param userId
     * @return
     */
    List<EventFavorite> findByUserId(String userId);

    /**
     * 根据用户获取
     * @param userId
     * @return
     */
    List<EventFavorite> findByUserId(String userId, String favoriteName);

    void deleteEventFavorite(String id);
}
