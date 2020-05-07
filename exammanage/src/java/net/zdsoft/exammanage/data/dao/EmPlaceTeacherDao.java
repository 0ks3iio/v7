package net.zdsoft.exammanage.data.dao;

import net.zdsoft.exammanage.data.entity.EmPlaceTeacher;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmPlaceTeacherDao extends BaseJpaRepositoryDao<EmPlaceTeacher, String> {

    public List<EmPlaceTeacher> findByExamIdAndType(String examId, String type);

    @Modifying
    @Query("delete from EmPlaceTeacher where id=?1")
    public void deleteById(String emPlaceTeacherId);

    public List<EmPlaceTeacher> findByExamIdAndSubjectIdAndType(String examId, String subjectId, String tEACHER_TYPE1);

    public void deleteByExamPlaceIdIn(String[] emPlaceIds);

    @Modifying
    @Query("delete from EmPlaceTeacher where examId =?1 and type = ?2")
    public void deleteByExamId(String examId, String tEACHER_TYPE2);

    public List<EmPlaceTeacher> findByExamId(String examId);

    @Query("From EmPlaceTeacher where unitId = ?1 and TO_CHAR(startTime,'yyyy-mm-dd') >= ?2 and TO_CHAR(startTime,'yyyy-mm-dd') <= ?3 and type = ?4")
    public List<EmPlaceTeacher> findByUnitIdAndStartTimeAndType(String unitId, String startTime, String endTime, String tEACHER_TYPE);

    @Modifying
    @Query("delete from EmPlaceTeacher where subjectId=?1 and examId=?2 and unitId=?3")
    public void deleteAllPlaceTeacher(String subjectId, String examId, String unitId);

    @Query("from EmPlaceTeacher where unitId=?1 and examId=?2 and teacherIdsIn like ?3 ")
    public List<EmPlaceTeacher> findByTeacherIn(String unitId, String examId,
                                                String teacherIdLike);

    @Query("from EmPlaceTeacher where examId=?1 and examPlaceId=?2 and subjectId=?3 and type= ?4 ")
    public List<EmPlaceTeacher> findByExamIdAndPlaceIdAndSubjectId(String examId, String placeId, String subjectId, String type);
}
