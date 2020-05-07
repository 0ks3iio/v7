package net.zdsoft.studevelop.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.studevelop.data.entity.StuLeagueRecord;

public interface StuLeagueRecordService extends BaseService<StuLeagueRecord, String>{

	/**
	 * 查找社团活动登记详情
	 * @param stuId
	 * @param acadyear
	 * @param semester
	 * @return
	 */
	public List<StuLeagueRecord> getStuLeagueRecordList(String stuId,String acadyear,String semester);

	/**
	 * 通过id找任职详情
	 * @param id
	 * @return
	 */
	public StuLeagueRecord findById(String id);

	/**
	 * 查找班级社团活动情况
	 * @param acadyear
	 * @param semester
	 * @param array
	 * @return
	 */
	public List<StuLeagueRecord> findListByCls(String acadyear,
			String semester, String[] array);
	
}
