package net.zdsoft.bigdata.extend.data.service.impl;

import com.google.common.collect.Maps;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.extend.data.dao.EventFavoriteParamDao;
import net.zdsoft.bigdata.extend.data.entity.EventFavoriteParam;
import net.zdsoft.bigdata.extend.data.service.EventFavoriteParamService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by wangdongdong on 2018/7/9 13:42.
 */
@Service
public class EventFavoriteParamServiceImpl extends BaseServiceImpl<EventFavoriteParam, String> implements
		EventFavoriteParamService {

	@Resource
	private EventFavoriteParamDao eventFavoriteParamDao;

	@Override
	protected BaseJpaRepositoryDao<EventFavoriteParam, String> getJpaDao() {
		return eventFavoriteParamDao;
	}

	@Override
	protected Class<EventFavoriteParam> getEntityClass() {
		return EventFavoriteParam.class;
	}

	@Override
	public List<EventFavoriteParam> findByFavoriteId(String favoriteId) {
		return eventFavoriteParamDao.findAllByFavoriteId(favoriteId);
	}

	@Override
	public Map<String, String> getMapByFavoriteId(String favoriteId) {
		List<EventFavoriteParam> params = this.findByFavoriteId(favoriteId);
		Map<String, String> paramMap = Maps.newHashMap();
		params.forEach(e-> paramMap.putIfAbsent(e.getParamType(), e.getParamValue()));
		return paramMap;
	}

	@Override
	public void deleteByFavoriteId(String favoriteId) {
		eventFavoriteParamDao.deleteByFavoriteId(favoriteId);
	}
}
