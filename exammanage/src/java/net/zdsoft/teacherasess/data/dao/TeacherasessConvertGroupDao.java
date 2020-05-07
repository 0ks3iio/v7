package net.zdsoft.teacherasess.data.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.teacherasess.data.entity.TeacherasessConvertGroup;

import org.springframework.data.jpa.repository.Query;

public interface TeacherasessConvertGroupDao extends BaseJpaRepositoryDao<TeacherasessConvertGroup, String>{
	@Query("From TeacherasessConvertGroup where convertId in (?1)")
	public List<TeacherasessConvertGroup> findListByConvertIdInWithMaster(String[] convertIds);

}
