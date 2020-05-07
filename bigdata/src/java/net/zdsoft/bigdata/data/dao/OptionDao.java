package net.zdsoft.bigdata.data.dao;

import java.util.List;

import net.zdsoft.bigdata.data.entity.Option;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface OptionDao extends BaseJpaRepositoryDao<Option, String> {

	List<Option> findAllByCode(String code);
	
    @Modifying
	@Query("update Option set mobility = ?2 Where code = ?1")
	public void updateMobilityByCode(String frameCode,Integer mobility);

	@Query("FROM Option order by orderId asc")
	List<Option> findAllOption();
	
	@Query("FROM Option Where type = ?1 order by orderId asc")
	List<Option> findAllOptionByType(String type);
}
