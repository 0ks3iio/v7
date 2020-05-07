package net.zdsoft.bigdata.extend.data.dao;

import net.zdsoft.bigdata.extend.data.entity.TeacherTagResult;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by wangdongdong on 2018/7/9 13:44.
 */
public interface TeacherTagResultDao extends BaseJpaRepositoryDao<TeacherTagResult, String> {

    @Query(value = "select * from BG_TEACHER_TAG_RESULT where ID in (select min(id) from BG_TEACHER_TAG_RESULT where TAG_ID in (:tagIds) group by TEACHER_ID)", nativeQuery = true)
    List<TeacherTagResult> getTeacherTagResultByTagIdIn(@Param(value = "tagIds")String[] tagIds, Pageable page);

    List<TeacherTagResult> getTeacherTagResultByTagIdIn(String[] tagIds);

    @Query(value = "select count(*) from (select TEACHER_ID from BG_TEACHER_TAG_RESULT where TAG_ID in (:tagIds) group by TEACHER_ID)", nativeQuery = true)
    Long countTeacherTagResultByTagIdIn(@Param(value = "tagIds")String[] tagIds);
}
