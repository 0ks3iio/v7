package net.zdsoft.exammanage.data.dao;

import net.zdsoft.exammanage.data.entity.EmPlace;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmPlaceDao extends BaseJpaRepositoryDao<EmPlace, String>, EmPlaceJdbcDao {

    @Query("From EmPlace where examId=?1 and schoolId =?2 order by examPlaceCode")
    List<EmPlace> findByExamIdAndSchoolId(String examId, String unitId);

    @Query("From EmPlace where examId=?1 and optionId in (?2) order by examPlaceCode")
    List<EmPlace> findByExamIdAndOptionIds(String examId, String[] optionIds);

    List<EmPlace> getEmPlacesByExamIdAndOptionIdInOrderByExamPlaceCode(String examId, String[] optionIds);

    @Modifying
    @Query("delete from EmPlace where examId=?1 and schoolId =?2 ")
    void deleteByExamIdAndSchoolId(String examId, String unitId);

    @Query("From EmPlace where id=?1")
    EmPlace findByEmPlaceId(String id);

    @Modifying
    @Query("delete from EmPlace where id in (?1)")
    void deleteByIds(String[] ids);

    @Query("select count(*) from EmPlace where examId =?1 and schoolId =?2")
    Integer getSizeByExamIdAndSchoolId(String examId, String unitId);

    @Modifying
    @Query("delete from EmPlace where examId=?1 ")
    void deleteByExamId(String examId);

    @Modifying
    @Query("delete from EmPlace where examId=?1 and optionId=?2 ")
    void deleteByExamIdAndOptionId(String examId, String optionId);

    List<EmPlace> findByExamId(String examId);

    @Query("From EmPlace where schoolId=?1 and examId =?2 and placeId=?3")
    EmPlace findByExamIdAndSchoolIdAndPlaceId(String unitId, String examId, String placeId);
}
