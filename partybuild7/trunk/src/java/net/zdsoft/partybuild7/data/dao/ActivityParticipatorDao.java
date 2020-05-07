package net.zdsoft.partybuild7.data.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.partybuild7.data.entity.ActivityParticipator;

import org.springframework.data.jpa.repository.Query;

/**
 * 
 * @author weixh
 * @since 2017-9-19 下午5:23:08
 */
public interface ActivityParticipatorDao extends
		BaseJpaRepositoryDao<ActivityParticipator, String> {
	
//	@Query("SELECT DISTINCT activityId FROM ActivityParticipator WHERE memberId=?1")
//	public List<String> findActivityIdsByMemberId(String memberId);
	
	@Query("SELECT partyMemberId FROM ActivityParticipator WHERE activityId=?1")
	public List<String> findMemberIdsByActivityId(String actId);
}
