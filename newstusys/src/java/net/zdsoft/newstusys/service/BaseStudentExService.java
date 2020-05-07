package net.zdsoft.newstusys.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newstusys.entity.BaseStudentEx;

/**
 * 
 * @author weixh
 * @since 2018年3月2日 下午5:42:01
 */
public interface BaseStudentExService extends BaseService<BaseStudentEx, String> {
	public void deleteByStuIds(String... ids);
}
