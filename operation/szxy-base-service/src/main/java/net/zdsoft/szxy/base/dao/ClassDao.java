package net.zdsoft.szxy.base.dao;

import net.zdsoft.szxy.base.entity.Clazz;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shenke
 * @since 2019/3/20 下午5:27
 */
@Repository
public interface ClassDao extends JpaRepository<Clazz, String> {


    /**
     * 根据学校ID删除所有的班级
     * @param schoolId
     */
    @Modifying
    @Query(value = "update Clazz set isDeleted=1, modifyTime=:#{new java.util.Date()} where schoolId=?1")
    void deleteClazzBySchoolId(String schoolId);

    /**
     * 根据班级ID查询班级信息（不包括软删的班级）
     * @param clazzId 班级ID
     * @return Clazz
     */
    @Query(value = "from Clazz where id=?1 and isDeleted=0")
    Clazz getClazzById(String clazzId);

    /**
     * 根据年级Id查询对应的班级信息
     * @param gradeId 年级ID
     * @param sort 排序规则
     * @return
     */
    @Query(value = "from Clazz where gradeId=?1 and isDeleted=0")
    List<Clazz> getClazzesByGradeId(String gradeId, Sort sort);
}
