package net.zdsoft.teacherasess.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.teacherasess.data.entity.TeacherasessConvertExam;

public interface TeacherasessConvertExamDao extends BaseJpaRepositoryDao<TeacherasessConvertExam, String>{
	
	@Query("From TeacherasessConvertExam where convertId in (?1)")
	public List<TeacherasessConvertExam> findListByConvertIdInWithMaster(String[] convertIds);

}
