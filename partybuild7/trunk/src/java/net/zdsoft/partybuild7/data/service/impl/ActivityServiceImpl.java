package net.zdsoft.partybuild7.data.service.impl;

import java.util.List;

import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.partybuild7.data.dao.ActivityDao;
import net.zdsoft.partybuild7.data.dao.ActivityParticipatorDao;
import net.zdsoft.partybuild7.data.entity.Activity;
import net.zdsoft.partybuild7.data.service.ActivityService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * 
 * @author weixh
 * @since 2017-9-21 下午2:16:40
 */
@Service("activityService")
public class ActivityServiceImpl extends BaseServiceImpl<Activity , String>
		implements ActivityService {
	@Autowired
	private ActivityDao activityDao;
	@Autowired
	private ActivityParticipatorDao activityParticipatorDao;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	
	@Override
	protected BaseJpaRepositoryDao<Activity, String> getJpaDao() {
		return activityDao;
	}

	@Override
	protected Class<Activity> getEntityClass() {
		return Activity.class;
	}

	@Override
	public Activity findNewByMemberId(String memberId, int level) {
		Pagination page = new Pagination(1, 1, false);
		List<Activity> acts = activityDao.findByMemberId(memberId, level, page.toPageable());
		if(CollectionUtils.isEmpty(acts)){
			return null;
		}
		Activity act = acts.get(0);
		return act;
	}
	
	public Activity findById(String id){
		Activity act = activityDao.findById(id).orElse(null);
		if(act == null){
			return null;
		}
		List<String> tids = activityParticipatorDao.findMemberIdsByActivityId(id);
		if(CollectionUtils.isNotEmpty(tids)){
			act.setMemberIds(StringUtils.join(tids.toArray(new String[0]), ","));
			List<String> tns = EntityUtils.getList(SUtils.dt(teacherRemoteService.findListByIds(tids.toArray(new String[0])), new TR<List<Teacher>>(){}), "teacherName");
			if(CollectionUtils.isNotEmpty(tns)){
				act.setMemberNames(StringUtils.join(tns.toArray(new String[0]), "、"));
			}
		}
		return act;
	}

	/* 
	 * @see net.zdsoft.partybuild.data.service.ActivityService#findByMemberId(java.lang.String, int, org.springframework.data.domain.Pageable)
	 */
	@Override
	public List<Activity> findByMemberId(String memberId, int level,
			Pageable page) {
		return activityDao.findByMemberId(memberId, level, page);
	}

}
