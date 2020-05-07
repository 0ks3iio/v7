package net.zdsoft.gkelective.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.gkelective.data.entity.GkRelationship;

public interface GkRelationshipService extends BaseService<GkRelationship, String>{

	/**
	 * 根据主id删除
	 * @param id
	 */
	public void deleteByPrimaryId(String primaryId,String type);
	
	public void deleteByPrimaryIds(String[] primaryId);

	public void saveAll(List<GkRelationship> insertRShop);

	public List<GkRelationship> findByTypePrimaryIdIn(String type,
			String... primaryIds);

}
