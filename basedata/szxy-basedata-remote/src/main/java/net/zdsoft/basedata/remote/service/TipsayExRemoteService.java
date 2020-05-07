package net.zdsoft.basedata.remote.service;

import net.zdsoft.basedata.entity.TipsayEx;

/**
 * 
 * @author niuchao
 * @since  2019年3月22日
 */
public interface TipsayExRemoteService extends BaseRemoteService<TipsayEx,String> {
	
	/**
	 * 查询教师本周调代课数(定制)
	 * 调课数（鹰硕）=调课数（万朋），代课数（鹰硕）=代课数+代管数（万朋）
	 * @param acadyear
	 * @param semester
	 * @param teacherId
	 * @return courseNum-本周课时数，takeNum-代课数，beTakeNum-被代课数，adjustNum-调课数
	 */
    String findByAcadyearAndSemesterAndTeacherId(String acadyear, Integer semester, String teacherId);
}
