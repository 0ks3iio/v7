package net.zdsoft.basedata.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;

public interface UnitDao extends BaseJpaRepositoryDao<Unit, String> {

    /**
     * 获取系统级别的顶级单位，不建议直接使用此接口，用unitService的方法
     *
     * @return
     */
    @Query("From Unit where parentId = '00000000000000000000000000000000' and isDeleted = '0'")
    Unit findTopUnit();

    @Query("From Unit where unionCode like ?1% and isDeleted = '0'")
    List<Unit> findByBeginWithUnionCode(String unionCode, Pageable page);

    /**
     * @param parentId
     * @return
     */
    @Query("From Unit where isDeleted = 0 and parentId = ?1 order by displayOrder")
    List<Unit> findByParent(String parentId);

    /**
     * @param unionCode
     * @return
     */
    @Query("From Unit where isDeleted = 0 and unionCode like ?1 order by displayOrder")
    List<Unit> findByUniCode(String unionCode);

    @Query("From Unit where unitName like ?1 and isDeleted = '0'")
    List<Unit> findByUnitName(String unitName);

    @Query("From Unit where regionCode like ?1 and unitState>= ?2 and isDeleted = '0'")
    List<Unit> findByRegion(String region, int state);

    @Query("From Unit where unitState = ?1 and use_type = ?2 and isDeleted = '0'")
    List<Unit> findByUseType(int state, int useType);

    @Query("From Unit where isDeleted = '0' and unitName IN ?1")
    List<Unit> findByUnitNames(String... unitNames);

    @Query("From Unit where unitName like ?1 and unionCode like ?2 and unitState = ?3 and unitClass = ?4 and isDeleted = '0'")
    List<Unit> findByNameAndUnionCode(String unitName, String unionId, int state, int unitClass);

    @Query("From Unit where  unionCode like ?1% and unitState = ?2 and unitClass = ?3 and isDeleted = '0'")
    List<Unit> findByUnionCode(String unionId, int state, int unitClass);

    @Query("From Unit where unitName like ?1 and unionCode like ?2 and isDeleted = '0'")
    List<Unit> findByUnderlingUnits(String unitName, String unionId, Pageable pageable);

    // @Query("select count(*) from Teacher where unitId = ?1 and isDeleted = 0")
    @Query("select count(*) from Unit where unitName like ?1 and unionCode like ?2 and isDeleted = 0")
    Long countUnderlingUnits(String unitName, String unionId);

    @Query("From Unit where unitName like ?1 and unionCode like ?2 and isDeleted = '0'")
    List<Unit> findByUnderlingUnits(String unitName, String unionId);

    @Query("From Unit where unitName like ?1 and isDeleted = '0'")
    List<Unit> findByUnitName(String unitName, Pageable pageable);

    @Query("From Unit where serialNumber >= ?1 and serialNumber <= ?2 and isDeleted = '0'")
    List<Unit> findBySerialNumber(String sNumber, String eNumber);

    @Query("From Unit where parentId in (?1) and unitState = ?2 and unitClass = ?3 and isDeleted = '0'")
    List<Unit> findByParentIdAndUnitClass(String[] parentId, int state, int unitClass);

    @Query("From Unit where parentId = ?1 and unitState = ?2 and unitClass = ?3 and isDeleted = '0'")
    List<Unit> findByParentIdAndUnitClass(String parentId, int state, int unitClass, Pageable pageable);

    @Modifying
    @Query("delete from Unit where id in (?1)")
    void deleteAllByIds(String... id);

    /**
     * @param regionCode
     * @return
     */
    @Query("From Unit where isDeleted = 0 and regionCode like ?1 ")
    List<Unit> findByRegionCode(String regionCode);

    /**
     * @param unitName
     * @param regionCode
     * @return
     */
    @Query("From Unit where isDeleted = 0 and unitName like ?1 and regionCode like ?2 ")
    List<Unit> findByRegionCodeUnitName(String unitName, String regionCode);

    @Query("From Unit where regionCode like ?1 and unitName like ?2 and isDeleted = '0'")
    List<Unit> findByRegionAndUnitName(String region, String unitName, Pageable page);

    /**
     * @param unitClass
     * @return
     */
    @Query("From Unit where unitClass = ?1 and isDeleted = '0'")
    List<Unit> findByUnitClass(int unitClass);

    @Query("select max(unionCode) from Unit where unionCode like ?1 and isDeleted = '0' and unitClass = ?2")
    String findMaxUnionCode(String regionCode, int unitClass);

    @Query(
            value = "FROM Unit where unionCode like ?1% and isDeleted=0"
    )
    List<Unit> findAllSubUnitByUnionCode(String unionCode);

    /**
     * @param unitClass
     * @param unionCodes
     * @return
     */
    @Query("From Unit where unitClass = ?1 and unionCode in ?2 and isDeleted = '0'")
    List<Unit> findByUnitClassAndUnionCode(int unitClass, String[] unionCodes);

    Unit findByOrganizationCode(String unitCode);

    @Modifying
    @Query(
            value = "update Unit set isDeleted=1 where id=?1"
    )
    void deleteUnitByUnitId(String unitId);


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
     * 获取乡镇教育局的最大UnionCode
     * @param parentUnitId 上级单位ID
     * @return
     */
    @Query(
            value = "select max(unionCode) from Unit where parentId=?1 and unitClass='1' and unitType<>8 and isDeleted=0"
    )
    Optional<String> getMaxTownEduUnionCode(String parentUnitId);

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
     * 根据unionCode判断unionCode是否存在
     * @param unionCode unionCode
     * @return
     */
    boolean existsByUnionCodeAndIsDeleted(String unionCode, Integer isDeleted);

    /**
     * 查询下级单位的数量，直属单位，根据parentId判定
     * @param parentId
     * @return
     */
    @Query(
            value = "select count(*) from Unit where parentId=?1 and isDeleted=0"
    )
    int countUnderUnits(String parentId);
    
//    @Query("From Unit where ahUnitId IN ?1 and isDeleted = '0'")
//	List<Unit> findByAhUnitIds(String[] unitAhIds);
}
