package net.zdsoft.basedata.remote.service;

import net.zdsoft.basedata.entity.SchoolBuildingArea;

/**
 * @author yangsj  2017-1-24下午5:29:36
 */
public interface SchoolBuildingAreaRemoteService extends BaseRemoteService<SchoolBuildingArea,String> {

	/**
	 * @param ids
	 */
	void deleteByInIds(String[] ids);

}
