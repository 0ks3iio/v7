package net.zdsoft.syncdata.custom.zy.service;

import java.util.List;

import net.zdsoft.syncdata.custom.zy.entity.ZyCard;

public interface ZyCardService {
	
	/**
	 * 获取所有有效卡号
	 * @return
	 */
	public List<ZyCard> getAllCards();
	

}
