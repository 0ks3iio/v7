package net.zdsoft.szxy.base.dao;

import net.zdsoft.szxy.base.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author shenke
 * @since 2019/3/19 下午2:55
 */
@Repository
public interface TeacherDao extends JpaRepository<Teacher, String> {

    /**
     * 查询教师，不包含软删的数据
     * @param s 教师ID
     * @return Optional
     */
    @Query(value = "from Teacher where id=?1 and isDeleted=0")
    @Override
    Optional<Teacher> findById(String s);

    /**
     * 根据主键跟新教师排序号
     * @param displayOrder 新的排序号
     * @param id 主键
     */
    @Modifying
    @Query(value = "update Teacher set displayOrder=?1 where id=?2")
    void updateDisplayOrder(Integer displayOrder, String id);

    /**
     * 删除指定单位下的教师（软删）
     * @param unitId 单位ID
     */
    @Modifying
    @Query(value = "update Teacher set isDeleted=1, modifyTime=:#{new java.util.Date()} where unitId=?1")
    void deleteTeachersByUnitId(String unitId);
}
