package net.zdsoft.stuwork.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stuwork.data.entity.DyClassOtherCheck;

public interface DyClassOtherCheckService extends BaseService<DyClassOtherCheck,String>{

	/**
	 * 查找学年学期下某学校的班级其它考核信息
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @return
	 */
	public List<DyClassOtherCheck> findByUnitId(String unitId, String acadyear,Integer semester);

	/**
	 * 查找学年学期下某班级的班级其它考核信息
	 * @param classId
	 * @param acadyear
	 * @param semester
	 * @return
	 */
	public List<DyClassOtherCheck> findByClassId(String classId,String acadyear, Integer semester);
	/**
	 * 查找学年学期下某班级某周 的班级其它考核信息
	 * @param classId 可为空
	 * @param week
	 * @param acadyear
	 * @param semester
	 * @return
	 */
	public List<DyClassOtherCheck> findByClassIdAndWeek(String classId,int week,String acadyear, Integer semester);
}
