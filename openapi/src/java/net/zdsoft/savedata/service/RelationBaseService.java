package net.zdsoft.savedata.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.savedata.entity.RelationBase;

/**
 * @author yangsj  2018年8月2日上午10:55:35
 */
public interface RelationBaseService extends BaseService<RelationBase, String> {

	/**
	 * @param area
	 * @param baseUnitType
	 * @param relationIds
	 * @return
	 */
	List<RelationBase> findByAreaAndTypeAndRelationIdIn(String area, String type, String... relationIds);

}
