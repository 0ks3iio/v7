package net.zdsoft.newstusys.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newstusys.entity.StusysStudentBak;

/**
 * 
 * @author weixh
 * 2018年9月18日	
 */
public interface StusysStudentBakService extends BaseService<StusysStudentBak, String> {
	
	/**
	 * 备份学校数据
	 * @param schId
	 */
	public void saveStudentBak(String schId);

}
