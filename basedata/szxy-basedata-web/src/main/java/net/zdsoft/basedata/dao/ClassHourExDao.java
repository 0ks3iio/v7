package net.zdsoft.basedata.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.basedata.entity.ClassHourEx;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface ClassHourExDao extends BaseJpaRepositoryDao<ClassHourEx, String>{
	
	@Query("From ClassHourEx where classHourId in (?1)  and isDeleted=0 ")
	List<ClassHourEx> findByClassHourIdIn(String[] classHourIds);

	@Modifying
	@Query("update ClassHourEx set isDeleted=1,modifyTime=sysdate where id =?1 ")
	void updateById(String id);
	
	
	@Modifying
	@Query("update ClassHourEx set isDeleted=1,modifyTime=sysdate where classHourId in (?1) ")
	void deleteUpdateByClassHourIdIn(String[] classHourIds);
	
}
