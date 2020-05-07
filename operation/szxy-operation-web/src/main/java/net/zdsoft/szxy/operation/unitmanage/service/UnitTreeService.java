package net.zdsoft.szxy.operation.unitmanage.service;

import java.util.List;

import net.zdsoft.szxy.operation.dto.TreeNode;

/**
 * 查询单位树
 *
 * @author panlf   2019年1月15日
 */
public interface UnitTreeService {

    /**
     * 根据parentId查询子单位,如果参数为空,则查询所有顶级单位
     *
     * @param id
     * @return
     */
    List<TreeNode> findUnitByParentId(String id);

    /**
     * 根据unitName单位树
     *
     * @param unitName
     * @return
     */
    List<TreeNode> findUnitByUnitName(String unitName);
}
