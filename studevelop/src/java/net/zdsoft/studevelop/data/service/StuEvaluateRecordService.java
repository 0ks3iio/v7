package net.zdsoft.studevelop.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.studevelop.data.entity.StuEvaluateRecord;

public interface StuEvaluateRecordService extends BaseService<StuEvaluateRecord,String>{
	
	/**
	 * 查找班级内老师评语
	 * @param acadyear
	 * @param semester
	 * @param array
	 * @return
	 */
	public List<StuEvaluateRecord> findListByCls(String acadyear, String semester,String[] array);

	/**
	 * 查询某位学生的评语详情
	 * @param stuId
	 * @param acadyear
	 * @param semester
	 * @return
	 */
	public StuEvaluateRecord findById(String stuId, String acadyear,String semester);

	public String doImport(String unitId, List<String[]> datas, String acadyear, String semester);
	
	public Integer deleteByStuIds(String acadyear, String semester,String[] studentIds);
}
