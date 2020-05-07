package net.zdsoft.basedata.remote.service;

import net.zdsoft.basedata.entity.TeachArea;

/**
 * @author yangsj  2018年5月22日下午2:16:10
 */
public interface TeachAreaRemoteService extends BaseRemoteService<TeachArea,String>{

	/**
	 * @param array
	 * @return
	 */
	String findByUnitIdIn(String[] uidList);

}
