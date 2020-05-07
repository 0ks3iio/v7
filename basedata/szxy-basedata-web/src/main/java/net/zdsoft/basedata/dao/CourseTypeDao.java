package net.zdsoft.basedata.dao;

import java.util.List;

import net.zdsoft.basedata.entity.CourseType;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CourseTypeDao extends BaseJpaRepositoryDao<CourseType, String>{
	
	//根据科目名称查找科目数据
	List<CourseType> findByIsDeletedAndNameLikeAndType(Integer isDeleted,
			String seachName, String type);

	Integer countByIsDeletedAndTypeAndNameLike(Integer isDeleted, String type, String seachName);

	List<CourseType> findByIsDeletedAndName(Integer isDelete, String name);

	List<CourseType> findByIsDeletedAndCode(Integer isDeleted, String code);
	@Modifying
	@Query("update CourseType set isDeleted=1 where id in (?1)")
	void updateIsDeleteds(String[] ids);

	List<CourseType> findByIsDeletedAndType(Integer isDeleted, String type);

	List<CourseType> findByIsDeletedAndTypeIn(int isDeleted, String[] types);

}
