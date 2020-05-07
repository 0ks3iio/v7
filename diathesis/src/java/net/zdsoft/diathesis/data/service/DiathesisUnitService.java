package net.zdsoft.diathesis.data.service;

import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.diathesis.data.dto.DiathesisTreeDto;

import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/7/29 9:22
 */
public interface DiathesisUnitService  {
    public List<Unit> findAllChildUnit(String unitId);

    /**
     * 转成树结构
     * @param unitList
     * @return
     */
    public List<DiathesisTreeDto> turnToTree(List<Unit> unitList, String topUnitId);

    /**
     * 树结构的List 得到所有unitId
     * @param treeList
     * @return
     */
    public void getIds(List<DiathesisTreeDto> treeList,List<String> unitList);
}
