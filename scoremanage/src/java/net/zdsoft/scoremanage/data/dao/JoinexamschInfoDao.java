package net.zdsoft.scoremanage.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.scoremanage.data.entity.JoinexamschInfo;

public interface JoinexamschInfoDao extends BaseJpaRepositoryDao<JoinexamschInfo, String>{

	List<JoinexamschInfo> findByExamInfoId(String examInfoId);

	@Modifying
	@Query("delete from JoinexamschInfo where id in (?1)")
	void deleteAllByIds(String... id);
	
}
