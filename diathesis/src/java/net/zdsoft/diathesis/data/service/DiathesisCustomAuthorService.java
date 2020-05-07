package net.zdsoft.diathesis.data.service;

import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.diathesis.data.entity.DiathesisCustomAuthor;

import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/6/11 9:52
 */
public interface DiathesisCustomAuthorService extends BaseService<DiathesisCustomAuthor,String> {
    /**
     * 判断学校是否有设置权限
     * @param unitId
     * @return
     */
    boolean hasSetAuthor(String unitId);

    /**
     *
     *
     * 教育局设置下属单位的权限, 如果授权了 1.全局设置 2. 项目 这2个权限,则要初始化单位的数据
     * @param unitId
     * @param authorArray
     * @param realName
     */
    void saveChildUnitAuthor(String unitId, List<Integer> authorArray, String realName);

    /**
     * 根据unitId 查找所有权限
     * @param unitId
     * @return
     */
    List<DiathesisCustomAuthor> findAuthorListByUnitId(String unitId);

    /**
     *  查找某个权限的 实际拥有权限的unitId
     * @param unitId
     * @param type
     * @return
     */
    String findUsingUnitId(String unitId, Integer type);


    boolean hasAuthorByType(String unitId,Integer type);

    /**
     * 根据regionCode查找所有单位的权限
     * @param region
     * @param integers
     * @return
     */
    List<DiathesisCustomAuthor> findByRegionCodeAndAuthorTypeIn(String region, Integer[] integers);


    List<DiathesisCustomAuthor> findAuthorListByUnitIdInAndTypeIn(List<String> unitIds,Integer[] types);

    /**
     * 返回 id parent name 三个属性的单位集合 不包含自己
     * @param unitId
     * @return
     */
    List<Unit> findAllChildUnit(String unitId);
}
