package net.zdsoft.basedata.remote.service;


import net.zdsoft.basedata.entity.SchoolBuilding;

/**
 * @author yangsj  2017-1-24下午5:28:31
 */
public interface SchoolBuildingRemoteService extends BaseRemoteService<SchoolBuilding,String> {

	

	/**
	 * @param ids
	 */
	void deleteByInIds(String[] ids);

}
