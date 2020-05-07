package net.zdsoft.basedata.remote.service;

import net.zdsoft.basedata.entity.School;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.UnionCodeAlreadyExistsException;
import net.zdsoft.basedata.remote.UsernameAlreadyExistsException;
import net.zdsoft.framework.entity.Pagination;

public interface UnitRemoteService extends BaseRemoteService<Unit, String> {

    /**
     * 根据单位分类，检索所有的单位信息
     *
     * @param unitClass
     * @return List&lt;Unit&gt;
     */
    String findByUnitClass(int unitClass);

    /**
     * 根据当前的单位ID，获取实际的顶级单位ID
     *
     * @param unitId
     * @return
     */
    Unit findTopUnitObject(String unitId);

    /**
     * 检索顶级单位      建议调用 String findTopUnit(String unitId);
     *
     * @return Unitl
     * @deprecated 因为一套系统内，可能会存在多个顶级单位，建议根据当前单位的root_unit_id来判断，此方法不建议使用
     */
    String findTopUnit();

    /**
     * 获取顶级单位信息
     *
     * @param unitId 本单id
     * @return
     */
    String findTopUnit(String unitId);

    /**
     * 获取直属单位信息
     *
     * @param unitId
     * @param unitClass
     * @return List&lt;Unit&gt;
     */
    String findDirectUnits(String unitId, Integer unitClass);

    /*** v6修改加入以下接口 ADD NIZQ ****/
    /**
     * @param unionId   不需要值为null
     * @param state     不需要值为-1
     * @param unitClass 不需要值为-1
     * @return
     */
    String findByUnionId(String unionId, int state, int unitClass);

    /**
     * 传单个名字时进行模糊查询，多个名字时进行全匹配
     *
     * @param unitNames
     * @return
     */
    String findByUnitName(String... unitNames);

    String findByRegion(String region, int state);

    String findByUseType(int state, int useType);

    String findByNameAndUnionCode(String unitName, String unionId, int state, int unitClass);

    String findByUnderlingUnits(String unitName, String unionId);

    String findByUnderlingUnits(String unitName, String unionId, String pagination);

    String findByUnitName(String unitName, String page);

    String findBySerialNumber(String sNumber, String eNumber);

    String findByParentIdAndUnitClass(String[] parentId, int state, int unitClass);

    String findByParentIdAndUnitClass(String parentId, int state, int unitClass, String page);

    /**
     * 获取父id里面的所有id
     *
     * @param parentId
     * @return
     */
    String findByParentId(String parentId);

    /**
     * 按分类获取区域下的单位信息
     *
     * @param unitClass
     * @param regionCodes
     * @return List&lt;Unit&gt;
     */
    String findByUnitClassAndRegion(int unitClass, String... regionCodes);

    /**
     * 获取直属单位信息
     *
     * @param unitId
     * @param unitClass
     * @return List&lt;Unit&gt;
     */
    String findDirectUnits(String unitId, int unitClass);

    /**
     * 数组形式entitys参数，返回list的json数据
     *
     * @param entitys
     * @return
     */
    String saveAllEntitys(String entitys);

    /**
     * @param unitClass
     * @param regionCodes
     * @param page
     */
    String findByUnitClassAndRegion(int unitClass, String regionCodes, Pagination page);

    /**
     * @param regionCode
     * @return
     */
    String findByRegionCode(String regionCode);

    /**
     * @param commitUnitId
     * @param unitClassInteger
     * @param page
     */
    String findDirectUnits(String commitUnitId, Integer unitClassInteger, Pagination page);

    /**
     * @param unitName
     * @param regionCode
     */
    String findByRegionCodeUnitName(String unitName, String regionCode);

    /**
     * @param unionCode
     * @return
     */
    String findByUniCode(String unionCode);

    String findNextUnionCode(String regionCode, int unitClass);

    String findByUnionCode(String unionId, int state, int unitClass);

    /**
     * 根据行政区划和单位名称查找，结果做分页处理，按照单位名称升序
     *
     * @param region
     * @param unitName
     * @param page
     * @return
     * @author cuimq
     */
    String findByRegionAndUnitName(String region, String unitName, String page);

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
    String findByRegionAndUnitClassAndUnitIdIn(Integer[] unitClass, String regionCodes, String[] unitIds,
                                               String page);

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
    String findByRegionAndUnitClassAndUnitIdNotIn(Integer[] unitClass, String regionCodes, String[] unitIds,
                                                  String page);

    /**
     * 初始化顶级单位（包括初始部门）
     *
     * @param unitName
     * @param regionCode
     * @author dingw
     */
    void initTopUnit(String unitName, String regionCode);

    /**
     * 查找已授权某应用单位列表
     *
     * @param unitClass
     * @param regionCode
     * @param serverId
     * @param page
     * @return
     * @author cuimq
     */
    String findAutorizeServerUnit(Integer[] unitClass, String regionCode, Integer serverId, String page);

    /**
     * 查找未授权某应用的单位列表
     *
     * @param unitClass
     * @param regionCode
     * @param serverId
     * @param page
     * @return
     * @author cuimq
     */
    String findUnAutorizeServerUnit(Integer[] unitClass, String regionCode, Integer serverId, String page);

    /**
     * 根据单位ids取所有正常的直属单位
     *
     * @param unitClass 可为null取包括教育局和学校
     * @param unitIds
     * @return Map<String   ,       List   <   Unit>>
     */
    String findDirectUnitsByParentIds(Integer unitClassSchool, String[] unitIds);

    String getAllSubUnitByParentId(String unitId);

    /**
     * @param unitClassEdu
     * @param array
     * @return
     */
    String findByUnitClassAndUnionCode(int unitClassEdu, String... unionCodes);

    /**
     * 美师优课需要提供的接口
     *
     * @return List<Unit>
     */
    String findTopUnitList();

    String findByOrganizationCode(String unitCode);

    /**
     * 根据单位ID删除单位数据
     * 删除单位，
     * 会删除和单位相关的所有基础数据
     *
     * base_unit base_user base_family base_student base_class base_dept base_eduinfo
     * base_teacher base_grade sys_date_info sys_systemini_unit base_server_extension base_unit_extension
     *
     * @param unitId 单位ID
     */
    void deleteByUnitId(String unitId);

    /**
     * 获取教育局下过滤学段单位数据
     *
     * @param unionCode
     * @param section
     * @param isedu     是否含教育局单位
     * @param isSchool  是否含学校
     * @return
     */
    String findUnionCodeSectionList(String unionCode, String section, boolean isedu, boolean isSchool);

    /**
     * 新增单位
     * <p>
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
     * @param unit     无论是学校还是教育局，unit的数据都必须是完整的
     * @param school   当创建单位是学校的时候需要
     * @param username 管理员账号
     * @param password 管理员账号密码
     */
    void createUnit(Unit unit, School school, String username, String password) throws UsernameAlreadyExistsException, UnionCodeAlreadyExistsException;


    /**
     * 创建 unionCode 同时校验unionCode的合法性
     * @param parentUnitId 上级单位ID
     * @param unitClass 教育局还是学校
     * @param unitType unitType
     * @param fullCode regionFullCode
     * @return
     * @throws UnionCodeAlreadyExistsException
     */
    String createUnionCode(String parentUnitId, Integer unitClass, Integer unitType, String fullCode) throws UnionCodeAlreadyExistsException ;

    /**
     * 判断是否存在下属单位
     * @param parentId 上级单位ID
     * @return Boolean
     */
    Boolean existsUnderUnits(String parentId);
    
    /**
     * 安徽对接专用
     * @param userAhId
     * @return
     */
//	public String findByAhUnitIds(String[] unitAhIds);
}
