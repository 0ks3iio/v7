package net.zdsoft.szxy.base.dao;

import net.zdsoft.szxy.base.entity.Grade;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shenke
 * @since 2019/3/20 下午5:28
 */
@Repository
public interface GradeDao extends JpaRepository<Grade, String> {

    /**
     * 删除指定学校的所有年级
     * @param schoolId 学校ID
     */
    @Modifying
    @Query(value = "update Grade set isDeleted=1, modifyTime=:#{new java.util.Date()} where schoolId=?1")
    void deleteGradesBySchoolId(String schoolId);

    /**
     * 不包含软删的数据
     * @param id grade Id
     * @return
     */
    @Query(value = "from Grade where id=?1 and isDeleted=0")
    Grade getGradeById(String id);

    /**
     * 查询指定学校的年级信息
     * @param schoolId 学校ID
     * @param sort 排序规则
     * @return
     */
    @Query(value = "from Grade where schoolId=?1 and isDeleted=0 and isGraduate=0")
    List<Grade> getGradesBySchoolId(String schoolId, Sort sort);
}
