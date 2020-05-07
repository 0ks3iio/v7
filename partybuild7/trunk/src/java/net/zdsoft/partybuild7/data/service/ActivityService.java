package net.zdsoft.partybuild7.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.partybuild7.data.entity.Activity;

import org.springframework.data.domain.Pageable;

/**
 * 
 * @author weixh
 * @since 2017-9-20 上午10:07:56
 */
public interface ActivityService extends BaseService<Activity, String> {

	/**
	 * 获取最新一条
	 * @param memberId
	 * @return
	 */
	public Activity findNewByMemberId(String memberId, int level);
	
	public List<Activity> findByMemberId(String memberId, int level, Pageable page);
	
	public Activity findById(String id);
}
