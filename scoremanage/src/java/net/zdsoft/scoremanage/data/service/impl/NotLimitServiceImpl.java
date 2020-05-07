package net.zdsoft.scoremanage.data.service.impl;

import java.util.ArrayList;
import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.scoremanage.data.dao.NotLimitDao;
import net.zdsoft.scoremanage.data.entity.NotLimit;
import net.zdsoft.scoremanage.data.service.NotLimitService;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("notLimitService")
public class NotLimitServiceImpl extends BaseServiceImpl<NotLimit, String> implements NotLimitService{
	@Autowired
	private NotLimitDao notLimitDao;
	@Override
	public List<String> findTeacherIdByUnitId(String unitId) {
		
		return notLimitDao.findByUnitId(unitId);
	}

	@Override
	public void saveTeacherIds(String[] teacherIds, String unitId) {
		deleteByUnitId(unitId);
		if(ArrayUtils.isNotEmpty(teacherIds)){
			List<NotLimit> limitList=new ArrayList<NotLimit>();
			NotLimit limit=null;
			for(String s:teacherIds){
				limit=new NotLimit();
				limit.setUnitId(unitId);
				limit.setTeacherId(s);
				limit.setId(UuidUtils.generateUuid());
				limitList.add(limit);
			}
			notLimitDao.saveAll(limitList);
		}
		
	}

	@Override
	public void deleteByUnitId(String unitId) {
		notLimitDao.deleteByUnitId(unitId);
	}

	@Override
	protected BaseJpaRepositoryDao<NotLimit, String> getJpaDao() {
		return notLimitDao;
	}

	@Override
	protected Class<NotLimit> getEntityClass() {
		return NotLimit.class;
	}

}
