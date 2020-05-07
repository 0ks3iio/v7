package net.zdsoft.basedata.service;

import java.util.Date;
import java.util.List;

import net.zdsoft.basedata.entity.Semester;

public interface SemesterService extends BaseService<Semester, String> {

    /**
     * 返回当前时间的学年学期 若当前时间不在学期时间内 则根据type返回
     * 
     * @param type
     *            0 返回空 1返回之前的一个学期 2返回之后的一个学期
     * @return
     */
    public Semester getCurrentSemester(int type);

    /**
     * 获取所有未删除学年
     * 
     * @return
     */
    public List<String> findAcadeyearList();

    /**
     * 先查找当前学校的学校学期，若当前学校学年学期不存在，则取教育局设置的学校学期
     * @param type
     *          </br>0 根据当前时间取，若当前时间不在学年学期内，则返回空 1返回之前的一个学期 2返回之后的一个学期
     * @param schoolId
     *          学校Id
     * @return
     */
    Semester findCurrentSemester(int type, String schoolId);
    /**
     * 根据学年学期获取学期信息
     * @param acadyear
     * @param semester
     * @param unitId 学校id 先取学校
     * @return
     */
	public Semester findByAcadyearAndSemester(String acadyear, int semester,
			String unitId);

	/**
	 * 获取所有教育局设置的，未删除的学期，date以及以后的学期
	 * @param date
	 * @return List<Semester>;
	 */
	public List<Semester> findListByDate(Date date);

}
