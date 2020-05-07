package net.zdsoft.exammanage.data.dao;

import net.zdsoft.exammanage.data.entity.EmPlaceGroup;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmPlaceGroupDao extends BaseJpaRepositoryDao<EmPlaceGroup, String> {

    List<EmPlaceGroup> findByGroupIdAndSchoolId(String groupId, String schoolId);

    List<EmPlaceGroup> findBySchoolIdAndGroupIdIn(String schoolId, String[] groupIds);

    @Modifying
    @Query("delete from EmPlaceGroup where examId =?1 and schoolId = ?2")
    public void deleteByExamIdAndSchoolId(String examId, String unitId);

    List<EmPlaceGroup> findByExamIdAndSchoolId(String examId, String schoolId);

    @Modifying
    @Query("delete from EmPlaceGroup where examId =?1 and schoolId = ?2 and groupId = ?3")
    public void deleteByExamIdAndSchoolIdAndGroupId(String examId, String unitId, String groupId);
}
