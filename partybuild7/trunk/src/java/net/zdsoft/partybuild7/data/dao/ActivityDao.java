package net.zdsoft.partybuild7.data.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.partybuild7.data.entity.Activity;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

/**
 * 
 * @author weixh
 * @since 2017-9-19 下午5:22:31
 */
public interface ActivityDao extends BaseJpaRepositoryDao<Activity, String> {
	
	@Query("SELECT act FROM Activity act, ActivityParticipator ap WHERE ap.activityId=act.id and ap.partyMemberId=?1 and act.activityLevel=?2 ORDER BY act.creationTime DESC, act.activityStartDate DESC, act.activityEndDate DESC")
	public List<Activity> findByMemberId(String memberId, int level, Pageable page);
	
}
