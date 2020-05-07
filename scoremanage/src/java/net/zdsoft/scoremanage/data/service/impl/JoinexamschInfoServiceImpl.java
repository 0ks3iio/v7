package net.zdsoft.scoremanage.data.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.scoremanage.data.dao.JoinexamschInfoDao;
import net.zdsoft.scoremanage.data.entity.JoinexamschInfo;
import net.zdsoft.scoremanage.data.service.JoinexamschInfoService;

@Service("joinexamschInfoService")
public class JoinexamschInfoServiceImpl extends BaseServiceImpl<JoinexamschInfo, String> implements JoinexamschInfoService{

	@Autowired
	private JoinexamschInfoDao joinexamschInfoDao;
	
	@Override
	protected BaseJpaRepositoryDao<JoinexamschInfo, String> getJpaDao() {
		return joinexamschInfoDao;
	}

	@Override
	protected Class<JoinexamschInfo> getEntityClass() {
		return JoinexamschInfo.class;
	}

	@Override
	public void deleteByExamInfoId(String examInfoId) {
		Set<String> ids = new HashSet<String>();
		List<JoinexamschInfo> findByExamInfoId = joinexamschInfoDao.findByExamInfoId(examInfoId);
		for (JoinexamschInfo item : findByExamInfoId) {
			ids.add(item.getId());
		}
		if(CollectionUtils.isNotEmpty(ids))
			this.deleteAllByIds(ids.toArray(new String[0]));
	}

	@Override
	public void deleteAllByIds(String... id) {
		if(id!=null && id.length>0)
			joinexamschInfoDao.deleteAllByIds(id);
	}

	@Override
	public List<JoinexamschInfo> findByExamInfoId(String examInfoId) {
		return joinexamschInfoDao.findByExamInfoId(examInfoId);
	}

	@Override
	public List<JoinexamschInfo> saveAllEntitys(JoinexamschInfo... joinexamschInfo) {
		return joinexamschInfoDao.saveAll(checkSave(joinexamschInfo));
	}

}
