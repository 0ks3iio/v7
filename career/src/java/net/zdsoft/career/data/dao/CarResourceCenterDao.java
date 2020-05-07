package net.zdsoft.career.data.dao;

import net.zdsoft.career.data.entity.CarResourceCenter;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CarResourceCenterDao extends BaseJpaRepositoryDao<CarResourceCenter,String>{

	@Query("From CarResourceCenter where id = ?1 and isDeleted = 0")
	public CarResourceCenter findOneById(String resourceId);

	@Modifying
    @Query("update from CarResourceCenter set isDeleted = 1 Where id in (?1)")
	public void deleteByIds(String[] ids);

}
