package net.zdsoft.newstusys.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newstusys.entity.BaseStudentEx;

/**
 * 
 * @author weixh
 * @since 2018年3月2日 下午5:43:52
 */
public interface BaseStudentExDao extends BaseJpaRepositoryDao<BaseStudentEx, String> {
	/**
     * 通过学生Id删除 学生
     * @param stuIds 学生Id数组
     */
    @Modifying
    @Query("update BaseStudentEx set isDeleted= 1 where id in (?1)")
    public void deleteByStuIds(String[] stuIds);
}
