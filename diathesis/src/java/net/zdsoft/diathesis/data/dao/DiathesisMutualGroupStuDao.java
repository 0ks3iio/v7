package net.zdsoft.diathesis.data.dao;

import net.zdsoft.diathesis.data.entity.DiathesisMutualGroupStu;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/6/3 17:14
 */
public interface DiathesisMutualGroupStuDao extends BaseJpaRepositoryDao<DiathesisMutualGroupStu,String> {
    /**
     * 删除一个组的成员
     * @param mutualId
     */
    @Modifying
    @Query("delete from DiathesisMutualGroupStu where mutualGroupId=?1")
    void deleteByMutualId(String mutualId);

    /**
     * 软删除一个组的成员
     * @param mutualId
     */
    @Modifying
    @Query("delete from DiathesisMutualGroupStu where mutualGroupId=?1")
    void deleteSoftByMutualId(String mutualId);

    /**
     * 根据 组 ids  获得所有学生的关系数据
     * @param groupIds
     * @return
     */
    @Query("from DiathesisMutualGroupStu where  mutualGroupId in (?1)")
    List<DiathesisMutualGroupStu> findListByMutualGroupIdIn(List<String> groupIds);

    @Query("from DiathesisMutualGroupStu where mutualGroupId =?1")
    List<DiathesisMutualGroupStu> findListByMutualGroupId(String MutualGroupId);

    @Query(nativeQuery = true,value = "select id from newdiathesis_mutual_group where acadyear=?2 and semeter=?3  and " +
            " id in(select mutualGroupId from newdiathesis_mutual_group_stu where student_id=?1 ) ")
    String findGroupIdByAcadyearAndSemeterAndStudentId(String studentId, String acadyear, Integer semeter);

    @Query(nativeQuery = true,value = "select mutual_group_id from newdiathesis_mutual_group_stu " +
            "where  student_id=?1 and mutual_group_id in (select id from newdiathesis_mutual_group " +
            "where acadyear=?2 and semester=?3 )")
    String findMutualGroupIdByStudentIdAndSemester(String studentId, String acady, Integer semester);

    @Modifying
    @Query("delete from DiathesisMutualGroupStu where mutualGroupId in ?1")
    void deleteByMutualGroupIds(List<String> groupIds);
}
