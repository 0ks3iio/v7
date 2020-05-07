package net.zdsoft.stuwork.data.service.impl;

import java.util.List;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.dao.DyWeekCheckItemDayDao;
import net.zdsoft.stuwork.data.entity.DyWeekCheckItemDay;
import net.zdsoft.stuwork.data.service.DyWeekCheckItemDayService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dyWeekCheckItemDayService")
public class DyWeekCheckItemDayServiceImpl extends BaseServiceImpl<DyWeekCheckItemDay, String>  implements DyWeekCheckItemDayService{

	@Autowired
	private DyWeekCheckItemDayDao dyWeekCheckItemDayDao;
	
	@Override
	protected BaseJpaRepositoryDao<DyWeekCheckItemDay, String> getJpaDao() {
		return dyWeekCheckItemDayDao;
	}

	@Override
	protected Class<DyWeekCheckItemDay> getEntityClass() {
		return DyWeekCheckItemDay.class;
	}

	@Override
	public List<DyWeekCheckItemDay> findByItemIds(String[] itemIds) {
		return dyWeekCheckItemDayDao.findByItemIds(itemIds);
	}
	
	@Override
	public void deleteByItemId(String itemId) {
		dyWeekCheckItemDayDao.deleteByItemId(itemId);
	}
	
	@Override
	public void saveList(List<DyWeekCheckItemDay> itemDays) {
		DyWeekCheckItemDay[] dayArrs = itemDays.toArray(new DyWeekCheckItemDay[0]);
		checkSave(dayArrs);
		saveAll(dayArrs);
	}
	
}
