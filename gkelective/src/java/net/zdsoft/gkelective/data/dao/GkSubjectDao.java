package net.zdsoft.gkelective.data.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.gkelective.data.entity.GkSubject;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface GkSubjectDao extends BaseJpaRepositoryDao<GkSubject, String>{

	public List<GkSubject> findByRoundsId(String roundsId);

	@Query("select count(*) From GkSubject where roundsId=?1 and teachModel = ?2")
	public int findSubNumByRoundsIdTeachModel(String roundsId, int teachModel);

	public List<GkSubject> findByRoundsIdAndTeachModel(String roundsId,
			Integer teachModel);
	@Modifying
	@Query("DELETE FROM GkSubject WHERE roundsId = ?1")
	public void deleteByRoundsId(String roundsId);
}
