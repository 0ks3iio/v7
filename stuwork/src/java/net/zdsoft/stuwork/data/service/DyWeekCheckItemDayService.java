package net.zdsoft.stuwork.data.service;

import java.util.List;
import java.util.Set;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stuwork.data.entity.DyWeekCheckItem;
import net.zdsoft.stuwork.data.entity.DyWeekCheckItemDay;

import org.springframework.data.jpa.repository.Query;

/**
 * dy_week_check_item_day
 */
public interface DyWeekCheckItemDayService extends BaseService<DyWeekCheckItemDay, String>{
	
	public List<DyWeekCheckItemDay> findByItemIds(String[] itemIds);

	public void deleteByItemId(String itemId);

	public void saveList(List<DyWeekCheckItemDay> itemDays);
}