package net.zdsoft.szxy.base.dao;

import net.zdsoft.szxy.base.entity.SchoolTypeSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author shenke
 * @since 2019/3/20 下午4:36
 */
@Repository
public interface SchoolTypeSectionDao extends JpaRepository<SchoolTypeSection, String> {

    /**
     * 根据学校类型获取学段信息
     * @param schoolType 学校分类
     * @return
     */
    @Query(value = "from SchoolTypeSection where schoolType=?1 and isDeleted=0")
    Optional<SchoolTypeSection> getSectionBySchoolType(String schoolType);
}
