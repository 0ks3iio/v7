package net.zdsoft.szxy.base.dao;

import net.zdsoft.szxy.base.entity.Family;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shenke
 * @since 2019/3/20 下午5:21
 */
@Repository
public interface FamilyDao extends JpaRepository<Family, String>, JpaSpecificationExecutor<Family> {

    /**
     * 删除指定单位下的所有家长数据（软删）
     * @param schoolId 学校ID
     */
    @Modifying
    @Query(value = "update Family set isDeleted=1, modifyTime=:#{new java.util.Date()} where schoolId=?1")
    void deleteFamiliesBySchoolId(String schoolId);

    /**
     * 根据家长ID查询家长信息，不包含软删的数据
     * @param ids 家长ID
     * @return
     */
    @Query(value = "from Family where id in (?1) and isDeleted=0")
    List<Family> getFamiliesById(String[] ids);

    /**
     * 根据学生Id查询对应的家长信息，不包含软删的数据
     * @param studentIds 学生ID
     * @return
     */
    @Query(value = "from Family where studentId in (?1) and isDeleted=0")
    List<Family> getFamiliesByStudentId(String[] studentIds);

    /**
     * 更新指定家长的手机号
     * @param mobilePhone 手机号码
     * @param id ID
     */
    @Modifying
    @Query(value = "update Family set mobilePhone=?1 where id=?2")
    void updateMobilePhoneById(String mobilePhone, String id);
}
