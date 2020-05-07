package net.zdsoft.studevelop.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.studevelop.data.entity.StuHealthRecord;

public interface StuHealthRecordService extends BaseService<StuHealthRecord,String>{
	
	/**
	 * 根据学年学期和学生id查找，学生身心健康详情
	 * @param stuId
	 * @param acadyear
	 * @param semester
	 * @return
	 */
	public StuHealthRecord getHealthRecordByStuIdSemes(String stuId, String acadyear,String semester);

}
