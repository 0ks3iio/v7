package net.zdsoft.bigdata.datav.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.datav.entity.Screen;

import java.util.Date;
import java.util.List;

/**
 * @author shenke
 * @since 2018/9/26 15:52
 */
public interface ScreenService extends BaseService<Screen, String> {

    long countAll();

    long countByTime(Date start, Date end);

    void updateName(String id, String name);

    /**
     * 批量删除
     * @param ids id
     */
    void batchDelete(String[] ids);

    /**
     * 更新权限类型
     * 会删除其他之前已经授权的关系数据
     * @param id id
     * @param orderType 权限类型
     */
    void updateOrderType(String id, Integer orderType);

    /**
     * 所有的screen 包括私人的
     * @param unitId 单位ID
     * @return
     */
    List<Screen> getScreensByUnitId(String unitId, String userId);

    /**
     * 所有的screen 不包括私人的
     * @param unitId 单位ID
     * @param userId 用户ID
     * @return
     */
    List<Screen> getScreensByUnitIdAndUserId(String unitId, String userId);

    List<Screen> getScreensForSuper(String userId);

    List<Screen> getScreensByUnitIdAndUserIdForGroup(String unitId, String userId, String[] screenIds);

    /**
     * 请不要调用这个方法，调用那个noTransactionl的方法
     *
     * @param id
     */
    @Deprecated
    void cloneScreen(String id, String name, String userId);

    /**
     * 复制一个大屏，包括所有的组件
     * @param id
     * @param name
     */
    void cloneNoTransactional(String id, String name, String userId);

    /**
     * 根据模版创建大屏
     * @param templateId
     * @param name 要创建大屏的名称
     * @param unitId
     * @return
     */
    String createFromLibrary(String templateId, String name, String unitId, String userId);

    /**
     * 计算有权限查看的数量
     * @param unitId
     * @param userId
     * @return
     */
    long countScreensForQuery(String unitId, String userId);

}
