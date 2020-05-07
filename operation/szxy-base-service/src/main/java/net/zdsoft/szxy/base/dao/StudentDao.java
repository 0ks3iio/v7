package net.zdsoft.szxy.base.dao;

import net.zdsoft.szxy.base.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shenke
 * @since 2019/3/20 下午5:25
 */
@Repository
public interface StudentDao extends JpaRepository<Student, String>, JpaSpecificationExecutor<Student> {

    /**
     * 删除指定学校的学生（软删）
     * @param schoolId 学校ID
     */
    @Modifying
    @Query(value = "update Student set isDeleted=1, modifyTime=:#{new java.util.Date()} where schoolId=?1")
    void deleteStudentsBySchoolId(String schoolId);

    /**
     * 根据学生ID查询
     * @param ids 学生Id列表
     * @return list
     */
    @Query(value = "from Student where id in (?1)")
    List<Student> getStudentsById(String[] ids);

    /**
     * 根据学生ID查询学生信息，过滤掉软删的数据
     * @param id 学生ID
     * @return
     */
    @Query(value = "from Student where id=?1 and isDeleted=0")
    Student getStudentById(String id);
}
