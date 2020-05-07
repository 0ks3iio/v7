package net.zdsoft.studevelop.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.studevelop.data.entity.StudevelopActivity;

/**
 * 
 * @author weixh
 * @since 2017-7-20 下午4:34:25
 */
public interface StudevelopActivityDao extends BaseJpaRepositoryDao<StudevelopActivity, String> {
	
	@Query("from StudevelopActivity where acadyear=?1 and semester=?2 and act_type=?3 and range_id=?4 and range_type=?5 order by creation_time desc")
	public List<StudevelopActivity> findActBySemeRangeId(
			String acadyear, int semester, String actType, String rangeId, String rangeType);

	@Query("from StudevelopActivity where acadyear=?1 and semester=?2 and act_type=?3  and range_type=?4 order by creation_time desc")
	public List<StudevelopActivity> findActBySemeRangeType(
			String acadyear, int semester, String actType, String rangeType);

	@Modifying
	@Query("delete from StudevelopActivity where id in (?1)")
	public void deleteByIds(String[] ids);

}
