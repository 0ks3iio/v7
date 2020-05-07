package net.zdsoft.syncdata.custom.zy.dao;

import java.util.List;

import net.zdsoft.syncdata.custom.zy.entity.ZyCard;

public interface ZyCardDao {
	
	/**
	 * 获取所有卡号
	 * @return
	 */
	public List<ZyCard> getAllCards();
}
