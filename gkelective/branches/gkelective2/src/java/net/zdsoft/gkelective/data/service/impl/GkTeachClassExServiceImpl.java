package net.zdsoft.gkelective.data.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.gkelective.data.dao.GkTeachClassExDao;
import net.zdsoft.gkelective.data.entity.GkTeachClassEx;
import net.zdsoft.gkelective.data.service.GkTeachClassExService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("gkTeachClassExService")
public class GkTeachClassExServiceImpl extends BaseServiceImpl<GkTeachClassEx, String> implements GkTeachClassExService{

	@Autowired
	private GkTeachClassExDao gkTeachClassExDao;
	
	@Override
	protected BaseJpaRepositoryDao<GkTeachClassEx, String> getJpaDao() {
		return gkTeachClassExDao;
	}

	@Override
	protected Class<GkTeachClassEx> getEntityClass() {
		return GkTeachClassEx.class;
	}

	@Override
	public List<GkTeachClassEx> findGkTeachClassExList(String roundId, String[] teachClassIds) {
		if(ArrayUtils.isEmpty(teachClassIds)){
			return new ArrayList<GkTeachClassEx>();
		}
		return gkTeachClassExDao.findGkTeachClassExList(roundId,teachClassIds);
	}

	@Override
	public void saveAllEntitys(GkTeachClassEx... array) {
		gkTeachClassExDao.saveAll(checkSave(array));
	}

	@Override
	public Map<String,GkTeachClassEx> findByGkRoundId(String roundId) {
		Map<String,GkTeachClassEx> returnMap=new HashMap<String, GkTeachClassEx>();
		List<GkTeachClassEx> list =gkTeachClassExDao.findByGkRoundId(roundId);
		//一个轮次下 教学班平均分就一条
		if(CollectionUtils.isNotEmpty(list)){
			for(GkTeachClassEx ex:list){
				returnMap.put(ex.getTeachClassId(), ex);
			}
		}
		return returnMap;
	}

	@Override
	public void deleteByIds(String[] ids) {
		if(ids!=null && ids.length>0){
			gkTeachClassExDao.deleteByIds(ids);
		}
		
	}

}
