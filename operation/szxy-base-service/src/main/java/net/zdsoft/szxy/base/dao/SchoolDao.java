package net.zdsoft.szxy.base.dao;

import net.zdsoft.szxy.base.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author zhujy
 * 日期:2019/3/19 0019
 */
@Repository
public interface SchoolDao extends JpaRepository<School, String> {

    /**
     * 根据学校ID获取学校信息,不包含软删的数据
     * @param id 学校ID
     * @return School or null
     *
     */
    @Query("from School where id=?1 and isDeleted=0")
    School getSchoolById(String id);

    /**
     * 根据字段名更新学校数据，school必须包含主键数据
     * @param school 学校数据
     * @param properties 要更新的字段名列表
     */
    @Modifying
    void updateSchool(School school, String[] properties);
}
