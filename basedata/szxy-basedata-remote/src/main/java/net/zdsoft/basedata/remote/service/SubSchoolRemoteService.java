package net.zdsoft.basedata.remote.service;

import net.zdsoft.basedata.entity.SubSchool;

public interface SubSchoolRemoteService extends BaseRemoteService<SubSchool,String> {

	/**
	 * 数组形式entitys参数，返回list的json数据
	 * @param entitys
	 * @return
	 */
	public String saveAllEntitys(String entitys);

	/**
	 * 根据学校id来查找校区数据
	 * @param unitId
	 * @return
	 */
	public String findbySchoolIdIn(String... unitId);

}
