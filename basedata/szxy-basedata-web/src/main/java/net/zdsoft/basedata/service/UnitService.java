package net.zdsoft.basedata.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.remote.UnionCodeAlreadyExistsException;
import net.zdsoft.basedata.remote.UsernameAlreadyExistsException;
import net.zdsoft.passport.exception.PassportException;
import org.springframework.data.domain.Pageable;

import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.framework.entity.Pagination;

public interface UnitService extends BaseService<Unit, String> {
    @Override
    List<Unit> findAll();

    List<Unit> findAll(Pagination page);

    /**
     * 建议调用带参数 顶级单位规则发生改变findTopUnit(String unitId)
     * (因为一套系统内，可能会存在多个顶级单位，建议根据当前单位的root_unit_id来判断，此方法不建议使用)
     */
    @Deprecated
    Unit findTopUnit();

    /**
     * 美师优课需要提供的接口
     *
     * @return
     */
    List<Unit> findTopUnitList();

    /**
     * 根据当前的单位ID，获取实际的顶级单位ID
     *
     * @param unitId
     * @return
     */
    Unit findTopUnit(String unitId);

    /**
     * 取出所有直属单位
     *
     * @param parentId
     * @param unitClass 单位分类：学校或者教育局 可为null
     * @return
     */
    List<Unit> findDirectUnitsByParentId(String parentId, Integer unitClass);

    List<Unit> findByUnitClassAndRegionCode(int unitClass, String... regionCodes);

    /**
     * 取出所有直属单位
     *
     * @param unitClass 单位分类：学校或者教育局 可为null
     * @param parentId
     * @return
     */
    Map<String, List<Unit>> findDirectUnitsByParentIds(Integer unitClass, String... parentId);

    List<Unit> findAllUnitsByParentId(String parentId, Pagination page);

    List<Unit> findByUnitClass(int unitClass);

    /**
     * 公共接口
     *
     * @param unionId
     * @param state
     * @param unitClass
     * @return
     */
    List<Unit> findByUnionId(String unionId, int state, int unitClass);

    List<Unit> findByUnitName(String... unitName);

    List<Unit> findByRegion(String region, int state);

    List<Unit> findByUseType(int state, int useType);

    List<Unit> findByNameAndUnionCode(String unitName, String unionId, int state, int unitClass);

    List<Unit> findByUnderlingUnits(String unitName, String unionId, Pageable pageable);

    Long countUnderlingUnits(String unitName, String unionId);

    List<Unit> findByUnderlingUnits(String unitName, String unionId);

    List<Unit> findByUnitName(String unitName, Pageable pageable);

    Long countByUnitName(String unitName);

    List<Unit> findBySerialNumber(String sNumber, String eNumber);

    List<Unit> findByParentIdAndUnitClass(String[] parentId, int state, int unitClass);

    Long countParentIdAndUnitClass(String parentId, int state, int unitClass);

    List<Unit> findByParentIdAndUnitClass(String parentId, int state, int unitClass, Pageable pageable);

    List<Unit> saveAllEntitys(Unit... unit);

    /**
     * 硬删
     *
     * @param id
     */
    void deleteAllByIds(String... id);

    /**
     * @param unitClass
     * @param regionCodes
     * @param page
     * @return
     */
    List<Unit> findByUnitClassAndRegion(int unitClass, String regionCodes, Pagination page);

    /**
     * @param commitUnitId
     * @param unitClassInteger
     * @param page
     * @return
     */
    List<Unit> findDirectUnits(String commitUnitId, Integer unitClassInteger, Pagination page);

    /**
     * 根据行政区划和单位名称查找，结果做分页处理，按照单位名称升序
     *
     * @param region
     * @param unitName
     * @param page
     * @return
     * @author cuimq
     */
    List<Unit> findByRegionAndUnitName(String region, String unitName, Pagination page);

    /**
     * 根据单位类型，行政区划和单位id查找，结果做分页处理，按照单位名称升序
     *
     * @param unitClass
     * @param regionCodes
     * @param unitIds
     * @param page
     * @return
     * @author cuimq
     */
    List<Unit> findByRegionAndUnitClassAndUnitIdIn(Integer[] unitClass, String regionCodes, String[] unitIds,
                                                   Pagination page);

    /**
     * 根据单位类型，行政区划,除条件单位id外的查找，结果做分页处理，按照单位名称升序
     *
     * @param unitClass
     * @param regionCodes
     * @param unitIds
     * @param page
     * @return
     * @author cuimq
     */
    List<Unit> findByRegionAndUnitClassAndUnitIdNotIn(Integer[] unitClass, String regionCodes, String[] unitIds,
                                                      Pagination page);

    void addTopUnit(String unitName, String regionCode);

    /**
     * 查找已订阅某应用的单位列表
     *
     * @param unitClass
     * @param regionCode
     * @param serverId
     * @param page
     * @return
     * @author cuimq
     */
    List<Unit> findAutorizeServerUnit(Integer[] unitClass, String regionCode, Integer serverId, Pagination page);

    /**
     * 查找未订阅某应用的单位列表
     *
     * @param unitClass
     * @param regionCode
     * @param serverId
     * @param page
     * @return
     * @author cuimq
     */
    List<Unit> findUnAutorizeServerUnit(Integer[] unitClass, String regionCode, Integer serverId, Pagination page);

    public String findNextUnionCode(String regionCode, int unitClass);

    List<Unit> findByUnitClassAndUnionCode(int unitClass, String... unionCodes);

    Unit findByOrganizationCode(String unitCode);

    /**
     * 获取教育局下过滤学段单位数据
     *
     * @param unionCode
     * @param section
     * @param isedu     是否包含教育局单位
     * @param isSchool  是否包含学校
     * @return
     */
    List<Unit> findUnionCodeSectionList(String unionCode, String section, boolean isedu, boolean isSchool);


    /*************************************************基础数据操作接口***************************************************/

    /**
     * 删除单位
     * 删除和单位相关的基础数据
     * base_school
     * @param unitId
     */
    void deleteUnit(String unitId) throws PassportException;

    @Override
    void delete(String id);

    @Override
    void delete(Unit unit);

    /**
     * 新增单位 </br>
     * 新增单位可能会进行以下数据生成
     * 1、单位信息 base_unit
     * 2、学校 base_school
     * 3、edu_info
     * 4、单位管理员账号
     * 5、管理员账号对应的教师信息
     * 6、默认部门
     * 7、默认角色
     * 8、同步增管理员账号到Passport
     * 9、当前单位的默认配置参数
     *
     * @param unit     单位信息
     * @param school   当创建单位是学校的时候需要
     * @param username
     * @param password
     */
    void addUnit(Unit unit, School school, String username, String password) throws UsernameAlreadyExistsException, PassportException, UnionCodeAlreadyExistsException;

    /**
     *
     * UnionCode的生成策略如下
     * 1、学校
     *    根据上级单位查询所有学校的最大MaxUnionCode 截取maxUnionCode
     *
     * @param parentUnitId 上级单位ID
     * @param unitClass 单位类别
     * @param unitType 单位类型
     * @param regionCode 单位行政区划码 fullCode
     * @return
     */
    String createUnionCode(String parentUnitId, Integer unitClass, Integer unitType, String regionCode);

//	List<Unit> findByAhUnitIds(String[] unitAhIds);
}
