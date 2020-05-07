package net.zdsoft.newstusys.dao;

import net.zdsoft.basedata.entity.Student;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by Administrator on 2018/3/1.
 */
public interface BaseStudentDao extends BaseJpaRepositoryDao<Student,String> {
    /**
     * 通过学生Id删除 学生
     * @param stuIds 学生Id数组
     */
    @Modifying
    @Query("update Student set isDeleted= 1 where id in (?1)")
    public void deleteByStuIds(String[] stuIds);
}
