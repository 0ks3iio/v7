package net.zdsoft.newgkelective.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkDivideEx;

public interface NewGkDivideExDao extends BaseJpaRepositoryDao<NewGkDivideEx, String>{
	
	@Query("from NewGkDivideEx where divideId =?1  ")
	List<NewGkDivideEx> findByDivideId(String divideId);
	
	@Query("from NewGkDivideEx where divideId =?1 and groupType=?2 ")
	public List<NewGkDivideEx> findByDivideIdAndGroupType(String divideId, String groupType);
	
	@Modifying
	@Query(" delete from  NewGkDivideEx where divideId =?1  ")
	void deleteByDivideId(String divideId);

}
