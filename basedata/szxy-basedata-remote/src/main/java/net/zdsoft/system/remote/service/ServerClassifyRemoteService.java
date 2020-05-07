package net.zdsoft.system.remote.service;

import net.zdsoft.system.entity.server.ServerClassify;
import net.zdsoft.system.remote.dto.UnitServerClassify;

import java.util.List;

/**
 * @author shenke
 * @since 2019/3/13 上午11:07
 */
public interface ServerClassifyRemoteService {

    /**
     * 获取指定单位的系统分类明细,
     * 若当前单位没有定义分类标准则取当前单位的顶级单位的分类标准
     * 如果当前单位的顶级单位没有分类标准则取系统的分类标准
     * @param unitId 单位ID
     * @return
     */
    List<UnitServerClassify> getClassifyByUnitId(String unitId);
}
