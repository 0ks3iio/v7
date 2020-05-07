package net.zdsoft.gkelective.data.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.gkelective.data.entity.GkAllocation;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface GkAllocationDao extends BaseJpaRepositoryDao<GkAllocation, String>{

	/**
	 * 查询某一次排班的优先顺序
	 * @param arrangeSubjectId
	 * @return
	 */
	@Query("From GkAllocation where subjectArrangeId=?1 and isUsing=1 order by sort ")
	public List<GkAllocation> findByArrangeIdIsUsing(String arrangeSubjectId);

	@Query("From GkAllocation where subjectArrangeId = ?1 and isUsing = ?2 order by sort ")
	public List<GkAllocation> findAllocationList(String arrangeId, String isUsing);
	
	@Modifying
	@Query("DELETE FROM GkAllocation where subjectArrangeId = ?1 ")
	public void deleteBySubjectArrangeId(String arrangeSubjectId);
}
