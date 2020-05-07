package net.zdsoft.newgkelective.data.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.newgkelective.data.dao.NewGkTeacherGroupExDao;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherGroupEx;
import net.zdsoft.newgkelective.data.service.NewGkTeacherGroupExService;
import redis.clients.jedis.Jedis;

@Service("newGkTeacherGroupExService")
public class NewGkTeacherGroupExServiceImpl extends BaseServiceImpl<NewGkTeacherGroupEx, String>
		implements NewGkTeacherGroupExService {
	
	@Autowired
	private NewGkTeacherGroupExDao newGkTeacherGroupExDao;
	
	@Override
	public List<NewGkTeacherGroupEx> findByTeacherGroupIdIn(String[] teaherGroupIds) {
		if(teaherGroupIds == null|| teaherGroupIds.length<1) {
			return new ArrayList<>();
		}
		return newGkTeacherGroupExDao.findByTeacherGroupIdIn(teaherGroupIds);
	}

	@Override
	protected BaseJpaRepositoryDao<NewGkTeacherGroupEx, String> getJpaDao() {
		return newGkTeacherGroupExDao;
	}

	@Override
	protected Class<NewGkTeacherGroupEx> getEntityClass() {
		return NewGkTeacherGroupEx.class;
	}

	@Override
	public void deleteByTeacherGroupIds(String[] tgIds) {
		if(tgIds == null|| tgIds.length<1) {
			return ;
		}
		newGkTeacherGroupExDao.deleteByTeacherGroupIdIn(tgIds);
	}
	
	@Override
	public void deleteByTeacherIds(String gradeId, List<String> tids) {
		if(StringUtils.isBlank(gradeId) || CollectionUtils.isEmpty(tids)) {
			return;
		}
		newGkTeacherGroupExDao.deleteByTeacherIds(gradeId,tids.toArray(new String[0]));
	}

	@Override
	public List<NewGkTeacherGroupEx> findByGradeIdAndTid(String gradeId, String[] tids) {
		if(StringUtils.isBlank(gradeId) || tids == null || tids.length<=0) {
			return new ArrayList<>();
		}
		
		return newGkTeacherGroupExDao.findByGradeIdAndTid(gradeId,tids);
	}
}
