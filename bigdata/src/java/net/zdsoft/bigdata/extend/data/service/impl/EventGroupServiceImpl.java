package net.zdsoft.bigdata.extend.data.service.impl;

import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.extend.data.dao.EventGroupDao;
import net.zdsoft.bigdata.extend.data.entity.EventGroup;
import net.zdsoft.bigdata.extend.data.service.EventGroupService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 事件预览组接口实现
 * 
 * @author jiangf
 *
 */
@Service("eventGroupService")
public class EventGroupServiceImpl extends BaseServiceImpl<EventGroup, String>
		implements EventGroupService {

	@Autowired
	private EventGroupDao eventGroupDao;

	@Override
	public List<EventGroup> findGroupListByUserId(String userId) {
		return eventGroupDao.findGroupListByUserId(userId);
	}

	@Override
	public void saveOrUpdateGroup(EventGroup group) throws Exception {
		// 判断是否重名
		List<EventGroup> groupList = eventGroupDao.findGroupListByGroupName(
				group.getUserId(), group.getGroupName());

		if (StringUtils.isNotBlank(group.getId())) {
			for (EventGroup oldGroup : groupList) {
				if (oldGroup.getGroupName().equals(group.getGroupName())
						&& !oldGroup.getId().equals(group.getId())) {
					throw new BigDataBusinessException("该组名已经存在,请重新维护");
				}
			}
			update(group, group.getId(),
					new String[] { "groupName", "orderId" });
		} else {
			for (EventGroup oldGroup : groupList) {
				if (oldGroup.getGroupName().equals(group.getGroupName())) {
					throw new BigDataBusinessException("该组名已经存在,请重新维护");
				}
			}
			group.setId(UuidUtils.generateUuid());
			save(group);
		}
	}

	@Override
	public Integer getMaxOrderIdByUserId(String userId) {
		return eventGroupDao.getMaxOrderIdByUserId(userId);
	}

	@Override
	protected BaseJpaRepositoryDao<EventGroup, String> getJpaDao() {
		return eventGroupDao;
	}

	@Override
	protected Class<EventGroup> getEntityClass() {
		return EventGroup.class;
	}

}
