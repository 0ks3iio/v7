package net.zdsoft.syncdata.custom.zy.service.impl;

import java.util.List;

import net.zdsoft.syncdata.custom.zy.dao.ZyCardDao;
import net.zdsoft.syncdata.custom.zy.entity.ZyCard;
import net.zdsoft.syncdata.custom.zy.service.ZyCardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("zyCardService")
public class ZyCardServiceImp implements ZyCardService {

	@Autowired
	private ZyCardDao zyCardDao;

	@Override
	public List<ZyCard> getAllCards() {
		return zyCardDao.getAllCards();
	}
}
