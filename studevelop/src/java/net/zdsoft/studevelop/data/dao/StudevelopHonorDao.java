package net.zdsoft.studevelop.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.entity.StudevelopHonor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 
 * @author weixh
 * @since 2017-7-20 下午5:14:05
 */
public interface StudevelopHonorDao extends
		BaseJpaRepositoryDao<StudevelopHonor, String> {

	@Query(nativeQuery = true , value = "select * from studevelop_honor where acadyear = ?1 and semester = ?2  and class_id = ?3 ")
	public List<StudevelopHonor> getStudevelopHonorByAcadyearAndSemester(String acadyear, int semester , String classId);

	@Query(nativeQuery = true , value = "select * from studevelop_honor where acadyear = ?1 and semester = ?2  and class_id in ?3 ")
	public List<StudevelopHonor> getStudevelopHonorByClassIds(String acadyear, int semester , String[] classIds);
}
