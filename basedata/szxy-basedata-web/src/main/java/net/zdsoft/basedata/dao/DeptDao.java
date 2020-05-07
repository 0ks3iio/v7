package net.zdsoft.basedata.dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface DeptDao extends BaseJpaRepositoryDao<Dept, String> {

    public static final String SQL_DEPT = " and isDeleted = 0 order by displayOrder";

    @Query("from Dept where unitId = ?1 and isDeleted = 0 order by displayOrder")
    List<Dept> findByUnitId(String unitId);

    @Query("from Dept where parentId = ?1 and isDeleted = 0")
    List<Dept> findByParentId(String parentId);

    @Query("from Dept where unitId = ?1 and isDeleted = 0")
    List<Dept> findByUnitId(String unitId, Pageable page);

    @Query("select count(*) from Dept where isDeleted = 0 and unitId = ?1")
    Integer countByUnitId(String unitId);

    @Query("select count(*) from Dept where isDeleted = 0 and parentId = ?1")
    Integer countByParentId(String parentId);

    @Query("from Dept where parentId = ?1" + SQL_DEPT)
    List<Dept> findByParentId(String parentId, Pageable pageable);

    @Query("from Dept where unitId = ?1 and parentId = ?2" + SQL_DEPT)
    List<Dept> findByUnitIdAndParentId(String unitId, String parentId);

    @Query("from Dept where parentId = ?1 and deptName like ?2" + SQL_DEPT)
    List<Dept> findByParentIdAndDeptName(String parentId, String deptName);

    @Query("from Dept where parentId = ?1 and instituteId = ?2" + SQL_DEPT)
    List<Dept> findByParentIdAndInstituteId(String parentId, String instituteId);

    @Query("from Dept where unitId = ?1 and deputyHeadId = ?2 and isDeleted = 0")
    List<Dept> findByUnitIdAndDeputyHeadId(String unitId, String deputyHeadId);

    @Query("from Dept where areaId = ?1 and isDeleted = 0")
    List<Dept> findByAreaId(String areaId);

    @Query("from Dept where teacherId = ?1 and idDeleted = 0")
    List<Dept> findByTeacherId(String userId);

    @Query("from Dept where unitId = ?1 and leaderId = ?2 and idDeleted = 0")
    List<Dept> findByUnitIdAndLeaderId(String unitId, String leaderId);
    
    @Query("from Dept where unitId = ?1 and deptCode = ?2 and idDeleted = 0")
    public List<Dept> findByUnitAndCode(String unitId, String code);

    @Query("from Dept where instituteId = ?1" + SQL_DEPT)
    List<Dept> findByInstituteId(String instituteId);

    @Query("from Dept where unitId = ?1 and deptName like ?2" + SQL_DEPT)
    List<Dept> findByUnitIdAndDeptNameLike(String unitId, String deptName);

    @Query("from Dept where idDeleted = 0 and id in ?1")
    List<Dept> findByIds(String... ids);
    
    @Modifying
    @Query("delete from Dept where id in (?1)")
    void deleteAllByIds(String... id);

    /**
     * 根据单位和更新时间获取部门
     *
     * @param 
     * @return
     */
    @Query("From Dept where unit_id = ?1 and modify_time >= ?2 order by creation_time")
    List<Dept> findByUnitIdAndModifyTime(String unitId, Date modifyTime);
    
    /**
     * 根据单位获取部门
     *
     * @param 
     * @return
     */
    @Query("From Dept where unit_id = ?1 order by creation_time")
    List<Dept> findAllByUnitId(String unitId);
    
    /**
     * 更新用户钉钉号
     */
    @Modifying
    @Query("update Dept set dingding_id=?1 where id=?2")
    void updateDingDingIdById(String dingdingId, String id);

    /**
     * 获取指定单位下的指定名称的部门
     * @param unitId
     * @param deptName
     * @return
     */
    @Query(
            value = "from Dept where unitId=?1 and deptName=?2"
    )
    Dept getDeptByDeptName(String unitId, String deptName);

    /**
     * 获取指定单位的最大部门编号
     * @param unitId
     * @return
     */
    @Query(
            value = "select max(deptCode) from Dept where unitId=?1"
    )
    Integer getMaxDeptCodeByUnitId(String unitId);

    /**
     * 获取最大的部门排序号
     * @param unitId
     * @return
     */
    @Query(
            value = "select max(displayOrder) from Dept where unitId=?1"
    )
    Optional<Integer> getMaxDisplayOrderByUnitId(String unitId);

    @Modifying
    @Query(
            value = "update Dept set isDeleted=1 where unitId=?1"
    )
    void deleteDeptsByUnitId(String unitId);
}
