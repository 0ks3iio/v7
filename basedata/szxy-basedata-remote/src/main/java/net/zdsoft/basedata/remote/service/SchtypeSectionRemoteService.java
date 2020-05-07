package net.zdsoft.basedata.remote.service;

import net.zdsoft.basedata.entity.SchtypeSection;

public interface SchtypeSectionRemoteService extends BaseRemoteService<SchtypeSection,String>{

	/**
     * 根据学校类型取得该类型所包含的学段字符串
     * 
     * @param schtype 学校类型
     * @return 如小学、初中，就返回字符串"1,2"；如果没有符合该学校类型的学段则返回null
     */
	public String findBySchoolType(String schoolType);
	
}
