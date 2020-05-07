package net.zdsoft.exammanage.data.dao;

import net.zdsoft.exammanage.data.entity.EmPlaceStudent;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EmPlaceStudentDao extends BaseJpaRepositoryDao<EmPlaceStudent, String>, EmPlaceStudentJdbcDao {

    @Query("From EmPlaceStudent where examPlaceId in (?1) order by seatNum")
    List<EmPlaceStudent> findByExamPlaceId(String[] examPlaceId);

    @Query("From EmPlaceStudent where examId=?1 and studentId in (?2)")
    List<EmPlaceStudent> findByExamIdStuIds(String examId, String[] stuids);

    @Query("From EmPlaceStudent where examId=?1 and groupId=?2")
    List<EmPlaceStudent> findByExamIdAndGroupId(String examId, String groupId);

    @Query("From EmPlaceStudent where  examId in (?1)")
    List<EmPlaceStudent> findByExamIds(String[] examIds);

    @Query("From EmPlaceStudent where  examPlaceId in (?1)")
    List<EmPlaceStudent> findByExamPlaceIds(String[] placeIds);

    @Query("From EmPlaceStudent where examId=?1 and examPlaceId=?2")
    List<EmPlaceStudent> findByExamIdAndPlaceId(String examId, String placeId);

    @Modifying
    @Transactional
    @Query("delete from EmPlaceStudent where examPlaceId in (?1)")
    void deleteByExamPlaceIdIn(String[] examPlaceId);

    void deleteByExamId(String examId);

    @Query("From EmPlaceStudent where groupId in (?1) and examPlaceId in (?2) order by seatNum")
    List<EmPlaceStudent> findByGroupAndExamPlaceId(String[] groupIds, String[] examPlaceId);

    @Query("From EmPlaceStudent where examId = ?1 and studentId in (?2)")
    List<EmPlaceStudent> findByExamIdAndStuIds(String examId, String[] stuIds);

}
