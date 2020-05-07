package net.zdsoft.szxy.base.dao;

import net.zdsoft.szxy.base.entity.Dept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author shenke
 * @since 2019/3/20 下午4:25
 */
@Repository
public interface DeptDao extends JpaRepository<Dept, String> {

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
            value = "update Dept set isDeleted=1, modifyTime=:#{new java.util.Date()} where unitId=?1"
    )
    void deleteDeptsByUnitId(String unitId);

    /**
     * 获取指定单位的部门最大编号
     * @param unitId 单位ID
     * @return
     */
    String getAvailableDeptCodeByUnitId(String unitId);
}
