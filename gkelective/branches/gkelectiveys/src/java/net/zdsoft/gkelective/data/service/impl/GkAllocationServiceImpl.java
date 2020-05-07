package net.zdsoft.gkelective.data.service.impl;

import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.gkelective.data.dao.GkAllocationDao;
import net.zdsoft.gkelective.data.entity.GkAllocation;
import net.zdsoft.gkelective.data.service.GkAllocationService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service("gkAllocationService")
public class GkAllocationServiceImpl extends BaseServiceImpl<GkAllocation, String> implements GkAllocationService{
	@Autowired
	private GkAllocationDao gkAllocationDao;

	@Override
	public List<GkAllocation> findByArrangeIdIsUsing(String arrangeSubjectId) {
		List<GkAllocation> list=gkAllocationDao.findByArrangeIdIsUsing(arrangeSubjectId);
		if(CollectionUtils.isEmpty(list)){
			list=gkAllocationDao.findByArrangeIdIsUsing(Constant.GUID_ZERO);
		}
		return list;
	}
	
	public void save(GkAllocation... allocations){
		this.saveAll(checkSave(allocations).toArray(new GkAllocation[]{}));
	}

	@Override
	protected BaseJpaRepositoryDao<GkAllocation, String> getJpaDao() {
		return gkAllocationDao;
	}

	@Override
	protected Class<GkAllocation> getEntityClass() {
		return GkAllocation.class;
	}

	@Override
	public List<GkAllocation> findAllocationList(String arrangeId, boolean isUsing, boolean isZero) {
		List<GkAllocation> findAllocationList = gkAllocationDao.findAllocationList(arrangeId,isUsing?"1":"0");
		if(CollectionUtils.isEmpty(findAllocationList) && isZero){
			findAllocationList = gkAllocationDao.findAllocationList(Constant.GUID_ZERO,isUsing?"1":"0");
			for (GkAllocation gkAllocation : findAllocationList) {
				gkAllocation.setId("");
			}
		}
		return findAllocationList;
	}

	@Override
	public void saveBatch(List<GkAllocation> allocationList) {
		gkAllocationDao.saveAll(checkSave(allocationList.toArray(new GkAllocation[0])));
	}
}
