package net.zdsoft.bigdata.extend.data.dao;

import net.zdsoft.bigdata.extend.data.entity.StudentTagResult;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by wangdongdong on 2018/7/9 13:44.
 */
public interface StudentTagResultDao extends BaseJpaRepositoryDao<StudentTagResult, String> {

    @Query(value = "select * from BG_STUDENT_TAG_RESULT where ID in (select min(id) from BG_STUDENT_TAG_RESULT where TAG_ID in (:tagIds) group by STUDENT_ID)", nativeQuery = true)
    List<StudentTagResult> getStudentTagResultByTagIdIn(@Param(value = "tagIds") String [] tagIds, Pageable page);

    List<StudentTagResult> getStudentTagResultByTagIdIn(String [] tagIds);

    @Query(value = "select count(*) from (select STUDENT_ID from BG_STUDENT_TAG_RESULT where TAG_ID in (:tagIds) group by STUDENT_ID)", nativeQuery = true)
    Long countStudentTagResultByTagIdIn(@Param(value = "tagIds")String [] tagIds);
}
