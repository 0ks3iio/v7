package net.zdsoft.basedata.remote.service;

import net.zdsoft.basedata.entity.TeachClassEx;

public interface TeachClassExRemoteService extends BaseRemoteService<TeachClassEx,String>{
	/**
	 * 
	 * @param teachClassIds
	 * @param isMake 是否组装额外字段
	 * @return
	 */
	String findByTeachClassIdIn(String[] teachClassIds,boolean isMake);

	void deleteByTeachClass(String[] teachClassId);

	String findByTeachClassId(String teachClassId);

}
