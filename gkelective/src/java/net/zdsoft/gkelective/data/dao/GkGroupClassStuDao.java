package net.zdsoft.gkelective.data.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.gkelective.data.entity.GkGroupClassStu;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface GkGroupClassStuDao extends BaseJpaRepositoryDao<GkGroupClassStu, String>{

	public List<GkGroupClassStu> findByGroupClassIdIn(String... groupClassIds);

	public void deleteByGroupClassIdIn(String[] groupClassIds);

	@Modifying
	@Query("delete from GkGroupClassStu where groupClassId in (?1) and studentId in (?2)")
	public void deleteStu(String[] groupClassIds, String[] stuId);

	@Query("select t1 from GkGroupClassStu t1,GkGroupClass t2 where t1.groupClassId = t2.id and t2.roundsId = ?1 and t1.studentId in (?2)")
	public List<GkGroupClassStu> findGkGroupClassStuList(String roundId, String[] stuId);
	
	@Modifying
	@Query("DELETE FROM GkGroupClassStu WHERE groupClassId =?1 AND studentId in (?2)")
	public void deleteByGroupClsIdStuId(String groupClsId, String... stuId);

	@Query("select t1 from GkGroupClassStu t1,GkGroupClass t2 where t1.groupClassId = t2.id and t2.roundsId = ?1")
	public List<GkGroupClassStu> findGkGroupClassStuList(String roundId);
}
