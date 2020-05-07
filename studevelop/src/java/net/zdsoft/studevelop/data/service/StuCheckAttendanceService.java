package net.zdsoft.studevelop.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.studevelop.data.entity.StuCheckAttendance;

public interface StuCheckAttendanceService extends BaseService<StuCheckAttendance,String>{

	/**
	 * 班级考勤统计列表
	 * @param acadyear
	 * @param semester
	 * @param array
	 * @return
	 */
	public List<StuCheckAttendance> findListByCls(String acadyear, String semester,
			String[] array);

	/**
	 * 查看某位学生的考勤总计
	 * @param acadyear
	 * @param semester
	 * @param stuId
	 * @return
	 */
	public StuCheckAttendance findBystudentId(String acadyear, String semester,
			String stuId);
	/**
	 * 删除一批学生的考勤数据
	 * @param acadyear
	 * @param semester
	 * @param studentIds
	 * @return
	 */
	public Integer deleteByStuIds(String acadyear, String semester,String[] studentIds);

}
