package net.zdsoft.desktop.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.desktop.entity.FunctionArea;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface FunctionAreaDao extends BaseJpaRepositoryDao<FunctionArea, String>{

	/**
	 * @return
	 */
	@Query("From FunctionArea where state = 1 and  type in ?1 ")
	List<FunctionArea> findByTypes(List<String> typeIds);

	/**
	 * @param type
	 * @return
	 */
	@Query("From FunctionArea where state = 1 and  type = ?1 ")
	List<FunctionArea> findByType(String type);

	/**
	 * @param unitClass
	 * @param ownerType
	 * @return
	 * 
	 */
	@Query("From FunctionArea where unitClass =?1 and  userType like  ?2 ")
	List<FunctionArea> findByUnitClassAndType(Integer unitClass,
			String ownerType);

	
	

}
