package net.zdsoft.szxy.base.api;

import net.zdsoft.szxy.base.RecordName;
import net.zdsoft.szxy.base.entity.School;
import net.zdsoft.szxy.base.entity.Unit;
import net.zdsoft.szxy.base.exception.SzxyPassportException;
import net.zdsoft.szxy.base.exception.UnionCodeAlreadyExistsException;
import net.zdsoft.szxy.base.exception.UsernameAlreadyExistsException;
import net.zdsoft.szxy.monitor.Rpc;

import java.util.List;
import java.util.Map;

/**
 * 基础数据，单位接口
 * 除特殊说明isDelete的值，所有的查询都不包含软删的数据
 * @author shenke
 * @since 2019/3/18 下午2:51
 */
@Rpc(domain = RecordName.name)
public interface UnitRemoteService {


    /**
     * 根据单位ID查询单位，不包含软删的单位
     * @param unitId 单位ID
     * @return Unit
     */
    Unit getUnitById(String unitId);

    /**
     * 查询是否存在下属单位
     * @param unitId 单位ID
     * @return True存在 False不存在
     */
    Boolean existsUnderlingUnits(String unitId);

    /**
     * 获取指定单位的下属直属单位
     * @param unitId 上级单位ID
     * @return List
     */
    List<Unit> getUnderlingUnits(String unitId);

    /**
     * 根据单位ID批量查询
     * @param unitIds 单位ID数组
     * @return List
     */
    List<Unit> getUnitsByIds(String[] unitIds);

    /**
     * 获取指定单位的名称Map，key->unitId value->unitName
     * @param unitIds 单位ID列表
     * @return Map
     */
    Map<String, String> getUnitNameMap(String[] unitIds);

    /**
     * 获取所有的顶级单位，顶级单位的判定方式如下
     * <ul>
     *     <li>parentId 为32位'0'</li>
     *     <li>parentId 为32位'1'</li>
     *     <li>rootUnitId即为顶级单位ID</li>
     * </ul>
     * @return List
     */
    List<Unit> getTopUnits();

    /**
     * 根据单位名称查询
     * @param unitName 要查询的单位名称
     * @return Unit
     */
    Unit getUnitByUnitName(String unitName);

    /**
     * 检查单位名称是否被占用
     * @param unitName 单位名称
     * @return true 被占用，false未被占用
     */
    Boolean existsUnitName(String unitName);

    /**
     * 检查unionCode是否被使用
     * @param unionCode unionCode
     * @return {@link Boolean#TRUE} 被占用 {@link Boolean#FALSE}未被占用
     */
    Boolean existsByUnionCode(String unionCode);

    /**
     * 新增单位
     * 新增单位可能会进行以下数据生成
     *  1、单位信息 base_unit
     *  2、学校 base_school
     *  3、edu_info
     *  4、单位管理员账号
     *  5、管理员账号对应的教师信息
     *  6、默认部门
     *  7、默认角色
     *  8、同步增管理员账号到Passport
     *  9、当前单位的默认配置参数
     * @param unit 单位
     * @return Unit
     */
    void addUnit(Unit unit, School school, String username, String password) throws UnionCodeAlreadyExistsException, UsernameAlreadyExistsException, SzxyPassportException;

    /**
     * 创建单位编号
     * @param parentUnitId 上级单位ID
     * @param unitType 单位类型
     * @param regionCode 行政区划
     * @return unionCode
     */
    String createUnionCode(String parentUnitId, Integer unitType, String regionCode);

    /**
     * 删除单位（软删），和单位相关的基础数据会全部软删掉
     * @param unitId 单位ID
     */
    void deleteByUnitId(String unitId);

    /**
     * 更新单位名称
     * @param unitName 单位名称
     * @param unitId 单位ID
     */
    void updateUnit(String unitName, String unitId);
}