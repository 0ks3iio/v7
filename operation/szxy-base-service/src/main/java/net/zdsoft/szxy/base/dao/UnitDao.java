package net.zdsoft.szxy.base.dao;

import net.zdsoft.szxy.base.dto.UpdateUnit;
import net.zdsoft.szxy.base.entity.Unit;
import net.zdsoft.szxy.base.enu.ID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author shenke
 * @since 2019/3/18 下午2:57
 */
@Repository
public interface UnitDao extends JpaRepository<Unit, String> {


    /**
     * 根据单位ID查询单位信息，不包含软删的数据
     * @param unitId 单位ID
     * @return unit or null
     */
    @Query(value = "from Unit where id=?1 and isDeleted=0")
    Unit getUnitById(String unitId);

    /**
     * 获取直属下属单位的数量
     * @param unitId 单位ID
     * @return long
     */
    @Query(value = "select count(1) from Unit where parentId=?1 and isDeleted=0")
    Long countUnderlingUnits(String unitId);

    /**
     * 获取指定单位的下属直属单位
     * @param unitId 要查询的上级单位ID
     * @return List
     */
    @Query(value = "from Unit where parentId=?1 and isDeleted=0")
    List<Unit> getUnderlingUnits(String unitId);

    /**
     * 根据单位ID批量查询单位数据
     * @param unitIds 单位ID
     * @return List
     */
    @Query(value = "from Unit where id in (?1) and isDeleted=0")
    List<Unit> getUnitsByUnitIds(String[] unitIds);

    /**
     * 查询所有的顶级单位
     * @return List
     */
    @Query(value = "from Unit where (parentId in ('" + ID.ZERO_32 + "','"+ ID.ONE_32 +"' ) or id=rootUnitId) and isDeleted=0")
    List<Unit> getTopUnits();

    /**
     * 根据unitName 查找单位
     * @param unitName 要查找的单位名称
     * @return
     */
    @Query(value="from Unit where unitName = ?1 and isDeleted=0")
    Optional<Unit> getUnitByUnitName(String unitName);

    /**
     * 获取非教育机构的UnionCode最大值
     * @param parentUnitId 上级单位ID
     * @param unionCodeLength unionCode长度
     * @param parentUnitUnionCodeLength 上级单位的unionCode的长度
     * @return
     */
    @Query(
            value = "select max(substring(unionCode, 1+?3)) from Unit where parentId=?1 and length(unionCode)=?2 and isDeleted=0"
    )
    Optional<String> getMaxNoEduUnionCode(String parentUnitId, Integer unionCodeLength, Integer parentUnitUnionCodeLength);

    /**
     * 获取乡镇教育局的最大UnionCode
     * @param parentUnitId 上级单位ID
     * @return
     */
    @Query(
            value = "select max(unionCode) from Unit where parentId=?1 and unitClass='1' and unitType<>8 and isDeleted=0"
    )
    Optional<String> getMaxTownEduUnionCode(String parentUnitId);

    /**
     * 获取指定单位下学校的UnionCode的最大值
     * @param parentUnitId 上级单位ID
     * @return
     */
    @Query(
            value = "select max(unionCode) from Unit where parentId=?1 and unitClass='2' and length(unionCode)=12 and isDeleted=0"
    )
    Optional<String> getMaxSchoolUnionCode(String parentUnitId);

    /**
     * 根据unionCode判断unionCode是否存在
     * @param unionCode unionCode
     * @return
     */
    boolean existsByUnionCodeAndIsDeleted(String unionCode, Integer isDeleted);

    /**
     * 删除指定单位（软删）
     * @param unitId 单位ID
     */
    @Modifying
    @Query(value = "update Unit set isDeleted=1, modifyTime=:#{new java.util.Date()} where id=?1")
    void deleteUnitByUnitId(String unitId);

    /**
     * 跟新单位名称
     * @param unitName 新的单位名称
     * @param unitId 单位ID
     */
    @Modifying
    @Query(value = "update Unit set unitName=?1 , modifyTime=:#{new java.util.Date()} where id=?2")
    void updateUnitName(String unitName, String unitId);

    /**
     * 更新单位名称或者上级单位或者regionCode，
     * 根据UpdateUnit的值动态更新
     * 这个方法一般在更新学校的时候用到
     */
    @Modifying
    void updateUnit(UpdateUnit updateUnit);
}
