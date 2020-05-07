package net.zdsoft.eclasscard.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.eclasscard.data.entity.EccBulletinTo;


public interface EccBulletinToService extends BaseService<EccBulletinTo, String>{

	public void deleteByBulletinId(String bulletinId);
}
