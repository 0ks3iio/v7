package net.zdsoft.diathesis.data.dao;

import net.zdsoft.diathesis.data.entity.DiathesisMutualGroup;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/6/3 17:13
 */
public interface DiathesisMutualGroupDao extends BaseJpaRepositoryDao<DiathesisMutualGroup,String> {

    /**
     * 根据id软删
     * @param groupId
     */
    @Modifying
    @Query("delete from DiathesisMutualGroup where id=?1")
    void deleteGroupById(String groupId);

    /**
     * 根据学年  学期  classId 获得 组 列表
     * @param acadyear
     * @param semester
     * @param classId
     * @return
     */
    @Query(nativeQuery = true,value = "SELECT * FROM newdiathesis_mutual_group where acadyear=?1 and semester=?2 and class_Id=?3 order by sort_Num")
    List<DiathesisMutualGroup> findByAcadyearAndSemeterAndClassId(String acadyear, Integer semester, String classId);

    @Query("from DiathesisMutualGroup where leaderId=?1 and  acadyear=?2 and semester=?3")
    DiathesisMutualGroup findByGroupIdAndTimeInfo(String studentId, String acadyear, Integer semeter);

    @Query("from DiathesisMutualGroup where leaderId=?1 and acadyear=?2 and semester=?3 ")
    List<DiathesisMutualGroup> findByLeadIdAndSemester(String studentId, String acadyear, Integer semester);

    @Query("from DiathesisMutualGroup where id=?1")
    DiathesisMutualGroup findOneById(String id);


    @Modifying
    @Query("delete from DiathesisMutualGroup where id in ?1")
    void deleteByIds(List<String> groupIds);

    @Query("from DiathesisMutualGroup where id in (?1)")
    List<DiathesisMutualGroup> findListByIds(List<String> ids);
}
