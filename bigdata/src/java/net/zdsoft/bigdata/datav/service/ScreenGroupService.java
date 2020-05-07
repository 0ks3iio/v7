package net.zdsoft.bigdata.datav.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.datav.entity.ScreenGroup;

import java.util.List;

/**
 * @author shenke
 * @since 2018/11/14 上午9:33
 */
public interface ScreenGroupService extends BaseService<ScreenGroup, String> {

    /**
     * 查询某个人的分组
     * @param userId 用户ID
     * @return List
     */
    List<ScreenGroup> getGroupsByUserId(String userId);
}
